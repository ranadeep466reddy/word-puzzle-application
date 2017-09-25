// package hashing;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * 
 */
public class WordPuzzle {

    public static void main(String[] args) {
    	
    	Scanner input = new Scanner(System.in);
        System.out.print("Enter number of row: ");
        int row = input.nextInt();
        System.out.print("Enter number of column: ");
        int col = input.nextInt();

        String dictionaryFile = "dictionary.txt";

        ArrayList<String> dictionary = readDictionary(dictionaryFile);

        char[][] table = new char[row][col];
        generateTableContent(table);

        System.out.println("The table \n");
        
        for (char[] cs : table) {
            for (int j = 0; j < cs.length; j++) {
                char c = cs[j];
                System.out.print(c + " ");
            }
            System.out.println("");
        }
        
        System.out.println("");
        Map<String, String> map = generateAllString(table);

        findInDictionary(map, dictionary);

    }

    /**
     * process th work and compare all result 
     * @param map
     * @param dictionary 
     */
    private static void findInDictionary(
            Map<String, String> map, ArrayList<String> dictionary) {
        usingHashTable(map, dictionary);
        System.out.println("----------------------------\n");
        usingLinkedList(map, dictionary);
        System.out.println("----------------------------\n");
        usingAVLTree(map, dictionary);
        System.out.println("----------------------------\n");
    }

    /**
     * using avl tree 
     * @param map
     * @param dictionary 
     */
    private static void usingAVLTree(
            Map<String, String> map, ArrayList<String> dictionary) {
        AVL tree = new AVL();

        System.out.println("Using AVL tree ");
        long start = System.nanoTime();
        for (String string : dictionary) {
            tree.insert(string);
        }
        long stop = System.nanoTime();
        System.out.printf("Time for inserting: %d%n", stop - start);

        ArrayList<String> found = new ArrayList<>();

        start = System.nanoTime();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (tree.contains(key)) {
                found.add(key);
            }
        }

        stop = System.nanoTime();

        System.out.printf("Running time: %d%n", stop - start);
        if (found.isEmpty()) {
            System.out.println("No word has been found");
        } else {
            System.out.print("Found: ");
            printResult(found);
            System.out.println("");
        }

    }

    /**
     * using linked list
     * @param map
     * @param dictionary 
     */
    private static void usingLinkedList(
            Map<String, String> map, ArrayList<String> dictionary) {
        LinkedListDictionary lld = new LinkedListDictionary();

        System.out.println("Using linked list ");
        long start = System.nanoTime();
        for (String string : dictionary) {
            lld.insert(string);
        }
        long stop = System.nanoTime();

        System.out.printf("Time for inserting: %d%n", stop - start);

        ArrayList<String> found = new ArrayList<>();

        start = System.nanoTime();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (lld.contains(key)) {
                found.add(key);
            }
        }

        stop = System.nanoTime();

        System.out.printf("Running time: %d%n", stop - start);
        if (found.isEmpty()) {
            System.out.println("No word has been found");
        } else {
            System.out.print("Found: ");
            printResult(found);
            System.out.println("");
        }

    }

    /***
     * using hash table 
     * @param map
     * @param dictionary 
     */
    private static void usingHashTable(
            Map<String, String> map, ArrayList<String> dictionary) {
        int size = (int) Math.sqrt(dictionary.size());
        MyHashTable hashTable = new MyHashTable(size);

        System.out.println("Using hashtable ");
        long start = System.nanoTime();
        for (String string : dictionary) {
            hashTable.insert(string);
        }
        long stop = System.nanoTime();

        System.out.printf("Time for inserting: %d%n", stop - start);

        ArrayList<String> found = new ArrayList<>();

        start = System.nanoTime();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (hashTable.contains(key)) {
                found.add(key);
            }
        }

        stop = System.nanoTime();

        System.out.printf("Running time: %d%n", stop - start);
        if (found.isEmpty()) {
            System.out.println("No word has been found");
        } else {
            System.out.print("Found: ");
            printResult(found);
            System.out.println("");
        }

    }
    
    private static void printResult(ArrayList<String> found){
        String get;
        for (int i = 0; i < found.size(); i++) {
            get = found.get(i);
            if (i % 10 == 0) {
                System.out.println("");
            }
            System.out.print(get.toUpperCase() + " ");
        }
    }
    
    /**
     * read all words in the dictionary 
     * @param fileName
     * @return 
     */
    private static ArrayList<String> readDictionary(String fileName) {
        ArrayList<String> list = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(fileName));
            String next;
            while (scanner.hasNext()) {
                next = scanner.next();
                list.add(next);
            }
        } catch (Exception e) {
        }

        return list;
    }

    /**
     * generate all unique string that could be created from the table
     * @param table
     * @return 
     */
    private static Map<String, String> generateAllString(char[][] table) {
        Map<String, String> map = new TreeMap<>();
        ArrayList<String> list = generateBigString(table);
        Map<String, String> bigString = cleanDuplicatedString(list);

        Set<String> keys = bigString.keySet();

        for (String key : keys) {
            Map<String, String> tmp = generateSubstring(key);
            map.putAll(tmp);
        }

        return map;
    }

    /**
     * generate all sub string from a string
     * such as the length that sub string is longer than 1
     * @param word
     * @return 
     */
    private static Map<String, String> generateSubstring(String word) {
        Map<String, String> map = new TreeMap<>();
        String s;
        for (int from = 0; from < word.length(); from++) {
            for (int to = from + 1; to <= word.length(); to++) {
                s = word.substring(from, to);
                if (s.length() > 1) {
                    map.put(s, s);
                    s = new StringBuffer(s).reverse().toString();
                    map.put(s, s);
                }
            }
        }
        return map;
    }

    /**
     * clear all duplicated string
     * @param list
     * @return 
     */
    private static Map<String, String> 
        cleanDuplicatedString(ArrayList<String> list) {

        Map<String, String> map = new TreeMap<>();

        for (String string : list) {
            map.put(string.toLowerCase(), string.toLowerCase());
        }

        return map;
    }

    /**
     * get all string that are on rows, columns and diagonals 
     * @param table
     * @return 
     */
    private static ArrayList<String> generateBigString(char[][] table) {
        ArrayList<String> list = new ArrayList<>();

        list.addAll(generateBigStringOnRow(table));
        list.addAll(generateBigStringOnCol(table));
        list.addAll(generateBigStringOnLeftDiagonal(table));
        list.addAll(generateBigStringOnRightDiagonal(table));

        return list;
    }

    /**
     * get all right diagonal string
     * @param table
     * @return 
     */
    private static ArrayList<String>
            generateBigStringOnRightDiagonal(char[][] table) {
        ArrayList<String> list = new ArrayList<>();

        list.addAll(generateBigStringOnRightDiagonalLowerHalf(table));
        list.addAll(generateBigStringOnRightDiagonalUpperHalf(table));
        int col = table[0].length - 1;
        int row = 0;
        list.add(rightDiagonal(table, row, col));

        return list;
    }

    /**
     * get all string on lower part of right diagonal
     *
     * @param table
     * @return
     */
    private static ArrayList<String>
            generateBigStringOnRightDiagonalLowerHalf(char[][] table) {
        ArrayList<String> list = new ArrayList<>();
        int col = table[0].length - 1;
        for (int i = 1; i < table.length - 1; i++) {
            list.add(rightDiagonal(table, i, col));
        }
        return list;
    }

    /**
     * get all string on upper part of right diagonal
     *
     * @param table
     * @return
     */
    private static ArrayList<String>
            generateBigStringOnRightDiagonalUpperHalf(char[][] table) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < table[0].length - 1; i++) {
            list.add(rightDiagonal(table, 0, i));
        }
        return list;
    }

    /**
     * get string start from row and col on right diagonal
     *
     * @param table
     * @param row
     * @param col
     * @return
     */
    private static String rightDiagonal(char[][] table, int row, int col) {
        String s = "";
        while (row < table.length
                && col >= 0) {
            s += table[row][col];
            row++;
            col--;
        }
        return s;
    }

    /**
     * get all left diagonal string
     *
     * @param table
     * @return
     */
    private static ArrayList<String>
            generateBigStringOnLeftDiagonal(char[][] table) {
        ArrayList<String> list = new ArrayList<>();

        list.addAll(generateBigStringOnLeftDiagonalLowerHalf(table));
        list.addAll(generateBigStringOnLeftDiagonalUpperHalf(table));
        list.add(leftDiagonal(table, 0, 0));

        return list;
    }

    /**
     * get all string on upper part of left diagonal
     *
     * @param table
     * @return
     */
    private static ArrayList<String>
            generateBigStringOnLeftDiagonalUpperHalf(char[][] table) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 1; i < table[0].length - 1; i++) {
            list.add(leftDiagonal(table, 0, i));
        }

        return list;
    }

    /**
     * get all string on lower part of left diagonal
     *
     * @param table
     * @return
     */
    private static ArrayList<String>
            generateBigStringOnLeftDiagonalLowerHalf(char[][] table) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 1; i < table.length - 1; i++) {
            list.add(leftDiagonal(table, i, 0));
        }

        return list;
    }

    /**
     * get string start from row and col on left diagonal
     *
     * @param table
     * @param row
     * @param col
     * @return
     */
    private static String leftDiagonal(char[][] table, int row, int col) {
        String s = "";
        while (row < table.length
                && col < table[0].length) {
            s += table[row][col];
            row++;
            col++;
        }
        return s;
    }

    /**
     * get all strings on columns
     *
     * @param table
     * @return
     */
    private static ArrayList<String> generateBigStringOnCol(char[][] table) {
        ArrayList<String> list = new ArrayList<>();
        String s;

        for (int i = 0; i < table[0].length; i++) {
            s = "";
            for (int j = 0; j < table.length; j++) {
                s += table[j][i];
            }
            list.add(s);
        }

        return list;
    }

    /**
     * get all string on row
     *
     * @param table
     * @return
     */
    private static ArrayList<String> generateBigStringOnRow(char[][] table) {
        ArrayList<String> list = new ArrayList<>();
        String s;
        for (char[] cs : table) {
            s = "";
            for (int j = 0; j < cs.length; j++) {
                s += cs[j];
            }
            list.add(s);
        }
        return list;
    }

    /**
     * generate a random table of character
     *
     * @param table
     */
    private static void generateTableContent(char[][] table) {
        for (char[] cs : table) {
            for (int j = 0; j < cs.length; j++) {
                cs[j] = (char) (Math.random() * 26 + 'A');
            }
        }


    	
   	}

}

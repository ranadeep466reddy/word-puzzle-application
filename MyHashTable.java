// package hashing;


/**
 *
 * 
 */
public class MyHashTable {

    // using separated chaining 
    private LinkedListDictionary[] table;
    private int size;

    /**
     * constructor
     *
     * @param size
     */
    public MyHashTable(int size) {

        // bad case 
        if (size <= 0) {
            throw new IllegalArgumentException("Invalid size of the hashtable");
        }
        
        table = new LinkedListDictionary[size];
        this.size = size;
        // init all cells 
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedListDictionary();
        }
    }
    
    /**
     * insert into the table
     * @param key 
     */
    public void insert(String key) {
        // get hash code
        int hash = Math.abs(key.hashCode());
        // get cell
        int position = hash % size;
        // insert into cell
        table[position].insert(key);
    }

    /**
     * 
     * @param key
     * @return true or false 
     */
    public boolean contains(String key) {
        // has the string
        int hash = Math.abs(key.hashCode());
        // guess the position of the cell
        int position = hash % size;
        // find it
        return table[position].contains(key);
    }
}

/**
 * ***************************************************************************
 */
class LinkedListDictionary {
    
    // head of the list
    private ListNode head;
    
    /**
     * constructor 
     */
    public LinkedListDictionary() {
        head = null;
    }
    
    /**
     * 
     * @return true if the list is empty
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * 
     * @param key
     * @return true if list contain this key
     */
    public boolean contains(String key) {

        ListNode curr = head;
        int comparison;
        // iterate from the start the the end of list
        while (curr != null) {
            comparison = key.compareTo(curr.key);
            if (comparison == 0) { // matching
                return true;
            }        
            curr = curr.next;
        }

        return false;
    }

    /**
     * insert key to the list in ascending  order 
     * @param key 
     */
    public void insert(String key) {
        if (head == null) { // basic case 
            head = new ListNode(key);
        } else {
            ListNode curr = head;
            int comparison;

            comparison = key.compareTo(curr.key);
            if (comparison < 0) { // add to head
                ListNode tmp = new ListNode(key);
                tmp.next = head;
                head.prev = tmp;
                head = tmp;
            } else if (comparison > 0) { // move to the right position 
                while (curr.next != null) {
                    comparison = key.compareTo(curr.key);
                    if (comparison == 0) { // duplicated case
                        return;
                    } else if (comparison > 0) { // get to the right position 
                        break;
                    }
                    curr = curr.next; // to the next node
                }
                if (curr.next == null) { // add to the end of list
                    curr.next = new ListNode(key, null, curr);
                } else { // add to the middle of the list
                    ListNode tmp = new ListNode(key, curr.next, curr);
                    curr.next.prev = tmp;
                    curr.next = tmp;
                }
            } // end else if
        } // end else 
    } // end insert method

}

class ListNode {

    String key;
    ListNode next;
    ListNode prev;
    
    /**
     * constructor
     * @param key 
     */
    public ListNode(String key) {
        this(key, null, null);
    }

    /**
     * constructor
     * @param key
     * @param next
     * @param prev 
     */
    public ListNode(String key, ListNode next, ListNode prev) {
        this.key = key;
        this.next = next;
        this.prev = prev;
    }

}

/**
 * ***************************************************************************
 */
class AVL {

    private Node root;

    /**
     * constructor 
     */
    public AVL() {
        root = null;
    }

    /**
     * 
     * @param key
     * @return true if the tree contains the key
     */
    public boolean contains(String key) {
        return contains(root, key);
    }

    /**
     * recursion function 
     * @param n
     * @param key
     * @return true if the sub tree contains the key
     */
    private boolean contains(Node n, String key) {
        if (n == null) {
            return false;
        }
        int comparison = n.key.compareTo(key);
        if (comparison == 0) {
            return true;
        }
        if (comparison < 0) {
            return contains(n.right, key);
        }
        return contains(n.left, key);
    }

    /**
     * insert key to tree
     *
     * @param key
     */
    public void insert(String key) {

        Node q = new Node(key);
        insert(root, q);

    }

    /**
     * insert key below node p
     *
     * @param p
     * @param q
     */
    public void insert(Node p, Node q) {

        if (p == null) {
            root = q;
        } else {

            if (q.key.compareTo(p.key) < 0) { // go left
                if (p.left == null) {
                    p.left = q;
                    q.parent = p;
                    //    System.out.println("Insert " + q.key);
                    recursiveBalance(p);

                } else {
                    insert(p.left, q);
                }
            } else if (q.key.compareTo(p.key) > 0) {// go right
                if (p.right == null) {
                    p.right = q;
                    q.parent = p;
                    //    System.out.println("Insert " + q.key);
                    recursiveBalance(p);

                } else {
                    insert(p.right, q);
                }
            } else {
                // duplicated value
            }
        }
    }

    /**
     * go up to the root make tree balance
     *
     * @param n
     */
    private void recursiveBalance(Node n) {

        int balance = getBalance(n);
        //int balance = n.balance;
        if (balance == -2) {
            if (height(n.left.left) >= height(n.left.right)) {
                //   System.out.println("\trotateRight");
                n = rotateRight(n);
            } else {
                //    System.out.println("\trotateLeftRight");
                n = rotateLeftRight(n);
            }
        } else if (balance == 2) {
            if (height(n.right.right) >= height(n.right.left)) {
                //     System.out.println("\trotateLeft");
                n = rotateLeft(n);
            } else {
                //    System.out.println("\trotateRightLeft");
                n = rotateRightLeft(n);
            }
        }

        //move back to root
        if (n.parent != null) {
            recursiveBalance(n.parent);
        } else {
            root = n;
        }

    }

    private Node rotateLeft(Node n) {
        Node k = n.right;

        k.parent = n.parent;
        n.right = k.left;

        if (n.right != null) {
            n.right.parent = n;
        }

        k.left = n;
        n.parent = k;

        if (k.parent != null) {
            if (k.parent.right == n) {
                k.parent.right = k;
            } else if (k.parent.left == n) {
                k.parent.left = k;
            }
        }

        return k;
    }

    private Node rotateRight(Node n) {
        Node k = n.left;

        k.parent = n.parent;
        n.left = k.right;

        if (n.left != null) {
            n.left.parent = n;
        }

        k.right = n;
        n.parent = k;

        if (k.parent != null) {
            if (k.parent.right == n) {
                k.parent.right = k;
            } else if (k.parent.left == n) {
                k.parent.left = k;
            }
        }

        return k;
    }

    private Node rotateLeftRight(Node n) {
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }

    private Node rotateRightLeft(Node n) {
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }

    /**
     * get balance
     *
     * @param n
     * @return
     */
    private int getBalance(Node n) {
        return height(n.right) - height(n.left);
    }

    /**
     * get height of node
     *
     * @param n
     * @return
     */
    private int height(Node n) {
        if (n == null) {
            return -1;
        }
        if (n.left == null
                && n.right == null) {
            n.height = 0;
            return 0;
        } else if (n.left == null) {
            n.height = 1 + height(n.right);
            return n.height;
            // return 1 + height(n.right);
        } else if (n.right == null) {
            n.height = 1 + height(n.left);
            return n.height;
            //  return 1 + height(n.left);
        } else {
            n.height = 1 + Math.max(height(n.left), height(n.right));
            return n.height;
            //   return maxs(height(n.left), height(n.right));
        }
    }

    private int maxs(int a, int b) {
        return a > b ? a : b;
    }

}

class Node {

    String key;
    int balance;
    int height;
    Node left;
    Node right;
    Node parent;

    /**
     * constructor
     *
     * @param key
     */
    public Node(String key) {
        this.key = key;
        this.balance = 0;
        this.height = 0;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    @Override
    public String toString() {
        return key + "\t" + height;
    }

}


/**
 * Created by kfwong on 2/2/17.
 */
// Copy paste this Java Template and save it as "PatientNames.java"

import java.util.*;
import java.io.*;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here: Google, StackOverflow
// year 2017 hash code: DZAjKugdE9QiOQKGFbut (do NOT delete this line)

/*

 */

class PatientNames {
    // if needed, declare a private data structure here that
    // is accessible to all methods in this class

    // --------------------------------------------

    BinarySearchTree<String> patients;

    // --------------------------------------------

    public PatientNames() {
        // Write necessary code during construction;
        //
        // write your answer here

        // --------------------------------------------

        patients = new BinarySearchTree<>();

        // --------------------------------------------
    }

    void AddPatient(String patientName, int gender) {
        // You have to insert the information (patientName, gender)
        // into your chosen data structure
        //
        // write your answer here

        // --------------------------------------------

        patients.insert(patientName);

        // --------------------------------------------
    }

    void RemovePatient(String patientName) {
        // You have to remove the patientName from your chosen data structure
        //
        // write your answer here

        // --------------------------------------------


        // --------------------------------------------
    }

    int Query(String START, String END, int gender) {
        int ans = 0;

        // You have to answer how many patient name starts
        // with prefix that is inside query interval [START..END)
        //
        // write your answer here

        // --------------------------------------------

        String r = patients.q(START);
        System.out.println(r);
        System.out.println(patients.rank(r));

        // --------------------------------------------

        return ans;
    }

    void run() throws Exception {
        // do not alter this method to avoid unnecessary errors with the automated judging
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        while (true) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());
            if (command == 0) { // end of input
                break;
            } else if (command == 1) { // AddPatient
                AddPatient(st.nextToken(), Integer.parseInt(st.nextToken()));
                Printer.print(patients.root);
            } else if (command == 2) { // RemovePatient
                RemovePatient(st.nextToken());
            } else { // if (command == 3) // Query
                pr.println(Query(st.nextToken(), // START
                        st.nextToken(), // END
                        Integer.parseInt(st.nextToken()))); // GENDER
            }
        }
        pr.close();
    }

    public static void main(String[] args) throws Exception {
        // do not alter this method to avoid unnecessary errors with the automated judging
        PatientNames ps2 = new PatientNames();
        ps2.run();
    }
}

class BinarySearchTree<E extends Comparable<E>> {
    protected ListNode<E> root;

    public BinarySearchTree() {
        this.root = null;
    }

    public void insert(E data) {
        root = insert(root, data);
    }

    public E q(E data) {
        return findGreaterOrEquals(root, null, data).getData();
    }

    public int rank(E data) {
        return rank(root, 0, data);
    }

    protected ListNode<E> insert(ListNode<E> currNode, E data) {
        if (currNode == null) {
            return new ListNode<>(data);
        }

        int comparison = data.compareTo(currNode.getData());

        if (comparison <= 0) {
            ListNode<E> insertion = insert(currNode.getLeft(), data);
            currNode.setLeft(insertion);
            insertion.setParent(currNode);
        } else if (comparison > 0) {
            ListNode<E> insertion = insert(currNode.getRight(), data);
            currNode.setRight(insertion);
            insertion.setParent(currNode);
        }

        updateHeight(currNode);

        updateSize(currNode);

        currNode = rebalance(currNode);

        return currNode;
    }

    protected ListNode<E> remove(ListNode<E> currNode, E data) {
        if (currNode == null) {
            throw new NoSuchElementException();
        }

        int comparison = data.compareTo(currNode.getData());

        if (comparison < 0) {
            ListNode<E> leftSubtree = remove(currNode.getLeft(), data);
            leftSubtree.setParent(currNode);
            currNode.setLeft(leftSubtree);

            return currNode;
        } else if (comparison > 0) {
            ListNode<E> rightSubtree = remove(currNode.getRight(), data);
            rightSubtree.setParent(currNode);
            currNode.setRight(rightSubtree);

            return currNode;
        } else if (comparison == 0) {
            if (currNode.hasBoth()) { // wtf case, has two child

                // http://www.algolist.net/Data_structures/Binary_search_tree/Removal

                // 1. find target node
                // 2. find target node's successor
                // 3. change the target node's value (not reference) into its successor's value
                // 4. remove successor's node

                ListNode<E> successor = successor(currNode);
                currNode.setData(successor.getData());

                ListNode<E> rightSubtree = remove(currNode.getRight(), successor.getData());
                rightSubtree.setParent(currNode);
                currNode.setRight(rightSubtree);

            } else if (!currNode.hasLeft() && !currNode.hasRight()) { // has no child
                currNode = null;
            } else { // current node has one child
                if (currNode == currNode.getParent().getLeft()) { // current node is at the left of parent node
                    if (currNode.hasLeft()) {
                        ListNode<E> left = currNode.getLeft();

                        left.setParent(currNode.getParent());

                        currNode.setLeft(null);
                        currNode = left;
                    } else if (currNode.hasRight()) {
                        ListNode<E> right = currNode.getRight();

                        right.setParent(currNode.getParent());

                        currNode.setRight(null);
                        currNode = right;
                    }
                } else if (currNode == currNode.getParent().getRight()) { // current node is at the right of parent node
                    if (currNode.hasLeft()) {
                        ListNode<E> left = currNode.getLeft();

                        left.setParent(currNode.getParent());

                        currNode.setLeft(null);
                        currNode = left;

                    } else if (currNode.hasRight()) {
                        ListNode<E> right = currNode.getRight();

                        currNode.getRight().setParent(currNode.getParent());

                        currNode.setRight(null);
                        currNode = right;
                    }
                }
            }

            updateHeight(currNode);
            updateSize(currNode);

            currNode = rebalance(currNode);

            return currNode;
        }

        return currNode;
    }

    // precond: data must exist
    private int rank(ListNode<E> currNode, int currResult, E data) {

        if (data.compareTo(currNode.getData()) < 0) {
            // data is less than current node, recurse left
            return rank(currNode.getLeft(), currResult, data);
        } else if (data.compareTo(currNode.getData()) > 0) {
            // data is greater than current node, recurse right

            if (currNode.hasLeft()) {
                currResult += currNode.getLeft().getSize();
            }

            currResult += 1;

            return rank(currNode.getRight(), currResult, data);
        } else {
            // data is equal to current node, return result

            if(currNode.hasLeft() && currNode.hasRight()){
                currResult += currNode.getLeft().getSize();
            }

            return currResult;
        }
    }

    private ListNode<E> findGreaterOrEquals(ListNode<E> currNode, ListNode<E> currResult, E data) {
        if (currNode == null) {
            // reached the bottom, return result so far
            return currResult;
        }

        if (data.compareTo(currNode.getData()) <= 0) {
            // data is less than or equal to the current node
            // set the result so far to current node
            currResult = currNode;
        }

        if (data.compareTo(currNode.getData()) < 0) {
            // data is less than current node, recurse left
            return findGreaterOrEquals(currNode.getLeft(), currResult, data);
        } else if (data.compareTo(currNode.getData()) > 0) {
            // data is greater than current node, recurse right
            return findGreaterOrEquals(currNode.getRight(), currResult, data);
        } else {
            // data is equal to current node, return result
            return currResult;
        }
    }

    protected ListNode<E> rebalance(ListNode<E> currNode) {
        // https://www.cise.ufl.edu/~nemo/cop3530/AVL-Tree-Rotations.pdf
        // check page 5 for pseudocode. More human readable than the lecture notes.

        // less efficient due to more comparison operation
        // but more readable
        if (isLeftLeftCase(currNode)) {
            currNode = rotateRight(currNode);

        } else if (isLeftRightCase(currNode)) {
            currNode.setLeft(rotateLeft(currNode.getLeft()));
            currNode = rotateRight(currNode);

        } else if (isRightRightCase(currNode)) {
            currNode = rotateLeft(currNode);

        } else if (isRightLeftCase(currNode)) {
            currNode.setRight(rotateRight(currNode.getRight()));
            currNode = rotateLeft(currNode);

        }

        return currNode;
    }

    protected boolean isLeftLeftCase(ListNode<E> currNode) {
        int currNodeBF = balanceFactor(currNode);
        int leftNodeBF = balanceFactor(currNode.getLeft());

        return currNodeBF == 2 && // left heavy
                (leftNodeBF >= 0 && leftNodeBF <= 1); // left heavy
    }

    protected boolean isLeftRightCase(ListNode<E> currNode) {
        int currNodeBF = balanceFactor(currNode);
        int leftNodeBF = balanceFactor(currNode.getLeft());

        return currNodeBF == 2 && // left heavy
                (leftNodeBF == -1); // right heavy
    }

    private boolean isRightRightCase(ListNode<E> currNode) {
        int currNodeBF = balanceFactor(currNode);
        int rightNodeBF = balanceFactor(currNode.getRight());

        return currNodeBF == -2 && // right heavy
                (rightNodeBF >= -1 && rightNodeBF <= 0); // right heavy
    }

    private boolean isRightLeftCase(ListNode<E> currNode) {
        int currNodeBF = balanceFactor(currNode);
        int rightNodeBF = balanceFactor(currNode.getRight());

        return currNodeBF == -2 && // right heavy
                (rightNodeBF == 1); // left heavy
    }

    protected ListNode<E> rotateLeft(ListNode<E> currNode) {
        if (currNode == null) {
            // empty tree
            throw new NoSuchElementException("You gave me nothing to spin >:(");
        }

        ListNode<E> rightSubtree = null;

        if (currNode.hasRight()) {
            rightSubtree = currNode.getRight();

            rightSubtree.setParent(currNode.getParent());
            currNode.setParent(rightSubtree);
            currNode.setRight(rightSubtree.getLeft());

            if (rightSubtree.hasLeft()) {
                rightSubtree.getLeft().setParent(currNode);
            }

            rightSubtree.setLeft(currNode);

            updateHeight(rightSubtree);
            updateHeight(currNode);

            updateSize(rightSubtree);
            updateSize(currNode);

        } else {
            // wtf?
            assert false : "If you spin it without right child the subtree will be BROKEN!";
        }

        return rightSubtree;
    }

    protected ListNode<E> rotateRight(ListNode<E> currNode) {
        if (currNode == null) {
            // empty tree
            throw new NoSuchElementException("You gave me nothing to spin >:(");
        }

        ListNode<E> leftSubtree = null;

        if (currNode.hasLeft()) {
            leftSubtree = currNode.getLeft();

            leftSubtree.setParent(currNode.getParent());
            currNode.setParent(leftSubtree);
            currNode.setLeft(leftSubtree.getRight());

            if (leftSubtree.hasRight()) {
                leftSubtree.getRight().setParent(currNode);
            }

            leftSubtree.setRight(currNode);

            updateHeight(currNode);
            updateHeight(leftSubtree);

            updateSize(currNode);
            updateSize(leftSubtree);

        } else {
            // wtf?
            assert false : "If you spin it without left child the subtree will be BROKEN!";
        }

        return leftSubtree;
    }

    protected ListNode<E> successor(ListNode<E> currNode) {
        /*
            Finding the successor of a given node. Two cases:
            1. The node has a right subtree.
                -> then next larger key must be in the right subtree.
                -> find the smallest of right subtree. [FOUND]

            2. The node does not have a right subtree.
                -> Need to bubble up the ancestors.
                2.1. The current node is the left of its parent.
                    -> then the parent is the successor. [FOUND]
                2.2. The current node is the right of its parent. (wtf case)
                    -> keep bubble up the ancestors.
                    -> the successor is the first encounter on the right. like "<". (now it's case 2.1) [FOUND]
         */

        // following code implemented according to the visualgo pseudocode
        if (currNode.hasRight()) {
            return minimum(currNode.getRight());
        } else {
            ListNode<E> parent = currNode.getParent();

            while (parent != null && currNode == parent.getRight()) {
                currNode = parent;
                parent = currNode.getParent();
            }

            if (parent == null) {
                throw new NoSuchElementException("Sad little one has no sucessor :'(");
            } else {
                return parent;
            }
        }

    }

    protected ListNode<E> minimum(ListNode<E> currNode) {
        if (currNode == null) {
            // empty tree
            throw new NoSuchElementException("There's NOTHING in an empty tree. Go home you're drunk.");
        }

        if (currNode.hasLeft()) {
            return minimum(currNode.getLeft());
        } else {
            return currNode;
        }
    }

    protected int balanceFactor(ListNode<E> currNode) {
        if (currNode == null) {
            return 0;
        }

        int leftHeight = currNode.hasLeft() ? currNode.getLeft().getHeight() : -1;
        int rightHeight = currNode.hasRight() ? currNode.getRight().getHeight() : -1;

        return leftHeight - rightHeight;
    }

    protected int updateHeight(ListNode<E> currNode) {
        if (currNode == null) {
            return -1;
        } else {
            currNode.setHeight(Math.max(updateHeight(currNode.getLeft()), updateHeight(currNode.getRight())) + 1);

            return currNode.getHeight();
        }
    }

    protected int updateSize(ListNode<E> currNode) {
        // http://www.geeksforgeeks.org/write-a-c-program-to-calculate-size-of-a-tree/
        if (currNode == null) {
            return 0;
        } else {
            currNode.setSize(updateSize(currNode.getLeft()) + updateSize(currNode.getRight()) + 1);

            return currNode.getSize();
        }
    }
}

class ListNode<E extends Comparable<E>> {
    private E data;
    private int height;
    private int size;
    private ListNode<E> parent;
    private ListNode<E> left;
    private ListNode<E> right;

    public ListNode(E data) {
        this.data = data;
        this.parent = null;
        this.left = null;
        this.right = null;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ListNode<E> getParent() {
        return parent;
    }

    public void setParent(ListNode<E> parent) {
        this.parent = parent;
    }

    public ListNode<E> getLeft() {
        return left;
    }

    public void setLeft(ListNode<E> listNode) {
        this.left = listNode;
    }

    public ListNode<E> getRight() {
        return right;
    }

    public void setRight(ListNode<E> listNode) {
        this.right = listNode;
    }

    public boolean hasLeft() {
        return this.left != null;
    }

    public boolean hasRight() {
        return this.right != null;
    }

    public boolean hasBoth() {
        return hasLeft() && hasRight();
    }

}

/////////////

class Printer {
    /**
     * Print a tree
     *
     * @param root tree root node
     */
    public static void print(ListNode root) {
        List<List<String>> lines = new ArrayList<List<String>>();

        List<ListNode> level = new ArrayList<ListNode>();
        List<ListNode> next = new ArrayList<ListNode>();

        level.add(root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<String>();

            nn = 0;

            for (ListNode n : level) {
                if (n == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                } else {
                    String aa = n.getData().toString() + " [H:" + n.getHeight() + ", S:" + n.getSize() + "]";
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.getLeft());
                    next.add(n.getRight());

                    if (n.getLeft() != null) nn++;
                    if (n.getRight() != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;

            lines.add(line);

            List<ListNode> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {

                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            // print line of numbers
            for (int j = 0; j < line.size(); j++) {

                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                // a number
                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            perpiece /= 2;
        }
    }
}

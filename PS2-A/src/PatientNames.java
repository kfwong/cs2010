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

class PatientNames {
    // if needed, declare a private data structure here that
    // is accessible to all methods in this class

    // --------------------------------------------

    private PatientDirectory patientDirectory;

    // --------------------------------------------

    public PatientNames() {
        // Write necessary code during construction;
        //
        // write your answer here

        // --------------------------------------------

        this.patientDirectory = new PatientDirectory();

        // --------------------------------------------
    }

    void AddPatient(String patientName, int gender) {
        // You have to insert the information (patientName, gender)
        // into your chosen data structure
        //
        // write your answer here

        // --------------------------------------------

        Patient patient = new Patient(patientName, gender);
        patientDirectory.insert(patient);


        // --------------------------------------------
    }

    void RemovePatient(String patientName) {
        // You have to remove the patientName from your chosen data structure
        //
        // write your answer here

        // --------------------------------------------

        patientDirectory.remove(new Patient(patientName, -1));

        // --------------------------------------------
    }

    int Query(String START, String END, int gender) {
        int ans;

        // You have to answer how many patient name starts
        // with prefix that is inside query interval [START..END)
        //
        // write your answer here

        // --------------------------------------------

        ans = patientDirectory.rangeSearch(START, END, gender);

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
            if (command == 0) // end of input
                break;
            else if (command == 1) // AddPatient
                AddPatient(st.nextToken(), Integer.parseInt(st.nextToken()));
            else if (command == 2) // RemovePatient
                RemovePatient(st.nextToken());
            else // if (command == 3) // Query
                pr.println(Query(st.nextToken(), // START
                        st.nextToken(), // END
                        Integer.parseInt(st.nextToken()))); // GENDER
        }
        pr.close();
    }

    public static void main(String[] args) throws Exception {
        // do not alter this method to avoid unnecessary errors with the automated judging
        PatientNames ps2 = new PatientNames();
        ps2.run();
    }
}

class PatientDirectory extends BinarySearchTree<Patient> {

    public int rangeSearch(String start, String end, int gender) {
        return rangeSearch(root, start, end, gender);
    }

    private int rangeSearch(ListNode<Patient> currNode, String start, String end, int gender) {
        /*
        Modified in-order traversal

        1) If value of root’s key is greater than k1, then recursively call in left subtree.
        2) If value of root’s key is in range, then print the root’s key.
        3) If value of root’s key is smaller than k2, then recursively call in right subtree.
         */

        if (currNode == null) {
            return 0;
        }

        int total = 0;

        if (currNode.getData().getName().compareTo(start) >= 0) {
            //System.out.println(currNode.getData().getName());
            total += rangeSearch(currNode.getLeft(), start, end, gender);
        }

        if (currNode.getData().getName().compareTo(start) >= 0 && currNode.getData().getName().compareTo(end) < 0) {
            if (gender == 0 || gender == currNode.getData().getGender()) {
                //System.out.println(currNode.getData().getName());
                total++;
            }
        }

        if (currNode.getData().getName().compareTo(end) < 0) {
            //System.out.println(currNode.getData().getName());
            total += rangeSearch(currNode.getRight(), start, end, gender);
        }

        return total;
    }
}

class Patient implements Comparable<Patient> {
    private String name;
    private int gender;

    public Patient(String name, int gender) {
        this.name = name;
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }

    public String getName() {

        return name;
    }

    @Override
    public int compareTo(Patient o) {
        return this.name.compareTo(o.getName());
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

    public void remove(E data) {
        remove(root, data);
    }

    public int height() {
        return height(root);
    }

    public E minimum() {
        return minimum(root).getData();
    }

    public void printInOrder() {
        printInOrder(root);
        System.out.println();
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
        }

        if (comparison > 0) {
            ListNode<E> insertion = insert(currNode.getRight(), data);
            currNode.setRight(insertion);
            insertion.setParent(currNode);
        }

        // update height of nodes after insertion
        // this will bubble up from inserted node
        currNode.setHeight(currNode.getHeight() + 1);

        return currNode;
    }

    protected void remove(ListNode<E> currNode, E data) {
        if (currNode == null) {
            throw new NoSuchElementException();
        }

        int comparison = data.compareTo(currNode.getData());

        if (comparison < 0) {
            remove(currNode.getLeft(), data);
        }

        if (comparison > 0) {
            remove(currNode.getRight(), data);
        }

        if (comparison == 0) {
            if (currNode.hasBoth()) { // wtf case, has two child

                // http://www.algolist.net/Data_structures/Binary_search_tree/Removal

                // 1. find target node
                // 2. find target node's successor
                // 3. change the target node's value (not reference) into its successor's value
                // 4. remove successor's node

                ListNode<E> successor = successor(currNode);
                currNode.setData(successor.getData());
                remove(currNode.getRight(), successor.getData());

            } else if (!currNode.hasLeft() && !currNode.hasRight()) { // has no child
                if (currNode == root) {
                    root = null;
                } else if (currNode.getParent().getLeft() == currNode) {
                    currNode.getParent().setLeft(null);
                } else if (currNode.getParent().getRight() == currNode) {
                    currNode.getParent().setRight(null);
                }
            } else { // current node has one child
                if (currNode == root) { // current node is the root
                    if (currNode.hasLeft()) {
                        root = currNode.getLeft();
                    } else if (currNode.hasRight()) {
                        root = currNode.getRight();
                    }
                } else if (currNode == currNode.getParent().getLeft()) { // current node is at the left of parent node
                    if (currNode.hasLeft()) {
                        currNode.getLeft().setParent(currNode.getParent());
                        currNode.getParent().setLeft(currNode.getLeft());
                        currNode.setLeft(null);
                    } else if (currNode.hasRight()) {
                        currNode.getRight().setParent(currNode.getParent());
                        currNode.getParent().setLeft(currNode.getRight());
                        currNode.setRight(null);
                    }
                } else if (currNode == currNode.getParent().getRight()) { // current node is at the right of parent node
                    if (currNode.hasLeft()) {
                        currNode.getLeft().setParent(currNode.getParent());
                        currNode.getParent().setRight(currNode.getLeft());
                        currNode.setLeft(null);
                    } else if (currNode.hasRight()) {
                        currNode.getRight().setParent(currNode.getParent());
                        currNode.getParent().setRight(currNode.getRight());
                        currNode.setRight(null);
                    }
                }
            }
        }

    }

    protected void rebalance(ListNode<E> currNode) {
        int currBalanceFactor = balanceFactor(currNode);
        int leftBalanceFactor = balanceFactor(currNode.getLeft());
        int rightBalanceFactor = balanceFactor(currNode.getRight());

        if (currBalanceFactor == 2 && leftBalanceFactor == -1) {
            rotateLeft(currNode.getLeft());
            rotateRight(currNode);
        } else if (currBalanceFactor == 2 && (leftBalanceFactor >= 0 && leftBalanceFactor <= 1)) {
            rotateRight(currNode);
        }else if(currBalanceFactor == -2 && rightBalanceFactor == 1){
            rotateRight(currNode.getRight());
            rotateLeft(currNode);
        }else if(currBalanceFactor == -2 && (rightBalanceFactor >= -1 && rightBalanceFactor <= 0)){
            rotateLeft(currNode);
        }
    }

    protected int balanceFactor(ListNode<E> currNode) {
        if (currNode == null) {
            return 0;
        }

        return currNode.getLeft().getHeight() - currNode.getRight().getHeight();
    }

    protected ListNode<E> rotateLeft(ListNode<E> currNode) {
        if (currNode.hasRight()) {
            ListNode<E> rightSubtree = currNode.getRight();
            rightSubtree.setParent(currNode.getParent());
            currNode.setParent(rightSubtree);
            currNode.setRight(rightSubtree.getLeft());

            if (rightSubtree.hasLeft()) {
                rightSubtree.getLeft().setParent(currNode);
            }

            rightSubtree.setLeft(currNode);

            return rightSubtree;
        }

        return null;
    }

    protected ListNode<E> rotateRight(ListNode<E> currNode) {
        if (currNode.hasLeft()) {
            ListNode<E> leftSubtree = currNode.getLeft();
            leftSubtree.setParent(currNode.getParent());
            currNode.setParent(leftSubtree);
            currNode.setLeft(leftSubtree.getRight());

            if (leftSubtree.hasRight()) {
                leftSubtree.getRight().setParent(currNode);
            }

            leftSubtree.setRight(currNode);

            return leftSubtree;
        }

        return null;
    }

    protected ListNode<E> successor(ListNode<E> currNode) {
        return minimum(currNode.getRight());
    }

    protected ListNode<E> minimum(ListNode<E> currNode) {
        if (currNode == null) {
            // empty tree
            throw new NoSuchElementException();
        }

        if (currNode.hasLeft()) {
            return minimum(currNode.getLeft());
        } else {
            return currNode;
        }
    }

    protected int height(ListNode<E> currNode) {
        if (currNode == null) {
            return -1;
        } else {
            currNode.setHeight(Math.max(height(currNode.getLeft()), height(currNode.getRight())) + 1);

            return currNode.getHeight();
        }
    }

    protected void printInOrder(ListNode<E> currNode) {
        if (currNode != null) {
            printInOrder(currNode.getLeft());
            System.out.print(currNode.getData() + " ");
            printInOrder(currNode.getRight());
        }
    }

}

class ListNode<E extends Comparable<E>> {
    private E data;
    private int height;
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

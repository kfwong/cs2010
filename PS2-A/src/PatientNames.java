/**
 * Created by kfwong on 2/2/17.
 */
// Copy paste this Java Template and save it as "PatientNames.java"

import java.util.*;
import java.io.*;
import java.util.stream.IntStream;

// write your matric number here:
// write your name here:
// write list of collaborators here:
// year 2017 hash code: DZAjKugdE9QiOQKGFbut (do NOT delete this line)

class PatientNames {
    // if needed, declare a private data structure here that
    // is accessible to all methods in this class

    // --------------------------------------------


    // --------------------------------------------

    public PatientNames() {
        // Write necessary code during construction;
        //
        // write your answer here

        // --------------------------------------------


        // --------------------------------------------
    }

    void AddPatient(String patientName, int gender) {
        // You have to insert the information (patientName, gender)
        // into your chosen data structure
        //
        // write your answer here

        // --------------------------------------------


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

        /*
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        IntStream.of(new int[]{63, 11, 57, 59, 12, 73, 44, 25, 54, 23, 49, 39, 22, 3, 8, 93, 88, 97, 32}).forEach(i -> {
            //new Random().ints(20, 1, 100).distinct().forEach(i->{
            //System.out.print(i +",");
            bst.insert(i);
        });

        bst.printInOrder();

        bst.remove(49);

        bst.printInOrder();

        //System.out.println(bst.minimum());
        */
    }
}

class BinarySearchTree<E extends Comparable<E>> {
    private ListNode<E> root;

    public BinarySearchTree() {
        this.root = null;
    }

    public void insert(E data) {
        root = insert(root, data);
    }

    public void remove(E data) {
        remove(null, root, data);
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

    private ListNode<E> insert(ListNode<E> currNode, E data) {
        if (currNode == null) {
            return new ListNode<>(data);
        }

        int comparison = data.compareTo(currNode.getData());

        if (comparison <= 0) {
            currNode.setLeft(insert(currNode.getLeft(), data));
        }

        if (comparison > 0) {
            currNode.setRight(insert(currNode.getRight(), data));
        }

        // update height of nodes after insertion
        // this will bubble up from inserted node
        currNode.setHeight(currNode.getHeight() + 1);

        return currNode;
    }

    private void remove(ListNode<E> prevNode, ListNode<E> currNode, E data) {
        if (currNode == null) {
            throw new NoSuchElementException();
        }

        int comparison = data.compareTo(currNode.getData());

        if (comparison < 0) {
            remove(currNode, currNode.getLeft(), data);
        }

        if (comparison > 0) {
            remove(currNode, currNode.getRight(), data);
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
                remove(currNode, currNode.getRight(), successor.getData());

            } else if (!currNode.hasLeft() && !currNode.hasRight()) { // has no child
                if (prevNode.getLeft() == currNode) {
                    prevNode.setLeft(null);
                } else {
                    prevNode.setRight(null);
                }
            } else { // current node has one child
                if (prevNode.getLeft() == currNode) { // current node is at the left of parent node
                    if (currNode.hasLeft()) {
                        prevNode.setLeft(currNode.getLeft());
                    } else if (currNode.hasRight()) {
                        prevNode.setLeft(currNode.getRight());
                    }
                } else if (prevNode.getRight() == currNode) { // current node is at the right of parent node
                    if (currNode.hasLeft()) {
                        prevNode.setRight(currNode.getLeft());
                    } else if (currNode.hasRight()) {
                        prevNode.setRight(currNode.getRight());
                    }
                }
            }
        }

    }

    private ListNode<E> successor(ListNode<E> currNode) {
        return minimum(currNode.getRight());
    }

    private ListNode<E> minimum(ListNode<E> currNode) {
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

    private int height(ListNode<E> currNode) {
        if (currNode == null) {
            return -1;
        } else {
            currNode.setHeight(Math.max(height(currNode.getLeft()), height(currNode.getRight())) + 1);

            return currNode.getHeight();
        }
    }

    private void printInOrder(ListNode<E> currNode) {
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
    private ListNode<E> left;
    private ListNode<E> right;

    public ListNode(E data) {
        this.data = data;
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

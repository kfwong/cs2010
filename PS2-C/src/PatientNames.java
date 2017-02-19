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

    public static final int MALE = 1;
    public static final int FEMALE = 2;

    private BinarySearchTree<String> malePatients;
    private BinarySearchTree<String> femalePatients;

    // --------------------------------------------

    public PatientNames() {
        // Write necessary code during construction;
        //
        // write your answer here

        // --------------------------------------------

        malePatients = new BinarySearchTree<>();
        femalePatients = new BinarySearchTree<>();

        // --------------------------------------------
    }

    void AddPatient(String patientName, int gender) {
        // You have to insert the information (patientName, gender)
        // into your chosen data structure
        //
        // write your answer here

        // --------------------------------------------
        if (gender == MALE) {
            malePatients.insert(patientName);
        } else if (gender == FEMALE) {
            femalePatients.insert(patientName);
        } else {
            // wtf?
            assert false : "gender must be 1 or 2. Go home you're drunk.";
        }



        // --------------------------------------------
    }

    void RemovePatient(String patientName) {
        // You have to remove the patientName from your chosen data structure
        //
        // write your answer here

        // --------------------------------------------

        if (malePatients.contains(patientName)) {
            //System.out.println("remove male");
            malePatients.remove(patientName);
        } else if (femalePatients.contains(patientName)) {
            //System.out.println("remove female");
            femalePatients.remove(patientName);
        }



        // --------------------------------------------
    }

    int Query(String start, String end, int gender) {
        int ans = 0;

        // You have to answer how many patient name starts
        // with prefix that is inside query interval [START..END)
        //
        // write your answer here

        // --------------------------------------------


        if (gender == MALE) {
            ans = malePatients.rangeCount(start, end);
        } else if (gender == FEMALE) {
            ans = femalePatients.rangeCount(start, end);
        } else {

            ans = malePatients.rangeCount(start, end) + femalePatients.rangeCount(start, end);
        }

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

/*
                System.out.println("-----insert--------");
                malePatients.inOrderTraversal(malePatients.root);
                System.out.println();
                femalePatients.inOrderTraversal(femalePatients.root);
*/
/*
                System.out.println("-------------");
                System.out.println(malePatients.minimum()+":"+malePatients.maximum());
                System.out.println(femalePatients.minimum() + ":" + femalePatients.maximum());
*/
            } else if (command == 2) { // RemovePatient
                RemovePatient(st.nextToken());
/*
                System.out.println("-----delete--------");
                malePatients.inOrderTraversal(malePatients.root);
                System.out.println();
                femalePatients.inOrderTraversal(femalePatients.root);
                */
            } else // if (command == 3) // Query
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

class BinarySearchTree<E extends Comparable<E>> {
    protected ListNode<E> root;

    public BinarySearchTree() {
        this.root = null;
    }

    public void insert(E data) {
        root = insert(root, data);
/*
        System.out.println("************MIN:"+root.getLocalMin().getData());
        System.out.println("************MAX:"+root.getLocalMax().getData());
        */
    }

    public void remove(E data) {
        root = remove(root, data);
    }

    public boolean contains(E data) {
        return contains(root, data);
    }

    protected ListNode<E> insert(ListNode<E> currNode, E data) {
        if (currNode == null) {
            return new ListNode<>(data);
        }

        int comparison = data.compareTo(currNode.getData());

        if (comparison <= 0) {
            ListNode<E> insertion = insert(currNode.getLeft(), data);
            currNode.setLeft(insertion);
        } else if (comparison > 0) {
            ListNode<E> insertion = insert(currNode.getRight(), data);
            currNode.setRight(insertion);
        }

        updateHeight(currNode);

        updateSize(currNode);

        //updateLocalMin(currNode);
        //updateLocalMax(currNode);

        currNode = rebalance(currNode);

        return currNode;
    }

    public ListNode<E> remove(ListNode<E> currNode, E data) {
        if (currNode == null) {
            return null;
        }

        int comparison = data.compareTo(currNode.getData());

        if (comparison < 0) {
            if (currNode.hasLeft()) {
                ListNode<E> leftSubtree = remove(currNode.getLeft(), data);
                currNode.setLeft(leftSubtree);

                return currNode;
            }
        } else if (comparison > 0) {
            if (currNode.hasRight()) {

                ListNode<E> rightSubtree = remove(currNode.getRight(), data);
                currNode.setRight(rightSubtree);

                return currNode;
            }

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

                currNode.setRight(rightSubtree);

            } else if (!currNode.hasLeft() && !currNode.hasRight()) { // has no child
                currNode = null;
            } else { // current node has one child
                if (currNode.hasLeft()) {
                    return currNode.getLeft();
                } else if (currNode.hasRight()) {
                    return currNode.getRight();
                }
            }
        }

        updateHeight(currNode);
        updateSize(currNode);

        //updateLocalMin(currNode);
        //updateLocalMax(currNode);

        currNode = rebalance(currNode);

        return currNode;
    }

    protected boolean contains(ListNode<E> currNode, E data) {
        if (currNode == null) {
            return false;
        }

        int comparison = data.compareTo(currNode.getData());

        if (comparison > 0) {
            return contains(currNode.getRight(), data);
        } else if (comparison < 0) {
            return contains(currNode.getLeft(), data);
        } else {
            return true;
        }
    }

    protected int rangeCount(E start, E end) {
        if (root == null) {
            // empty tree
            return 0;
        }

        ListNode<E> startNode;
        ListNode<E> endNode;

        startNode = fuzzySearchStartNode(root, start);
        endNode = fuzzySearchEndNode(root, end);



/*
        System.out.println("----nodes---");
        System.out.println(startNode!= null? startNode.getData():"OMG");
        System.out.println(endNode!= null? endNode.getData():"OMG");
*/
        if (startNode == null || endNode == null) {
            // startNode == null as the 'start' is larger than the maximum in the tree
            // endNode == null as the 'end' is smaller than the minimum in the tree
            return 0;
        }

        //System.out.println(endNode.getData());

        if (end.compareTo(endNode.getData()) == 0) {
            // need to exclude the node if match

            // change the end node to its predecessor instead
            endNode = predecessor(endNode);

            // no predecessor
            if (endNode == null) {
                return 0;
            }
        }

        //System.out.println(endNode.getData());


        return rank(root, endNode.getData()) -
                rank(root, startNode.getData()) +
                1;
    }

    protected ListNode<E> fuzzySearchStartNode(ListNode<E> currNode, E data) {
        if (currNode == null) {
            return null;
        }

        int comparison = data.compareTo(currNode.getData());
        if (comparison > 0) {
            return fuzzySearchStartNode(currNode.getRight(), data);
        } else if (comparison < 0) {
            ListNode<E> nodeFoundInLeftSubtree = fuzzySearchStartNode(currNode.getLeft(), data);

            if (nodeFoundInLeftSubtree == null) {
                return currNode;
            } else {
                return nodeFoundInLeftSubtree;
            }
        } else {
            return currNode;
        }
    }

    protected ListNode<E> fuzzySearchEndNode(ListNode<E> currNode, E data) {
        if (currNode == null) {
            return null;
        }

        int comparison = data.compareTo(currNode.getData());
        if (comparison > 0) {
            /*
            ListNode<E> rightSubtreeLocalMin = currNode.getRight().getLocalMin();
            ListNode<E> rightSubtreeLocalMax = currNode.getRight().getLocalMax();

            boolean isInRightSubtree = data.compareTo(rightSubtreeLocalMin.getData()) >= 0 &&
                    data.compareTo(rightSubtreeLocalMax.getData()) <= 0;

            if (isInRightSubtree) {
                return fuzzySearchEndNode(currNode.getRight(), data);
            } else {
                return currNode;
            }
            */

            ListNode<E> nodeFoundInRightSubtree = fuzzySearchEndNode(currNode.getRight(), data);

            if (nodeFoundInRightSubtree == null) {
                return currNode;
            } else {
                return nodeFoundInRightSubtree;
            }

        } else if (comparison < 0) {
            return fuzzySearchEndNode(currNode.getLeft(), data);
        } else {
            return currNode;
        }
    }

    public void inOrderTraversal(ListNode<E> currNode) {
        if (currNode == null) {
            return;
        }
        inOrderTraversal(currNode.getLeft());
        System.out.println(currNode.getData().toString() + "[" + rank(root, currNode.getData()) + "]");
        inOrderTraversal(currNode.getRight());
    }

    protected int rank(ListNode<E> currNode, E data) {

        if (currNode == null) {
            return 0;
        } else if (data.compareTo(currNode.getData()) < 0) {
            return rank(currNode.getLeft(), data);
        } else if (data.compareTo(currNode.getData()) > 0) {
            return rank(currNode.getRight(), data) + size(currNode.getLeft()) + 1;
        } else {
            return size(currNode.getLeft());
        }
    }

    protected int size(ListNode<E> currNode) {
        if (currNode == null) {
            return 0;
        } else {
            return currNode.getSize();
        }
    }

    protected ListNode<E> rebalance(ListNode<E> currNode) {
        // https://www.cise.ufl.edu/~nemo/cop3530/AVL-Tree-Rotations.pdf
        // check page 5 for pseudocode. More human readable than the lecture notes.

        int currNodeBF = balanceFactor(currNode);

        if (currNodeBF == 2) {
            int leftNodeBF = balanceFactor(currNode.getLeft());

            if (isLeftLeftCase(currNodeBF, leftNodeBF)) {
                currNode = rotateRight(currNode);

            } else if (isLeftRightCase(currNodeBF, leftNodeBF)) {
                currNode.setLeft(rotateLeft(currNode.getLeft()));
                currNode = rotateRight(currNode);

            }
        } else if (currNodeBF == -2) {
            int rightNodeBF = balanceFactor(currNode.getRight());

            if (isRightRightCase(currNodeBF, rightNodeBF)) {
                currNode = rotateLeft(currNode);

            } else if (isRightLeftCase(currNodeBF, rightNodeBF)) {
                currNode.setRight(rotateRight(currNode.getRight()));
                currNode = rotateLeft(currNode);
            }
        }

        return currNode;
    }

    protected boolean isLeftLeftCase(int currNodeBF, int leftNodeBF) {

        return currNodeBF == 2 && // left heavy
                (leftNodeBF >= 0 && leftNodeBF <= 1); // left heavy
    }

    protected boolean isLeftRightCase(int currNodeBF, int leftNodeBF) {
        return currNodeBF == 2 && // left heavy
                (leftNodeBF == -1); // right heavy
    }

    private boolean isRightRightCase(int currNodeBF, int rightNodeBF) {
        return currNodeBF == -2 && // right heavy
                (rightNodeBF >= -1 && rightNodeBF <= 0); // right heavy
    }

    private boolean isRightLeftCase(int currNodeBF, int rightNodeBF) {

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

            currNode.setRight(rightSubtree.getLeft());

            rightSubtree.setLeft(currNode);

            updateHeight(rightSubtree);
            updateHeight(currNode);

            // we only care about the size of left subtree
            //updateSize(rightSubtree);
            //updateSize(currNode);

            //updateLocalMin(rightSubtree);
            //updateLocalMax(rightSubtree);

            //updateLocalMin(currNode);
            //updateLocalMax(currNode);


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

            currNode.setLeft(leftSubtree.getRight());

            leftSubtree.setRight(currNode);

            updateHeight(currNode);
            updateHeight(leftSubtree);

            updateSize(currNode);
            updateSize(leftSubtree);

            //updateLocalMin(currNode);
            //updateLocalMax(currNode);

            //updateLocalMin(leftSubtree);
            //updateLocalMax(leftSubtree);

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

        // found iterative version from stackoverflow
        // http://stackoverflow.com/questions/3796003/find-the-successor-without-using-parent-pointer

        if (currNode.hasRight()) {
            return minimum(currNode.getRight());
        } else {
            ListNode<E> result = null;
            ListNode<E> parent = root;

            while (root != currNode) {
                if (currNode.getData().compareTo(parent.getData()) < 0) {
                    result = parent;
                    parent = parent.getLeft();
                } else {
                    parent = parent.getRight();
                }
            }
            return result;
        }

    }

    private ListNode<E> predecessor(ListNode<E> currNode) {
        if (currNode.hasLeft()) {
            return maximum(currNode.getLeft());
        } else {
            ListNode<E> result = null;
            ListNode<E> parent = root;

            while (root != currNode) {
                if (currNode.getData().compareTo(parent.getData()) > 0) {
                    result = parent;
                    parent = parent.getRight();
                } else {
                    parent = parent.getLeft();
                }
            }
            return result;
        }
    }

    protected ListNode<E> maximum(ListNode<E> currNode) {
        if (currNode == null) {
            // empty tree
            throw new NoSuchElementException("There's NOTHING in an empty tree. Go home you're drunk.");
        }

        if (currNode.hasRight()) {
            return maximum(currNode.getRight());
        } else {
            return currNode;
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
    // not used
    protected ListNode<E> updateLocalMax(ListNode<E> currNode) {
        if (currNode == null) {
            return null;
        }

        if (currNode.hasBoth()) {
            ListNode<E> leftSubtreeLocalMax = updateLocalMax(currNode.getLeft());
            ListNode<E> rightSubtreeLocalMax = updateLocalMax(currNode.getRight());
            ListNode<E> currLocalMax = currNode.getLocalMax();

            ListNode<E> childrenMax = leftSubtreeLocalMax.getData().compareTo(rightSubtreeLocalMax.getData()) > 0 ? leftSubtreeLocalMax : rightSubtreeLocalMax;

            currNode.setLocalMax(childrenMax.getData().compareTo(currLocalMax.getData()) > 0 ? childrenMax : currLocalMax);
        } else if (currNode.hasLeft()) {
            ListNode<E> leftSubtreeLocalMax = updateLocalMax(currNode.getLeft());
            ListNode<E> currLocalMax = currNode.getLocalMax();

            currNode.setLocalMax(leftSubtreeLocalMax.getData().compareTo(currLocalMax.getData()) > 0 ? leftSubtreeLocalMax : currLocalMax);
        } else if (currNode.hasRight()) {
            ListNode<E> rightSubtreeLocalMax = updateLocalMax(currNode.getRight());
            ListNode<E> currLocalMax = currNode.getLocalMax();

            currNode.setLocalMax(rightSubtreeLocalMax.getData().compareTo(currLocalMax.getData()) > 0 ? rightSubtreeLocalMax : currLocalMax);
        } else { // if current node has both child
            currNode.setLocalMax(currNode);
        }

        return currNode.getLocalMax();
    }

    // not used
    protected ListNode<E> updateLocalMin(ListNode<E> currNode) {
        if (currNode == null) {
            return null;
        }

        if (currNode.hasBoth()) {
            ListNode<E> leftSubtreeLocalMin = updateLocalMin(currNode.getLeft());
            ListNode<E> rightSubtreeLocalMin = updateLocalMin(currNode.getRight().getLocalMin());
            ListNode<E> currLocalMin = currNode.getLocalMin();

            ListNode<E> childrenMin = leftSubtreeLocalMin.getData().compareTo(rightSubtreeLocalMin.getData()) < 0 ? leftSubtreeLocalMin : rightSubtreeLocalMin;

            currNode.setLocalMin(childrenMin.getData().compareTo(currLocalMin.getData()) < 0 ? childrenMin : currLocalMin);
        } else if (currNode.hasLeft()) {
            ListNode<E> leftSubtreeLocalMin = updateLocalMin(currNode.getLeft());
            ListNode<E> currLocalMin = currNode.getLocalMin();

            currNode.setLocalMin(leftSubtreeLocalMin.getData().compareTo(currLocalMin.getData()) < 0 ? leftSubtreeLocalMin : currLocalMin);
        } else if (currNode.hasRight()) {
            ListNode<E> rightSubtreeLocalMin = updateLocalMin(currNode.getRight());
            ListNode<E> currLocalMin = currNode.getLocalMin();

            currNode.setLocalMin(rightSubtreeLocalMin.getData().compareTo(currLocalMin.getData()) < 0 ? rightSubtreeLocalMin : currLocalMin);
        } else {
            currNode.setLocalMin(currNode);
        }

        return currNode.getLocalMin();
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

    private ListNode<E> localMax;
    private ListNode<E> localMin;

    //private ListNode<E> parent;
    private ListNode<E> left;
    private ListNode<E> right;

    public ListNode(E data) {
        this.data = data;
        this.localMin = this;
        this.localMax = this;
        //this.parent = null;
        this.left = null;
        this.right = null;
    }

    public ListNode<E> getLocalMax() {
        return localMax;
    }

    public void setLocalMax(ListNode<E> localMax) {
        this.localMax = localMax;
    }

    public ListNode<E> getLocalMin() {
        return localMin;
    }

    public void setLocalMin(ListNode<E> localMin) {
        this.localMin = localMin;
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

    /*

        public ListNode<E> getParent() {
            return parent;
        }

        public void setParent(ListNode<E> parent) {
            this.parent = parent;
        }
    */
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

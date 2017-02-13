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
            if (command == 0) { // end of input
                Printer.print(patientDirectory.root);
                break;
            } else if (command == 1) { // AddPatient
                AddPatient(st.nextToken(), Integer.parseInt(st.nextToken()));
                Printer.print(patientDirectory.root);
            }else if (command == 2) { // RemovePatient
                RemovePatient(st.nextToken());
                Printer.print(patientDirectory.root);
            }else { // if (command == 3) // Query
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

        // TODO
        // use hashset

        if (currNode == null) {
            return 0;
        }

        int total = 0;

        if (currNode.getData().getName().compareTo(start) >= 0) {
            total += rangeSearch(currNode.getLeft(), start, end, gender);
        }

        if (currNode.getData().getName().compareTo(start) >= 0 && currNode.getData().getName().compareTo(end) < 0) {
            if (gender == 0 || gender == currNode.getData().getGender()) {
                total++;
            }
        }

        if (currNode.getData().getName().compareTo(end) < 0) {
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

    @Override
    public String toString() {
        return this.name;
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
        root = remove(root, data);
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
            updateHeight(currNode);
            currNode = rebalance(currNode);
        }

        if (comparison > 0) {
            ListNode<E> insertion = insert(currNode.getRight(), data);
            currNode.setRight(insertion);
            insertion.setParent(currNode);
            updateHeight(currNode);
            currNode = rebalance(currNode);
        }

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

            currNode = rebalance(currNode);

            return currNode;
        }

        return currNode;
    }

    protected ListNode<E> rebalance(ListNode<E> currNode) {
        System.out.println(currNode);
        int currBalanceFactor = balanceFactor(currNode);

        if (currBalanceFactor == 2) {
            int leftBalanceFactor = balanceFactor(currNode.getLeft());
            if(leftBalanceFactor == -1){
                currNode.setLeft(rotateLeft(currNode.getLeft()));
                currNode = rotateRight(currNode);
            }else if((leftBalanceFactor >= 0 && leftBalanceFactor <= 1)) {
                currNode = rotateRight(currNode);
            }
        } else if (currBalanceFactor == -2) {
            int rightBalanceFactor = balanceFactor(currNode.getRight());
            if (rightBalanceFactor == 1) {
                currNode.setRight(rotateRight(currNode.getRight()));
                currNode = rotateLeft(currNode);
            } else if (rightBalanceFactor >= -1 && rightBalanceFactor <= 0) {
                currNode = rotateLeft(currNode);
            }
        }

        return currNode;

    }

    protected int balanceFactor(ListNode<E> currNode) {
        if (currNode == null) {
            return 0;
        }

        int leftHeight = currNode.hasLeft() ? currNode.getLeft().getHeight() : -1;
        int rightHeight = currNode.hasRight() ? currNode.getRight().getHeight() : -1;

        return leftHeight - rightHeight;
    }

    protected ListNode<E> rotateLeft(ListNode<E> currNode) {
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

        }

        return rightSubtree;
    }

    protected ListNode<E> rotateRight(ListNode<E> currNode) {
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

            updateHeight(leftSubtree);
            updateHeight(currNode);
        }

        return leftSubtree;
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

    protected int updateHeight(ListNode<E> currNode) {
        if (currNode == null) {
            return -1;
        } else {
            currNode.setHeight(Math.max(updateHeight(currNode.getLeft()), updateHeight(currNode.getRight())) + 1);

            return currNode.getHeight();
        }
    }


    // need to recode to use hashset -- faster!!!
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

/////////////

class Printer {
    /**
     * Print a tree
     *
     * @param root tree root node
     */
    public static void print(ListNode<Patient> root) {
        List<List<String>> lines = new ArrayList<List<String>>();

        List<ListNode> level = new ArrayList<ListNode>();
        List<ListNode> next = new ArrayList<ListNode>();

        level.add(root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<String>();

            nn = 0;

            for (ListNode<Patient> n : level) {
                if (n == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                } else {
                    String aa = n.getData().getName() + " (" + n.getHeight() + ")";
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

// Copy paste this Java Template and save it as "EmergencyRoom.java"

import java.util.*;
import java.io.*;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here:
/*
 Charles Goh Chang Kang: For his test cases! @ 21 August 2016.

 Chow Yuan Bin: for his hints on the "tie breaker" on same post @ 21 Aug 2016.

 I didn't realize pseudo-code in lecture note did not preserve the natural order of the PQ in heap.
 The test cases Charles provided allows me to discover the bug.

 In addtion, this Stackoverflow post helps.
 http://stackoverflow.com/questions/6909617/how-to-preserve-the-order-of-elements-of-the-same-priority-in-a-priority-queue-i

 */
// year 2017 hash code: oIxT79fEI2IQdQqvg1rx (do NOT delete this line)

class EmergencyRoom {
    private PatientHeap patientHeap;

    public EmergencyRoom() {
        patientHeap = new PatientHeap();
    }

    void ArriveAtHospital(String patientName, int emergencyLvl) {
        Patient patient = new Patient(patientName, emergencyLvl);
        patientHeap.insert(patient);

    }

    void UpdateEmergencyLvl(String patientName, int incEmergencyLvl) {
        patientHeap.updateWithIncrement(patientName, incEmergencyLvl);

    }

    void Treat(String patientName) {
        if(patientHeap.peekMax().getName().equals(patientName)){
            patientHeap.extractMax();
        }else{
            patientHeap.bump(patientName);
            patientHeap.extractMax();
        }

    }

    String Query() {
        String ans = "The emergency room is empty";

        if(patientHeap.isEmpty()){
            return ans;
        }else{
            return patientHeap.peekMax().getName();
        }
    }

    void run() throws Exception {
        // do not alter this method

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pr = new PrintWriter(new BufferedWriter(new
                OutputStreamWriter(System.out)));
        int numCMD = Integer.parseInt(br.readLine()); // note that numCMD is >= N
        while (numCMD-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());
            switch (command) {
                case 0:
                    ArriveAtHospital(st.nextToken(), Integer.parseInt(st.nextToken()));
                    break;
                case 1:
                    UpdateEmergencyLvl(st.nextToken(),
                            Integer.parseInt(st.nextToken()));
                    break;
                case 2:
                    Treat(st.nextToken());
                    break;
                case 3:
                    pr.println(Query());
                    break;
            }
        }
        pr.close();
    }

    public static void main(String[] args) throws Exception {
        // do not alter this method
        EmergencyRoom ps1 = new EmergencyRoom();
        ps1.run();
    }
}

/**
 * Internal structure is BinaryHeap which make use of the concept of 1 based compact array for its implementation.
 */
class PatientHeap {
    // array reference based heap
    // making use of ArrayList to automatically resize array when needed
    private ArrayList<Patient> patients;

    // This HashMap will hold the name of the patient (key), and the object reference of the Patient (value)
    // For all updating and treat operation, we should use this to find the patient, which uses O(1) time.
    private HashMap<String, Patient> patientsDirectory;

    // This HashMap will hold the current position (index) in the heap.
    private HashMap<String, Integer> patientsPosition;

    /*
    Side note: I figured it would be more elegant if the heap is implemented in doubly linked list, so we only need to keep track of the object reference using pnly one HashMap
     However the implementation of doubly linked list is troublesome. I decided to use 2 HashMap for minimal changes.
     */

    // need to maintain the heap size attribute manually because ArrayList.size() does not account for dummy at index 0
    private int heapSize;

    public PatientHeap(){
        this.patients = new ArrayList<>();
        this.patientsDirectory = new HashMap<>();
        this.patientsPosition = new HashMap<>();
        this.heapSize = 0;

        // dummy object at 0 index
        this.patients.add(new Patient("DUMMY", -1));
    }

    /**
     * Insert a new Patient into the heap.
     * @param item the patient to be added
     */
    public void insert(Patient item){
        this.patients.add(item);
        this.patientsDirectory.put(item.getName(), item);

        heapSize++;

        this.patientsPosition.put(item.getName(), heapSize);

        shiftUp(heapSize);
    }

    /**
     * Remove and return the max item from heap.
     * @return the max item
     */
    public Patient extractMax(){
        Patient max = patients.get(1);
        Patient last = patients.get(heapSize);

        // swap the last item in the heap to the root
        patients.set(1, last);
        patients.remove(heapSize);

        heapSize--;

        // remove the position record (the extracted max) from the hashmap
        patientsPosition.remove(max.getName());
        patientsDirectory.remove(max.getName());

        // update the swapee (the last node that swap to the root) to the first position
        patientsPosition.put(last.getName(), 1);

        shiftDown(1);

        return max;
    }

    /**
     * Return the max item from heap. (Does not remove)
     * @return the max item
     */
    public Patient peekMax(){
        return patients.get(1);
    }

    /**
     * Return true if heap is empty
     * @return true if heap is empty
     */
    public boolean isEmpty(){
        return heapSize == 0;
    }

    /**
     * Update a patient's emergency level given the name.
     * The heap property is maintained after the execution.
     * @param patientName the name of the patient
     * @param increment value of increment
     */
    public void updateWithIncrement(String patientName, int increment){

        Patient p = patientsDirectory.get(patientName);
        int position = patientsPosition.get(patientName);

        p.setEmergencyLevel(p.getEmergencyLevel()+increment);

        shiftUp(position);

    }

    /**
     * Promote the patient to the highest priority. (Set emergency level to 100)
     * * The heap property is maintained after the execution.
     * @param patientName the name of the patient
     */
    public void bump(String patientName){

        Patient p = patientsDirectory.get(patientName);
        int position = patientsPosition.get(patientName);

        p.setEmergencyLevel(100);

        shiftUp(position);

    }

    /**
     * Bubble up operation for maintaining heap property.
     * @param index check the heap property given the index
     */
    private void shiftUp(int index){
        Patient parent = patients.get(parentOf(index));
        Patient child = patients.get(index);

        while( index > 1 && parent.compareTo(child) == -1){
            swap(parentOf(index), index);

            index = parentOf(index);

            parent = patients.get(parentOf(index));
        }
    }

    /**
     * Bubble down operation for maintaining heap property.
     * @param index check the heap property given the index
     */
    private void shiftDown(int index){
        while( index <= heapSize){
            int maxIndex = index;
            Patient max = patients.get(index);

            int leftChildIndex = leftChildOf(index);
            if(leftChildIndex != -1) { // if left child exist
                Patient leftChild = patients.get(leftChildIndex);

                if (leftChildIndex <= heapSize && max.compareTo(leftChild) == -1) {
                    maxIndex = leftChildIndex;
                    max = leftChild;
                }
            }

            int rightChildIndex = rightChildOf(index);
            if(rightChildIndex != -1) { // if right child exist
                Patient rightChild = patients.get(rightChildIndex);

                if (rightChildIndex <= heapSize && max.compareTo(rightChild) == -1) {
                    maxIndex = rightChildIndex;
                    max = rightChild;
                }
            }

            if(maxIndex != index){
                swap(index, maxIndex);
                index = maxIndex;
            } else {
                break;
            }
        }
    }

    /**
     * Swap the patients given two indexes.
     * @param index1 first target item's index
     * @param index2 second target item's index
     */
    private void swap(int index1, int index2){
        Patient p1 = patients.get(index1);
        Patient p2 = patients.get(index2);
        patients.set(index1, p2);
        patients.set(index2, p1);

        // update/swap position in the hash map
        patientsPosition.put(p1.getName(), index2);
        patientsPosition.put(p2.getName(), index1);
    }

    /**
     * Return the parent's index given a child index.
     * Return 1 if the index is root (no parent)
     * @param index child index
     * @return positive integer, return 1 only when the index given is 1 (root)
     */
    private int parentOf(int index){
        if(index > heapSize || index < 1){
            throw new HeapIndexOutOfBoundsException();
        }

        if(index == 1){
            return 1;
        }

        return (int) (Math.floor(index/2));
    }

    /**
     * Return the reference index of left child given a parent index.
     * @param index parent index
     * @return left child index, -1 if there's no left child
     */
    private int leftChildOf(int index){
        if(index > heapSize || index < 1){
            throw new HeapIndexOutOfBoundsException();
        }

        int leftChildIndex = 2 * index;

        return leftChildIndex > heapSize? -1: leftChildIndex;
    }

    /**
     * Return the reference index of right child given a parent index.
     * @param index parent index
     * @return right child index, -1 if there's no right child
     */
    private int rightChildOf(int index){
        if(index > heapSize|| index < 1){
            throw new HeapIndexOutOfBoundsException();
        }

        int rightChildIndex = 2 * index + 1;

        return rightChildIndex > heapSize? -1: rightChildIndex;
    }

}

class Patient implements Comparable<Patient>{
    private static int nextQueueNum = 0;

    private int queueNum;
    private String name;
    private int emergencyLevel;

    public Patient(String name, int emergencyLevel){
        this.queueNum = nextQueueNum;
        this.name = name;
        this.emergencyLevel = emergencyLevel;

        nextQueueNum++;
    }

    public int getQueueNum() {
        return queueNum;
    }

    public String getName() {
        return name;
    }

    public int getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(int emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    @Override
    public int compareTo(Patient o) {
        if(this.getEmergencyLevel() > o.getEmergencyLevel() || (this.getEmergencyLevel() == o.getEmergencyLevel() && this.getQueueNum() < o.getQueueNum())){
            return 1;
        }else{
            return -1;
        }
    }
}

class HeapIndexOutOfBoundsException extends ArrayIndexOutOfBoundsException{}
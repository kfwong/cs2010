// Copy paste this Java Template and save it as "EmergencyRoom.java"

import java.util.*;
import java.io.*;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here: NIL
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

    // need to maintain the heap size attribute manually because ArrayList.size() does not account for dummy at index 0
    private int heapSize;

    public PatientHeap(){
        this.patients = new ArrayList<Patient>();
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

        heapSize++;

        shiftUp(heapSize);
    }

    /**
     * Remove and return the max item from heap.
     * @return the max item
     */
    public Patient extractMax(){
        Patient max = patients.get(1);

        // swap the last item in the heap to the root
        patients.set(1, patients.get(heapSize));
        patients.remove(heapSize);

        heapSize--;

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

        for(int i = 1; i<patients.size(); i++){
            if(patients.get(i).getName().equals(patientName)){
                Patient patient = patients.get(i);

                int newEmergencyLevel = patient.getEmergencyLevel() + increment;

                patient.setEmergencyLevel(newEmergencyLevel);

                shiftUp(i);

                break;
            }
        }
    }

    /**
     * Promote the patient to the highest priority. (Set emergency level to 100)
     * * The heap property is maintained after the execution.
     * @param patientName the name of the patient
     */
    public void bump(String patientName){
        for(int i = 1; i<patients.size(); i++){
            if(patients.get(i).getName().equals(patientName)){
                Patient patient = patients.get(i);

                patient.setEmergencyLevel(100);

                shiftUp(i);

                break;
            }
        }
    }

    /**
     * Bubble up operation for maintaining heap property.
     * @param index check the heap property given the index
     */
    private void shiftUp(int index){
        Patient parent = patients.get(parentOf(index));
        Patient child = patients.get(index);

        int parentEmergencyLevel = parent.getEmergencyLevel();
        int childEmergencyLevel = child.getEmergencyLevel();

        while( index > 1 && parentEmergencyLevel < childEmergencyLevel){
            swap(parentOf(index), index);

            index = parentOf(index);
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

                if (leftChildIndex <= heapSize && max.getEmergencyLevel() < leftChild.getEmergencyLevel()) {
                    maxIndex = leftChildIndex;
                    max = leftChild;
                }
            }

            int rightChildIndex = rightChildOf(index);
            if(rightChildIndex != -1) { // if right child exist
                Patient rightChild = patients.get(rightChildIndex);

                if (rightChildIndex <= heapSize && max.getEmergencyLevel() < rightChild.getEmergencyLevel()) {
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
        Patient temp = patients.get(index1);
        patients.set(index1, patients.get(index2));
        patients.set(index2, temp);
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

class Patient{
    private String name;
    private int emergencyLevel;

    public Patient(String name, int emergencyLevel){
        this.name = name;
        this.emergencyLevel = emergencyLevel;
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
}

class HeapIndexOutOfBoundsException extends ArrayIndexOutOfBoundsException{}
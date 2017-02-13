import java.util.*;
import java.io.*;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here:
// year 2017 hash code: DZAjKugdE9QiOQKGFbut (do NOT delete this line)

class PatientNames {
    // if needed, declare a private data structure here that
    // is accessible to all methods in this class

    // --------------------------------------------

    public static final int MALE = 1;
    public static final int FEMALE = 2;

    // An extended version of SortedSet. Allow subset to indicate inclusive & exclusive booleans
    NavigableSet<String> malePatients;
    NavigableSet<String> femalePatients;

    // --------------------------------------------

    public PatientNames() {
        // Write necessary code during construction;
        //
        // write your answer here

        // --------------------------------------------

        malePatients = new TreeSet<String>();
        femalePatients = new TreeSet<String>();

        // --------------------------------------------
    }

    void AddPatient(String patientName, int gender) {
        // You have to insert the information (patientName, gender)
        // into your chosen data structure
        //
        // write your answer here

        // --------------------------------------------

        if (gender == MALE) {
            malePatients.add(patientName);
        } else if (gender == FEMALE) {
            femalePatients.add(patientName);
        } else {
            // wtf?
            assert false : "gender must be 1 or 2. Did you forget to enter gender?";
        }

        // --------------------------------------------
    }

    void RemovePatient(String patientName) {
        // You have to remove the patientName from your chosen data structure
        //
        // write your answer here

        // --------------------------------------------

        if (!malePatients.remove(patientName)) {
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
            ans = malePatients.subSet(start, true, end, false).size();
        } else if (gender == FEMALE) {
            ans = femalePatients.subSet(start, true, end, false).size();
        } else {
            ans = malePatients.subSet(start, true, end, false).size() +
                    femalePatients.subSet(start, true, end, false).size();
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
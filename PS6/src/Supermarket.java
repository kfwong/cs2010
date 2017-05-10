// Copy paste this Java Template and save it as "Supermarket.java"
import java.util.*;
import java.io.*;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here:
// year 2017 hash code: mZGXoXQ2wBX0FK7WCZun (do NOT delete this line)

class Supermarket {
    private int N; // number of items in the supermarket. V = N+1
    private int K; // the number of items that Ketfah has to buy
    private int[] shoppingList; // indices of items that Ketfah has to buy
    private int[][] T; // the complete weighted graph that measures the direct wheeling time to go from one point to another point in seconds

    private int[][] apsp;

    // if needed, declare a private data structure here that
    // is accessible to all methods in this class
    // --------------------------------------------



    public Supermarket() {
        // Write necessary code during construction
        //
        // write your answer here

    }

    int Query() {
        int ans = 0;

        // You have to report the quickest shopping time that is measured
        // since Ketfah enters the supermarket (vertex 0),
        // completes the task of buying K items in that supermarket,
        // then reaches the cashier of the supermarket (back to vertex 0).
        //
        // write your answer here

        apsp = new int[N][];
        for(int i=0; i< N; i++){
            System.arraycopy(T[i], 0, apsp[i], 0, N);
        }


        for(int k=0; k<N; k++){
            for(int i=0; i<N; i++){
                for(int j=0; j<N; j++){
                    if(apsp[i][k] + apsp[k][j] < apsp[i][j]){
                        apsp[i][j] = apsp[i][k] + apsp[k][j];
                    }
                }
            }
        }

        return ans;
    }

    // You can add extra function if needed
    // --------------------------------------------



    void run() throws Exception {
        // do not alter this method to standardize the I/O speed (this is already very fast)
        IntegerScanner sc = new IntegerScanner(System.in);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int TC = sc.nextInt(); // there will be several test cases
        while (TC-- > 0) {
            // read the information of the complete graph with N+1 vertices
            N = sc.nextInt(); K = sc.nextInt(); // K is the number of items to be bought

            shoppingList = new int[K];
            for (int i = 0; i < K; i++)
                shoppingList[i] = sc.nextInt();

            T = new int[N+1][N+1];
            for (int i = 0; i <= N; i++)
                for (int j = 0; j <= N; j++)
                    T[i][j] = sc.nextInt();

            pw.println(Query());
        }

        pw.close();
    }

    public static void main(String[] args) throws Exception {
        // do not alter this method
        Supermarket ps6 = new Supermarket();
        ps6.run();
    }
}



class IntegerScanner { // coded by Ian Leow, using any other I/O method is not recommended
    BufferedInputStream bis;
    IntegerScanner(InputStream is) {
        bis = new BufferedInputStream(is, 1000000);
    }

    public int nextInt() {
        int result = 0;
        try {
            int cur = bis.read();
            if (cur == -1)
                return -1;

            while ((cur < 48 || cur > 57) && cur != 45) {
                cur = bis.read();
            }

            boolean negate = false;
            if (cur == 45) {
                negate = true;
                cur = bis.read();
            }

            while (cur >= 48 && cur <= 57) {
                result = result * 10 + (cur - 48);
                cur = bis.read();
            }

            if (negate) {
                return -result;
            }
            return result;
        }
        catch (IOException ioe) {
            return -1;
        }
    }
}
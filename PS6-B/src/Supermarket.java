// Copy paste this Java Template and save it as "Supermarket.java"

import java.io.*;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here: Xing Kai, Dylan
/*
CP 3 DP on TSP part
* https://stackoverflow.com/questions/10910913/how-do-shift-operators-work-in-java
* http://www.comp.nus.edu.sg/%7Estevenha/myteaching/competitive_programming/week04_dp_1.pdf
* https://stackoverflow.com/questions/47981/how-do-you-set-clear-and-toggle-a-single-bit-in-c-c

 */
// year 2017 hash code: mZGXoXQ2wBX0FK7WCZun (do NOT delete this line)

class Supermarket {
    private int N; // number of items in the supermarket. V = N+1
    private int K; // the number of items that Ketfah has to buy
    private int[] shoppingList; // indices of items that Ketfah has to buy
    private int[][] T; // the complete weighted graph that measures the direct wheeling time to go from one point to another point in seconds

    // if needed, declare a private data structure here that
    // is accessible to all methods in this class
    // --------------------------------------------
    private int[][] apsp; // for floyd warshall apsp solution for T
    private int[][] memo;

    private int itemsMask;
    private int[] _infinity;

    public Supermarket() {
        // Write necessary code during construction
        //
        // write your answer here

        //System.out.println("OMGTEST:"+(1<<0));
        //System.out.println("OMGTEST:"+(1<<1));
        //System.out.println("OMGTEST:"+(1<<2));


    }

    int Query() {

        // You have to report the quickest shopping time that is measured
        // since Ketfah enters the supermarket (vertex 0),
        // completes the task of buying K items in that supermarket,
        // then reaches the cashier of the supermarket (back to vertex 0).
        //
        // write your answer here

        //System.out.println("numstates: "+itemsMask);

        fw();

        return dp(0, 1);
    }

    // You can add extra function if needed
    // --------------------------------------------
    private void fw() {
        // copy the T
        apsp = new int[T.length][];
        for (int i = 0; i < T.length; i++) {
            apsp[i] = new int[T[i].length];
            System.arraycopy(T[i], 0, apsp[i], 0, T[i].length);
        }

        // FW algorithm on T
        for (int k = 0; k <= N; k++) {
            for (int i = 0; i <= N; i++) {
                for (int j = 0; j <= N; j++) {
                    apsp[i][j] = Math.min(apsp[i][j], apsp[i][k] + apsp[k][j]);
                }
            }
        }
    }

    private int dp(int u, int bitmask) {
        // similar to TSP top down DP, http://codeforces.com/blog/entry/47782

        // base case: all k items bought, return to source
        // tsp(post, 2^N-1) = dist[post][0]
        if (bitmask == (1 << (K + 1)) - 1) return apsp[u][0]; // bitmask are all 1's -> all k items bought
        /*
         * https://stackoverflow.com/questions/10910913/how-do-shift-operators-work-in-java
         * http://www.comp.nus.edu.sg/%7Estevenha/myteaching/competitive_programming/week04_dp_1.pdf
         * https://stackoverflow.com/questions/47981/how-do-you-set-clear-and-toggle-a-single-bit-in-c-c
          * */

        // memoization: if computed before, return the result from memo table
        if (memo[u][bitmask] != Integer.MAX_VALUE) return memo[u][bitmask];

        // given each K items need to be bought
        //      if the item cannot be bought at the current vertex AND all k items has not been bought yet
        //          recurse
        for (int k = 0; k < K; k++) {
            if (shoppingList[k] != u && (bitmask & (1 << (k+1))) == 0) { // this will get the value at k index
                // try next k item, return the minimum path among them
                memo[u][bitmask] = Math.min(memo[u][bitmask], apsp[u][shoppingList[k]] + dp(shoppingList[k], bitmask | (1 << (k+1))));
            }
        }

        // return current minimum cost
        return memo[u][bitmask];
    }

    void run() throws Exception {
        // do not alter this method to standardize the I/O speed (this is already very fast)
        IntegerScanner sc = new IntegerScanner(System.in);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int TC = sc.nextInt(); // there will be several test cases
        while (TC-- > 0) {
            // read the information of the complete graph with N+1 vertices
            N = sc.nextInt();
            K = sc.nextInt(); // K is the number of items to be bought

            itemsMask = 1 << (K + 1);
            memo = new int[N+1][itemsMask];
            _infinity = new int[itemsMask];

            for(int i = 0; i< itemsMask; i++){
                _infinity[i] = Integer.MAX_VALUE;
            }

            for (int i = 0; i <= N; i++) {
                memo[i] = new int[itemsMask];
                System.arraycopy(_infinity, 0, memo[i], 0, itemsMask);
            }

            shoppingList = new int[K];
            for (int i = 0; i < K; i++)
                shoppingList[i] = sc.nextInt();

            T = new int[N + 1][N + 1];
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
        } catch (IOException ioe) {
            return -1;
        }
    }
}
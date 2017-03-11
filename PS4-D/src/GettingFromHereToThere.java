// Copy paste this Java Template and save it as "GettingFromHereToThere.java"

import java.util.*;
import java.io.*;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here:
// Minimax: https://stackoverflow.com/questions/9022672/understanding-the-minimax-maximin-paths-floyd-warshall
// Floyd-Warshall Algo: https://www.youtube.com/watch?v=X6n30V6qCWU
// CP3 Page 155-159 on Floyd-Warshall Algo
// FW Optimization: http://www.jroller.com/vschiavoni/entry/a_fast_java_implementation_of
// Floyd-Warshall is so cooool!!!
//
// year 2017 hash code: x4gxK7xzMSlNvFsMEUVn (do NOT delete this line)

class GettingFromHereToThere {
    private int V; // number of vertices in the graph (number of rooms in the building)

    private int adjMat[][]; // adjacency matrix, storing max cost from i to j

    // if needed, declare a private data structure here that
    // is accessible to all methods in this class
    // --------------------------------------------


    // --------------------------------------------

    public GettingFromHereToThere() {
        // Write necessary codes during construction;
        //
        // write your answer here
    }

    void PreProcess() {
        // write your answer here
        // you can leave this method blank if you do not need it

        // initialize all other non-directly connected path to be infinity
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i == j) {
                    adjMat[i][j] = 0;
                } else {
                    adjMat[i][j] = Integer.MAX_VALUE;
                }
            }
        }

    }

    void floydWarshall() {

        for (int k = 0; k < V; k++) { // k is the vertices allowed as intermediate vertices
            for (int i = 0; i < V; i++) { // i is the source vertex, we restrict to 0~9 only
                for (int j = 0; j < V; j++) { // v is the destination vertex, 0~(V-1)
                    adjMat[i][j] = Math.min(
                            adjMat[i][j],
                            Math.max(adjMat[i][k], adjMat[k][j])
                    );

                    //adjMat[j][i] = adjMat[i][j];
                }
            }
        }
/*
        for (int i = 0; i < V; i++) {

            for (int j = 0; j < Math.min(V, 10); j++) {

                if (i != j) {

                    final int costSoFar = (i < j) ? adjMat[j][i] : adjMat[i][j];

                    for (int k = Math.min(i, j); k >= 0; k--) {
                        adjMat[j][k] = Math.min(adjMat[j][k], Math.max(costSoFar, adjMat[i][k]));
                    }

                    for (int k = i; k < j; k++) {

                        adjMat[j][k] = Math.min(adjMat[j][k], Math.max(costSoFar, adjMat[k][i]));

                    }

                }

            }

        }
*/
    }

    int Query(int source, int destination) {
        int ans = 0;

        // You have to report the weight of a corridor (an edge)
        // which has the highest effort rating in the easiest path from source to destination for the wheelchair bound
        //
        // write your answer here

        ans = adjMat[Math.max(source, destination)][Math.min(source, destination)];

        return ans;
    }

    // You can add extra function if needed
    // --------------------------------------------


    // --------------------------------------------

    void run() throws Exception {
        // do not alter this method
        IntegerScanner sc = new IntegerScanner(System.in);
        PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int TC = sc.nextInt(); // there will be several test cases
        while (TC-- > 0) {
            V = sc.nextInt();

            // clear the graph and read in a new graph as Adjacency List
            // restrict source vertices range from [0..min(9,V-1)] (at most 10 vertices in total)
            // the destination vertices in the query can range from [0..V-1].
            // (2 ≤ V ≤ 2 000, 0 ≤ E ≤ 100 000, 1 ≤ Q ≤ 100 000).

            // Floyd-Warshall can only be used in small graph, where n(V)<=400, the AdjMat needed is 400^2=160,000
            // Since we restrict the source V to be at most 10, then the AdjMat we care about is 10*2000=20,000
            // so it's still feasible to use Floyd-Warshall
            adjMat = new int[V][V];

            PreProcess(); // you may want to use this function or leave it empty if you do not need it

            for (int i = 0; i < V; i++) {
                int k = sc.nextInt(); // number of edges input for vertex i
                while (k-- > 0) {
                    int j = sc.nextInt();
                    int w = sc.nextInt();
                    adjMat[i][j] = w; // storing max cost from i to j
                }
            }

            // build the minimax matrix!
            floydWarshall();

            //prettyPrint();

            int Q = sc.nextInt();
            while (Q-- > 0)
                pr.println(Query(sc.nextInt(), sc.nextInt()));
            pr.println(); // separate the answer between two different graphs
        }


        pr.close();
    }

    private void prettyPrint() {
        for (int i = 0; i < adjMat.length; i++) {
            for (int j = 0; j < adjMat[i].length; j++) {
                System.out.print(adjMat[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        // do not alter this method
        GettingFromHereToThere ps4 = new GettingFromHereToThere();
        ps4.run();
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


class IntegerPair implements Comparable<IntegerPair> {
    Integer _first, _second;

    public IntegerPair(Integer f, Integer s) {
        _first = f;
        _second = s;
    }

    public int compareTo(IntegerPair o) {
        if (!this.first().equals(o.first()))
            return this.first() - o.first();
        else
            return this.second() - o.second();
    }

    Integer first() {
        return _first;
    }

    Integer second() {
        return _second;
    }
}


class IntegerTriple implements Comparable<IntegerTriple> {
    Integer _first, _second, _third;

    public IntegerTriple(Integer f, Integer s, Integer t) {
        _first = f;
        _second = s;
        _third = t;
    }

    public int compareTo(IntegerTriple o) {
        if (!this.first().equals(o.first()))
            return this.first() - o.first();
        else if (!this.second().equals(o.second()))
            return this.second() - o.second();
        else
            return this.third() - o.third();
    }

    Integer first() {
        return _first;
    }

    Integer second() {
        return _second;
    }

    Integer third() {
        return _third;
    }
}
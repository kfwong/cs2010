// Copy paste this Java Template and save it as "Supermarket.java"

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here:
// year 2017 hash code: mZGXoXQ2wBX0FK7WCZun (do NOT delete this line)

class Supermarket {
    private int N; // number of items in the supermarket. V = N+1
    private int K; // the number of items that Ketfah has to buy
    private int[] shoppingList; // indices of items that Ketfah has to buy
    private int[][] T; // the complete weighted graph that measures the direct wheeling time to go from one point to another point in seconds

    // if needed, declare a private data structure here that
    // is accessible to all methods in this class
    // --------------------------------------------
    private int minCost = Integer.MAX_VALUE;
    private boolean visited[];
    private List<Integer> path = new ArrayList<>();

    public Supermarket() {
        // Write necessary code during construction
        //
        // write your answer here

    }

    int Query() {

        minCost = Integer.MAX_VALUE;
        path.clear();

        dfsBacktrack(0);
        int ans = minCost;

        // You have to report the quickest shopping time that is measured
        // since Ketfah enters the supermarket (vertex 0),
        // completes the task of buying K items in that supermarket,
        // then reaches the cashier of the supermarket (back to vertex 0).
        //
        // write your answer here




        return ans;
    }

    // You can add extra function if needed
    // --------------------------------------------

    private void dfsBacktrack(int u) {
        path.add(u); // we append vertex u to current path
        visited[u] = true; // to avoid cycle

        // need to check if all vertices are visited
        boolean allVisited = true;
        for (int v = 1; v <= N; v++) {
            if (!visited[v]) {
                allVisited = false;
                break;
            }
        }

        // when all vertices has been visited, compute the cost
        if (allVisited) {
            int cost = 0;

            int fromV =path.get(0); // get the starting point of the path

            for(int v = 1; v <= N; v++){ // start accumulating the cost from second vertex in the path
                cost += T[fromV][path.get(v)];
                fromV = path.get(v); // update fromV to the current vertex for computing next iteration
            }

            cost += T[path.get(path.size()-1)][0]; // add up the cost from last vertex in the path to the source

            minCost = Math.min(minCost, cost);
        } else {
            for (int v = 1; v <= N; v++) {
                if (!visited[v]) {
                    dfsBacktrack(v);
                }
            }
        }

        visited[u] = false; // this is the only change to turn DFSrec to backtracking :)
        path.remove(path.size() - 1); // remove last vertex
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

            shoppingList = new int[K];
            for (int i = 0; i < K; i++)
                shoppingList[i] = sc.nextInt();

            T = new int[N + 1][N + 1];
            visited = new boolean[N + 1];
            for (int i = 0; i <= N; i++) {
                visited[i] = false; // make use of this step to initialize the array
                for (int j = 0; j <= N; j++) {
                    T[i][j] = sc.nextInt();
                }
            }


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
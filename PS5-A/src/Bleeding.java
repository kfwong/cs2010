// Copy paste this Java Template and save it as "Bleeding.java"

import java.util.*;
import java.io.*;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here:
// year 2017 hash code: x8DYWsALaAzykZ8dYPZP (do NOT delete this line)

class Bleeding {
    private int V; // number of vertices in the graph (number of junctions in Singapore map)
    private int Q; // number of queries

    // IntegerPair store (toVertex, weight)
    private ArrayList<ArrayList<IntegerPair>> AdjList; // the weighted graph (the Singapore map), the length of each edge (road) is stored here too, as the weight of edge

    // if needed, declare a private data structure here that
    // is accessible to all methods in this class
    // --------------------------------------------

    // IntegerPair stores (dist[u], u)
    private PriorityQueue<IntegerPair> pq;

    // distance cost matrix
    private int[][] distMatrix;

    // this variable initialize all distance to infinity, for quick System.arraycopy() only, not for calculation
    private int[] _dist;

    // --------------------------------------------

    public void buildSSSP(int s) {
        // modified dijkstra

        int[] dist = new int[V];
        // https://stackoverflow.com/questions/18638743/is-it-better-to-use-system-arraycopy-than-a-for-loop-for-copying-arrays
        // native array copy, copy by memory block instead of by each element, faster than manual loop if length>25
        System.arraycopy(_dist, 0, dist, 0, V);

        dist[s] = 0; // init source distance cost as zero

        pq.offer(new IntegerPair(0, s));

        while (!pq.isEmpty()) {
            IntegerPair processingVertex = pq.poll();

            int currDistance = processingVertex._first;
            int currVertex = processingVertex._second;

            if (currDistance == dist[currVertex]) { // lazy pq check
                AdjList.get(currVertex).forEach(adjVertex -> {
                    int toVertex = adjVertex._first;
                    int weight = adjVertex._second;

                    int distanceIfIncludeThisVertex = dist[currVertex] + weight;

                    if(dist[toVertex] > distanceIfIncludeThisVertex){ // the recorded distance at this adjacent vertex cost is too high -> can relax
                        dist[toVertex] = distanceIfIncludeThisVertex;
                        pq.offer(new IntegerPair(dist[toVertex], toVertex)); // re-enqueue this vertex
                    }
                });
            }

        }

        // record in matrix
        distMatrix[s] = dist;
    }

    public Bleeding() {
        // Write necessary code during construction
        //
        // write your answer here

        pq = new PriorityQueue<>();

    }

    void PreProcess() {
        // Write necessary code to preprocess the graph, if needed
        //
        // write your answer here
        //-------------------------------------------------------------------------

        //-------------------------------------------------------------------------
    }

    int Query(int s, int t, int k) {
        int ans = -1;

        // You have to report the shortest path from Ket Fah's current position s
        // to reach the chosen hospital t, output -1 if t is not reachable from s
        // with one catch: this path cannot use more than k vertices
        //
        // write your answer here

        if(distMatrix[s] == null) { // check if the SSSP at source has been before before
            buildSSSP(s);
        }

        ans = (distMatrix[s][t] == Integer.MAX_VALUE) ? -1 : distMatrix[s][t]; // O(ElogV) for first query, O(1) for subsequent query

        //-------------------------------------------------------------------------

        return ans;
    }

    // You can add extra function if needed
    // --------------------------------------------


    // --------------------------------------------

    void run() throws Exception {
        // you can alter this method if you need to do so
        IntegerScanner sc = new IntegerScanner(System.in);
        PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int TC = sc.nextInt(); // there will be several test cases
        while (TC-- > 0) {
            V = sc.nextInt();

            distMatrix = new int[V][];
            _dist = new int[V]; // init the size of distance vector

            // clear the graph and read in a new graph as Adjacency List
            AdjList = new ArrayList<ArrayList<IntegerPair>>();
            for (int i = 0; i < V; i++) {
                AdjList.add(new ArrayList<IntegerPair>());

                int k = sc.nextInt();
                while (k-- > 0) {
                    int j = sc.nextInt(), w = sc.nextInt();
                    AdjList.get(i).add(new IntegerPair(j, w)); // edge (road) weight (in minutes) is stored here
                }

                _dist[i] = Integer.MAX_VALUE; // making use of this loop to init dummy distance to all vertex to be "infinity"
            }

            PreProcess(); // optional

            Q = sc.nextInt();
            while (Q-- > 0)
                pr.println(Query(sc.nextInt(), sc.nextInt(), sc.nextInt()));

            if (TC > 0)
                pr.println();
        }

        pr.close();
    }

    public static void main(String[] args) throws Exception {
        // do not alter this method
        Bleeding ps5 = new Bleeding();
        ps5.run();
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
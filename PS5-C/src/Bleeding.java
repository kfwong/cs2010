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
    //private PriorityQueue<IntegerPair> pq;

    // IntegerTriple stores (dist[u], u, k-hops)
    private PriorityQueue<IntegerTriple> pq;

    // limited hops cheapest cost matrix
    private int[] hopsMatrix;

    private int[][] hopsMatrix2;

    // this variable initialize all distance to infinity, for quick System.arraycopy() only, not for calculation
    private int[] _dist;

    // --------------------------------------------

    public void buildSSSP(int s, int k) {
        // modified dijkstra

        hopsMatrix2 = new int[k+1][];

        int dist[] = new int[V];

        System.arraycopy(_dist, 0, dist, 0, V); // native byte code mem copy array, faster than manual loop if length>25

        for(int i=0; i<=k; i++){
            int dist2[] = new int[V];
            System.arraycopy(dist, 0, dist2, 0, V);
            hopsMatrix2[i] = dist2;
        }

        dist[s] = 0; // init source distance cost as zero

        pq.offer(new IntegerTriple(0, s, 1));

        while (!pq.isEmpty()) {

            IntegerTriple processingVertex = pq.poll();

            int currDistance = processingVertex._first;
            int currVertex = processingVertex._second;
            int currHops = processingVertex._third;

            prox(dist, currHops);

            if (currDistance == dist[currVertex]) { // lazy pq check
                AdjList.get(currVertex).forEach(adjVertex -> {
                    int toVertex = adjVertex._first;
                    int weight = adjVertex._second;

                    int distanceIfIncludeThisVertex = dist[currVertex] + weight;

                    if (currHops + 1 <= k) {
                        if (dist[toVertex] > distanceIfIncludeThisVertex) {
                            dist[toVertex] = distanceIfIncludeThisVertex;
                        }

                        pq.offer(new IntegerTriple(dist[toVertex], toVertex, currHops + 1)); // re-enqueue this vertex
                    }

                });
            }
        }

        hopsMatrix = dist;
    }

    public void prox(int[] dist, int k){
        for(int i=0; i<V; i++){
            if(hopsMatrix2[k][i] == Integer.MAX_VALUE) {
                hopsMatrix2[k][i] = dist[i];
            }
        }
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

        buildSSSP(s, k);


        //ans = (hopsMatrix[t] == Integer.MAX_VALUE) ? -1 : hopsMatrix[t];
        ans = (hopsMatrix2[k][t] == Integer.MAX_VALUE) ? -1 : hopsMatrix2[k][t];

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
            while (Q-- > 0) {
                int source = sc.nextInt();
                int destination = sc.nextInt();
                int k = sc.nextInt(); // the limit of hops

                pr.println(Query(source, destination, k));
            }

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
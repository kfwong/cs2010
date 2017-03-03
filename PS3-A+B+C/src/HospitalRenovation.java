// Copy paste this Java Template and save it as "HospitalRenovation.java"

import java.util.*;
import java.io.*;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here:
// https://www.youtube.com/watch?v=2kREIkF9UAs
// Tarjan's Algorithm: https://en.wikipedia.org/wiki/Biconnected_component
//
// year 2017 hash code: AlaYnzmQ65P4x2Uc559u (do NOT delete this line)

class HospitalRenovation {
    private int V; // number of vertices in the graph (number of rooms in the hospital)
    private int[][] AdjMatrix; // the graph (the hospital)
    private int[] RatingScore; // the weight of each vertex (rating score of each room)

    // if needed, declare a private data structure here that
    // is accessible to all methods in this class
    private boolean[] visited;
    private int[] parent;
    private int[] childrenCount;
    private IntegerPair[] luckyNumbers; // for each vertices at i assign [dfsVisitedTime, earliestVisitedTime]
    private int dfsVisitedTime;
    private HashSet<Integer> articulationPoints;

    public HospitalRenovation() {
        // Write necessary code during construction
        //
        // write your answer here

    }

    int Query() {
        int ans = 0;

        // initialize visited boolean array for all vertices to default false value
        visited = new boolean[V];

        // initialize parent array for all vertices to -1
        parent = new int[V];
        parent = Arrays.stream(parent).map(v -> -1).toArray();

        // initialize children count for all vertices to 0
        childrenCount = new int[V];

        // initialize empty articulationPoints array
        articulationPoints = new HashSet<>();

        // initialize dfsVisitedTime
        dfsVisitedTime = 0;

        // initialize luckyNumbers
        luckyNumbers = new IntegerPair[V];

        // start DFS from vertex 0
        findArticulationPoints(0);

        //System.out.println(articulationPoints);

        // You have to report the rating score of the critical room (vertex)
        // with the lowest rating score in this hospital
        //
        // or report -1 if that hospital has no critical room
        //
        // write your answer here

        if (articulationPoints.size() == 0) {
            ans = -1;
        } else {
            // Java Stream API FTW!!!!
            ans = articulationPoints.stream() // read ArrayList elements as stream
                    .map(v -> RatingScore[v]) // transform elements into rating score
                    .reduce(Integer::min) // reduce the rating score using Math.min() (https://stackoverflow.com/questions/31378324/how-to-find-maximum-value-from-a-integer-using-stream-in-java-8)
                    .get();
        }


        return ans;
    }

    // You can add extra function if needed
    // --------------------------------------------

    // Tarjan's Algorithm
    private void findArticulationPoints(int u) {

        // mark current vertex as visited
        visited[u] = true;
        //System.out.println("visited: "+u);

        luckyNumbers[u] = new IntegerPair(dfsVisitedTime, dfsVisitedTime);

        dfsVisitedTime++;

        // for all vertices
        for (int v = 0; v < V; v++) {
            // if v adjacent to u && v has not been visited before
            if (AdjMatrix[u][v] == 1) {
                if (!visited[v]) {

                    childrenCount[u]++;

                    parent[v] = u;

                    findArticulationPoints(v);

                    // update earliestVisitedTime of u to earliestVisitedTime of v if the latter is smaller
                    // this will make the parent (u) share the same earliest visitedTime as its child
                    luckyNumbers[u]._second = Math.min(luckyNumbers[u]._second, luckyNumbers[v]._second);

                    if (isArticulationPoint(u, v)) {
                        articulationPoints.add(u);
                    }
                } else {
                    // this means that u has back-edge to v
                    // update earliestVisitedTime of u to visitedTime of v if the latter has been visited before
                    luckyNumbers[u]._second = Math.min(luckyNumbers[u]._second, luckyNumbers[v]._first);
                }
            }
        }
    }

    private boolean isArticulationPoint(int u, int v) {
        if (parent[u] == -1) {
            // root vertex with more than 2 independent children

            return childrenCount[u] >= 2;
        } else {
            // dfsVisitedTime of u <= earliestVisitedTime of v
            return (luckyNumbers[u]._first <= luckyNumbers[v]._second);
        }

    }

    // to display human readable array.
    public static void prettyPrint(int[][] array) {
        System.out.println(Arrays.deepToString(array));
    }

    // --------------------------------------------

    void run() throws Exception {
        // for this PS3, you can alter this method as you see fit

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        int TC = Integer.parseInt(br.readLine()); // there will be several test cases
        while (TC-- > 0) {
            br.readLine(); // ignore dummy blank line
            V = Integer.parseInt(br.readLine());

            StringTokenizer st = new StringTokenizer(br.readLine());
            // read rating scores, A (index 0), B (index 1), C (index 2), ..., until the V-th index
            RatingScore = new int[V];
            for (int i = 0; i < V; i++)
                RatingScore[i] = Integer.parseInt(st.nextToken());

            // clear the graph and read in a new graph as Adjacency Matrix
            AdjMatrix = new int[V][V];
            for (int i = 0; i < V; i++) {
                st = new StringTokenizer(br.readLine());
                int k = Integer.parseInt(st.nextToken());
                while (k-- > 0) {
                    int j = Integer.parseInt(st.nextToken());
                    AdjMatrix[i][j] = 1; // edge weight is always 1 (the weight is on vertices now)
                }
            }

            pr.println(Query());
        }
        //prettyPrint(AdjMatrix);
        pr.close();
    }

    public static void main(String[] args) throws Exception {
        // do not alter this method
        HospitalRenovation ps3 = new HospitalRenovation();
        ps3.run();
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

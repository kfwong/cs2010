// Copy paste this Java Template and save it as "GettingFromHereToThere.java"

import java.util.*;
import java.io.*;

// write your matric number here: A0138862W
// write your name here: Wong Kang Fei
// write list of collaborators here:
// year 2017 hash code: x4gxK7xzMSlNvFsMEUVn (do NOT delete this line)

class GettingFromHereToThere {
    private int V; // number of vertices in the graph (number of rooms in the building)
    private ArrayList<ArrayList<IntegerPair>> AdjList; // the weighted graph (the building), effort rating of each corridor is stored here too
    private int[][] AdjMat;


    private ArrayList<ArrayList<IntegerPair>> mst2;
    private SortedSet<IntegerTriple> mst;

    private int[][] maxCostMatrix;

    // the index represent the vertex added into the maxCost,
    // the value is the MAX cost so far from this vertex to root
    private int[] maxCost;

    // the index represent the vertex in AdjList,
    // the value show true if the vertex has been added to maxCost
    private boolean[] added;

    // for dfs
    private boolean[] visited;

    PriorityQueue<IntegerTriple> queue;

    public GettingFromHereToThere() {
        // Write necessary codes during construction;
        //
        // write your answer here
    }

    void PreProcess() {
        // write your answer here
        // you can leave this method blank if you do not need it

        // reinitialize just in case it's dirty
        queue = new PriorityQueue<>();

        // initialize all vertices default max cost so far as -1
        maxCost = new int[V];

        // initialize all added booleans as false
        added = new boolean[V];

        mst = new TreeSet<>();

        mst2 = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            mst2.add(new ArrayList<>());
        }
    }

    int Query(int source, int destination) {
        int ans;

        int processedVerticesCount = 0;

        // You have to report the weight of a corridor (an edge)
        // which has the highest effort rating in the easiest path from source to destination for the wheelchair bound
        //
        // write your answer here

        // Prim's Algorithm modified to compute the max cost from each vertex to the source as it build the MST

        // add the source into maxCost
        maxCost[source] = 0;
        processedVerticesCount++;
        added[source] = true;

        // we build MST from source
        AdjList.get(source).stream() // read edges connected to source as stream
                .forEach(adjacentVertex -> {
                    // IntegerTriple store (cost, vertex, parent)
                    IntegerTriple edge = new IntegerTriple(adjacentVertex.first(), adjacentVertex.second(), source);

                    // push the edges to PQ
                    queue.offer(edge);
                });

        while (!queue.isEmpty()) {
            // break the loop if we already added all the vertices into the maxCost
            if (processedVerticesCount >= V) {
                break;
            }

            IntegerTriple edge = queue.poll();

            if (!added[edge.second()]) {
                // get the parent vertex's max cost so far
                Integer parentMaxCostSoFar = maxCost[edge.third()];

                // edge store (cost, vertex, parent)
                maxCost[edge.second()] = Math.max(edge.first(), parentMaxCostSoFar);

                // add to mst
                if (edge.second() < edge.third()) {
                    mst.add(new IntegerTriple(edge.second(), edge.third(), edge.first()));
                } else {
                    mst.add(new IntegerTriple(edge.third(), edge.second(), edge.first()));
                }

                mst2.get(edge.third()).add(new IntegerPair(edge.second(), edge.first()));

                // get the newly added vertex's index
                int vIndex = edge.second();

                // push all edges adjacent to that newly added vertex into PQ
                AdjList.get(vIndex).stream()
                        .forEach(adjacentVertex -> {
                            // IntegerTriple store (vertex, cost, parent)
                            IntegerTriple edge2 = new IntegerTriple(adjacentVertex.first(), adjacentVertex.second(), vIndex);

                            // push the edges to PQ
                            queue.offer(edge2);
                        });

                // set added to maxCost as true
                added[edge.second()] = true;
                processedVerticesCount++;
            }
        }

        maxCostMatrix[source] = maxCost;

        ans = maxCost[destination];
        //System.out.println(maxCost);
/*
        mst.forEach(it -> {
            System.out.println(it.first() + ":" + it.second() + ":" + it.third());
        });
*/
        //prettyPrint(AdjMat);

        dfsHelper(source);

        //System.out.println();

        //prettyPrintAdjList(mst2);

        //System.out.println();

        return ans;
    }

    private void prettyPrint(int[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void prettyPrintAdjList(ArrayList<ArrayList<IntegerPair>> adjList) {
        for (int i = 0; i < adjList.size(); i++) {
            System.out.print("[" + i + "] ");
            for (int j = 0; j < adjList.get(i).size(); j++) {
                System.out.print(adjList.get(i).get(j).first() + ":" + adjList.get(i).get(j).second() + " ");
            }
            ;
            System.out.println();
        }
        ;
    }

    private void dfs(int u, int cost, int p) {
        visited[u] = true;
        if (p == -1) {
            maxCost[u] = 0;
        } else {
            maxCost[u] = Math.max(cost, maxCost[p]);
        }

        //System.out.println("dfs visiting: " + u+", max cost so far: "+ maxCost[u]);

        mst2.get(u).forEach(adjacentVertex -> {
            if (!visited[adjacentVertex.first()]) {
                dfs(adjacentVertex.first(), adjacentVertex.second(), u);
            }
        });
    }

    private void dfsHelper(int u) {
        visited = new boolean[V];
        maxCost = new int[V];
        dfs(u, 0, -1);
    }

    void run() throws Exception {
        // do not alter this method
        IntegerScanner sc = new IntegerScanner(System.in);
        PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int TC = sc.nextInt(); // there will be several test cases
        while (TC-- > 0) {
            V = sc.nextInt();

            // clear the graph and read in a new graph as Adjacency List
            AdjList = new ArrayList<>();
            AdjMat = new int[V][V];
            for (int i = 0; i < V; i++) {
                AdjList.add(new ArrayList<>());

                int k = sc.nextInt();
                while (k-- > 0) {
                    int j = sc.nextInt();
                    int w = sc.nextInt();
                    AdjList.get(i).add(new IntegerPair(w, j)); // edge (corridor) weight (effort rating) is stored here
                    AdjMat[i][j] = w;
                }
            }

            int Q = sc.nextInt();
            while (Q-- > 0) {
                int src = sc.nextInt();
                int dest = sc.nextInt();

                // clear the maxCostMatrix for new test case
                // For this PS4, we restrict range from [0..min(9,V-1)]
                maxCostMatrix = new int[10][2000];

                PreProcess(); // you may want to use this function or leave it empty if you do not need it
                pr.println(Query(src, dest));

            }
            pr.println(); // separate the answer between two different graphs
        }

        pr.close();
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
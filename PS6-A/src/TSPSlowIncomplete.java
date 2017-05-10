import java.util.*;
import java.util.stream.Collectors;

public class TSPSlowIncomplete {
    private static int INF = Integer.MAX_VALUE;
    private static int N;
    private static int[][] AdjMatrix = new int[16][16];
    private static int counter;
    private static boolean[] visited = new boolean[16];


    // this is what you already know from Lecture 06, the basic DFS
    private static void DFSrec(int u) {
        visited[u] = true; // to avoid cycle
        //System.out.printf("%d ", u);
        for (int v = 0; v < N; v++)
            if (!visited[v])
                DFSrec(v);
        visited[u] = false; // this is the only change to turn DFSrec to backtracking :)
    }


    private static int minCost;
    private static ArrayList<Integer> path;

    // this is what happen if we reset "visited[u]" at the end of recursion
    private static void backtracking(int u) {
        path.add(u); // we append vertex u to current path
        visited[u] = true; // to avoid cycle

        // need to check if all vertices are visited
        boolean allVisited = true;
        for (int v = 0; v < N; v++) {
            if (!visited[v]) {
                allVisited = false;
                break;
            }
        }

        // when all vertices has been visited, compute the cost
        if (allVisited) {
            int cost = 0;

            int fromV =path.get(0); // get the starting point of the path

            for(int v = 1; v < N; v++){ // start accumulating the cost from second vertex in the path
                cost += AdjMatrix[fromV][path.get(v)];
                fromV = path.get(v); // update fromV to the current vertex for computing next iteration
            }

            cost += AdjMatrix[path.get(path.size()-1)][0]; // add up the cost from last vertex in the path to the source

            minCost = Math.min(minCost, cost);
        } else {
            for (int v = 0; v < N; v++) {
                if (!visited[v]) {
                    backtracking(v);
                }
            }
        }

        visited[u] = false; // this is the only change to turn DFSrec to backtracking :)
        path.remove(path.size() - 1); // remove last vertex
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
/*
// as shown in lecture notes
// answer = 97
4
0 20 42 35
20 0 30 34
42 30 0 12
35 34 12 0

// another test case
// answer = 
5
0 10 21 15 11
10 0 10 9 22
21 10 0 2 30
15 9 2 0 20
11 22 30 20 0

// now see the performance gap of various approach using this test case
// answer = 2576
10
0 42 468 335 501 170 725 479 359 963
42 0 465 706 146 282 828 962 492 996
468 465 0 943 828 437 392 605 903 154
335 706 943 0 293 383 422 717 719 896
501 146 828 293 0 448 727 772 539 870
170 282 437 383 448 0 913 668 300 36
725 828 392 422 727 913 0 895 704 812
479 962 605 717 772 668 895 0 323 334
359 492 903 719 539 300 704 323 0 674
963 996 154 896 870 36 812 334 674 0

// backtracking is getting slower... 11!
// answer = 2145
11
0 42 468 335 501 170 725 479 359 963 465
42 0 706 146 282 828 962 492 996 943 828
468 706 0 437 392 605 903 154 293 383 422
335 146 437 0 717 719 896 448 727 772 539
501 282 392 717 0 870 913 668 300 36 895
170 828 605 719 870 0 704 812 323 334 674
725 962 903 896 913 704 0 665 142 712 254
479 492 154 448 668 812 665 0 869 548 645
359 996 293 727 300 323 142 869 0 663 758
963 943 383 772 36 334 712 548 663 0 38
465 828 422 539 895 674 254 645 758 38 0

// backtracking is getting even slower... 12!
// answer = 2410
12
0 42 468 335 501 170 725 479 359 963 465 706
42 0 146 282 828 962 492 996 943 828 437 392
468 146 0 605 903 154 293 383 422 717 719 896
335 282 605 0 448 727 772 539 870 913 668 300
501 828 903 448 0 36 895 704 812 323 334 674
170 962 154 727 36 0 665 142 712 254 869 548
725 492 293 772 895 665 0 645 663 758 38 860
479 996 383 539 704 142 645 0 724 742 530 779
359 943 422 870 812 712 663 724 0 317 36 191
963 828 717 913 323 254 758 742 317 0 843 289
465 437 719 668 334 869 38 530 36 843 0 107
706 392 896 300 674 548 860 779 191 289 107 0

// the ultimate test... how about this one?
// answer = 2234
16
0 42 468 335 501 170 725 479 359 963 465 706 146 282 828 962
42 0 492 996 943 828 437 392 605 903 154 293 383 422 717 719
468 492 0 896 448 727 772 539 870 913 668 300 36 895 704 812
335 996 896 0 323 334 674 665 142 712 254 869 548 645 663 758
501 943 448 323 0 38 860 724 742 530 779 317 36 191 843 289
170 828 727 334 38 0 107 41 943 265 649 447 806 891 730 371
725 437 772 674 860 107 0 351 7 102 394 549 630 624 85 955
479 392 539 665 724 41 351 0 757 841 967 377 932 309 945 440
359 605 870 142 742 943 7 757 0 627 324 538 539 119 83 930
963 903 913 712 530 265 102 841 627 0 542 834 116 640 659 705
465 154 668 254 779 649 394 967 324 542 0 931 978 307 674 387
706 293 300 869 317 447 549 377 538 834 931 0 22 746 925 73
146 383 36 548 36 806 630 932 539 116 978 22 0 271 830 778
282 422 895 645 191 891 624 309 119 640 307 746 271 0 574 98
828 717 704 663 843 730 85 945 83 659 674 925 830 574 0 513
962 719 812 758 289 371 955 440 930 705 387 73 778 98 513 0
*/

        N = sc.nextInt();
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                AdjMatrix[i][j] = sc.nextInt();

        // System.out.printf("====================================\n");
        // System.out.printf("DFS\n");
        // Arrays.fill(visited, false);
        // DFSrec(0); // because the input graph is a complete graph,
        //            // DFSrec(0) is enough to traverse the entire graph
        // System.out.println();
        // System.out.printf("====================================\n");

        System.out.printf("====================================\n");
        System.out.printf("Backtracking\n");
        Arrays.fill(visited, false);
        path = new ArrayList<>();
        counter = 0;
        minCost = INF;
        backtracking(0); // start from vertex 0
        System.out.printf("TSP cost = %d\n", minCost);
        System.out.printf("====================================\n");
    }
}

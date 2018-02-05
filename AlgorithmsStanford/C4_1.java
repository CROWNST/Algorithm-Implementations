import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

/*
 * This class demonstrates the use of the Floyd-Warshall algorithm, which computes
 * the shortest path for all pairs of vertices in a directed graph. It is slightly
 * modified to give the "shortest shortest" path. The main method takes the 
 * shortest shortest path from each of three graphs and outputs their values. Each
 * graph is a directed graph that has negative edges and possibly negative cycles.
 * */
public class C4_1
{
    public static void main(String[] args) throws FileNotFoundException
    {
        HashMap<Integer, Integer>[] g1 = readGraph("g1.txt");
        HashMap<Integer, Integer>[] g2 = readGraph("g2.txt");
        HashMap<Integer, Integer>[] g3 = readGraph("g3.txt");
        Integer g1ShortestShortestPath = allPairsShortestPath(g1);
        Integer g2ShortestShortestPath = allPairsShortestPath(g2);
        Integer g3ShortestShortestPath = allPairsShortestPath(g3);
        System.out.println(g1ShortestShortestPath);
        System.out.println(g2ShortestShortestPath);
        System.out.println(g3ShortestShortestPath);
    }
    
    //This method reads the given graph and creates an array of HashMaps. The array
    //is indexed by every possible tail. Each array index contains a HashMap whose keys
    //are the heads of edges with the current index as the tail. The values of the HashMap
    //are the edge costs.
    public static HashMap<Integer, Integer>[] readGraph(String fileName) throws FileNotFoundException
    {
        Scanner scan = new Scanner(new File(fileName));
        int numVertices = scan.nextInt();
        int numEdges = scan.nextInt();
        HashMap<Integer, Integer>[] graph = new HashMap[numVertices + 1]; //numVertices + 1 to account for unused index 0
        for (int i = 1; i < graph.length; i++) //Initialize the HashMap in array indices
        {
            graph[i] = new HashMap<Integer, Integer>();
        }
        for (int i = 0; i < numEdges; i++)
        {
            int tail = scan.nextInt();
            int head = scan.nextInt();
            int cost = scan.nextInt();
            graph[tail].put(head, cost);
        }
        scan.close();
        return graph;
    }
    
    //This method first calculates the shortest path for all pairs of vertices. Then,
    //it finds the shortest path out of all the shortest paths and returns it. However,
    //if there is a negative cycle, we just return null.,
    public static Integer allPairsShortestPath(HashMap<Integer, Integer>[] graph)
    {
        ArrayList<int[][]> shortestPaths = new ArrayList<int[][]>(); //An ArrayList to store the updating shortest path data
                                //in the form of two 2D arrays. One will hold the old data, the other will hold the new data
        shortestPaths.add(new int[graph.length][graph.length]);
        shortestPaths.add(new int[graph.length][graph.length]);
        //Initialize the base cases in the first 2D array. Think of 1 through k as the vertices we will allow between
        //any two vertices in the shortest path between them. Therefore, the base case is when k = 0 and no vertices
        //between each pair is allowed.
        for (int i = 1; i < graph.length; i++)
        {
            for (int j = 1; j < graph.length; j++)
            {
                if (i == j)
                {
                    shortestPaths.get(0)[i][j] = 0; //No vertices or edges exist between this pair
                }
                else if (graph[i].containsKey(j))
                {
                    shortestPaths.get(0)[i][j] = graph[i].get(j); //Initialize the shortest path simply to the edge cost,
                    //since no vertices exist between the pair but there is one edge. Remember, k = 0 in the base cases,
                    //which are the entries in this 2D array
                }
                else
                {
                    shortestPaths.get(0)[i][j] = 1000000000; //Initialize to infinity because the existing shortest path
                                                         //would have to use vertices, which is not allowed in base case.
                }
            }
        }
        for (int k = 1; k < graph.length; k++)
        {
            for (int i = 1; i < graph.length; i++)
            {
                for (int j = 1; j < graph.length; j++)
                {
                    //Define an alternate shortest path. If either shortest path in the if statement is 1000000000,
                    //we define the alternate as 1000000000, not as 1000000000 plus the other shortest path. This is
                    //because 1000000000 simulates infinity.
                    int alternate = 1000000000;
                    if (shortestPaths.get(0)[i][k] != 1000000000 && shortestPaths.get(0)[k][j] != 1000000000)
                    {
                        alternate = shortestPaths.get(0)[i][k] + shortestPaths.get(0)[k][j];
                    }
                    shortestPaths.get(1)[i][j] = Math.min(shortestPaths.get(0)[i][j], 
                        alternate); //You either keep the shortest path from the last iteration or you use alternate
                }
            }
            shortestPaths.remove(0); //Remove the old data (for space considerations)
            shortestPaths.add(new int[graph.length][graph.length]); //Add a new 2D array for the next iteration
        }
        for (int a = 1; a < graph.length; a++)
        {
            if (shortestPaths.get(0)[a][a] < 0) //We traverse this diagonal because if even one of the entries in the diagonal
                                        //is negative, that means there was a negative cycle in the graph. This is because
                                        //in the iteration where k = the biggest vertex in the negative cycle containing a 
                                        //and we are filling in index [a][a] for some arbitrary a, it will calculate the shortest
                                        //path as shortestPaths.get(0)[i][k] + shortestPaths.get(0)[k][j], which will return
                                        //a negative number. It will return a negative number for a few reasons.
                                                    //1. shortestPaths.get(0)[i][k] and shortestPaths.get(0)[k][j]
                                                    //can each only contain vertices from 1 through k - 1.
                                                    //2. Because in this special iteration where k = the largest vertex,
                                                    // The two shortestPath calculations will return the two halves of
                                                    //the negative cycle, since all other vertices in the cycle are less than k and
                                                    //are therefore permissible.
                                        //If there is no negative cycle, the diagonal will be filled with 0's.
            {
                return null; //Indicating a negative cycle was detected
            }
        }
        int minPath = shortestPaths.get(0)[1][1]; //Initialize the value of the minimum path for finding the minimum
        for (int i = 1; i < graph.length; i++)
        {
            for (int j = 1; j < graph.length; j++)
            {
                if (shortestPaths.get(0)[i][j] < minPath)
                {
                    minPath = shortestPaths.get(0)[i][j];
                }
            }
        }
        return minPath; //Returning the "shortest shortest" path in the absence of negative cycles
    }
}

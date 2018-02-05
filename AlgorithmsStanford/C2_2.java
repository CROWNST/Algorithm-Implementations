import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * This class demonstrates Dijkstra's Algorithm. Dijkstra's Algorithm finds the
 * shortest path to any node in the given graph, starting from node 1.
 * */
public class C2_2
{
    //This demonstrates Dijkstra's Algorithm
    public static void main(String[] args) throws FileNotFoundException
    {
        int[] shortestPaths = new int[201];
        ArrayList<int[]>[] graph = new ArrayList[201];
        for (int r = 1; r < graph.length; r++)
        {
            graph[r] = new ArrayList<int[]>();
        }
        
        scanData(graph);
        dijkstraAlgorithm(shortestPaths, graph);
        
        //This outputs the shortest paths for a few specific nodes
        System.out.println(shortestPaths[7] + "," + shortestPaths[37] + "," + shortestPaths[59] + ","
        + shortestPaths[82] + "," + shortestPaths[99] + "," + shortestPaths[115] + "," + shortestPaths[133] + ","
        + shortestPaths[165] + "," + shortestPaths[188] + "," + shortestPaths[197]);
    }
    
    //The algorithm itself
    public static void dijkstraAlgorithm(int[] shortestPaths, ArrayList<int[]>[] graph)
    {
        ArrayList<Integer> verticesProcessed = new ArrayList<Integer>();
        verticesProcessed.add(1);
        shortestPaths[1] = 0;
        
        while (verticesProcessed.size() < graph.length - 1)
        {
            int minGreedyScore = 0;
            int newVertex = 0;
            //Initializes minGreedyScore and newVertex
            for (int i = 0; i < verticesProcessed.size(); i++)
            {
                for (int c = 0; c < graph[verticesProcessed.get(i)].size(); c++)
                {
                    if (!verticesProcessed.contains(graph[verticesProcessed.get(i)].get(c)[0]))
                    {
                        minGreedyScore = shortestPaths[verticesProcessed.get(i)] + graph[verticesProcessed.get(i)].get(c)[1];
                        newVertex = graph[verticesProcessed.get(i)].get(c)[0];
                        break;
                    }
                }
            }
            
            //Finds the minimum Dijkstra Greedy Score
            for (int i = 0; i < verticesProcessed.size(); i++)
            {
                for (int c = 0; c < graph[verticesProcessed.get(i)].size(); c++)
                {
                    if (shortestPaths[verticesProcessed.get(i)] + graph[verticesProcessed.get(i)].get(c)[1] < minGreedyScore
                                    && !verticesProcessed.contains(graph[verticesProcessed.get(i)].get(c)[0]))
                    {
                        minGreedyScore = shortestPaths[verticesProcessed.get(i)] + graph[verticesProcessed.get(i)].get(c)[1];
                        newVertex = graph[verticesProcessed.get(i)].get(c)[0];
                    }
                }
            }
            
            //Record the new minimum greedy score and vertex
            verticesProcessed.add(newVertex);
            shortestPaths[newVertex] = minGreedyScore;
        }
    }
    
    //Scans the data into an array of arraylists of arrays. Each of the suffixes of the
    //outer array stands for a node. The arraylist holds arrays, which each contain
    //an adjacent node to the suffix and the edge length
    public static void scanData(ArrayList<int[]>[] graph) throws FileNotFoundException
    {
        Scanner scan1 = new Scanner(new File("dijkstraData.txt"));
        Scanner scan2;
        
        while (scan1.hasNextLine())
        {
            String line = scan1.nextLine();
            scan2 = new Scanner(line);
            int node = scan2.nextInt();
            
            while (scan2.hasNext())
            {
                int[] newEntry = new int[2];
                String adjNodeAndEdge = scan2.next();
                String[] data = adjNodeAndEdge.split(",");
                newEntry[0] = Integer.parseInt(data[0]);
                newEntry[1] = Integer.parseInt(data[1]);
                
                graph[node].add(newEntry);
            }
            scan2.close();
        }
        scan1.close();
    }
}

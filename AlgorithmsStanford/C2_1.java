import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;

/*
 * This program demonstrates Kosaraju's Algorithm, implementing two
 * depth-first searches via a loop. The first depth-first search assigns a
 * finishing time to each node, while the second depth-first search
 * traverses the nodes from largest finishing time to smallest
 * finishing time and assigns a leader to each node, based on each call
 * to DFS. Kosaraju's Algorithm reveals the strongly connected components
 * of the graph, the nodes of which have the same leader.
 */
public class C2_1
{
    private static int t = 0; //finishing time
    private static int s = 0; //leader (finishing time)
    
    //Main method to demonstrate Kosaraju's Algorithm, which utilizes DFSLoop and DFS together
    public static void main(String[] args) throws FileNotFoundException
    {
        ArrayList<Integer>[] adjList = new ArrayList[875715]; //Adjacency list to represent the graph itself
                                                              //Each array suffix marks a node, which is the node that the edges are incident on
        int[][] nodeData = new int[875715][3]; //Information about each node: Each subarray contains the following information in this order:
                                               //0 or 1 for not explored or explored, finishing time, leader finishing time
        for (int r = 1; r < adjList.length; r++)
        {
            adjList[r] = new ArrayList<Integer>();
        }
        
        readEdges(true, adjList); //Reading the edges in REVERSE order first and initializing adjList
        DFSLoop(true, adjList, nodeData); //First DFSLoop pass to assign finishing times
        for (int r = 1; r < adjList.length; r++) //Preparing adjList for a second reading
        {
            adjList[r].clear();
        }
        readEdges(false, adjList); //Reading the edges in the normal, FORWARD order and initializing adjList
        for (int i = 1; i < nodeData.length; i++) //Remarking each node in nodeData as unexplored for the second DFSLoop pass
        {
            nodeData[i][0] = 0;
        }
        DFSLoop(false, adjList, nodeData); //Second DFSLoop pass for assigning leaders
        
        int[] biggestSCCs = getBiggestSCCs(nodeData); //Obtaining the sizes of the five biggest strongly connected components
        for (int i = 0; i < biggestSCCs.length - 1; i++)
        {
            System.out.print(biggestSCCs[i] + ",");
        }
        System.out.println(biggestSCCs[biggestSCCs.length - 1]);
    }
    
    //Reads the edges into adjList, based on boolean parameter reverseEdges
    public static void readEdges(boolean reverseEdges, ArrayList<Integer>[] adjList) throws FileNotFoundException
    {
        Scanner scan = new Scanner(new File("SCC.txt"));
        int firstInt;
        int secondInt;
        
        while (scan.hasNextInt())
        {
            firstInt = scan.nextInt();
            secondInt = scan.nextInt();
            
            if (reverseEdges)
            {
                adjList[secondInt].add(firstInt);
            }
            else
            {
                adjList[firstInt].add(secondInt);
            }
        }
        scan.close();
    }
    
    //Ensures that all nodes in the graph are traversed. Exact execution depends on first or second pass.
    public static void DFSLoop(boolean firstPass, ArrayList<Integer>[] adjList, int[][] nodeData)
    {
        if (firstPass) //To ensure exploration of each node using reversed edges and marking their finishing times
        {
            for (int i = nodeData.length - 1; i >= 1; i--)
            {
                if (nodeData[i][0] == 0)
                {
                   DFS(true, adjList, nodeData, i);
                }
            }
        }
        else //To ensure exploration of each node in the order of largest to smallest finishing time and marking leader finishing time for each node
        {
            int[] ftData = new int[nodeData.length]; //Suffixes stand for finishing times, entries stand for nodes
            for (int i = 1; i < nodeData.length; i++)
            {
                ftData[nodeData[i][1]] = i;
            }
            for (int ft = ftData.length - 1; ft >= 1; ft--)
            {
                if (nodeData[ftData[ft]][0] == 0)
                {
                    s = ft;
                    DFS(false, adjList, nodeData, ftData[ft]);
                }
            }
        }
    }
    
    //Depth-first search. Exact execution and use of global variables depends on boolean firstPass parameter
    public static void DFS(boolean firstPass, ArrayList<Integer>[] adjList, int[][] nodeData, int node)
    {
        nodeData[node][0] = 1; //Mark node as explored
        
        if (!firstPass)
        {
            nodeData[node][2] = s; //Enter the leader for the node
        }
        for (int c = 0; c < adjList[node].size(); c++)
        {
            if (nodeData[adjList[node].get(c)][0] == 0 && firstPass) //Recursive call depends on whether the DFSLoop is in first or second pass
            {
                DFS(true, adjList, nodeData, adjList[node].get(c));
            }
            else if (nodeData[adjList[node].get(c)][0] == 0)
            {
                DFS(false, adjList, nodeData, adjList[node].get(c));
            }
        }
        
        t++;
        nodeData[node][1] = t; //Marking finishing times (Also affects DFS recursive calls in second pass of DFSLoop, but doesn't matter in second pass)
    }
    
    //Method for returning the sizes of the five biggest strongly connected components. The nodes in each SCC
    //have the same leader
    public static int[] getBiggestSCCs(int[][] nodeData)
    {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 1; i < nodeData.length; i++)
        {
            if (map.containsKey(nodeData[i][2]))
            {
                int size = map.get(nodeData[i][2]) + 1;
                map.put(nodeData[i][2], size);
            }
            else
            {
                map.put(nodeData[i][2], 1);
            }
        }
        
        int[] biggestSCCs = new int[5];
        int i = 0;
        while (biggestSCCs[4] == 0 && !map.isEmpty())
        {
            Iterator<Integer> iter = map.keySet().iterator();
            int biggestKey = iter.next();
            int biggest = map.get(biggestKey);
            for (Integer value: map.keySet())
            {
                if (map.get(value).intValue() > biggest)
                {
                    biggestKey = value;
                    biggest = map.get(value);
                }
            }
            map.remove(biggestKey);
            
            biggestSCCs[i] = biggest;
            i++;
        }
        
        return biggestSCCs;
    }
}

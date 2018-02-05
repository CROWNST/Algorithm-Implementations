import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

/*
 * This program demonstrates running Karger's Random Contraction
 * Algorithm at least n^2 times to determine the min cut of a given
 * adjacency list with 1/n probability of failing, n being the number
 * of nodes in the adjacency list. Note that this program runs a bit
 * slow, requiring a wait of a few minutes.
 */
public class C1_4
{
    //Main method demonstrating running the random contraction algorithm many times.
    //It outputs the lowest cut obtained, in hopes of having the min cut.
    public static void main(String[] args) throws FileNotFoundException
    {
        ArrayList<ArrayList<String>> test = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> edgesList = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> edgesListTemp = new ArrayList<ArrayList<String>>();
        int numOfNodes = readAdjacencyList(edgesList);
        for (int i = 0; i < edgesList.size(); i++)
        {
            edgesListTemp.add(new ArrayList<String>());
            for (int a = 0; a < edgesList.get(i).size(); a++)
            {
                edgesListTemp.get(i).add(edgesList.get(i).get(a));
            }
        }
        int minCut = kargersRandomContraction(edgesList, numOfNodes);
        
        for (int i = 1; i < 50000; i++)
        {
            edgesList.clear();
            //Resetting edgesList for the next round
            for (int a = 0; a < edgesListTemp.size(); a++)
            {
                edgesList.add(new ArrayList<String>());
                for (int b = 0; b < edgesListTemp.get(a).size(); b++)
                {
                    edgesList.get(a).add(edgesListTemp.get(a).get(b));
                }
            }
            int temp = kargersRandomContraction(edgesList, numOfNodes);
            if (temp < minCut)
            {
                minCut = temp;
            }
            test = edgesList;
        }
        System.out.println("High Probability Min Cut: " + minCut);
    }
    
    //This is Karger's Random Contraction algorithm
    //Returns a possible min cut
    public static int kargersRandomContraction(ArrayList<ArrayList<String>> edgesList, int numOfNodes)
    {
        while (numOfNodes > 2)
        {
            Random rand = new Random();
            int edgeIndex = rand.nextInt(edgesList.size());
            String firstVertex = edgesList.get(edgeIndex).get(0);
            String secondVertex = edgesList.get(edgeIndex).get(1);
            String combinedVertex = firstVertex + "+" + secondVertex;
            edgesList.remove(edgeIndex);
            
            //This updates edges and removes edges in edgeList
            for (int e = 0; e < edgesList.size(); e++)
            {
                if (edgesList.get(e).get(0).equals(firstVertex) || edgesList.get(e).get(0).equals(secondVertex))
                {
                    edgesList.get(e).set(0, combinedVertex);
                }
                if (edgesList.get(e).get(1).equals(firstVertex) || edgesList.get(e).get(1).equals(secondVertex))
                {
                    edgesList.get(e).set(1, combinedVertex);
                }
                if (edgesList.get(e).get(0).equals(combinedVertex) && edgesList.get(e).get(1).equals(combinedVertex))
                {
                    edgesList.remove(e);
                    e--;
                }
            }
            numOfNodes--;
        }
        return edgesList.size();
    }
    
    //Method to read the adjacency list and input data into the edgesList parameter, assuming
    //the adjacency list does not include parallel arcs
    public static int readAdjacencyList(ArrayList<ArrayList<String>> edgesList) throws FileNotFoundException
    {
        Scanner scan1 = new Scanner(new File("AdjacencyList.txt"));
        Scanner scan2;
        int numOfNodes = 0;
        
        while (scan1.hasNextLine())
        {
            String row = scan1.nextLine();
            scan2 = new Scanner(row);
            int firstVertex = scan2.nextInt();
            numOfNodes++;
            
            while (scan2.hasNextInt())
            {
                int vertex = scan2.nextInt();
                boolean addNewEdge = true;
                for (int i = 0; i < edgesList.size(); i++)
                {
                    if (edgesList.get(i).contains("" + firstVertex) && edgesList.get(i).contains("" + vertex))
                    {
                        addNewEdge = false;
                    }
                }
                if (addNewEdge)
                {
                    edgesList.add(new ArrayList<String>());
                    edgesList.get(edgesList.size() - 1).add("" + firstVertex);
                    edgesList.get(edgesList.size() - 1).add("" + vertex);
                }
            }
            scan2.close();
        }
        scan1.close();
        
        return numOfNodes;
    }
}

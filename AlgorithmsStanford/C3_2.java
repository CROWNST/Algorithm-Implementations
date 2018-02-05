import java.util.Comparator;
import java.util.PriorityQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/*
 * This class displays two programs. The first program asks for the
 * value of the max spacing after reducing the number of clusters
 * to the specified amount, which in this case is 4. The first program
 * does not take into account duplicate nodes. The second
 * program computes the maximum number of clusters for a max spacing
 * of 3. The second program does not take into account duplicate nodes.
 * Distances in the second program are equal to the Hamming
 * distance between two binary nodes, which is equal to the number
 * of differing bits. The second program takes over an hour to 
 * compute.
 * */
public class C3_2
{
    //Main method to run both programs
    public static void main(String[] args) throws FileNotFoundException
    {
        int k = 4; //number of clusters for first program
        PriorityQueue<Integer[]> distances = readData(); //returns distances in order from least to greatest
        int[] unionFind = initUnionFind(); //returns a union find of nodes to their leaders
        ArrayList<Integer> leaderCounts = initLeaderCounts(); //arraylist containing frequency for each leader
        
        int maxSpacing = kClusteringAlgorithm(k, distances, unionFind, leaderCounts); //k-clustering algorithm that
                                                                                      //produces the max spacing for k clusters
        System.out.println(maxSpacing); //result of program 1
        ////////////////////////////////////////////////////////////////////////////////
        Hashtable<Integer, Integer> unionFindV2 = new Hashtable<Integer, Integer>(); //union find for nodes of
                                                                                     //second program. Nodes were originally
                                                                                     //binary, but converted into Integers
        Hashtable<Integer, Integer> leaderCountsV2 = new Hashtable<Integer, Integer>(); //leader count for nodes of the second
                                                                                        //program. Nodes were originally
                                                                                        //binary, but converted into Integers
        ArrayList<Integer> numClustersAndBitsPerNode = readData(unionFindV2, leaderCountsV2); //The method in this line initializes
                                                                                        //unionFindV2 and leaderCountsV2, and returns
                                                                                        //an arraylist containing the number of
                                                                                        //clusters originally present and the
                                                                                        //number of bits per node
        int finalNumClusters = kClusteringAlgorithm(unionFindV2, leaderCountsV2, numClustersAndBitsPerNode.get(0),
            numClustersAndBitsPerNode.get(1)); //calculates the final number of clusters for a max Hamming distance of 3
        System.out.println(finalNumClusters); //outputs the result
    }
    
    //first program that outputs the max distance for k desired clusters
    public static int kClusteringAlgorithm(int k, PriorityQueue<Integer[]> distances, int[] unionFind, ArrayList<Integer> leaderCounts)
    {
        int numClusters = 500; //Number of clusters we begin with (from clustering1.txt)
        int maxSpacing = 0; //Variable for max spacing
        
        while(numClusters > k)
        {
            Integer[] distance = distances.remove(); //distance will contain the two nodes and the distance
            
            if (!sameGroup(distance[0], distance[1], unionFind)) //if they aren't already in the same group
            {
                //determine which node has the bigger group
                int aLeader = unionFind[distance[0]];
                int bLeader = unionFind[distance[1]];
                
                if (leaderCounts.get(aLeader) > leaderCounts.get(bLeader))
                {
                    updateUnionFind(distance[1], distance[0], unionFind); //update the unionFind so that all nodes with bLeader
                                                                          //will have a new leader of aLeader
                }
                else
                {
                    updateUnionFind(distance[0], distance[1], unionFind); //update the unionFind so that all nodes with aLeader
                                                                          //will have a new leader of bLeader
                }
                updateLeaderCounts(aLeader, bLeader, leaderCounts); //update the leaderCounts so that each leader has the
                                                                    //correct frequency
                
                numClusters--; //decrement the number of clusters since we combined two clusters to form one
            }
        }
        
        //After we have reached k clusters
        Integer[] distance = distances.remove();
        while (sameGroup(distance[0], distance[1], unionFind)) //while the two nodes in the distance are in the same cluster
        {
            distance = distances.remove(); //get the next smallest distance
        }
        maxSpacing = distance[2];
        return maxSpacing; //the final max spacing result
    }
    
    //This method reads the distances and returns a PriorityQueue<Integer[]>, where distances are in order from least to greatest
    public static PriorityQueue<Integer[]> readData() throws FileNotFoundException
    {
        Scanner scan = new Scanner(new File("clustering1.txt"));
        PriorityQueue<Integer[]> data = new PriorityQueue<Integer[]>(new SortDistances()); //Creates a new PriorityQueue based on
                                                                                           //the SortDistances Comparator, provided below
        scan.nextInt(); //Getting past the listed # of nodes, which I hardcode myself
        
        while (scan.hasNextInt())
        {
            int nodeOne = scan.nextInt();
            int nodeTwo = scan.nextInt();
            int distance = scan.nextInt();
            Integer[] x = {nodeOne, nodeTwo, distance}; //an entry contains both nodes and the distance between them
            data.add(x);
        }
        
        scan.close();
        return data;
    }
    
    //This method initializes the union find data structure
    public static int[] initUnionFind()
    {
        int[] unionFind = new int[501]; //Hardcoded to contain 500 nodes (each node is its own index, and the nodes
                                        //start from 1, which is why we have 501 slots
        for (int i = 1; i < unionFind.length; i++)
        {
            unionFind[i] = i; //Setting the leader of each node to itself
        }
        
        return unionFind;
    }
    
    public static ArrayList<Integer> initLeaderCounts()
    {
        ArrayList<Integer> leaderCounts = new ArrayList<Integer>(501); //The nodes start at 1 and there are 500 nodes,
                                                                       //which is why capacity is set to 501
        leaderCounts.add(0); //Useless extra data to start the arraylist at index 1
        for (int i = 1; i < 501; i++)
        {
            leaderCounts.add(1); //Each leader(index) has a frequency of 1 at the start (every node's leader is itself at the start)
        }
        
        return leaderCounts;
    }
    
    //This method updates the leader counts given two leaders and the leaderCounts
    public static void updateLeaderCounts(int aLeader, int bLeader, ArrayList<Integer> leaderCounts)
    {
        if (leaderCounts.get(aLeader) > leaderCounts.get(bLeader)) //This check is made so that the smaller leader count gets
                                                                   //merged into the larger one
        {
            leaderCounts.set(aLeader, leaderCounts.get(aLeader) + leaderCounts.get(bLeader)); //aLeader contains both leader counts combined now
            leaderCounts.set(bLeader, 0); //bLeader contains nothing now, since its count was moved to aLeader
        }
        else //Applied when bLeader count >= aLeader count
        {
            leaderCounts.set(bLeader, leaderCounts.get(aLeader) + leaderCounts.get(bLeader));
            leaderCounts.set(aLeader, 0);
        }
    }
    
    //This method updates the unionFind so that all nodes with a's leader will have b's leader as their new leader
    public static void updateUnionFind(int a, int b, int[] unionFind)
    {
        int aLeader = unionFind[a];
        int bLeader = unionFind[b];
        for (int i = 1; i < unionFind.length; i++)
        {
            if (unionFind[i] == aLeader)
            {
                unionFind[i] = bLeader;
            }
        }
    }
    
    //Returns whether two nodes are in the same group
    public static boolean sameGroup(int a, int b, int[] unionFind)
    {
        return unionFind[a] == unionFind[b];
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //This is the second program, which outputs the number of clusters after all nodes whose distances between each other is less than
    //3 are removed
    public static int kClusteringAlgorithm(Hashtable<Integer, Integer> unionFind, Hashtable<Integer, Integer> leaderCount,
        int numClusters, int bitsPerNode)
    {
        Iterator<Integer> unionIter = unionFind.keySet().iterator();
        
        while (unionIter.hasNext())
        {
            Integer node = unionIter.next();
            int possibleNode;
            
            //We are trying to generate ints which we can xor with node to get possible nodes to check. For example,
            //if node is 1011, then its int representation is 11. If we wish to find a node that is 1 Hamming distance away,
            //we would xor 1011 with 1000, 0100, 0010, and 0001, which are 8, 4, 2, and 1.
            for (int i = 0; i < bitsPerNode; i++)
            {
                for (int j = i + 1; j < bitsPerNode; j++)
                {
                    if (j == i + 1) //This is used to generate nodes that are 1 Hamming distance away from node
                    {
                        possibleNode = node ^ (int)(Math.pow(2, i)); 
                        if (unionFind.keySet().contains(possibleNode) && !unionFind.get(node).equals(unionFind.get(possibleNode)))
                        {
                            processNodesAndUpdate(node, possibleNode, unionFind, leaderCount);
                            numClusters--;
                        }
                    }
                    
                    //This is used to generate nodes that are 2 Hamming distance away node
                    possibleNode = node ^ (int)(Math.pow(2, i) + Math.pow(2, j));
                    if (unionFind.keySet().contains(possibleNode) && !unionFind.get(node).equals(unionFind.get(possibleNode)))
                    {
                        processNodesAndUpdate(node, possibleNode, unionFind, leaderCount);
                        numClusters--;
                    }
                }
            }
            
            //This last piece is to take care of the case in which the left-most bit is different, a case not checked in the
            //double for loop
            possibleNode = node ^ (int)(Math.pow(2, bitsPerNode - 1));
            if (unionFind.keySet().contains(possibleNode) && !unionFind.get(node).equals(unionFind.get(possibleNode)))
            {
                processNodesAndUpdate(node, possibleNode, unionFind, leaderCount);
                numClusters--;
            }
        }
        
        return numClusters;
    }
    
    //This method initializes unionFind so that it shows the leaders to all unique nodes, initializes leaderCount
    //So that duplicates will share the same leader, reduces the number of clusters due to grouping duplicates, and
    //returns an ArrayList<Integer> containing the number of clusters after reduction and the number of bits per node.
    public static ArrayList<Integer> readData(Hashtable<Integer, Integer> unionFind, Hashtable<Integer, Integer> leaderCount) throws FileNotFoundException
    {
        Scanner scan = new Scanner(new File("clustering_big.txt"));
        int numNodes = scan.nextInt();
        int numClusters = numNodes; //The number of clusters is originally the number of nodes
        int bitsPerNode = scan.nextInt();
        
        for (int n = 0; n < numNodes; n++)
        {
            //The next few lines are for converting the binary form of the nodes in the file to int form
            int exp = bitsPerNode - 1;
            double x = Math.pow(2, exp);
            int node = 0;
            
            for (int i = 0; i < bitsPerNode; i++)
            {
                int y = scan.nextInt();
                node += y * x;
                exp--;
                x = Math.pow(2, exp);
            }
            
            unionFind.put(node, node); //Each node key only appears once due to Hashtable function, regardless of duplicates
            //Adds duplicates to leaderCount
            if (leaderCount.keySet().contains(node))
            {
                leaderCount.put(node, leaderCount.get(node) + 1);
                numClusters--; //Must subtract numClusters by 1 because a duplicate is merged into a larger group
            }
            else
            {
                leaderCount.put(node, 1);
            }
        }
        
        scan.close();
        ArrayList<Integer> numClustersAndBitsPerNode = new ArrayList<Integer>();
        numClustersAndBitsPerNode.add(numClusters);
        numClustersAndBitsPerNode.add(bitsPerNode);
        return numClustersAndBitsPerNode;
    }
    
    //Processes the nodes and updates unionFind and leaderCount, but not numClusters
    public static void processNodesAndUpdate(Integer a, Integer b, Hashtable<Integer, Integer> unionFind, 
        Hashtable<Integer, Integer> leaderCount)
    {
        Integer aLeader = unionFind.get(a);
        Integer bLeader = unionFind.get(b);
        
        //Update unionFind and leaderCount differently depending on the leader counts of each node
        if (leaderCount.get(aLeader) > leaderCount.get(bLeader))
        {
            Iterator<Integer> unionIter = unionFind.keySet().iterator();
            
            while (unionIter.hasNext())
            {
                Integer unionKey = unionIter.next();
                if (unionFind.get(unionKey).equals(bLeader))
                {
                    unionFind.put(unionKey, aLeader);
                }
            }
            
            leaderCount.put(aLeader, leaderCount.get(aLeader) + leaderCount.get(bLeader));
            leaderCount.put(bLeader, 0);
        }
        else
        {
            Iterator<Integer> unionIter = unionFind.keySet().iterator();
            
            while (unionIter.hasNext())
            {
                Integer unionKey = unionIter.next();
                if (unionFind.get(unionKey).equals(aLeader))
                {
                    unionFind.put(unionKey, bLeader);
                }
            }
            
            leaderCount.put(bLeader, leaderCount.get(bLeader) + leaderCount.get(aLeader));
            leaderCount.put(aLeader, 0);
        }
    }
}

/*
 * This class is used by the first program to sort the entries from smallest to largest distance
 * */
class SortDistances implements Comparator<Integer[]>
{
    public int compare(Integer[] a, Integer[] b)
    {
        return a[2].compareTo(b[2]); //Index 2 contains the distance
    }
}

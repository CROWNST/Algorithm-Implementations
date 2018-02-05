import java.util.Comparator;
import java.util.PriorityQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.TreeMap;

/*
 * This class shows two programs: the recursive Huffman's Algorithm and
 * a dynamic programming approach to the maximum weight independent set
 * of a path graph problem. The Huffman's Algorithm will display the 
 * minimum length of an encoded letter based on the letters' weights
 * given as input data. You could similarly find the maximum encoded
 * length as well. The dynamic programming solution for the max weight
 * independent set of a path graph problem will find the vertices from
 * the input data that are included in the max weight indep. set. Then,
 * upon request of the original assignment, it will identify whether or
 * not certain vertices are included.
 * */
public class C3_3
{
    static int minCodeLength; //Global variable for keeping track of the min. coding length for Huffman's Algorithm
    
    //Main method to run both algorithms
    public static void main(String[] args) throws FileNotFoundException
    {
        PriorityQueue<Integer[]> letters = readHuffmanData(); //A priority queue that is sorted
                                                        //from smallest weight to highest weight
        huffmanAlgorithm(letters); //The recursive algorithm runs, which will update the global variable
        System.out.println(minCodeLength);
        System.out.println();
        
        ArrayList<Integer> verticesInMaxWeightIndepSet = maxWeightIndepSetAlgorithm(); //Contains all the vertices
                                                                   //that are included in the max weight indep. set
        TreeMap<Integer, String> testVertices = new TreeMap<Integer, String>(); //This TreeMap will associate a "1"
                                                 //with all the test vertices that are contained in the final answer
        testVertices.put(1, "0");
        testVertices.put(2, "0");
        testVertices.put(3, "0");
        testVertices.put(4, "0");
        testVertices.put(17, "0");
        testVertices.put(117, "0");
        testVertices.put(517, "0");
        testVertices.put(997, "0");
        for (int i = 0; i < verticesInMaxWeightIndepSet.size(); i++)
        {
            if (testVertices.keySet().contains(verticesInMaxWeightIndepSet.get(i)))
            {
                testVertices.put(verticesInMaxWeightIndepSet.get(i), "1");
            }
        }
        for (Integer a : testVertices.keySet())
        {
            System.out.print(testVertices.get(a));
        }
        System.out.println();
    }
    
    //This method reads the Huffman data and returns a priority queue that is sorted
    //based on the letters' weights.
    public static PriorityQueue<Integer[]> readHuffmanData() throws FileNotFoundException
    {
        PriorityQueue<Integer[]> letters = new PriorityQueue<Integer[]>(new SortLetters());
        Scanner scan = new Scanner(new File("huffman.txt"));
        int numLetters = scan.nextInt();
        for (int i = 0; i < numLetters; i++)
        {
            Integer[] newLetter = new Integer[3]; //Each letter is represented as an Integer[], where index 0
            //contains the letter's "identification," which isn't used later on, index 1 contains the weight of the
            //letter, and index 2 contains the min # of merges that the vertex has. These "letters" are
            //really vertices in a tree.
            newLetter[0] = i + 1;
            newLetter[1] = scan.nextInt();
            newLetter[2] = 0;
            letters.add(newLetter);
        }
        scan.close();
        return letters;
    }
    
    //This method runs the Huffman Algorithm. It is actually unnecessary to return the PriorityQueue
    public static PriorityQueue<Integer[]> huffmanAlgorithm(PriorityQueue<Integer[]> letters)
    {
        //Base case
        if (letters.size() == 1)
        {
            return letters;
        }
        else
        {
            Integer[] smallest = letters.remove(); //Least weight letter
            Integer[] nextSmallest = letters.remove(); //Next smallest letter
            Integer[] mergedLetters = new Integer[3]; //A vertex to represent the combined letters
            mergedLetters[0] = smallest[0] + nextSmallest[0]; //Identification (not used)
            mergedLetters[1] = smallest[1] + nextSmallest[1]; //Weight is added
            mergedLetters[2] = Math.min(smallest[2], nextSmallest[2]) + 1; //Min # of merges for this combined vertex
            minCodeLength = mergedLetters[2]; //Update global variable
            letters.remove(smallest); //Remove both smallest and nextSmallest, adding mergedLetters to letters
            letters.remove(nextSmallest);
            letters.add(mergedLetters);
            return huffmanAlgorithm(letters); //Repeat the procedure (don't really need to return a PriorityQueue)
        }
    }
    
    //This method returns the vertices that are contained in the max weight indep. set of the inputted path graph
    public static ArrayList<Integer> maxWeightIndepSetAlgorithm() throws FileNotFoundException
    {
        ArrayList<Integer> verticesInMaxWeightIndepSet = new ArrayList<Integer>(); //Will store the vertices for the answer
        Scanner scan = new Scanner(new File("mwis.txt"));
        int numVertices = scan.nextInt();
        int[] mwis = new int[numVertices + 1]; //An array to hold the optimal solutions to smaller subproblems.
                                               //Plus one because we must include the subproblem of 0 vertices.
        mwis[0] = 0;
        mwis[1] = scan.nextInt(); //Initializations for subproblems of 0 and 1 vertices, respectively.
        for (int i = 2; i < mwis.length; i++)
        {
            int nextVertex = scan.nextInt(); //Scan in the next vertex
            mwis[i] = Math.max(mwis[i - 1], mwis[i - 2] + nextVertex); //The optimal solution for subproblem i,
                                                                       //which can only have two possibilities
        }
        scan.close();
        //We begin to rediscover what vertices are included in the max weight indep. set
        int i = mwis.length - 1;
        int curWeight = mwis[i];
        while (i >= 1)
        {
            if (i == 1) //If you reach subproblem of size 1 vertex, include its optimal solution, which is its own weight
            {
                verticesInMaxWeightIndepSet.add(1);
                break;
            }
            else if (curWeight > mwis[i - 1]) //If your current weight is greater than the opt. soln. of the first
                                     //case, the second case was used; therefore you include the vertex in the set.
            {
                verticesInMaxWeightIndepSet.add(i);
                i = i - 2;
            }
            else //If your current weight is greater than the opt. solution of the second case, then the first case
                 //was used; you do not include the vertex in the set. This will also handle cases in which the
                 //opt. solns. of both cases are equal.
            {
                i--;
            }
            curWeight = mwis[i];
        }
        return verticesInMaxWeightIndepSet;
    }
}

//This class will be used by the priority queue to sort letters in increasing order of weight
class SortLetters implements Comparator<Integer[]>
{
    public int compare(Integer[] a, Integer[] b)
    {
        return a[1].compareTo(b[1]);
    }
}

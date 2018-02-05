import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;

/*
 * This program demonstrates two completely different greedy
 * algorithms: scheduling to minimize the sum of the weighted
 * computing times of jobs in scheduling, and Prim's Minimum
 * Spanning Tree greedy algorithm. The first algorithm will use
 * the optimal greedy solution for ordering jobs, which is 
 * decreasing order of weight/length. The second algorithm uses
 * a heap to keep track of the minimum edge, which might or might
 * not be crossing the frontier.
 * */
public class C3_1
{
    public static void main(String[] args) throws FileNotFoundException
    {
        PriorityQueue<Double[]> jobs = readData();
        System.out.println(sumWeightedCompTimes(jobs)); //First algorithm
        
        PriorityQueue<Integer[]> edges = readEdges();
        System.out.println(primMSTAlgorithm(edges)); //Second algorithm
    }
    
    //This method reads in the jobs, representing each job as a Double[]
    //with the index 0 containing the weight, index 1 containing the length,
    //and index 3 containing the optimal greedy comparison metric of weight/length.
    //Jobs with a higher ratio are earlier in the queue.
    public static PriorityQueue<Double[]> readData() throws FileNotFoundException
    {
        PriorityQueue<Double[]> jobs = new PriorityQueue<Double[]>(new SortJobs());
        Scanner scan = new Scanner(new File("jobs.txt"));
        int data = scan.nextInt();
        for (int i = 0; i < data; i++)
        {
            Double[] job = new Double[3];
            job[0] = new Double(scan.nextInt());
            job[1] = new Double(scan.nextInt());
            job[2] = job[0] / job[1];
            jobs.add(job);
        }
        scan.close();
        return jobs;
    }
    
    //Computes the sum of the weighted computation times.
    //Each weighted computation time is defined as the weight of the
    //job * (completion time).
    //The completion time of a job is its length + the completion time
    //of the job before it.
    public static String sumWeightedCompTimes(PriorityQueue<Double[]> jobs)
    {
        BigInteger time = new BigInteger("0");
        BigInteger sum = new BigInteger("0");
        int numJobs = jobs.size();
        for (int i = 0; i < numJobs; i++)
        {
            Double[] job = jobs.remove();
            time = time.add(new BigInteger(String.valueOf(job[1].intValue())));
            sum = sum.add(time.multiply(new BigInteger(String.valueOf(job[0].intValue()))));
        }
        return sum.toString();
    }
    
    //This method demonstrates Prim's MST Algorithm.
    public static int primMSTAlgorithm(PriorityQueue<Integer[]> edges) throws FileNotFoundException
    {
        Scanner scan = new Scanner(new File("edges.txt"));
        int numNodes = scan.nextInt() - 1; //We're planning on starting with one vertex
                                           //already in the swallowedVertices ArrayList<Integer>
        scan.close();
        ArrayList<Integer> swallowedVertices = new ArrayList<Integer>(); //Vertices already in the MST thus far
        swallowedVertices.add(edges.peek()[0]); //Starting with one vertex already in the ArrayList<Integer>
        ArrayList<Integer[]> temp = new ArrayList<Integer[]>(); //To store edges that do not cross the frontier
                                                                //(in other words, do not have one vertex in swallowedVertices
                                                                //and one not) as we remove minimum edges from edges PriorityQueue
        int sumOfMST = 0; //To store the sum of the MST
        
        while(numNodes > 0) //While the number of nodes outside swallowedVertices is greater than one
        {
            numNodes--;
            while(swallowedVertices.contains(edges.peek()[0]) && swallowedVertices.contains(edges.peek()[1])
                            || !swallowedVertices.contains(edges.peek()[0]) && !swallowedVertices.contains(edges.peek()[1]))
                //While the minimum edge does not cross the frontier
            {
                temp.add(edges.remove()); //Store edges that don't cross the frontier. These edges will be put back later.
            }
            
            if (swallowedVertices.contains(edges.peek()[0])) //After we select the min edge that crosses the frontier,
                                                             //we place its vertex that is not in swallowedVertices into
                                                             //swallowedVertices.
            {
                swallowedVertices.add(edges.peek()[1]);
            }
            else
            {
                swallowedVertices.add(edges.peek()[0]);
            }
            sumOfMST += edges.peek()[2]; //Adding its edge length to sum
            edges.remove(); //Removing the edge, since it is now used
            
            for (int i = 0; i < temp.size(); i++) //Put back the edges stored in temp into edges PriorityQueue
            {
                edges.add(temp.get(i));
            }
            temp.clear(); //Clear temp for the next iteration
        }
        
        return sumOfMST;
    }
    
    //Reads the edges, representing each edge as an Integer[]. Both index 0 and 1 will
    //contain a vertex of the edge, and index 2 will contain the edge cost.
    public static PriorityQueue<Integer[]> readEdges() throws FileNotFoundException
    {
        PriorityQueue<Integer[]> edges = new PriorityQueue<Integer[]>(new SortEdges());
        Scanner scan = new Scanner(new File("edges.txt"));
        scan.nextInt();
        int numEdges = scan.nextInt();
        for (int i = 0; i < numEdges; i++)
        {
            Integer[] edge = new Integer[3];
            edge[0] = scan.nextInt();
            edge[1] = scan.nextInt();
            edge[2] = scan.nextInt();
            edges.add(edge);
        }
        scan.close();
        return edges;
    }
}

/*
 * This comparator class is used by the scheduling algorithm's PriorityQueue,
 * which stores jobs with a higher ratio of weight/length earlier than jobs
 * with a smaller ratio.
 * */
class SortJobs implements Comparator<Double[]>
{
    public int compare(Double[] a, Double[] b)
    {
        if (a[2].equals(b[2]))
        {
            if (a[0].doubleValue() > b[0].doubleValue())
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
        return -1 * a[2].compareTo(b[2]);
    }
}

/*
 * This comparator class is used by the MST Algorithm's PriorityQueue to sort
 * the edges in ascending order of edge length
 * */
class SortEdges implements Comparator<Integer[]>
{
    public int compare(Integer[] a, Integer[] b)
    {
        return a[2].compareTo(b[2]);
    }
}

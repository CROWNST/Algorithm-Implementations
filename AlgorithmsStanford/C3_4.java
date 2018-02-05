import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * This class demonstrates the knapsack algorithm. The first knapsack algorithm
 * uses a 2D array to keep track of all the subproblems. Each linear array within
 * the 2D array represents an additional vertex, and they each span the possible
 * weight capacities of subproblems. Each subproblem is represented as a location 
 * within this 2D array. The second knapsack algorithm does not use a 2D array,
 * so it is more space and time-efficient. It only uses two arrays, one to keep
 * track of subproblems with a current number of vertices, and the other to keep
 * track of subproblems with one less number of vertices. The two programs output
 * the optimal values of two different knapsack instances. The first program
 * would let you identify the vertices in the optimal solution, since it records
 * all the information. The second algorithm would not.
 * */
public class C3_4
{
    //This is the main method to output the optimal total values for both the first and
    //second knapsack algorithms
    public static void main(String[] args) throws FileNotFoundException
    {
        System.out.println(naiveKnapSackAlgorithm());
        
        System.out.println(knapSackAlgorithm());
    }
    
    //This is the first knapsack algorithm, which records all information in a 2D array
    public static int naiveKnapSackAlgorithm() throws FileNotFoundException
    {
        Scanner scan = new Scanner(new File("knapsack1.txt"));
        int capacity = scan.nextInt(); //Capacity for the original problem
        int numVertices = scan.nextInt(); //Number of vertices in the original problem
        int[][] subProblems = new int[numVertices + 1][capacity + 1]; //Creating the 2D array table
        for (int w = 0; w < subProblems[0].length; w++)
        {
            subProblems[0][w] = 0; //Set the optimal solution to these subproblems to 0, since they represent no vertices
        }
        for (int v = 1; v < subProblems.length; v++) //Iterate along the linear arrays within the 2D array
        {
            int vertexValue = scan.nextInt(); //Record the vertex value and weight for this linear array
            int vertexWeight = scan.nextInt();
            for (int w = 0; w < subProblems[0].length; w++) //Calculate optimal solutions to all the subproblems in
                                        //the current linear array
            {
                if (w - vertexWeight >= 0) //Checking to make sure that the second possible optimal solution to
                //the current subproblem is feasible. If not, then inherit the optimal solution to the subproblem
                //with one less vertex but with the same capacity
                {
                    subProblems[v][w] = Math.max(subProblems[v - 1][w], subProblems[v - 1][w - vertexWeight] + vertexValue);
                }
                else
                {
                    subProblems[v][w] = subProblems[v - 1][w];
                }
            }
        }
        scan.close();
        return subProblems[numVertices][capacity]; //Return the answer to the original problem
    }
    
    //This method is the knapsack algorithm that only uses two arrays for necessary information, which would
    //not allow the programmer to identify the vertices of the optimal solution, only the optimal value. However,
    //this implementation saves space and time. It is slightly slower than the first method only because it has
    //a higher volume of vertices to process.
    public static int knapSackAlgorithm() throws FileNotFoundException
    {
        Scanner scan = new Scanner(new File("knapsack_big.txt"));
        int capacity = scan.nextInt(); //Capacity of the original problem
        int numVertices = scan.nextInt(); //Number of vertices of the original problem
        int[] prevSubProblems = new int[capacity + 1]; //Contains the linear array to contain
                                                       //the subproblems with one less vertex
        for (int w = 0; w < prevSubProblems.length; w++)
        {
            prevSubProblems[w] = 0; //Because the optimal solution to subproblems with 0 vertices is 0
        }
        int[] currentSubProblems = new int[capacity + 1]; //Linear array to contain optimal solutions to
                                                          //subproblems with the current number of vertices
        for (int v = 1; v <= numVertices; v++) //Calculate subproblems for each vertex and its associated linear array
        {
            int vertexValue = scan.nextInt(); //The value and weight of the current vertex
            int vertexWeight = scan.nextInt();
            for (int w = 0; w < currentSubProblems.length; w++)
            {
                if (w - vertexWeight >= 0) //if w - vertexWeight < 0, the second option is not possible
                {
                    currentSubProblems[w] = Math.max(prevSubProblems[w], prevSubProblems[w - vertexWeight] + vertexValue);
                }
                else
                {
                    currentSubProblems[w] = prevSubProblems[w];
                }
            }
            for (int w = 0; w < prevSubProblems.length; w++)
            {
                prevSubProblems[w] = currentSubProblems[w]; //Making sure to update prevSubProblems for the next round's vertex
            }
        }
        scan.close();
        return currentSubProblems[capacity]; //Return the final optimal value
    }
}

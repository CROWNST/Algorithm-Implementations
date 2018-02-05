import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * This class essentially solves the Median Maintenance problem
 * and keeps track of the running median using two heaps for
 * logarithmic time in terms of the step, i. In each step, we
 * add a new entry to be processed.
 * */
public class C2_3
{
    //Main method to demonstrate the median maintenance algorithm
    public static void main(String[] args) throws FileNotFoundException
    {
        int[] medians = medianMaintenance();
        int sum = 0;
        for (int i = 0; i < medians.length; i++)
        {
            sum += medians[i];
        }
        System.out.println(sum % 10000);
    }
    
    //Median maintenance algorithm itself, using heaps
    public static int[] medianMaintenance() throws FileNotFoundException
    {
        int[] medians = new int[10000];
        int i = 0;
        Comparator<Integer> lowComparator = new SortIntegers(); //Comparator for making the largest element the
                                                                //highest priority in the lowHeap
        PriorityQueue<Integer> lowHeap = new PriorityQueue<Integer>(lowComparator);
        PriorityQueue<Integer> highHeap = new PriorityQueue<Integer>();
        Scanner scan = new Scanner(new File("Median.txt"));
        
        while (scan.hasNextInt())
        {
            Integer num = scan.nextInt();
            
            if (lowHeap.isEmpty() && highHeap.isEmpty())
            {
                lowHeap.add(num);
            }
            else
            {
                if (num.intValue() < lowHeap.peek().intValue())
                {
                    lowHeap.add(num);
                }
                else
                {
                    highHeap.add(num);
                }
            }
            
            if (lowHeap.size() - highHeap.size() == 2)
            {
                highHeap.add(lowHeap.remove());
            }
            else if (highHeap.size() - lowHeap.size() == 2)
            {
                lowHeap.add(highHeap.remove());
            }
            
            if (lowHeap.size() > highHeap.size())
            {
                medians[i] = lowHeap.peek();
            }
            else if (highHeap.size() > lowHeap.size())
            {
                medians[i] = highHeap.peek();
            }
            else
            {
                medians[i] = lowHeap.peek();
            }
            i++;
        }
        
        return medians;
    }
}

//Private Comparator class for organizing entries within lowHeap
class SortIntegers implements Comparator<Integer>
{
    public int compare(Integer a, Integer b)
    {
        return -1 * a.compareTo(b);
    }
}
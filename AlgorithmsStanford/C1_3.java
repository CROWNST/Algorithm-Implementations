import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//This is the Quicksort Algorithm using one of the existing
//partition subroutines along with optimal pivot selection.
public class C1_3
{
    //This is the main method to demonstrate the Quicksort Algorithm
    public static void main(String[] args) throws FileNotFoundException
    {
        int[] integerArray = getIntArray();
        int numOfComparisons = quickSort(integerArray, 0, integerArray.length - 1);
        System.out.print("Number of comparisons: " + numOfComparisons);
    }
    
    //This is the Quicksort algorithm, implemented with one of the existing
    //partition subroutines, and uses optimal pivot selection.
    public static int quickSort(int[] integerArray, int l, int r)
    {   
        int numOfComparisons = 0;
        
        if (r - l + 1 <= 1) //Base case
        {
            return 0;
        }
        else
        {
            int firstElementIndex = l;
            int lastElementIndex = r;
            int middleElementIndex;
            if ((r - l + 1) % 2 == 0)
            {
                middleElementIndex = ((r - l + 1) / 2 - 1) + l;
            }
            else
            {
                middleElementIndex = (r - l + 1) / 2 + l;
            }
            int medianIndex = median(integerArray, firstElementIndex, middleElementIndex, lastElementIndex); //Optimal pivot index
            int pivotLocation = partition(medianIndex, integerArray, l, r); //partition subroutine, returning pivot location for further recursion
            
            int numOfComparisonsFirstHalf = quickSort(integerArray, l, pivotLocation - 1); //Recursive call that also return number of comparisons in subproblems
            int numOfComparisonsSecondHalf = quickSort(integerArray, pivotLocation + 1, r); //See directly above
            numOfComparisons += (pivotLocation - 1 - l + r - (pivotLocation + 1) + 2) + numOfComparisonsFirstHalf
                            + numOfComparisonsSecondHalf; //Total number of comparisons for this array. The pivot in this call is compared to all
                                                          //other elements in the problem's range of the array
            
            return numOfComparisons;
        }
    }
    
    //Partitions the array in the range according to pivot index
    public static int partition(int pivotIndex, int[] integerArray, int l, int r)
    {
        if (pivotIndex != l) //Move the pivot element to the correct location
        {
            swap(integerArray, pivotIndex, l);
        }
        int i = l + 1;
        
        for (int j = l + 1; j <= r; j++)
        {
            if (integerArray[j] < integerArray[l])
            {
                swap(integerArray, j, i);
                i++;
            }
        }
        
        swap(integerArray, l, i - 1); //Move the pivot to its rightful position
        return i - 1;
    }
    
    public static void swap(int[] integerArray, int j, int i)
    {
        int temp = integerArray[j];
        integerArray[j] = integerArray[i];
        integerArray[i] = temp;
    }
    
    //Returns the index of the median; the parameters are indexes
    public static int median(int[] integerArray, int a, int b, int c)
    {
        int elemAtA = integerArray[a];
        int elemAtB = integerArray[b];
        int elemAtC = integerArray[c];
        
        if (elemAtA >= elemAtB && elemAtB >= elemAtC || elemAtC >= elemAtB && elemAtB >= elemAtA)
        {
            return b;
        }
        else if (elemAtB >= elemAtA && elemAtA >= elemAtC || elemAtC >= elemAtA && elemAtA >= elemAtB)
        {
            return a;
        }
        else
        {
            return c;
        }
    }
    
    //Returns an array with the integers of IntegerArray2.txt
    public static int[] getIntArray() throws FileNotFoundException
    {
        int[] integerArray = new int[10000];
        File file = new File("IntegerArray2.txt");
        Scanner scan = new Scanner(file);
        
        int i = 0;
        while (scan.hasNextInt())
        {
            integerArray[i] = scan.nextInt();
            i++;
        }
        scan.close();
        
        return integerArray;
    }
}

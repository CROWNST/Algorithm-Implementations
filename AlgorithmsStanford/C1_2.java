import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;

/*
 * This static program demonstrates the Algorithm for Counting Inversions in an array
 */
public class C1_2
{
    //Main method to demonstrate the program
    public static void main(String[] args) throws FileNotFoundException
    {
        int[] integerArray = getIntArray();
        BigInteger inversionCount = sortAndCountInv(integerArray);
        System.out.println("Number of inversions: " + inversionCount);
    }
    
    //Returns an array with the integers of IntegerArray.txt
    public static int[] getIntArray() throws FileNotFoundException
    {
        int[] integerArray = new int[100000];
        File file = new File("IntegerArray.txt");
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
    
    //The algorithm itself
    public static BigInteger sortAndCountInv(int[] integerArray)
    {
        if (integerArray.length == 1)
        {
            return new BigInteger("0");
        }
        else
        {
            BigInteger x;
            BigInteger y;
            BigInteger z;
            int[] firstHalf = new int[integerArray.length / 2];
            int[] secondHalf;
            
            if (integerArray.length % 2 == 0)
            {
                secondHalf = new int[integerArray.length / 2];
            }
            else
            {
                secondHalf = new int[integerArray.length / 2 + 1];
            }
            
            for (int i = 0; i < firstHalf.length; i++)
            {
                firstHalf[i] = integerArray[i];
            }
            for (int i = 0; i < secondHalf.length; i++)
            {
                secondHalf[i] = integerArray[firstHalf.length + i];
            }
            
            x = sortAndCountInv(firstHalf);
            y = sortAndCountInv(secondHalf);
            z = countSplitInvAndMerge(integerArray, firstHalf, secondHalf);
            
            return x.add(y).add(z);
        }
    }
    
    //A subroutine for sorting integerArray and counting split inversions
    public static BigInteger countSplitInvAndMerge(int[] integerArray, int[] firstHalf, int[] secondHalf)
    {
        int i = 0;
        int j = 0;
        BigInteger splitInvCount = new BigInteger("0");
        
        for (int x = 0; x < integerArray.length; x++)
        {
            if (i == firstHalf.length)
            {
                integerArray[x] = secondHalf[j];
                j++;
            }
            else if (j == secondHalf.length)
            {
                integerArray[x] = firstHalf[i];
                i++;
            }
            else if (firstHalf[i] < secondHalf[j])
            {
                integerArray[x] = firstHalf[i];
                i++;
            }
            else
            {
                integerArray[x] = secondHalf[j];
                j++;
                for (int count = i; count < firstHalf.length; count++)
                {
                    splitInvCount = splitInvCount.add(new BigInteger("1"));
                }
            }
        }
        
        return splitInvCount;
    }
}

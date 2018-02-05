import java.util.Hashtable;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.ArrayList;

/*
 * This class demonstrates solving the 2-Sum problem using hash tables.
 * It outputs the number of target sums between -10000 and 10000 that
 * have distinct values x and y from the file 2Sum.txt that sum to the
 * target sum. The program takes about 45 minutes to complete.
 * */
public class C2_4
{
    //Main program to demonstrate
    public static void main(String[] args) throws FileNotFoundException
    {
        System.out.println(twoSum());
    }
    
    //twoSum problem algorithm using hash tables for fast lookups
    //It uses an arraylist to delete successful target sums, only
    //leaving target sums with no distinct x and y that sum to them
    public static int twoSum() throws FileNotFoundException
    {
        Hashtable<Long, Long> hashTable = new Hashtable<Long, Long>();
        ArrayList<Integer> targetSums = new ArrayList<Integer>();
        int targetSumsOrigSize;
        Scanner scan = new Scanner(new File("2Sum.txt"));
        
        while (scan.hasNextLong())
        {
            long nextNum = scan.nextLong();
            hashTable.put(nextNum, nextNum);
        }
        scan.close();
        
        for (int i = -10000; i <= 10000; i++)
        {
            targetSums.add(i);
        }
        targetSumsOrigSize = targetSums.size();
        
        Iterator<Long> iter = hashTable.keySet().iterator();
        while(iter.hasNext())
        {
            Long x = iter.next();
            for (int i = 0; i < targetSums.size(); i++)
            {
                if (hashTable.keySet().contains(targetSums.get(i).intValue() - x.longValue())
                                && targetSums.get(i).intValue() - x.longValue() != x.longValue())
                {
                    targetSums.remove(i);
                    i--;
                }
            }
        }
        
        return targetSumsOrigSize - targetSums.size();
    }
}

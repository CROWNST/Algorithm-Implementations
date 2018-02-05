import java.math.BigInteger;
import java.util.Scanner;

/*
 * This demonstrates the Karatsuba Recursive Algorithm for recursively multiplying two numbers, which
 * don't have to have the same number of digits
 */
public class C1_1
{
    //Main program that demonstrates the algorithm
    public static void main(String[] args)
    {
        String firstNumber;
        String secondNumber;
        
        Scanner sc = new Scanner(System.in);
        System.out.print("First number: ");
        firstNumber = sc.next();
        System.out.print("Second number: ");
        secondNumber = sc.next();
        sc.close();
        
        System.out.println("Answer = " + recursiveMultiply(firstNumber, secondNumber));
    }
    
    //The algorithm itself
    public static String recursiveMultiply(String x, String y)
    {
        if (x.length() == 1 && y.length() == 1)
        {
            int a = Integer.parseInt(x);
            int b = Integer.parseInt(y);
            int result = a * b;
            return Integer.toString(result);
        }
        else
        {
            String[] answer = padZeroes(x, y);
            x = answer[0];
            y = answer[1];
            
            String a = x.substring(0, x.length() / 2);
            String b = x.substring(x.length() / 2, x.length());
            String c = y.substring(0, y.length() / 2);
            String d = y.substring(y.length() / 2, y.length());
            
            BigInteger aPlusb = new BigInteger(a).add(new BigInteger(b));
            BigInteger cPlusd = new BigInteger(c).add(new BigInteger(d));
            
            BigInteger ac = new BigInteger(recursiveMultiply(a, c));
            BigInteger bd = new BigInteger(recursiveMultiply(b, d));
            BigInteger adPlusbc = new BigInteger(recursiveMultiply(aPlusb.toString(), cPlusd.toString())).subtract(ac).subtract(bd);
            
            String tenToTheN = "";
            String tenToTheNOverTwo = "";
            for (int i = 0; i < x.length(); i++)
            {
                tenToTheN += "0";
            }
            for (int i = 0; i < x.length() / 2; i++)
            {
                tenToTheNOverTwo += "0";
            }
            
            ac = new BigInteger(ac.toString() + tenToTheN);
            adPlusbc = new BigInteger(adPlusbc.toString() + tenToTheNOverTwo);
            
            return ac.add(adPlusbc).add(bd).toString();
        }
    }
    
    //Subroutine for placing zeroes behind each of x and y, with each ending up
    //with 2^a number of digits, for some integer a. 2^a must be the first power
    // of 2 that is larger than the number of digits in y.
    public static String[] padZeroes(String x, String y)
    {
        String[] answer = new String[2];
        
        String larger;
        String smaller;
        boolean v = false;
        boolean w = false;
        if (x.length() >= y.length())
        {
            larger = x;
            smaller = y;
            v = true;
        }
        else
        {
            larger = y;
            smaller = x;
            w = true;
        }
        
        int i = 0;
        while (Math.pow(2, i) < larger.length())
        {
            i++;
        }
        
        String zeroes = "";
        for (int a = 0; a < Math.pow(2, i) - larger.length(); a++)
        {
            zeroes += "0";
        }
        larger = zeroes + larger;
        
        zeroes = "";
        for (int a = 0; a < Math.pow(2, i) - smaller.length(); a++)
        {
            zeroes += "0";
        }
        smaller = zeroes + smaller;
        
        if (v == true)
        {
            answer[0] = larger;
            answer[1] = smaller;
            return answer;
        }
        else
        {
            answer[0] = smaller;
            answer[1] = larger;
            return answer;
        }
    }
}

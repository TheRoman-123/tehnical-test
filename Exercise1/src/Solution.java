import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = 0;

        System.out.println("Enter amount of candles in diapason 2..999: ");
        while (n == 0) {
            try {
                n = scanner.nextInt();
                if (n <= 1 || n >= 1000) throw new IllegalArgumentException();
            } catch (NoSuchElementException e) {
                System.out.println("Enter an integer variable!");
            } catch (IllegalArgumentException e) {
                System.out.println("Enter correct amount!");
                n = 0;
            }
        }

        int[] candles = new int[n];
        System.out.println("Enter heights of candles: ");
        for (int i = 0; i < candles.length; i++) {
            candles[i] = scanner.nextInt();
            if (candles[i] <= 1 || candles[i] >= 1000) {
                System.out.println("Enter correct height!");
                --i;
            }
        }
        scanner.close();

        int amountHighestCandles = birthdayCakeCandles(candles);
        System.out.println("Amount of highest candles: " + amountHighestCandles);
    }

    private static int birthdayCakeCandles(int[] candles) {
        if (candles == null || candles.length == 0) throw new NullPointerException("Candles[] is empty.");
        int highestCandle = Arrays.stream(candles).max().orElse(0);
        return (int) Arrays.stream(candles).filter(a -> a == highestCandle).count();
    }
}

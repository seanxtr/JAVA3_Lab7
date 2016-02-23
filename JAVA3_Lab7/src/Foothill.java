// CS1C Lab Assignment #7 - Shell Sort
// Tianrong Xiao, Foothill College, Winter 2016

import java.util.Random;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Foothill {
   
   /**
    * Method to sort an array using shell sort with gap = 1
    * @param a      array to sort
    */
   public static < E extends Comparable< ? super E > > 
   int shellSort1(E[] a) {
      int gap = 1;
      int swaps = 0;
      
      int k, pos, arraySize;
      E tmp;
       
      arraySize = a.length;
      for (gap = arraySize/2;  gap > 0;  gap /= 2) {
         for(pos = gap; pos < arraySize; pos++ ) {
            tmp = a[pos];
            for(k = pos; k >= gap && tmp.compareTo(a[k-gap]) < 0; k -= gap ) {
               a[k] = a[k-gap];
               swaps++;
            }
            a[k] = tmp;
         }
      }
      
      return swaps;
   }
   
   /**
    * Method to sort an array using shell sort with custom gap sequence
    * @param a      array to sort
    * @param gaps   gap sequences from small to large
    * @return       total swaps performed to sort the array
    */
   public static <E extends Comparable<? super E>> 
   int shellSortX(E[] a, int[] gaps) {
      int swaps = 0;
      int gap = 1;
      
      int i, k, pos, arraySize;
      E tmp;
       
      arraySize = a.length;
      for (i = gaps.length - 1;  i >= 0; i--) {
         gap = gaps[i];
         for(pos = gap; pos < arraySize; pos++ ) {
            tmp = a[pos];
            for(k = pos; k >= gap && tmp.compareTo(a[k-gap]) < 0; k -= gap ) {
               a[k] = a[k-gap];
               swaps++;
            }
            a[k] = tmp;
         }
      }
      
      return swaps;
   }
   
   // -------------- Main Program ----------------
   public static void main(String[] args) throws Exception {
      final int ARRAY_SIZE = 10000;
      final int TEST_REP = 100;
      
      int i, testNumber = 7, actualSize;
      NumberFormat tidy = NumberFormat.getInstance(Locale.US);
      tidy.setMaximumFractionDigits(4);
      Integer[] arrayOfInts1;
      
      int[] gapArray = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024,
         2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288};
      int[] sedgewickArray = getSedgewickArray(15);
      int[] hibbardArray = getHibbardArray(20);
      int[] knuthArray = getKnuthArray(15);
      int[] tokudaArray = getTokudaArray(30);
      
      for (i = 0; i < testNumber; i++) {
         actualSize = ARRAY_SIZE * (int)Math.pow(2, i);
         System.out.printf("%n%n------------ Array Size %d ------------%n%n", actualSize);
         arrayOfInts1 = generateRandomArray(actualSize);
         
         TestShellSort("One", arrayOfInts1, null, TEST_REP);
         TestShellSort("Power", arrayOfInts1, gapArray, TEST_REP);
         TestShellSort("Knuth", arrayOfInts1, knuthArray, TEST_REP);
         TestShellSort("Hibbard", arrayOfInts1, hibbardArray, TEST_REP);
         TestShellSort("Sedgewick", arrayOfInts1, sedgewickArray, TEST_REP);
         TestShellSort("Tokuda", arrayOfInts1, tokudaArray, TEST_REP);
      }
   }

   /**
    * Method to test a given shell sort algorithm
    * @param array       sample array used in test
    * @param gaps        gap sequence
    * @param rep         repetition
    */
   private static void TestShellSort(
      String name, Integer[] array, int[] gaps, int rep) {
      
      int i;
      Integer[] testArray;
      long startTime, stopTime, avgTime = 0, swaps = 0; 
      NumberFormat tidy = NumberFormat.getInstance(Locale.US);
      tidy.setMaximumFractionDigits(4);
      
      for (i = 0; i < rep; i++) {
         testArray = CopyIntArray(array);
         startTime = System.nanoTime();  // ------------------ start 
         swaps += (gaps == null ? shellSort1(testArray) : shellSortX(testArray, gaps));
         stopTime = System.nanoTime();   // ---------------------- stop
         avgTime += (stopTime - startTime);
      }
      
      System.out.printf("Avg Elapsed Time (%s): %s seconds, %d swaps%n",
         name, tidy.format( (avgTime / rep) / 1e9), swaps / rep);
   }
   
   /**
    * Make a hard copy of an array
    */
   private static Integer[] CopyIntArray(Integer[] source) {
      Integer[] copy = new Integer[source.length];
      for(int i = 0; i < source.length; i++) {
         copy[i] = source[i];
      }
      return copy;
   }
   
   /**
    * Method to generate an array of integer with no duplicates
    * @param size          size of array
    * @return              random array
    */
   private static Integer[] generateRandomArray(int size) {
      int index;
      Random rand = new Random();
      Integer[] array = new Integer[size];
      ArrayList<Integer> list = new ArrayList<Integer>(size);
      
      for(int i = 1; i <= size; i++) 
          list.add(i);
      
      for (int i = 0; i < size; i++) {
         index = rand.nextInt(list.size());
         array[i] = list.get(index);
         list.remove(index);
      }
      return array;
   }
   
   /**
    * Method to generate Sedgewick gap sequence
    * @param size      size of sequence array
    * @return          sequence array
    */
   private static int[] getSedgewickArray(int size) {
      
      if (size <= 0)
         throw new NegativeArraySizeException();
      
      int[] seq = new int[size];
      seq[0] = 1; // fix the first element with one
      
      // fill rest of array when size is greater than 1
      if (size > 1) {
         for(int i = 1; i < size; i++) {
            seq[i] = (int) (Math.pow(4, i) + 3 * Math.pow(2, i-1) + 1);
         }
      }
      
      return seq;
   }
   
   /**
    * Method to generate Hibbard gap sequence
    * @param size      size of sequence array
    * @return          sequence array
    */
   private static int[] getHibbardArray(int size) {
      if (size <= 0)
         throw new NegativeArraySizeException();
      
      int[] seq = new int[size];

      for(int i = 1; i <= size; i++) {
         seq[i - 1] = (int) (Math.pow(2, i) - 1);
      }
      
      return seq;
   }
   
   /**
    * Method to generate Knuth gap sequence
    * @param size      size of sequence array
    * @return          sequence array
    */
   private static int[] getKnuthArray(int size) {
      if (size <= 0)
         throw new NegativeArraySizeException();
      
      int[] seq = new int[size];

      for(int i = 1; i <= size; i++) {
         seq[i - 1] = (int) ((Math.pow(3, i) - 1) / 2);
      }
      
      return seq;
   }
   
   /**
    * Method to generate Tokuda gap sequence
    * @param size      size of sequence array
    * @return          sequence array
    */
   private static int[] getTokudaArray(int size) {
      if (size <= 0)
         throw new NegativeArraySizeException();
      
      int[] seq = new int[size];

      for(int i = 1; i <= size; i++) {
         seq[i - 1] = (int) ((Math.pow(9, i) - Math.pow(4, i)) / (5*Math.pow(4, i-1)));
      }
      
      return seq;
   }
}

/***************** Runs ********************


------------ Array Size 10000 ------------

Avg Elapsed Time (One): 0.0022 seconds, 149693 swaps
Avg Elapsed Time (Power): 0.0036 seconds, 467767 swaps
Avg Elapsed Time (Knuth): 0.0018 seconds, 166031 swaps
Avg Elapsed Time (Hibbard): 0.0021 seconds, 129349 swaps
Avg Elapsed Time (Sedgewick): 0.0017 seconds, 168669 swaps
Avg Elapsed Time (Tokuda): 0.0018 seconds, 101417 swaps


------------ Array Size 20000 ------------

Avg Elapsed Time (One): 0.0044 seconds, 372046 swaps
Avg Elapsed Time (Power): 0.0078 seconds, 1146654 swaps
Avg Elapsed Time (Knuth): 0.0043 seconds, 405572 swaps
Avg Elapsed Time (Hibbard): 0.0052 seconds, 319498 swaps
Avg Elapsed Time (Sedgewick): 0.004 seconds, 376246 swaps
Avg Elapsed Time (Tokuda): 0.0046 seconds, 225148 swaps


------------ Array Size 40000 ------------

Avg Elapsed Time (One): 0.0101 seconds, 893233 swaps
Avg Elapsed Time (Power): 0.0245 seconds, 4572523 swaps
Avg Elapsed Time (Knuth): 0.0109 seconds, 1008115 swaps
Avg Elapsed Time (Hibbard): 0.0125 seconds, 795977 swaps
Avg Elapsed Time (Sedgewick): 0.0095 seconds, 841109 swaps
Avg Elapsed Time (Tokuda): 0.0107 seconds, 492213 swaps


------------ Array Size 80000 ------------

Avg Elapsed Time (One): 0.0242 seconds, 2079847 swaps
Avg Elapsed Time (Power): 0.0648 seconds, 12311923 swaps
Avg Elapsed Time (Knuth): 0.0248 seconds, 2277419 swaps
Avg Elapsed Time (Hibbard): 0.0289 seconds, 1895093 swaps
Avg Elapsed Time (Sedgewick): 0.0216 seconds, 1822653 swaps
Avg Elapsed Time (Tokuda): 0.0241 seconds, 1061703 swaps


------------ Array Size 160000 ------------

Avg Elapsed Time (One): 0.0632 seconds, 5656545 swaps
Avg Elapsed Time (Power): 0.1582 seconds, 29597155 swaps
Avg Elapsed Time (Knuth): 0.0582 seconds, 5474373 swaps
Avg Elapsed Time (Hibbard): 0.068 seconds, 4323731 swaps
Avg Elapsed Time (Sedgewick): 0.0478 seconds, 4006243 swaps
Avg Elapsed Time (Tokuda): 0.0543 seconds, 2297827 swaps


------------ Array Size 320000 ------------

Avg Elapsed Time (One): 0.1824 seconds, 12838492 swaps
Avg Elapsed Time (Power): 0.5963 seconds, 121243760 swaps
Avg Elapsed Time (Knuth): 0.1634 seconds, 12551448 swaps
Avg Elapsed Time (Hibbard): 0.1998 seconds, 11252880 swaps
Avg Elapsed Time (Sedgewick): 0.1401 seconds, 8599474 swaps
Avg Elapsed Time (Tokuda): 0.1484 seconds, 4959524 swaps


------------ Array Size 640000 ------------

Avg Elapsed Time (One): 0.4076 seconds, 33720496 swaps
Avg Elapsed Time (Power): 1.6608 seconds, 397000898 swaps
Avg Elapsed Time (Knuth): 0.3402 seconds, 30270656 swaps
Avg Elapsed Time (Hibbard): 0.3918 seconds, 24818996 swaps
Avg Elapsed Time (Sedgewick): 0.2801 seconds, 18859314 swaps
Avg Elapsed Time (Tokuda): 0.3067 seconds, 10615620 swaps

********************************************/

/***************** Discussion ********************

Summary test results into a table:

  Size     One    Power   Sedgewick   hibbard    knuth   Tokuda
------     ---    -----   ---------   -------    -----   ------
 10000   0.0022  0.0036      0.0017    0.0021   0.0018   0.0018
 20000   0.0044  0.0078      0.0040    0.0052   0.0043   0.0046
 40000   0.0101  0.0245      0.0095    0.0125   0.0109   0.0107
 80000   0.0242  0.0648      0.0216    0.0289   0.0248   0.0241
160000   0.0673  0.1582      0.0478    0.0680   0.0582   0.0543
320000   0.1824  0.5963      0.1401    0.1998   0.1634   0.1484
640000   0.4076  1.6608      0.2801    0.3918   0.3402   0.3067

Q1: Why does Shell's gap sequence implied by shellSort1() give a different 
timing result than the explicit array described above and passed to shellSortX()?  
Q2: Which is faster and why?

Answers:
Q1: by using gap sequence, the shell sorting algorithm is able to swap elements
far away, and less swaps are needed as the array becomes more ordered. Reduced 
redundant comparisons and swaps make the shellsortX much faster shellsort1 when
using certain gap sequences.

Q2: Among all testings, Sedgewick sequence performs the best, and power sequence being
the worst. I can't find a sequence faster than Sedgewick, and the closest is Tokuda.

Guess and Estimate time complexity based on last two sizes:
For power sequence, when size doubled, time increased 2.79x, so it could have O(N^1.5)
For Kruth, when size doubled, time up by 2.08x, so it could have O(N)
Similar analysis could be done to all sequences but it turns out to be unstable from 
size to size.
**************************************************/

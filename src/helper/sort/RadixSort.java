package helper.sort;

// Creel Academy of Computer Science
// https://www.youtube.com/watch?v=TPpWvpnQq5s

import helper.io.IOHandler;
import helper.methods.CommonMethods;
import helper.struct.SMTimer;

public class RadixSort {

    // Unsigned Int
    public static void sort256(int[] arr, int n) {
        IOHandler.printString("Only Use Unsigned Int With RadixSort");
        if(n<=1){return;}
        int[] output = new int[n];
        int[] count = new int[256];
        //int[] originalArr = arr;

        for (int shift = 0,s=0;shift<4;shift++,s+=8){
            // Zero the counts
            for (int i = 0;i<256;i++){count[i] = 0;}
            // Store count of occurrences in count[]
            for (int i = 0;i<n;i++){count[(arr[i]>>s)&0xff]++;}
            // Change count[i] so that count[i] now contains
            // actual position of this digit in output[]
            for (int i = 1;i<256;i++){count[i] += count[i - 1];}

            // Build the output array
            for (int i=n-1;i>=0;i--) {
                // precalculate the offset as it's a few instructions
                int idx = (arr[i] >> s) & 0xff;
                // Subtract from the count and store the value
                output[--count[idx]] = arr[i];
            }

            // Copy the output array to input[], so that input[]
            // is sorted according to current digit

            // We can just swap the pointers
            int[] tmp = arr;
            arr = output;
            output = tmp;
        }
        // If we switched pointers an odd number of times,
        // make sure we copy before returning
        /*if (originalArr == output) {
            int[] tmp = arr;
            arr = output;
            output = tmp;
            for (int i = 0; i < n; i++){arr[i] = output[i];}
        }*/
        //output = null;
        //count = null;
    }

    static void printArray(int[] arr,int size){
        for(int i=0;i<size;i++){
            IOHandler.printInt(arr[i]);
        }
    }

    public static void runTest(int size){
        SMTimer timer = new SMTimer();
        int[]arrRadix = new int[size];
        for(int i = 0;i<size;i++){
            arrRadix[i] = CommonMethods.getRand(size);
        }
        timer.startClock();
        RadixSort.sort256(arrRadix,arrRadix.length);
        IOHandler.printString("RadixSort RunningTime on %d elements: %s".formatted(size,timer.getTimePassedString()));
    }
}

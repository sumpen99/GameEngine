package helper.sort;

import helper.io.IOHandler;
import helper.methods.CommonMethods;
import helper.struct.SMTimer;

public class HeapSort {

   static void heapify(int[]arr,int n,int i){
        int largest = i;
        int left = 2 * i +1;
        int right = 2 * i + 2;

        if(left < n && arr[left] > arr[largest]){largest = left;}
        if(right < n && arr[right] > arr[largest]){largest = right;}

        if(largest!=i){
            CommonMethods.swapIntArrayObject(arr,i,largest);
            heapify(arr,n,largest);
        }

   }

    public static void sort(int[] arr,int n){
        for(int i = n/2-1;i>=0;i--){
            heapify(arr,n,i);
        }
        for(int i = n-1;i>=0;i--){
            CommonMethods.swapIntArrayObject(arr,0,i);
            heapify(arr,i,0);
        }
    }

    public static void printSortedArray(int[]arr,int n){
       IOHandler.printString("Sorted Array: ");
       for(int i=0;i<n;i++){
           IOHandler.printInt(arr[i]);
       }
    }

    public static void runTest(){
        SMTimer timer = new SMTimer();
        int size = 10000;
        int[]arrHeap = new int[size];
        for(int i = 0;i<size;i++){
            arrHeap[i] = CommonMethods.getRand(size);
        }
        timer.startClock();
        HeapSort.sort(arrHeap,arrHeap.length);
        IOHandler.printString("HeapSort RunningTime on %d elements: %s".formatted(size,timer.getTimePassedString()));
   }
}

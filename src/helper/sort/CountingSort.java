package helper.sort;

import helper.io.IOHandler;
import helper.methods.CommonMethods;
import helper.struct.SMTimer;

public class CountingSort {


    public static void sort(int[] arr,int n){
        int[] arr1 = new int[n+1];
        int x = arr[0];
        for(int i = 1;i<n;i++){
            if(arr[i] > x){
                x = arr[i];
            }
        }
        int[] countArr = new int[x+1];
        for(int i = 0;i<n;i++){
            countArr[arr[i]]++;
        }
        for(int i = 1;i<=x;i++){
            countArr[i] += countArr[i-1];
        }
        for(int i = n-1;i>=0;i--){
            arr1[countArr[arr[i]]-1] = arr[i];
            countArr[arr[i]]--;
        }
        for(int i = 0;i<n;i++){
            arr[i] = arr1[i];
        }
    }

    public static void printArray(int[]arr,int n){
        IOHandler.printString("Sorted Array: ");
        for(int i=0;i<n;i++){
            IOHandler.printInt(arr[i]);
        }
    }

    public static void runTest(int size){
        SMTimer timer = new SMTimer();
        int[]arrCounting = new int[size];
        for(int i = 0;i<size;i++){
            arrCounting[i] = CommonMethods.getRand(size);
        }
        timer.startClock();
        CountingSort.sort(arrCounting,arrCounting.length);
        IOHandler.printString("CountingSort RunningTime on %d elements: %s".formatted(size,timer.getTimePassedString()));
        //printArray(arrCounting,arrCounting.length);
    }
}

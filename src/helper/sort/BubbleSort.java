package helper.sort;

import helper.io.IOHandler;
import helper.methods.CommonMethods;
import helper.struct.SMTimer;

public class BubbleSort {

    public static void sortMin(int[] arr,int size){
        int n = size-1;
        for(int i = 0 ;i < n;i++)
            for(int j = 0;j<n-i;j++)
                if(arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
    }

    public static void runTest(){
        SMTimer timer = new SMTimer();
        int size = 10000;
        int[]arrBubble = new int[size];
        for(int i = 0;i<size;i++){
            arrBubble[i] = CommonMethods.getRand(size);
        }
        timer.startClock();
        BubbleSort.sortMin(arrBubble,size);
        IOHandler.printString("BubbleSort RunningTime on %d elements: %s".formatted(size,timer.getTimePassedString()));
    }
}

package helper.search;

import helper.io.IOHandler;
import helper.sort.QuickSort;

import static helper.methods.CommonMethods.getRand;

public class InterpolationSearch {

    public static void testAlgorithm(){
        int i = 1,size=10000,key = getRand(size);
        int[] arr = new int[size];
        arr[0] = key;
        while(i<size){
            arr[i++] = getRand(size);
        }
        QuickSort.sortIntArray(arr,0,arr.length-1);
        int found = search(arr,0,arr.length-1,key);
        IOHandler.printString("Searched for: %d -> Found: %d At Index: %d".formatted(key,arr[found],found));
    }

    public static int search(int[] arr,int low,int high,int key){
        int pos;

        if(low<=high && key >= arr[low] && key <= arr[high]){
            pos = (int)(low + (((double)(high - low) / (arr[high] - arr[low])) * (key - arr[low])));

            if(arr[pos] == key){return pos;}

            if(arr[pos] < key){return search(arr,pos+1,high,key);}

            if(arr[pos]>key){return search(arr,low,pos-1,key);}
        }
        return -1;
    }
}

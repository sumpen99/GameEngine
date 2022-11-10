package helper.sort;

import helper.struct.AutoWord;
import helper.struct.PriorityQueue;

import static helper.methods.CommonMethods.*;

public class QuickSort {

    public static void sortPriorityQueue(PriorityQueue pQueue,int low,int high){
        sortWordList(pQueue.bufWords,low,high);

    }

    public static void sortFloatArray(float[] floatList,int low,int high){
        sortFloatList(floatList,low,high);

    }

    public static void sortIntArray(int[] intList,int low,int high){
        sortIntList(intList,low,high);

    }

    static void sortWordList(AutoWord[] wordList,int low,int high){
        if(low < high){
            int q = partitionWordList(wordList,low,high);
            sortWordList(wordList,low,q);
            sortWordList(wordList,q+1,high);
        }
    }

    static int partitionWordList(AutoWord[] wordList,int low,int high){
        int pivot = wordList[low].edits;
        int i = low-1;
        int j = high+1;
        while(true){
            while(++i < high && wordList[i].edits < pivot);
            while(--j > low && wordList[j].edits > pivot);
            if(i < j){swapAutoWord(wordList[i],wordList[j]);}
            else{return j;}
        }
    }

    static void sortFloatList(float[] floatList,int low,int high){
        if(low < high){
            int q = partitionFloatList(floatList,low,high);
            sortFloatList(floatList,low,q);
            sortFloatList(floatList,q+1,high);
        }
    }

    static int partitionFloatList(float[] floatList,int low,int high){
        float pivot = floatList[low];
        int i = low-1;
        int j = high+1;
        while(true){
            while(++i < high && floatList[i] < pivot);
            while(--j > low && floatList[j] > pivot);
            if(i < j){swapFloat(floatList,i,j);}
            else{return j;}
        }
    }

    static void sortIntList(int[] intList,int low,int high){
        if(low < high){
            int q = partitionIntList(intList,low,high);
            sortIntList(intList,low,q);
            sortIntList(intList,q+1,high);
        }
    }

    static int partitionIntList(int[] intList,int low,int high){
        float pivot = intList[low];
        int i = low-1;
        int j = high+1;
        while(true){
            while(++i < high && intList[i] < pivot);
            while(--j > low && intList[j] > pivot);
            if(i < j){swapIntArrayObject(intList,i,j);}
            else{return j;}
        }
    }

}

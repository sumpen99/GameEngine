package helper.sort;
import static helper.methods.CommonMethods.swapAutoWord;
import helper.struct.AutoWord;
import helper.struct.PriorityQueue;

public class QuickSort {

    public static void sortPriorityQueue(PriorityQueue pQueue,int low,int high){
        sortWordList(pQueue.bufWords,low,high);

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

}

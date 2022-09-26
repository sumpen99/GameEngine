package helper.search;
import helper.struct.SMTimer;
import helper.struct.SearchInfo;

public class BinarySearch {

    public static void setUpSearch(int[] arr, int key, SearchInfo sInfo){
        int result,leftMin,rightMax,foundIndex,foundCount;
        boolean found = false;
        leftMin = -1;rightMax = -1;foundCount=0;
        SMTimer timer = new SMTimer();
        timer.startClock();
        foundIndex = search(arr,key,0,arr.length-1);
        if(foundIndex > -1){
            found = true;
            result = foundIndex;
            while((result = search(arr,key,0,result-1)) > -1){leftMin = result;}
            result = foundIndex;
            while((result = search(arr,key,result+1,arr.length-1)) > -1){rightMax = result;}

            leftMin = leftMin == -1 ? foundIndex : leftMin;
            rightMax = rightMax == -1 ? foundIndex : rightMax;
            foundCount = rightMax - leftMin + 1;
        }
        sInfo.searchTime = timer.getTimePassed();
        sInfo.leftMin = leftMin;
        sInfo.rightMax = rightMax;
        sInfo.foundIndex = foundIndex;
        sInfo.found = found;
        sInfo.foundCount = foundCount;
    }

    public static int search(int[] arr, int key,int first,int last){
        int middle;
        while(first<=last){
            middle = (first+last)/2;
            if(key < arr[middle]){last = middle-1;}
            else if(key > arr[middle]){first = middle+1;}
            else if(key == arr[middle]){
                return middle;
            }
        }
        return -1;
    }

}

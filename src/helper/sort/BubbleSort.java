package helper.sort;

public class BubbleSort {

    public static void minBubbleSort(int[] arr,int size){
        int n = size-1;
        for(int i = 0 ;i < n;i++)
            for(int j = 0;j<n-i;j++)
                if(arr[j] > arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
    }
}

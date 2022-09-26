package helper.struct;
import static helper.methods.CommonMethods.swapAutoWord;
public class PriorityQueue {
    public AutoWord[] bufWords;
    public int wordCount;
    int bufSize;
    public PriorityQueue(int bufSize){
        this.bufSize = bufSize;
        this.wordCount = 0;
        this.bufWords = new AutoWord[bufSize];
    }

    public void insertWord(AutoWord w){bufWords[wordCount++] = w;}

    public void insertWord1(AutoWord w){
        if(wordCount == 0){bufWords[wordCount++] = w;}
        else{
            bufWords[wordCount++] = w;
            for(int i = wordCount/2-1;i>=0;i--){
                reOrganizeBuf(i);
            }
        }
    }

    void reOrganizeBuf(int i){
        if(wordCount == 1)return;
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        if(l < wordCount && bufWords[l].edits < bufWords[largest].edits)largest = l;
        if(r < wordCount && bufWords[r].edits < bufWords[largest].edits)largest = r;

        if(largest != i){
            swapAutoWord(bufWords[i],bufWords[largest]);
            reOrganizeBuf(largest);
        }
    }

    void delAutoWord(int num) {
        int i,j;
        for(i = 0; i < wordCount; i++) {
            if(num == bufWords[i].edits)
                break;
        }
        swapAutoWord(bufWords[i],bufWords[wordCount-1]);
        wordCount--;
        for(j = wordCount/2-1;j>=0;j--) {
            reOrganizeBuf(j);
        }
    }

}

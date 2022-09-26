package helper.struct;

public class AutoWord {
    public String word;
    public int edits,index;
    public AutoWord(int index,int edits,String word){
        this.index = index;
        this.edits = edits;
        this.word = word;
    }

    public void  swapValues(int index,int edits,String word){
        this.index = index;
        this.edits = edits;
        this.word = word;
    }

}

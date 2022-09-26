package helper.struct;

public class AutoWords {
    public String[] words;
    public int count;

    public void initWordsList(){
        words = new String[count];
    }

    public String GetNextWord(int index){
        return words[index];
    }
}

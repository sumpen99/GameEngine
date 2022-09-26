package helper.struct;

public class Edit{
    public static int col;
    public static int row;
    public char c1,c2;
    public int n;
    public Edit next;

    public Edit(){
        c1 = '\0';
        c2 = '\0';
        n = 0;
        next = null;
    }
    public static int getIndex(int row,int col){
        return (row*Edit.col)+col;
    }

    public static void setRowCol(Edit[] edits,int row,int col){
        int cnt = 0,maxCount = row*col;
        Edit.row = row;
        Edit.col = col;
        while(cnt < maxCount){edits[cnt++] = new Edit();}

    }

}


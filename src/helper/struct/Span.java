package helper.struct;

public class Span{
    public int x1,x2;
    public void setInstance(int x1,int x2){
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
    }
}

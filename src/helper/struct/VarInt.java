package helper.struct;

public class VarInt {
    public int p0;
    public int p1;

    @Override
    public String toString(){
        return "%d %d".formatted(p0,p1);
    }
}

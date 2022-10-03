package helper.struct;

public class UInt {
    public long uI32;

    public void set32(long i){
        assert i >= 0 && i <= 4294967295L : "Not A Valid 32bits Unsigned Number";
        uI32 = i;
    }

}

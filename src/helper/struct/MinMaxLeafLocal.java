package helper.struct;

public class MinMaxLeafLocal {
    public int usableSize,minLeaf,maxLeaf,minLocal,maxLocal;

    public MinMaxLeafLocal(int usableSize){
        this.usableSize = usableSize;
        this.maxLocal = ((usableSize - 12) * 64 / 255 - 23);
        this.minLocal = ((usableSize - 12) * 32 / 255 - 23);
        this.maxLeaf = usableSize - 35;
        this.minLeaf = ((usableSize - 12) * 32 / 255 - 23);
    }
}

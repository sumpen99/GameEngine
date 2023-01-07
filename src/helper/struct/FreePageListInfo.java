package helper.struct;

public class FreePageListInfo {
    public long startPage;
    public long length;

    public FreePageListInfo(long startPage,long lenght){
        this.startPage = startPage;
        this.length = lenght;
    }

    @Override
    public String toString(){
        return startPage == 0 ? "No free page" : "start at index: %d length %d".formatted(startPage,length);
    }
}

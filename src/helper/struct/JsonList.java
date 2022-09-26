package helper.struct;
import helper.list.SMHashMap;
public class JsonList {
    public SMHashMap[] objMaps;
    public int objCount;
    public JsonList(int size){
        objCount = 0;
        objMaps = new SMHashMap[size];
        initData(size);
    }

    void initData(int size){
        for(int i = 0;i < size;i++){objMaps[i] = new SMHashMap(100,.75f);}
    }
}

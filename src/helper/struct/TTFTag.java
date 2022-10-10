package helper.struct;

import helper.enums.TTFTable;

public class TTFTag {
    public int checkSum,offset,length;
    public TTFTable tag;
    public Object tableValues;
    public TTFTag(TTFTable t, int c, int o, int l){
        tag = t;
        checkSum = c;
        offset = o;
        length = l;
    }

    public void setTableValues(Object obj){
        tableValues = obj;
    }

    public Object getTableValue(){
        return tableValues;
    }
}

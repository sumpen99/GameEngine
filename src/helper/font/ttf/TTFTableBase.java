package helper.font.ttf;
import helper.enums.TTFTable;
import helper.interfaces.ITTFTableInfo;

public class TTFTableBase{
    public ITTFTableInfo info;
    public TTFTable tableTag;
    public TTFTableBase(TTFTable tag){
        tableTag = tag;
        setSelf();
    }

    public void setSelf(){
        if(tableTag == TTFTable.LOCA){}
        if(tableTag == TTFTable.GLYF){}
        if(tableTag == TTFTable.DSIG){}
        if(tableTag == TTFTable.NAME){}
        if(tableTag == TTFTable.POST){}
        if(tableTag == TTFTable.CMAP){}
        if(tableTag == TTFTable.HMTX){}
        if(tableTag == TTFTable.CVT){}
        if(tableTag == TTFTable.GASP){}
        if(tableTag == TTFTable.FFTM){}
        if(tableTag == TTFTable.HEAD){info = new TTFHeadInfo();}
        if(tableTag == TTFTable.HHEA){}
        if(tableTag == TTFTable.MAXP){info = new TTFMaxPInfo();}
        if(tableTag == TTFTable.GDEF){}
        if(tableTag == TTFTable.OS_2){}
        if(tableTag == TTFTable.LOCA){}
        if(tableTag == TTFTable.LOCA){}
    }

    public int getOffset(TTFFile header){
        return header.getTableTag(tableTag).offset;
    }

    public int getLength(TTFFile header){
        return header.getTableTag(tableTag).length;
    }

    public void setValue(TTFFile header){
        header.setTableValue(tableTag,info);
    }

    public void convertToSize(byte[] buf){
        info.setValues(buf);
    }
}

package helper.font.ttf;
import helper.enums.TTFTable;
import helper.interfaces.ITTFTableInfo;
import helper.font.ttf.TTFhheaInfo;
import helper.io.IOHandler;

public class TTFTableBase{
    public ITTFTableInfo info;
    public TTFTable tableTag;
    public TTFTableBase(TTFTable tag){
        tableTag = tag;
    }
    public void setSelf(TTFFile header){
        if(tableTag == TTFTable.LOCA){}
        if(tableTag == TTFTable.GLYF){}
        if(tableTag == TTFTable.DSIG){}
        if(tableTag == TTFTable.NAME){}
        if(tableTag == TTFTable.POST){}
        if(tableTag == TTFTable.CMAP){}
        if(tableTag == TTFTable.HMTX){
            short numOfLongHorMetrics = (short)header.getTableValue(TTFTable.HHEA).getValues();
            short numGlyphs = (short)header.getTableValue(TTFTable.MAXP).getValues();
            info = new TTFhmtxInfo(numOfLongHorMetrics,numGlyphs);
        }
        if(tableTag == TTFTable.CVT){}
        if(tableTag == TTFTable.GASP){}
        if(tableTag == TTFTable.FFTM){}
        if(tableTag == TTFTable.HEAD){info = new TTFheadInfo();}
        if(tableTag == TTFTable.HHEA){info = new TTFhheaInfo();}
        if(tableTag == TTFTable.MAXP){info = new TTFmaxpInfo();}
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

    public void setValue(TTFFile header){header.setTableValue(tableTag,info);}

    public void convertToSize(byte[] buf){
        info.setValues(buf);
    }
}

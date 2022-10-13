package helper.font.ttf;
import helper.enums.TTFTable;
import helper.interfaces.ITTFTableInfo;

public class TTFTableBase{
    public ITTFTableInfo info;
    public TTFTable tableTag;
    public TTFTableBase(TTFTable tag){
        tableTag = tag;
    }
    public void setSelf(TTFFile header){
        if(tableTag == TTFTable.LOCA){
            short indexToLocalFormat = (short)header.getTableValue(TTFTable.HEAD).getValues();
            short numGlyphs = (short)header.getTableValue(TTFTable.MAXP).getValues();
            info = new TTFlocaInfo(indexToLocalFormat,numGlyphs);
            return;
        }
        if(tableTag == TTFTable.GLYF){
            short indexToLocalFormat = (short)header.getTableValue(TTFTable.HEAD).getValues();
            Object loca = header.getTableValue(TTFTable.LOCA).getValues();
            info = new TTFglyfInfo(indexToLocalFormat,loca);
            return;
        }
        if(tableTag == TTFTable.DSIG){return;}
        if(tableTag == TTFTable.NAME){return;}
        if(tableTag == TTFTable.POST){return;}
        if(tableTag == TTFTable.CMAP){info = new TTFcmapInfo();return;}
        if(tableTag == TTFTable.HMTX){
            short numOfLongHorMetrics = (short)header.getTableValue(TTFTable.HHEA).getValues();
            short numGlyphs = (short)header.getTableValue(TTFTable.MAXP).getValues();
            info = new TTFhmtxInfo(numOfLongHorMetrics,numGlyphs);
            return;
        }
        if(tableTag == TTFTable.CVT){return;}
        if(tableTag == TTFTable.GASP){return;}
        if(tableTag == TTFTable.FFTM){return;}
        if(tableTag == TTFTable.HEAD){info = new TTFheadInfo();return;}
        if(tableTag == TTFTable.HHEA){info = new TTFhheaInfo();return;}
        if(tableTag == TTFTable.MAXP){info = new TTFmaxpInfo();return;}
        if(tableTag == TTFTable.GDEF){return;}
        if(tableTag == TTFTable.OS_2){return;}
    }

    public int getOffset(TTFFile header){
        return header.getTableTag(tableTag).offset;
    }

    public int getLength(TTFFile header){
        return header.getTableTag(tableTag).length;
    }

    public int getAddedOffset(){
        //if(tableTag == TTFTable.LOCA)return 1;
        //if(tableTag == TTFTable.GLYF)return 10;
        return 0;
    }

    public void setValue(TTFFile header){header.setTableValue(tableTag,info);}

    public void convertToSize(byte[] buf){
        info.setValues(buf);
    }
}

package helper.font.ttf;
import helper.enums.TTFBits;
import helper.enums.TTFTable;
import helper.interfaces.ITTFTableInfo;
import helper.io.IOHandler;
import helper.list.SMHashMap;
import helper.struct.FontChar;
import helper.struct.Glyf;
import helper.struct.HMetrics;

import static helper.methods.CommonMethods.bytesToInt;

public class TTFFile {
    public String path;
    public int scalarType;
    public short numTables,searchRange,entrySelector,rangeShift,unitsPerEM;
    public String[] fileInfo;
    public SMHashMap table;
    public FontChar[] map;
    public SMHashMap glyphIndexMap;
    public Glyf[] glyfs;
    public HMetrics[] hMetrics;
    public TTFFile(String dir){
        path = dir;
        setTable();
    }

    public void setUpFontMap(){
        TTFheadInfo head = (TTFheadInfo)getTableValue(TTFTable.HEAD);
        unitsPerEM = head.unitsPerEM;
        glyphIndexMap = (SMHashMap)getTableValue(TTFTable.CMAP).getValues();
        glyfs = (Glyf[])getTableValue(TTFTable.GLYF).getValues();
        hMetrics = (HMetrics[])getTableValue(TTFTable.HMTX).getValues();
    }

    public void setUpCharMap(){
        char[] alphabet = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~".toCharArray();
        map = new FontChar[alphabet.length];
        Glyf glyf;
        HMetrics hmetric;
        Object m;
        for(char c:alphabet){
            m = glyphIndexMap.getValue("%s".formatted((int)c));
            if(m!= null){
                int index = (int)m;
                glyf = glyfs[index];
                hmetric = hMetrics[index];
                map[c-' '] = new FontChar(c,glyf.xMin,glyf.xMax,glyf.yMin,glyf.yMax,hmetric.leftSideBearing,hmetric.advanceWidth);
            }
        }
    }

    public void clearTable(){
        table = null;
    }

    public void dumpCharMap(){
        IOHandler.printFontCharMap(map);
    }

    public void convertToSize(TTFBits dst, byte[] buf){
        switch(dst){
            case SCALAR_TYPE:{
                scalarType = bytesToInt(buf,0,4,false);
                break;
            }
            case NUM_TABLES:{
                numTables = (short)bytesToInt(buf,0,2,false);
                break;
            }
            case SEARCH_RANGE:{
                searchRange = (short)bytesToInt(buf,0,2,false);
                break;
            }
            case ENTRY_SELECTOR:{
                entrySelector = (short)bytesToInt(buf,0,2,false);
                break;
            }
            case RANGE_SHIFT:{
                rangeShift = (short)bytesToInt(buf,0,2,false);
                break;
            }
        }
    }

    public TTFTableTag getTableTag(TTFTable tag){
        return (TTFTableTag)table.getObject(tag.getValue()).value;
    }

    public void setTableValue(TTFTable tag, ITTFTableInfo obj){
        ((TTFTableTag)table.getObject(tag.getValue()).value).setTableValues(obj);
    }

    public ITTFTableInfo getTableValue(TTFTable tag){
        return ((TTFTableTag)table.getObject(tag.getValue()).value).getTableValue();
    }

    public void setTable(){
        table = new SMHashMap(100,.75f);
    }

    public void setFileInfo(){
        fileInfo = new String[5];
        fileInfo[0] = ("ScalarType   : %d".formatted(scalarType));
        fileInfo[1] = ("NumTables    : %d".formatted(numTables));
        fileInfo[2] = ("SearchRange  : %d".formatted(searchRange));
        fileInfo[3] = ("EntrySelector: %s".formatted(entrySelector));
        fileInfo[4] = ("RangeShift   : %d".formatted(rangeShift));
    }

    public String[] getFileInfo(){
        return fileInfo;
    }

    public void printFileInfo(){
        IOHandler.printTTFFileInfo(getFileInfo());
    }

    public void printTableInfo(){
        IOHandler.printHashMap(table);
    }

}

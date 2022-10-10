package helper.struct;
import helper.enums.TTFBits;
import helper.enums.TTFTable;
import helper.io.IOHandler;
import helper.list.SMHashMap;

import static helper.methods.CommonMethods.bytesToInt;


public class TTFFile {
    public String path;
    public int scalarType;
    public short numTables,searchRange,entrySelector,rangeShift;
    public String[] fileInfo;
    public SMHashMap table;
    public TTFFile(String dir){
        path = dir;
        setTable();
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

    public TTFTag getTag(TTFTable tag){
        return (TTFTag)table.getObject(tag.getValue()).value;
    }

    public void setTable(){
        table = new SMHashMap(100,.75f);
    }

    public void setHeadData(){


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

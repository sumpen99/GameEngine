package helper.sql;
import helper.enums.*;
import helper.io.IOHandler;
import helper.struct.VarInt;

import static helper.enums.ErrorCodes.*;
import static helper.enums.SqliteHeaderBits.*;
import static helper.methods.CommonMethods.*;
import static helper.methods.IntToEnum.*;


public class BTreePage extends SqlPage{
    public BTreePageType pageType;
    public int startFirstFreeBlock,numberOfCells,startOfCellContentArea,numberOfFragmentedFreeBytes;
    public long pageNumber;
    public long rightPointer;
    public int cellPointerIndex = 8;

    public BTreePage(){
        super(SqlPageType.B_TREE_PAGE);
    }

    public void readHeaderInfo(byte[] pageContent){
        convertToSize(PAGE_TYPE,pageContent);
        convertToSize(START_FRIST_FREE_BLOCK,pageContent);
        convertToSize(NUMBER_OF_CELLS,pageContent);
        convertToSize(START_OF_CELL_CONTENT_AREA,pageContent);
        convertToSize(NUMBER_OF_FRAGMENTED_FREE_BYTES,pageContent);
        convertToSize(RIGHT_POINTER,pageContent);
        readCellContent(pageContent);
    }

    public void convertToSize(SqliteHeaderBits dst,byte[] pageContent){
        switch(dst){
            case PAGE_TYPE:{
                pageType = intToBTreePageType(bytesToInt(pageContent,0,1,false));
                validPageType();
                break;
            }
            case START_FRIST_FREE_BLOCK:{
                startFirstFreeBlock = castShortHexToInt(bytesToIntHex(pageContent,1,2,false));
                break;
            }
            case NUMBER_OF_CELLS:{
                numberOfCells = castShortHexToInt(bytesToIntHex(pageContent,3,2,false));
                break;
            }
            case START_OF_CELL_CONTENT_AREA:{
                startOfCellContentArea = castShortHexToInt(bytesToIntHex(pageContent,5,2,false));
                validStartOfCellContentArea();
                break;
            }
            case NUMBER_OF_FRAGMENTED_FREE_BYTES:{
                numberOfFragmentedFreeBytes = bytesToInt(pageContent,7,1,false);
                break;
            }
            case RIGHT_POINTER:{
                if(shouldReadPageNumber()){
                    cellPointerIndex = 12;
                    rightPointer = castIntHexToLong(bytesToIntHex(pageContent,8,4,false));
                }
                break;
            }
        }
    }

    void readCellContent(byte[] pageContent){
        int cell = 0;
        long pageNumber = 0;
        while(cell < numberOfCells){
            int index = cell*2+cellPointerIndex;
            int cellPtr = castShortHexToInt(bytesToIntHex(pageContent,index,2,false));
            if(shouldReadPageNumber()){
                pageNumber = castIntHexToLong(bytesToIntHex(pageContent,cellPtr,4,false));
                //IOHandler.printLong(pageNumber);
                cellPtr+=4;
            }
            VarInt vInt = getVarInt(pageContent,cellPtr);
            IOHandler.printString(vInt.toString());
            cellPtr+=vInt.p1;
            cellPtr+=updateCellPtr(pageContent,cellPtr);

            cell++;
        }

    }

    int updateCellPtr(byte[] pageContent,int cellPtr){
        switch(pageType){
            /*case INTERIOR_INDEX:{ //0x02
                return 0;
            }
            case INTERIOR_TABLE:{ // 0x05
                return 0;
            }
            case LEAF_INDEX:{ // 0x0a
                return 0;
            }
            case UNKNOWN:{
                return 0;
                break;
            }
            */
            case LEAF_TABLE:{ // 0x0d
                VarInt vInt = getVarInt(pageContent,cellPtr);
                return vInt.p1;
            }
        }
        return 0;
    }

    boolean shouldReadPageNumber(){
        return (pageType == BTreePageType.INTERIOR_INDEX || pageType == BTreePageType.INTERIOR_TABLE);
    }

    void validStartOfCellContentArea(){
        if(startOfCellContentArea == 0){startOfCellContentArea = 65536;}
    }

    public void validPageType(){
        if(pageType == BTreePageType.UNKNOWN){errorCodes.add(PAGE_TYPE_MISMATCH);}
    }

    @Override
    public String toString(){
        return  "PAGE_TYPE -> %s".formatted(pageType.name()) + "\n" +
                "START_FIRST_FREE_BLOCK -> %d".formatted(startFirstFreeBlock) + "\n" +
                "NUMBER_OF_CELLS -> %d".formatted(numberOfCells) + "\n" +
                "START_OF_CELL_CONTENT_AREA -> %d".formatted(startOfCellContentArea) + "\n" +
                "NUMBER_OF_FRAGMENTED_FREE_BYTES -> %d".formatted(numberOfFragmentedFreeBytes) + "\n" +
                "RIGHT_POINTER -> %d".formatted(rightPointer);
    }

}

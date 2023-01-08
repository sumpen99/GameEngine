package helper.struct;
import helper.enums.*;
import helper.io.IOHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import static helper.enums.ErrorCodes.*;
import static helper.enums.SqliteBits.MAGIC_STRING;
import static helper.enums.SqliteBits.PAGE_SIZE;
import static helper.enums.SqliteHeaderBits.*;
import static helper.methods.CommonMethods.*;
import static helper.methods.IntToEnum.*;


public class BTreePageHeader {
    public ArrayList<ErrorCodes> errorCodes;
    public BTreePageType pageType;
    public int startFirstFreeBlock,numberOfCells,startOfCellContentArea,numberOfFragmentedFreeBytes;
    public long pageNumber;
    public int cellPointerIndex = 8;

    public BTreePageHeader(){
        errorCodes = new ArrayList<>();
    }

    public void readHeaderInfo(byte[] pageContent){
        convertToSize(PAGE_TYPE,pageContent);
        convertToSize(START_FRIST_FREE_BLOCK,pageContent);
        convertToSize(NUMBER_OF_CELLS,pageContent);
        convertToSize(START_OF_CELL_CONTENT_AREA,pageContent);
        convertToSize(NUMBER_OF_FRAGMENTED_FREE_BYTES,pageContent);
        convertToSize(PAGE_NUMBER,pageContent);
    }

    public void convertToSize(SqliteHeaderBits dst,byte[] pageContent){
        switch(dst){
            case PAGE_TYPE:{
                pageType = intToBTreePageType(bytesToInt(pageContent,0,1,false));
                validPageType();
                break;
            }
            case START_FRIST_FREE_BLOCK:{
                startFirstFreeBlock = bytesToInt(pageContent,1,2,false);
                break;
            }
            case NUMBER_OF_CELLS:{
                numberOfCells = bytesToInt(pageContent,3,2,false);
                break;
            }
            case START_OF_CELL_CONTENT_AREA:{
                startOfCellContentArea = bytesToInt(pageContent,5,2,false);
                validStartOfCellContentArea();
                break;
            }
            case NUMBER_OF_FRAGMENTED_FREE_BYTES:{
                numberOfFragmentedFreeBytes = (short)bytesToInt(pageContent,7,1,false);
                break;
            }
            case PAGE_NUMBER:{
                if(shouldReadPageNumber()){
                    cellPointerIndex = 12;
                    pageNumber = bytesToInt(pageContent,8,4,false);
                    validateUint32(pageNumber,PAGE_NUMBER_MISMATCH);
                }
                break;
            }
        }
    }

    boolean shouldReadPageNumber(){
        return (pageType == BTreePageType.INTERIOR_INDEX || pageType == BTreePageType.INTERIOR_TABLE);
    }

    void validStartOfCellContentArea(){
        if(startOfCellContentArea == 0){startOfCellContentArea = 65536;}
    }

    void validPageType(){
        if(pageType == BTreePageType.UNKNOWN){errorCodes.add(PAGE_TYPE_MISMATCH);}
    }

    void validateUint32(long value,ErrorCodes code){
        if(!validUint32(value)){errorCodes.add(code);}
    }

    public void printHeaderInfo(){
        IOHandler.printString("PAGE_TYPE -> %s".formatted(pageType.name()));
        IOHandler.printString("START_FIRST_FREE_BLOCK -> %d".formatted(startFirstFreeBlock));
        IOHandler.printString("NUMBER_OF_CELLS -> %d".formatted(numberOfCells));
        IOHandler.printString("START_OF_CELL_CONTENT_AREA -> %d".formatted(startOfCellContentArea));
        IOHandler.printString("NUMBER_OF_FRAGMENTED_FREE_BYTES -> %d".formatted(numberOfFragmentedFreeBytes));
        IOHandler.printString("PAGE_NUMBER -> %d".formatted(pageNumber));
    }

    public void showUserErrorMessage(){
        if(errorCodes.size() > 0){IOHandler.printErrorCodes(errorCodes);}
    }

}

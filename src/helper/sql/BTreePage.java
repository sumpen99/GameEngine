package helper.sql;
import helper.enums.*;
import helper.io.IOHandler;
import static helper.enums.ErrorCodes.*;
import static helper.enums.SqliteHeaderBits.*;
import static helper.methods.CommonMethods.*;
import static helper.methods.IntToEnum.*;


public class BTreePage extends SqlPage{
    public BTreePageType pageType;
    public int startFirstFreeBlock,numberOfCells,startOfCellContentArea,numberOfFragmentedFreeBytes;
    public long pageNumber;
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
            case PAGE_NUMBER:{
                if(shouldReadPageNumber()){
                    cellPointerIndex = 12;
                    pageNumber = castIntHexToLong(bytesToIntHex(pageContent,8,4,false));
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

    public void validPageType(){
        if(pageType == BTreePageType.UNKNOWN){errorCodes.add(PAGE_TYPE_MISMATCH);}
    }

    @Override
    public String toString(){
        return  "PAGE_TYPE -> %s".formatted(pageType.name()) + "\n" +
                "START_FIRST_FREE_BLOCK -> %d".formatted(startFirstFreeBlock) + "\n" +
                "NUMBER_OF_CELLS -> %d".formatted(numberOfCells) +
                "START_OF_CELL_CONTENT_AREA -> %d".formatted(startOfCellContentArea) + "\n" +
                "NUMBER_OF_FRAGMENTED_FREE_BYTES -> %d".formatted(numberOfFragmentedFreeBytes) + "\n" +
                "PAGE_NUMBER -> %d".formatted(pageNumber);
    }

}

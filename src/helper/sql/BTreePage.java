package helper.sql;
import helper.enums.*;
import helper.io.IOHandler;
import helper.struct.ColDataHtml;
import helper.struct.VarInt;
import static helper.enums.ErrorCodes.*;
import static helper.enums.SqliteHeaderBits.*;
import static helper.methods.CommonMethods.*;
import static helper.methods.IntToEnum.*;


public class BTreePage extends SqlPage{
    public BTreePageType pageType;
    public int startFirstFreeBlock,numberOfCells,startOfCellContentArea,numberOfFragmentedFreeBytes;
    public long rightPointer;
    public int cellPointerIndex = 8,xFactor;

    public BTreePage(SqliteFile root){
        super(root,SqlPageType.B_TREE_PAGE);

    }

    public void readHeaderInfo(byte[] pageContent){
        convertToSize(PAGE_TYPE,pageContent);
        convertToSize(START_FRIST_FREE_BLOCK,pageContent);
        convertToSize(NUMBER_OF_CELLS,pageContent);
        convertToSize(START_OF_CELL_CONTENT_AREA,pageContent);
        convertToSize(NUMBER_OF_FRAGMENTED_FREE_BYTES,pageContent);
        convertToSize(RIGHT_POINTER,pageContent);
        //readCellContent(pageContent);
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
            cellPtr+=vInt.p1;
            cellPtr+=updateCellPtr(pageContent,cellPtr);

            if(pageType!=BTreePageType.INTERIOR_TABLE){
                int P = vInt.p0;
                int pageNo = 0;
                byte[] oarr = new byte[0];
                if(P>xFactor){
                    int M = root.minMaxLeafLocal.minLeaf;
                    int ovflwMaxPageBytes = (root.minMaxLeafLocal.usableSize- 4);
                    int K = M + (P - M) % ovflwMaxPageBytes;
                    int surplus = P - (K > xFactor ? M : K);
                    int dataEnd = cellPtr + P - surplus;
                    pageNo = bytesToInt(pageContent,dataEnd,4,false);
                    oarr = new byte[P];
                    for(int k = cellPtr;k<dataEnd;k++){
                        oarr[k-cellPtr] = pageContent[k];
                    }

                    int oPageNo = pageNo;
                    while(surplus>0){
                        int toRead = (Math.min(surplus, ovflwMaxPageBytes)) + 4;
                        byte[] obuf = root.getPage(0,oPageNo,toRead);
                        if(obuf != null){
                            IOHandler.printInt(obuf.length);
                            toRead -= 4;
                            for(var k = 0; k < toRead; k++){ oarr[k + dataEnd - cellPtr] = obuf[k + 4];}
                            oPageNo = bytesToInt(obuf,0,4,false);
                            if(oPageNo == 0){break;}
                            dataEnd += toRead;
                        }
                        surplus -= ovflwMaxPageBytes;
                    }
                }
                //ColDataHtml hdrDtl = formColDataHtml((P>xFactor?oarr:pageContent),)
            }



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
            }*/
            case LEAF_TABLE:{ // 0x0d
                xFactor = root.minMaxLeafLocal.maxLeaf;
                VarInt vInt = getVarInt(pageContent,cellPtr);
                return vInt.p1;
            }
        }
        xFactor = root.minMaxLeafLocal.maxLocal;
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

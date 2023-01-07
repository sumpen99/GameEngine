package helper.sql;
import helper.enums.ErrorCodes;
import helper.enums.FormatVersion;
import helper.enums.SqliteBits;
import helper.io.IOHandler;
import helper.methods.IntToEnum;
import helper.struct.FormatFour;
import helper.struct.PassedCheck;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import static helper.enums.ErrorCodes.*;
import static helper.methods.CommonMethods.*;

public class SqliteFile {
    public PassedCheck passedCheck = new PassedCheck();
    public ArrayList<ErrorCodes> errorCodes;
    public String path;
    public int pageSize;
    public byte[] magicString;
    public FormatVersion formatVersionRead;
    public FormatVersion formatVersionWrite;
    public short searchRange,entrySelector;

    public SqliteFile(String pathToFile){
        path = pathToFile;
        errorCodes = new ArrayList<ErrorCodes>();
    }

    public void convertToSize(SqliteBits dst, byte[] buf){
        switch(dst){
            case MAGIC_STRING:{
                magicString = buf;
                validateMagicString();
                break;
            }
            case PAGE_SIZE:{
                pageSize = bytesToInt(buf,0,2,false);
                validatePageSize();
                break;
            }
            case FORMAT_VERSION_WRITE:{
                formatVersionWrite = IntToEnum.shortToFormatVersion((short)bytesToInt(buf,0,1,false));
                validateFormatVersionWrite();
                break;
            }
            case FORMAT_VERSION_READ:{
                formatVersionRead = IntToEnum.shortToFormatVersion((short)bytesToInt(buf,0,1,false));
                validateFormatVersionRead();
                break;
            }


        }
    }

    private void validateFormatVersionWrite(){
        if(formatVersionWrite == FormatVersion.UNKNOWN){errorCodes.add(FORMAT_VERSION_WRITE_UNKNOWN);}
    }

    private void validateFormatVersionRead(){
        if(formatVersionRead == FormatVersion.UNKNOWN){errorCodes.add(FORMAT_VERSION_READ_UNKNOWN);}
    }

    private void validateMagicString(){
        byte[] corectMagic = new byte[]{83,81,76,105,116,101,32,102,111,114,109,97,116,32,51,0};
        if(!Arrays.equals(magicString, corectMagic)){errorCodes.add(MAGIC_NUMBER_MISMATCH);}
    }

    private void validatePageSize(){
        if(pageSize<512){errorCodes.add(PAGE_SIZE_TO_SMALL);}
        else if(pageSize>65536){errorCodes.add(PAGE_SIZE_TO_BIG);}
        else if(!intIsPowerOfTwo(pageSize)){errorCodes.add(PAGE_SIZE_NOT_A_POWER_OF_TWO);}
    }

    public void printFileInfo(){
        IOHandler.printString("Magic String");
        IOHandler.printCharBufInLine(new String(magicString,StandardCharsets.UTF_8).toCharArray(),true);
        IOHandler.printString("\nPage Size\n%d".formatted(pageSize));
        IOHandler.printString("Format Version Write\n%s".formatted(formatVersionWrite.name()));
        IOHandler.printString("Format Version Read\n%s".formatted(formatVersionWrite.name()));

    }

    public void showUserErrorMessage(){
        if(passedCheck.message != null){ IOHandler.printString(passedCheck.message);}
        if(errorCodes.size() > 0){IOHandler.printErrorCodes(errorCodes);}
    }

}

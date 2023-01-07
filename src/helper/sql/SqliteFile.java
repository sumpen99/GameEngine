package helper.sql;
import helper.enums.*;
import helper.io.IOHandler;
import helper.struct.FreePageListInfo;
import helper.struct.PassedCheck;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import static helper.enums.ErrorCodes.*;
import static helper.methods.CommonMethods.*;
import static helper.methods.IntToEnum.*;

public class SqliteFile {
    public PassedCheck passedCheck = new PassedCheck();
    public ArrayList<ErrorCodes> errorCodes;
    public String path;
    public FreePageListInfo freePage;
    public long changeCounter,databaseSize,firstFreePage,firstFreePageLen,schemaCookie,cacheSize,vacuumSettingsRaw,isIncremental,applicationId;
    public int pageSize,userVersion;
    public byte[] magicString;
    public short reservedBytes,fractionMax,fractionMin,fractionLeaf;
    public FormatVersion formatVersionRead;
    public FormatVersion formatVersionWrite;
    public SchemaVersion schemaVersion;
    public VacuumSetting vacuumSetting;
    public TextEncoding textEncoding;

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
                formatVersionWrite = shortToFormatVersion((short)bytesToInt(buf,0,1,false));
                validateFormatVersionWrite();
                break;
            }
            case FORMAT_VERSION_READ:{
                formatVersionRead = shortToFormatVersion((short)bytesToInt(buf,0,1,false));
                validateFormatVersionRead();
                break;
            }
            case RESERVED_BYTES:{
                reservedBytes = (short)bytesToInt(buf,0,1,false);
                validateReservedBytes();
                break;
            }
            case FRACTION_MAX:{
                fractionMax = (short)bytesToInt(buf,0,1,false);
                validateFraction(fractionMax,(short)64,FRACTION_MAX_MISMATH);
                break;
            }
            case FRACTION_MIN:{
                fractionMin = (short)bytesToInt(buf,0,1,false);
                validateFraction(fractionMin,(short)32,FRACTION_MIN_MISMATH);
                break;
            }
            case FRACTION_LEAF:{
                fractionLeaf = (short)bytesToInt(buf,0,1,false);
                validateFraction(fractionLeaf,(short)32,FRACTION_LEAF_MISMATH);
                break;
            }
            case CHANGE_COUNTER:{
                changeCounter = bytesToInt(buf,0,4,false);
                valideUint32(changeCounter,CHANGE_COUNTER_MISMATH);
                break;
            }
            case DATABASE_SIZE:{
                databaseSize = bytesToInt(buf,0,4,false);
                valideUint32(databaseSize,DATABASE_SIZE_MISMATCH);
                break;
            }
            case FIRST_FREE_PAGE:{
                firstFreePage = bytesToInt(buf,0,4,false);
                valideUint32(firstFreePage,FIRST_FREE_PAGE_MISMATCH);
                break;
            }
            case FIRST_FREE_PAGE_LEN:{
                firstFreePageLen = bytesToInt(buf,0,4,false);
                valideUint32(firstFreePageLen,FIRST_FREE_PAGE_LENGTH_MISMATCH);
                validateFreePageInfo();
                break;
            }
            case SCHEMA_COOKIE:{
                schemaCookie = bytesToInt(buf,0,4,false);
                valideUint32(schemaCookie,SCHEMA_COOKIE_MISMATCH);
                break;
            }
            case SCHEMA_VERSION:{
                schemaVersion = intToSchemaVersion(bytesToInt(buf,0,4,false));
                validSchemaVersion();
                break;
            }
            case CACHE_SIZE:{
                cacheSize = bytesToInt(buf,0,4,false);
                valideUint32(cacheSize,CACHE_SIZE_MISMATCH);
                break;
            }
            case VACUUM_SETTING:{
                vacuumSettingsRaw = bytesToInt(buf,0,4,false);
                break;
            }
            case TEXT_ENCODING:{
                textEncoding = intToTextEncoding(bytesToInt(buf,0,4,false));
                validTextEncoding();
                break;
            }
            case USER_VERSION:{
                userVersion = bytesToInt(buf,0,4,false);
                break;
            }
            case INCREMENTAL_VACUUM:{
                isIncremental = bytesToInt(buf,0,4,false);
                validVacuumSetting();
                break;
            }
            case APPLICATION_ID:{
                applicationId = bytesToInt(buf,0,4,false);
                valideUint32(applicationId,APPLICATION_ID_MISMATCH);
                break;
            }
        }
    }

    void validTextEncoding(){
        if(textEncoding == TextEncoding.UNKNOWN){errorCodes.add(TEXT_ENCODING_MISMATCH);}
    }

    void validVacuumSetting(){
        vacuumSetting = intToVacuumSetting(vacuumSettingsRaw,isIncremental);
        if(vacuumSetting == VacuumSetting.UNKNOWN){errorCodes.add(VACUUM_SETTINGS_MISMATCH);}
    }

    void validSchemaVersion(){
        if(schemaVersion == SchemaVersion.UNKNOWN){errorCodes.add(SCHEMA_VERSION_MISMATCH);}
    }

    void validateFreePageInfo(){
        freePage = new FreePageListInfo(firstFreePage,firstFreePageLen);
    }

    void valideUint32(long value,ErrorCodes code){
        if(!validUint32(value)){errorCodes.add(code);}
    }

    void validateFraction(short valueIs,short valueHasToBe,ErrorCodes code){
        if(valueIs != valueHasToBe){errorCodes.add(code);}
    }

    void validateReservedBytes(){
        if(reservedBytes < 0){errorCodes.add(RESERVED_BYTES_MISMATCH);}
    }

    void validateFormatVersionWrite(){
        if(formatVersionWrite == FormatVersion.UNKNOWN){errorCodes.add(FORMAT_VERSION_WRITE_UNKNOWN);}
    }

    void validateFormatVersionRead(){
        if(formatVersionRead == FormatVersion.UNKNOWN){errorCodes.add(FORMAT_VERSION_READ_UNKNOWN);}
    }

    void validateMagicString(){
        byte[] corectMagic = new byte[]{83,81,76,105,116,101,32,102,111,114,109,97,116,32,51,0};
        if(!Arrays.equals(magicString, corectMagic)){errorCodes.add(MAGIC_NUMBER_MISMATCH);}
    }

    void validatePageSize(){
        if(pageSize<512){errorCodes.add(PAGE_SIZE_TO_SMALL);}
        else if(pageSize>65536){errorCodes.add(PAGE_SIZE_TO_BIG);}
        else if(!intIsPowerOfTwo(pageSize)){errorCodes.add(PAGE_SIZE_NOT_A_POWER_OF_TWO);}
    }

    public void printFileInfo(){
        IOHandler.printString("Magic String");
        IOHandler.printCharBufInLine(new String(magicString,StandardCharsets.UTF_8).toCharArray(),true);
        IOHandler.printString("\nPage Size\n%d".formatted(pageSize));
        IOHandler.printString("Write Version\n%s".formatted(formatVersionWrite.name()));
        IOHandler.printString("Read Version\n%s".formatted(formatVersionWrite.name()));
        IOHandler.printString("Reserved Bytes\n%d".formatted(reservedBytes));
        IOHandler.printString("Fraction Max\n%d".formatted(fractionMax));
        IOHandler.printString("Fraction Min\n%d".formatted(fractionMin));
        IOHandler.printString("Fraction Leaf\n%d".formatted(fractionLeaf));
        IOHandler.printString("Change Counter\n%d".formatted(changeCounter));
        IOHandler.printString("Database Size\n%d".formatted(databaseSize));
        IOHandler.printString("Free Page\n%s".formatted(freePage.toString()));
        IOHandler.printString("Schema Cookie\n%d".formatted(schemaCookie));
        IOHandler.printString("Schema Version\n%s".formatted(schemaVersion.name()));
        IOHandler.printString("Cache Size\n%d".formatted(cacheSize));
        IOHandler.printString("Textencoding\n%s".formatted(textEncoding.name()));
        IOHandler.printString("Userversion\n%d".formatted(userVersion));
        IOHandler.printString("Vacuum settings\n%s (%d) isincremental (%d)".formatted(vacuumSetting.name(),vacuumSettingsRaw,isIncremental));
        IOHandler.printString("Application ID\n%d".formatted(applicationId));

    }

    public void showUserErrorMessage(){
        if(passedCheck.message != null){ IOHandler.printString(passedCheck.message);}
        if(errorCodes.size() > 0){IOHandler.printErrorCodes(errorCodes);}
    }

}

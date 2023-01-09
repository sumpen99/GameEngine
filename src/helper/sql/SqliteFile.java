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
//https://freemasen.com/blog/sqlite-parser-pt-1/
//https://freemasen.com/blog/sqlite-parser-pt-2/
//https://freemasen.com/blog/sqlite-parser-pt-3/
//https://github.com/FreeMasen/WiredForge.com/blob/main/content/blog/

//https://www.sqlite.org/fileformat2.html#btree
public class SqliteFile {
    public PassedCheck passedCheck = new PassedCheck();
    public ArrayList<ErrorCodes> errorCodes;
    public String path;
    public FreePageListInfo freePage;
    public boolean reservedZeros;
    public long changeCounter,
            databaseSize,
            firstFreePage,
            firstFreePageLen,
            schemaCookie,
            cacheSize,
            vacuumSettingsRaw,
            isIncremental,
            applicationId,
            versionValidFor,
            libraryWriteVersion,
            totalFileSize,
            userVersion;
    public int pageSize,reservedBytes,fractionMax,fractionMin,fractionLeaf;
    public byte[] magicString;
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
                pageSize = castShortHexToInt(bytesToIntHex(buf,0,2,false));
                validatePageSize();
                break;
            }
            case FORMAT_VERSION_WRITE:{
                formatVersionWrite = intToFormatVersion(bytesToInt(buf,0,1,false));
                validateFormatVersionWrite();
                break;
            }
            case FORMAT_VERSION_READ:{
                formatVersionRead = intToFormatVersion(bytesToInt(buf,0,1,false));
                validateFormatVersionRead();
                break;
            }
            case RESERVED_BYTES:{
                reservedBytes = bytesToInt(buf,0,1,false);
                validateReservedBytes();
                break;
            }
            case FRACTION_MAX:{
                fractionMax = bytesToInt(buf,0,1,false);
                validateFraction(fractionMax,64,FRACTION_MAX_MISMATH);
                break;
            }
            case FRACTION_MIN:{
                fractionMin = bytesToInt(buf,0,1,false);
                validateFraction(fractionMin,32,FRACTION_MIN_MISMATH);
                break;
            }
            case FRACTION_LEAF:{
                fractionLeaf = bytesToInt(buf,0,1,false);
                validateFraction(fractionLeaf,32,FRACTION_LEAF_MISMATH);
                break;
            }
            case CHANGE_COUNTER:{
                changeCounter = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(changeCounter,CHANGE_COUNTER_MISMATH);
                break;
            }
            case DATABASE_SIZE:{
                databaseSize = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(databaseSize,DATABASE_SIZE_MISMATCH);
                break;
            }
            case FIRST_FREE_PAGE:{
                firstFreePage = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(firstFreePage,FIRST_FREE_PAGE_MISMATCH);
                break;
            }
            case FIRST_FREE_PAGE_LEN:{
                firstFreePageLen = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(firstFreePageLen,FIRST_FREE_PAGE_LENGTH_MISMATCH);
                validateFreePageInfo();
                break;
            }
            case SCHEMA_COOKIE:{
                schemaCookie = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(schemaCookie,SCHEMA_COOKIE_MISMATCH);
                break;
            }
            case SCHEMA_VERSION:{
                schemaVersion = intToSchemaVersion(bytesToInt(buf,0,4,false));
                validSchemaVersion();
                break;
            }
            case CACHE_SIZE:{
                cacheSize = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(cacheSize,CACHE_SIZE_MISMATCH);
                break;
            }
            case VACUUM_SETTING:{
                vacuumSettingsRaw = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(vacuumSettingsRaw,VACUUM_SETTINGS_RAW_MISMATCH);
                break;
            }
            case TEXT_ENCODING:{
                textEncoding = intToTextEncoding(bytesToInt(buf,0,4,false));
                validTextEncoding();
                break;
            }
            case USER_VERSION:{
                userVersion = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(userVersion,USER_VERSION_MISMATCH);
                break;
            }
            case INCREMENTAL_VACUUM:{
                isIncremental = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(userVersion,IS_INCREMENTAL_MISMATCH);
                validVacuumSetting();
                break;
            }
            case APPLICATION_ID:{
                applicationId = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(applicationId,APPLICATION_ID_MISMATCH);
                break;
            }
            case RESERVED_ZEROS:{
                reservedZeros = checkNonZeroValue(buf);
                validateZeroValue(reservedZeros);
                break;
            }
            case VERSION_VALID_FOR:{
                versionValidFor = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(versionValidFor,VERSION_VALID_FOR_MISMATCH);
                break;
            }
            case LIBRARY_WRITE_VERSION:{
                libraryWriteVersion = castIntHexToLong(bytesToIntHex(buf,0,4,false));
                validateUint32(libraryWriteVersion,LIBRARY_WRITE_VERSION_MISMATCH);
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

    void validateUint32(long value,ErrorCodes code){
        if(!validUint32(value)){errorCodes.add(code);}
    }

    void validateZeroValue(boolean value){
        if(!value){errorCodes.add(RESERVED_ZEROS_MISMATCH);}
    }

    void validateFraction(int valueIs,int valueHasToBe,ErrorCodes code){
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

    public long getTotalFileSize(){
        return pageSize*databaseSize;
    }

    public void printFileInfo(){
        IOHandler.printString("Magic String equals             -> (83,81,76,105,116,101,32,102,111,114,109,97,116,32,51,0)");
        //IOHandler.printCharBufInLine(new String(magicString,StandardCharsets.UTF_8).toCharArray(),true);
        //IOHandler.printString("");
        IOHandler.printString("Page Size                       -> %d".formatted(pageSize));
        IOHandler.printString("Write Version                   -> %s".formatted(formatVersionWrite.name()));
        IOHandler.printString("Read Version                    -> %s".formatted(formatVersionWrite.name()));
        IOHandler.printString("Reserved Bytes                  -> %d".formatted(reservedBytes));
        IOHandler.printString("Fraction Max                    -> %d".formatted(fractionMax));
        IOHandler.printString("Fraction Min                    -> %d".formatted(fractionMin));
        IOHandler.printString("Fraction Leaf                   -> %d".formatted(fractionLeaf));
        IOHandler.printString("Change Counter                  -> %d".formatted(changeCounter));
        IOHandler.printString("Database Size (number of pages) -> %d".formatted(databaseSize));
        IOHandler.printString("Free Page                       -> %s".formatted(freePage.toString()));
        IOHandler.printString("Schema Cookie                   -> %d".formatted(schemaCookie));
        IOHandler.printString("Schema Version                  -> %s".formatted(schemaVersion.name()));
        IOHandler.printString("Cache Size                      -> %d".formatted(cacheSize));
        IOHandler.printString("Textencoding                    -> %s".formatted(textEncoding.name()));
        IOHandler.printString("Userversion                     -> %d".formatted(userVersion));
        IOHandler.printString("Vacuum settings                 -> %s (%d) isincremental (%d)".formatted(vacuumSetting.name(),vacuumSettingsRaw,isIncremental));
        IOHandler.printString("Application ID                  -> %d".formatted(applicationId));
        IOHandler.printString("Reserved zeros (20b) is zero    -> %b".formatted(reservedZeros));
        IOHandler.printString("Version valid for               -> (versionValidFor == changeCounter)%b %d".formatted(versionValidFor == changeCounter,versionValidFor));
        IOHandler.printString("Library write version           -> %d".formatted(libraryWriteVersion));
        IOHandler.printString("Total filesize should be");
        IOHandler.printString("%d(b)".formatted(getTotalFileSize()));
        IOHandler.printString("%f(kb)".formatted(getBytesToKilobytes(getTotalFileSize())));
        IOHandler.printString("%f(mb)".formatted(getBytesToMegabytes(getTotalFileSize())));
        IOHandler.printString("%f(gb)".formatted(getBytesToGigabytes(getTotalFileSize())));
    }

    public void showUserErrorMessage(){
        if(passedCheck.message != null){ IOHandler.printString(passedCheck.message);}
        if(errorCodes.size() > 0){IOHandler.printErrorCodes(errorCodes);}
    }

}

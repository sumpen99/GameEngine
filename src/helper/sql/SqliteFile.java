package helper.sql;
import helper.enums.*;
import helper.io.IOHandler;
import helper.struct.FreePageListInfo;
import helper.struct.MinMaxLeafLocal;
import helper.struct.PassedCheck;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import static helper.enums.ErrorCodes.*;
import static helper.enums.SqliteBits.*;
import static helper.enums.SqliteBits.MAX_MIN_LOCAL_LEAF;
import static helper.methods.CommonMethods.*;
import static helper.methods.IntToEnum.*;
import static java.lang.Math.floor;
//https://freemasen.com/blog/sqlite-parser-pt-1/
//https://freemasen.com/blog/sqlite-parser-pt-2/
//https://freemasen.com/blog/sqlite-parser-pt-3/
//https://github.com/FreeMasen/WiredForge.com/blob/main/content/blog/

//https://www.sqlite.org/fileformat2.html#btree

//https://github.com/siara-cc/sqlite3_page_explorer/blob/master/index.js
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
            userVersion;
    public int pageSize,reservedBytes,fractionMax,fractionMin,fractionLeaf;
    public MinMaxLeafLocal minMaxLeafLocal;
    public byte[] magicString;
    public byte[] pageBuffer;
    public FormatVersion formatVersionRead;
    public FormatVersion formatVersionWrite;
    public SchemaVersion schemaVersion;
    public VacuumSetting vacuumSetting;
    public TextEncoding textEncoding;
    DataInputStream reader = null;

    public SqliteFile(String pathToFile){
        path = pathToFile;
        errorCodes = new ArrayList<ErrorCodes>();
    }

    byte[] getPage(int offset,int pageNumber,int len){
        try{
            return getByteSubArray(pageBuffer,offset + ((pageNumber-1)*(pageSize)),len);
        }
        catch(Exception err){
            IOHandler.logToFile(err.getMessage());
            passedCheck.passed = false;
            passedCheck.message = err.getMessage();
        }
        return null;
    }

    public void parseFile(){
        passedCheck.passed = true;
        try{
            reader = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
            pageBuffer = reader.readAllBytes();
            readHeader();
        }
        catch(IOException err){
            passedCheck.passed = false;
            passedCheck.message = err.getMessage();
            return;
        }
        IOHandler.closeDataInputStream(reader);
    }

    void readHeader(){
        // READ SQLITE HEADER INFO
        try{
            convertToSize(MAGIC_STRING);
            convertToSize(PAGE_SIZE);
            convertToSize(FORMAT_VERSION_WRITE);
            convertToSize(FORMAT_VERSION_READ);
            convertToSize(RESERVED_BYTES);
            convertToSize(FRACTION_MAX);
            convertToSize(FRACTION_MIN);
            convertToSize(FRACTION_LEAF);
            convertToSize(CHANGE_COUNTER);
            convertToSize(DATABASE_SIZE);
            convertToSize(FIRST_FREE_PAGE);
            convertToSize(FIRST_FREE_PAGE_LEN);
            convertToSize(SCHEMA_COOKIE);
            convertToSize(SCHEMA_VERSION);
            convertToSize(CACHE_SIZE);
            convertToSize(VACUUM_SETTING);
            convertToSize(TEXT_ENCODING);
            convertToSize(USER_VERSION);
            convertToSize(INCREMENTAL_VACUUM);
            convertToSize(APPLICATION_ID);
            convertToSize(RESERVED_ZEROS);
            convertToSize(VERSION_VALID_FOR);
            convertToSize(LIBRARY_WRITE_VERSION);
            convertToSize(MAX_MIN_LOCAL_LEAF);

            // https://github.com/FreeMasen/WiredForge.com/tree/c0528ce3506630b6de0c103e7d09fcbf9b4bb348/content/blog
            int pagesTotal = 0;
            assert(validInt32(databaseSize)) : "Ooops";
            pagesTotal = (int)databaseSize;

            SqlPage[] pages = new SqlPage[pagesTotal];

            byte[] page = getPage(100,1,pageSize);
            //getPage(2);
            //getPage(3);
            /*while(read<getTotalFileSize()){
                read += reader.read(pageData,0,pageData.length + offset);
                offset = 0;
                //bTreePage = new BTreePageHeader();
                //bTreePage.readHeaderInfo(bufferPage);
                //if(bTreePage.errorCodes.size() != 0){bTreePage.showUserErrorMessage();}
                //else{bTreePage.printHeaderInfo();}
                //IOHandler.printString("############# Read  At %d ###########################".formatted(read));
                break;
            }*/
            pages[0] = new BTreePage(this);
            pages[0].readHeaderInfo(page);
            if(pages[0].errorCodes.size() != 0){pages[0].showUserErrorMessage();}
            else{pages[0].printHeaderInfo();}
        }
        catch(Exception err){
            //IOHandler.logToFile(err.getMessage());
            passedCheck.passed = false;
            passedCheck.message = err.getMessage();
        }
    }

    public void convertToSize(SqliteBits dst){
        switch(dst){
            case MAGIC_STRING:{
                magicString = getByteSubArray(pageBuffer,0,16);
                validateMagicString();
                break;
            }
            case PAGE_SIZE:{
                pageSize = castShortHexToInt(bytesToIntHex(pageBuffer,16,2,false));
                validatePageSize();
                break;
            }
            case FORMAT_VERSION_WRITE:{
                formatVersionWrite = intToFormatVersion(bytesToInt(pageBuffer,18,1,false));
                validateFormatVersionWrite();
                break;
            }
            case FORMAT_VERSION_READ:{
                formatVersionRead = intToFormatVersion(bytesToInt(pageBuffer,19,1,false));
                validateFormatVersionRead();
                break;
            }
            case RESERVED_BYTES:{
                reservedBytes = bytesToInt(pageBuffer,20,1,false);
                validateReservedBytes();
                break;
            }
            case FRACTION_MAX:{
                fractionMax = bytesToInt(pageBuffer,21,1,false);
                validateFraction(fractionMax,64,FRACTION_MAX_MISMATH);
                break;
            }
            case FRACTION_MIN:{
                fractionMin = bytesToInt(pageBuffer,22,1,false);
                validateFraction(fractionMin,32,FRACTION_MIN_MISMATH);
                break;
            }
            case FRACTION_LEAF:{
                fractionLeaf = bytesToInt(pageBuffer,23,1,false);
                validateFraction(fractionLeaf,32,FRACTION_LEAF_MISMATH);
                break;
            }
            case CHANGE_COUNTER:{
                changeCounter = castIntHexToLong(bytesToIntHex(pageBuffer,24,4,false));
                break;
            }
            case DATABASE_SIZE:{
                databaseSize = castIntHexToLong(bytesToIntHex(pageBuffer,28,4,false));
                break;
            }
            case FIRST_FREE_PAGE:{
                firstFreePage = castIntHexToLong(bytesToIntHex(pageBuffer,32,4,false));
                break;
            }
            case FIRST_FREE_PAGE_LEN:{
                firstFreePageLen = castIntHexToLong(bytesToIntHex(pageBuffer,36,4,false));
                validateFreePageInfo();
                break;
            }
            case SCHEMA_COOKIE:{
                schemaCookie = castIntHexToLong(bytesToIntHex(pageBuffer,40,4,false));
                break;
            }
            case SCHEMA_VERSION:{
                schemaVersion = intToSchemaVersion(bytesToInt(pageBuffer,44,4,false));
                validSchemaVersion();
                break;
            }
            case CACHE_SIZE:{
                cacheSize = castIntHexToLong(bytesToIntHex(pageBuffer,48,4,false));
                break;
            }
            case VACUUM_SETTING:{
                vacuumSettingsRaw = castIntHexToLong(bytesToIntHex(pageBuffer,52,4,false));
                break;
            }
            case TEXT_ENCODING:{
                textEncoding = intToTextEncoding(bytesToInt(pageBuffer,56,4,false));
                validTextEncoding();
                break;
            }
            case USER_VERSION:{
                userVersion = castIntHexToLong(bytesToIntHex(pageBuffer,60,4,false));
                break;
            }
            case INCREMENTAL_VACUUM:{
                isIncremental = castIntHexToLong(bytesToIntHex(pageBuffer,64,4,false));
                validVacuumSetting();
                break;
            }
            case APPLICATION_ID:{
                applicationId = castIntHexToLong(bytesToIntHex(pageBuffer,68,4,false));
                break;
            }
            case RESERVED_ZEROS:{
                reservedZeros = checkNonZeroValue(getByteSubArray(pageBuffer,72,20));
                validateZeroValue(reservedZeros);
                break;
            }
            case VERSION_VALID_FOR:{
                versionValidFor = castIntHexToLong(bytesToIntHex(pageBuffer,92,4,false));
                break;
            }
            case LIBRARY_WRITE_VERSION:{
                libraryWriteVersion = castIntHexToLong(bytesToIntHex(pageBuffer,96,4,false));
                break;
            }
            case MAX_MIN_LOCAL_LEAF:{
                int usableSize = pageSize-reservedBytes;
                minMaxLeafLocal = new MinMaxLeafLocal(usableSize);
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
        IOHandler.printString("Max Local                       -> %d".formatted(minMaxLeafLocal.maxLocal));
        IOHandler.printString("Min Local                       -> %d".formatted(minMaxLeafLocal.minLocal));
        IOHandler.printString("Max Leaf                        -> %d".formatted(minMaxLeafLocal.maxLeaf));
        IOHandler.printString("Min Leaf                        -> %d".formatted(minMaxLeafLocal.minLeaf));


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

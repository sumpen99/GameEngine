package helper.io;
import java.awt.image.*;
import java.io.*;
import engine.GameEngine;
import helper.enums.*;
import helper.font.ttf.*;
import helper.input.KeyboardHandler;
import helper.input.MouseHandler;
import helper.interfaces.ITTFTableInfo;
import helper.layout.Layout;
import helper.list.SMHashMap;
import helper.struct.*;
import helper.widget.Widget;

import static helper.enums.EntrieType.ENTRIE_TTF_GLYPHINDEX;
import static helper.enums.EntrieType.ENTRIE_TTF_TAG;
import static helper.enums.KeyboardState.*;
import static helper.enums.MouseState.*;
import static helper.methods.CommonMethods.*;
import static helper.methods.CommonMethods.littleEndianToBigEndian;
import static helper.methods.StringToEnum.*;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class IOHandler {

    public static  void printJsonObject(JsonObject jsonObj){
        if(jsonObj.listCount > 0){
            printString("Parsed JsonObject Contains %d Objects [{},{}...] Inside Json.objList".formatted(jsonObj.objList.objCount));
            printJsonList(jsonObj.objList);
        }
        else if(jsonObj.objCount > 0){
            printString("Parsed JsonObject Contains One Object {} Inside Json.objMap");
            printJsonObjMap(jsonObj.objMap);
        }
    }

    public static void printJsonList(JsonList jList){
        for(int i = 0;i < jList.objCount;i++){
            printString("################ OBJECT %d ################".formatted(i+1));
            printJsonObjMap(jList.objMaps[i]);
        }
    }

    public static void printJsonObjMap(SMHashMap objMap){
        for(int i = 0;i<objMap.capacity;i++){
            if(objMap.entries[i].set){
                printJsonEntrie(objMap.entries[i]);
            }
        }
    }

    public static void printJsonEntrie(Entrie e){
        if(e.eType == EntrieType.ENTRIE_JSON_STRING){
            System.out.println("%s %s".formatted(e.key,(String)e.value));
        }
        else if(e.eType == EntrieType.ENTRIE_JSON_LIST){
            JsonList objList = (JsonList)e.value;
            for(int i = 0;i < objList.objCount;i++){
                printJsonObjMap(objList.objMaps[i]);
            }
        }
    }

    public static void printAutoWord(AutoWord w,String strIn){
        System.out.println("[%s] needs-> [%d] edits to become -> [%s] (index: %d)".formatted(w.word,w.edits,strIn,w.index));
    }

    public static void printEdit(Edit edit,String strIn,String strOut){
        System.out.println("[%s] -> needs [%d] swaps to become -> [%s]".formatted(strIn,edit.n,strOut));
    }

    public static void printMultiPoints(Point[][] points){
        int i = 0,size = points.length;
        while(i<size)printPoints(points[i++]);
    }

    public static void printPoints(Point[] points){
        int i = 0,size = points.length;
        while(i<size)printPoint(points[i++]);
    }

    public static void printBaseLines(BaseLine[] lines){
        for(int i = 0;i<lines.length;i++){printBaseLine(lines[i]);}
    }

    public static void printBaseLine(BaseLine l){
        printString("X1: %f Y1: %f X2: %f Y2: %f".formatted(l.p1.x,l.p1.y,l.p2.x,l.p2.y));
    }

    public static void printPoint(Point p){
        if(p == null)return;
        printString("X: %f Y: %f Length: %f".formatted(p.x,p.y,p.length));
    }

    public static void printIntArray(int[] arr){
        printString("Int Array length: %d".formatted(arr.length));
        for(int i = 0;i<arr.length;i++)printInt(arr[i]);
    }

    public static void printByteArray(byte[] arr){
        for(int i = 0;i<arr.length;i++){
            if(i%64 == 0 && i != 0)printString("");
            System.out.print(arr[i] + " ");
        }
    }

    public static void printFloatArray(float[] arr){
        for(int i = 0;i<arr.length;i++){
            if(i%64 == 0 && i != 0)printString("");
            System.out.print(arr[i] + " ");
        }
        printString("\n");
    }

    public static void printShortArray(short[] arr){
        for(int i = 0;i<arr.length;i++){
            if(i%64 == 0 && i != 0)printString("");
            System.out.print(arr[i] + " ");
        }
    }

    public static void printCharBuf(CharBuf buf,int col){
        int i = 0,base = col,d;
        while(buf.buf[i] != '\0'){
            char c = buf.buf[i];
            d = c;
            System.out.print("%d".formatted(d));
            if((i%col) == 0 && i != 0){
                printString("");
                col+=base+1;
            }
            i++;
        }
    }

    public static void printString(String arg){
        System.out.println(arg);
    }

    public static void printStringMessage(String arg1,String arg2){
        System.out.println(arg1 + " " + arg2);
    }

    public static void printByte(byte b){
        printString("%d".formatted(b));
    }

    public static void printDouble(double d){
        printString("%f".formatted(d));
    }

    public static void printChar(char c){
        printString("%c".formatted(c));
    }

    public static void printFloat(float num){
        printString("%f".formatted(num));
    }

    public static void printLong(long num){
        printString("%d".formatted(num));
    }

    public static void printBool(boolean b){
        printString("%b".formatted(b));
    }

    public static void printInt(int i){
        printString("%d".formatted(i));
    }

    public static void printIntToHex(int value){
        printString("Hex Value of %d 0x%08X".formatted(value,value));
    }

    public static void printPosAndIntFourArray(int x,int y,int[] v){
        System.out.println("(Pos.x: %d Pos.y: %d) (X1: %d) (Y1: %d) (X2: %d) (Y2: %d)".formatted(x,y,v[0],v[1],v[2],v[3]));
    }

    public static void printEdge(Edge e){
        System.out.println(" E1.x: " + e.p1.x + " E1.y: " + e.p1.y + " E2.x: " + e.p2.x + " E2.y: " + e.p2.y);
    }

    public static void printSearchInfo(SearchInfo s){
        System.out.println("Found: %b FoundIndex: %d LeftMin: %d RightMax: %d FoundCount: %d SearchTime %f".formatted(s.found,s.foundIndex,s.leftMin,s.rightMax,s.foundCount,s.searchTime));
    }

    public static void printVec2d(Vec2d v){
        System.out.println(" X: " + v.x + " Y: " + v.y);
    }

    public static void printSpan(Span s){
        System.out.println(" X1: " + s.x1 + " X2: " + s.x2);
    }

    public static void removeFilesFromFolder(String folder){
        File dir = new File(folder);
        for(File file: dir.listFiles()){
            if(file == null)printString("File is null Error");
            else if(!file.delete())printString("Unable To Remove File %s".formatted(file.toString()));
        }
    }

    public static String getFileFromFolder(String folder,int index){
        File dir = new File(folder);
        File[] files = dir.listFiles();
        if(files != null && pointInRange(0,files.length-1,index))return files[index].getAbsolutePath();
        return null;
    }

    public static void removeFile(String fileName){
        File file = new File(fileName);
        if(!file.delete()){printString("Unable To Remove File %s".formatted(file.toString()));}

    }

    public static void logToFile(String msg){
        PrintWriter writer;
        String out = "%s -> %s".formatted(SMDateTime.getDateTime(),msg);
        GameEngine.funcToCheck.passed = true;
        try{
            writer = new PrintWriter(new BufferedWriter(new FileWriter("./resources/files/log/error/error.log", true)));
            writer.println(out);
            writer.close();

        }
        catch(IOException err){
            GameEngine.funcToCheck.message = err.getMessage();
            GameEngine.funcToCheck.passed = false;
        }
    }

    public static boolean testPathToLogFile(){
        logToFile("Start Of Program");
        return GameEngine.funcToCheck.passed;
    }

    public static boolean readFromGUIFile(String path, String[] data, GlobalCounter counter){
        BufferedReader reader;
        char SKIP_LINE = Token.SKIP_LINE.getChar();
        try{reader = new BufferedReader(new FileReader(path));}
        catch(java.io.FileNotFoundException err){
            GameEngine.funcToCheck.message = err.getMessage();
            return false;
        }

        String line;
        char ch;
        int cnt = 0;
        try{
            while((line = reader.readLine()) != null){
                line = line.trim();
                if(line.length() > 0){
                    ch = line.charAt(0);
                    if(ch != SKIP_LINE && cnt < data.length-1){
                        data[cnt++] = line;
                    }
                }
            }
            reader.close();

        }
        catch(java.io.IOException err){
            GameEngine.funcToCheck.message = err.getMessage();
            return false;
        }

        data[cnt] = Token.END_OF_GUI.getValue();
        counter.index = cnt;
        return true;
    }

    public static boolean readFromWordList(String path,AutoWords autoWords){
        BufferedReader reader;
        FileInputStream fRead;
        try{
            fRead = new FileInputStream(path);
            reader = new BufferedReader(new InputStreamReader(fRead));
        }
        catch(java.io.FileNotFoundException err){
            GameEngine.funcToCheck.message = err.getMessage();
            return false;
        }
        int cnt = 0;
        String line;
        try{
            while((reader.readLine() != null)){autoWords.count++;}
            fRead.getChannel().position(0);
            reader = new BufferedReader(new InputStreamReader(fRead));
            autoWords.initWordsList();
            while(((line = reader.readLine()) != null) && (cnt < autoWords.count)){
                line = line.trim();
                if(line.length() > 0){autoWords.words[cnt++] = line;}
            }
            reader.close();
        }
        catch(java.io.IOException err){
            GameEngine.funcToCheck.message = err.getMessage();
            return false;
        }
        return true;
    }

    public static String[] getTTFFiles(){
        String[] ttfs = new String[20];
        File dir = new File("./resources/files/fonts");
        int cnt = 0;
        for(File file: dir.listFiles()){
            ttfs[cnt++] = file.toString();
        }
        return cnt>0 ? ttfs : null;
    }

    public static void parseTTFFile(TTFFile header,boolean printHeaderInfo, PassedCheck result){
        DataInputStream reader = null;
        boolean passed = true;
        int read;
        try{
            reader = new DataInputStream(new BufferedInputStream(new FileInputStream(header.path)));
        }
        catch(FileNotFoundException err){
            result.passed = false;
            result.message = err.getMessage();
            return;
        }
        try{
            int i = 0,checkSum=0,offset=0,length=0,addedOffset=0,diff = 0;
            byte[] bufferFour = new byte[4];
            byte[] bufferTwo = new byte[2];

            // READ FILEINFO ... NUMTABLES
            read = reader.read(bufferFour,0,bufferFour.length);
            header.convertToSize(TTFBits.SCALAR_TYPE,bufferFour);
            read += reader.read(bufferTwo,0,bufferTwo.length);
            header.convertToSize(TTFBits.NUM_TABLES,bufferTwo);
            read += reader.read(bufferTwo,0,bufferTwo.length);
            header.convertToSize(TTFBits.SEARCH_RANGE,bufferTwo);
            read += reader.read(bufferTwo,0,bufferTwo.length);
            header.convertToSize(TTFBits.ENTRY_SELECTOR,bufferTwo);
            read += reader.read(bufferTwo,0,bufferTwo.length);
            header.convertToSize(TTFBits.RANGE_SHIFT,bufferTwo);

            // READ TABLES
            while(i++< header.numTables){
                read += reader.read(bufferFour,0,bufferFour.length);
                TTFTable tag = getTTFTableTag(byteBufToString(bufferFour));
                for(int j = 0;j<3;j++){
                    read += reader.read(bufferFour,0,bufferFour.length);
                    if(j == 0)checkSum = bytesToInt(bufferFour,0,4,false);
                    else if(j==1)offset = bytesToInt(bufferFour,0,4,false);
                    else length = bytesToInt(bufferFour,0,4,false);
                }
                assert tag != TTFTable.TTF_TABLE_TAG_UNKNOWN : "Unknown Table Tag!";
                header.table.addNewItem(tag.getValue(),new TTFTableTag(tag,checkSum,offset,length),ENTRIE_TTF_TAG);
            }

            byte[] bufferTables;
            i = 0;
            TTFTable[] tags = TTFTable.values();
            TTFTableBase table;
            TTFTable tag;
            // READ TABLE INFO
            //Tag: HEAD CheckSum: -121228132 offset: 252 length: 54
            //Tag: HHEA CheckSum: 138675504 offset: 308 length: 36
            //Tag: MAXP CheckSum: 17956997 offset: 344 length: 32
            //Tag: OS_2 CheckSum: 1469514715 offset: 376 length: 86
            //Tag: HMTX CheckSum: -1091553889 offset: 464 length: 788
            //Tag: CMAP CheckSum: -1571823404 offset: 1252 length: 724
            //Tag: CVT CheckSum: 2163321 offset: 1976 length: 4
            //Tag: LOCA CheckSum: 884688770 offset: 1980 length: 396
            //Tag: GLYF CheckSum: -1390321047 offset: 2376 length: 19724
            //Tag: NAME CheckSum: 1070178860 offset: 22100 length: 801
            //Tag: POST CheckSum: 688007320 offset: 22904 length: 428
            //Tag: GASP CheckSum: -65520 offset: 23332 length: 8
            //Tag: DSIG CheckSum: 1 offset: 23340 length: 8
            //Tag: GDEF CheckSum: 983070 offset: 23348 length: 30
            //Tag: FFTM CheckSum: 1593728467 offset: 23380 length: 28
            while(i<tags.length){
                tag = tags[i++];
                table = new TTFTableBase(tag);
                table.setSelf(header);
                if(table.info != null){
                    //reader.mark(read);
                    offset = table.getOffset(header);
                    length = table.getLength(header);
                    addedOffset = table.getAddedOffset();
                    bufferTables = new byte[length+addedOffset];
                    diff = offset-read;
                    read+=diff;
                    offsetBufferedReader(reader,diff);
                    //IOHandler.printString(tag.getValue());
                    //IOHandler.printString("%d %d %d".formatted(read,offset,length));
                    read += reader.read(bufferTables,0,bufferTables.length);
                    //IOHandler.printInt(read);
                    table.convertToSize(bufferTables);
                    table.setValue(header);
                    if(printHeaderInfo)printTTFTableInfo(table.info);
                    //table.info.checkForValuesBelowZero();
                    //IOHandler.printString("%d %d %d".formatted(read,offset,length));
                    read = offset+length;
                }
            }
        }
        catch(java.io.IOException err){
            IOHandler.logToFile(err.getMessage());
            passed = false;
            //result.passed = false;
            result.message = err.getMessage();
        }
        closeDataInputStream(reader);
        result.passed = passed;
    }

    public static void offsetBufferedReader(DataInputStream reader,int skipBytes){
        try{
            if(skipBytes > 0)reader.skip(skipBytes);
        }
        catch(Exception err){
            IOHandler.logToFile(err.getMessage());
        }

    }

    public static void parseWaveFile(WaveFile header,PassedCheck result){
        //http://truelogic.org/wordpress/2015/09/04/parsing-a-wav-file-in-c/
        DataInputStream reader = null;
        try{
            reader = new DataInputStream(new BufferedInputStream(new FileInputStream(header.path)));
        }
        catch(FileNotFoundException err){
            result.passed = false;
            result.message = err.getMessage();
            return;
        }
        try{
            byte[] bufferFour = new byte[4];
            byte[] bufferTwo = new byte[2];
            reader.read(header.riff,0,header.riff.length);
            reader.read(bufferFour,0,4);
            header.convertToSize(WaveBits.OVERALL_SIZE,bufferFour);
            reader.read(header.wave,0,header.wave.length);
            reader.read(header.fmtChunkMarker,0,header.fmtChunkMarker.length);
            reader.read(bufferFour,0,4);
            header.convertToSize(WaveBits.LENGTH_OF_FMT,bufferFour);
            reader.read(bufferTwo,0,2);
            header.convertToSize(WaveBits.FORMAT_TYPE,bufferTwo);
            header.formatTypeToName();
            reader.read(bufferTwo,0,2);
            header.convertToSize(WaveBits.CHANNELS,bufferTwo);
            reader.read(bufferFour,0,4);
            header.convertToSize(WaveBits.SAMPLE_RATE,bufferFour);
            reader.read(bufferFour,0,4);
            header.convertToSize(WaveBits.BYTE_RATE,bufferFour);
            reader.read(bufferTwo,0,2);
            header.convertToSize(WaveBits.BLOCK_ALIGN,bufferTwo);
            reader.read(bufferTwo,0,2);
            header.convertToSize(WaveBits.BITS_PER_SAMPLE,bufferTwo);
            reader.read(header.dataChunkHeader,0,header.dataChunkHeader.length);
            header.convertToSize(WaveBits.BITS_PER_SAMPLE,bufferTwo);
            header.setLimits();
            reader.read(bufferFour,0,4);
            header.convertToSize(WaveBits.DATA_SIZE,bufferFour);
            header.getSampleSize();
        }
        catch(java.io.IOException err){
            result.passed = false;
            result.message = err.getMessage();
        }
        closeDataInputStream(reader);
        result.passed = true;
    }

    public static void readWaveSampleData(WaveFile header,PassedCheck result){
        DataInputStream reader;
        boolean passed = true;
        try{
            reader = new DataInputStream(new BufferedInputStream(new FileInputStream(header.path)));
            long skip = reader.skip(header.BUF_OFFSET);
            if(skip != header.BUF_OFFSET){
                result.message = "Skipped More Bytes Then Allowed";
                result.passed = false;
                closeDataInputStream(reader);
                return;
            }
        }
        catch(IOException err){
            result.passed = false;
            result.message = err.getMessage();
            return;
        }
        int read;
        if(header.format == WaveFormatType.PCM){
            byte[] dataBuffer = new byte[(int)header.sizeOfEachSample];
            long bytesInEachChannel = (header.sizeOfEachSample / header.channels);
            if ((bytesInEachChannel  * header.channels) == header.sizeOfEachSample) {
                header.sampleDataPairs = new SamplePair[(int)header.numSamples/header.SAMPLE_CHUNK_SIZE];
                // numSamples = 214968
                // perSec = 44100
                // chunkSize 256
                // sampleDataPairs = 840
                int minValue = 0,maxValue = 0;
                outside : for (long i = 1; i <= header.numSamples; i++) {
                    try {
                        read = reader.read(dataBuffer, 0, dataBuffer.length);
                        if (read == header.sizeOfEachSample) {
                            int offset = 0; // move the offset for every iteration in the loop below
                            int dataInChannel = 0;
                            for (int xchannels = 0; xchannels < header.channels; xchannels++) {
                                if (bytesInEachChannel == 4) {
                                    dataInChannel = littleEndianToBigEndian(dataBuffer,offset,4);
                                }
                                else if (bytesInEachChannel == 2) {
                                    dataInChannel = littleEndianToBigEndian(dataBuffer,offset,2);
                                }
                                else if (bytesInEachChannel == 1) {
                                    dataInChannel = littleEndianToBigEndian(dataBuffer,offset,1);
                                    dataInChannel -= 128; //in wave, 8-bit are unsigned, so shifting to signed
                                }
                                offset += bytesInEachChannel;
                                if (dataInChannel < header.lowLimit || dataInChannel > header.highLimit){
                                    passed = false;
                                    result.message = "value out of range %d".formatted(dataInChannel);
                                    //assert false : "Encountered Value Ouside Limits";
                                    break outside;
                                }
                                else{
                                    minValue = Math.min(minValue,dataInChannel);
                                    maxValue = Math.max(maxValue,dataInChannel);
                                }
                            }
                        }
                        else {
                            passed = false;
                            result.message = "Error reading file. %d bytes".formatted(read);
                            break;
                        }

                        if(i % header.SAMPLE_CHUNK_SIZE == 0){
                            header.sampleDataPairs[(int)i/header.SAMPLE_CHUNK_SIZE-1] = new SamplePair(minValue,maxValue);
                            minValue=0;maxValue=0;
                        }
                    }
                    catch(java.io.IOException err){
                        passed = false;
                        result.message = err.getMessage();
                    }
                }
            }
        }
        result.passed = passed;
        closeDataInputStream(reader);
    }

    public static void closeDataInputStream(DataInputStream reader){
        try{
            reader.close();
        }
        catch(Exception err){
            logToFile(err.getMessage());
        }
    }

    public static void printGlyph(Glyf g,boolean printPoints){
        int i = 0;
        printString("instructionlength: %d numberOfContours: %d (xMin: %d yMin: %d) (xMax: %d yMax: %d)".formatted(g.instructionLength,g.numberOfContours,g.xMin,g.yMin,g.xMax,g.yMax));
        while(i<g.numberOfContours){
            printString("Contour %d ends at Index: %d".formatted(i+1,g.endPtsOfContours[i]));
            i++;
        }
        if(g.numberOfContours >= 0 && printPoints){
            int lastIndex = g.endPtsOfContours[g.numberOfContours-1];
            i=0;
            while(i<=lastIndex){
                printString("x: %d y: %d".formatted(g.xCoordinates[i],g.yCoordinates[i]));
                i++;
            }
        }
    }

    public static void printXAndYCoordinates(int[] xCoordinates,int[] yCoordinates){
        assert xCoordinates.length == yCoordinates.length;
        int i=0,size=xCoordinates.length;
        while(i<size){
            printString("x: %d y: %d".formatted(xCoordinates[i],yCoordinates[i]));
            i++;
        }
    }

    public static void printGlyphFlag(byte flag){
        boolean onCurve = (0x01 & flag) != 0;
        boolean xShort = (0x02 & flag) != 0;
        boolean yShort = (0x04 & flag) != 0;
        boolean repeat = (0x08 & flag) != 0;
        boolean xShortPos = (0x10 & flag) != 0;
        boolean yShortPos = (0x20 & flag) != 0;
        boolean reservedOne = (0x40 & flag) != 0;
        boolean reservedTwo = (0x80 & flag) != 0;
        printString("onCurve:%b xShort:%b yShort:%b repeat:%b xShortPos:%b yShortPos:%b reservedOne:%b reservedTwo:%b".formatted(onCurve,xShort,yShort,repeat,xShortPos,yShortPos,reservedOne,reservedTwo));
    }

    public static void printEncodingRecord(EncodingRecord e){
        printString("platformID %d encodingID %d offset %d isWindowsPlatform %b isUniCodePlatform: %b".formatted(e.platformID,e.encodingID,e.offSet,e.isWindowsPlatform,e.isUniCodePlatform));
    }

    public static void printFormatFour(FormatFour f4){
        printString("Format Four");
        printString("Length: %d\nLanguage: %d\nSegCountX2: %d\nSearchRange: %d\nEntrySelector: %d\nRangeShift: %d".formatted(f4.length,f4.language,f4.segCountX2,f4.searchRange,f4.entrySelector,f4.rangeShift));
        for(int i = 0;i<f4.segCount;i++){
            printString("SegCount: %d".formatted(i));
            printString("endCode: %d\nstartCode: %d\nidDelta: %d\nidRangeOffset: %d".formatted(f4.endCode[i],f4.startCode[i],f4.idDelta[i],f4.idRangeOffset[i]));
        }
        printString("Format Four HashMap GlyphIndex");
        printHashMap(f4.glyphIndexMap);
    }

    public static void printWaveFileInfo(String[] info){
        int size = info.length,i=0;
        while(i<size)printString(info[i++]);
    }

    public static void printTTFFileInfo(String[] info){
        int size = info.length,i=0;
        while(i<size)printString(info[i++]);
    }

    public static byte[] readBytesFromImage(String path,ImageInfo imgInfo){
        BufferedImage img;
        byte[] buf;
        try{
            String dir = "./resources/files/images/"+path;
            img = ImageIO.read(new File(dir));
            String[] subPath = path.split("\\.");
            imgInfo.name = subPath[0];
            imgInfo.imgType = subPath[1];
            imgInfo.width = img.getWidth();
            imgInfo.height = img.getHeight();
            imgInfo.setDataType(img.getRaster().getDataBuffer().getDataType());
            buf = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
            imgInfo.size = buf.length;
            imgInfo.chanels = imgInfo.size/ (imgInfo.width* imgInfo.height);
            //return ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
            return buf;
        }
        catch(java.io.IOException err){
            logToFile(err.getMessage());
        }
        return null;
    }

    public static void printSpline(Spline s,boolean onlySplinePoints){
        int size = s.splinePoints.length,i=0;
        printString("SplinePoints");
        while(i<size){
            printString("X: %f Y: %f".formatted(s.splinePoints[i].x,s.splinePoints[i].y));
            i++;
        }
        if(!onlySplinePoints){
            i=0;
            size = s.points.length;
            printString("BasePoints");
            while(i<size){
                printString("X: %f Y: %f".formatted(s.points[i].x,s.points[i].y));
                i++;
            }
        }
    }

    public static void printCharBuf(char[] buf,boolean asInt){
        int size = buf.length,i = 0;
        if(asInt)while(i<size)printInt(buf[i++]);
        else while(i<size)printChar(buf[i++]);
    }

    public static void printImageInfo(ImageInfo img){
        printString("Image name: %s".formatted(img.name));
        printString("Image type: %s".formatted(img.imgType));
        printString("Image width: %s".formatted(img.width));
        printString("Image height: %s".formatted(img.height));
        printString("Image size: %s".formatted(img.size));
        printString("Image chanels: %s".formatted(img.chanels));
        printString("Image Data type: %s".formatted(img.dataType));
    }

    public  static String readInputStream(HttpsURLConnection streamIn){
        String line = "",outPutData = "";
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(streamIn.getInputStream()));

            while((line = in.readLine())!= null){outPutData+=line;}
            in.close();

        }
        catch(Exception e){
            IOHandler.logToFile(e.getMessage());
        }
        return outPutData;
    }

    public static void writeOutputStream(HttpsURLConnection streamOut,byte[] postData){
        try{
            DataOutputStream wr = new DataOutputStream(streamOut.getOutputStream());
            wr.write(postData);
        }
        catch(Exception e){
            IOHandler.logToFile(e.getMessage());
        }
    }

    public static void writeWaveDataToFile(String name,AudioFileFormat.Type fileType,AudioInputStream audioInputStream){
        try{
            File f = verifyFileName("./resources/files/sound",name,fileType.getExtension());
            audioInputStream.reset();
            AudioSystem.write(audioInputStream,fileType,f);
        }
        catch(Exception e){
            IOHandler.logToFile(e.getMessage());
        }
    }

    public static void printThreadInfo(Thread t){
        if(t!= null)printString("Thread-Name %s\nThread-Priority %s\nThread-Group %s\nThread-ID %s\nThread-State %s\nThread-IsAlive %b\n".formatted(t.getName(),t.getPriority(),t.getThreadGroup(),t.getId(),t.getState(),t.isAlive()));
        else printString("Thread is Null");
    }

    public static String getEnv(String key){
        BufferedReader reader;
        FileInputStream fRead;
        try{
            fRead = new FileInputStream(".env");
            reader = new BufferedReader(new InputStreamReader(fRead));
        }
        catch(java.io.FileNotFoundException err){
            logToFile(err.getMessage());
            return null;
        }
        String line;
        try{
            while(((line=reader.readLine()) != null)){
                String[] keyValue = line.split(" ");
                if(keyValue[0].equals(key))return keyValue[1];
            }
            reader.close();
        }
        catch(java.io.IOException err){
            logToFile(err.getMessage());
        }
        return null;
    }

    public static void printColorInfo(int color){
        String name = getIntToColor(color);
        printString("%s 0x%08X".formatted(name,color));
    }

    public static boolean readHexColors(){
        BufferedReader reader;
        PrintWriter writer;
        /*try{reader = new BufferedReader(new FileReader("./resources/files/colors/hexColorsNew.txt"));}
        catch(java.io.FileNotFoundException err){
            return false;
        }

        String line;
        String[][] name_hex = new String[130][2];
        int cnt = 0;
        try{
            while((line = reader.readLine()) != null){
                line = line.trim();
                name_hex[cnt++] = line.split(" ");
            }
            reader.close();

        }
        catch(java.io.IOException err){
            return false;
        }

        try{
            writer = new PrintWriter(new BufferedWriter(new FileWriter("./resources/files/colors/hexColorsNew.txt", true)));
            for(int i = 0;i < name_hex.length;i++){
                String str = "%s %s 0x%s".formatted(name_hex[i][0].toUpperCase(),name_hex[i][0],name_hex[i][1].substring(1));
                writer.println(str);
            }
            writer.close();
            writer = new PrintWriter(new BufferedWriter(new FileWriter("./resources/files/colors/colorValue.txt", true)));
            for(int i = 0;i < name_hex.length;i++){
                String str = "%s(0x%s),".formatted(name_hex[i][0].toUpperCase(),name_hex[i][1].substring(1));
                writer.println(str);
            }
            writer.close();
            writer = new PrintWriter(new BufferedWriter(new FileWriter("./resources/files/colors/colorstring.txt", true)));
            for(int i = 0;i < name_hex.length;i++){
                String str = "if(type.equals(\"%s\"))return Color.%s;".formatted(name_hex[i][0],name_hex[i][0].toUpperCase());
                writer.println(str);
            }
            writer.close();

        }
        catch(IOException err){
            return false;
        }*/

        return true;
    }

    public static void printValueError(WidgetVariable err, int lnum){
        WidgetVariable[] ww = WidgetVariable.values();
        int size = ww.length,i=0;
        while(i<size){
            if(err == ww[i]){
                assert false : "\nNOT A CORRECT %s VALUE. ELEMENT AT Line ->:: %d (linenumber is not totally accurate)".formatted(err.toString(),lnum);
                break;
            }
            i++;
        }
    }

    public static void printDrawValues(DrawValues dww){
        printString("################# NEW DRAWVALUES ###########################");
        System.out.println("Dww.bind (String): " + dww.bind);
        System.out.println("Dww.path (String): " + dww.path);
        System.out.println("Dww.text (String): " + dww.text);
        System.out.println("Dww.id (String): " + dww.id);
        System.out.println("Dww.lnum (int): " + dww.lnum);
        System.out.println("Dww.col (int): " + dww.col);
        System.out.println("Dww.row (int): " + dww.row);
        System.out.println("Dww.left (int): " + dww.left);
        System.out.println("Dww.top (int): " + dww.top);
        System.out.println("Dww.width (int): " + dww.width);
        System.out.println("Dww.height (int): " + dww.height);
        System.out.println("Dww.radie (int): " + dww.radie);
        System.out.println("Dww.radiex (int): " + dww.radiex);
        System.out.println("Dww.radiey (int): " + dww.radiey);
        System.out.println("Dww.color (int): " + dww.color);
        System.out.println("Dww.opacity (int): " + dww.opacity);
        System.out.println("Dww.callback (boolean): " + dww.callback);
        System.out.println("Dww.update (boolean): " + dww.update);
        System.out.println("Dww.valign (boolean): " + dww.valign);
        System.out.println("Dww.halign (boolean): " + dww.halign);
        System.out.println("Dww.draw: (DrawMode)" + dww.draw.getValue());
        System.out.println("Dww.wShape (WidgetShape): " + dww.wShape.getValue());
        System.out.println("Dww.wType (WidgetType): " + dww.wType.getValue());
        if(dww.functionMethod != null){System.out.println("Dww.functionMethod (Callback): " + dww.functionMethod.getValue());}
        if(dww.points != null){System.out.println("Dww.points Count (int[]): " + dww.points.length);}
        if(dww.degrees != null){System.out.println("Dww.degrees (Mask): " + dww.degrees.bit);}
    }

    public static void printLayout(Layout l){
        printString("################# NEW LAYOUT ###########################");
        printString(l.getLayoutInfo());
    }

    public static void printWidget(Widget w){
        printString("################# NEW WIDGET ###########################");
        printString(w.getWidgetInfo());
    }

    public static void printMouseState(){
        if(MouseHandler.mouseBitSet(SM_MOUSE_LEFT_DOWN.getIndex()))printString("LeftMouseDown: Yes");
        if(MouseHandler.mouseBitSet(SM_MOUSE_LEFT_MOVE.getIndex()))printString("LeftMouseMove: Yes");

        if(MouseHandler.mouseBitSet(SM_MOUSE_RIGHT_DOWN.getIndex()))printString("RightMouseDown: Yes");
        if(MouseHandler.mouseBitSet(SM_MOUSE_RIGHT_MOVE.getIndex()))printString("RightMouseMove: Yes");

        if(MouseHandler.mouseBitSet(SM_MOUSE_LEFT_DOUBLE_CLICK.getIndex()))printString("MouseLeftDoubleClick: Yes");
        if(MouseHandler.mouseBitSet(SM_MOUSE_RIGHT_DOUBLE_CLICK.getIndex()))printString("MouseRightDoubleClick: Yes");

        if(MouseHandler.mouseBitSet(SM_MOUSE_TOUCH_UP.getIndex()))printString("MouseTouchUp: Yes");
        if(MouseHandler.mouseBitSet(SM_MOUSE_TOUCH_MOVE.getIndex()))printString("MouseTouchMove: Yes");
        if(MouseHandler.mouseBitSet(SM_MOUSE_RELEASE_TOUCH.getIndex()))printString("MouseReleaseTouch: Yes");

        if(MouseHandler.mouseBitSet(SM_MOUSE_SCROLL_UP.getIndex()))printString("MouseScrollUp: Yes");
        if(MouseHandler.mouseBitSet(SM_MOUSE_SCROLL_DOWN.getIndex()))printString("MouseScrollDown: Yes");
        if(MouseHandler.mouseBitSet(SM_MOUSE_WHEEL.getIndex()))printString("MouseWheel: Yes");

    }

    public static void printKeyboardState(){
        if(KeyboardHandler.keyboardBitSet(SM_KEY_DOWN.getIndex()))printString("KeyDown: Yes");
        if(KeyboardHandler.keyboardBitSet(SM_KEY_MULTI_TOUCH.getIndex()))printString("KeyMultiTouch: Yes");
        if(KeyboardHandler.keyboardBitSet(SM_KEY_UP.getIndex()))printString("KeyUp: Yes");
    }

    public static void refreshBitWiseOperations(){
        // uint32 0x91AF0214 =  2444165652
        // MaxValues int32 = -2147483648 +2147483647
        // MaxValues uint32 = 0 4294967295
        // 0x8000     0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
        // 0x80000000 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
        // 0x80000000 -2147483648
        int num1 = 0x91AF0214,num2 = 0x1392C790;
        int i1 = num1,i2 = num2;
        printIntBits(i1);
        printIntBits(i2);
        System.out.println("num1 -> 0x91AF0214 -> [(int32) %d] [(uint32) 2444165652]".formatted(num1));
        System.out.println("num2 -> 0x1392C790 -> %d".formatted(num2));
        printString("MaxValues: uInt32 -> (0 4294967295) Int32 -> (-2147483648 +2147483647)\n");

        i1|=i2;
        printIntBits(i1);
        printString("After Operation OR -> |= ([1-1 = 1] [1-0 = 1] [0-0 = 0])\n");

        i1 = num1;
        i2 = num2;
        i1&=i2;
        printIntBits(i1);
        printString("After Operation AND -> &= ([1-1 = 1] [1-0 = 0] [0-0 = 0])\n");

        i1 = num1;
        i2 = num2;
        i1^=i2;
        printIntBits(i1);
        printString("After Operation XOR -> ^= ([1-1 = 0] [1-0 = 1] [0-0 = 0])\n");

        i1 = 7;
        printIntBits(i1);
        i1 &= ~(1<<1);
        printIntBits(i1);
        printString("i1 & ~(1<<pos) [pos=1] Clear Bit\n");

        i1 = 3;
        i2 = 5;
        printIntBits(i1);
        printIntBits(i2);
        printString("i1 = 3\ni2 = 5");
        i1=i1^i2^(i2=i1);
        printIntBits(i1);
        printIntBits(i2);
        printString("i1 = 5\ni2 = 3");
        printString("i1 = i1^i2^(i2=i1) Swap Values\n");

        i1 = (1<<2);
        printIntBits(i1);
        printString("MaxBitValue (1<<2) -> %d".formatted(i1));
        i1 = (1<<3);
        printIntBits(i1);
        printString("MaxBitValue (1<<3) -> %d".formatted(i1));
        i1 = (1<<4);
        printIntBits(i1);
        printString("MaxBitValue (1<<4) -> %d".formatted(i1));


    }

    public static void printBitWiseAnd(int num1,int num2){
        printIntBits(num1);
        printIntBits(num2);
        num1&=num2;
        printIntBits(num1);
        printInt(num1);
    }

    public static void printBitWiseOr(int num1,int num2){
        printIntBits(num1);
        printIntBits(num2);
        num1|=num2;
        printIntBits(num1);
        printInt(num1);
    }

    public static void printIntBits(int value){
        for(int i = 31;i>=0;i--){
            int set = (value & (1 << i)) != 0 ? 1 : 0;
            System.out.print(set);
            System.out.print(" ");
        }
        System.out.println("");
    }

    public static void printHashMap(SMHashMap hashMap){
        Entrie e;
        for(int i = 0;i < hashMap.capacity;i++){
            if((e = hashMap.entries[i]).set){
                while(e != null){
                    printEntrie(e);
                    e = e.next;
                }
            }
        }
    }

    public static void printEntrie(Entrie e){
        if(e.eType == ENTRIE_TTF_TAG){
            printEntrieTTFTag((TTFTableTag)e.value);
        }
        else if(e.eType == ENTRIE_TTF_GLYPHINDEX){
            System.out.println("Key: %s Value: %d Bucket: %d isSet: %b".formatted(e.key,(int)e.value,e.bucket,e.set));
        }
        else System.out.println("Key: %s Value(Object): %s Bucket: %d isSet: %b".formatted(e.key,e.value.toString(),e.bucket,e.set));
    }

    public static void printEntrieTTFTag(TTFTableTag t){
        printString("Tag: %s CheckSum: %d offset: %d length: %d".formatted(t.tag,t.checkSum,t.offset,t.length));
    }

    public static void printTTFTableInfo(ITTFTableInfo tableInfo){
        tableInfo.dumpValues();
    }

    public static void printFontCharMap(FontChar[] map){
        int size = map.length,i = 0;
        while(i<size)printFontChar(map[i++]);
    }

    public static void printFontChar(FontChar c){
        if(c!=null)printString("%c xMin: %d yMIn: %d width: %d height: %d leftSideBearing: %d rightSideBearing %d".formatted(c.charValue,c.x,c.y,c.width,c.height,c.lsb,c.rsb));
    }

    public static void printSampleDataPairs(SamplePair[] samplePairs){
        int size = samplePairs.length,i = 0;
        while(i<size)printSamplePair(samplePairs[i++]);
    }

    public static void printSamplePair(SamplePair sp){
        printString("MinValue: %d MaxValue: %d".formatted(sp.minValue,sp.maxValue));
    }

    public static void printCharValues(){
        for(int i = 0;i< 96;i++){
            System.out.println("Char: %c Value: %d".formatted((char)(i+32),i));
        }
    }

    public static SSLSocketFactory loadSSLCert(){
        //https://gist.github.com/erickok/7692592
        try {

            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // From https://www.washington.edu/itconnect/security/ca/load-der.crt
            String pathSSL = getEnv("pathssl");
            InputStream is = new FileInputStream(pathSSL);
            InputStream caInput = new BufferedInputStream(is);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            return context.getSocketFactory();

        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException | IOException err ) {
            logToFile(err.getMessage());
        }
        return null;
    }

}

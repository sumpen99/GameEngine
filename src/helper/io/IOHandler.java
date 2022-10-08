package helper.io;
import java.awt.image.*;
import java.io.*;
import engine.GameEngine;
import helper.enums.*;
import helper.input.KeyboardHandler;
import helper.input.MouseHandler;
import helper.layout.Layout;
import helper.list.SMHashMap;
import helper.struct.*;
import helper.widget.Widget;
import static helper.enums.KeyboardState.*;
import static helper.enums.MouseState.*;
import static helper.methods.CommonMethods.*;
import static helper.methods.StringToEnum.getIntToColor;
import static helper.methods.CommonMethods.littleEndianToBigEndian;
import static helper.struct.SMDateTime.secondsToTime;
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
        printString("X: %f Y: %f Length: %f".formatted(p.x,p.y,p.length));
    }

    public static void printIntArray(int[] arr){
        printString("Int Array length: %d".formatted(arr.length));
        for(int i = 0;i<arr.length;i++)printInt(arr[i]);
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
        System.out.println(" E1.x: " + e.e1.x + " E1.y: " + e.e1.y + " E2.x: " + e.e2.x + " E2.y: " + e.e2.y);
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

    public static void parseWaveFile(WaveFile header,PassedCheck result){
        //http://truelogic.org/wordpress/2015/09/04/parsing-a-wav-file-in-c/
        DataInputStream reader = null;
        try{
            reader = new DataInputStream(new BufferedInputStream(new FileInputStream(header.path)));
        }
        catch(FileNotFoundException err){
            result.passed = false;
            result.message = err.getMessage();
        }
        try{
            byte[] bufferFour = new byte[4];
            byte[] bufferTwo = new byte[2];
            assert reader != null;
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

    public static void readWaveSampleData(WaveFile header,boolean printInfo,PassedCheck result){
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
                long low_limit = 0L;
                long high_limit = 0L;
                switch (header.bitsPerSample) {
                    case 8:
                        low_limit = -128;
                        high_limit = 127;
                        break;
                    case 16:
                        low_limit = -32768;
                        high_limit = 32767;
                        break;
                    case 32:
                        low_limit = -2147483648;
                        high_limit = 2147483647;
                        break;
                }
                if(printInfo)printString("Valid range for data values : %d to %d".formatted(low_limit, high_limit));
                for (long i = 1; i <= header.numSamples; i++) {
                    if(printInfo)printString("==========Sample %d / %d=============".formatted(i, header.numSamples));
                    try {
                        read = reader.read(dataBuffer, 0, dataBuffer.length);
                        if (read == header.sizeOfEachSample) {
                            // dump the data read
                            int offset = 0; // move the offset for every iteration in the loop below
                            int dataInChannel = 0;
                            for (int xchannels = 0; xchannels < header.channels; xchannels++) {
                                if(printInfo)printString("Channel#%d : ".formatted(xchannels + 1));
                                // convert data from little endian to big endian based on bytes in each channel sample
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
                                if(printInfo)printString("%d ".formatted(dataInChannel));
                                // check if value was in range
                                if (dataInChannel < low_limit || dataInChannel > high_limit){
                                    passed = false;
                                    result.message = "value out of range %d".formatted(dataInChannel);
                                }

                            }
                        }
                        else {
                            passed = false;
                            result.message = "Error reading file. %d bytes".formatted(read);
                            break;
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

    public static void printWaveFileInfo(WaveFile header){
        float duration_in_seconds = (float) header.overallSize / header.byteRate;
        printString("(1-4): %s".formatted(byteBufToString(header.riff)));
        printString("(5-8) Overall size: bytes:%d, Kb:%d".formatted(header.overallSize, header.overallSize/1024));
        printString("(9-12) Wave marker: %s".formatted(byteBufToString(header.wave)));
        printString("(13-16) Fmt marker: %s".formatted(byteBufToString(header.fmtChunkMarker)));
        printString("(17-20) Length of Fmt header: %d".formatted(header.lengthOfFmt));
        printString("(21-22) Format type: %d %s".formatted(header.formatType,header.format.getName()));
        printString("(23-24) Channels: %d".formatted(header.channels));
        printString("(25-28) Sample rate: %d".formatted(header.sampleRate));
        printString("(29-32) Byte Rate: %d , Bit Rate:%d".formatted(header.byteRate,header.byteRate*8));
        printString("(33-34) Block Alignment: %d".formatted(header.blockAlign));
        printString("(35-36) Bits per sample: %d".formatted(header.bitsPerSample));
        printString("(37-40) Data Marker: %s".formatted(byteBufToString(header.dataChunkHeader)));
        printString("(41-44) Size of data chunk: %d".formatted(header.dataSize));
        printString("Number of samples:%d".formatted(header.numSamples));
        printString("Size of each sample:%d bytes".formatted(header.sizeOfEachSample));
        printString("Approx.Duration in seconds=%f".formatted(duration_in_seconds));
        printString("Approx.Duration in h:m:s=%s".formatted(secondsToTime(duration_in_seconds)));
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

    public static void printValueError(WidgetVariable err, String lnum){
        switch(err){
            case LEFT:{printStringMessage("NOT A CORRECT LEFT VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case TOP:{printStringMessage("NOT A CORRECT TOP VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case COL:{printStringMessage("NOT A CORRECT COL VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case ROW:{printStringMessage("NOT A CORRECT ROW VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case OBJ_COUNT:{printStringMessage("NOT A CORRECT OBJ_COUNT VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case IDENTITY:{printStringMessage("NOT A CORRECT IDENTITY VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case BIND:{printStringMessage("NOT A CORRECT BIND VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case WIDTH:{printStringMessage("NOT A CORRECT WIDTH VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case HEIGHT:{printStringMessage("NOT A CORRECT HEIGHT VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case CALLBACK:{printStringMessage("NOT A CORRECT TP_CALLBACK. ELEMENT AT VALUE Line ->:: ",lnum);break;}
            case PATH:{printStringMessage("NOT A CORRECT PATH VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case UPDATE:{printStringMessage("NOT A CORRECT UPDATE VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case OPACITY:{printStringMessage("NOT A CORRECT OPACITY VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case FUNCTION:{printStringMessage("NOT A CORRECT FUNC VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case COLOR:{printStringMessage("NOT A CORRECT COLOR VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case BLEND:{printStringMessage("COLORS ADDED TO BLEND NOT ACCURATE. ELEMENT AT Line ->:: ",lnum);break;}
            case TEXT_COLOR:{printStringMessage("NOT A CORRECT TEXT_COLOR VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case ENABLE_AUTO_CORRECT:{printStringMessage("NOT A CORRECT ENABLE_AUTO_CORRECT. ELEMENT AT Line ->:: ",lnum);break;}
            case VALIGN:{printStringMessage("NOT A CORRECT VALIGN VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case HALIGN:{printStringMessage("NOT A CORRECT HALIGN VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case DRAW:{printStringMessage("NOT A CORRECT DRAW VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case POINTS:{printStringMessage("NOT A CORRECT TP_POINTS VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case TEXT:{printStringMessage("NOT A CORRECT TEXT VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case HINT_TEXT:{printStringMessage("NOT A CORRECT HINT_TEXT VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case ARGS:{printStringMessage("NOT A CORRECT ARGS VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case WSHAPE:{printStringMessage("NOT A CORRECT SHAPE VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case WTYPE:{printStringMessage("NOT A CORRECT TYPE VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case RADIE:{printStringMessage("NOT A CORRECT SHAPE VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case RADIEX:{printStringMessage("NOT A CORRECT SHAPE VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case RADIEY:{printStringMessage("NOT A CORRECT SHAPE VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case DEGREES:{printStringMessage("NOT A CORRECT DEGREES VALUE. ELEMENT AT Line ->:: ",lnum);break;}
            case SM_VARIABLE_NOT_IMPLEMENTED:{printStringMessage("NOT A CORRECT WIDGETVARIABLE VALUE Line ->:: ",lnum);break;}
            default:{printStringMessage("NOT A CORRECT VARIABLE Line ->:: ",lnum);break;}
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
            if((e = hashMap.entries[i]).set)printEntrie(e);
        }
    }

    public static void printEntrie(Entrie e){
        System.out.println("Key: %s Value(Object): %s Bucket: %d isSet: %b".formatted(e.key,e.value.toString(),e.bucket,e.set));
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

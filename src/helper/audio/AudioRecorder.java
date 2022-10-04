package helper.audio;
import helper.io.IOHandler;
import helper.threading.BaseThreading;
import helper.widget.Widget;
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

//https://www.baeldung.com/java-sound-api-capture-mic

public class AudioRecorder extends BaseThreading {
    double duration;
    boolean writeToFile;
    AudioFormat audioFormat;
    AudioInputStream audioInputStream;
    ByteArrayOutputStream outStream;
    TargetDataLine targetDataLine;
    int frameSizeInBytes,bufferLengthInFrames,bufferLengthInBytes;
    public AudioRecorder(Widget wself,AudioFormat format,boolean writetofile){
        super(wself);
        writeToFile = writetofile;
        audioFormat = format;
        outStream = new ByteArrayOutputStream();
        targetDataLine = getTargetDataLine();
        if(targetDataLine != null){
            frameSizeInBytes = audioFormat.getFrameSize();
            bufferLengthInFrames = targetDataLine.getBufferSize() / 8;
            bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        }
    }

    @Override
    public void heavyDuty(){
        if(outStream != null && targetDataLine != null){
            try{
                buildByteOutputStream(outStream, targetDataLine, frameSizeInBytes, bufferLengthInBytes);
                setAudioInputStream();
                audioInputStream.reset();
                if(writeToFile)AudioHandler.writeSampleToFile();
            }
            catch (Exception err) {
                IOHandler.logToFile(err.getMessage());
            }
        }
    }

    void setAudioInputStream() {
        this.audioInputStream = convertToAudioIStream(outStream, frameSizeInBytes);
    }

    void buildByteOutputStream(final ByteArrayOutputStream outStream, final TargetDataLine targetDataLine, int frameSizeInBytes, final int bufferLengthInBytes){
        final byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead;
        targetDataLine.start();
        while(((numBytesRead = targetDataLine.read(data, 0, bufferLengthInBytes)) != -1) && AudioHandler.isRecording()) {
            //IOHandler.printInt(numBytesRead);
            outStream.write(data, 0, numBytesRead);
        }
    }

    AudioInputStream convertToAudioIStream(final ByteArrayOutputStream outStream, int frameSizeInBytes) {
        byte[] audioBytes = outStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(audioBytes);
        //AudioInputStream audioStream = new AudioInputStream(inputStream, audioFormat, audioBytes.length / frameSizeInBytes);
        //long milliseconds = (long) ((new AudioInputStream(targetDataLine).getFrameLength() * 1000) / audioFormat.getFrameRate());
        //duration = milliseconds / 1000.0;
        //System.out.println("Recorded duration in seconds:" + milliseconds);
        return new AudioInputStream(inputStream, audioFormat, audioBytes.length / frameSizeInBytes);
    }

    public AudioInputStream getInputStream(){
        return audioInputStream;
    }

    TargetDataLine getTargetDataLine(){
        TargetDataLine line = null;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,audioFormat);
        if(!AudioSystem.isLineSupported(info)){
            IOHandler.logToFile("AudioSystem.isLineSupported(info) Did Not Check Out OK!");
            return null;
        }
        try{
            line = (TargetDataLine)AudioSystem.getLine(info);
            line.open(audioFormat,line.getBufferSize());

        }
        catch(Exception err){
            IOHandler.logToFile(err.getMessage());
            return null;
        }
        return line;
    }

}

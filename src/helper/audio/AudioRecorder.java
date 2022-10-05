package helper.audio;
import helper.io.IOHandler;
import helper.threading.BaseThreading;
import helper.widget.Widget;
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

//https://www.baeldung.com/java-sound-api-capture-mic

//https://stackoverflow.com/questions/6235016/convert-wav-audio-format-byte-array-to-floating-point

public class AudioRecorder extends BaseThreading {
    double duration;
    AudioFormat audioFormat;
    AudioInputStream audioInputStream;
    ByteArrayOutputStream outStream;
    TargetDataLine targetDataLine;
    int frameSizeInBytes,bufferLengthInFrames,bufferLengthInBytes;
    public AudioRecorder(Widget wself,AudioFormat format){
        super(wself);
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
                setDuration();
                audioInputStream.reset();
                wSelf.reachOutsideWorld();
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
            outStream.write(data, 0, numBytesRead);
        }
        targetDataLine.close();
    }

    AudioInputStream convertToAudioIStream(final ByteArrayOutputStream outStream, int frameSizeInBytes) {
        byte[] audioBytes = outStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(audioBytes);
        return new AudioInputStream(inputStream, audioFormat, audioBytes.length / frameSizeInBytes);
    }

    void setDuration(){
        long milliseconds = (long) ((getInputStream().getFrameLength() * 1000) / audioFormat.getFrameRate());
        duration = milliseconds / 1000.0;
    }

    public double getDuration(){
        return duration;
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
            IOHandler.printString(info.toString());
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

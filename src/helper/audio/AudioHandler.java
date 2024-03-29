package helper.audio;
import helper.io.IOHandler;
import helper.widget.Widget;

import javax.sound.sampled.*;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
/*public final AudioFormat.Encoding ENCODING = AudioFormat.Encoding.PCM_SIGNED;
public final float RATE = 44100.0f;
public final int CHANNELS = 1;
public final int SAMPLE_SIZE = 16;
public final boolean BIG_ENDIAN = true;*/
//https://www.baeldung.com/java-sound-api-capture-mic
public class AudioHandler {
    /*
      PCM_SIGNED
      PCM_UNSIGNED
      PCM_FLOAT
      ULAW
      ALAW
     */
    AudioFormat audioFormat;
    AudioRecorder audioRecorder;
    AudioReader audioReader;
    static AudioHandler self = null;
    static boolean isSet = false;
    boolean recording,writeToFile;
    final AudioFormat.Encoding ENCODING = PCM_SIGNED;
    final float SAMPLE_RATE = 44100.0F,FRAME_RATE = 44100.0F;
    final int CHANNELS = 1,SAMPLE_SIZE = 16,FRAME_SIZE = 2;
    final boolean BIG_ENDIAN = true;
    //FRAME_SIZE = (SAMPLE_SIZE / 8) * channels

    public AudioHandler(){
        assert !AudioHandler.isSet :"AudioHandler is already set!";
        AudioHandler.setInstance();
    }

    private static void setInstance(){
        AudioHandler.isSet = true;
    }

    public static void initAudioHandler(){
        if(self == null){
            self = new AudioHandler();
            self.setAudioFormat();
            self.writeToFile = true;
        }
    }

    public static boolean setAudioReader(Widget wSelf,String fileName){
        self.audioReader = new AudioReader(wSelf);
        return self.audioReader.setWaveFile(fileName);
    }

    public static void setAudioRecorder(Widget wSelf){
        self.audioRecorder = new AudioRecorder(wSelf,self.getAudioFormat());
    }

    public static void closeAudioRecorder(){
        self.audioRecorder = null;
    }

    public static void closeAudioReader(){
        self.audioReader = null;
    }

    public static AudioRecorder getAudioRecorder(){
        return self.audioRecorder;
    }

    public static AudioReader getAudioReader(){
        return self.audioReader;
    }

    public static void writeSampleToFile(){
        if(self.audioRecorder != null && self.writeToFile){
            IOHandler.writeWaveDataToFile("soundClip",AudioFileFormat.Type.WAVE,self.audioRecorder.getInputStream());
        }
    }

    public static String getAudioRecorderInfo(){
        return "%f".formatted(self.audioRecorder.getDuration());
    }

    void setAudioFormat(){
        audioFormat = new AudioFormat(ENCODING,SAMPLE_RATE,SAMPLE_SIZE,CHANNELS,FRAME_SIZE,FRAME_RATE,BIG_ENDIAN);
    }

    AudioFormat getAudioFormat(){
        return self.audioFormat;
    }

    public static void setRecording(boolean value){
        self.recording = value;
    }

    public static boolean isRecording(){
        return self.recording;
    }

  }

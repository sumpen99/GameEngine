package helper.audio;

import helper.struct.PassedCheck;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

//https://www.baeldung.com/java-sound-api-capture-mic
public class AudioHandler {
    /*
      PCM_SIGNED = new Encoding("PCM_SIGNED");
      PCM_UNSIGNED = new AudioFormat.Encoding("PCM_UNSIGNED");
      PCM_FLOAT = new AudioFormat.Encoding("PCM_FLOAT");
      ULAW = new AudioFormat.Encoding("ULAW");
      ALAW
     */
    private static AudioHandler self = null;
    AudioFormat audioFormat;
    private static boolean isSet = false;
    final AudioFormat.Encoding ENCODING = PCM_SIGNED;
    final float SAMPLE_RATE = 44100.0F,FRAME_RATE = 44100.0F;
    final int CHANNELS = 2,SAMPLE_SIZE = 16,FRAME_SIZE = 4;
    final boolean BIG_ENDIAN = true;

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
        }
    }

    void setAudioFormat(){
        new AudioFormat(PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, true);
        audioFormat = new AudioFormat(ENCODING,SAMPLE_RATE,SAMPLE_SIZE,CHANNELS,FRAME_SIZE,FRAME_RATE,BIG_ENDIAN);
    }

    TargetDataLine getTargetDataLine(PassedCheck result){
        TargetDataLine line = null;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,audioFormat);
        if(!AudioSystem.isLineSupported(info))return null;
        try{
            line = (TargetDataLine)AudioSystem.getLine(info);
            line.open(audioFormat,line.getBufferSize());
            result.passed = true;

        }
        catch(Exception err){
            result.passed = false;
            result.message = err.getMessage();
        }
        return line;

    }
}

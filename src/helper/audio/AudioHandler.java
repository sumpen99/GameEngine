package helper.audio;
//https://www.baeldung.com/java-sound-api-capture-mic
public class AudioHandler {
    //private BufferedImage imgBuffer;
    //private byte[] frameBuffer;
    //private int screenWidth,screenHeight;
    private static AudioHandler self = null;
    private static boolean isSet = false;

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
}

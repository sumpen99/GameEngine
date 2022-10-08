package helper.audio;
import helper.struct.WaveFile;
import helper.threading.BaseThreading;
import helper.widget.Widget;


public class AudioReader extends BaseThreading {
    WaveFile f;
    public AudioReader(Widget wself,String fileName){
        super(wself);
        f = new WaveFile(verifyPath(fileName));
    }

    String verifyPath(String fileName){
        return "./resources/files/sound/%s".formatted(fileName);
    }

    @Override
    public void heavyDuty(){
        if(f.readFile())wSelf.setBindingValue(f.getFileInfo());
    }
}

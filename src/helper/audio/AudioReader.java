package helper.audio;
import helper.io.IOHandler;
import helper.struct.WaveFile;
import helper.threading.BaseThreading;
import helper.widget.Widget;

import static helper.methods.CommonMethods.fileExists;


public class AudioReader extends BaseThreading {
    WaveFile f;
    public AudioReader(Widget wself){
        super(wself);
    }

    public boolean setWaveFile(String path){
        String fileName = verifyPath(path);
        if(fileName != null){
            f = new WaveFile(fileName);
            return true;
        }
        return false;
    }

    String verifyPath(String fileName){
        fileName = fileName.length() == 0 ? "-1" : fileName;
        String dir = "./resources/files/sound/%s".formatted(fileName);
        if(fileExists(dir))return dir;
        return IOHandler.getFileFromFolder("./resources/files/sound",0);
    }

    @Override
    public void heavyDuty(){
        if(f.readFile()){
            wSelf.setBindingValue(f);
            /*wSelf.setBindingValue(f.getFileInfo());
            wSelf.setBindingValue(f.getSampleChunkSize());
            wSelf.setBindingValue(f.getLimitsLowHigh());
            wSelf.setBindingValue(f.getSamplePairs());*/
        }
    }
}

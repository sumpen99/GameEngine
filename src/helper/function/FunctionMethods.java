package helper.function;
import engine.GameEngine;
import helper.api.ApiHandler;
import helper.audio.AudioHandler;
import helper.io.IOHandler;
import helper.struct.StopWatch;
import helper.threading.ThreadHandler;
import helper.widget.*;
import static helper.enums.ApiFunction.URL_GET_HIGHSCORE;
import static helper.enums.ApiFunction.URL_UPLOAD_HIGHSCORE;
import static helper.methods.CommonMethods.intColorToHex;

public class FunctionMethods {

    public void getColorInfo(Integer arg,String[] args,Object self){
        Widget wSelf = (Widget)self;
        if(wSelf != null){
            GameEngine.setInfoTextStatus(true);
            GameEngine.setInfoText(intColorToHex(wSelf.getWidgetColorInfo()));
        }
    }


    public void recordAudio(Integer arg,String[] args,Object self){
        Widget wSelf = (Widget)self;
        if(wSelf != null){
            AudioHandler.setAudioRecorder(wSelf);
            ThreadHandler.executeNewThread(AudioHandler.getAudioRecorder());
        }
    }

    public void readWaveFile(Integer arg,String[] args,Object self){
        Widget wSelf = (Widget)self;
        Widget wWaveView = (WaveViewBox)GameEngine.getWidgetById(args[0]);
        Widget wTxtBox = (FlatTextBox)GameEngine.getWidgetById(args[1]);
        if(wSelf != null && wWaveView != null){
            String fileName = (String)wTxtBox.getBindingValue();
            if(AudioHandler.setAudioReader(wWaveView,fileName)){
                ThreadHandler.executeNewThread(AudioHandler.getAudioReader());
            }
        }
    }

    public void playWaveFile(Integer arg,String[] args,Object self){
        Widget wSelf = (Widget)self;
        WaveViewBox wWaveView = (WaveViewBox)GameEngine.getWidgetById(args[0]);
        if(wSelf != null && wWaveView != null){
            wWaveView.pressPlayButton();
            if(wWaveView.isAudioPlaying()){
                wSelf.setBindingValue("Stop");
                ThreadHandler.executeNewThread(wWaveView);
            }
            else{
                wSelf.setBindingValue("Play");
            }
        }
    }

    public void getHighScore(Integer arg,String[] args,Object self){
        Widget wSelf = (Widget)self;
        Widget wLabelBox = (LabelBox)GameEngine.getWidgetById(args[0]);
        if(wLabelBox != null){
            String[] rowValues = (String[])wLabelBox.getBindingValue();
            ThreadHandler.executeNewThread(new ApiHandler(wSelf,URL_GET_HIGHSCORE,rowValues));
        }
    }

    public void uploadHighScore(Integer arg,String[] args,Object self){
        Widget wSelf = (Widget)self;
        Widget wTextBox = (TextBox)GameEngine.getWidgetById(args[0]);
        Widget wScoreCounter = (RoundedLabel)GameEngine.getWidgetById(args[1]);
        if(wTextBox != null && wScoreCounter != null){
            String userName = (String)wTextBox.getBindingValue();
            String userScore = (String)wScoreCounter.getBindingValue();
            if(((userName = userName.trim()).length() != 0) && (userScore=userScore.trim()).length() != 0){
                ThreadHandler.executeNewThread(new ApiHandler(wSelf,URL_UPLOAD_HIGHSCORE,new String[]{userName,userScore}));
            }
        }
    }

    public void startClock(Integer arg,String[] args,Object self){
        Widget wSelf = (Widget)self;
        Widget wGametextbox = (SpellMeTextBox)GameEngine.getWidgetById(args[0]);
        Widget wSpinner = (Image)GameEngine.getWidgetById(args[1]);
        if(wSelf != null && wGametextbox != null){
            wSpinner.setBindingValue(true);
            wGametextbox.setBindingValue(true);
            ThreadHandler.executeNewThread(new StopWatch(wSelf,new Widget[]{wGametextbox,wSpinner},30,0,1000,true));
        }
    }

    public void shutDown(Integer arg,String[] args,Object self){GameEngine.exitEngineLoop();}
}

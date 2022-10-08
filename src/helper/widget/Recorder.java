package helper.widget;
import helper.audio.AudioHandler;
import helper.struct.DrawValues;

import static helper.enums.WidgetState.SM_TOUCH_PROCESSED;

public class Recorder extends FlatImage{
    public Recorder(Object obj, DrawValues dww){
        super(obj,dww);
    }

    @Override
    public boolean onMouseLeftDown(int x,int y){
        if(touchEventNotProcessed()){
            setWidgetBit(SM_TOUCH_PROCESSED.getValue());
            //shiftBitsLeft();
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseReleaseTouch(int x,int y){
        if(getWidgetBitSet(SM_TOUCH_PROCESSED.getIndex())){
            clearWidgetBits();
            if(AudioHandler.isRecording()){
                AudioHandler.setRecording(false);
                setRecLabelText("Press To Record");
            }
            else{
                setRecLabelText("Press To Stop");
                //setLabelBoxText("Recording...");
                AudioHandler.setRecording(true);
                execFuncMethod();
            }
            return true;
        }
        return false;
    }

    void setRecLabelText(String txt){
        Widget recLbl = (Widget)getParameterValue(1);
        recLbl.setBindingValue(txt);
    }

    void setLabelBoxText(String txt){
        Widget lblBox = (Widget)getParameterValue(0);
        lblBox.setBindingValue(txt);
    }

    @Override
    public void reachOutsideWorld(){
        AudioHandler.writeSampleToFile();
        setLabelBoxText("But For Now We Only Get Duration: %s sec".formatted(AudioHandler.getAudioRecorderInfo()));
        AudioHandler.closeAudioRecorder();
    }

    @Override
    public boolean onMouseScrollUp(int x,int y){return false;}

    @Override
    public boolean onMouseScrollDown(int x,int y){return false;}


}

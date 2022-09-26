package helper.struct;
import helper.io.IOHandler;
import helper.threading.BaseThreading;
import helper.widget.Widget;

public class StopWatch extends BaseThreading {
    int startValue,stopValue,freQuens;
    boolean countDown;
    Widget[] wTargets;
    public StopWatch(Widget wself,Widget[] wtargets,int startvalue,int stopvalue,int frequens,boolean countdown){
        super(wself);
        startValue = startvalue;
        stopValue = stopvalue;
        countDown = countdown;
        freQuens = frequens;
        wTargets = wtargets;
    }

    public void heavyDuty(){
        if(countDown){
            String defaultValue = (String)wSelf.getBindingValue();
            startDownCount();
            wSelf.setBindingValue(defaultValue);
            resetWidgetTargets();

        }
    }

    void resetWidgetTargets(){
        if(wTargets != null){
            int size = wTargets.length;
            for(int i = 0;i < size;i++){
                wTargets[i].resetWidgetState();
            }
        }
    }

    private void startDownCount() {
        if(startValue <= stopValue)return;
        while(startValue >= stopValue){
            wSelf.setBindingValue("%d".formatted(startValue--));
            try{
                Thread.sleep(freQuens);
            }
            catch(InterruptedException e){
                IOHandler.logToFile(e.getMessage());
            }
        }
    }
}

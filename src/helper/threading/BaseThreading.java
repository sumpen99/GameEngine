package helper.threading;
import helper.interfaces.IThreading;
import helper.widget.Widget;

public abstract class BaseThreading implements IThreading {
    protected Widget wSelf;

    public BaseThreading(Widget w){
        wSelf = w;
        setCallbackInProgress(true);
    }

    public void setCallbackInProgress(boolean value){
        if(wSelf != null){wSelf.setCallbackInProgress(value);}
    }
}

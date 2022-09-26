package helper.threading;
import helper.interfaces.IThreading;

public class ThreadHandler{
    public static void executeNewThread(IThreading obj){
        TBF tbf = new TBF(obj);
        Thread t1 = new Thread(tbf);
        t1.start();
    }
}

class TBF implements Runnable{
    IThreading object;
    public TBF(IThreading obj){
        object = obj;
    }

    public void run(){
        object.heavyDuty();
        object.setCallbackInProgress(false);
    }
}

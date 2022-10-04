package helper.threading;
import helper.interfaces.IThreading;
import helper.io.IOHandler;

public class ThreadHandler{
    static Thread t;
    public static void executeNewThread(IThreading obj){
        TBF tbf = new TBF(obj);
        t = new Thread(tbf);
        t.start();
    }

    public static void getThreadInfo(){
        IOHandler.printThreadInfo(t);
    }

    public static void closeThread(){
        t = null;
    }

    public static Thread getCurrentThread(){
        return t;
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
        ThreadHandler.closeThread();
    }
}

package program;
import engine.GameEngine;

public class RecordSound extends GameEngine {
    public RecordSound(int width,int height){
        super(width,height);
    }

    @Override
    public boolean setUpProgram(){
        return this.parseGuiFile("./resources/files/gui/recorder.fs");
    }

    @Override
    public void onUserUpdate(float fElapsedTime){
        //IOHandler.printKeyboardState();
        //IOHandler.printMouseState();
        //IOHandler.printFloat(fElapsedTime);

    }
}

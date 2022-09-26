package program;

import engine.GameEngine;

public class ColorPicker extends GameEngine {
    public ColorPicker(int width,int height){
        super(width,height);
    }

    @Override
    public boolean setUpProgram(){
        return this.parseGuiFile("./resources/files/gui/colorpicker.fs");
    }

    @Override
    public void onUserUpdate(float fElapsedTime){
        //IOHandler.printKeyboardState();
        //IOHandler.printMouseState();
        //IOHandler.printFloat(fElapsedTime);

    }
}

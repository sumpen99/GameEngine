package program;
import engine.GameEngine;


public class QuadTreeBalls extends GameEngine {
    public QuadTreeBalls(int width,int height){
        super(width,height);
    }

    @Override
    public boolean setUpProgram(){
        return this.parseGuiFile("./resources/files/gui/quadtree.fs");
    }

    @Override
    public void onUserUpdate(float fElapsedTime){
        //IOHandler.printKeyboardState();
        //IOHandler.printMouseState();
        //IOHandler.printFloat(fElapsedTime);

    }
}

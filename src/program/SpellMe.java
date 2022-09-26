package program;
import engine.GameEngine;
import helper.function.FunctionMethods;

public class SpellMe extends GameEngine{
    public SpellMe(int width,int height){
        super(width,height);
    }

    @Override
    public boolean setUpProgram(){
        if(this.parseGuiFile("./resources/files/gui/spellme.fs")){
            new FunctionMethods().getHighScore(1,new String[]{"highscorelist"},null);
            return true;
        }
        return false;
    }

    @Override
    public void onUserUpdate(float fElapsedTime){
        //IOHandler.printKeyboardState();
        //IOHandler.printMouseState();
        //IOHandler.printFloat(fElapsedTime);

    }
}

import engine.GameEngine;
import program.ColorPicker;
import program.QuadTreeBalls;
import program.SpellMe;
import helper.io.IOHandler;

//https://personal.ntu.edu.sg/ehchua/programming/java/javanativeinterface.html 

public class StartUp {

    public static void main(String[] args){
        GameEngine program;
        //IOHandler.refreshBitWiseOperations();
        //IOHandler.printBitWiseAnd(40,0x7f);
        IOHandler.removeFilesFromFolder("./resources/files/log/gc");
        IOHandler.removeFile("./resources/files/log/error/error.log");

        program = new SpellMe(800,500);
        //program = new ColorPicker(800,500);
        //program = new QuadTreeBalls(800,500);
        if(program.setUpProgram()){
            program.runEngineLoop();
        }
        else{
            IOHandler.logToFile(program.getErrorMessage());
        }
    }

}

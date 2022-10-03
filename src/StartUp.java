import engine.GameEngine;
import helper.struct.UInt;
import program.ColorPicker;
import program.QuadTreeBalls;
import program.SpellMe;
import helper.io.IOHandler;

//https://personal.ntu.edu.sg/ehchua/programming/java/javanativeinterface.html
//http://midi.teragonaudio.com/tech/lowaud.htm

public class StartUp {

    public static void main(String[] args){
        GameEngine program;
        //IOHandler.refreshBitWiseOperations();
        //IOHandler.printBitWiseAnd(40,0x7f);
        IOHandler.removeFilesFromFolder("./resources/files/log/gc");
        IOHandler.removeFile("./resources/files/log/error/error.log");


        //UInt v = new UInt();
        //v.set32(0xffffffffL);
        //IOHandler.printLong(v.uI32);
        //IOHandler.printIntBits((int)v.uI32);

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

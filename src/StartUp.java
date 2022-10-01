import engine.GameEngine;
import helper.search.BinarySearch;
import helper.struct.Point;
import helper.struct.SearchInfo;
import program.ColorPicker;
import program.QuadTreeBalls;
import program.SpellMe;
import helper.io.IOHandler;

import static helper.methods.CommonMethods.buildPolygonShape;
import static helper.methods.CommonMethods.intArrToPointArr;


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

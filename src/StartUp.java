import engine.GameEngine;
import helper.struct.PassedCheck;
import helper.font.ttf.TTFFile;
import helper.struct.SMDateTime;
import program.RecordSound;
import helper.io.IOHandler;
//https://personal.ntu.edu.sg/ehchua/programming/java/javanativeinterface.html
//http://midi.teragonaudio.com/tech/lowaud.htm
//https://tchayen.github.io/posts/ttf-file-parsing



public class StartUp {

    public static void main(String[] args){
        GameEngine program;
        //IOHandler.refreshBitWiseOperations();
        //IOHandler.printBitWiseAnd(40,0x7f);
        IOHandler.removeFilesFromFolder("./resources/files/log/gc");
        //IOHandler.removeFilesFromFolder("./resources/files/sound");
        //IOHandler.removeFile("./resources/files/log/error/error.log");
        PassedCheck psc = new PassedCheck();
        TTFFile ttf = new TTFFile("./resources/files/fonts/Quicksand-Bold.ttf");
        IOHandler.parseTTFFile(ttf,psc);
        ttf.setFileInfo();
        ttf.setUpFontMap();
        ttf.setUpCharMap();
        //ttf.clearTable();
        //ttf.dumpCharMap();


        //ttf.printFileInfo();
        //ttf.printTableInfo();

        //WaveFile f = new WaveFile("./resources/files/sound/soundClip-2.wav");

        //program = new SpellMe(800,500); // ./resources/files/gui/spellme.fs
        //program = new ColorPicker(800,500); // ./resources/files/gui/colorpicker.fs
        //program = new QuadTreeBalls(800,500); // ./resources/files/gui/quadtree.fs
        program = new RecordSound(800,500); // ./resources/files/gui/recorder.fs
        if(program.setUpProgram()){
            program.runEngineLoop();
        }
        else{
            IOHandler.logToFile(program.getErrorMessage());
        }
    }
}

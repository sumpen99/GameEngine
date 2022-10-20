import engine.GameEngine;
import helper.enums.Color;
import helper.list.SMHashMap;
import helper.struct.PassedCheck;
import helper.font.ttf.TTFFile;
import helper.struct.SMDateTime;
import helper.text.TextWriter;
import program.RecordSound;
import helper.io.IOHandler;
import program.SpellMe;

import static helper.enums.EntrieType.ENTRIE_JSON_STRING;
//https://personal.ntu.edu.sg/ehchua/programming/java/javanativeinterface.html
//http://midi.teragonaudio.com/tech/lowaud.htm
//https://tchayen.github.io/posts/ttf-file-parsing

// TODO WRITE TEXT FROM TTF FILE

public class StartUp {

    public static void main(String[] args){
        GameEngine program;
        //IOHandler.refreshBitWiseOperations();
        IOHandler.removeFilesFromFolder("./resources/files/log/gc");
        //IOHandler.removeFilesFromFolder("./resources/files/sound");
        //IOHandler.removeFile("./resources/files/log/error/error.log");
        //WaveFile f = new WaveFile("./resources/files/sound/soundClip-2.wav");

        //program = new SpellMe(800,500); // ./resources/files/gui/spellme.fs
        //program = new ColorPicker(800,500); // ./resources/files/gui/colorpicker.fs
        //program = new QuadTreeBalls(800,500); // ./resources/files/gui/quadtree.fs
        program = new RecordSound(800,500); // ./resources/files/gui/recorder.fs
        if(program.setUpProgram()){
            char[] buf = {'h','e','l','l','o',' ','w','o','r','l','d'};
            //TextWriter.drawFontText(buf,0,0,12, Color.PALEGOLDENROD.getValue());
            program.runEngineLoop();
        }
        else{
            IOHandler.logToFile(program.getErrorMessage());
        }
    }

}

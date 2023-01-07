import engine.GameEngine;
import helper.enums.Color;
import helper.list.SMHashMap;
import helper.methods.CommonMethods;
import helper.search.InterpolationSearch;
import helper.sort.*;
import helper.sql.SqliteFile;
import helper.struct.*;
import helper.font.ttf.TTFFile;
import helper.text.TextWriter;
import helper.tree.BinarySearchTree;
import helper.tree.KDTree;
import helper.tree.RedBlackTree;
import program.ColorPicker;
import program.QuadTreeBalls;
import program.RecordSound;
import helper.io.IOHandler;
import program.SpellMe;

import java.util.ArrayList;

import static helper.enums.EntrieType.ENTRIE_JSON_STRING;
//https://personal.ntu.edu.sg/ehchua/programming/java/javanativeinterface.html
//http://midi.teragonaudio.com/tech/lowaud.htm
//https://tchayen.github.io/posts/ttf-file-parsing


public class StartUp {

    public static void main(String[] args){
        GameEngine program;
        //IOHandler.refreshBitWiseOperations();
        //IOHandler.printPoints(BezierCurve.tessellate(new Point(0,0),new Point(10,10),new Point(10,0)));
        IOHandler.removeFilesFromFolder("./resources/files/log/gc");
        //IOHandler.removeFilesFromFolder("./resources/files/sound");
        //IOHandler.removeFile("./resources/files/log/error/error.log");
        //WaveFile f = new WaveFile("./resources/files/sound/soundClip-2.wav");
        //program = new SpellMe(800,500); // ./resources/files/gui/spellme.fs
        //int size = 100000;
        //CountingSort.runTest(size);
        //BubbleSort.runTest(size);
        //HeapSort.runTest(size);
        //QuickSort.runTest(size);
        //RadixSort.runTest(size);
        //BinarySearchTree.runTest();
        //RedBlackTree.runTest();
        //KDTree tree = new KDTree(null,2);
        //tree.search(tree.testNode);
        //InterpolationSearch.testAlgorithm();
        //program = new ColorPicker(800,500); // ./resources/files/gui/colorpicker.fs
        //program = new QuadTreeBalls(800,500); // ./resources/files/gui/quadtree.fs


        testFunction();

        program = new RecordSound(800,500); // ./resources/files/gui/recorder.fs
        if(program.setUpProgram()){
            program.runEngineLoop();
        }
        else{
            IOHandler.logToFile(program.getErrorMessage());
        }
    }

    static void testFunction(){
        SqliteFile sql = new SqliteFile("./resources/files/sqlite/dbtest.sqlite3");
        IOHandler.parseSqliteFile(sql);
        if(!sql.passedCheck.passed || sql.errorCodes.size() != 0){sql.showUserErrorMessage();}
        else{sql.printFileInfo();}
    }
}
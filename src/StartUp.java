import engine.GameEngine;
import helper.enums.Color;
import helper.list.SMHashMap;
import helper.methods.CommonMethods;
import helper.sort.HeapSort;
import helper.struct.*;
import helper.font.ttf.TTFFile;
import helper.text.TextWriter;
import helper.tree.BinarySearchTree;
import helper.tree.RedBlackTree;
import program.ColorPicker;
import program.QuadTreeBalls;
import program.RecordSound;
import helper.io.IOHandler;
import program.SpellMe;

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



        int size = 10;
        BinarySearchTree tree = new BinarySearchTree();
        int toDelete = 0;
        for(int i = 0;i<size;i++){
            toDelete = CommonMethods.getRand(10000);
            tree.insert(toDelete);
        }
        tree.inOrderTraversal(tree.root,true,false,false);
        IOHandler.printString("Value To Delete: %d".formatted(toDelete));
        tree.delete(toDelete);
        tree.inOrderTraversal(tree.root,true,false,false);

        /*
        int[]arr = new int[size];
        for(int i = 0;i<size;i++){
            arr[i] = CommonMethods.getRand(size);
        }

        HeapSort.sort(arr,arr.length);
        HeapSort.printSortedArray(arr,arr.length);
        */

        /*RedBlackTree tree = new RedBlackTree();
        tree.redBlackInsert(-1);
        tree.redBlackInsert(100);
        tree.redBlackInsert(100);
        tree.redBlackInsert(0);
        tree.redBlackInsert(7);
        tree.redBlackInsert(6);
        tree.redBlackInsert(9);
        tree.redBlackInsert(1);
        tree.redBlackInsert(99);
        tree.redBlackInsert(4);
        tree.redBlackInsert(2);
        tree.redBlackInsert(3);
        tree.redBlackInsert(8);
        tree.searchTree(0);
        tree.printRedBlackTree();*/

        program = new ColorPicker(800,500); // ./resources/files/gui/colorpicker.fs
        //program = new QuadTreeBalls(800,500); // ./resources/files/gui/quadtree.fs
        //program = new RecordSound(800,500); // ./resources/files/gui/recorder.fs
        if(program.setUpProgram()){
            program.runEngineLoop();
        }
        else{
            IOHandler.logToFile(program.getErrorMessage());
        }
    }
}

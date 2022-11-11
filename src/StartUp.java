import engine.GameEngine;
import helper.enums.Color;
import helper.list.SMHashMap;
import helper.methods.CommonMethods;
import helper.sort.HeapSort;
import helper.sort.QuickSort;
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


        /*int size = 10;
        BinarySearchTree tree = new BinarySearchTree();
        int toDelete = 0;
        for(int i = 0;i<size;i++){
            toDelete = CommonMethods.getRand(10000);
            tree.insert(toDelete);
        }
        //tree.printInOrder(tree.root,true,false,false);
        //IOHandler.printString("Value To Delete: %d".formatted(toDelete));
        //tree.delete(toDelete);
        tree.printInOrder(tree.root,true,false,false);
        if(!tree.isBalanced()){
            tree.balanceTree();
            tree.isBalanced();
            tree.printInOrder(tree.root,true,false,false);
            Node temp = tree.searchForNode(toDelete);
            if(temp!= null){
                IOHandler.printString("Found Key: %d Sesrched For: %d".formatted(temp.key,toDelete));
            }
        }*/

        /*int[]arrHeap = new int[size];
        int[]arrQSort = new int[size];
        for(int i = 0;i<size;i++){
            arrHeap[i] = arrQSort[i] =  CommonMethods.getRand(size);
        }


        SMTimer timer = new SMTimer();
        timer.startClock();
        HeapSort.sort(arrHeap,arrHeap.length);
        IOHandler.printString("HeapSort RunningTime: %s".formatted(timer.getTimePassedString()));
        timer = new SMTimer();
        timer.startClock();
        QuickSort.sortIntArray(arrQSort,0,arrQSort.length-1);
        IOHandler.printString("QuickSort RunningTime: %s".formatted(timer.getTimePassedString()));*/

        //HeapSort.printSortedArray(arrHeap,arrHeap.length);

        RedBlackTree tree = new RedBlackTree();
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
        tree.printRedBlackTree();

        tree.deleteNode(-1);
        tree.deleteNode(100);
        tree.deleteNode(100);

        tree.printRedBlackTree();

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

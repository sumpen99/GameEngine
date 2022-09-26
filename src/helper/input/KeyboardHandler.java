package helper.input;
import engine.GameEngine;
import helper.io.IOHandler;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static helper.enums.KeyboardState.*;

public class KeyboardHandler extends KeyAdapter {
    private static KeyboardHandler self = null;
    private static boolean isSet = false;
    private int keyboardState;
    private char lastKey;
    private int lastKeyCode;
    public KeyboardHandler(){
        assert !KeyboardHandler.isSet :"KeyboardHandler is already set!";
        helper.input.KeyboardHandler.setInstance();
    }

    private static void setInstance(){
        KeyboardHandler.isSet = true;
    }

    public static void initKeyboardHandler(){
        if(self == null) {
            self = new KeyboardHandler();
            self.keyboardState = 0;
        }
    }

    public static KeyboardHandler getSelf(){return self;}

    private void resetKeyboard(){
        lastKey = ' ';
        keyboardState = 0;

    }

    private void setKeyboardBit(int value){
        //self.keyboardState = 0;
        keyboardState |= value;
    }

    private void clearKeyboardBit(int pos){
        keyboardState &= ~(1<<pos);
    }

    private void clearKeyboardBits(){
        keyboardState = 0;
    }

    private boolean getKeyboardBitSet(int bitSet){
        return ((keyboardState & (1 << bitSet)) != 0);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)GameEngine.exitEngineLoop();
        setKeyboardBit(SM_KEY_DOWN.getValue());
        lastKey = e.getKeyChar();
        lastKeyCode = e.getKeyCode();
    }
    @Override
    public void keyReleased(KeyEvent e) {
        //setKeyboardBit(SM_KEY_UP.getValue());
        clearKeyboardBits();
    }
    @Override
    public void keyTyped(KeyEvent e) {
        //clearKeyboardBits();
    }

    public static boolean keyboardBitSet(int bitSet){return self.getKeyboardBitSet(bitSet);}
    public static char getLastKey(){return self.lastKey;}
    public static int getLastKeyCode(){return self.lastKeyCode;}
    public static void releaseKeyPressed(){self.clearKeyboardBits();}
}

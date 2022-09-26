package helper.widget;
import helper.drawobjects.DrawObject;
import helper.drawobjects.TextWriter;
import helper.enums.*;
import helper.input.KeyboardHandler;
import helper.methods.Levenstein;
import helper.methods.PointInsideFunc;
import helper.struct.CharBuf;
import helper.struct.Vec2d;
import static helper.enums.WidgetState.*;
import static helper.methods.IntToEnum.intToKeyType;

public abstract class TextBox extends Label{
    protected SuggestionBox suggestionBox;
    protected boolean enableAutoCorrect;
    protected CharBuf str;
    protected Vec2d currentTextPos;
    protected Cursor cursor;
    protected final int increaseHeight = 2;
    protected final char NEW_LINE = Token.NEW_LINE.getChar();
    protected final char SKIP_CHAR = Token.SKIP_CHAR.getChar();
    protected final char END_OF_BUF = Token.END_OF_BUF.getChar();
    public TextBox(Object obj, Callback callback, WidgetType type, DrawObject shape, String text,String hintText, int txtclr, boolean alignText,boolean enableAutoCorrect){
        super(obj,callback,type,shape,text,hintText,txtclr,alignText);
        this.enableAutoCorrect = enableAutoCorrect;
        initTextBox();
    }

    void initTextBox(){
        int colCount,rowCount,cursorHeight;
        cursorHeight = TextWriter.getCharHeight()*increaseHeight;
        colCount = this.wObj.getSize().x/fontWidth;
        rowCount = this.wObj.getSize().y/(fontHeight*increaseHeight);
        this.currentTextPos = new Vec2d(0,0);
        this.suggestionBox = new SuggestionBox(
                this.wObj.getPos().x,
                this.wObj.getPos().y+TextWriter.getCharHeight()*3,
                this.wObj.getSize().x,
                4,
                20,
                Color.BLACK.getValue());
        this.cursor = new Cursor(TextWriter.getCharWidth(),
                TextWriter.getCharHeight(),
                this.wObj.getPos().x,this.wObj.getPos().y,
                0,cursorHeight,
                colCount,rowCount,
                this.wObj.getSize().x,this.wObj.getSize().y,
                Color.BLACK.getValue(),false);
        this.str = new CharBuf(colCount,rowCount);
    }

    @Override
    public String getBindingValue(){
        return this.str.getSubStringIndexForward(0,SKIP_CHAR);
    }

    @Override
    public void setBindingValue(Object value){
        this.str.buf = (char[])value;
    }

    @Override
    public void reposition(Vec2d offset){
        this.wObj.rePosition(offset);
        this.cursor.reposition(offset);
        this.suggestionBox.reposition(offset);
        this.alignText();
    }

    @Override
    public void pointInside(int x,int y){
        if(getWidgetBitSet(SM_TEXT_HAS_FOCUS.getIndex())){return;}
        if(PointInsideFunc.insideSelf(x,y,this.wObj)){setWidgetBit(SM_MOUSE_HOOVER.getValue());}
        else{clearWidgetBits();}
    }

    @Override
    public boolean onMouseLeftDown(int x,int y){
        if(getWidgetBitSet(SM_TEXT_HAS_FOCUS.getIndex())){
            clearWidgetBits();
            cursor.setVisibility(false);
            if(!str.containsText()){
                drawHintText = true;
                shiftBitsRight();
            }
        }
        else if(touchEventNotProcessed()){
            setWidgetBit(SM_TOUCH_PROCESSED.getValue());
            shiftBitsLeft();
            cursor.setVisibility(true);
            cursor.moveByIndex(cursor.getCurrentIndex());
            drawHintText = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseReleaseTouch(int x,int y){
        if(getWidgetBitSet(SM_TOUCH_PROCESSED.getIndex())){
            clearWidgetBits();
            setWidgetBit(SM_TEXT_HAS_FOCUS.getValue());
            //showWidgetBindToSelf();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(){
        if(getWidgetBitSet(SM_TEXT_HAS_FOCUS.getIndex())){
            char c = KeyboardHandler.getLastKey();
            int keyCode = KeyboardHandler.getLastKeyCode();
            int charValue = (c&0x7F);
            if(charValue == ' ' && enableAutoCorrect){
                if(!openSuggestionBox()){
                    String word = getLastWord();
                    if(Levenstein.checkForSuggestion(word,suggestionBox.wordSuggestions)){
                        suggestionBox.setInsertIndex(cursor.getCurrentIndex()-word.length());
                        suggestionBox.visible ^= true;
                        //return true;
                    }

                }
            }
            if(charValue < ' ' || charValue == TextWriter.getCharPadDummy()){
                if(!charPassedTest(c)){charPassedTest(keyCode);}
            }
            else{
                if(insideWidgetWidth(0)){appendNewChar();}
                else if(insideWidgetHeight(0)){jumpToLine();}
            }
            return true;
        }
        return false;
    }

    @Override
    public void alignText(){
        this.textPos.x = this.wObj.getPos().x;
        this.textPos.y = this.wObj.getPos().y+(TextWriter.getCharHeight()*increaseHeight);
        alignCenterTextPos();
    }

    @Override
    public void draw(){
        if(this.wObj.opacity != 0){this.wObj.draw();}
        if(this.drawHintText){
            if(hintText.length()*fontWidth > getSize().x)TextWriter.drawTextLine("%s".formatted(hintText),getPos().x,centerTextPos.y,getSize().x,this.txtColor);
            else{TextWriter.drawText("%s".formatted(hintText),centerTextPos.x,centerTextPos.y,this.txtColor);}
        }
        if(this.cursor.cInfo.visible){this.cursor.draw();}
        if(this.suggestionBox.visible){this.suggestionBox.draw();}
        TextWriter.drawCharBuffer(this.str.buf,this.textPos.x,this.textPos.y,cursor.cInfo.colCount,this.txtColor);
    }

    @Override
    public boolean insideWidgetWidth(int x){
        return this.cursor.horizontalSpace();
    }

    @Override
    public boolean insideWidgetHeight(int y){
        return this.cursor.verticalSpace();
    }

    protected boolean charPassedTest(int charValue){
        int index = this.cursor.getCurrentIndex();
        switch(intToKeyType(charValue)){
            case KEY_BACKSPACE:{
                if(openSuggestionBox()){suggestionBox.visible = false;}
                else{
                    char c2 = str.getCharByIndex(index-1);
                    if(c2 != END_OF_BUF){
                        if(str.removeCharByIndex(index-1)){
                            charCheckTest();
                            adjustCursorPos();
                        }
                    }
                }
                return true;
            }
            case KEY_ENTER:{
                if(openSuggestionBox()){
                    int endIndex = str.insertWordAt(suggestionBox.getInsertIndex(),suggestionBox.getSelectedWord(),cursor.getCurrentIndex());
                    cursor.moveByIndex(endIndex);
                    suggestionBox.visible = false;
                }
                else{
                    if(insideWidgetHeight(0)){addNewLine();}
                }
                return true;
            }
            case KEY_LEFT:{
                moveCursorBack();
                return true;
            }
            case KEY_UP:{
                if(openSuggestionBox()){suggestionBox.currentIndex--;}
                else{moveCursorUp();}
                return true;
            }
            case KEY_RIGHT:{
                moveCursorForward();
                return true;
            }
            case KEY_DOWN:{
                if(openSuggestionBox()){suggestionBox.currentIndex++;}
                else{moveCursorDown();}
                return true;
            }
        }
        return false;
    }

    protected void charCheckTest(){
        if(cursor.validJumpLine()) {
            currentTextPos.y = cursor.goUp();
            currentTextPos.x = cursor.goToCharOnRow(backTrackString());
        }
        else{
            currentTextPos.x = cursor.goLeft();
        }
    }

    protected void moveCursorBack(){
        currentTextPos.x = cursor.goLeft();
        adjustCursorPos();
    }

    protected void moveCursorForward(){
        currentTextPos.x = cursor.goRight();
        adjustCursorPos();
    }

    protected void moveCursorDown(){
        currentTextPos.y = cursor.goDown();
        adjustCursorPos();
    }

    protected void moveCursorUp(){
        currentTextPos.y = cursor.goUp();
        adjustCursorPos();
    }

    protected int backTrackString(){
        return str.findCharFromIndex(NEW_LINE,cursor.getCurrentIndex()-1) * cursor.cInfo.fontWidth;
    }

    protected void addNewLine(){
        currentTextPos.x = 0;
        currentTextPos.y = cursor.goDown();
        str.appendCharByIndex(cursor.getCurrentIndex(),NEW_LINE);
        adjustCursorPos();
    }

    protected void jumpToLine(){
        currentTextPos.x = cursor.cInfo.fontWidth;
        currentTextPos.y = cursor.goDown();
        str.appendCharByIndex(cursor.getCurrentIndex(),KeyboardHandler.getLastKey());
        adjustCursorPos();
    }

    protected String getLastWord(){
        return str.getSubStringIndexBackwards(cursor.getCurrentIndex()-1,SKIP_CHAR);
    }

    protected void appendNewChar(){
        currentTextPos.x = cursor.goRight();
        str.appendCharByIndex(cursor.getCurrentIndex(),KeyboardHandler.getLastKey());
        adjustCursorPos();
    }

    protected void adjustCursorPos(){
        cursor.updateColRow(currentTextPos);
        suggestionBox.setPositionY(currentTextPos.y);
    }

    protected boolean openSuggestionBox(){
        return enableAutoCorrect && suggestionBox.visible;
    }

}

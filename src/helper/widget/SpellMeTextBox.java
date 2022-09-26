package helper.widget;
import helper.drawobjects.Rectangle;
import helper.drawobjects.TextWriter;
import helper.enums.WidgetType;
import helper.input.KeyboardHandler;
import helper.methods.Levenstein;
import helper.struct.DrawValues;
import static helper.enums.WidgetState.*;
public class SpellMeTextBox extends TextBox{
    int scoreCount;
    public SpellMeTextBox(Object obj, DrawValues dww){
        super(obj,
                dww.functionMethod,
                WidgetType.SM_SPELL_ME_TEXTBOX,
                new Rectangle(dww.left,dww.top,dww.width,dww.height,dww.color,dww.opacity,dww.draw),
                dww.text,dww.hintText,dww.textColor,dww.alignText,dww.enableAutoCorrect);
    }

    @Override
    public void resetWidgetState(){
        clearWidgetBits();
        suggestionBox.visible = false;
        str.initColRow();
        cursor.setVisibility(false);
        hintText = "Your Score Was %d".formatted(scoreCount);
        alignCenterTextPos();
        drawHintText = true;
    }

    @Override
    public void setBindingValue(Object value){
        if((boolean)value){
            suggestionBox.visible = false;
            setWidgetBit(SM_TEXT_KEEP_FOCUS.getValue());
            shiftBitsLeft();
            cursor.setVisibility(true);
            cursor.moveByIndex(0);
            drawHintText = false;
            childWidget.setBindingValue("%d".formatted(0));
            scoreCount = 0;
        }
    }

    @Override
    public void pointInside(int x,int y){}

    @Override
    public boolean onMouseLeftDown(int x,int y){
        return false;
    }

    @Override
    public boolean onMouseReleaseTouch(int x,int y){
        return false;
    }

    @Override
    public boolean onKeyDown(){
        if(getWidgetBitSet(SM_TEXT_KEEP_FOCUS.getIndex())){
            char c = KeyboardHandler.getLastKey();
            int keyCode = KeyboardHandler.getLastKeyCode();
            int charValue = (c&0x7F);
            if(charValue == ' ' && enableAutoCorrect){
                if(!openSuggestionBox()){
                    String word = getLastWord();
                    if(Levenstein.checkForSuggestion(word,suggestionBox.wordSuggestions)){
                        suggestionBox.setInsertIndex(cursor.getCurrentIndex()-word.length());
                        suggestionBox.visible ^= true;
                    }
                    else{
                        scoreCount+=word.length();
                        childWidget.setBindingValue("%d".formatted(scoreCount));
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

}

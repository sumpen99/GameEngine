package helper.widget;
import helper.drawobjects.Rectangle;
import helper.enums.Color;
import helper.enums.WidgetType;
import helper.io.IOHandler;
import helper.struct.CharBuf;
import helper.struct.DrawValues;
import helper.struct.Vec2d;
import helper.text.TextWriter;

public class FlatFontTextBox extends TextBox{
    public FlatFontTextBox(Object obj, DrawValues dww){
        super(obj,
                dww.functionMethod,
                WidgetType.SM_FLAT_FONT_TEXTBOX,
                new Rectangle(dww.left,dww.top,dww.width,dww.height,dww.color,dww.opacity,dww.draw),
                dww.text,dww.hintText,dww.textColor,dww.fontSize,dww.font,dww.alignText,dww.enableAutoCorrect);
    }

    @Override
    void initTextBox(){
        int colCount,rowCount,cursorHeight;
        updateTextWriter();
        cursorHeight = TextWriter.getFontCharHeight();
        colCount = this.wObj.getSize().x/TextWriter.getFontCharWidth();
        rowCount = this.wObj.getSize().y/(TextWriter.getFontCharHeight());
        this.currentTextPos = new Vec2d(0,0);
        this.suggestionBox = new SuggestionBox(
                this.wObj.getPos().x,
                this.wObj.getPos().y+TextWriter.getFontCharHeight()*3,
                this.wObj.getSize().x,
                4,
                20,
                Color.BLACK.getValue());
        this.cursor = new Cursor(TextWriter.getFontCharWidth(),
                TextWriter.getFontCharHeight(),
                this.wObj.getPos().x,this.wObj.getPos().y,
                0,cursorHeight,
                colCount,rowCount,
                this.wObj.getSize().x,this.wObj.getSize().y,
                Color.BLACK.getValue(),false);
        this.str = new CharBuf(colCount,rowCount);
    }

    @Override
    public void alignText(){
        this.textPos.x = this.wObj.getPos().x;
        this.textPos.y = this.wObj.getPos().y;
        alignCenterTextPos();
    }

    public void updateTextWriter(){
        TextWriter.setCurrentFont(this.font);
        TextWriter.setCurrentFontUnits(this.fontSize);
    }


    @Override
    public void draw(){
        updateTextWriter();
        if(this.wObj.opacity != 0){this.wObj.draw();}
        if(this.drawHintText){
            if(hintText.length()*fontWidth > getSize().x) TextWriter.drawTextLine("%s".formatted(hintText),getPos().x,centerTextPos.y,getSize().x,this.txtColor);
            else{TextWriter.drawText("%s".formatted(hintText),centerTextPos.x,centerTextPos.y,this.txtColor);}
        }
        if(this.suggestionBox.visible){this.suggestionBox.draw();}
        if(this.cursor.cInfo.visible){this.cursor.draw();}
        TextWriter.drawFontCharBuffer(this.str.buf,this.textPos.x,this.textPos.y,cursor.cInfo.colCount,this.txtColor);
    }
}

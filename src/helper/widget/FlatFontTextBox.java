package helper.widget;

import helper.drawobjects.Rectangle;
import helper.enums.WidgetType;
import helper.struct.DrawValues;
import helper.text.TextWriter;

public class FlatFontTextBox extends TextBox{
    public FlatFontTextBox(Object obj, DrawValues dww){
        super(obj,
                dww.functionMethod,
                WidgetType.SM_FLAT_FONT_TEXTBOX,
                new Rectangle(dww.left,dww.top,dww.width,dww.height,dww.color,dww.opacity,dww.draw),
                dww.text,dww.hintText,dww.textColor,dww.fontSize,dww.alignText,dww.enableAutoCorrect);
    }


    @Override
    public void draw(){
        if(this.wObj.opacity != 0){this.wObj.draw();}
        if(this.drawHintText){
            if(hintText.length()*fontWidth > getSize().x) TextWriter.drawTextLine("%s".formatted(hintText),getPos().x,centerTextPos.y,getSize().x,this.txtColor);
            else{TextWriter.drawText("%s".formatted(hintText),centerTextPos.x,centerTextPos.y,this.txtColor);}
        }
        if(this.cursor.cInfo.visible){this.cursor.draw();}
        if(this.suggestionBox.visible){this.suggestionBox.draw();}
        TextWriter.drawFontCharBuffer(this.str.buf,this.textPos.x,this.textPos.y,cursor.cInfo.colCount,this.fontSize,this.txtColor);
    }
}

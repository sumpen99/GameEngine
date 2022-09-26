package helper.widget;
import helper.drawobjects.TextWriter;
import helper.enums.WidgetType;
import helper.struct.DrawValues;

public class LabelBox extends GridBox{
    String title;
    String[] rowValues;
    public LabelBox(DrawValues dww){
        super(WidgetType.SM_LABEL_BOX,dww.left, dww.top, dww.width, dww.height,dww.col,dww.row,dww.color);
        title = dww.text;
        rowValues = new String[5];
    }

    @Override
    public String[] getBindingValue(){
        return rowValues;
    }

    @Override
    public void setBindingValue(Object value){
        rowValues = (String[])value;
    }

    @Override
    public void draw() {
        super.draw();
        for (int i = 0; i < rowValues.length; i++) {
            if(rowValues[i] != null){
                getCenterPos(i);
                TextWriter.drawText("%s".formatted(rowValues[i]),fontWidth*2,centerPos.y+fontHeight/2, lineColor);
            }
            //TextWriter.drawText("%s".formatted(rowValues[i]),centerPos.x-((rowValues[i].length()/2)*fontWidth),centerPos.y+fontHeight/2, lineColor);
        }
    }

    void drawTitle(){
        getCenterPos(0);
        TextWriter.drawText("%s".formatted(title),centerPos.x-((title.length()/2)*fontWidth),centerPos.y+fontHeight/2, lineColor);
    }
}


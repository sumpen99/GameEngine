package helper.widget;
import helper.drawobjects.TextWriter;
import helper.enums.Color;
import helper.struct.DrawValues;

public class WaveViewBox extends LabelBox{
    boolean drawInfo;
    public WaveViewBox(DrawValues dww){
        super(dww);
        lineColor = dww.textColor;
        setDrawInfoMode(true);
    }

    public void setDrawInfoMode(boolean value){
        drawInfo = value;
    }

    @Override
    public void setBindingValue(Object value){
        if(rowValues.getClass() == value.getClass()){
            rowValues = (String[])value;
        }
        else if(Boolean.class == value.getClass()){
            drawInfo = (boolean)value;
        }
    }

    @Override
    public void draw() {
        wObj.draw();
        if(drawInfo){
            for (int i = 0; i < rowValues.length; i++) {
                if(rowValues[i] != null){
                    getCenterPos(i);
                    TextWriter.drawText("%s".formatted(rowValues[i]),fontWidth*2,centerPos.y+fontHeight, lineColor);
                }
            }
        }
    }
}

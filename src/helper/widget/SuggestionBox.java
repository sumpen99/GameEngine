package helper.widget;
import helper.drawobjects.Rectangle;
import helper.drawobjects.TextWriter;
import helper.enums.Color;
import helper.enums.WidgetType;


public class SuggestionBox extends GridBox{
    boolean visible;
    int txtColor;
    int currentIndex,insertIndex;
    String wordSuggestions[];
    public SuggestionBox(int x,int y,int width,int row,int rowsize,int color){
        super(WidgetType.SM_SUGGESTION_BOX,x,y,width,row*rowsize,1,row,Color.LIGHTPINK.getValue());
        currentIndex = 0;
        txtColor = color;
        wordSuggestions = new String[rowCount];
    }

    public void setInsertIndex(int index){insertIndex = index;}

    public int getInsertIndex(){return insertIndex;}

    public String getSelectedWord(){
        int i = currentIndex;
        currentIndex = 0;
        return wordSuggestions[i];
    }

    @Override
    public void draw(){
        if(visible && wordSuggestions != null){
            super.draw();
            currentIndex%=rowCount;
            currentIndex = currentIndex < 0 ? rowCount-1 : currentIndex;
            Rectangle.drawRectangleFilled(basePos.x,offsetPos.y+rowSize*currentIndex,getSize().x,rowSize,Color.PALEGOLDENROD.getValue());
            int row;
            for(int i = 0;i<rowCount;i++){
                row = i+1;
                TextWriter.drawText("%s".formatted(wordSuggestions[i]),basePos.x + fontWidth,(offsetPos.y+rowSize*row)-pad,this.txtColor);
            }
        }
    }

}

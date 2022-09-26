package helper.widget;
import helper.drawobjects.Line;
import helper.drawobjects.Rectangle;
import helper.drawobjects.TextWriter;
import helper.enums.Color;
import helper.enums.WidgetType;
import helper.struct.Vec2d;
import static helper.enums.DrawMode.FILL;

public abstract class GridBox extends Widget{
    Vec2d centerPos;
    Vec2d basePos;
    Vec2d offsetPos;
    int colCount,rowCount,colSize,rowSize,lineColor;
    int fontHeight,fontWidth,pad;

    public GridBox(WidgetType wType,int x,int y,int width,int height,int colcount,int rowcount,int color){
        super(null,null,wType,new Rectangle(x,y,width,height, Color.WHITESMOKE.getValue(), 1,FILL));
        centerPos = new Vec2d(0,0);
        offsetPos = new Vec2d(x,y);
        basePos = new Vec2d(x,y);
        colCount = Math.max(1,colcount);
        rowCount = Math.max(1,rowcount);
        colSize = width/colCount;
        rowSize = height/rowCount;
        lineColor = color;
        fontHeight = TextWriter.getCharHeight();
        fontWidth = TextWriter.getCharWidth();
        pad = (rowSize/fontHeight);
    }

    public void setPositionY(int y){
        this.wObj.setNewPositionY(basePos.y+y);
        offsetPos.y = basePos.y+y;
        offsetPos.x = basePos.x;
    }

    protected void getCenterPos(int index){
        centerPos.x = offsetPos.x + (colSize*colCount)/2;
        centerPos.y = (offsetPos.y+rowSize*index) + rowSize/2;
    }

    public void addValuesToRow(int index){}

    @Override
    public void reposition(Vec2d offset){
        this.wObj.rePosition(offset);
        basePos.x+=offset.x;
        basePos.y+=offset.y;
        offsetPos.x+= offset.x;
        offsetPos.y+= offset.y;
    }

    @Override
    public void draw(){
        this.wObj.draw();
        for(int i = 0;i<=rowCount;i++){
            int x1 = offsetPos.x,y1 = offsetPos.y+rowSize*i,x2 = offsetPos.x + colSize*colCount,y2 = offsetPos.y+rowSize*i;
            Line.drawLine(x1,y1,x2,y2, lineColor);
        }
        for(int i = 0;i<=colCount;i++){
            int x1 = offsetPos.x+colSize*i,y1 = offsetPos.y,x2 = offsetPos.x+colSize*i,y2 = offsetPos.y+rowSize*rowCount;
            Line.drawLine(x1,y1,x2,y2, lineColor);
        }
    }
}

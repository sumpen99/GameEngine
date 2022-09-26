package helper.widget;
import helper.drawobjects.Line;
import helper.enums.WidgetType;
import helper.io.IOHandler;
import helper.struct.CInfo;
import helper.struct.Vec2d;

public class Cursor extends Widget{
    Vec2d basePos;
    Vec2d offsetPos;
    Vec2d cursorSize;
    Vec2d currentIndexPos;
    Vec2d currentColRow;
    CInfo cInfo;


    public Cursor(int fontWidth,int fontHeight,int x,int y,int cursorWidth,int cursorHeight,int colCount,int rowCount,int gridWidth,int gridHeight,int color,boolean visible){
        super(null,null,WidgetType.SM_CURSOR,null);
        this.basePos = new Vec2d(x,y);
        this.offsetPos = new Vec2d(0,0);
        this.cursorSize = new Vec2d(cursorWidth,cursorHeight);
        this.currentIndexPos = new Vec2d(0,0);
        this.currentColRow = new Vec2d(0,0);
        this.cInfo = new CInfo(fontWidth,fontHeight,0,0,colCount,rowCount,gridWidth,gridHeight,cursorHeight,color,visible);

    }

    void moveCursor(Vec2d pos,int index){
        setCurrentIndexPos(pos);
        setCurrentIndex(index);
        offsetPos.x = basePos.x+pos.x;
        offsetPos.y = basePos.y+pos.y;
    }

    public void updateColRow(Vec2d pos){
        int row = pos.y/cInfo.cursorHeight;
        int index = getNewIndex(pos);
        moveCursor(pos,index);
        currentColRow.x = currentIndexPos.x/cInfo.fontWidth;
        currentColRow.y = currentIndexPos.y/cInfo.cursorHeight;
    }

    public boolean verticalSpace(){
        return this.currentColRow.y < cInfo.rowCount-1;
    }

    public boolean horizontalSpace(){
        return this.currentColRow.x < cInfo.colCount;
    }

    void setCurrentIndexPos(Vec2d pos){currentIndexPos.x = pos.x;currentIndexPos.y = pos.y;}
    void setCurrentIndex(int i){cInfo.currentIndex = i;}
    int getCurrentIndex(){return cInfo.currentIndex;}
    int getCursorHeight(){return cursorSize.y;}
    int getCursorWidth(){return cursorSize.x;}
    Vec2d getCursorColumnRow(){return currentColRow;}

    void moveByIndex(int index){
        int x = (index%cInfo.colCount)*cInfo.fontWidth;
        int y = (index/cInfo.colCount)*cInfo.cursorHeight;
        moveCursor(new Vec2d(x,y),index);
    }
    int getNewIndex(Vec2d pos){
        int col = pos.x/cInfo.fontWidth;
        int row = pos.y/cInfo.cursorHeight;
        return (row*(cInfo.colCount))+col;
    }
    public void setVisibility(boolean value){this.cInfo.visible = value;}
    public int goDown(){
        if(!verticalSpace())return currentIndexPos.y;
        return currentIndexPos.y + cursorSize.y;
    }
    public int goToCharOnRow(int x){
        return Math.max(0,cInfo.colCount*cInfo.fontWidth - x);
    }
    public int goUp(){return Math.max(0,currentIndexPos.y - cursorSize.y);}
    public int goRight(){return Math.min(cInfo.gridWidth,currentIndexPos.x+cInfo.fontWidth);}
    public int goLeft(){return Math.max(0,currentIndexPos.x-cInfo.fontWidth);}
    public int goColMax(){return (cInfo.colCount-1)*cInfo.fontWidth;}
    public boolean validJumpLine(){
        return (cInfo.currentIndex % cInfo.colCount == 0) &&
                (cInfo.currentIndex != 0) &&
                (cInfo.currentIndex != cInfo.colCount*cInfo.rowCount);
    }

    @Override
    public void reposition(Vec2d offset){
        basePos.x+= offset.x;
        basePos.y+= offset.y;
    }

    @Override
    public void draw(){
        if(this.cInfo.visible){Line.drawLine(offsetPos.x, offsetPos.y, offsetPos.x, offsetPos.y+ cursorSize.y,cInfo.color);}
    }

}

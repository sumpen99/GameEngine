package helper.struct;

public class CInfo {
    public int color,col,row,currentIndex,colCount,rowCount,gridWidth,gridHeight,fontWidth,fontHeight,cursorHeight;
    public boolean visible;
    public CInfo(int fontWidth,int fontHeight,int col,int row,int colCount,int rowCount,int gridWidth,int gridHeight,int cursorHeight,int color,boolean visible){
        this.fontWidth = fontWidth;
        this.fontHeight = fontHeight;
        this.col = col;
        this.row = row;
        this.currentIndex = 0;
        this.colCount = colCount;
        this.rowCount = rowCount;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.cursorHeight = cursorHeight;
        this.color = color;
        this.visible = visible;

    }
}

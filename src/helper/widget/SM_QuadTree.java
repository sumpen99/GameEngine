package helper.widget;
import engine.GameEngine;
import helper.drawobjects.Rectangle;
import helper.enums.Color;
import helper.enums.DrawMode;
import helper.enums.WidgetType;
import helper.input.MouseHandler;
import helper.struct.*;
import static helper.drawobjects.Circle.drawFilledCircle;
import static helper.enums.IntListField.*;
import static helper.enums.QuadGlobal.QUAD_HEIGHT;
import static helper.enums.QuadGlobal.QUAD_WIDTH;
import static helper.methods.CommonMethods.intersect;

public class SM_QuadTree extends Widget{
    QuadTree qTree;
    IntBoundaries win;
    Vec2d dim1,dim2,eraserOffset;
    FVec2d screener1,screener2;
    IntList out;
    SMTimer timer;
    Rectangle eraser;
    boolean drawEraser;
    public SM_QuadTree(Object obj, DrawValues dww){
        super(obj,dww.functionMethod, WidgetType.SM_QUADTREE,new Rectangle(dww.left,dww.top,dww.width,dww.height,dww.color,dww.opacity,dww.draw));
        qTree = new QuadTree(dww.objCount);
        win = new IntBoundaries();
        dim1 = new Vec2d();
        dim2 = new Vec2d();
        out = new IntList();
        screener1 = new FVec2d();
        screener2 = new FVec2d();
        timer = new SMTimer();
        GameEngine.setInfoTextStatus(true);
        eraserOffset = new Vec2d();
        eraser = new Rectangle(0,0,100,100, Color.LIGHTSTEELBLUE.getValue(),1,DrawMode.FILL);
        drawEraser = false;
    }

    void screenerInsideWorld(int x,int y){
        int sizeX = eraser.getSize().x,sizeY = eraser.getSize().y;
        MouseHandler.screenerInsideWorld((float)(x-sizeX/2),(float)(y-sizeY/2),screener1);
        MouseHandler.screenerInsideWorld((float)(x+sizeX/2),(float)(y+sizeY/2),screener2);
    }

    boolean screenOverlapsWindow(IntBoundaries win,boolean getScreen){
        Vec2d ip1 = new Vec2d();
        Vec2d ip2 = new Vec2d();
        FVec2d fp1 = new FVec2d();
        FVec2d fp2 = new FVec2d();
        int x1,y1,x2,y2;
        x1 = wObj.getPos().x;
        y1 = wObj.getPos().y;
        x2 = x1+wObj.getSize().x;
        y2 = y1+wObj.getSize().y-30;
        MouseHandler.screenToWorld(x1,y1,fp1);
        MouseHandler.screenToWorld(x2,y2,fp2);
        MouseHandler.worldToScreen(0.0f,0.0f,ip1);
        MouseHandler.worldToScreen((float)QUAD_WIDTH.getValue()-1,(float)QUAD_HEIGHT.getValue()-1,ip2);
        if(getScreen){
            win.x1 = (int)fp1.x;
            win.y1 = (int)fp1.y;
            win.x2 = (int)fp2.x;
            win.y2 = (int)fp2.y;
        }
        else{
            win.x1 = ip1.x;
            win.y1 = ip1.y;
            win.x2 = ip2.x;
            win.y2 = ip2.y;
        }
        return intersect(ip1.x, ip1.y, ip2.x, ip2.y,0,0,x2,y2);
    }

    void clearNodesFromTree(int x,int y){
        IntList out = new IntList();
        screenerInsideWorld(x,y);
        out.create(ENODE_IDX_ELT);
        qTree.query(out,screener1.x,screener1.y,screener2.x,screener2.y);
        qTree.objCount -= out.size();
        for(int i = 0;i < out.size();i++){
            qTree.remove(out.data[i]);
        }
        out.destroy();
    }

    int drawNodesInsideScreen(){
        int i = 0;
        if(screenOverlapsWindow(win,true)){
            out.create(ENODE_IDX_ELT);
            qTree.query(out,(float)win.x1,(float)win.y1,(float)win.x2,(float)win.y2);
            for(;i < out.size();i++){
                int element = out.data[i];
                final int lft = qTree.elts.get(element, ELT_IDX_LFT);
                final int top = qTree.elts.get(element, ELT_IDX_TOP);
                final int rgt = qTree.elts.get(element, ELT_IDX_RGT);
                final int btm = qTree.elts.get(element, ELT_IDX_BTM);
                int color = qTree.elts.get(element, ELT_IDX_CLR);
                MouseHandler.worldToScreen((float)lft,(float)top,dim1);
                MouseHandler.worldToScreen((float)rgt,(float)btm,dim2);
                int radie = (dim2.x-dim1.x);
                int cx = dim1.x + ((dim2.x - dim1.x)/2);
                int cy = dim1.y + ((dim2.y - dim1.y)/2);
                drawFilledCircle(cx,cy,radie, color);
            }
            out.destroy();
            if(drawEraser)eraser.draw();
        }
        return i;
    }

    @Override
    public void reposition(Vec2d offset){
        this.wObj.rePosition(offset);
        this.eraser.rePosition(offset);
    }

    @Override
    public boolean onMouseRightDown(int x,int y){
        if(touchEventNotProcessed()){
            drawEraser = true;
            eraser.setNewPosition(x-(eraser.getSize().x/2),y-(eraser.getSize().y/2));
        }
        return false;
    }

    @Override
    public boolean onMouseRightMove(int x,int y){
        if(touchEventNotProcessed()){
            drawEraser = true;
            eraser.setNewPosition(x-(eraser.getSize().x/2),y-(eraser.getSize().y/2));
            clearNodesFromTree(x,y);
        }
        return false;
    }

    @Override
    public boolean onMouseReleaseTouch(int x,int y){
        drawEraser = false;
        return false;
    }

    @Override
    public void draw(){
        timer.startClock();
        if(this.wObj.opacity != 0){this.wObj.draw();}
        int i = drawNodesInsideScreen();
        String elapsedTime = timer.getTimePassedString();
        String fps = "%s".formatted(GameEngine.getCurrentFPS());
        GameEngine.setInfoText("Drawn: %d (%d) Time: %s FPS: %s".formatted(i,qTree.objCount,elapsedTime,fps));
    }

}

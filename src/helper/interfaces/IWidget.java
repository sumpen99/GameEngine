package helper.interfaces;
import helper.struct.Vec2d;

public interface IWidget {
    void draw();
    void execFuncMethod();
    void setID(String wid);
    void setChildID(String childid);
    void setParameters(Object[]args);
    void collectFuncMethod();
    void reposition(Vec2d offset);
    void setCallbackInProgress(boolean value);
    void setOrientated(boolean value);
    void pointInside(int x,int y);
    void showWidgetBindToSelf();
    void reachOutsideWorld();
    void resetWidgetState();
    boolean onMouseLeftDown(int x,int y);
    boolean onMouseLeftMove(int x,int y);
    boolean onMouseLeftDoubleClick(int x,int y);
    boolean onMouseRightDown(int x,int y);
    boolean onMouseRightMove(int x,int y);
    boolean onMouseRightDoubleClick(int x,int y);
    boolean onMouseScrollUp(int x,int y);
    boolean onMouseScrollDown(int x,int y);
    boolean onMouseWheel(int x,int y);
    boolean onMouseReleaseTouch(int x,int y);
    boolean onFrameUpdate(int x,int y);
    boolean onText();
    boolean onKeyDown();
    boolean onKeyUp();
    boolean isOrientated();
    boolean insideWidget(int x,int y);
    boolean insideWidgetWidth(int x);
    boolean insideWidgetHeight(int y);
    Vec2d getPos();
    Vec2d getSize();
    Vec2d getCenter();
    Object getBindingValue();
    Object getParameterValue(int param);
    String getWidgetInfo();
    int getWidgetColorInfo();
    String getID();
    String getChildID();
}

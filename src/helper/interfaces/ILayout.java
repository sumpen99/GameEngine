package helper.interfaces;
import helper.struct.Vec2d;
import helper.widget.Widget;
import helper.layout.Layout;

public interface ILayout {
    boolean orientateChildHorizontal(Widget child);
    boolean orientateChildVertical(Widget child);
    boolean orientateChild(Widget w);
    boolean fitChildHorizontal(int left,int width);
    boolean fitChildVertical(int top,int height);
    boolean layoutFitInside(Layout l);
    boolean orientateLayout(Layout l);
    boolean orientateLayoutVertical(Layout l);
    boolean orientateLayoutHorizontal(Layout l);
    boolean fitLayoutHorizontal(int left,int width);
    boolean fitLayoutVertical(int top,int height);
    void setAlignment(boolean vertical);
    void reposition(Vec2d pos);
    void addLayout(Layout layout);
    void addChild(Widget w);
    void setID(String lid);
    void updateWidgetPos();
    void draw();
    String getLayoutInfo();
    String getID();
    Layout getCurrentLayout();

}

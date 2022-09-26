package helper.layout;
import helper.drawobjects.DrawObject;
import helper.interfaces.ILayout;
import helper.methods.CommonMethods;
import helper.widget.Widget;
import helper.struct.Vec2d;

public abstract class Layout implements ILayout {
    public Widget wNext;
    public Layout lNext;
    protected DrawObject lObj;
    protected Vec2d widgetPos;
    protected Vec2d layoutPos;
    protected Vec2d offsetWidget;
    protected Vec2d offsetLayout;
    protected Vec2d size;
    protected Vec2d pos;
    protected boolean alignVertical;
    protected boolean isOrientated;
    protected String lID;

    public Layout(DrawObject lobj){
        this.lObj = lobj;
        this.offsetWidget = new Vec2d(0,0);
        this.offsetLayout = new Vec2d(0,0);
        this.pos = this.lObj.getPos();
        this.size = this.lObj.getSize();
        this.widgetPos = new Vec2d(this.pos.x,this.pos.y);
        this.layoutPos = new Vec2d(this.pos.x,this.pos.y);
    }

    public void setAlignment(boolean vertical){
        this.alignVertical = vertical;
    }

    public Vec2d getPos(){return this.lObj.getPos();}

    public Vec2d getSize(){return this.lObj.getSize();}

    public void reposition(Vec2d pos){
        this.lObj.rePosition(pos);
    }

    public void updateWidgetPos(){
        this.widgetPos.x = this.pos.x;
        this.widgetPos.y = this.pos.y;
    }

    public boolean orientateChildVertical(Widget w){
        int width;
        if(fitChildVertical(w.getPos().y,w.getSize().y)){
            w.reposition(this.widgetPos);
            this.widgetPos.y = w.getPos().y+w.getSize().y;
            width = w.getPos().x+w.getSize().x;
            this.offsetWidget.x = Math.max(width,this.pos.x);
            return (width <= this.lObj.getPos().x + this.lObj.getSize().x);
        }
        else{
            if(w.isOrientated())return false;
            w.setOrientated(true);
            this.widgetPos.x = this.offsetWidget.x;
            this.widgetPos.y = this.pos.y;
            return orientateChildVertical(w);
        }
    }

    public boolean orientateChildHorizontal(Widget w){
        int height;
        if(fitChildHorizontal(w.getPos().x,w.getSize().x)){
            w.reposition(this.widgetPos);
            this.widgetPos.x = w.getPos().x+w.getSize().x;
            height = w.getPos().y+w.getSize().y;
            this.offsetWidget.y = Math.max(height,this.pos.y);
            return (height <= this.lObj.getPos().y + this.lObj.getSize().y);
        }
        else{
            if(w.isOrientated())return false;
            w.setOrientated(true);
            this.widgetPos.x = this.pos.x;
            this.widgetPos.y = this.offsetWidget.y;
            return orientateChildHorizontal(w);
        }
    }

    public boolean orientateChild(Widget w){
        if(this.alignVertical){return orientateChildVertical(w);}
        return orientateChildHorizontal(w);
    }

    public boolean fitChildHorizontal(int left,int width){
        int wMax = this.size.x + this.lObj.getPos().x;
        int wChild = width + this.widgetPos.x;
        return (wChild<=wMax);
    }

    public boolean fitChildVertical(int top,int height) {
        int wMax = this.size.y + this.lObj.getPos().y;
        int wChild = height + this.widgetPos.y;
        return (wChild<=wMax);
    }

    public void addChild(Widget w){
        String passedCheckMessage = "Valid";
        if(!orientateChild(w)){passedCheckMessage="Widget Does Not Fit Inside Layout";}
        else{
            w.next = this.wNext;
            this.wNext = w;
        }
        assert passedCheckMessage.equals("Valid") : passedCheckMessage;
    }

    public Layout getCurrentLayout(){return this.lNext;}

    /*if draworder matters
    public Layout getCurrentLayout(){
        Layout next = this.lNext;
        while(next.lNext != null){next=next.lNext;}
        return next;

    }*/

    public boolean layoutFitInside(Layout l){
        int minValueX = this.pos.x,maxValueX = this.pos.x+this.size.x;
        int minValueY = this.pos.y,maxValueY = this.pos.y+this.size.y;
        if(CommonMethods.pointInRange(minValueX,maxValueX,l.pos.x) && CommonMethods.pointInRange(minValueX,maxValueX,l.pos.x+l.size.x))
            return CommonMethods.pointInRange(minValueY,maxValueY,l.pos.y) && CommonMethods.pointInRange(minValueY,maxValueY,l.pos.y+l.size.y);
        return false;
    }
    public String getLayoutInfo(){
        return ("""
                DrawObject: %s
                WidgetPos.x: %d
                WidgetPos.y: %d
                LayoutPos.x: %d
                LayoutPos.y: %d
                Pos.x: %d
                Pos.y: %d
                Size.x: %d
                Size.y: %d""").formatted(this.lObj.shape.getValue(),
                this.widgetPos.x,
                this.widgetPos.y,
                this.layoutPos.x,
                this.layoutPos.y,
                this.pos.x,
                this.pos.y,
                this.size.x,
                this.size.y);
    }
    public void draw(){
        if(this.lObj.opacity != 0){this.lObj.draw();}
    }
    public boolean orientateLayout(Layout l){return false;}
    public boolean orientateLayoutVertical(Layout l){return false;}
    public boolean orientateLayoutHorizontal(Layout l){return false;}
    public boolean fitLayoutHorizontal(int left,int width){return false;}
    public boolean fitLayoutVertical(int top,int height){return false;}
    public String getID(){return lID;}
    public void setID(String lid){this.lID = lid;}
    public void addLayout(Layout layout){}

}

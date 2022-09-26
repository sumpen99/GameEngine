package helper.layout;
import helper.drawobjects.Rectangle;
import helper.enums.DrawMode;

public class RootLayout extends Layout{
    public RootLayout(int x,int y,int width,int height){
        super(new Rectangle(x,y,width,height,0xffffffff,0, DrawMode.TRANSPARENT));
    }

    @Override
    public boolean orientateLayout(Layout l){
        if(this.alignVertical){return orientateLayoutVertical(l);}
        return orientateLayoutHorizontal(l);
    }

    @Override
    public boolean orientateLayoutVertical(Layout l){
        int width;
        if(fitLayoutVertical(l.getPos().y,l.getSize().y)){
            l.reposition(this.layoutPos);
            this.layoutPos.y = l.getPos().y+l.getSize().y;
            width = l.getPos().x+l.getSize().x;
            this.offsetLayout.x = Math.max(width,this.pos.x);
            return (width <= this.getPos().x + this.getSize().x);
        }
        else{
            if(l.isOrientated)return false;
            l.isOrientated = true;
            this.layoutPos.x = this.offsetLayout.x;
            this.layoutPos.y = this.pos.y;
            return orientateLayoutVertical(l);
        }
    }

    @Override
    public boolean orientateLayoutHorizontal(Layout l){
        int height;
        if(fitLayoutHorizontal(l.getPos().x,l.getSize().x)){
            l.reposition(this.layoutPos);
            this.layoutPos.x = l.getPos().x+l.getSize().x;
            height = l.getPos().y+l.getSize().y;
            this.offsetLayout.y = Math.max(height,this.pos.y);
            return (height <= this.getPos().y + this.getSize().y);
        }
        else{
            if(l.isOrientated)return false;
            l.isOrientated = true;
            this.layoutPos.x = this.pos.x;
            this.layoutPos.y = this.offsetLayout.y;
            return orientateLayoutHorizontal(l);
        }
    }

    @Override
    public boolean fitLayoutHorizontal(int left,int width){
        int wMax = this.size.x + this.getPos().x;
        int wChild = width + this.layoutPos.x;
        return (wChild<=wMax);
    }

    @Override
    public boolean fitLayoutVertical(int top,int height) {
        int wMax = this.size.y + this.getPos().y;
        int wChild = height + this.layoutPos.y;
        return (wChild<=wMax);
    }

    @Override
    public void addLayout(Layout layout){
        String passedCheckMessage = "Valid";
        if(!orientateLayout(layout)){passedCheckMessage="Layout Does Not Fit Inside Root Layout";}
        else{
            layout.updateWidgetPos();
            layout.lNext = this.lNext;
            this.lNext = layout;
        }
        assert passedCheckMessage.equals("Valid") : passedCheckMessage;
    }

    /*if draworder matters
     if(this.lNext == null)this.lNext = layout;
            else{
                Layout next = this.lNext;
                while(next.lNext != null){next=next.lNext;}
                next.lNext = layout;
            }
     */
    @Override
    public void draw(){}

}

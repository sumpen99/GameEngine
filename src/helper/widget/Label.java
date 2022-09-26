package helper.widget;
import helper.drawobjects.DrawObject;
import helper.drawobjects.TextWriter;
import helper.enums.Callback;
import helper.enums.WidgetType;
import helper.interfaces.ILabel;
import helper.struct.Vec2d;

public abstract class Label extends Widget implements ILabel {
    protected String text;
    protected int txtColor;
    protected boolean drawHintText = true;
    protected Vec2d textPos;
    protected Vec2d centerTextPos;
    protected String hintText;
    protected int fontWidth,fontHeight;
    public Label(Object obj, Callback callback, WidgetType type, DrawObject shape, String text,String hinttext, int txtclr, boolean alignText){
        super(obj,callback,type,shape);
        hintText = hinttext;
        fontWidth = TextWriter.getCharWidth();
        fontHeight = TextWriter.getCharHeight();
        this.text = text;
        this.txtColor = txtclr;
        this.textPos = new Vec2d(0,0);
        this.centerTextPos = new Vec2d(0,0);
    }

    public void alignCenterTextPos(){
        this.centerTextPos.x = this.textPos.x+(getSize().x/2)-(hintText.length()/2*fontWidth);
        this.centerTextPos.y = this.textPos.y+getSize().y/2-(fontHeight);
    }

    public void alignText(){
        this.textPos.x = this.wObj.alignAxisX(this.text.length()* TextWriter.getCharWidth());
        this.textPos.y = this.wObj.alignAxisY(-TextWriter.getCharHeight()/2);
        alignCenterTextPos();
    }

    @Override
    public String getBindingValue(){
        return this.text;
    }

    @Override
    public void setBindingValue(Object value){
        this.text = (String)value;
        alignText();
    }

    @Override
    public void reposition(Vec2d offset){
        this.wObj.rePosition(offset);
        this.alignText();
    }

    @Override
    public void draw(){
        if(this.wObj.opacity != 0){this.wObj.draw();}
        //if(this.drawHintText)TextWriter.drawText("%s".formatted(hintText),centerTextPos.x,centerTextPos.y,this.txtColor);
        TextWriter.drawText(this.text,this.textPos.x,this.textPos.y,this.txtColor);
    }
}

package helper.layout;
import helper.drawobjects.Rectangle;
import helper.struct.DrawValues;

public class BoxLayout extends Layout{
    public BoxLayout(DrawValues dww){
        super(new Rectangle(dww.left,dww.top,dww.width,dww.height,dww.color,dww.opacity,dww.draw));
        this.setAlignment(dww.valign);
    }

}

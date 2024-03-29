package helper.struct;
import helper.enums.*;

public class DrawValues {
    public String[] args;
    public String bind;
    public String path;
    public String text;
    public String hintText;
    public String id;
    public String font;
    public Callback functionMethod;
    public boolean enableAutoCorrect;
    public boolean alignText;
    public boolean callback;
    public boolean selected;
    public int fontSize;
    public int objCount;
    public int lnum;
    public int col;
    public int row;
    public int left;
    public int top;
    public int width;
    public int height;
    public int radie;
    public int radiex;
    public int radiey;
    public int color;
    public int textColor;
    public int opacity;
    public int[] points;
    public boolean update;
    public boolean valign;
    public boolean halign;
    public Mask degrees;
    public DrawMode draw;
    public WidgetShape wShape;
    public WidgetType wType;

    public DrawValues(){
        hintText = "";
        text = "";
        opacity = 1;
        fontSize = 20;
        color = Color.WHITE.getValue();
        textColor = Color.BLACK.getValue();
        alignText = true;
        enableAutoCorrect = true;
    }
}

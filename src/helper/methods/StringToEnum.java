package helper.methods;
import helper.enums.*;
import helper.enums.Token;

public class StringToEnum {

    public static Token getJsonToken(char token){
        if(token == ':')return Token.JSON_OPEN_OBJECT;
        if(token == '"')return Token.JSON_OPEN_STRING;
        if(token == '[')return Token.JSON_OPEN_LIST;
        if(token == '{')return Token.JSON_OPEN_DIC;
        if(token == ']')return Token.JSON_CLOSE_LIST;
        if(token == '}')return Token.JSON_CLOSE_DIC;
        if(token == ',')return Token.JSON_NEW_PARAMETER;
        if((int)token >= 48 && (int)token <= 57)return Token.JSON_NUMBER_VALUE;
        if(((int)token >= 65 && (int)token <= 90) || ((int)token >= 97 && (int)token <= 122))return Token.JSON_STRING_VALUE;
        return Token.JSON_TOKEN_UNKNOWN;
    }

    public static Color getStrToColor(String type){
        if(type.equals("black"))return Color.BLACK;
        if(type.equals("white"))return Color.WHITE;
        if(type.equals("blue"))return Color.BLUE;
        if(type.equals("red"))return Color.RED;
        if(type.equals("green"))return Color.GREEN;
        if(type.equals("yellow"))return Color.YELLOW;
        if(type.equals("lightgreen"))return Color.LIGHTGREEN;
        if(type.equals("lightblue"))return Color.LIGHTBLUE;
        if(type.equals("lightorange"))return Color.LIGHTORANGE;
        if(type.equals("lightred"))return Color.LIGHTRED;
        if(type.equals("maroon"))return Color.MAROON;
        if(type.equals("darkred"))return Color.DARKRED;
        if(type.equals("brown"))return Color.BROWN;
        if(type.equals("firebrick"))return Color.FIREBRICK;
        if(type.equals("crimson"))return Color.CRIMSON;
        if(type.equals("tomato"))return Color.TOMATO;
        if(type.equals("coral"))return Color.CORAL;
        if(type.equals("indianred"))return Color.INDIANRED;
        if(type.equals("lightcoral"))return Color.LIGHTCORAL;
        if(type.equals("darksalmon"))return Color.DARKSALMON;
        if(type.equals("salmon"))return Color.SALMON;
        if(type.equals("lightsalmon"))return Color.LIGHTSALMON;
        if(type.equals("orangered"))return Color.ORANGERED;
        if(type.equals("darkorange"))return Color.DARKORANGE;
        if(type.equals("orange"))return Color.ORANGE;
        if(type.equals("gold"))return Color.GOLD;
        if(type.equals("darkgoldenrod"))return Color.DARKGOLDENROD;
        if(type.equals("goldenrod"))return Color.GOLDENROD;
        if(type.equals("palegoldenrod"))return Color.PALEGOLDENROD;
        if(type.equals("darkkhaki"))return Color.DARKKHAKI;
        if(type.equals("khaki"))return Color.KHAKI;
        if(type.equals("olive"))return Color.OLIVE;
        if(type.equals("yellowgreen"))return Color.YELLOWGREEN;
        if(type.equals("darkolivegreen"))return Color.DARKOLIVEGREEN;
        if(type.equals("olivedrab"))return Color.OLIVEDRAB;
        if(type.equals("lawngreen"))return Color.LAWNGREEN;
        if(type.equals("chartreuse"))return Color.CHARTREUSE;
        if(type.equals("greenyellow"))return Color.GREENYELLOW;
        if(type.equals("darkgreen"))return Color.DARKGREEN;
        if(type.equals("forestgreen"))return Color.FORESTGREEN;
        if(type.equals("lime"))return Color.LIME;
        if(type.equals("limegreen"))return Color.LIMEGREEN;
        if(type.equals("palegreen"))return Color.PALEGREEN;
        if(type.equals("darkseagreen"))return Color.DARKSEAGREEN;
        if(type.equals("mediumspringgreen"))return Color.MEDIUMSPRINGGREEN;
        if(type.equals("springgreen"))return Color.SPRINGGREEN;
        if(type.equals("seagreen"))return Color.SEAGREEN;
        if(type.equals("mediumaquamarine"))return Color.MEDIUMAQUAMARINE;
        if(type.equals("mediumseagreen"))return Color.MEDIUMSEAGREEN;
        if(type.equals("lightseagreen"))return Color.LIGHTSEAGREEN;
        if(type.equals("darkslategray"))return Color.DARKSLATEGRAY;
        if(type.equals("teal"))return Color.TEAL;
        if(type.equals("darkcyan"))return Color.DARKCYAN;
        if(type.equals("aqua"))return Color.AQUA;
        if(type.equals("cyan"))return Color.CYAN;
        if(type.equals("lightcyan"))return Color.LIGHTCYAN;
        if(type.equals("darkturquoise"))return Color.DARKTURQUOISE;
        if(type.equals("turquoise"))return Color.TURQUOISE;
        if(type.equals("mediumturquoise"))return Color.MEDIUMTURQUOISE;
        if(type.equals("paleturquoise"))return Color.PALETURQUOISE;
        if(type.equals("aquamarine"))return Color.AQUAMARINE;
        if(type.equals("powderblue"))return Color.POWDERBLUE;
        if(type.equals("cadetblue"))return Color.CADETBLUE;
        if(type.equals("steelblue"))return Color.STEELBLUE;
        if(type.equals("cornflowerblue"))return Color.CORNFLOWERBLUE;
        if(type.equals("deepskyblue"))return Color.DEEPSKYBLUE;
        if(type.equals("dodgerblue"))return Color.DODGERBLUE;
        if(type.equals("skyblue"))return Color.SKYBLUE;
        if(type.equals("lightskyblue"))return Color.LIGHTSKYBLUE;
        if(type.equals("midnightblue"))return Color.MIDNIGHTBLUE;
        if(type.equals("navy"))return Color.NAVY;
        if(type.equals("darkblue"))return Color.DARKBLUE;
        if(type.equals("mediumblue"))return Color.MEDIUMBLUE;
        if(type.equals("royalblue"))return Color.ROYALBLUE;
        if(type.equals("blueviolet"))return Color.BLUEVIOLET;
        if(type.equals("indigo"))return Color.INDIGO;
        if(type.equals("darkslateblue"))return Color.DARKSLATEBLUE;
        if(type.equals("slateblue"))return Color.SLATEBLUE;
        if(type.equals("mediumslateblue"))return Color.MEDIUMSLATEBLUE;
        if(type.equals("mediumpurple"))return Color.MEDIUMPURPLE;
        if(type.equals("darkmagenta"))return Color.DARKMAGENTA;
        if(type.equals("darkviolet"))return Color.DARKVIOLET;
        if(type.equals("darkorchid"))return Color.DARKORCHID;
        if(type.equals("mediumorchid"))return Color.MEDIUMORCHID;
        if(type.equals("purple"))return Color.PURPLE;
        if(type.equals("thistle"))return Color.THISTLE;
        if(type.equals("plum"))return Color.PLUM;
        if(type.equals("violet"))return Color.VIOLET;
        if(type.equals("fuchsia"))return Color.FUCHSIA;
        if(type.equals("orchid"))return Color.ORCHID;
        if(type.equals("mediumvioletred"))return Color.MEDIUMVIOLETRED;
        if(type.equals("palevioletred"))return Color.PALEVIOLETRED;
        if(type.equals("deeppink"))return Color.DEEPPINK;
        if(type.equals("lightpink"))return Color.LIGHTPINK;
        if(type.equals("pink"))return Color.PINK;
        if(type.equals("antiquewhite"))return Color.ANTIQUEWHITE;
        if(type.equals("beige"))return Color.BEIGE;
        if(type.equals("bisque"))return Color.BISQUE;
        if(type.equals("blanchedalmond"))return Color.BLANCHEDALMOND;
        if(type.equals("wheat"))return Color.WHEAT;
        if(type.equals("cornsilk"))return Color.CORNSILK;
        if(type.equals("lemonchiffon"))return Color.LEMONCHIFFON;
        if(type.equals("lightgoldenrodyellow"))return Color.LIGHTGOLDENRODYELLOW;
        if(type.equals("lightyellow"))return Color.LIGHTYELLOW;
        if(type.equals("saddlebrown"))return Color.SADDLEBROWN;
        if(type.equals("sienna"))return Color.SIENNA;
        if(type.equals("chocolate"))return Color.CHOCOLATE;
        if(type.equals("peru"))return Color.PERU;
        if(type.equals("sandybrown"))return Color.SANDYBROWN;
        if(type.equals("burlywood"))return Color.BURLYWOOD;
        if(type.equals("tan"))return Color.TAN;
        if(type.equals("rosybrown"))return Color.ROSYBROWN;
        if(type.equals("moccasin"))return Color.MOCCASIN;
        if(type.equals("navajowhite"))return Color.NAVAJOWHITE;
        if(type.equals("peachpuff"))return Color.PEACHPUFF;
        if(type.equals("mistyrose"))return Color.MISTYROSE;
        if(type.equals("lavenderblush"))return Color.LAVENDERBLUSH;
        if(type.equals("linen"))return Color.LINEN;
        if(type.equals("oldlace"))return Color.OLDLACE;
        if(type.equals("papayawhip"))return Color.PAPAYAWHIP;
        if(type.equals("seashell"))return Color.SEASHELL;
        if(type.equals("mintcream"))return Color.MINTCREAM;
        if(type.equals("slategray"))return Color.SLATEGRAY;
        if(type.equals("lightslategray"))return Color.LIGHTSLATEGRAY;
        if(type.equals("lightsteelblue"))return Color.LIGHTSTEELBLUE;
        if(type.equals("lavender"))return Color.LAVENDER;
        if(type.equals("floralwhite"))return Color.FLORALWHITE;
        if(type.equals("aliceblue"))return Color.ALICEBLUE;
        if(type.equals("ghostwhite"))return Color.GHOSTWHITE;
        if(type.equals("honeydew"))return Color.HONEYDEW;
        if(type.equals("ivory"))return Color.IVORY;
        if(type.equals("azure"))return Color.AZURE;
        if(type.equals("snow"))return Color.SNOW;
        if(type.equals("dimgray"))return Color.DIMGRAY;
        if(type.equals("gray"))return Color.GRAY;
        if(type.equals("darkgray"))return Color.DARKGRAY;
        if(type.equals("silver"))return Color.SILVER;
        if(type.equals("lightgray"))return Color.LIGHTGRAY;
        if(type.equals("gainsboro"))return Color.GAINSBORO;
        if(type.equals("whitesmoke"))return Color.WHITESMOKE;
        return Color.SM_COLOR_NOT_IMPLEMENTED;
    }

    public static String getIntToColor(int color){
        final Color[] colors = Color.values();
        for(int i = 0;i < colors.length;i++){
            if(colors[i].getValue() == color)return colors[i].toString();
        }
        return "";
    }

    public static Mask getStrToMask(String mask){
        if("45".equals(mask))return Mask.DEG_45;
        if("90".equals(mask))return Mask.DEG_90;
        if("135".equals(mask))return Mask.DEG_135;
        if("180".equals(mask))return Mask.DEG_180;
        if("225".equals(mask))return Mask.DEG_225;
        if("270".equals(mask))return Mask.DEG_270;
        if("315".equals(mask))return Mask.DEG_315;
        return Mask.DEG_360;
    }

    public static WidgetShape getStrToWidgetShape(String shape){
        if(shape.equals("rectangle"))return WidgetShape.SM_RECTANGLE;
        if(shape.equals("circle"))return WidgetShape.SM_CIRCLE;
        if(shape.equals("ellipse"))return WidgetShape.SM_ELLIPSE;
        if(shape.equals("polygon"))return WidgetShape.SM_POLYGON;
        if(shape.equals("triangle"))return WidgetShape.SM_TRIANGLE;
        if(shape.equals("line"))return WidgetShape.SM_LINE;
        return WidgetShape.SM_WIDGET_SHAPE_NOT_IMPLEMENTED;
    }

    public static ApiFunction getStrToApiFunction(String api){
        if(api.equals("uploadhighscore"))return ApiFunction.URL_UPLOAD_HIGHSCORE;
        if(api.equals("gethighscore"))return ApiFunction.URL_GET_HIGHSCORE;
        return ApiFunction.URL_NOT_IMPLEMENTED;
    }

    public static DrawMode getStrToDrawMode(String draw){
        if(draw.equals("fill"))return DrawMode.FILL;
        if(draw.equals("outline"))return DrawMode.OUTLINE;
        if(draw.equals("dot"))return DrawMode.DOT;
        if(draw.equals("transparent"))return DrawMode.TRANSPARENT;
        return DrawMode.SM_DRAWMODE_NOT_IMPLEMENTED;
    }

    public static Callback getStrToCallback(String callback){
        if(callback.equals("shutdown"))return Callback.SHUT_DOWN_SYSTEM;
        if(callback.equals("timer"))return Callback.TAKE_TIME;
        if(callback.equals("gethighscore"))return Callback.GET_HIGH_SCORE;
        if(callback.equals("uploadhighscore"))return Callback.UPLOAD_HIGH_SCORE;
        if(callback.equals("getcolorinfo"))return Callback.GET_COLOR_INFO;
        return Callback.SM_CALLBACK_NOT_IMPLEMENTED;
    }

    public static WidgetVariable getWidgetVariable(String var){
        if(var.equals("id"))return WidgetVariable.IDENTITY;
        if(var.equals("bind"))return WidgetVariable.BIND;
        if(var.equals("args"))return WidgetVariable.ARGS;
        if(var.equals("left"))return WidgetVariable.LEFT;
        if(var.equals("top"))return WidgetVariable.TOP;
        if(var.equals("width"))return WidgetVariable.WIDTH;
        if(var.equals("height"))return WidgetVariable.HEIGHT;
        if(var.equals("col"))return WidgetVariable.COL;
        if(var.equals("row"))return WidgetVariable.ROW;
        if(var.equals("objcount"))return WidgetVariable.OBJ_COUNT;
        if(var.equals("enableautocorrect"))return WidgetVariable.ENABLE_AUTO_CORRECT;
        if(var.equals("talign"))return WidgetVariable.TALIGN;
        if(var.equals("halign"))return WidgetVariable.HALIGN;
        if(var.equals("valign"))return WidgetVariable.VALIGN;
        if(var.equals("callback"))return WidgetVariable.CALLBACK;
        if(var.equals("path"))return WidgetVariable.PATH;
        if(var.equals("opacity"))return WidgetVariable.OPACITY;
        if(var.equals("text"))return WidgetVariable.TEXT;
        if(var.equals("hinttext"))return WidgetVariable.HINT_TEXT;
        if(var.equals("textcolor"))return WidgetVariable.TEXT_COLOR;
        if(var.equals("func"))return WidgetVariable.FUNCTION;
        if(var.equals("color"))return WidgetVariable.COLOR;
        if(var.equals("draw"))return WidgetVariable.DRAW;
        if(var.equals("points"))return WidgetVariable.POINTS;
        if(var.equals("shape"))return WidgetVariable.WSHAPE;
        if(var.equals("radie"))return WidgetVariable.RADIE;
        if(var.equals("radiex"))return WidgetVariable.RADIEX;
        if(var.equals("radiey"))return WidgetVariable.RADIEY;
        if(var.equals("update"))return WidgetVariable.UPDATE;
        if(var.equals("degrees"))return WidgetVariable.DEGREES;
        if(var.equals("type"))return WidgetVariable.WTYPE;
        return WidgetVariable.SM_VARIABLE_NOT_IMPLEMENTED;
    }


}

package helper.methods;
import helper.enums.*;
import helper.io.IOHandler;
import helper.layout.BoxLayout;
import helper.layout.Layout;
import helper.struct.DrawValues;
import helper.struct.PassedCheck;
import helper.widget.*;
import static helper.enums.GuiVariable.*;
import static helper.enums.WidgetType.*;
import static helper.enums.WidgetVariable.*;
import static helper.methods.CommonMethods.stringIsInt;
import static helper.methods.CommonMethods.parsePoints;
import static helper.methods.StringToEnum.*;


public class ParseLayout {
    public static void parseDrawValues(DrawValues dww, int start, int rows, String[] data){
        int i;
        i = start;
        while(i++ < start+rows){
            String var,val;
            String[] var_val = data[i].split(Token.VAR_VAL.getValue());
            if(var_val.length == 2){
                var = var_val[0].toLowerCase();
                val = var_val[1];
                setWidgetLayoutValue(dww,var,val);
            }
        }
    }

    private static void setWidgetLayoutValue(DrawValues dww,String var,String val){
        WidgetVariable err = SM_VARIABLE_DUMMY;
        PassedCheck verify;
        switch(getWidgetVariable(var)){
            case IDENTITY:{
                if(val.length() > 0){dww.id = val;}
                else{err = IDENTITY;}
                break;
            }
            case BIND:{
                if(val.length() > 0){dww.bind = val;}
                else{err = BIND;}
                break;
            }
            case LEFT:{
                verify = stringIsInt(val);
                if(verify.passed){dww.left = verify.iNum;}
                else{err = LEFT;}
                break;
            }
            case TOP:{
                verify = stringIsInt(val);
                if(verify.passed){dww.top = verify.iNum;}
                else{err = TOP;}
                break;
            }
            case WIDTH:{
                verify = stringIsInt(val);
                if(verify.passed){dww.width = verify.iNum;}
                else{err = WIDTH;}
                break;
            }
            case HEIGHT:{
                verify = stringIsInt(val);
                if(verify.passed){dww.height = verify.iNum;}
                else{err = HEIGHT;}
                break;
            }
            case COL:{
                verify = stringIsInt(val);
                if(verify.passed){dww.col = verify.iNum;}
                else{err = COL;}
                break;
            }
            case ROW:{
                verify = stringIsInt(val);
                if(verify.passed){dww.row = verify.iNum;}
                else{err = ROW;}
                break;
            }
            case OBJ_COUNT:{
                verify = stringIsInt(val);
                if(verify.passed){dww.objCount = verify.iNum;}
                else{err = OBJ_COUNT;}
                break;
            }
            case COLOR:{
                Color c;
                if((c = getStrToColor(val.toLowerCase()))!= Color.SM_COLOR_NOT_IMPLEMENTED){
                    dww.color = c.getValue();
                }
                else{err = COLOR;}
                break;
            }
            case TEXT_COLOR:{
                Color c;
                if((c = getStrToColor(val.toLowerCase()))!= Color.SM_COLOR_NOT_IMPLEMENTED){
                    dww.textColor = c.getValue();
                }
                else{err = TEXT_COLOR;}
                break;
            }
            case CALLBACK:{
                if(val.length() > 0){dww.callback = ("true".equals(val));}
                else{err = CALLBACK;}
                break;
            }
            case PATH:{
                if(val.length() > 0){dww.path = val;}
                else{err = PATH;}
                break;
            }
            case DRAW:{
                DrawMode d;
                if((d = getStrToDrawMode(val))!= DrawMode.SM_DRAWMODE_NOT_IMPLEMENTED){
                    dww.draw = d;
                }
                else{err = DRAW;}
                break;
            }
            case UPDATE:{
                if(val.length() > 0){dww.update = ("true".equals(val));}
                else{err = UPDATE;}
                break;
            }
            case OPACITY:{
                verify = stringIsInt(val);
                if(verify.passed){dww.opacity = verify.iNum;}
                else{err = OPACITY;}
                break;
            }
            case FUNCTION:{
                if((dww.functionMethod = getStrToCallback(val))!= Callback.SM_CALLBACK_NOT_IMPLEMENTED){break;}
                else{err = FUNCTION;}
                break;
            }
            case ENABLE_AUTO_CORRECT:{
                if(val.length() > 0){dww.enableAutoCorrect = ("true".equals(val));}
                else{err = ENABLE_AUTO_CORRECT;}
                break;
            }
            case TALIGN:{
                if(val.length() > 0){dww.alignText = ("true".equals(val));}
                else{err = TALIGN;}
                break;
            }
            case VALIGN:{
                if(val.length() > 0){dww.valign = ("true".equals(val));}
                else{err = VALIGN;}
                break;
            }
            case HALIGN:{
                if(val.length() > 0){dww.halign = ("true".equals(val));}
                else{err = HALIGN;}
                break;
            }
            case RADIE:{
                verify = stringIsInt(val);
                if(verify.passed){dww.radie = verify.iNum;}
                else{err = RADIE;}
                break;
            }
            case RADIEX:{
                verify = stringIsInt(val);
                if(verify.passed){dww.radiex = verify.iNum;}
                else{err = RADIEX;}
                break;
            }
            case RADIEY:{
                verify = stringIsInt(val);
                if(verify.passed){dww.radiey = verify.iNum;}
                else{err = RADIEY;}
                break;
            }
            case POINTS:{
                int[] points = parsePoints(val);
                if(points != null){dww.points = points;}
                else{err = POINTS;}
                break;
            }
            case ARGS:{
                String[] args = val.split(Token.POINT_SPLIT.getValue());
                if(args.length != 0){dww.args = args;}
                else{err = ARGS;}
                break;
            }
            case TEXT:{
                if(val.length() > 0){dww.text = val;}
                else{err = TEXT;}
                break;
            }
            case HINT_TEXT:{
                if(val.length() > 0){dww.hintText = val;}
                else{err = HINT_TEXT;}
                break;
            }
            case WSHAPE:{
                if((dww.wShape = getStrToWidgetShape(val))!= WidgetShape.SM_WIDGET_SHAPE_NOT_IMPLEMENTED){break;}
                else{err = WSHAPE;}
                break;

            }
            case DEGREES:{
                dww.degrees = getStrToMask(val);
                break;
            }
            default:{
                err = SM_VARIABLE_NOT_IMPLEMENTED;
                break;
            }
        }
        if(err != SM_VARIABLE_DUMMY){
            IOHandler.printValueError(err,"Variable %s Value %s %d".formatted(var,val,dww.lnum));
            assert false : "Not A Correct Widget/Layout Value, Check Out LogFile And update [helper.methods.StringToEnum]";
        }
    }

    public static boolean isLayout(String element,DrawValues dww){
        if(element.equals(SM_BOXLAYOUT.getValue())){
            dww.wType = SM_BOXLAYOUT;
            return true;
        }
        return false;
    }

    public static boolean isWidget(String element,DrawValues dww){
        if(element.equals(SM_FLAT_BUTTON.getValue())){
            dww.wType = SM_FLAT_BUTTON;
            return true;
        }
        if(element.equals(SM_ROUNDED_BUTTON.getValue())){
            dww.wType = SM_ROUNDED_BUTTON;
            return true;
        }
        if(element.equals(SM_FLAT_LABEL.getValue())){
            dww.wType = SM_FLAT_LABEL;
            return true;
        }
        if(element.equals(SM_ROUNDED_LABEL.getValue())){
            dww.wType = SM_ROUNDED_LABEL;
            return true;
        }
        if(element.equals(SM_FLAT_TEXTBOX.getValue())) {
            dww.wType = SM_FLAT_TEXTBOX;
            return true;
        }
        if(element.equals(SM_ROUNDED_TEXTBOX.getValue())) {
            dww.wType = SM_ROUNDED_TEXTBOX;
            return true;
        }
        if(element.equals(SM_SPELL_ME_TEXTBOX.getValue())) {
            dww.wType = SM_SPELL_ME_TEXTBOX;
            return true;
        }
        if(element.equals(SM_LABEL_BOX.getValue())) {
            dww.wType = SM_LABEL_BOX;
            return true;
        }
        if(element.equals(SM_CHECK_BOX.getValue())) {
            dww.wType = SM_CHECK_BOX;
            return true;
        }
        if(element.equals(SM_ROUNDED_IMAGE.getValue())) {
            dww.wType = SM_ROUNDED_IMAGE;
            return true;
        }
        if(element.equals(SM_FLAT_IMAGE.getValue())) {
            dww.wType = SM_FLAT_IMAGE;
            return true;
        }
        if(element.equals(SM_QUADTREE.getValue())) {
            dww.wType = SM_QUADTREE;
            return true;
        }
        if(element.equals(SM_CURSOR.getValue())) {
            dww.wType = SM_CURSOR;
            return true;
        }
        return false;
    }

    public static boolean isTopLevel(String element, Layout l){
        boolean vertical = false,horizontal = false;
        if((horizontal = (element.equals(SM_HORIZONTAL.getValue()))) || (vertical = (element.equals(SM_VERTICAL.getValue())))){
            l.setAlignment(vertical);
            return true;
        }
        return false;
    }

    public static Layout getNewLayout(DrawValues dww){
        if (dww.wType == SM_BOXLAYOUT) {
            return new BoxLayout(dww);
        }
        return null;
    }

    public static Widget getNewWidget(Object objCallerClass,DrawValues dww){
        switch(dww.wType){
            case SM_FLAT_BUTTON:{
                return new FlatButton(objCallerClass,dww);
            }
            case SM_ROUNDED_BUTTON:{
                return new RoundedButton(objCallerClass,dww);
            }
            case SM_ROUNDED_LABEL:{
                return new RoundedLabel(objCallerClass,dww);
            }
            case SM_FLAT_LABEL:{
                return new FlatLabel(objCallerClass,dww);
            }
            case SM_FLAT_TEXTBOX:{
                return new FlatTextBox(objCallerClass,dww);
            }
            case SM_SPELL_ME_TEXTBOX:{
                return new SpellMeTextBox(objCallerClass,dww);
            }
            case SM_ROUNDED_TEXTBOX:{
                return new RoundedTextBox(objCallerClass,dww);
            }
            case SM_ROUNDED_IMAGE:{
                return new RoundedImage(objCallerClass,dww);
            }
            case SM_FLAT_IMAGE:{
                return new FlatImage(objCallerClass,dww);
            }
            case SM_LABEL_BOX:{
                return new LabelBox(dww);
            }
            case SM_CHECK_BOX:{
                return new CheckBox(objCallerClass,dww);
            }
            case SM_QUADTREE:{
                return new SM_QuadTree(objCallerClass,dww);
            }

            default:{return null;}
        }
    }

}

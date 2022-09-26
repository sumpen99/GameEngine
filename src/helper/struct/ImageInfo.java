package helper.struct;

import static java.awt.image.DataBuffer.*;
import static java.awt.image.DataBuffer.TYPE_DOUBLE;

public class ImageInfo {
    public int width,height,chanels,size;
    public String dataType;
    public String name,imgType;

    public void setDataType(int t){
        switch(t){
            case 0:{
                dataType = "TYPE_BYTE";
                break;
            }
            case 1:{
                dataType = "TYPE_USHORT";
                break;
            }
            case 2:{
                dataType = "TYPE_SHORT";
                break;
            }
            case 3:{
                dataType = "TYPE_INT";
                break;
            }
            case 4:{
                dataType = "TYPE_FLOAT";
                break;
            }
            case 5:{
                dataType = "TYPE_DOUBLE";
                break;
            }
            default:{dataType = "UNDEFINED_TYPE";}
        }
    }
}

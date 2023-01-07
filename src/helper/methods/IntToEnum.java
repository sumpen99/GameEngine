package helper.methods;

import helper.enums.FormatVersion;
import helper.enums.KeyType;

public class IntToEnum {

    public static KeyType intToKeyType(int value){
        if(value == 8)return KeyType.KEY_BACKSPACE;
        else if(value == 10)return KeyType.KEY_ENTER;
        else if(value == 37)return KeyType.KEY_LEFT;
        else if(value == 38)return KeyType.KEY_UP;
        else if(value == 39)return KeyType.KEY_RIGHT;
        else if(value == 40)return KeyType.KEY_DOWN;
        return KeyType.KEY_DUMMY;
    }

    public static FormatVersion shortToFormatVersion(short value){
        if(value == 1){return FormatVersion.LEGACY;}
        if(value == 2){return FormatVersion.WRITE_AHEAD_LOG;}
        return FormatVersion.UNKNOWN;
    }


}

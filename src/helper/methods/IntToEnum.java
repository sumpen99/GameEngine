package helper.methods;

import helper.enums.*;

import static helper.methods.CommonMethods.validUint32;

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

    public static SchemaVersion intToSchemaVersion(int value){
        if(value == 1){return SchemaVersion.ONE;}
        if(value == 2){return SchemaVersion.TWO;}
        if(value == 3){return SchemaVersion.THREE;}
        if(value == 4){return SchemaVersion.FOUR;}
        return SchemaVersion.UNKNOWN;
    }

    public static VacuumSetting intToVacuumSetting(long rawVacuum,long isIncremental){
        if(rawVacuum==0){return VacuumSetting.NONE;}
        if(validUint32(rawVacuum) && validUint32(isIncremental)){
            if(isIncremental > 0){return VacuumSetting.INCREMENTAL;}
            else{return VacuumSetting.FULL;}
        }
        return VacuumSetting.UNKNOWN;
    }

    public static TextEncoding intToTextEncoding(int value){
        if(value==1){return TextEncoding.UTF_8;}
        if(value==2){return TextEncoding.UTF_16_LE;}
        if(value==3){return TextEncoding.UTF_16_BE;}
        return TextEncoding.UNKNOWN;
    }


}

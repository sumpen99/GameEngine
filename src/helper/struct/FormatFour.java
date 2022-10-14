package helper.struct;

import helper.list.SMHashMap;

public class FormatFour {
    public int segCount;
    public short length,language,segCountX2,searchRange,entrySelector,rangeShift;
    public short[]startCode,endCode,idDelta,idRangeOffset;
    public SMHashMap glyphIndexMap;

    public FormatFour(){
        glyphIndexMap = new SMHashMap(100,.75f);
    }
}

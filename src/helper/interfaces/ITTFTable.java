package helper.interfaces;

import helper.enums.TTFTable;
import helper.font.ttf.TTFFile;

public interface ITTFTable {
    void convertToSize(byte[] buf);
    int getTableOffset(TTFFile header);
    int getTableLength(TTFFile header);
    void setTableValue(TTFFile header);
}

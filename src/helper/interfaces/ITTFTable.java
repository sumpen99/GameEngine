package helper.interfaces;

import helper.enums.TTFTable;
import helper.font.ttf.TTFFile;

public interface ITTFTable {
    void convertToSize(byte[] buf);
    void setTableValue(TTFFile header);
}

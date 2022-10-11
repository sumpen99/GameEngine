package helper.font.ttf;
import helper.interfaces.ITTFTable;
import helper.interfaces.ITTFTableInfo;

public abstract class TTFTableBase implements ITTFTable {
    public ITTFTableInfo self;
    public TTFTableBase(ITTFTableInfo tableInfo){
        self = tableInfo;
    }

    @Override
    public void convertToSize(byte[] buf){
        self.setValues(buf);
    }
}

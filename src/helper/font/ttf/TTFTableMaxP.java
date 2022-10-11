package helper.font.ttf;
import helper.enums.TTFTable;

public class TTFTableMaxP extends TTFTableBase{
    public TTFTableMaxP(){
        super(new TTFMaxPInfo());
    }

    @Override
    public void setTableValue(TTFFile header){
        header.setTableValue(TTFTable.MAXP,self);
    }

}

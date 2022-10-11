package helper.font.ttf;
import helper.enums.TTFTable;

public class TTFTableHead extends TTFTableBase{
    public TTFTableHead(){
        super(new TTFHeadInfo());
    }

    @Override
    public void setTableValue(TTFFile header){
        header.setTableValue(TTFTable.HEAD,self);
    }

}

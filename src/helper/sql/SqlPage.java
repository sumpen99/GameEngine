package helper.sql;
import helper.enums.ErrorCodes;
import helper.enums.SqlPageType;
import helper.interfaces.ISqlPage;
import helper.io.IOHandler;
import java.util.ArrayList;

public abstract class SqlPage implements ISqlPage {
    public ArrayList<ErrorCodes> errorCodes;
    public SqlPageType pageType;

    public SqlPage(SqlPageType pageType){
        this.pageType = pageType;
        errorCodes = new ArrayList<>();
    }

    public void showUserErrorMessage(){
        if(errorCodes.size() > 0){
            IOHandler.printErrorCodes(errorCodes);}
    }

    public void printHeaderInfo(){
        IOHandler.printString(toString());
    }

}

package helper.interfaces;
import helper.enums.SqliteHeaderBits;

public interface ISqlPage {
    void readHeaderInfo(byte[] pageContent);
    void convertToSize(SqliteHeaderBits dst, byte[] pageContent);
    void showUserErrorMessage();
    void validPageType();
}

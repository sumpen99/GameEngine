package helper.interfaces;

public interface ITTFTableInfo {
    void dumpValues();
    void setValues(byte[] buf);
    void checkForValuesBelowZero();
    Object getValues();
}

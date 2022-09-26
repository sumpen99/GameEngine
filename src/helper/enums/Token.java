package helper.enums;

public enum Token {
    JSON_OPEN_OBJECT("JSON_OPEN_OBJECT",':'),
    JSON_OPEN_STRING("JSON_OPEN_STRING",'"'),
    JSON_OPEN_LIST("JSON_OPEN_LIST",'['),
    JSON_OPEN_DIC("JSON_OPEN_DIC",'{'),
    JSON_CLOSE_LIST("JSON_CLOSE_LIST",']'),
    JSON_CLOSE_DIC("JSON_CLOSE_DIC",'}'),
    JSON_NEW_PARAMETER("JSON_NEW_PARAMETER",','),
    JSON_NUMBER_VALUE("JSON_NUMBER_VALUE",'1'),
    JSON_STRING_VALUE("JSON_STRING_VALUE",'A'),
    JSON_BOOLEAN_VALUE("JSON_BOOLEAN_VALUE",'0'),
    JSON_TOKEN_UNKNOWN("JSON_TOKEN_UNKNOWN",'_'),
    END_OF_GUI("**//**",'*'),
    END_OF_ELEMENT(";",';'),
    END_OF_BUF("END_OF_BUF",'\0'),
    VAR_VAL(":",':'),
    POINT_SPLIT(",",','),
    SKIP_CHAR("SKIP_CHAR",' '),
    NEW_LINE("NEW_LINE",'\n'),
    NEW_TAB("NEW_TAB",'\t'),
    SKIP_LINE("SKIP_LINE",'#'),
    EMPTY_CHAR("EMPTY_CHAR",'\u0000');

    private final String strValue;
    private final char chrValue;
    Token(String strValue,char chrValue){this.strValue = strValue;this.chrValue = chrValue;}
    public String getValue(){return this.strValue;}
    public char getChar(){return this.chrValue;}
}

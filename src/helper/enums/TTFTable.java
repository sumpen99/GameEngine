package helper.enums;

public enum TTFTable {
    HEAD("head"),
    HHEA("hhea"),
    MAXP("maxp"),
    OS_2("OS/2"),
    HMTX("hmtx"),
    CMAP("cmap"),
    CVT("cvt"),
    LOCA("loca"),
    GLYF("glyf"),
    NAME("name"),
    POST("post"),
    GASP("gasp"),
    DSIG("DSIG"),
    GDEF("GDEF"),
    FFTM("FFTM"),
    TTF_TABLE_TAG_UNKNOWN("Unknown Table Tag");
    private final String value;
    TTFTable(String str){this.value = str;}
    public String getValue(){return value;}
}

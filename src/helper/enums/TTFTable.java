package helper.enums;

public enum TTFTable {
    LOCA("loca"),
    GLYF("glyf"),
    DSIG("DSIG"),
    POST("post"),
    NAME("name"),
    CMAP("cmap"),
    HMTX("hmtx"),
    CVT("cvt"),
    GASP("gasp"),
    FFTM("FFTM"),
    HEAD("head"),
    HHEA("hhea"),
    GDEF("GDEF"),
    MAXP("maxp"),
    OS_2("OS/2"),
    TTF_TABLE_TAG_UNKNOWN("Unknown Table Tag");
    private final String value;
    TTFTable(String str){this.value = str;}
    public String getValue(){return value;}
}

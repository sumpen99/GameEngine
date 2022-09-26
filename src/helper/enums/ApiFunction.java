package helper.enums;

public enum ApiFunction {
    URL_UPLOAD_HIGHSCORE("uploadhighscore"),
    URL_GET_HIGHSCORE("gethighscore"),
    URL_NOT_IMPLEMENTED("not a valid URL");
    private final String value;
    ApiFunction(String value){this.value = value;}
    public String getValue(){return this.value;}
}

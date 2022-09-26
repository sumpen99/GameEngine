package helper.enums;

public enum GuiVariable{
    SM_HORIZONTAL("horizontal"),
    SM_VERTICAL("vertical");
    private final String value;
    GuiVariable(String value){this.value = value;}
    public String getValue(){return this.value;}

}

package helper.enums;

public enum WidgetType{
    SM_BOXLAYOUT("boxlayout"),
    SM_FLAT_BUTTON("flatbutton"),
    SM_ROUNDED_BUTTON("roundedbutton"),
    SM_FLAT_TEXTBOX("flattextbox"),
    SM_SPELL_ME_TEXTBOX("spellmetextbox"),
    SM_ROUNDED_TEXTBOX("roundedtextbox"),
    SM_ROUNDED_LABEL("roundedlabel"),
    SM_FLAT_LABEL("flatlabel"),
    SM_CURSOR("cursor"),
    SM_ROUNDED_IMAGE("roundedimage"),
    SM_FLAT_IMAGE("flatimage"),
    SM_LABEL_BOX("labelbox"),
    SM_CHECK_BOX("checkbox"),
    SM_SUGGESTION_BOX("suggestionbox"),
    SM_QUADTREE("quadtree"),
    SM_WIDGET_NOT_IMPLEMENTED("not a valid widget");
    private final String value;
    WidgetType(String value){this.value = value;}
    public String getValue(){return this.value;}
}

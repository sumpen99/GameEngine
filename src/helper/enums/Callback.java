package helper.enums;

public enum Callback {
    READ_WAVE_FILE("readWaveFile"),
    PLAY_WAVE_FILE("playWaveFile"),
    RECORD_AUDIO("recordAudio"),
    GET_HIGH_SCORE("getHighScore"),
    UPLOAD_HIGH_SCORE("uploadHighScore"),
    TAKE_TIME("startClock"),
    SHUT_DOWN_SYSTEM("shutDown"),
    GET_COLOR_INFO("getColorInfo"),
    SM_CALLBACK_NOT_IMPLEMENTED("Not a valid callback");
    private final String value;
    Callback(String value){this.value = value;}
    public String getValue(){return this.value;}
}

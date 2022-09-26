package helper.interfaces;

public interface IGameEngine {
    boolean setUpProgram();
    boolean parseGuiFile(String pathGui);
    void onUserUpdate(float fElapsedTime);

}

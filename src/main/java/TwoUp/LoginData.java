package TwoUp;

public class LoginData {
    private static LoginData instance;

    static String username;

    private LoginData(){
        //private constructor
    }
    public static void setInstance(LoginData instance) {
        LoginData.instance = instance;
    }
    public static LoginData getInstance(){
        if(instance == null) {
           instance = new LoginData();
        }
        return instance;
    }

public static void setUsername(String username){
        LoginData.username = username;
}
public static String getUsername(){
        return username;
}
}
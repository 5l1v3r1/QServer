
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class UserObj {

    String userName;
    String userCode;
    DataOutputStream giden;
    DataInputStream gelen;
    String getuName;

    public UserObj(String uName, String uCode, DataInputStream is, DataOutputStream os) {
        try { // Stream işlemlerinde takılmayı önlemek için
            userName = uName;
            userCode = uCode;
            giden = os;
            gelen = is;
            giden.flush();
        } catch (Exception e1) {
        }
    }

    public String getUCode() {
        return userCode;
    }

    public String getUName() {
        return userName;
    }

    public DataOutputStream getGiden() {
        return giden;
    }

    public DataInputStream getGelen() {
        return gelen;
    }

    public void mesajYolla(String from, String to, String msj) {
        //<msg:GONDEREN:ALICI>MESAJ</msg>
        try {
            giden.writeUTF("<msg:" + from + ":" + to + ">" + msj + "</msg>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

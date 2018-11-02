
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

public class msjBekle extends Thread {

    Vector Users;
    String UserName;
    String UserCode;
    DataInputStream gelen;
    DataOutputStream giden;

    public msjBekle(DataInputStream is, DataOutputStream os, Vector v, String UserName,String UserCode) {
        try {

            Users = v;
            this.UserName = UserName;
            this.UserCode=UserCode;
            gelen = is;
            giden = os;
            giden.flush();

        } catch (Exception e) {
        }
    }

    public String msjParser(String m) {
        //<msg:ali:veli>Merhaba Ali! :)</msg>
        System.out.println(m);
        int ilktag, sontag;
        ilktag = m.indexOf('>', 0) + 1;
        sontag = m.indexOf("</msg>", ilktag);
        return m.substring(ilktag, sontag);
    }

    public synchronized String aliciParser(String m) {
        //<msg:ali>Merhaba Ali! :)</msg>
        //<msg:GONDEREN:ALICI>MESAJ</msg>
        int ilktag1, ilktag, sontag;
        ilktag1 = m.indexOf(':', 0) + 1;
        ilktag = m.indexOf(':', ilktag1) + 1;
        sontag = m.indexOf('>', ilktag);
        return m.substring(ilktag, sontag);
    }

    public synchronized String gonderenParser(String m) {
        //<msg:ali>Merhaba Ali! :)</msg>
        //<msg:GONDEREN:ALICI>MESAJ</msg>
        int ilktag1, ilktag, sontag;
        ilktag = m.indexOf("<msg:", 0) + 5;
        sontag = m.indexOf(':', ilktag);
        return m.substring(ilktag, sontag);
    }

    public synchronized int getUserIndex(String uNme) {
        int uIndex = -1;
        UserObj uO;
        for (int i = 0; i < Users.size(); i++) {
            uO = (UserObj) Users.elementAt(i);
            if (uO.getUCode().equals(uNme)) {
                uIndex = i;
                break;
            }
        }
        return uIndex;
    }

    public void run() {
        System.out.println("Server: mesajBekleme Threadi çalıştı..");
        String msjAl;
        UserObj alici, uO;
        boolean donBabaDonelim = true; // olumsuz bir durum var mı?
        int aliciIndex;
        try {

            while (donBabaDonelim) {
                System.out.println("Mesaj Bekleniyor");
                msjAl = gelen.readUTF();
                System.out.println("Mesaj Geldi");
                if (msjAl.equals("<Disconnect>")) {
                    donBabaDonelim = false;
                    System.out.println("Koptu...");
                } else if (msjAl.equals("<KullaniciListesi>")) {
                    giden.writeUTF("<KullaniciListesi>");
                    giden.writeInt(Users.size());
                    System.out.println(Users.size() + "");
                    System.out.println("Listeyi istedi...");
                    for (int a = 0; a < Users.size(); a++) {
                        // kullADI:kullKODU
                        uO = (UserObj) Users.elementAt(a);
                            giden.writeUTF(uO.getUName() + ":" + uO.getUCode());
                            System.out.println(uO.getUName() + ":" + uO.getUCode());
                            System.out.println("bi dene gitti");
                        
                    }
                    giden.writeUTF("</KullaniciListesi>");
                    System.out.println("Bitti mesajı yollandı");
                    giden.flush();
                    System.out.println("Liste Gönderildi");

                } else if (msjAl.startsWith("<msg:")) {
                    System.out.println("Mesaj geldi..");
                    aliciIndex = getUserIndex(aliciParser(msjAl));
                    if (aliciIndex < 0) {
                        giden.writeUTF("<KullaniciAdiHatali>");
                        System.out.println("Alıcı adı bulunamadı");
                    } else {
                        alici = (UserObj) Users.elementAt(aliciIndex);
                        alici.mesajYolla(UserCode, aliciParser(msjAl), msjParser(msjAl));
                        System.out.println("\nYollandı");
                        System.out.println(msjAl);
                        System.out.println(aliciParser(msjAl) + "==>" + gonderenParser(msjAl));
                        System.out.println("\n");
                    }
                    System.out.println("Mesaj işlendi");
                }
            }
            Users.removeElementAt(getUserIndex(UserCode)); // sonsuz döngüden çıktı, bu kişiyi vektörden sil..
            System.out.println("Ayrıldı:" + UserName);
        } catch (Exception e1) {
        }


    }
}

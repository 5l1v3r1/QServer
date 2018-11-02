
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.UIManager;

public class Server extends javax.swing.JFrame {

    private ServerSocket ev;
    private Socket tunel;
    private DataInputStream gelen;
    private DataOutputStream giden;
    private int port = 5005; //65535 port açılabilir...
    String uName;
    Vector kullanicilar = new Vector();

    public Server() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        initComponents();
        new Thread(new listeYenileyici(this)).start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Qostebek Server");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        jScrollPane2.setViewportView(jList1);

        jLabel1.setText("Bağlı Kullanıcılar :");

        jLabel2.setText("Durum");

        jButton1.setText("Listeyi Yenile");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        getUserList();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        try {
            UserObj uObj;
            for (int u = 0; u < kullanicilar.size(); u++) {
                uObj = (UserObj) kullanicilar.elementAt(u);
                uObj.giden.writeUTF("<ShotDown>");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_formWindowClosing

    public boolean uNameKontrol(String uN) {
        boolean k = true;
        UserObj uObj;
        String uNeym;
        if (uN.equals("") || uN.length() < 3 || uN.length() > 10) {
            return false;
        }

        for (int u = 0; u < kullanicilar.size(); u++) {
            uObj = (UserObj) kullanicilar.elementAt(u);
            uNeym = uObj.getUName();
            if (uNeym.equals(uN)) {
                k = false;
                break;
            }
        }
        return k;
    }

    public synchronized void getUserList() {
        UserObj uObj;
        UserObj uObj2;
        DefaultListModel dlm = new DefaultListModel();
        try {

            for (int u = 0; u < kullanicilar.size(); u++) {
                uObj = (UserObj) kullanicilar.elementAt(u);
                dlm.addElement(uObj.getUName());
            }
            jList1.setModel(dlm);

        } catch (Exception e) {
        }
    }

    public String UserIDGenerator() {
  
      String gen;
        Random r = new Random();
        gen = "" + Math.abs(r.nextLong());
        System.out.println(gen);
        return gen;
    }

    public void basla() {
        setVisible(true);  //bu pencereyi grünür yapıyor..
        UserObj u; // Kullanıcı nesnesi kullanılacak..
        String uID;
        try {
            ev = new ServerSocket(port, 30);
            jTextArea1.append("Server çalışmaya başladı..");
            while (true) { // sonsuz döngüyle, bağlantı bekliyor...
                tunel = ev.accept(); // Bağlantı gelirse, Kabul et, Socket aç...
                jTextArea1.append("\n\tYeni bir kullanıcı bağlandı!");
                gelen = new DataInputStream(tunel.getInputStream()); // Gelen Akışı al..
                giden = new DataOutputStream(tunel.getOutputStream()); // Giden Akışı al
                giden.flush(); // Mesajların gittiğinden emin ol
                uName = gelen.readUTF(); // Kullanıcının ismini bekle..
                if (uNameKontrol(uName)) { // Kullanıcınıın girdiği isim geçerli mi?
                    giden.writeUTF("<Baglanti:Basarili>"); // isim geçerli, hoşgeldi..
                    uID = UserIDGenerator();
                    giden.writeUTF(uID);
                    jTextArea1.append("\n\tBağlanan:" + uName); // Kullanıcıyı Pencerede göster
                    u = new UserObj(uName, uID, gelen, giden); // Bu kullanıcıya ait yeni bir nesne oluştur.Ona has 19 haneli sayı oluştur.
                    kullanicilar.add(u); // bu nesneyi vektöre al..
                    new Thread(new msjBekle(gelen, giden, kullanicilar, uName,uID)).start(); //  Bu kullanıcıdan gelecek verileri okuyacak olan bir thread oluştur.
                } else {
                    giden.writeUTF("<Hata:KAdiKullaniliyor>");
                }
                getUserList();
            }

        } catch (Exception e1) {
        }
    }

    public static void main(String args[]) {
        new Server().basla();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}

public class listeYenileyici extends Thread {

    private Server server;

    public listeYenileyici(Server s) {
        server = s;
    }

    public void run() {
        try {
            while(true){
            server.getUserList();
            Thread.sleep(1000);
            }
        } catch (Exception e) {
        }

    }
}

package clientlab;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author fachr
 */
public class Browser extends StackPane {

    WebView webView = new WebView();
    public WebEngine webEngine = webView.getEngine();
    final String urlAwal = "http://lab.ti/user/log_penggunaan_lab/auth/index/" + getMacAdress();
    final String urlAwal2 = "http://lab.ti/user/log_penggunaan_lab/auth/login";
    final String urlAwal3 = "http://lab.ti/user/log_penggunaan_lab/log_penggunaan/create?q";
    final String urlAwal4 = "http://lab.ti/user/log_penggunaan_lab/log_penggunaan/create_action";
    final String urlShutdown = "http://lab.ti/user/log_penggunaan_lab/auth/shutdown";
    
    String urlSekarang;

    public Browser(Stage stage) {
        webEngine.load(urlAwal);
        getChildren().add(webView);

        /*Event ketika halaman url berubah*/
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                /* Jika perpindahan url sukses */
                if (newValue == Worker.State.SUCCEEDED) {
                    /* get url yang sekarang */
                    urlSekarang = webView.getEngine().getLocation();
                    /* Kalau url masih sama dengan kondisi awal -> belum login */
                    if (urlSekarang.equalsIgnoreCase(urlAwal) || urlSekarang.equalsIgnoreCase(urlAwal2) || urlSekarang.equalsIgnoreCase(urlAwal3) || urlSekarang.equalsIgnoreCase(urlAwal4)) {
                        stage.setAlwaysOnTop(true);
                        stage.setMaximized(true);
                        stage.toFront();
                    } else if(urlSekarang.equalsIgnoreCase(urlShutdown)){
                        try {
                            shutdown();
                        } catch (RuntimeException | IOException ex) {
                            Logger.getLogger(Browser.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        stage.setAlwaysOnTop(false);
                        stage.setMaximized(false);
                        stage.toBack();                                                                        
                    }
                }
            }
        }
        );
    }

    private static String getMacAdress() {
        StringBuilder sb = new StringBuilder();
        try {
            InetAddress ip = InetAddress.getLocalHost();
            //System.out.println("Current IP address : " + ip.getHostAddress());
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
            }
        } catch (UnknownHostException | SocketException e) {
            System.out.println(e.getMessage());
        }
        return sb.toString();
    }
    
    public static void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;
        String operatingSystem = System.getProperty("os.name");
        if (operatingSystem.equalsIgnoreCase("lin") || operatingSystem.equalsIgnoreCase("mac")) {
            shutdownCommand = "shutdown -h now";
        }
        else if (operatingSystem.equalsIgnoreCase("win")) {
            shutdownCommand = "shutdown.exe -s -t 0";
        }
        else {
            throw new RuntimeException("Unsupported operating system.");
        }
        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    } 

}

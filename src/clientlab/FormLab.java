package clientlab;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author fachr
 */
public class FormLab extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage firstStage) {
        
        /*Stage pertama untuk menghilangkan icon pada taskbar*/
        firstStage.initStyle(StageStyle.UTILITY);
        firstStage.setOpacity(0);
        firstStage.setHeight(0);
        firstStage.setWidth(0);
        firstStage.show();

        /*Stage kedua untuk menampilkan UI di stage pertama*/
        Stage secondStage = new Stage();
        secondStage.initOwner(firstStage);
        secondStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(new Browser(secondStage), 1024, 800);
        secondStage.setScene(scene);
        secondStage.setMaximized(true);
        secondStage.setAlwaysOnTop(true);
        secondStage.toFront();
        secondStage.show();
        
        /*Untuk mencegah force close melalui ALT + F4*/
        Platform.setImplicitExit(false);
        secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
//                Alert a1 = new Alert(AlertType.NONE, "TAK BOLEH!!", ButtonType.OK);
//                a1.initOwner(secondStage);
//                a1.show();
                t.consume();
            }
        });

    }

}

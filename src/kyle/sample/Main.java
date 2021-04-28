package kyle.sample;

/** JavaFX */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String CSS = getClass().getResource("styles.css").toExternalForm();

        Parent root = FXMLLoader.load(getClass().getResource("studentPage.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add( CSS );

        primaryStage.setTitle( "Student thingy" );
        primaryStage.setScene( scene );
        primaryStage.setResizable( false );

        primaryStage.setOnCloseRequest( e -> {
            Query.getInstance().closeConnection();
        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


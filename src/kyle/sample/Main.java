package kyle.sample;

/* JavaFX */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Load css unfortunately not responsive design, was a bit hard to do with the limited -fx-css
        String CSS = getClass().getResource("styles.css").toExternalForm();

        // Load FXML
        Parent root = FXMLLoader.load(getClass().getResource("studentPage.fxml"));

        // Generate scene from FXML and add CSS
        Scene scene = new Scene(root);
        scene.getStylesheets().add( CSS );

        // Sets the window title and stuff
        primaryStage.setTitle( "Student thingy" );
        primaryStage.setScene( scene );

        // When close is pressed close the connection (basically why we need a singleton)
        primaryStage.setOnCloseRequest( e -> Query.getInstance().closeConnection());

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


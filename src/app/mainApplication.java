package app;

import java.io.IOException;

import controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class mainApplication extends Application {

    private Stage primaryStage;
    private AnchorPane mainLayout;
    
    @FXML
    private TextField DEBBPath;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Calculate energy consumption");

        initMainLayout();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initMainLayout() {
        try {
            // Load main layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(mainApplication.class.getResource("/view/MainView.fxml"));
            mainLayout = (AnchorPane) loader.load();
            
            // Show the scene containing the main layout.
            Scene scene = new Scene(mainLayout);
            
            primaryStage.setScene(scene);
            MainViewController controller = loader.getController();
            controller.init();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
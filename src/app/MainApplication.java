package app;

import java.io.IOException;

import controller.MainViewController;
import controller.WorkGenViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApplication extends Application {

    private Stage primaryStage;
    private AnchorPane mainLayout;
    private AnchorPane workLayout;
    
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
        	Object[] functionList = new Object[7];
            FXMLLoader loader1 = new FXMLLoader();
            loader1.setLocation(MainApplication.class.getResource("/view/MainView.fxml"));
            mainLayout = (AnchorPane) loader1.load();
            MainViewController mainViewController = loader1.getController();
            mainViewController.init(functionList);
            functionList[0] = mainLayout;
            
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(MainApplication.class.getResource("/view/WorkGenView.fxml"));
            workLayout = (AnchorPane) loader2.load();
            WorkGenViewController workGenViewController = loader2.getController();
            workGenViewController.init(functionList);
            workLayout.setVisible(false);
            functionList[1] = workLayout;
            
    		StackPane mainStackPane = new StackPane();
    		mainStackPane.getChildren().add(mainLayout);//登陆面板，可见
    		mainStackPane.getChildren().add(workLayout);//主页，不可见
            // Show the scene containing the main layout.
            Scene scene = new Scene(mainStackPane);
            primaryStage.setScene(scene);
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
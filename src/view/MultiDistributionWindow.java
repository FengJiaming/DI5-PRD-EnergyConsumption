package view;

import app.mainApplication;
import controller.MainViewController;
import controller.MultiDistViewController;
import controller.WorkGenViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MultiDistributionWindow extends Application {
	private Stage primaryStage;
    private AnchorPane mainLayout;
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MultiDistributionWindow.class.getResource("/view/MultiDistributionView.fxml"));
        mainLayout = (AnchorPane) loader.load();
        MultiDistViewController multiDistController = loader.getController();
        multiDistController.init();
        // Show the scene containing the main layout.
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
//        primaryStage.showAndWait();
        primaryStage.show();
		
	}
}

package controller;

import java.io.File;

import app.ConfigurationOptions;
import controller.simulator.DataCenterWorkloadSimulator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class MainViewController {

	@FXML
	private AnchorPane root;

	@FXML
	private Button DEBBButton;

	@FXML
	private Button swfButton;

	@FXML
	private TextField DEBBPath;

	@FXML
	private TextField swfPath;

	@FXML
	void DEBBChooseClick(ActionEvent event) {
		FileChooser DEBBChooser = new FileChooser();
		DEBBChooser.setTitle("Load DEBB Config");
		File file = DEBBChooser.showOpenDialog(root.getScene().getWindow());
		if (file != null) {
			System.out.println(file.getPath());
			DEBBPath.setText(file.getPath());
		}
	}

	@FXML
	void swfButtonClick(ActionEvent event) {
		FileChooser swfChooser = new FileChooser();
		swfChooser.setTitle("Select workload");
		File file = swfChooser.showOpenDialog(root.getScene().getWindow());
		if (file != null) {
			System.out.println(file.getPath());
			swfPath.setText(file.getPath());
		}
	}

	@FXML
	void runSimulationClick(ActionEvent event) {
		
		ConfigurationOptions configurationOptions = ConfigurationOptions.getConfiguration(DEBBPath.getText(), swfPath.getText());
		DataCenterWorkloadSimulator simulator = new DataCenterWorkloadSimulator();
		simulator.run(configurationOptions);
		
	}
}

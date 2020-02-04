package controller;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class MainViewController {
	@FXML
	private Button DEBBButton;

	@FXML
	private TextField DEBBPath;

	@FXML
	private AnchorPane root;

	@FXML
	private Button swfButton;

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

}

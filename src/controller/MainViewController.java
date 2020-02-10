package controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import app.ConfigurationOptions;
import controller.simulator.DataCenterWorkloadSimulator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
	private Button runButton;
	
	@FXML
	private TextArea console;
	
	public void init() {
		DEBBPath.setText("example/experiment1/resources1.xml");
		swfPath.setText("example/experiment1/workload.swf");
	}
	
	@FXML
	void DEBBChooseClick(ActionEvent event) {
		FileChooser DEBBChooser = new FileChooser();
		DEBBChooser.setTitle("Load DEBB Config");
		File file = DEBBChooser.showOpenDialog(root.getScene().getWindow());
		if (file != null) {
//			System.out.println(file.getPath());
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
	void runButtonClick(ActionEvent event) {
		System.out.println(DEBBPath.getText());
		ConfigurationOptions configurationOptions = ConfigurationOptions.getConfiguration(DEBBPath.getText(), swfPath.getText());
		DataCenterWorkloadSimulator simulator = new DataCenterWorkloadSimulator();
		System.setOut(new PrintStream(new Console(console)));
		System.setErr(new PrintStream(new Console(console)));
		simulator.run(configurationOptions);
		
	}
	
	public class Console extends OutputStream {
        private TextArea console;
 
        public Console(TextArea console) {
            this.console = console;
        }
 
        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }
 
        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }
}

package controller;

import java.io.File;
import java.io.PrintStream;

import app.ConfigurationOptions;
import controller.simulator.DataCenterWorkloadSimulator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
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

	@FXML
	private Menu MenuWorkload;

	@FXML
	private Menu MenuSimulation;

	@FXML
	private Menu MenuDEBB;
	
	@FXML
    private ComboBox<String> policyBox;
	@FXML
    private CheckBox isDebugCheckBox;

	private Object[] functionList;

	public void init(Object[] functionList) {
		DEBBPath.setText("example/xml/2020-03-15-02-43-44/PLMXML_PolytechPolyTestroom_20.xml");
		swfPath.setText("example/experiment1/workload.swf");
		this.functionList = functionList;
	}

	@FXML
	void DEBBChooseClick(ActionEvent event) {
		FileChooser DEBBChooser = new FileChooser();
		DEBBChooser.setTitle("Load DEBB Config");
		DEBBChooser.setInitialDirectory(new File("example/xml/"));
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
		swfChooser.setInitialDirectory(new File("example/"));
		File file = swfChooser.showOpenDialog(root.getScene().getWindow());
		if (file != null) {
			System.out.println(file.getPath());
			swfPath.setText(file.getPath());
		}
	}

	@FXML
	void runButtonClick(ActionEvent event) {
		System.out.println(DEBBPath.getText());
		boolean isDebug = false;
		if (isDebugCheckBox.isSelected()) {
			isDebug = true;
		}
		ConfigurationOptions configurationOptions = ConfigurationOptions.getConfiguration(DEBBPath.getText(),
				swfPath.getText(), policyBox.getValue(), isDebug);
		DataCenterWorkloadSimulator simulator = new DataCenterWorkloadSimulator();
		console.setText("");
		System.setOut(new PrintStream(new Console(console)));
		System.setErr(new PrintStream(new Console(console)));

		simulator.run(configurationOptions);

	}

	@FXML
	public void gotoWorkGenWindow(ActionEvent event) throws Exception {
		((AnchorPane) functionList[0]).setVisible(false);
		((AnchorPane) functionList[1]).setVisible(true);
	}

	@FXML
	public void gotoSimulationWindow(ActionEvent event) throws Exception {
//		((AnchorPane)functionList[0]).setVisible(false);
//		((AnchorPane)functionList[1]).setVisible(true);
	}

	@FXML
	public void gotoDebbConfigurator(ActionEvent event) throws Exception {
		
	}
}

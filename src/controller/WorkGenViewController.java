package controller;

import java.io.IOException;

import app.ConfigurationOptions;
import app.mainApplication;
import controller.workload.generator.WorkloadGenerator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import simulator.workload.generator.configuration.TaskCount;
import simulator.workload.generator.configuration.WorkloadConfiguration;
import simulator.workload.generator.configuration.types.ParameterAttributesDistributionType;

public class WorkGenViewController {

	@FXML
	private TextField workloadFilename;
	@FXML
	private TextField outputFolder;
	
    @FXML
    private TextField taskCountAvg;

    @FXML
    private TextField taskCountMin;

    @FXML
    private TextField taskCountMax;
    
    @FXML
    private ChoiceBox<?> taskCountDistribution;
    
	private Object[] functionList;

	public void init(Object[] functionList) {
		this.functionList = functionList;
		this.outputFolder.setText("example/workload");
		this.workloadFilename.setText("workload.swf");
	}

	@FXML
	void gotoSimulationWindow(ActionEvent event) {
		((AnchorPane) functionList[0]).setVisible(true);
		((AnchorPane) functionList[1]).setVisible(false);
	}

	@FXML
	void gotoWorkGenWindow(ActionEvent event) {

	}

	@FXML
	void generateButtonClick(ActionEvent event) {
		WorkloadGenerator generator = new WorkloadGenerator();
		ConfigurationOptions configurationOptions = ConfigurationOptions.getConfiguration(workloadFilename.getText(),
				outputFolder.getText());
		WorkloadConfiguration workload = new WorkloadConfiguration();
		TaskCount taskcount = new TaskCount();
		taskcount.setAvg(Double.parseDouble(taskCountAvg.toString()));
		taskcount.setDistribution(ParameterAttributesDistributionType.valueOf(taskCountDistribution.getValue().toString()));
		workload.setTaskCount(taskcount);
		generator.run(configurationOptions,workload);
	}

}

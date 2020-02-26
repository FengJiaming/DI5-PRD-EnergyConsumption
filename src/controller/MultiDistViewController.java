package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import simulator.workload.generator.configuration.Dist;
import simulator.workload.generator.configuration.MultiDistribution;
import simulator.workload.generator.configuration.types.ParameterAttributesDistributionType;

public class MultiDistViewController {

	@FXML
	private TextField Avg;

	@FXML
	private TextField Min;

	@FXML
	private Label MinLabel;

	@FXML
	private Label StdevLabel;

	@FXML
	private Label SeedLabel;

	@FXML
	private ComboBox<String> Distribution;

	@FXML
	private TextField Stdev;

	@FXML
	private Label DistLabel;

	@FXML
	private TextField Max;

	@FXML
	private AnchorPane root;

	@FXML
	private Label MaxLabel;

	@FXML
	private Label AvgLabel;

	@FXML
	private TextField Seed;

	@FXML
	private Button Save;

	@FXML
	private Button Delete;

	@FXML
	private Button Cancel;

	@FXML
	private Label DistributionRatioLabel;

	@FXML
	private TextField DistributionRatio;

	private WorkGenViewController workGenViewController;

	private String windowFlag;

	private String fieldName;
	
	private int indexDist;

	public void init(WorkGenViewController controller) {

		this.workGenViewController = controller;
		/*****************************/
		if (workGenViewController.taskCountDistribution.getValue() != null) {

		}

		this.MaxLabel.setDisable(true);
		this.Max.setDisable(true);
		this.MinLabel.setDisable(true);
		this.Min.setDisable(true);
		this.StdevLabel.setDisable(true);
		this.Stdev.setDisable(true);
		this.SeedLabel.setDisable(true);
		this.Seed.setDisable(true);

		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (event.getSource() instanceof ComboBox) {
					ComboBox<String> combobox = (ComboBox<String>) event.getSource();
					setComponentStatus(combobox);
				}
			}
		};
		this.Distribution.setOnAction(eventHandler);
	}

	public void loadDistributionData(int index) {
		this.indexDist = index;

		Dist dist = null;
		
		switch (this.fieldName) {
		case "TaskCount":
			dist = workGenViewController.taskCountListMD.getDist(index);
			break;
		case "TaskLength":
			dist = workGenViewController.taskLengthListMD.getDist(index);
			break;
		case "JobPackageLength":
			dist = workGenViewController.jobPackLenListMD.getDist(index);
			break;
		case "JobInterval":
			dist = workGenViewController.jobIntervalListMD.getDist(index);
			break;
		case "CpuCount":
			dist = workGenViewController.cpucntListMD.getDist(index);
			break;
		case "Memory":
			dist = workGenViewController.memoryListMD.getDist(index);
			break;
		}
		Avg.setText(String.valueOf(dist.getAvg()));
		Stdev.setText(String.valueOf(dist.getStdev()));
		Max.setText(String.valueOf(dist.getMax()));
		Min.setText(String.valueOf(dist.getMin()));
		Distribution.setValue(dist.getDistribution().toString());
		Seed.setText(String.valueOf(dist.getSeed()));
		DistributionRatio.setText(dist.getContent());

		setComponentStatus(Distribution);
	}

	private void setComponentStatus(ComboBox<String> distribution) {
		switch (Distribution.getValue().toString()) {
		case "constant":
			AvgLabel.setDisable(false);
			Avg.setDisable(false);
			StdevLabel.setDisable(true);
			Stdev.setDisable(true);
			MaxLabel.setDisable(true);
			Max.setDisable(true);
			MinLabel.setDisable(true);
			Min.setDisable(true);
			SeedLabel.setDisable(true);
			Seed.setDisable(true);
			break;
		case "normal":
			AvgLabel.setDisable(false);
			Avg.setDisable(false);
			StdevLabel.setDisable(false);
			Stdev.setDisable(false);
			MaxLabel.setDisable(false);
			Max.setDisable(false);
			MinLabel.setDisable(false);
			Min.setDisable(false);
			SeedLabel.setDisable(false);
			Seed.setDisable(false);
			break;
		case "poisson":
			AvgLabel.setDisable(false);
			Avg.setDisable(false);
			StdevLabel.setDisable(true);
			Stdev.setDisable(true);
			MaxLabel.setDisable(false);
			Max.setDisable(false);
			MinLabel.setDisable(false);
			Min.setDisable(false);
			SeedLabel.setDisable(false);
			Seed.setDisable(false);
			break;
		case "uniform":
			AvgLabel.setDisable(true);
			Avg.setDisable(true);
			StdevLabel.setDisable(true);
			Stdev.setDisable(true);
			MaxLabel.setDisable(false);
			Max.setDisable(false);
			MinLabel.setDisable(false);
			Min.setDisable(false);
			SeedLabel.setDisable(false);
			Seed.setDisable(false);
			break;
		}
		
	}

	@FXML
	public void saveButtonClick() {
		Dist dist = new Dist();

		dist.setContent(DistributionRatio.getText());
		dist.setDistribution(ParameterAttributesDistributionType.valueOf(Distribution.getValue().toString()));
		dist.setAvg(Double.parseDouble(Avg.getText()));
		if (this.Stdev.getText() != null && !this.Stdev.getText().equals(""))
			dist.setStdev(Double.parseDouble(this.Stdev.getText()));
		if (this.Max.getText() != null && !this.Max.getText().equals(""))
			dist.setMax(Double.parseDouble(this.Max.getText()));
		if (this.Min.getText() != null && !this.Min.getText().equals(""))
			dist.setMin(Double.parseDouble(this.Min.getText()));
		if (this.Seed.getText() != null && !this.Seed.getText().equals(""))
			dist.setSeed(Long.parseLong(this.Seed.getText()));
		String valueString = "Dist-" + DistributionRatio.getText();
		
		switch (this.fieldName) {
		case "TaskCount":
			if ("ADD".equals(this.windowFlag)) {
			
				workGenViewController.taskCountListMD.addDist(dist);
				workGenViewController.taskCountMDBox.getItems().add(valueString);
				workGenViewController.taskCountMDBox.setValue(valueString);

			} else if ("EDIT".equals(this.windowFlag)) {

				workGenViewController.taskCountListMD.setDist(this.indexDist, dist);
			}
			workGenViewController.taskCountMDEdit.setDisable(false);
			workGenViewController.taskCountMDDelete.setDisable(false);
			break;
		case "TaskLength":
			if ("ADD".equals(this.windowFlag)) {
				
				workGenViewController.taskLengthListMD.addDist(dist);
				workGenViewController.taskLengthMDBox.getItems().add(valueString);
				workGenViewController.taskLengthMDBox.setValue(valueString);

			} else if ("EDIT".equals(this.windowFlag)) {

				workGenViewController.taskLengthListMD.setDist(this.indexDist, dist);
			}
			workGenViewController.taskLengthMDEdit.setDisable(false);
			workGenViewController.taskLengthMDDelete.setDisable(false);
			break;
		case "JobPackageLength":
			if ("ADD".equals(this.windowFlag)) {
				
				workGenViewController.jobPackLenListMD.addDist(dist);
				workGenViewController.jobPackLenMDBox.getItems().add(valueString);
				workGenViewController.jobPackLenMDBox.setValue(valueString);

			} else if ("EDIT".equals(this.windowFlag)) {

				workGenViewController.jobPackLenListMD.setDist(this.indexDist, dist);
			}
			workGenViewController.jobPackLenMDEdit.setDisable(false);
			workGenViewController.jobPackLenMDDelete.setDisable(false);
			break;
		case "JobInterval":
			if ("ADD".equals(this.windowFlag)) {
				
				workGenViewController.jobIntervalListMD.addDist(dist);
				workGenViewController.jobIntervalMDBox.getItems().add(valueString);
				workGenViewController.jobIntervalMDBox.setValue(valueString);

			} else if ("EDIT".equals(this.windowFlag)) {

				workGenViewController.jobIntervalListMD.setDist(this.indexDist, dist);
			}
			workGenViewController.jobIntervalMDEdit.setDisable(false);
			workGenViewController.jobIntervalMDDelete.setDisable(false);
			break;
		case "CpuCount":
			if ("ADD".equals(this.windowFlag)) {
				
				workGenViewController.cpucntListMD.addDist(dist);
				workGenViewController.cpucntMDBox.getItems().add(valueString);
				workGenViewController.cpucntMDBox.setValue(valueString);

			} else if ("EDIT".equals(this.windowFlag)) {

				workGenViewController.cpucntListMD.setDist(this.indexDist, dist);
			}
			workGenViewController.cpucntMDEdit.setDisable(false);
			workGenViewController.cpucntMDDelete.setDisable(false);
			break;
		case "Memory":
			if ("ADD".equals(this.windowFlag)) {
				
				workGenViewController.memoryListMD.addDist(dist);
				workGenViewController.memoryMDBox.getItems().add(valueString);
				workGenViewController.memoryMDBox.setValue(valueString);

			} else if ("EDIT".equals(this.windowFlag)) {

				workGenViewController.memoryListMD.setDist(this.indexDist, dist);
			}
			workGenViewController.memoryMDEdit.setDisable(false);
			workGenViewController.memoryMDDelete.setDisable(false);
			break;
		}


		Stage stage = (Stage) Save.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void deleteButtonClick() {
		
		switch (this.fieldName) {
			case "TaskCount":
				workGenViewController.taskCountListMD.removeDistAt(workGenViewController.getTaskCountMDBoxIndex());
				workGenViewController.taskCountMDBox.getItems().remove(workGenViewController.getTaskCountMDBoxIndex());

				workGenViewController.checkTaskCountMDBoxStatus();
				break;
			case "TaskLength":
				workGenViewController.taskLengthListMD.removeDistAt(workGenViewController.getTaskLengthMDBoxIndex());
				workGenViewController.taskLengthMDBox.getItems().remove(workGenViewController.getTaskLengthMDBoxIndex());

				workGenViewController.checkTaskLengthMDBoxStatus();
				break;
			case "JobPackageLength":
				workGenViewController.jobPackLenListMD.removeDistAt(workGenViewController.getJobPackLenMDBoxIndex());
				workGenViewController.jobPackLenMDBox.getItems().remove(workGenViewController.getJobPackLenMDBoxIndex());

				workGenViewController.checkJobPackLenMDBoxStatus();
				break;
			case "JobInterval":
				workGenViewController.jobIntervalListMD.removeDistAt(workGenViewController.getJobIntervalMDBoxIndex());
				workGenViewController.jobIntervalMDBox.getItems().remove(workGenViewController.getJobIntervalMDBoxIndex());

				workGenViewController.checkJobIntervalMDBoxStatus();
				break;
			case "CpuCount":
				workGenViewController.cpucntListMD.removeDistAt(workGenViewController.getCpucntMDBoxIndex());
				workGenViewController.cpucntMDBox.getItems().remove(workGenViewController.getCpucntMDBoxIndex());

				workGenViewController.checkCpucntMDBoxStatus();
				break;
			case "Memory":
				workGenViewController.memoryListMD.removeDistAt(workGenViewController.getMemoryMDBoxIndex());
				workGenViewController.memoryMDBox.getItems().remove(workGenViewController.getMemoryMDBoxIndex());

				workGenViewController.checkMemoryMDBoxStatus();
				break;
		}
		
		Stage stage = (Stage) Save.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void cancelButtonClick() {
		Stage stage = (Stage) Cancel.getScene().getWindow();
		stage.close();
	}

	public void setFlag(String flag, String field) {
		this.windowFlag = flag;
		this.fieldName = field;
		if ("ADD".equals(windowFlag)) {
			Delete.setDisable(true);
		} else if ("EDIT".equals(windowFlag)) {
			Delete.setDisable(false);
		}
	}

}

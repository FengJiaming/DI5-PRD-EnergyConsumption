package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import simulator.workload.generator.configuration.Dist;
import simulator.workload.generator.configuration.PeriodicValidValues;
import simulator.workload.generator.configuration.types.ParameterAttributesDistributionType;

public class PeriodDistViewController {

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
	    private TextField PeriodBeginTime;
	    
	    @FXML
	    private TextField PeriodEndTime;
	    
		private WorkGenViewController workGenViewController;

		private String windowFlag;

		private String fieldName;
		
		private int indexDist;
	    
		public void init(WorkGenViewController controller) {
			
			this.workGenViewController = controller;
			/*****************************/
			if(workGenViewController.taskCountDistribution.getValue()!=null) {
				
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
			if ("ADD".equals(windowFlag)) {
				Delete.setDisable(true);
			} else if ("EDIT".equals(windowFlag)) {
				Delete.setDisable(false);
			}
			
			PeriodicValidValues vPeriodicValidValues = null;
			switch (this.fieldName) {
			case "TaskCount":
				vPeriodicValidValues = workGenViewController.taskCountListRP.getPeriodicValidValues(index);
				break;
			case "TaskLength":
				vPeriodicValidValues = workGenViewController.taskLengthListRP.getPeriodicValidValues(index);
				break;
			case "JobPackageLength":
				vPeriodicValidValues = workGenViewController.jobPackLenListRP.getPeriodicValidValues(index);
				break;
			case "JobInterval":
				vPeriodicValidValues = workGenViewController.jobIntervalListRP.getPeriodicValidValues(index);
				break;
			case "CpuCount":
				vPeriodicValidValues = workGenViewController.cpucntListRP.getPeriodicValidValues(index);
				break;
			case "Memory":
				vPeriodicValidValues = workGenViewController.memoryListRP.getPeriodicValidValues(index);
				break;
			}
			
			Avg.setText(String.valueOf(vPeriodicValidValues.getAvg()));
			Stdev.setText(String.valueOf(vPeriodicValidValues.getStdev()));
			Max.setText(String.valueOf(vPeriodicValidValues.getMax()));
			Min.setText(String.valueOf(vPeriodicValidValues.getMin()));
			Distribution.setValue(vPeriodicValidValues.getDistribution().toString());
			Seed.setText(String.valueOf(vPeriodicValidValues.getSeed()));
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			PeriodBeginTime.setText(String.valueOf(simpleDateFormat.format(vPeriodicValidValues.getBeginValidTime())));
			PeriodEndTime.setText(String.valueOf(simpleDateFormat.format(vPeriodicValidValues.getEndValidTime())));
			
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
			PeriodicValidValues vPeriodicValidValues = new PeriodicValidValues();

			vPeriodicValidValues.setDistribution(ParameterAttributesDistributionType.valueOf(Distribution.getValue().toString()));
			vPeriodicValidValues.setAvg(Double.parseDouble(Avg.getText()));
			if (this.Stdev.getText() != null && !this.Stdev.getText().equals(""))
				vPeriodicValidValues.setStdev(Double.parseDouble(this.Stdev.getText()));
			if (this.Max.getText() != null && !this.Max.getText().equals(""))
				vPeriodicValidValues.setMax(Double.parseDouble(this.Max.getText()));
			if (this.Min.getText() != null && !this.Min.getText().equals(""))
				vPeriodicValidValues.setMin(Double.parseDouble(this.Min.getText()));
			if (this.Seed.getText() != null && !this.Seed.getText().equals(""))
				vPeriodicValidValues.setSeed(Long.parseLong(this.Seed.getText()));
			if (this.PeriodBeginTime.getText() != null && !this.PeriodBeginTime.getText().equals("")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
				Date beginValidTime = null;
				try {
					beginValidTime = simpleDateFormat.parse(this.PeriodBeginTime.getText());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				vPeriodicValidValues.setBeginValidTime(beginValidTime);
			}
			if (this.PeriodEndTime.getText() != null && !this.PeriodEndTime.getText().equals("")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
				Date endValidTime = null;
				try {
					endValidTime = simpleDateFormat.parse(this.PeriodEndTime.getText());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				vPeriodicValidValues.setEndValidTime(endValidTime);
			}
			String valueString = this.PeriodBeginTime.getText().split("_")[1] + "-" + this.PeriodEndTime.getText().split("_")[1];
			switch (this.fieldName) {
			case "TaskCount":
				if ("ADD".equals(this.windowFlag)) {

					workGenViewController.taskCountListRP.addPeriodicValidValues(vPeriodicValidValues);

					workGenViewController.taskCountPeriodBox.getItems().add(valueString);
					workGenViewController.taskCountPeriodBox.setValue(valueString);

				} else if ("EDIT".equals(this.windowFlag)) {

					workGenViewController.taskCountListRP.setPeriodicValidValues(this.indexDist, vPeriodicValidValues);
				}
				workGenViewController.taskCountPeriodEdit.setDisable(false);
				workGenViewController.taskCountPeriodDelete.setDisable(false);
				break;
			case "TaskLength":
				if ("ADD".equals(this.windowFlag)) {

					workGenViewController.taskLengthListRP.addPeriodicValidValues(vPeriodicValidValues);

					workGenViewController.taskLengthPeriodBox.getItems().add(valueString);
					workGenViewController.taskLengthPeriodBox.setValue(valueString);

				} else if ("EDIT".equals(this.windowFlag)) {

					workGenViewController.taskLengthListRP.setPeriodicValidValues(this.indexDist, vPeriodicValidValues);
				}
				workGenViewController.taskLengthPeriodEdit.setDisable(false);
				workGenViewController.taskLengthPeriodDelete.setDisable(false);
				break;
			case "JobPackageLength":
				if ("ADD".equals(this.windowFlag)) {

					workGenViewController.jobPackLenListRP.addPeriodicValidValues(vPeriodicValidValues);

					workGenViewController.jobPackLenPeriodBox.getItems().add(valueString);
					workGenViewController.jobPackLenPeriodBox.setValue(valueString);

				} else if ("EDIT".equals(this.windowFlag)) {

					workGenViewController.jobPackLenListRP.setPeriodicValidValues(this.indexDist, vPeriodicValidValues);
				}
				workGenViewController.jobPackLenPeriodEdit.setDisable(false);
				workGenViewController.jobPackLenPeriodDelete.setDisable(false);
				break;
			case "JobInterval":
				if ("ADD".equals(this.windowFlag)) {

					workGenViewController.jobIntervalListRP.addPeriodicValidValues(vPeriodicValidValues);

					workGenViewController.jobIntervalPeriodBox.getItems().add(valueString);
					workGenViewController.jobIntervalPeriodBox.setValue(valueString);

				} else if ("EDIT".equals(this.windowFlag)) {

					workGenViewController.jobIntervalListRP.setPeriodicValidValues(this.indexDist, vPeriodicValidValues);
				}
				workGenViewController.jobIntervalPeriodEdit.setDisable(false);
				workGenViewController.jobIntervalPeriodDelete.setDisable(false);
				break;
			case "CpuCount":
				if ("ADD".equals(this.windowFlag)) {

					workGenViewController.cpucntListRP.addPeriodicValidValues(vPeriodicValidValues);

					workGenViewController.cpucntPeriodBox.getItems().add(valueString);
					workGenViewController.cpucntPeriodBox.setValue(valueString);

				} else if ("EDIT".equals(this.windowFlag)) {

					workGenViewController.cpucntListRP.setPeriodicValidValues(this.indexDist, vPeriodicValidValues);
				}
				workGenViewController.cpucntPeriodEdit.setDisable(false);
				workGenViewController.cpucntPeriodDelete.setDisable(false);
				break;
			case "Memory":
				if ("ADD".equals(this.windowFlag)) {

					workGenViewController.memoryListRP.addPeriodicValidValues(vPeriodicValidValues);

					workGenViewController.memoryPeriodBox.getItems().add(valueString);
					workGenViewController.memoryPeriodBox.setValue(valueString);

				} else if ("EDIT".equals(this.windowFlag)) {

					workGenViewController.memoryListRP.setPeriodicValidValues(this.indexDist, vPeriodicValidValues);
				}
				workGenViewController.memoryPeriodEdit.setDisable(false);
				workGenViewController.memoryPeriodDelete.setDisable(false);
				break;
			}
			Stage stage = (Stage) Save.getScene().getWindow();
			stage.close();
		}
		
		@FXML
		public void deleteButtonClick() {
			
			switch (this.fieldName) {
			case "TaskCount":
				workGenViewController.taskCountListRP.removePeriodicValidValuesAt(workGenViewController.getTaskLengthPeriodBoxIndex());
				workGenViewController.taskCountPeriodBox.getItems().remove(workGenViewController.getTaskLengthPeriodBoxIndex());

				workGenViewController.checkTaskCountPeriodBoxStatus();
				break;
			case "TaskLength":
				workGenViewController.taskLengthListRP.removePeriodicValidValuesAt(workGenViewController.getTaskLengthPeriodBoxIndex());
				workGenViewController.taskLengthPeriodBox.getItems().remove(workGenViewController.getTaskLengthPeriodBoxIndex());

				workGenViewController.checkTaskLengthPeriodBoxStatus();
				break;
			case "JobPackageLength":
				workGenViewController.jobPackLenListRP.removePeriodicValidValuesAt(workGenViewController.getTaskLengthPeriodBoxIndex());
				workGenViewController.jobPackLenPeriodBox.getItems().remove(workGenViewController.getTaskLengthPeriodBoxIndex());

				workGenViewController.checkJobPackLenPeriodBoxStatus();
				break;
			case "JobInterval":
				workGenViewController.jobIntervalListRP.removePeriodicValidValuesAt(workGenViewController.getTaskLengthPeriodBoxIndex());
				workGenViewController.jobIntervalPeriodBox.getItems().remove(workGenViewController.getTaskLengthPeriodBoxIndex());

				workGenViewController.checkJobIntervalPeriodBoxStatus();
				break;
			case "CpuCount":
				workGenViewController.cpucntListRP.removePeriodicValidValuesAt(workGenViewController.getTaskLengthPeriodBoxIndex());
				workGenViewController.cpucntPeriodBox.getItems().remove(workGenViewController.getTaskLengthPeriodBoxIndex());

				workGenViewController.checkCpucntPeriodBoxStatus();
				break;
			case "Memory":
				workGenViewController.memoryListRP.removePeriodicValidValuesAt(workGenViewController.getTaskLengthPeriodBoxIndex());
				workGenViewController.memoryPeriodBox.getItems().remove(workGenViewController.getTaskLengthPeriodBoxIndex());

				workGenViewController.checkMemoryPeriodBoxStatus();
				break;
		}
			Stage stage = (Stage) Save.getScene().getWindow();
			stage.close();
		}
		
		public void cancelButtonClick() {
		    Stage stage = (Stage)Cancel.getScene().getWindow();
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

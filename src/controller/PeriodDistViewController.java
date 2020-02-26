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

			PeriodicValidValues vPeriodicValidValues = workGenViewController.taskCountListRP.getPeriodicValidValues(index);
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
			if ("ADD".equals(this.windowFlag)) {

				workGenViewController.taskCountListRP.addPeriodicValidValues(vPeriodicValidValues);

//				String valueString = "Period-" + vPeriodicValidValues.getBeginValidTime().getHours() + ":" + 
//				vPeriodicValidValues.getBeginValidTime().getMinutes() + "-" + 
//						vPeriodicValidValues.getEndValidTime().getHours() + ":" + 
//				vPeriodicValidValues.getEndValidTime().getMinutes();
				String valueString = this.PeriodBeginTime.getText().split("_")[1] + "-" + this.PeriodEndTime.getText().split("_")[1];
				workGenViewController.taskCountPeriodBox.getItems().add(valueString);
				workGenViewController.taskCountPeriodBox.setValue(valueString);

			} else if ("EDIT".equals(this.windowFlag)) {

				workGenViewController.taskCountListRP.setPeriodicValidValues(this.indexDist, vPeriodicValidValues);
			}

			workGenViewController.taskCountPeriodEdit.setDisable(false);
			workGenViewController.taskCountPeriodDelete.setDisable(false);

			Stage stage = (Stage) Save.getScene().getWindow();
			stage.close();
		}
		
		@FXML
		public void deleteButtonClick() {
			workGenViewController.taskCountListRP.removePeriodicValidValuesAt(workGenViewController.getPeriodBoxIndex());
			workGenViewController.taskCountPeriodBox.getItems().remove(workGenViewController.getPeriodBoxIndex());

			workGenViewController.checkTaskCountPeriodBoxStatus();
			
			Stage stage = (Stage) Save.getScene().getWindow();
			stage.close();
		}
		
		public void cancelButtonClick() {
		    Stage stage = (Stage)Cancel.getScene().getWindow();
		    stage.close();
		}
		
		public void setFlag(String flag) {
			this.windowFlag = flag;
			if ("ADD".equals(windowFlag)) {
				Delete.setDisable(true);
			} else if ("EDIT".equals(windowFlag)) {
				Delete.setDisable(false);
			}
		}
	}

package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.ConfigurationOptions;
import app.MainApplication;
import controller.workload.generator.Dependency;
import controller.workload.generator.WorkloadGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simulator.workload.generator.configuration.MultiDistribution;
import simulator.workload.generator.configuration.RandParamsSequence;
import simulator.workload.generator.configuration.ComputingResourceHostParameter;
import simulator.workload.generator.configuration.JobCount;
import simulator.workload.generator.configuration.JobInterval;
import simulator.workload.generator.configuration.JobPackageLength;
import simulator.workload.generator.configuration.TaskCount;
import simulator.workload.generator.configuration.TaskLength;
import simulator.workload.generator.configuration.Value;
import simulator.workload.generator.configuration.WorkloadConfiguration;
import simulator.workload.generator.configuration.WorkloadConfigurationChoice;
import simulator.workload.generator.configuration.types.ParameterAttributesDistributionType;

public class WorkGenViewController {

	@FXML
	private AnchorPane root;
	
	@FXML
	private TextField workloadFilename;
	@FXML
	private TextField outputFolder;
	@FXML
	private TextField simulationStartTime;
	@FXML
	private TextField jobCount;

	/** TaskCount **/
	@FXML
	protected CheckBox taskCountRefCheck;
	@FXML
	protected Label taskCountRefLabel;
	@FXML
	protected ComboBox<String> taskCountRefBox;

	@FXML
	protected Label taskCountExprLabel;
	@FXML
	protected TextField taskCountExprText;

	@FXML
	protected Label taskCountDistLabel;
	@FXML
	protected ComboBox<?> taskCountDistribution;
	@FXML
	protected Label taskCountAvgLabel;
	@FXML
	protected TextField taskCountAvg;

	@FXML
	protected Label taskCountStdevLabel;
	@FXML
	protected TextField taskCountStdev;

	@FXML
	protected Label taskCountMaxLabel;
	@FXML
	protected TextField taskCountMax;
	@FXML
	protected Label taskCountMinLabel;
	@FXML
	protected TextField taskCountMin;
	@FXML
	protected Label taskCountSeedLabel;
	@FXML
	protected TextField taskCountSeed;
	@FXML
	protected CheckBox taskCountMDCheck;
	@FXML
	protected ComboBox<String> taskCountMDBox;
	@FXML
	protected Button taskCountMDAdd;
	@FXML
	protected Button taskCountMDEdit;
	@FXML
	protected Button taskCountMDDelete;
	@FXML
	protected CheckBox taskCountPeriodCheck;
	@FXML
	protected ComboBox<String> taskCountPeriodBox;
	@FXML
	protected Button taskCountPeriodAdd;
	@FXML
	protected Button taskCountPeriodEdit;
	@FXML
	protected Button taskCountPeriodDelete;
	
	public MultiDistribution taskCountListMD;
	
	public RandParamsSequence taskCountListRP;
	/** --------------------------------------------------------- */

	/** TaskLength */
	@FXML
	protected CheckBox taskLengthRefCheck;
	@FXML
	protected Label taskLengthRefLabel;
	@FXML
	protected ComboBox<String> taskLengthRefBox;

	@FXML
	protected Label taskLengthExprLabel;
	@FXML
	protected TextField taskLengthExprText;

	@FXML
	protected Label taskLengthDistLabel;
	@FXML
	protected ComboBox<?> taskLengthDistribution;
	@FXML
	protected Label taskLengthAvgLabel;
	@FXML
	protected TextField taskLengthAvg;

	@FXML
	protected Label taskLengthStdevLabel;
	@FXML
	protected TextField taskLengthStdev;

	@FXML
	protected Label taskLengthMaxLabel;
	@FXML
	protected TextField taskLengthMax;
	@FXML
	protected Label taskLengthMinLabel;
	@FXML
	protected TextField taskLengthMin;
	@FXML
	protected Label taskLengthSeedLabel;
	@FXML
	protected TextField taskLengthSeed;
	@FXML
	protected CheckBox taskLengthMDCheck;
	@FXML
	protected ComboBox<String> taskLengthMDBox;
	@FXML
	protected Button taskLengthMDAdd;
	@FXML
	protected Button taskLengthMDEdit;
	@FXML
	protected Button taskLengthMDDelete;
	@FXML
	protected CheckBox taskLengthPeriodCheck;
	@FXML
	protected ComboBox<String> taskLengthPeriodBox;
	@FXML
	protected Button taskLengthPeriodAdd;
	@FXML
	protected Button taskLengthPeriodEdit;
	@FXML
	protected Button taskLengthPeriodDelete;
	
	public MultiDistribution taskLengthListMD;
	
	public RandParamsSequence taskLengthListRP;
	/*** ---------------------------------------------------- */
	/** JobPackageLength */
	@FXML
	protected CheckBox jobPackLenRefCheck;
	@FXML
	protected Label jobPackLenRefLabel;
	@FXML
	protected ComboBox<String> jobPackLenRefBox;

	@FXML
	protected Label jobPackLenExprLabel;
	@FXML
	protected TextField jobPackLenExprText;

	@FXML
	protected Label jobPackLenDistLabel;
	@FXML
	protected ComboBox<?> jobPackLenDistribution;
	@FXML
	protected Label jobPackLenAvgLabel;
	@FXML
	protected TextField jobPackLenAvg;

	@FXML
	protected Label jobPackLenStdevLabel;
	@FXML
	protected TextField jobPackLenStdev;

	@FXML
	protected Label jobPackLenMaxLabel;
	@FXML
	protected TextField jobPackLenMax;
	@FXML
	protected Label jobPackLenMinLabel;
	@FXML
	protected TextField jobPackLenMin;
	@FXML
	protected Label jobPackLenSeedLabel;
	@FXML
	protected TextField jobPackLenSeed;
	@FXML
	protected CheckBox jobPackLenMDCheck;
	@FXML
	protected ComboBox<String> jobPackLenMDBox;
	@FXML
	protected Button jobPackLenMDAdd;
	@FXML
	protected Button jobPackLenMDEdit;
	@FXML
	protected Button jobPackLenMDDelete;
	@FXML
	protected CheckBox jobPackLenPeriodCheck;
	@FXML
	protected ComboBox<String> jobPackLenPeriodBox;
	@FXML
	protected Button jobPackLenPeriodAdd;
	@FXML
	protected Button jobPackLenPeriodEdit;
	@FXML
	protected Button jobPackLenPeriodDelete;
	
	public MultiDistribution jobPackLenListMD;
	
	public RandParamsSequence jobPackLenListRP;
	/** ------------------------------------------------- */
	/** JoaInterval */
	@FXML
	protected CheckBox jobIntervalRefCheck;
	@FXML
	protected Label jobIntervalRefLabel;
	@FXML
	protected ComboBox<String> jobIntervalRefBox;

	@FXML
	protected Label jobIntervalExprLabel;
	@FXML
	protected TextField jobIntervalExprText;

	@FXML
	protected Label jobIntervalDistLabel;
	@FXML
	protected ComboBox<?> jobIntervalDistribution;
	@FXML
	protected Label jobIntervalAvgLabel;
	@FXML
	protected TextField jobIntervalAvg;

	@FXML
	protected Label jobIntervalStdevLabel;
	@FXML
	protected TextField jobIntervalStdev;

	@FXML
	protected Label jobIntervalMaxLabel;
	@FXML
	protected TextField jobIntervalMax;
	@FXML
	protected Label jobIntervalMinLabel;
	@FXML
	protected TextField jobIntervalMin;
	@FXML
	protected Label jobIntervalSeedLabel;
	@FXML
	protected TextField jobIntervalSeed;
	@FXML
	protected CheckBox jobIntervalMDCheck;
	@FXML
	protected ComboBox<String> jobIntervalMDBox;
	@FXML
	protected Button jobIntervalMDAdd;
	@FXML
	protected Button jobIntervalMDEdit;
	@FXML
	protected Button jobIntervalMDDelete;
	@FXML
	protected CheckBox jobIntervalPeriodCheck;
	@FXML
	protected ComboBox<String> jobIntervalPeriodBox;
	@FXML
	protected Button jobIntervalPeriodAdd;
	@FXML
	protected Button jobIntervalPeriodEdit;
	@FXML
	protected Button jobIntervalPeriodDelete;
	
	public MultiDistribution jobIntervalListMD;
	
	public RandParamsSequence jobIntervalListRP;
	/** ------------------------------------------------- */
	/** CpuCount */
	@FXML
	protected CheckBox cpucntRefCheck;
	@FXML
	protected Label cpucntRefLabel;
	@FXML
	protected ComboBox<String> cpucntRefBox;

	@FXML
	protected Label cpucntExprLabel;
	@FXML
	protected TextField cpucntExprText;

	@FXML
	protected Label cpucntDistLabel;
	@FXML
	protected ComboBox<?> cpucntDistribution;
	@FXML
	protected Label cpucntAvgLabel;
	@FXML
	protected TextField cpucntAvg;

	@FXML
	protected Label cpucntStdevLabel;
	@FXML
	protected TextField cpucntStdev;

	@FXML
	protected Label cpucntMaxLabel;
	@FXML
	protected TextField cpucntMax;
	@FXML
	protected Label cpucntMinLabel;
	@FXML
	protected TextField cpucntMin;
	@FXML
	protected Label cpucntSeedLabel;
	@FXML
	protected TextField cpucntSeed;
	@FXML
	protected CheckBox cpucntMDCheck;
	@FXML
	protected ComboBox<String> cpucntMDBox;
	@FXML
	protected Button cpucntMDAdd;
	@FXML
	protected Button cpucntMDEdit;
	@FXML
	protected Button cpucntMDDelete;
	@FXML
	protected CheckBox cpucntPeriodCheck;
	@FXML
	protected ComboBox<String> cpucntPeriodBox;
	@FXML
	protected Button cpucntPeriodAdd;
	@FXML
	protected Button cpucntPeriodEdit;
	@FXML
	protected Button cpucntPeriodDelete;
	
	public MultiDistribution cpucntListMD;
	
	public RandParamsSequence cpucntListRP;
	/** ------------------------------------------------- */
	/** Memory */
	@FXML
	protected CheckBox memoryRefCheck;
	@FXML
	protected Label memoryRefLabel;
	@FXML
	protected ComboBox<String> memoryRefBox;

	@FXML
	protected Label memoryExprLabel;
	@FXML
	protected TextField memoryExprText;

	@FXML
	protected Label memoryDistLabel;
	@FXML
	protected ComboBox<?> memoryDistribution;
	@FXML
	protected Label memoryAvgLabel;
	@FXML
	protected TextField memoryAvg;

	@FXML
	protected Label memoryStdevLabel;
	@FXML
	protected TextField memoryStdev;

	@FXML
	protected Label memoryMaxLabel;
	@FXML
	protected TextField memoryMax;
	@FXML
	protected Label memoryMinLabel;
	@FXML
	protected TextField memoryMin;
	@FXML
	protected Label memorySeedLabel;
	@FXML
	protected TextField memorySeed;
	@FXML
	protected CheckBox memoryMDCheck;
	@FXML
	protected ComboBox<String> memoryMDBox;
	@FXML
	protected Button memoryMDAdd;
	@FXML
	protected Button memoryMDEdit;
	@FXML
	protected Button memoryMDDelete;
	@FXML
	protected CheckBox memoryPeriodCheck;
	@FXML
	protected ComboBox<String> memoryPeriodBox;
	@FXML
	protected Button memoryPeriodAdd;
	@FXML
	protected Button memoryPeriodEdit;
	@FXML
	protected Button memoryPeriodDelete;
	
	public MultiDistribution memoryListMD;
	
	public RandParamsSequence memoryListRP;
	/** ------------------------------------------------- */
	
	private MultiDistViewController multiDistController;
	
	private PeriodDistViewController periodDistController;
	
	private Object[] functionList;

	public void init(Object[] functionList) {
		this.functionList = functionList;
		
		this.taskCountListMD = new MultiDistribution();
		this.taskCountListRP = new RandParamsSequence();
		
		this.taskLengthListMD = new MultiDistribution();
		this.taskLengthListRP = new RandParamsSequence();
		
		this.jobPackLenListMD = new MultiDistribution();
		this.jobPackLenListRP = new RandParamsSequence();
		
		this.jobIntervalListMD = new MultiDistribution();
		this.jobIntervalListRP = new RandParamsSequence();
		
		this.cpucntListMD = new MultiDistribution();
		this.cpucntListRP = new RandParamsSequence();
		
		this.memoryListMD = new MultiDistribution();
		this.memoryListRP = new RandParamsSequence();
		
		/** for debug */
		this.outputFolder.setText("example/workload");
		this.workloadFilename.setText("workload.swf");

		this.simulationStartTime.setText("2009-01-15_10:00:00");
		this.jobCount.setText("100");
		this.taskCountAvg.setText("1");
		
		this.taskLengthAvg.setText("14400");
		this.taskLengthMax.setText("18000");
		this.taskLengthMin.setText("10800");
		this.taskLengthStdev.setText("3600.0");
		
		this.jobPackLenAvg.setText("1.0");
		
		this.jobIntervalAvg.setText("50");
		this.jobIntervalMax.setText("100");
		this.jobIntervalMin.setText("0.0");
		this.jobIntervalSeed.setText("21");
		
		this.cpucntAvg.setText("1");
		this.memoryAvg.setText("1024");
		
		initComponentStatus();
		
		initRefCombobox();
//		InterfaceEventHandler eventHandler = new InterfaceEventHandler();
		
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (event.getSource() instanceof CheckBox) {
					CheckBox checkbox = (CheckBox) event.getSource();
					switch (checkbox.getId()) {
					case "taskCountRefCheck":
						if (checkbox.isSelected()) {
							taskCountRefLabel.setDisable(false);
							taskCountRefBox.setDisable(false);
							taskCountExprLabel.setDisable(false);
							taskCountExprText.setDisable(false);

							taskCountDistLabel.setDisable(true);
							taskCountDistribution.setDisable(true);
							taskCountAvgLabel.setDisable(true);
							taskCountAvg.setDisable(true);
							taskCountStdevLabel.setDisable(true);
							taskCountStdev.setDisable(true);
							taskCountMaxLabel.setDisable(true);
							taskCountMax.setDisable(true);
							taskCountMinLabel.setDisable(true);
							taskCountMin.setDisable(true);
							taskCountSeedLabel.setDisable(true);
							taskCountSeed.setDisable(true);

							taskCountMDCheck.setDisable(true);
							taskCountMDBox.setDisable(true);
							taskCountMDAdd.setDisable(true);
							taskCountMDEdit.setDisable(true);
							taskCountMDDelete.setDisable(true);
							
							taskCountPeriodCheck.setDisable(true);
							taskCountPeriodBox.setDisable(true);
							taskCountPeriodAdd.setDisable(true);
							taskCountPeriodEdit.setDisable(true);
							taskCountPeriodDelete.setDisable(true);
						} else {
							
							changeTaskCountNodeStatus();
							
							taskCountRefBox.setDisable(true);
							taskCountExprLabel.setDisable(true);
							taskCountExprText.setDisable(true);
							
							taskCountDistLabel.setDisable(false);
							taskCountDistribution.setDisable(false);

							taskCountMDCheck.setDisable(false);
							taskCountMDBox.setDisable(true);
							taskCountMDAdd.setDisable(true);
							taskCountMDEdit.setDisable(true);
							taskCountMDDelete.setDisable(true);

							taskCountPeriodCheck.setDisable(false);
							taskCountPeriodBox.setDisable(true);
							taskCountPeriodAdd.setDisable(true);
							taskCountPeriodEdit.setDisable(true);
							taskCountPeriodDelete.setDisable(true);
						}
						break;
					case "taskCountMDCheck":
						if (checkbox.isSelected()) {
							taskCountRefCheck.setDisable(true);
							taskCountRefLabel.setDisable(true);
							taskCountRefBox.setDisable(true);
							taskCountExprLabel.setDisable(true);
							taskCountExprText.setDisable(true);

							taskCountDistLabel.setDisable(true);
							taskCountDistribution.setDisable(true);
							taskCountAvgLabel.setDisable(true);
							taskCountAvg.setDisable(true);
							taskCountStdevLabel.setDisable(true);
							taskCountStdev.setDisable(true);
							taskCountMaxLabel.setDisable(true);
							taskCountMax.setDisable(true);
							taskCountMinLabel.setDisable(true);
							taskCountMin.setDisable(true);
							taskCountSeedLabel.setDisable(true);
							taskCountSeed.setDisable(true);
							
							taskCountMDBox.setDisable(false);
							taskCountMDAdd.setDisable(false);
							
							checkTaskCountMDBoxStatus();
							
							taskCountPeriodCheck.setDisable(true);
							taskCountPeriodBox.setDisable(true);
							taskCountPeriodAdd.setDisable(true);
							
							taskCountPeriodEdit.setDisable(true);
							taskCountPeriodDelete.setDisable(true);
						} else {
							changeTaskCountNodeStatus();
							taskCountRefCheck.setDisable(false);

							taskCountDistLabel.setDisable(false);
							taskCountDistribution.setDisable(false);
							
							taskCountMDBox.setDisable(true);
							taskCountMDAdd.setDisable(true);
							taskCountMDEdit.setDisable(true);
							taskCountMDDelete.setDisable(true);
							taskCountPeriodCheck.setDisable(false);
						}
						break;
					case "taskCountPeriodCheck":
						if (checkbox.isSelected()) {
							taskCountRefCheck.setDisable(true);
							taskCountRefLabel.setDisable(true);
							taskCountRefBox.setDisable(true);
							taskCountExprLabel.setDisable(true);
							taskCountExprText.setDisable(true);

							taskCountMDCheck.setDisable(true);
							taskCountMDBox.setDisable(true);
							taskCountMDAdd.setDisable(true);
							taskCountMDEdit.setDisable(true);
							taskCountMDDelete.setDisable(true);
							
							taskCountPeriodBox.setDisable(false);
							taskCountPeriodAdd.setDisable(false);
							if(taskCountPeriodBox.getValue()!=null) {
								taskCountPeriodEdit.setDisable(false);
								taskCountPeriodDelete.setDisable(false);
							} else {
								taskCountPeriodEdit.setDisable(true);
								taskCountPeriodDelete.setDisable(true);
							}
						} else {
							changeTaskCountNodeStatus();
							taskCountRefCheck.setDisable(false);
							taskCountMDCheck.setDisable(false);
							taskCountPeriodBox.setDisable(true);
							taskCountPeriodAdd.setDisable(true);
							taskCountPeriodEdit.setDisable(true);
							taskCountPeriodDelete.setDisable(true);
							
						}
						break;
					case "taskLengthRefCheck":
						if (checkbox.isSelected()) {
							taskLengthRefLabel.setDisable(false);
							taskLengthRefBox.setDisable(false);
							taskLengthExprLabel.setDisable(false);
							taskLengthExprText.setDisable(false);

							taskLengthDistLabel.setDisable(true);
							taskLengthDistribution.setDisable(true);
							taskLengthAvgLabel.setDisable(true);
							taskLengthAvg.setDisable(true);
							taskLengthStdevLabel.setDisable(true);
							taskLengthStdev.setDisable(true);
							taskLengthMaxLabel.setDisable(true);
							taskLengthMax.setDisable(true);
							taskLengthMinLabel.setDisable(true);
							taskLengthMin.setDisable(true);
							taskLengthSeedLabel.setDisable(true);
							taskLengthSeed.setDisable(true);

							taskLengthMDCheck.setDisable(true);
							taskLengthMDBox.setDisable(true);
							taskLengthMDAdd.setDisable(true);
							taskLengthMDEdit.setDisable(true);
							taskLengthMDDelete.setDisable(true);
							
							taskLengthPeriodCheck.setDisable(true);
							taskLengthPeriodBox.setDisable(true);
							taskLengthPeriodAdd.setDisable(true);
							taskLengthPeriodEdit.setDisable(true);
							taskLengthPeriodDelete.setDisable(true);
						} else {
							
							changeTaskLengthNodeStatus();
							
							taskLengthRefBox.setDisable(true);
							taskLengthExprLabel.setDisable(true);
							taskLengthExprText.setDisable(true);
							
							taskLengthDistLabel.setDisable(false);
							taskLengthDistribution.setDisable(false);

							taskLengthMDCheck.setDisable(false);
							taskLengthMDBox.setDisable(true);
							taskLengthMDAdd.setDisable(true);
							taskLengthMDEdit.setDisable(true);
							taskLengthMDDelete.setDisable(true);

							taskLengthPeriodCheck.setDisable(false);
							taskLengthPeriodBox.setDisable(true);
							taskLengthPeriodAdd.setDisable(true);
							taskLengthPeriodEdit.setDisable(true);
							taskLengthPeriodDelete.setDisable(true);
						}
						break;
					case "taskLengthMDCheck":
						if (checkbox.isSelected()) {
							taskLengthRefCheck.setDisable(true);
							taskLengthRefLabel.setDisable(true);
							taskLengthRefBox.setDisable(true);
							taskLengthExprLabel.setDisable(true);
							taskLengthExprText.setDisable(true);

							taskLengthDistLabel.setDisable(true);
							taskLengthDistribution.setDisable(true);
							taskLengthAvgLabel.setDisable(true);
							taskLengthAvg.setDisable(true);
							taskLengthStdevLabel.setDisable(true);
							taskLengthStdev.setDisable(true);
							taskLengthMaxLabel.setDisable(true);
							taskLengthMax.setDisable(true);
							taskLengthMinLabel.setDisable(true);
							taskLengthMin.setDisable(true);
							taskLengthSeedLabel.setDisable(true);
							taskLengthSeed.setDisable(true);
							
							taskLengthMDBox.setDisable(false);
							taskLengthMDAdd.setDisable(false);
							
							checkTaskLengthMDBoxStatus();
							
							taskLengthPeriodCheck.setDisable(true);
							taskLengthPeriodBox.setDisable(true);
							taskLengthPeriodAdd.setDisable(true);
							
							taskLengthPeriodEdit.setDisable(true);
							taskLengthPeriodDelete.setDisable(true);
						} else {
							changeTaskLengthNodeStatus();
							taskLengthRefCheck.setDisable(false);

							taskLengthDistLabel.setDisable(false);
							taskLengthDistribution.setDisable(false);
							
							taskLengthMDBox.setDisable(true);
							taskLengthMDAdd.setDisable(true);
							taskLengthMDEdit.setDisable(true);
							taskLengthMDDelete.setDisable(true);
							taskLengthPeriodCheck.setDisable(false);
						}
						break;
					case "taskLengthPeriodCheck":
						if (checkbox.isSelected()) {
							taskLengthRefCheck.setDisable(true);
							taskLengthRefLabel.setDisable(true);
							taskLengthRefBox.setDisable(true);
							taskLengthExprLabel.setDisable(true);
							taskLengthExprText.setDisable(true);

							taskLengthMDCheck.setDisable(true);
							taskLengthMDBox.setDisable(true);
							taskLengthMDAdd.setDisable(true);
							taskLengthMDEdit.setDisable(true);
							taskLengthMDDelete.setDisable(true);
							
							taskLengthPeriodBox.setDisable(false);
							taskLengthPeriodAdd.setDisable(false);
							if(taskLengthPeriodBox.getValue()!=null) {
								taskLengthPeriodEdit.setDisable(false);
								taskLengthPeriodDelete.setDisable(false);
							} else {
								taskLengthPeriodEdit.setDisable(true);
								taskLengthPeriodDelete.setDisable(true);
							}
						} else {
							changeTaskLengthNodeStatus();
							taskLengthRefCheck.setDisable(false);
							taskLengthMDCheck.setDisable(false);
							taskLengthPeriodBox.setDisable(true);
							taskLengthPeriodAdd.setDisable(true);
							taskLengthPeriodEdit.setDisable(true);
							taskLengthPeriodDelete.setDisable(true);
							
						}
						break;
					case "jobPackLenRefCheck":
						if (checkbox.isSelected()) {
							jobPackLenRefLabel.setDisable(false);
							jobPackLenRefBox.setDisable(false);
							jobPackLenExprLabel.setDisable(false);
							jobPackLenExprText.setDisable(false);

							jobPackLenDistLabel.setDisable(true);
							jobPackLenDistribution.setDisable(true);
							jobPackLenAvgLabel.setDisable(true);
							jobPackLenAvg.setDisable(true);
							jobPackLenStdevLabel.setDisable(true);
							jobPackLenStdev.setDisable(true);
							jobPackLenMaxLabel.setDisable(true);
							jobPackLenMax.setDisable(true);
							jobPackLenMinLabel.setDisable(true);
							jobPackLenMin.setDisable(true);
							jobPackLenSeedLabel.setDisable(true);
							jobPackLenSeed.setDisable(true);

							jobPackLenMDCheck.setDisable(true);
							jobPackLenMDBox.setDisable(true);
							jobPackLenMDAdd.setDisable(true);
							jobPackLenMDEdit.setDisable(true);
							jobPackLenMDDelete.setDisable(true);
							
							jobPackLenPeriodCheck.setDisable(true);
							jobPackLenPeriodBox.setDisable(true);
							jobPackLenPeriodAdd.setDisable(true);
							jobPackLenPeriodEdit.setDisable(true);
							jobPackLenPeriodDelete.setDisable(true);
						} else {
							
							changeJobPackLenNodeStatus();
							
							jobPackLenRefBox.setDisable(true);
							jobPackLenExprLabel.setDisable(true);
							jobPackLenExprText.setDisable(true);
							
							jobPackLenDistLabel.setDisable(false);
							jobPackLenDistribution.setDisable(false);

							jobPackLenMDCheck.setDisable(false);
							jobPackLenMDBox.setDisable(true);
							jobPackLenMDAdd.setDisable(true);
							jobPackLenMDEdit.setDisable(true);
							jobPackLenMDDelete.setDisable(true);

							jobPackLenPeriodCheck.setDisable(false);
							jobPackLenPeriodBox.setDisable(true);
							jobPackLenPeriodAdd.setDisable(true);
							jobPackLenPeriodEdit.setDisable(true);
							jobPackLenPeriodDelete.setDisable(true);
						}
						break;
					case "jobPackLenMDCheck":
						if (checkbox.isSelected()) {
							jobPackLenRefCheck.setDisable(true);
							jobPackLenRefLabel.setDisable(true);
							jobPackLenRefBox.setDisable(true);
							jobPackLenExprLabel.setDisable(true);
							jobPackLenExprText.setDisable(true);

							jobPackLenDistLabel.setDisable(true);
							jobPackLenDistribution.setDisable(true);
							jobPackLenAvgLabel.setDisable(true);
							jobPackLenAvg.setDisable(true);
							jobPackLenStdevLabel.setDisable(true);
							jobPackLenStdev.setDisable(true);
							jobPackLenMaxLabel.setDisable(true);
							jobPackLenMax.setDisable(true);
							jobPackLenMinLabel.setDisable(true);
							jobPackLenMin.setDisable(true);
							jobPackLenSeedLabel.setDisable(true);
							jobPackLenSeed.setDisable(true);
							
							jobPackLenMDBox.setDisable(false);
							jobPackLenMDAdd.setDisable(false);
							
							checkJobPackLenMDBoxStatus();
							
							jobPackLenPeriodCheck.setDisable(true);
							jobPackLenPeriodBox.setDisable(true);
							jobPackLenPeriodAdd.setDisable(true);
							
							jobPackLenPeriodEdit.setDisable(true);
							jobPackLenPeriodDelete.setDisable(true);
						} else {
							changeJobPackLenNodeStatus();
							jobPackLenRefCheck.setDisable(false);

							jobPackLenDistLabel.setDisable(false);
							jobPackLenDistribution.setDisable(false);
							
							jobPackLenMDBox.setDisable(true);
							jobPackLenMDAdd.setDisable(true);
							jobPackLenMDEdit.setDisable(true);
							jobPackLenMDDelete.setDisable(true);
							jobPackLenPeriodCheck.setDisable(false);
						}
						break;
					case "jobPackLenPeriodCheck":
						if (checkbox.isSelected()) {
							jobPackLenRefCheck.setDisable(true);
							jobPackLenRefLabel.setDisable(true);
							jobPackLenRefBox.setDisable(true);
							jobPackLenExprLabel.setDisable(true);
							jobPackLenExprText.setDisable(true);

							jobPackLenMDCheck.setDisable(true);
							jobPackLenMDBox.setDisable(true);
							jobPackLenMDAdd.setDisable(true);
							jobPackLenMDEdit.setDisable(true);
							jobPackLenMDDelete.setDisable(true);
							
							jobPackLenPeriodBox.setDisable(false);
							jobPackLenPeriodAdd.setDisable(false);
							if(jobPackLenPeriodBox.getValue()!=null) {
								jobPackLenPeriodEdit.setDisable(false);
								jobPackLenPeriodDelete.setDisable(false);
							} else {
								jobPackLenPeriodEdit.setDisable(true);
								jobPackLenPeriodDelete.setDisable(true);
							}
						} else {
							changeJobPackLenNodeStatus();
							jobPackLenRefCheck.setDisable(false);
							jobPackLenMDCheck.setDisable(false);
							jobPackLenPeriodBox.setDisable(true);
							jobPackLenPeriodAdd.setDisable(true);
							jobPackLenPeriodEdit.setDisable(true);
							jobPackLenPeriodDelete.setDisable(true);
							
						}
						break;
					case "jobIntervalRefCheck":
						if (checkbox.isSelected()) {
							jobIntervalRefLabel.setDisable(false);
							jobIntervalRefBox.setDisable(false);
							jobIntervalExprLabel.setDisable(false);
							jobIntervalExprText.setDisable(false);

							jobIntervalDistLabel.setDisable(true);
							jobIntervalDistribution.setDisable(true);
							jobIntervalAvgLabel.setDisable(true);
							jobIntervalAvg.setDisable(true);
							jobIntervalStdevLabel.setDisable(true);
							jobIntervalStdev.setDisable(true);
							jobIntervalMaxLabel.setDisable(true);
							jobIntervalMax.setDisable(true);
							jobIntervalMinLabel.setDisable(true);
							jobIntervalMin.setDisable(true);
							jobIntervalSeedLabel.setDisable(true);
							jobIntervalSeed.setDisable(true);

							jobIntervalMDCheck.setDisable(true);
							jobIntervalMDBox.setDisable(true);
							jobIntervalMDAdd.setDisable(true);
							jobIntervalMDEdit.setDisable(true);
							jobIntervalMDDelete.setDisable(true);
							
							jobIntervalPeriodCheck.setDisable(true);
							jobIntervalPeriodBox.setDisable(true);
							jobIntervalPeriodAdd.setDisable(true);
							jobIntervalPeriodEdit.setDisable(true);
							jobIntervalPeriodDelete.setDisable(true);
						} else {
							
							changeJobIntervalNodeStatus();
							
							jobIntervalRefBox.setDisable(true);
							jobIntervalExprLabel.setDisable(true);
							jobIntervalExprText.setDisable(true);
							
							jobIntervalDistLabel.setDisable(false);
							jobIntervalDistribution.setDisable(false);

							jobIntervalMDCheck.setDisable(false);
							jobIntervalMDBox.setDisable(true);
							jobIntervalMDAdd.setDisable(true);
							jobIntervalMDEdit.setDisable(true);
							jobIntervalMDDelete.setDisable(true);

							jobIntervalPeriodCheck.setDisable(false);
							jobIntervalPeriodBox.setDisable(true);
							jobIntervalPeriodAdd.setDisable(true);
							jobIntervalPeriodEdit.setDisable(true);
							jobIntervalPeriodDelete.setDisable(true);
						}
						break;
					case "jobIntervalMDCheck":
						if (checkbox.isSelected()) {
							jobIntervalRefCheck.setDisable(true);
							jobIntervalRefLabel.setDisable(true);
							jobIntervalRefBox.setDisable(true);
							jobIntervalExprLabel.setDisable(true);
							jobIntervalExprText.setDisable(true);

							jobIntervalDistLabel.setDisable(true);
							jobIntervalDistribution.setDisable(true);
							jobIntervalAvgLabel.setDisable(true);
							jobIntervalAvg.setDisable(true);
							jobIntervalStdevLabel.setDisable(true);
							jobIntervalStdev.setDisable(true);
							jobIntervalMaxLabel.setDisable(true);
							jobIntervalMax.setDisable(true);
							jobIntervalMinLabel.setDisable(true);
							jobIntervalMin.setDisable(true);
							jobIntervalSeedLabel.setDisable(true);
							jobIntervalSeed.setDisable(true);
							
							jobIntervalMDBox.setDisable(false);
							jobIntervalMDAdd.setDisable(false);
							
							checkJobIntervalMDBoxStatus();
							
							jobIntervalPeriodCheck.setDisable(true);
							jobIntervalPeriodBox.setDisable(true);
							jobIntervalPeriodAdd.setDisable(true);
							
							jobIntervalPeriodEdit.setDisable(true);
							jobIntervalPeriodDelete.setDisable(true);
						} else {
							changeJobIntervalNodeStatus();
							jobIntervalRefCheck.setDisable(false);

							jobIntervalDistLabel.setDisable(false);
							jobIntervalDistribution.setDisable(false);
							
							jobIntervalMDBox.setDisable(true);
							jobIntervalMDAdd.setDisable(true);
							jobIntervalMDEdit.setDisable(true);
							jobIntervalMDDelete.setDisable(true);
							jobIntervalPeriodCheck.setDisable(false);
						}
						break;
					case "jobIntervalPeriodCheck":
						if (checkbox.isSelected()) {
							jobIntervalRefCheck.setDisable(true);
							jobIntervalRefLabel.setDisable(true);
							jobIntervalRefBox.setDisable(true);
							jobIntervalExprLabel.setDisable(true);
							jobIntervalExprText.setDisable(true);

							jobIntervalMDCheck.setDisable(true);
							jobIntervalMDBox.setDisable(true);
							jobIntervalMDAdd.setDisable(true);
							jobIntervalMDEdit.setDisable(true);
							jobIntervalMDDelete.setDisable(true);
							
							jobIntervalPeriodBox.setDisable(false);
							jobIntervalPeriodAdd.setDisable(false);
							if(jobIntervalPeriodBox.getValue()!=null) {
								jobIntervalPeriodEdit.setDisable(false);
								jobIntervalPeriodDelete.setDisable(false);
							} else {
								jobIntervalPeriodEdit.setDisable(true);
								jobIntervalPeriodDelete.setDisable(true);
							}
						} else {
							changeJobIntervalNodeStatus();
							jobIntervalRefCheck.setDisable(false);
							jobIntervalMDCheck.setDisable(false);
							jobIntervalPeriodBox.setDisable(true);
							jobIntervalPeriodAdd.setDisable(true);
							jobIntervalPeriodEdit.setDisable(true);
							jobIntervalPeriodDelete.setDisable(true);
							
						}
						break;
					case "cpucntRefCheck":
						if (checkbox.isSelected()) {
							cpucntRefLabel.setDisable(false);
							cpucntRefBox.setDisable(false);
							cpucntExprLabel.setDisable(false);
							cpucntExprText.setDisable(false);

							cpucntDistLabel.setDisable(true);
							cpucntDistribution.setDisable(true);
							cpucntAvgLabel.setDisable(true);
							cpucntAvg.setDisable(true);
							cpucntStdevLabel.setDisable(true);
							cpucntStdev.setDisable(true);
							cpucntMaxLabel.setDisable(true);
							cpucntMax.setDisable(true);
							cpucntMinLabel.setDisable(true);
							cpucntMin.setDisable(true);
							cpucntSeedLabel.setDisable(true);
							cpucntSeed.setDisable(true);

							cpucntMDCheck.setDisable(true);
							cpucntMDBox.setDisable(true);
							cpucntMDAdd.setDisable(true);
							cpucntMDEdit.setDisable(true);
							cpucntMDDelete.setDisable(true);
							
							cpucntPeriodCheck.setDisable(true);
							cpucntPeriodBox.setDisable(true);
							cpucntPeriodAdd.setDisable(true);
							cpucntPeriodEdit.setDisable(true);
							cpucntPeriodDelete.setDisable(true);
						} else {
							
							changeCpucntNodeStatus();
							
							cpucntRefBox.setDisable(true);
							cpucntExprLabel.setDisable(true);
							cpucntExprText.setDisable(true);
							
							cpucntDistLabel.setDisable(false);
							cpucntDistribution.setDisable(false);

							cpucntMDCheck.setDisable(false);
							cpucntMDBox.setDisable(true);
							cpucntMDAdd.setDisable(true);
							cpucntMDEdit.setDisable(true);
							cpucntMDDelete.setDisable(true);

							cpucntPeriodCheck.setDisable(false);
							cpucntPeriodBox.setDisable(true);
							cpucntPeriodAdd.setDisable(true);
							cpucntPeriodEdit.setDisable(true);
							cpucntPeriodDelete.setDisable(true);
						}
						break;
					case "cpucntMDCheck":
						if (checkbox.isSelected()) {
							cpucntRefCheck.setDisable(true);
							cpucntRefLabel.setDisable(true);
							cpucntRefBox.setDisable(true);
							cpucntExprLabel.setDisable(true);
							cpucntExprText.setDisable(true);

							cpucntDistLabel.setDisable(true);
							cpucntDistribution.setDisable(true);
							cpucntAvgLabel.setDisable(true);
							cpucntAvg.setDisable(true);
							cpucntStdevLabel.setDisable(true);
							cpucntStdev.setDisable(true);
							cpucntMaxLabel.setDisable(true);
							cpucntMax.setDisable(true);
							cpucntMinLabel.setDisable(true);
							cpucntMin.setDisable(true);
							cpucntSeedLabel.setDisable(true);
							cpucntSeed.setDisable(true);
							
							cpucntMDBox.setDisable(false);
							cpucntMDAdd.setDisable(false);
							
							checkCpucntMDBoxStatus();
							
							cpucntPeriodCheck.setDisable(true);
							cpucntPeriodBox.setDisable(true);
							cpucntPeriodAdd.setDisable(true);
							
							cpucntPeriodEdit.setDisable(true);
							cpucntPeriodDelete.setDisable(true);
						} else {
							changeCpucntNodeStatus();
							cpucntRefCheck.setDisable(false);

							cpucntDistLabel.setDisable(false);
							cpucntDistribution.setDisable(false);
							
							cpucntMDBox.setDisable(true);
							cpucntMDAdd.setDisable(true);
							cpucntMDEdit.setDisable(true);
							cpucntMDDelete.setDisable(true);
							cpucntPeriodCheck.setDisable(false);
						}
						break;
					case "cpucntPeriodCheck":
						if (checkbox.isSelected()) {
							cpucntRefCheck.setDisable(true);
							cpucntRefLabel.setDisable(true);
							cpucntRefBox.setDisable(true);
							cpucntExprLabel.setDisable(true);
							cpucntExprText.setDisable(true);

							cpucntMDCheck.setDisable(true);
							cpucntMDBox.setDisable(true);
							cpucntMDAdd.setDisable(true);
							cpucntMDEdit.setDisable(true);
							cpucntMDDelete.setDisable(true);
							
							cpucntPeriodBox.setDisable(false);
							cpucntPeriodAdd.setDisable(false);
							if(cpucntPeriodBox.getValue()!=null) {
								cpucntPeriodEdit.setDisable(false);
								cpucntPeriodDelete.setDisable(false);
							} else {
								cpucntPeriodEdit.setDisable(true);
								cpucntPeriodDelete.setDisable(true);
							}
						} else {
							changeCpucntNodeStatus();
							cpucntRefCheck.setDisable(false);
							cpucntMDCheck.setDisable(false);
							cpucntPeriodBox.setDisable(true);
							cpucntPeriodAdd.setDisable(true);
							cpucntPeriodEdit.setDisable(true);
							cpucntPeriodDelete.setDisable(true);
							
						}
						break;
					case "memoryRefCheck":
						if (checkbox.isSelected()) {
							memoryRefLabel.setDisable(false);
							memoryRefBox.setDisable(false);
							memoryExprLabel.setDisable(false);
							memoryExprText.setDisable(false);

							memoryDistLabel.setDisable(true);
							memoryDistribution.setDisable(true);
							memoryAvgLabel.setDisable(true);
							memoryAvg.setDisable(true);
							memoryStdevLabel.setDisable(true);
							memoryStdev.setDisable(true);
							memoryMaxLabel.setDisable(true);
							memoryMax.setDisable(true);
							memoryMinLabel.setDisable(true);
							memoryMin.setDisable(true);
							memorySeedLabel.setDisable(true);
							memorySeed.setDisable(true);

							memoryMDCheck.setDisable(true);
							memoryMDBox.setDisable(true);
							memoryMDAdd.setDisable(true);
							memoryMDEdit.setDisable(true);
							memoryMDDelete.setDisable(true);
							
							memoryPeriodCheck.setDisable(true);
							memoryPeriodBox.setDisable(true);
							memoryPeriodAdd.setDisable(true);
							memoryPeriodEdit.setDisable(true);
							memoryPeriodDelete.setDisable(true);
						} else {
							
							changeMemoryNodeStatus();
							
							memoryRefBox.setDisable(true);
							memoryExprLabel.setDisable(true);
							memoryExprText.setDisable(true);
							
							memoryDistLabel.setDisable(false);
							memoryDistribution.setDisable(false);

							memoryMDCheck.setDisable(false);
							memoryMDBox.setDisable(true);
							memoryMDAdd.setDisable(true);
							memoryMDEdit.setDisable(true);
							memoryMDDelete.setDisable(true);

							memoryPeriodCheck.setDisable(false);
							memoryPeriodBox.setDisable(true);
							memoryPeriodAdd.setDisable(true);
							memoryPeriodEdit.setDisable(true);
							memoryPeriodDelete.setDisable(true);
						}
						break;
					case "memoryMDCheck":
						if (checkbox.isSelected()) {
							memoryRefCheck.setDisable(true);
							memoryRefLabel.setDisable(true);
							memoryRefBox.setDisable(true);
							memoryExprLabel.setDisable(true);
							memoryExprText.setDisable(true);

							memoryDistLabel.setDisable(true);
							memoryDistribution.setDisable(true);
							memoryAvgLabel.setDisable(true);
							memoryAvg.setDisable(true);
							memoryStdevLabel.setDisable(true);
							memoryStdev.setDisable(true);
							memoryMaxLabel.setDisable(true);
							memoryMax.setDisable(true);
							memoryMinLabel.setDisable(true);
							memoryMin.setDisable(true);
							memorySeedLabel.setDisable(true);
							memorySeed.setDisable(true);
							
							memoryMDBox.setDisable(false);
							memoryMDAdd.setDisable(false);
							
							checkMemoryMDBoxStatus();
							
							memoryPeriodCheck.setDisable(true);
							memoryPeriodBox.setDisable(true);
							memoryPeriodAdd.setDisable(true);
							
							memoryPeriodEdit.setDisable(true);
							memoryPeriodDelete.setDisable(true);
						} else {
							changeMemoryNodeStatus();
							memoryRefCheck.setDisable(false);

							memoryDistLabel.setDisable(false);
							memoryDistribution.setDisable(false);
							
							memoryMDBox.setDisable(true);
							memoryMDAdd.setDisable(true);
							memoryMDEdit.setDisable(true);
							memoryMDDelete.setDisable(true);
							memoryPeriodCheck.setDisable(false);
						}
						break;
					case "memoryPeriodCheck":
						if (checkbox.isSelected()) {
							memoryRefCheck.setDisable(true);
							memoryRefLabel.setDisable(true);
							memoryRefBox.setDisable(true);
							memoryExprLabel.setDisable(true);
							memoryExprText.setDisable(true);

							memoryMDCheck.setDisable(true);
							memoryMDBox.setDisable(true);
							memoryMDAdd.setDisable(true);
							memoryMDEdit.setDisable(true);
							memoryMDDelete.setDisable(true);
							
							memoryPeriodBox.setDisable(false);
							memoryPeriodAdd.setDisable(false);
							if(memoryPeriodBox.getValue()!=null) {
								memoryPeriodEdit.setDisable(false);
								memoryPeriodDelete.setDisable(false);
							} else {
								memoryPeriodEdit.setDisable(true);
								memoryPeriodDelete.setDisable(true);
							}
						} else {
							changeMemoryNodeStatus();
							memoryRefCheck.setDisable(false);
							memoryMDCheck.setDisable(false);
							memoryPeriodBox.setDisable(true);
							memoryPeriodAdd.setDisable(true);
							memoryPeriodEdit.setDisable(true);
							memoryPeriodDelete.setDisable(true);
							
						}
						break;
					}
				}
				if (event.getSource() instanceof ComboBox) {
					ComboBox<String> combobox = (ComboBox<String>) event.getSource();
					if ("taskCountDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							taskCountAvgLabel.setDisable(false);
							taskCountAvg.setDisable(false);
							taskCountStdevLabel.setDisable(true);
							taskCountStdev.setDisable(true);
							taskCountMaxLabel.setDisable(true);
							taskCountMax.setDisable(true);
							taskCountMinLabel.setDisable(true);
							taskCountMin.setDisable(true);
							taskCountSeedLabel.setDisable(true);
							taskCountSeed.setDisable(true);
							break;
						case "normal":
							taskCountAvgLabel.setDisable(false);
							taskCountAvg.setDisable(false);
							taskCountStdevLabel.setDisable(false);
							taskCountStdev.setDisable(false);
							taskCountMaxLabel.setDisable(false);
							taskCountMax.setDisable(false);
							taskCountMinLabel.setDisable(false);
							taskCountMin.setDisable(false);
							taskCountSeedLabel.setDisable(false);
							taskCountSeed.setDisable(false);
							break;
						case "poisson":
							taskCountAvgLabel.setDisable(false);
							taskCountAvg.setDisable(false);
							taskCountStdevLabel.setDisable(true);
							taskCountStdev.setDisable(true);
							taskCountMaxLabel.setDisable(false);
							taskCountMax.setDisable(false);
							taskCountMinLabel.setDisable(false);
							taskCountMin.setDisable(false);
							taskCountSeedLabel.setDisable(false);
							taskCountSeed.setDisable(false);
							break;
						case "uniform":
							taskCountAvgLabel.setDisable(true);
							taskCountAvg.setDisable(true);
							taskCountStdevLabel.setDisable(true);
							taskCountStdev.setDisable(true);
							taskCountMaxLabel.setDisable(false);
							taskCountMax.setDisable(false);
							taskCountMinLabel.setDisable(false);
							taskCountMin.setDisable(false);
							taskCountSeedLabel.setDisable(false);
							taskCountSeed.setDisable(false);
							break;
						}
					} else if ("taskLengthDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							taskLengthAvgLabel.setDisable(false);
							taskLengthAvg.setDisable(false);
							taskLengthStdevLabel.setDisable(true);
							taskLengthStdev.setDisable(true);
							taskLengthMaxLabel.setDisable(true);
							taskLengthMax.setDisable(true);
							taskLengthMinLabel.setDisable(true);
							taskLengthMin.setDisable(true);
							taskLengthSeedLabel.setDisable(true);
							taskLengthSeed.setDisable(true);
							break;
						case "normal":
							taskLengthAvgLabel.setDisable(false);
							taskLengthAvg.setDisable(false);
							taskLengthStdevLabel.setDisable(false);
							taskLengthStdev.setDisable(false);
							taskLengthMaxLabel.setDisable(false);
							taskLengthMax.setDisable(false);
							taskLengthMinLabel.setDisable(false);
							taskLengthMin.setDisable(false);
							taskLengthSeedLabel.setDisable(false);
							taskLengthSeed.setDisable(false);
							break;
						case "poisson":
							taskLengthAvgLabel.setDisable(false);
							taskLengthAvg.setDisable(false);
							taskLengthStdevLabel.setDisable(true);
							taskLengthStdev.setDisable(true);
							taskLengthMaxLabel.setDisable(false);
							taskLengthMax.setDisable(false);
							taskLengthMinLabel.setDisable(false);
							taskLengthMin.setDisable(false);
							taskLengthSeedLabel.setDisable(false);
							taskLengthSeed.setDisable(false);
							break;
						case "uniform":
							taskLengthAvgLabel.setDisable(true);
							taskLengthAvg.setDisable(true);
							taskLengthStdevLabel.setDisable(true);
							taskLengthStdev.setDisable(true);
							taskLengthMaxLabel.setDisable(false);
							taskLengthMax.setDisable(false);
							taskLengthMinLabel.setDisable(false);
							taskLengthMin.setDisable(false);
							taskLengthSeedLabel.setDisable(false);
							taskLengthSeed.setDisable(false);
							break;
						}
					} else if ("jobPackLenDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							jobPackLenAvgLabel.setDisable(false);
							jobPackLenAvg.setDisable(false);
							jobPackLenStdevLabel.setDisable(true);
							jobPackLenStdev.setDisable(true);
							jobPackLenMaxLabel.setDisable(true);
							jobPackLenMax.setDisable(true);
							jobPackLenMinLabel.setDisable(true);
							jobPackLenMin.setDisable(true);
							jobPackLenSeedLabel.setDisable(true);
							jobPackLenSeed.setDisable(true);
							break;
						case "normal":
							jobPackLenAvgLabel.setDisable(false);
							jobPackLenAvg.setDisable(false);
							jobPackLenStdevLabel.setDisable(false);
							jobPackLenStdev.setDisable(false);
							jobPackLenMaxLabel.setDisable(false);
							jobPackLenMax.setDisable(false);
							jobPackLenMinLabel.setDisable(false);
							jobPackLenMin.setDisable(false);
							jobPackLenSeedLabel.setDisable(false);
							jobPackLenSeed.setDisable(false);
							break;
						case "poisson":
							jobPackLenAvgLabel.setDisable(false);
							jobPackLenAvg.setDisable(false);
							jobPackLenStdevLabel.setDisable(true);
							jobPackLenStdev.setDisable(true);
							jobPackLenMaxLabel.setDisable(false);
							jobPackLenMax.setDisable(false);
							jobPackLenMinLabel.setDisable(false);
							jobPackLenMin.setDisable(false);
							jobPackLenSeedLabel.setDisable(false);
							jobPackLenSeed.setDisable(false);
							break;
						case "uniform":
							jobPackLenAvgLabel.setDisable(true);
							jobPackLenAvg.setDisable(true);
							jobPackLenStdevLabel.setDisable(true);
							jobPackLenStdev.setDisable(true);
							jobPackLenMaxLabel.setDisable(false);
							jobPackLenMax.setDisable(false);
							jobPackLenMinLabel.setDisable(false);
							jobPackLenMin.setDisable(false);
							jobPackLenSeedLabel.setDisable(false);
							jobPackLenSeed.setDisable(false);
							break;
						}
					} else if ("jobIntervalDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							jobIntervalAvgLabel.setDisable(false);
							jobIntervalAvg.setDisable(false);
							jobIntervalStdevLabel.setDisable(true);
							jobIntervalStdev.setDisable(true);
							jobIntervalMaxLabel.setDisable(true);
							jobIntervalMax.setDisable(true);
							jobIntervalMinLabel.setDisable(true);
							jobIntervalMin.setDisable(true);
							jobIntervalSeedLabel.setDisable(true);
							jobIntervalSeed.setDisable(true);
							break;
						case "normal":
							jobIntervalAvgLabel.setDisable(false);
							jobIntervalAvg.setDisable(false);
							jobIntervalStdevLabel.setDisable(false);
							jobIntervalStdev.setDisable(false);
							jobIntervalMaxLabel.setDisable(false);
							jobIntervalMax.setDisable(false);
							jobIntervalMinLabel.setDisable(false);
							jobIntervalMin.setDisable(false);
							jobIntervalSeedLabel.setDisable(false);
							jobIntervalSeed.setDisable(false);
							break;
						case "poisson":
							jobIntervalAvgLabel.setDisable(false);
							jobIntervalAvg.setDisable(false);
							jobIntervalStdevLabel.setDisable(true);
							jobIntervalStdev.setDisable(true);
							jobIntervalMaxLabel.setDisable(false);
							jobIntervalMax.setDisable(false);
							jobIntervalMinLabel.setDisable(false);
							jobIntervalMin.setDisable(false);
							jobIntervalSeedLabel.setDisable(false);
							jobIntervalSeed.setDisable(false);
							break;
						case "uniform":
							jobIntervalAvgLabel.setDisable(true);
							jobIntervalAvg.setDisable(true);
							jobIntervalStdevLabel.setDisable(true);
							jobIntervalStdev.setDisable(true);
							jobIntervalMaxLabel.setDisable(false);
							jobIntervalMax.setDisable(false);
							jobIntervalMinLabel.setDisable(false);
							jobIntervalMin.setDisable(false);
							jobIntervalSeedLabel.setDisable(false);
							jobIntervalSeed.setDisable(false);
							break;
						}
					} else if ("cpucntDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							cpucntAvgLabel.setDisable(false);
							cpucntAvg.setDisable(false);
							cpucntStdevLabel.setDisable(true);
							cpucntStdev.setDisable(true);
							cpucntMaxLabel.setDisable(true);
							cpucntMax.setDisable(true);
							cpucntMinLabel.setDisable(true);
							cpucntMin.setDisable(true);
							cpucntSeedLabel.setDisable(true);
							cpucntSeed.setDisable(true);
							break;
						case "normal":
							cpucntAvgLabel.setDisable(false);
							cpucntAvg.setDisable(false);
							cpucntStdevLabel.setDisable(false);
							cpucntStdev.setDisable(false);
							cpucntMaxLabel.setDisable(false);
							cpucntMax.setDisable(false);
							cpucntMinLabel.setDisable(false);
							cpucntMin.setDisable(false);
							cpucntSeedLabel.setDisable(false);
							cpucntSeed.setDisable(false);
							break;
						case "poisson":
							cpucntAvgLabel.setDisable(false);
							cpucntAvg.setDisable(false);
							cpucntStdevLabel.setDisable(true);
							cpucntStdev.setDisable(true);
							cpucntMaxLabel.setDisable(false);
							cpucntMax.setDisable(false);
							cpucntMinLabel.setDisable(false);
							cpucntMin.setDisable(false);
							cpucntSeedLabel.setDisable(false);
							cpucntSeed.setDisable(false);
							break;
						case "uniform":
							cpucntAvgLabel.setDisable(true);
							cpucntAvg.setDisable(true);
							cpucntStdevLabel.setDisable(true);
							cpucntStdev.setDisable(true);
							cpucntMaxLabel.setDisable(false);
							cpucntMax.setDisable(false);
							cpucntMinLabel.setDisable(false);
							cpucntMin.setDisable(false);
							cpucntSeedLabel.setDisable(false);
							cpucntSeed.setDisable(false);
							break;
						}
					} else if ("memoryDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							memoryAvgLabel.setDisable(false);
							memoryAvg.setDisable(false);
							memoryStdevLabel.setDisable(true);
							memoryStdev.setDisable(true);
							memoryMaxLabel.setDisable(true);
							memoryMax.setDisable(true);
							memoryMinLabel.setDisable(true);
							memoryMin.setDisable(true);
							memorySeedLabel.setDisable(true);
							memorySeed.setDisable(true);
							break;
						case "normal":
							memoryAvgLabel.setDisable(false);
							memoryAvg.setDisable(false);
							memoryStdevLabel.setDisable(false);
							memoryStdev.setDisable(false);
							memoryMaxLabel.setDisable(false);
							memoryMax.setDisable(false);
							memoryMinLabel.setDisable(false);
							memoryMin.setDisable(false);
							memorySeedLabel.setDisable(false);
							memorySeed.setDisable(false);
							break;
						case "poisson":
							memoryAvgLabel.setDisable(false);
							memoryAvg.setDisable(false);
							memoryStdevLabel.setDisable(true);
							memoryStdev.setDisable(true);
							memoryMaxLabel.setDisable(false);
							memoryMax.setDisable(false);
							memoryMinLabel.setDisable(false);
							memoryMin.setDisable(false);
							memorySeedLabel.setDisable(false);
							memorySeed.setDisable(false);
							break;
						case "uniform":
							memoryAvgLabel.setDisable(true);
							memoryAvg.setDisable(true);
							memoryStdevLabel.setDisable(true);
							memoryStdev.setDisable(true);
							memoryMaxLabel.setDisable(false);
							memoryMax.setDisable(false);
							memoryMinLabel.setDisable(false);
							memoryMin.setDisable(false);
							memorySeedLabel.setDisable(false);
							memorySeed.setDisable(false);
							break;
						}
					}
				}
			}

		};
		this.taskCountDistribution.setOnAction(eventHandler);
		this.taskLengthDistribution.setOnAction(eventHandler);
		this.jobPackLenDistribution.setOnAction(eventHandler);
		this.jobIntervalDistribution.setOnAction(eventHandler);
		this.cpucntDistribution.setOnAction(eventHandler);
		this.memoryDistribution.setOnAction(eventHandler);
		
		this.taskCountRefCheck.setOnAction(eventHandler);
		this.taskCountMDCheck.setOnAction(eventHandler);
		this.taskCountPeriodCheck.setOnAction(eventHandler);

		this.taskLengthRefCheck.setOnAction(eventHandler);
		this.taskLengthMDCheck.setOnAction(eventHandler);
		this.taskLengthPeriodCheck.setOnAction(eventHandler);

		this.jobPackLenRefCheck.setOnAction(eventHandler);
		this.jobPackLenMDCheck.setOnAction(eventHandler);
		this.jobPackLenPeriodCheck.setOnAction(eventHandler);
		
		this.jobIntervalRefCheck.setOnAction(eventHandler);
		this.jobIntervalMDCheck.setOnAction(eventHandler);
		this.jobIntervalPeriodCheck.setOnAction(eventHandler);
		
		this.cpucntRefCheck.setOnAction(eventHandler);
		this.cpucntMDCheck.setOnAction(eventHandler);
		this.cpucntPeriodCheck.setOnAction(eventHandler);
		
		this.memoryRefCheck.setOnAction(eventHandler);
		this.memoryMDCheck.setOnAction(eventHandler);
		this.memoryPeriodCheck.setOnAction(eventHandler);
	}
	
	private void initRefCombobox() {
		ObservableList<String> options = FXCollections.observableArrayList(
			        "TaskLength",
			        "JobPackageLength",
			        "JobInterval",
			        "CpuCount",
			        "Memory"
			    );
		taskCountRefBox.setItems(options);
		taskLengthRefBox.setItems(options);
		jobPackLenRefBox.setItems(options);
		jobIntervalRefBox.setItems(options);
		cpucntRefBox.setItems(options);
		memoryRefBox.setItems(options);
	}

	public void initComponentStatus() {
		/** TaskCount*/
		this.taskCountRefLabel.setDisable(true);
		this.taskCountRefBox.setDisable(true);
		this.taskCountExprLabel.setDisable(true);
		this.taskCountExprText.setDisable(true);
		
		this.taskCountMaxLabel.setDisable(true);
		this.taskCountMax.setDisable(true);
		this.taskCountMinLabel.setDisable(true);
		this.taskCountMin.setDisable(true);
		this.taskCountStdevLabel.setDisable(true);
		this.taskCountStdev.setDisable(true);
		this.taskCountSeedLabel.setDisable(true);
		this.taskCountSeed.setDisable(true);
		
		this.taskCountMDBox.setDisable(true);
		taskCountMDAdd.setDisable(true);
		taskCountMDEdit.setDisable(true);
		taskCountMDDelete.setDisable(true);
		this.taskCountPeriodBox.setDisable(true);
		taskCountPeriodAdd.setDisable(true);
		taskCountPeriodEdit.setDisable(true);
		taskCountPeriodDelete.setDisable(true);
		
		/** TaskLength	*/
		this.taskLengthRefLabel.setDisable(true);
		this.taskLengthRefBox.setDisable(true);
		this.taskLengthExprLabel.setDisable(true);
		this.taskLengthExprText.setDisable(true);
		
		this.taskLengthMaxLabel.setDisable(true);
		this.taskLengthMax.setDisable(true);
		this.taskLengthMinLabel.setDisable(true);
		this.taskLengthMin.setDisable(true);
		this.taskLengthStdevLabel.setDisable(true);
		this.taskLengthStdev.setDisable(true);
		this.taskLengthSeedLabel.setDisable(true);
		this.taskLengthSeed.setDisable(true);
		
		this.taskLengthMDBox.setDisable(true);
		taskLengthMDAdd.setDisable(true);
		taskLengthMDEdit.setDisable(true);
		taskLengthMDDelete.setDisable(true);
		this.taskLengthPeriodBox.setDisable(true);
		taskLengthPeriodAdd.setDisable(true);
		taskLengthPeriodEdit.setDisable(true);
		taskLengthPeriodDelete.setDisable(true);
		
		/** JobPackageLength	*/
		this.jobPackLenRefLabel.setDisable(true);
		this.jobPackLenRefBox.setDisable(true);
		this.jobPackLenExprLabel.setDisable(true);
		this.jobPackLenExprText.setDisable(true);
		
		this.jobPackLenMaxLabel.setDisable(true);
		this.jobPackLenMax.setDisable(true);
		this.jobPackLenMinLabel.setDisable(true);
		this.jobPackLenMin.setDisable(true);
		this.jobPackLenStdevLabel.setDisable(true);
		this.jobPackLenStdev.setDisable(true);
		this.jobPackLenSeedLabel.setDisable(true);
		this.jobPackLenSeed.setDisable(true);
		
		this.jobPackLenMDBox.setDisable(true);
		jobPackLenMDAdd.setDisable(true);
		jobPackLenMDEdit.setDisable(true);
		jobPackLenMDDelete.setDisable(true);
		this.jobPackLenPeriodBox.setDisable(true);
		jobPackLenPeriodAdd.setDisable(true);
		jobPackLenPeriodEdit.setDisable(true);
		jobPackLenPeriodDelete.setDisable(true);
		
		/** JobInterval	*/
		this.jobIntervalRefLabel.setDisable(true);
		this.jobIntervalRefBox.setDisable(true);
		this.jobIntervalExprLabel.setDisable(true);
		this.jobIntervalExprText.setDisable(true);
		
		this.jobIntervalMaxLabel.setDisable(true);
		this.jobIntervalMax.setDisable(true);
		this.jobIntervalMinLabel.setDisable(true);
		this.jobIntervalMin.setDisable(true);
		this.jobIntervalStdevLabel.setDisable(true);
		this.jobIntervalStdev.setDisable(true);
		this.jobIntervalSeedLabel.setDisable(true);
		this.jobIntervalSeed.setDisable(true);
		
		this.jobIntervalMDBox.setDisable(true);
		jobIntervalMDAdd.setDisable(true);
		jobIntervalMDEdit.setDisable(true);
		jobIntervalMDDelete.setDisable(true);
		this.jobIntervalPeriodBox.setDisable(true);
		jobIntervalPeriodAdd.setDisable(true);
		jobIntervalPeriodEdit.setDisable(true);
		jobIntervalPeriodDelete.setDisable(true);
		
		/** CpuCount	*/
		this.cpucntRefLabel.setDisable(true);
		this.cpucntRefBox.setDisable(true);
		this.cpucntExprLabel.setDisable(true);
		this.cpucntExprText.setDisable(true);
		
		this.cpucntMaxLabel.setDisable(true);
		this.cpucntMax.setDisable(true);
		this.cpucntMinLabel.setDisable(true);
		this.cpucntMin.setDisable(true);
		this.cpucntStdevLabel.setDisable(true);
		this.cpucntStdev.setDisable(true);
		this.cpucntSeedLabel.setDisable(true);
		this.cpucntSeed.setDisable(true);
		
		this.cpucntMDBox.setDisable(true);
		cpucntMDAdd.setDisable(true);
		cpucntMDEdit.setDisable(true);
		cpucntMDDelete.setDisable(true);
		this.cpucntPeriodBox.setDisable(true);
		cpucntPeriodAdd.setDisable(true);
		cpucntPeriodEdit.setDisable(true);
		cpucntPeriodDelete.setDisable(true);
		
		/** Memory	*/
		this.memoryRefLabel.setDisable(true);
		this.memoryRefBox.setDisable(true);
		this.memoryExprLabel.setDisable(true);
		this.memoryExprText.setDisable(true);
		
		this.memoryMaxLabel.setDisable(true);
		this.memoryMax.setDisable(true);
		this.memoryMinLabel.setDisable(true);
		this.memoryMin.setDisable(true);
		this.memoryStdevLabel.setDisable(true);
		this.memoryStdev.setDisable(true);
		this.memorySeedLabel.setDisable(true);
		this.memorySeed.setDisable(true);
		
		this.memoryMDBox.setDisable(true);
		memoryMDAdd.setDisable(true);
		memoryMDEdit.setDisable(true);
		memoryMDDelete.setDisable(true);
		this.memoryPeriodBox.setDisable(true);
		memoryPeriodAdd.setDisable(true);
		memoryPeriodEdit.setDisable(true);
		memoryPeriodDelete.setDisable(true);
	}
	
	public void changeTaskCountNodeStatus() {
		switch(taskCountDistribution.getValue().toString()) {
		case "constant":
			taskCountAvgLabel.setDisable(false);
			taskCountAvg.setDisable(false);
			break;
		case "normal":
			taskCountAvgLabel.setDisable(false);
			taskCountAvg.setDisable(false);
			taskCountStdevLabel.setDisable(false);
			taskCountStdev.setDisable(false);
			taskCountMaxLabel.setDisable(false);
			taskCountMax.setDisable(false);
			taskCountMinLabel.setDisable(false);
			taskCountMin.setDisable(false);
			taskCountSeedLabel.setDisable(false);
			taskCountSeed.setDisable(false);
			break;
		case "poisson":
			taskCountAvgLabel.setDisable(false);
			taskCountAvg.setDisable(false);
			taskCountMaxLabel.setDisable(false);
			taskCountMax.setDisable(false);
			taskCountMinLabel.setDisable(false);
			taskCountMin.setDisable(false);
			taskCountSeedLabel.setDisable(false);
			taskCountSeed.setDisable(false);
			break;
		case "uniform":
			taskCountMaxLabel.setDisable(false);
			taskCountMax.setDisable(false);
			taskCountMinLabel.setDisable(false);
			taskCountMin.setDisable(false);
			taskCountSeedLabel.setDisable(false);
			taskCountSeed.setDisable(false);
			break;
		}
	}
	
	public void changeTaskLengthNodeStatus() {
		switch(taskLengthDistribution.getValue().toString()) {
		case "constant":
			taskLengthAvgLabel.setDisable(false);
			taskLengthAvg.setDisable(false);
			break;
		case "normal":
			taskLengthAvgLabel.setDisable(false);
			taskLengthAvg.setDisable(false);
			taskLengthStdevLabel.setDisable(false);
			taskLengthStdev.setDisable(false);
			taskLengthMaxLabel.setDisable(false);
			taskLengthMax.setDisable(false);
			taskLengthMinLabel.setDisable(false);
			taskLengthMin.setDisable(false);
			taskLengthSeedLabel.setDisable(false);
			taskLengthSeed.setDisable(false);
			break;
		case "poisson":
			taskLengthAvgLabel.setDisable(false);
			taskLengthAvg.setDisable(false);
			taskLengthMaxLabel.setDisable(false);
			taskLengthMax.setDisable(false);
			taskLengthMinLabel.setDisable(false);
			taskLengthMin.setDisable(false);
			taskLengthSeedLabel.setDisable(false);
			taskLengthSeed.setDisable(false);
			break;
		case "uniform":
			taskLengthMaxLabel.setDisable(false);
			taskLengthMax.setDisable(false);
			taskLengthMinLabel.setDisable(false);
			taskLengthMin.setDisable(false);
			taskLengthSeedLabel.setDisable(false);
			taskLengthSeed.setDisable(false);
			break;
		}
	}
	
	public void changeJobPackLenNodeStatus() {
		switch(jobPackLenDistribution.getValue().toString()) {
		case "constant":
			jobPackLenAvgLabel.setDisable(false);
			jobPackLenAvg.setDisable(false);
			break;
		case "normal":
			jobPackLenAvgLabel.setDisable(false);
			jobPackLenAvg.setDisable(false);
			jobPackLenStdevLabel.setDisable(false);
			jobPackLenStdev.setDisable(false);
			jobPackLenMaxLabel.setDisable(false);
			jobPackLenMax.setDisable(false);
			jobPackLenMinLabel.setDisable(false);
			jobPackLenMin.setDisable(false);
			jobPackLenSeedLabel.setDisable(false);
			jobPackLenSeed.setDisable(false);
			break;
		case "poisson":
			jobPackLenAvgLabel.setDisable(false);
			jobPackLenAvg.setDisable(false);
			jobPackLenMaxLabel.setDisable(false);
			jobPackLenMax.setDisable(false);
			jobPackLenMinLabel.setDisable(false);
			jobPackLenMin.setDisable(false);
			jobPackLenSeedLabel.setDisable(false);
			jobPackLenSeed.setDisable(false);
			break;
		case "uniform":
			jobPackLenMaxLabel.setDisable(false);
			jobPackLenMax.setDisable(false);
			jobPackLenMinLabel.setDisable(false);
			jobPackLenMin.setDisable(false);
			jobPackLenSeedLabel.setDisable(false);
			jobPackLenSeed.setDisable(false);
			break;
		}
	}
	
	public void changeJobIntervalNodeStatus() {
		switch(jobIntervalDistribution.getValue().toString()) {
		case "constant":
			jobIntervalAvgLabel.setDisable(false);
			jobIntervalAvg.setDisable(false);
			break;
		case "normal":
			jobIntervalAvgLabel.setDisable(false);
			jobIntervalAvg.setDisable(false);
			jobIntervalStdevLabel.setDisable(false);
			jobIntervalStdev.setDisable(false);
			jobIntervalMaxLabel.setDisable(false);
			jobIntervalMax.setDisable(false);
			jobIntervalMinLabel.setDisable(false);
			jobIntervalMin.setDisable(false);
			jobIntervalSeedLabel.setDisable(false);
			jobIntervalSeed.setDisable(false);
			break;
		case "poisson":
			jobIntervalAvgLabel.setDisable(false);
			jobIntervalAvg.setDisable(false);
			jobIntervalMaxLabel.setDisable(false);
			jobIntervalMax.setDisable(false);
			jobIntervalMinLabel.setDisable(false);
			jobIntervalMin.setDisable(false);
			jobIntervalSeedLabel.setDisable(false);
			jobIntervalSeed.setDisable(false);
			break;
		case "uniform":
			jobIntervalMaxLabel.setDisable(false);
			jobIntervalMax.setDisable(false);
			jobIntervalMinLabel.setDisable(false);
			jobIntervalMin.setDisable(false);
			jobIntervalSeedLabel.setDisable(false);
			jobIntervalSeed.setDisable(false);
			break;
		}
	}
	
	public void changeCpucntNodeStatus() {
		switch(cpucntDistribution.getValue().toString()) {
		case "constant":
			cpucntAvgLabel.setDisable(false);
			cpucntAvg.setDisable(false);
			break;
		case "normal":
			cpucntAvgLabel.setDisable(false);
			cpucntAvg.setDisable(false);
			cpucntStdevLabel.setDisable(false);
			cpucntStdev.setDisable(false);
			cpucntMaxLabel.setDisable(false);
			cpucntMax.setDisable(false);
			cpucntMinLabel.setDisable(false);
			cpucntMin.setDisable(false);
			cpucntSeedLabel.setDisable(false);
			cpucntSeed.setDisable(false);
			break;
		case "poisson":
			cpucntAvgLabel.setDisable(false);
			cpucntAvg.setDisable(false);
			cpucntMaxLabel.setDisable(false);
			cpucntMax.setDisable(false);
			cpucntMinLabel.setDisable(false);
			cpucntMin.setDisable(false);
			cpucntSeedLabel.setDisable(false);
			cpucntSeed.setDisable(false);
			break;
		case "uniform":
			cpucntMaxLabel.setDisable(false);
			cpucntMax.setDisable(false);
			cpucntMinLabel.setDisable(false);
			cpucntMin.setDisable(false);
			cpucntSeedLabel.setDisable(false);
			cpucntSeed.setDisable(false);
			break;
		}
	}
	
	public void changeMemoryNodeStatus() {
		switch(memoryDistribution.getValue().toString()) {
		case "constant":
			memoryAvgLabel.setDisable(false);
			memoryAvg.setDisable(false);
			break;
		case "normal":
			memoryAvgLabel.setDisable(false);
			memoryAvg.setDisable(false);
			memoryStdevLabel.setDisable(false);
			memoryStdev.setDisable(false);
			memoryMaxLabel.setDisable(false);
			memoryMax.setDisable(false);
			memoryMinLabel.setDisable(false);
			memoryMin.setDisable(false);
			memorySeedLabel.setDisable(false);
			memorySeed.setDisable(false);
			break;
		case "poisson":
			memoryAvgLabel.setDisable(false);
			memoryAvg.setDisable(false);
			memoryMaxLabel.setDisable(false);
			memoryMax.setDisable(false);
			memoryMinLabel.setDisable(false);
			memoryMin.setDisable(false);
			memorySeedLabel.setDisable(false);
			memorySeed.setDisable(false);
			break;
		case "uniform":
			memoryMaxLabel.setDisable(false);
			memoryMax.setDisable(false);
			memoryMinLabel.setDisable(false);
			memoryMin.setDisable(false);
			memorySeedLabel.setDisable(false);
			memorySeed.setDisable(false);
			break;
		}
	}
	
	/** Check MultiDistribution Combobox status	*/
	public void checkTaskCountMDBoxStatus() {
		if(taskCountMDBox.getItems().size() > 0) {
			taskCountMDEdit.setDisable(false);
			taskCountMDDelete.setDisable(false);
		} else {
			taskCountMDEdit.setDisable(true);
			taskCountMDDelete.setDisable(true);
		}
	}
	
	public void checkTaskLengthMDBoxStatus() {
		if(taskLengthMDBox.getItems().size() > 0) {
			taskLengthMDEdit.setDisable(false);
			taskLengthMDDelete.setDisable(false);
		} else {
			taskLengthMDEdit.setDisable(true);
			taskLengthMDDelete.setDisable(true);
		}
	}
	public void checkJobPackLenMDBoxStatus() {
		if(jobPackLenMDBox.getItems().size() > 0) {
			jobPackLenMDEdit.setDisable(false);
			jobPackLenMDDelete.setDisable(false);
		} else {
			jobPackLenMDEdit.setDisable(true);
			jobPackLenMDDelete.setDisable(true);
		}
	}
	public void checkJobIntervalMDBoxStatus() {
		if(jobIntervalMDBox.getItems().size() > 0) {
			jobIntervalMDEdit.setDisable(false);
			jobIntervalMDDelete.setDisable(false);
		} else {
			jobIntervalMDEdit.setDisable(true);
			jobIntervalMDDelete.setDisable(true);
		}
	}
	public void checkCpucntMDBoxStatus() {
		if(cpucntMDBox.getItems().size() > 0) {
			cpucntMDEdit.setDisable(false);
			cpucntMDDelete.setDisable(false);
		} else {
			cpucntMDEdit.setDisable(true);
			cpucntMDDelete.setDisable(true);
		}
	}
	public void checkMemoryMDBoxStatus() {
		if(memoryMDBox.getItems().size() > 0) {
			memoryMDEdit.setDisable(false);
			memoryMDDelete.setDisable(false);
		} else {
			memoryMDEdit.setDisable(true);
			memoryMDDelete.setDisable(true);
		}
	}
	
	/** Check Period Combobox status	*/
	public void checkTaskCountPeriodBoxStatus() {
		if(taskCountPeriodBox.getItems().size() > 0) {
			taskCountPeriodEdit.setDisable(false);
			taskCountPeriodDelete.setDisable(false);
		} else {
			taskCountPeriodEdit.setDisable(true);
			taskCountPeriodDelete.setDisable(true);
		}
	}
	
	public void checkTaskLengthPeriodBoxStatus() {
		if(taskLengthPeriodBox.getItems().size() > 0) {
			taskLengthPeriodEdit.setDisable(false);
			taskLengthPeriodDelete.setDisable(false);
		} else {
			taskLengthPeriodEdit.setDisable(true);
			taskLengthPeriodDelete.setDisable(true);
		}
	}

	public void checkJobPackLenPeriodBoxStatus() {
		if(jobPackLenPeriodBox.getItems().size() > 0) {
			jobPackLenPeriodEdit.setDisable(false);
			jobPackLenPeriodDelete.setDisable(false);
		} else {
			jobPackLenPeriodEdit.setDisable(true);
			jobPackLenPeriodDelete.setDisable(true);
		}
	}
	
	public void checkJobIntervalPeriodBoxStatus() {
		if(jobIntervalPeriodBox.getItems().size() > 0) {
			jobIntervalPeriodEdit.setDisable(false);
			jobIntervalPeriodDelete.setDisable(false);
		} else {
			jobIntervalPeriodEdit.setDisable(true);
			jobIntervalPeriodDelete.setDisable(true);
		}
	}
	
	public void checkCpucntPeriodBoxStatus() {
		if(cpucntPeriodBox.getItems().size() > 0) {
			cpucntPeriodEdit.setDisable(false);
			cpucntPeriodDelete.setDisable(false);
		} else {
			cpucntPeriodEdit.setDisable(true);
			cpucntPeriodDelete.setDisable(true);
		}
	}
	
	public void checkMemoryPeriodBoxStatus() {
		if(memoryPeriodBox.getItems().size() > 0) {
			memoryPeriodEdit.setDisable(false);
			memoryPeriodDelete.setDisable(false);
		} else {
			memoryPeriodEdit.setDisable(true);
			memoryPeriodDelete.setDisable(true);
		}
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
		if(checkWorkGenValue())
			generator.run(configurationOptions, workloadWrapper(), dependencyWrapper());
	}

	private Dependency dependencyWrapper() {
		return null;
	}

	private boolean checkWorkGenValue() {
		if(taskCountMDCheck.isSelected() && taskCountMDBox.getValue()==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.titleProperty().set("Warning");
            alert.headerTextProperty().set("MultiDistribution can not be null!");
            alert.showAndWait();
            return false;
		}
		if(taskCountPeriodCheck.isSelected() && taskCountPeriodBox.getValue()==null) {
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.titleProperty().set("Warning");
            alert.headerTextProperty().set("PeriodDistribution can not be null!");
            alert.showAndWait();
            return false;
		}
		return true;
	}

	/** TaskCount Buttons	*/
	@FXML
    void taskCountMDAddClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("ADD", "TaskCount");
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void taskCountMDEditClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("EDIT", "TaskCount");
        
        multiDistController.loadDistributionData(this.getTaskCountMDBoxIndex());
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void taskCountMDDeleteClick(ActionEvent event) throws Exception {
		taskCountListMD.removeDistAt(this.getTaskCountMDBoxIndex());
		taskCountMDBox.getItems().remove(this.getTaskCountMDBoxIndex());
		checkTaskCountMDBoxStatus();
    }
	
	public int getTaskCountMDBoxIndex() {
        int index = 0;
        for (int i = 0; i < taskCountMDBox.getItems().size();i++) {
        	if(this.taskCountMDBox.getValue().equals(taskCountMDBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}

	@FXML
    void taskCountPeriodAddClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        
        periodDistController.setFlag("ADD", "TaskCount");
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void taskCountPeriodEditClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        // Show the scene containing the main layout.
        periodDistController.setFlag("EDIT", "TaskCount");
        
        periodDistController.loadDistributionData(this.getTaskCountPeriodBoxIndex());
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void taskCountPeriodDeleteClick(ActionEvent event) throws IOException {
		taskCountListRP.removePeriodicValidValuesAt(this.getTaskCountPeriodBoxIndex());
		taskCountPeriodBox.getItems().remove(this.getTaskCountPeriodBoxIndex());
		checkTaskCountPeriodBoxStatus();
    }
	
	public int getTaskCountPeriodBoxIndex() {
        int index = 0;
        for (int i = 0; i < taskCountPeriodBox.getItems().size();i++) {
        	if(this.taskCountPeriodBox.getValue().equals(taskCountPeriodBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	
	/** TaskLength Buttons	*/
	@FXML
    void taskLengthMDAddClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("ADD", "TaskLength");
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void taskLengthMDEditClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("EDIT", "TaskLength");
        
        multiDistController.loadDistributionData(this.getTaskLengthMDBoxIndex());
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void taskLengthMDDeleteClick(ActionEvent event) throws Exception {
		taskLengthListMD.removeDistAt(this.getTaskLengthMDBoxIndex());
		taskLengthMDBox.getItems().remove(this.getTaskLengthMDBoxIndex());
		checkTaskLengthMDBoxStatus();
    }
	
	public int getTaskLengthMDBoxIndex() {
        int index = 0;
        for (int i = 0; i < taskLengthMDBox.getItems().size();i++) {
        	if(this.taskLengthMDBox.getValue().equals(taskLengthMDBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	@FXML
    void taskLengthPeriodAddClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        
        periodDistController.setFlag("ADD", "TaskLength");
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void taskLengthPeriodEditClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        // Show the scene containing the main layout.
        periodDistController.setFlag("EDIT", "TaskLength");
        
        periodDistController.loadDistributionData(this.getTaskLengthPeriodBoxIndex());
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void taskLengthPeriodDeleteClick(ActionEvent event) throws IOException {
		taskLengthListRP.removePeriodicValidValuesAt(this.getTaskLengthPeriodBoxIndex());
		taskLengthPeriodBox.getItems().remove(this.getTaskLengthPeriodBoxIndex());
		checkTaskLengthPeriodBoxStatus();
    }
	
	public int getTaskLengthPeriodBoxIndex() {
        int index = 0;
        for (int i = 0; i < taskLengthPeriodBox.getItems().size();i++) {
        	if(this.taskLengthPeriodBox.getValue().equals(taskLengthPeriodBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	/** JobPackageLength Buttons	*/
	
	@FXML
    void jobPackLenMDAddClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("ADD", "JobPackageLength");
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void jobPackLenMDEditClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("EDIT", "JobPackageLength");
        
        multiDistController.loadDistributionData(this.getJobPackLenMDBoxIndex());
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void jobPackLenMDDeleteClick(ActionEvent event) throws Exception {
		jobPackLenListMD.removeDistAt(this.getJobPackLenMDBoxIndex());
		jobPackLenMDBox.getItems().remove(this.getJobPackLenMDBoxIndex());
		checkJobPackLenMDBoxStatus();
    }
	
	public int getJobPackLenMDBoxIndex() {
        int index = 0;
        for (int i = 0; i < jobPackLenMDBox.getItems().size();i++) {
        	if(this.jobPackLenMDBox.getValue().equals(jobPackLenMDBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	@FXML
    void jobPackLenPeriodAddClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        
        periodDistController.setFlag("ADD", "JobPackageLength");
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void jobPackLenPeriodEditClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        // Show the scene containing the main layout.
        periodDistController.setFlag("EDIT", "JobPackageLength");
        
        periodDistController.loadDistributionData(this.getJobPackLenPeriodBoxIndex());
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void jobPackLenPeriodDeleteClick(ActionEvent event) throws IOException {
		jobPackLenListRP.removePeriodicValidValuesAt(this.getJobPackLenPeriodBoxIndex());
		jobPackLenPeriodBox.getItems().remove(this.getJobPackLenPeriodBoxIndex());
		checkJobPackLenPeriodBoxStatus();
    }
	
	public int getJobPackLenPeriodBoxIndex() {
        int index = 0;
        for (int i = 0; i < jobPackLenPeriodBox.getItems().size();i++) {
        	if(this.jobPackLenPeriodBox.getValue().equals(jobPackLenPeriodBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	/** JobInterval Buttons		*/
	@FXML
    void jobIntervalMDAddClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("ADD", "JobInterval");
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void jobIntervalMDEditClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("EDIT", "JobInterval");
        
        multiDistController.loadDistributionData(this.getJobIntervalMDBoxIndex());
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void jobIntervalMDDeleteClick(ActionEvent event) throws Exception {
		jobIntervalListMD.removeDistAt(this.getJobIntervalMDBoxIndex());
		jobIntervalMDBox.getItems().remove(this.getJobIntervalMDBoxIndex());
		checkJobIntervalMDBoxStatus();
    }
	
	public int getJobIntervalMDBoxIndex() {
        int index = 0;
        for (int i = 0; i < jobIntervalMDBox.getItems().size();i++) {
        	if(this.jobIntervalMDBox.getValue().equals(jobIntervalMDBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	@FXML
    void jobIntervalPeriodAddClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        
        periodDistController.setFlag("ADD", "JobInterval");
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void jobIntervalPeriodEditClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        // Show the scene containing the main layout.
        periodDistController.setFlag("EDIT", "JobInterval");
        
        periodDistController.loadDistributionData(this.getJobIntervalPeriodBoxIndex());
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void jobIntervalPeriodDeleteClick(ActionEvent event) throws IOException {
		jobIntervalListRP.removePeriodicValidValuesAt(this.getJobIntervalPeriodBoxIndex());
		jobIntervalPeriodBox.getItems().remove(this.getJobIntervalPeriodBoxIndex());
		checkJobIntervalPeriodBoxStatus();
    }
	
	public int getJobIntervalPeriodBoxIndex() {
        int index = 0;
        for (int i = 0; i < jobIntervalPeriodBox.getItems().size();i++) {
        	if(this.jobIntervalPeriodBox.getValue().equals(jobIntervalPeriodBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	/** CpuCount Buttons	*/
	
	@FXML
    void cpucntMDAddClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("ADD", "CpuCount");
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void cpucntMDEditClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("EDIT", "CpuCount");
        
        multiDistController.loadDistributionData(this.getCpucntMDBoxIndex());
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void cpucntMDDeleteClick(ActionEvent event) throws Exception {
		cpucntListMD.removeDistAt(this.getCpucntMDBoxIndex());
		cpucntMDBox.getItems().remove(this.getCpucntMDBoxIndex());
		checkCpucntMDBoxStatus();
    }
	
	public int getCpucntMDBoxIndex() {
        int index = 0;
        for (int i = 0; i < cpucntMDBox.getItems().size();i++) {
        	if(this.cpucntMDBox.getValue().equals(cpucntMDBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	@FXML
    void cpucntPeriodAddClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        
        periodDistController.setFlag("ADD", "CpuCount");
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void cpucntPeriodEditClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        // Show the scene containing the main layout.
        periodDistController.setFlag("EDIT", "CpuCount");
        
        periodDistController.loadDistributionData(this.getCpucntPeriodBoxIndex());
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void cpucntPeriodDeleteClick(ActionEvent event) throws IOException {
		cpucntListRP.removePeriodicValidValuesAt(this.getCpucntPeriodBoxIndex());
		cpucntPeriodBox.getItems().remove(this.getCpucntPeriodBoxIndex());
		checkCpucntPeriodBoxStatus();
    }
	
	public int getCpucntPeriodBoxIndex() {
        int index = 0;
        for (int i = 0; i < cpucntPeriodBox.getItems().size();i++) {
        	if(this.cpucntPeriodBox.getValue().equals(cpucntPeriodBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	/** Memory Buttons		*/

	@FXML
    void memoryMDAddClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("ADD", "Memory");
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void memoryMDEditClick(ActionEvent event) throws Exception {

        // Show the scene containing the main layout.
        Scene scene = new Scene(loadMultiDistViewFXML());
        
        multiDistController.setFlag("EDIT", "Memory");
        
        multiDistController.loadDistributionData(this.getMemoryMDBoxIndex());
        
        Stage MDStage = new Stage();
		MDStage.setTitle("MultiDistribution Settings");
        MDStage.setScene(scene);
        MDStage.initModality(Modality.APPLICATION_MODAL);
        MDStage.show();
    }
	
	@FXML
    void memoryMDDeleteClick(ActionEvent event) throws Exception {
		memoryListMD.removeDistAt(this.getMemoryMDBoxIndex());
		memoryMDBox.getItems().remove(this.getMemoryMDBoxIndex());
		checkMemoryMDBoxStatus();
    }
	
	public int getMemoryMDBoxIndex() {
        int index = 0;
        for (int i = 0; i < memoryMDBox.getItems().size();i++) {
        	if(this.memoryMDBox.getValue().equals(memoryMDBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	@FXML
    void memoryPeriodAddClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        
        periodDistController.setFlag("ADD", "Memory");
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void memoryPeriodEditClick(ActionEvent event) throws IOException {
        
        // Show the scene containing the main layout.
        Scene scene = new Scene(loadPeriodDistViewFXML());
        // Show the scene containing the main layout.
        periodDistController.setFlag("EDIT", "Memory");
        
        periodDistController.loadDistributionData(this.getMemoryPeriodBoxIndex());
        
		Stage periodStage = new Stage();
		periodStage.setTitle("PeriodDistribution Settings");
        periodStage.setScene(scene);
        periodStage.initModality(Modality.APPLICATION_MODAL);
        periodStage.show();
    }
	
	@FXML
    void memoryPeriodDeleteClick(ActionEvent event) throws IOException {
		memoryListRP.removePeriodicValidValuesAt(this.getMemoryPeriodBoxIndex());
		memoryPeriodBox.getItems().remove(this.getMemoryPeriodBoxIndex());
		checkMemoryPeriodBoxStatus();
    }
	
	public int getMemoryPeriodBoxIndex() {
        int index = 0;
        for (int i = 0; i < memoryPeriodBox.getItems().size();i++) {
        	if(this.memoryPeriodBox.getValue().equals(memoryPeriodBox.getItems().get(i))) {
        		index = i;
        		break;
        	}
        }
        return index;
	}
	/**********************************/
	
	private AnchorPane loadMultiDistViewFXML() throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApplication.class.getResource("/view/MultiDistributionView.fxml"));
        AnchorPane mainLayout = (AnchorPane) loader.load();
        multiDistController = loader.getController();
        multiDistController.init(this);
        return mainLayout;
	}
	
	private AnchorPane loadPeriodDistViewFXML() throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApplication.class.getResource("/view/PeriodDistributionView.fxml"));
        AnchorPane mainLayout = (AnchorPane) loader.load();
        periodDistController = loader.getController();
        periodDistController.init(this);
        return mainLayout;
	}
	
	private WorkloadConfiguration workloadWrapper() {

		// SimulationStartTime
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		Date simulationStartTime = null;
		try {
			simulationStartTime = simpleDateFormat.parse(this.simulationStartTime.getText());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// JobCount
		JobCount jobCount = new JobCount();
		jobCount.setAvg(Double.parseDouble(this.jobCount.getText()));
		jobCount.setDistribution(ParameterAttributesDistributionType.valueOf("constant"));

		// TaskCount
		TaskCount taskCount = new TaskCount();
		taskCount.setDistribution(
				ParameterAttributesDistributionType.valueOf(this.taskCountDistribution.getValue().toString()));
		taskCount.setAvg(Double.parseDouble(this.taskCountAvg.getText()));
		if(this.taskCountStdev.getText()!=null && !this.taskCountStdev.getText().equals(""))
			taskCount.setStdev(Double.parseDouble(this.taskCountStdev.getText()));
		if(this.taskCountMax.getText()!=null && !this.taskCountMax.getText().equals(""))	
			taskCount.setMax(Double.parseDouble(this.taskCountMax.getText()));
		if(this.taskCountMin.getText()!=null && !this.taskCountMin.getText().equals(""))
			taskCount.setMin(Double.parseDouble(this.taskCountMin.getText()));
		if(this.taskCountSeed.getText()!=null && !this.taskCountSeed.getText().equals(""))
			taskCount.setSeed(Long.parseLong(this.taskCountSeed.getText()));
		if(this.taskCountMDCheck.isSelected() && taskCountListMD!= null) {
			taskCount.setMultiDistribution(taskCountListMD);
		}
		if(this.taskCountRefCheck.isSelected()) {
			 if(this.taskCountRefBox.getValue()!= null)
				 taskCount.setRefElementId(this.taskCountRefBox.getValue().toString());
			 if(this.taskCountExprText.getText()!=null && !this.taskCountExprText.equals(""))
				 taskCount.setExpr(this.taskCountExprText.getText());
		}
			
		if(this.taskCountListMD.getDist().length!=0 && this.taskCountMDCheck.isSelected())
			taskCount.setMultiDistribution(taskCountListMD);
		if(this.taskCountListRP.getPeriodicValidValues().length!=0 && this.taskCountPeriodCheck.isSelected())
			taskCount.setRandParamsSequence(taskCountListRP);
		
		// TaskLength
		TaskLength taskLength = new TaskLength();
		taskLength.setDistribution(
				ParameterAttributesDistributionType.valueOf(this.taskLengthDistribution.getValue().toString()));
		if(this.taskLengthAvg.getText()!=null && !this.taskLengthAvg.getText().equals(""))
			taskLength.setAvg(Double.parseDouble(this.taskLengthAvg.getText()));
		if(this.taskLengthStdev.getText()!=null && !this.taskLengthStdev.getText().equals(""))
			taskLength.setStdev(Double.parseDouble(this.taskLengthStdev.getText()));
		if(this.taskLengthMax.getText()!=null && !this.taskLengthMax.getText().equals(""))
			taskLength.setMax(Double.parseDouble(this.taskLengthMax.getText()));
		if(this.taskLengthMin.getText()!=null && !this.taskLengthMin.getText().equals(""))	
			taskLength.setMin(Double.parseDouble(this.taskLengthMin.getText()));
		if(this.taskLengthSeed.getText()!=null && !this.taskLengthSeed.getText().equals(""))
			taskLength.setSeed(Long.parseLong(this.taskLengthSeed.getText()));
		if(this.taskLengthListMD.getDist().length!=0 && this.taskLengthMDCheck.isSelected()) {
			taskLength.setMultiDistribution(taskLengthListMD);
		}
		if(this.taskLengthListRP.getPeriodicValidValues().length!=0 && this.taskLengthPeriodCheck.isSelected()) {
			taskLength.setRandParamsSequence(taskLengthListRP);
		}
		
		// JobPackageLength
		JobPackageLength jobPackLen = new JobPackageLength();
		jobPackLen.setDistribution(
				ParameterAttributesDistributionType.valueOf(this.jobPackLenDistribution.getValue().toString()));
		if(this.jobPackLenAvg.getText()!=null && !this.jobPackLenAvg.getText().equals(""))
			jobPackLen.setAvg(Double.parseDouble(this.jobPackLenAvg.getText()));
		if(this.jobPackLenStdev.getText()!=null && !this.jobPackLenStdev.getText().equals(""))
			jobPackLen.setStdev(Double.parseDouble(this.jobPackLenStdev.getText()));
		if(this.jobPackLenMax.getText()!=null && !this.jobPackLenMax.getText().equals(""))
			jobPackLen.setMax(Double.parseDouble(this.jobPackLenMax.getText()));
		if(this.jobPackLenMin.getText()!=null && !this.jobPackLenMin.getText().equals(""))
			jobPackLen.setMin(Double.parseDouble(this.jobPackLenMin.getText()));
		if(this.jobPackLenSeed.getText()!=null && !this.jobPackLenSeed.getText().equals(""))
			jobPackLen.setSeed(Long.parseLong(this.jobPackLenSeed.getText()));
		if(this.jobPackLenListMD.getDist().length!=0 && this.jobPackLenMDCheck.isSelected()) {
			jobPackLen.setMultiDistribution(jobPackLenListMD);
		}
		if(this.jobPackLenListRP.getPeriodicValidValues().length!=0 && this.jobPackLenPeriodCheck.isSelected()) {
			jobPackLen.setRandParamsSequence(jobPackLenListRP);
		}
		
		// JobInterval
		JobInterval jobInterval = new JobInterval();
		jobInterval.setDistribution(
				ParameterAttributesDistributionType.valueOf(this.jobIntervalDistribution.getValue().toString()));
		if(this.jobIntervalAvg.getText()!=null && !this.jobIntervalAvg.getText().equals(""))
			jobInterval.setAvg(Double.parseDouble(this.jobIntervalAvg.getText()));
		if(this.jobIntervalStdev.getText()!=null && !this.jobIntervalStdev.getText().equals(""))
			jobInterval.setStdev(Double.parseDouble(this.jobIntervalStdev.getText()));
		if(this.jobIntervalMax.getText()!=null && !this.jobIntervalMax.getText().equals(""))
			jobInterval.setMax(Double.parseDouble(this.jobIntervalMax.getText()));
		if(this.jobIntervalMin.getText()!=null && !this.jobIntervalMin.getText().equals(""))
			jobInterval.setMin(Double.parseDouble(this.jobIntervalMin.getText()));
		if(this.jobIntervalSeed.getText()!=null && !this.jobIntervalSeed.getText().equals(""))
			jobInterval.setSeed(Long.parseLong(this.jobIntervalSeed.getText()));
		if(this.jobIntervalListMD.getDist().length!=0 && this.jobIntervalMDCheck.isSelected()) {
			jobInterval.setMultiDistribution(jobIntervalListMD);
		}
		if(this.jobIntervalListRP.getPeriodicValidValues().length!=0 && this.jobIntervalPeriodCheck.isSelected()) {
			jobInterval.setRandParamsSequence(jobIntervalListRP);
		}
		
		// ComputingResourceHostParameter
		// cpucount
		ComputingResourceHostParameter cpucnt = new ComputingResourceHostParameter();
		cpucnt.setMetric("cpucount");
		Value cpucntValue = new Value();
		cpucntValue.setId("cpucnt");
		cpucntValue.setDistribution(ParameterAttributesDistributionType.valueOf(this.cpucntDistribution.getValue().toString()));
		if(this.cpucntAvg.getText()!=null && !this.cpucntAvg.getText().equals(""))
			cpucntValue.setAvg(Double.parseDouble(this.cpucntAvg.getText()));
		if(this.cpucntStdev.getText()!=null && !this.cpucntStdev.getText().equals(""))
			cpucntValue.setStdev(Double.parseDouble(this.cpucntStdev.getText()));
		if(this.cpucntMax.getText()!=null && !this.cpucntMax.getText().equals(""))
			cpucntValue.setMax(Double.parseDouble(this.cpucntMax.getText()));
		if(this.cpucntMin.getText()!=null && !this.cpucntMin.getText().equals(""))
			cpucntValue.setMin(Double.parseDouble(this.cpucntMin.getText()));
		if(this.cpucntSeed.getText()!=null && !this.cpucntSeed.getText().equals(""))
			cpucntValue.setSeed(Long.parseLong(this.cpucntSeed.getText()));
		if(this.cpucntListMD.getDist().length!=0 && this.cpucntMDCheck.isSelected()) {
			cpucntValue.setMultiDistribution(cpucntListMD);
		}
		if(this.cpucntListRP.getPeriodicValidValues().length!=0 && this.cpucntPeriodCheck.isSelected()) {
			cpucntValue.setRandParamsSequence(cpucntListRP);
		}
		cpucnt.addValue(cpucntValue);
		
		// memory
		ComputingResourceHostParameter memory = new ComputingResourceHostParameter();
		memory.setMetric("memory");
		Value memoryValue = new Value();
		memoryValue.setId("memory");
		memoryValue.setDistribution(ParameterAttributesDistributionType.valueOf(this.cpucntDistribution.getValue().toString()));
		if(this.memoryAvg.getText()!=null && !this.memoryAvg.getText().equals(""))
			memoryValue.setAvg(Double.parseDouble(this.memoryAvg.getText()));
		if(this.memoryStdev.getText()!=null && !this.memoryStdev.getText().equals(""))
			memoryValue.setStdev(Double.parseDouble(this.memoryStdev.getText()));
		if(this.memoryMax.getText()!=null && !this.memoryMax.getText().equals(""))
			memoryValue.setMax(Double.parseDouble(this.memoryMax.getText()));
		if(this.memoryMin.getText()!=null && !this.memoryMin.getText().equals(""))
			memoryValue.setMin(Double.parseDouble(this.memoryMin.getText()));
		if(this.memorySeed.getText()!=null && !this.memorySeed.getText().equals(""))
			memoryValue.setSeed(Long.parseLong(this.memorySeed.getText()));
		if(this.memoryListMD.getDist().length!=0 && this.memoryMDCheck.isSelected()) {
			memoryValue.setMultiDistribution(memoryListMD);
		}
		if(this.memoryListRP.getPeriodicValidValues().length!=0 && this.memoryPeriodCheck.isSelected()) {
			memoryValue.setRandParamsSequence(memoryListRP);
		}
		memory.addValue(memoryValue);
		
		/** WorkloadConfiguration */
		WorkloadConfiguration workload = new WorkloadConfiguration();
		workload.setSimulationStartTime(simulationStartTime);

		WorkloadConfigurationChoice workloadConfigurationChoice = new WorkloadConfigurationChoice();
		workloadConfigurationChoice.setJobCount(jobCount);
		workload.setWorkloadConfigurationChoice(workloadConfigurationChoice);

		workload.setTaskCount(taskCount);
		workload.setTaskLength(taskLength);
		workload.setJobPackageLength(jobPackLen);
		workload.setJobInterval(jobInterval);
		
		workload.addComputingResourceHostParameter(cpucnt);
		workload.addComputingResourceHostParameter(memory);
		
		return workload;

	}
}

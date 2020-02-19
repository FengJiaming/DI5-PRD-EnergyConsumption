package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import app.ConfigurationOptions;
import controller.workload.generator.WorkloadGenerator;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import simulator.workload.generator.configuration.JobCount;
import simulator.workload.generator.configuration.TaskCount;
import simulator.workload.generator.configuration.WorkloadConfiguration;
import simulator.workload.generator.configuration.WorkloadConfigurationChoice;
import simulator.workload.generator.configuration.types.ParameterAttributesDistributionType;

public class WorkGenViewController {

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
	private CheckBox taskCountRefCheck;
	@FXML
	private Label taskCountRefLabel;
	@FXML
	private ComboBox<?> taskCountRefBox;

	@FXML
	private Label taskCountExprLabel;
	@FXML
	private TextField taskCountExprText;

	@FXML
	private Label taskCountDistLabel;
	@FXML
	private ComboBox<?> taskCountDistribution;
	@FXML
	private Label taskCountAvgLabel;
	@FXML
	private TextField taskCountAvg;

	@FXML
	private Label taskCountStdevLabel;
	@FXML
	private TextField taskCountStdev;

	@FXML
	private Label taskCountMaxLabel;
	@FXML
	private TextField taskCountMax;
	@FXML
	private Label taskCountMinLabel;
	@FXML
	private TextField taskCountMin;
	@FXML
	private Label taskCountSeedLabel;
	@FXML
	private TextField taskCountSeed;
	@FXML
	private CheckBox taskCountMDCheck;
	@FXML
	private ComboBox<?> taskCountMDBox;
	@FXML
	private Button taskCountMDButton;
	@FXML
	private CheckBox taskCountPeriodCheck;
	@FXML
	private ComboBox<?> taskCountPeriodBox;
	@FXML
	private Button taskCountPeriodButton;
	/** --------------------------------------------------------- */

	/** TaskLength */
	@FXML
	private CheckBox taskLengthRefCheck;
	@FXML
	private Label taskLengthRefLabel;
	@FXML
	private ComboBox<?> taskLengthRefBox;

	@FXML
	private Label taskLengthExprLabel;
	@FXML
	private TextField taskLengthExprText;

	@FXML
	private Label taskLengthDistLabel;
	@FXML
	private ComboBox<?> taskLengthDistribution;
	@FXML
	private Label taskLengthAvgLabel;
	@FXML
	private TextField taskLengthAvg;

	@FXML
	private Label taskLengthStdevLabel;
	@FXML
	private TextField taskLengthStdev;

	@FXML
	private Label taskLengthMaxLabel;
	@FXML
	private TextField taskLengthMax;
	@FXML
	private Label taskLengthMinLabel;
	@FXML
	private TextField taskLengthMin;
	@FXML
	private Label taskLengthSeedLabel;
	@FXML
	private TextField taskLengthSeed;
	@FXML
	private CheckBox taskLengthMDCheck;
	@FXML
	private ComboBox<?> taskLengthMDBox;
	@FXML
	private Button taskLengthMDButton;
	@FXML
	private CheckBox taskLengthPeriodCheck;
	@FXML
	private ComboBox<?> taskLengthPeriodBox;
	@FXML
	private Button taskLengthPeriodButton;
	/*** ---------------------------------------------------- */
	/** JobPackageLength */
	@FXML
	private CheckBox jobPackLenRefCheck;
	@FXML
	private Label jobPackLenRefLabel;
	@FXML
	private ComboBox<?> jobPackLenRefBox;

	@FXML
	private Label jobPackLenExprLabel;
	@FXML
	private TextField jobPackLenExprText;

	@FXML
	private Label jobPackLenDistLabel;
	@FXML
	private ComboBox<?> jobPackLenDistribution;
	@FXML
	private Label jobPackLenAvgLabel;
	@FXML
	private TextField jobPackLenAvg;

	@FXML
	private Label jobPackLenStdevLabel;
	@FXML
	private TextField jobPackLenStdev;

	@FXML
	private Label jobPackLenMaxLabel;
	@FXML
	private TextField jobPackLenMax;
	@FXML
	private Label jobPackLenMinLabel;
	@FXML
	private TextField jobPackLenMin;
	@FXML
	private Label jobPackLenSeedLabel;
	@FXML
	private TextField jobPackLenSeed;
	@FXML
	private CheckBox jobPackLenMDCheck;
	@FXML
	private ComboBox<?> jobPackLenMDBox;
	@FXML
	private Button jobPackLenMDButton;
	@FXML
	private CheckBox jobPackLenPeriodCheck;
	@FXML
	private ComboBox<?> jobPackLenPeriodBox;
	@FXML
	private Button jobPackLenPeriodButton;
	/** ------------------------------------------------- */
	/** JoaInterval */
	@FXML
	private CheckBox jobIntervalRefCheck;
	@FXML
	private Label jobIntervalRefLabel;
	@FXML
	private ComboBox<?> jobIntervalRefBox;

	@FXML
	private Label jobIntervalExprLabel;
	@FXML
	private TextField jobIntervalExprText;

	@FXML
	private Label jobIntervalDistLabel;
	@FXML
	private ComboBox<?> jobIntervalDistribution;
	@FXML
	private Label jobIntervalAvgLabel;
	@FXML
	private TextField jobIntervalAvg;

	@FXML
	private Label jobIntervalStdevLabel;
	@FXML
	private TextField jobIntervalStdev;

	@FXML
	private Label jobIntervalMaxLabel;
	@FXML
	private TextField jobIntervalMax;
	@FXML
	private Label jobIntervalMinLabel;
	@FXML
	private TextField jobIntervalMin;
	@FXML
	private Label jobIntervalSeedLabel;
	@FXML
	private TextField jobIntervalSeed;
	@FXML
	private CheckBox jobIntervalMDCheck;
	@FXML
	private ComboBox<?> jobIntervalMDBox;
	@FXML
	private Button jobIntervalMDButton;
	@FXML
	private CheckBox jobIntervalPeriodCheck;
	@FXML
	private ComboBox<?> jobIntervalPeriodBox;
	@FXML
	private Button jobIntervalPeriodButton;
	/** ------------------------------------------------- */
	/** CpuCount */
	@FXML
	private CheckBox cpucntRefCheck;
	@FXML
	private Label cpucntRefLabel;
	@FXML
	private ComboBox<?> cpucntRefBox;

	@FXML
	private Label cpucntExprLabel;
	@FXML
	private TextField cpucntExprText;

	@FXML
	private Label cpucntDistLabel;
	@FXML
	private ComboBox<?> cpucntDistribution;
	@FXML
	private Label cpucntAvgLabel;
	@FXML
	private TextField cpucntAvg;

	@FXML
	private Label cpucntStdevLabel;
	@FXML
	private TextField cpucntStdev;

	@FXML
	private Label cpucntMaxLabel;
	@FXML
	private TextField cpucntMax;
	@FXML
	private Label cpucntMinLabel;
	@FXML
	private TextField cpucntMin;
	@FXML
	private Label cpucntSeedLabel;
	@FXML
	private TextField cpucntSeed;
	@FXML
	private CheckBox cpucntMDCheck;
	@FXML
	private ComboBox<?> cpucntMDBox;
	@FXML
	private Button cpucntMDButton;
	@FXML
	private CheckBox cpucntPeriodCheck;
	@FXML
	private ComboBox<?> cpucntPeriodBox;
	@FXML
	private Button cpucntPeriodButton;
	/** ------------------------------------------------- */
	/** Memory */
	@FXML
	private CheckBox memoryRefCheck;
	@FXML
	private Label memoryRefLabel;
	@FXML
	private ComboBox<?> memoryRefBox;

	@FXML
	private Label memoryExprLabel;
	@FXML
	private TextField memoryExprText;

	@FXML
	private Label memoryDistLabel;
	@FXML
	private ComboBox<?> memoryDistribution;
	@FXML
	private Label memoryAvgLabel;
	@FXML
	private TextField memoryAvg;

	@FXML
	private Label memoryStdevLabel;
	@FXML
	private TextField memoryStdev;

	@FXML
	private Label memoryMaxLabel;
	@FXML
	private TextField memoryMax;
	@FXML
	private Label memoryMinLabel;
	@FXML
	private TextField memoryMin;
	@FXML
	private Label memorySeedLabel;
	@FXML
	private TextField memorySeed;
	@FXML
	private CheckBox memoryMDCheck;
	@FXML
	private ComboBox<?> memoryMDBox;
	@FXML
	private Button memoryMDButton;
	@FXML
	private CheckBox memoryPeriodCheck;
	@FXML
	private ComboBox<?> memoryPeriodBox;
	@FXML
	private Button memoryPeriodButton;
	/** ------------------------------------------------- */
	
	private Object[] functionList;

	public void init(Object[] functionList) {
		this.functionList = functionList;
		/** for debug */
		this.outputFolder.setText("example/workload");
		this.workloadFilename.setText("workload.swf");

		this.simulationStartTime.setText("2009-01-15_10:00:00");
		this.jobCount.setText("100");
		this.taskCountAvg.setText("1");

		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (event.getSource() instanceof CheckBox) {
					CheckBox checkbox = (CheckBox) event.getSource();
					switch (checkbox.getId()) {
					case "taskCountRefCheck":
						if (checkbox.isSelected()) {
							taskCountRefBox.setDisable(false);
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
							taskCountMDButton.setDisable(true);

							taskCountPeriodCheck.setDisable(true);
							taskCountPeriodBox.setDisable(true);
							taskCountPeriodButton.setDisable(true);
						} else {
							taskCountDistLabel.setDisable(false);
							taskCountDistribution.setDisable(false);
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

							taskCountMDCheck.setDisable(false);
							taskCountMDBox.setDisable(false);
							taskCountMDButton.setDisable(false);

							taskCountPeriodCheck.setDisable(false);
							taskCountPeriodBox.setDisable(false);
							taskCountPeriodButton.setDisable(false);
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

							taskCountPeriodCheck.setDisable(true);
							taskCountPeriodBox.setDisable(true);
							taskCountPeriodButton.setDisable(true);
						} else {
							taskCountRefCheck.setDisable(false);
							taskCountRefLabel.setDisable(false);
							taskCountRefBox.setDisable(false);
							taskCountExprLabel.setDisable(false);
							taskCountExprText.setDisable(false);

							taskCountDistLabel.setDisable(false);
							taskCountDistribution.setDisable(false);
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

							taskCountPeriodCheck.setDisable(false);
							taskCountPeriodBox.setDisable(false);
							taskCountPeriodButton.setDisable(false);
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
							taskCountMDButton.setDisable(true);
						} else {
							taskCountRefCheck.setDisable(false);
							taskCountRefLabel.setDisable(false);
							taskCountRefBox.setDisable(false);
							taskCountExprLabel.setDisable(false);
							taskCountExprText.setDisable(false);

							taskCountMDCheck.setDisable(false);
							taskCountMDBox.setDisable(false);
							taskCountMDButton.setDisable(false);
						}
						break;
					case "taskLengthRefCheck":
						if (checkbox.isSelected()) {
							taskLengthRefBox.setDisable(false);
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
							taskLengthMDButton.setDisable(true);

							taskLengthPeriodCheck.setDisable(true);
							taskLengthPeriodBox.setDisable(true);
							taskLengthPeriodButton.setDisable(true);
						} else {
							taskLengthDistLabel.setDisable(false);
							taskLengthDistribution.setDisable(false);
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

							taskLengthMDCheck.setDisable(false);
							taskLengthMDBox.setDisable(false);
							taskLengthMDButton.setDisable(false);

							taskLengthPeriodCheck.setDisable(false);
							taskLengthPeriodBox.setDisable(false);
							taskLengthPeriodButton.setDisable(false);
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

							taskLengthPeriodCheck.setDisable(true);
							taskLengthPeriodBox.setDisable(true);
							taskLengthPeriodButton.setDisable(true);
						} else {
							taskLengthRefCheck.setDisable(false);
							taskLengthRefLabel.setDisable(false);
							taskLengthRefBox.setDisable(false);
							taskLengthExprLabel.setDisable(false);
							taskLengthExprText.setDisable(false);

							taskLengthDistLabel.setDisable(false);
							taskLengthDistribution.setDisable(false);
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

							taskLengthPeriodCheck.setDisable(false);
							taskLengthPeriodBox.setDisable(false);
							taskLengthPeriodButton.setDisable(false);
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
							taskLengthMDButton.setDisable(true);
						} else {
							taskLengthRefCheck.setDisable(false);
							taskLengthRefLabel.setDisable(false);
							taskLengthRefBox.setDisable(false);
							taskLengthExprLabel.setDisable(false);
							taskLengthExprText.setDisable(false);

							taskLengthMDCheck.setDisable(false);
							taskLengthMDBox.setDisable(false);
							taskLengthMDButton.setDisable(false);
						}
						break;
					case "jobPackLenRefCheck":
						if (checkbox.isSelected()) {
							jobPackLenRefBox.setDisable(false);
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
							jobPackLenMDButton.setDisable(true);

							jobPackLenPeriodCheck.setDisable(true);
							jobPackLenPeriodBox.setDisable(true);
							jobPackLenPeriodButton.setDisable(true);
						} else {
							jobPackLenDistLabel.setDisable(false);
							jobPackLenDistribution.setDisable(false);
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

							jobPackLenMDCheck.setDisable(false);
							jobPackLenMDBox.setDisable(false);
							jobPackLenMDButton.setDisable(false);

							jobPackLenPeriodCheck.setDisable(false);
							jobPackLenPeriodBox.setDisable(false);
							jobPackLenPeriodButton.setDisable(false);
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

							jobPackLenPeriodCheck.setDisable(true);
							jobPackLenPeriodBox.setDisable(true);
							jobPackLenPeriodButton.setDisable(true);
						} else {
							jobPackLenRefCheck.setDisable(false);
							jobPackLenRefLabel.setDisable(false);
							jobPackLenRefBox.setDisable(false);
							jobPackLenExprLabel.setDisable(false);
							jobPackLenExprText.setDisable(false);

							jobPackLenDistLabel.setDisable(false);
							jobPackLenDistribution.setDisable(false);
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

							jobPackLenPeriodCheck.setDisable(false);
							jobPackLenPeriodBox.setDisable(false);
							jobPackLenPeriodButton.setDisable(false);
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
							jobPackLenMDButton.setDisable(true);
						} else {
							jobPackLenRefCheck.setDisable(false);
							jobPackLenRefLabel.setDisable(false);
							jobPackLenRefBox.setDisable(false);
							jobPackLenExprLabel.setDisable(false);
							jobPackLenExprText.setDisable(false);

							jobPackLenMDCheck.setDisable(false);
							jobPackLenMDBox.setDisable(false);
							jobPackLenMDButton.setDisable(false);
						}
						break;
					case "jobIntervalRefCheck":
						if (checkbox.isSelected()) {
							jobIntervalRefBox.setDisable(false);
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
							jobIntervalMDButton.setDisable(true);

							jobIntervalPeriodCheck.setDisable(true);
							jobIntervalPeriodBox.setDisable(true);
							jobIntervalPeriodButton.setDisable(true);
						} else {
							jobIntervalDistLabel.setDisable(false);
							jobIntervalDistribution.setDisable(false);
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

							jobIntervalMDCheck.setDisable(false);
							jobIntervalMDBox.setDisable(false);
							jobIntervalMDButton.setDisable(false);

							jobIntervalPeriodCheck.setDisable(false);
							jobIntervalPeriodBox.setDisable(false);
							jobIntervalPeriodButton.setDisable(false);
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

							jobIntervalPeriodCheck.setDisable(true);
							jobIntervalPeriodBox.setDisable(true);
							jobIntervalPeriodButton.setDisable(true);
						} else {
							jobIntervalRefCheck.setDisable(false);
							jobIntervalRefLabel.setDisable(false);
							jobIntervalRefBox.setDisable(false);
							jobIntervalExprLabel.setDisable(false);
							jobIntervalExprText.setDisable(false);

							jobIntervalDistLabel.setDisable(false);
							jobIntervalDistribution.setDisable(false);
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

							jobIntervalPeriodCheck.setDisable(false);
							jobIntervalPeriodBox.setDisable(false);
							jobIntervalPeriodButton.setDisable(false);
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
							jobIntervalMDButton.setDisable(true);
						} else {
							jobIntervalRefCheck.setDisable(false);
							jobIntervalRefLabel.setDisable(false);
							jobIntervalRefBox.setDisable(false);
							jobIntervalExprLabel.setDisable(false);
							jobIntervalExprText.setDisable(false);

							jobIntervalMDCheck.setDisable(false);
							jobIntervalMDBox.setDisable(false);
							jobIntervalMDButton.setDisable(false);
						}
						break;
					case "cpucntRefCheck":
						if (checkbox.isSelected()) {
							cpucntRefBox.setDisable(false);
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
							cpucntMDButton.setDisable(true);

							cpucntPeriodCheck.setDisable(true);
							cpucntPeriodBox.setDisable(true);
							cpucntPeriodButton.setDisable(true);
						} else {
							cpucntDistLabel.setDisable(false);
							cpucntDistribution.setDisable(false);
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

							cpucntMDCheck.setDisable(false);
							cpucntMDBox.setDisable(false);
							cpucntMDButton.setDisable(false);

							cpucntPeriodCheck.setDisable(false);
							cpucntPeriodBox.setDisable(false);
							cpucntPeriodButton.setDisable(false);
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

							cpucntPeriodCheck.setDisable(true);
							cpucntPeriodBox.setDisable(true);
							cpucntPeriodButton.setDisable(true);
						} else {
							cpucntRefCheck.setDisable(false);
							cpucntRefLabel.setDisable(false);
							cpucntRefBox.setDisable(false);
							cpucntExprLabel.setDisable(false);
							cpucntExprText.setDisable(false);

							cpucntDistLabel.setDisable(false);
							cpucntDistribution.setDisable(false);
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

							cpucntPeriodCheck.setDisable(false);
							cpucntPeriodBox.setDisable(false);
							cpucntPeriodButton.setDisable(false);
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
							cpucntMDButton.setDisable(true);
						} else {
							cpucntRefCheck.setDisable(false);
							cpucntRefLabel.setDisable(false);
							cpucntRefBox.setDisable(false);
							cpucntExprLabel.setDisable(false);
							cpucntExprText.setDisable(false);

							cpucntMDCheck.setDisable(false);
							cpucntMDBox.setDisable(false);
							cpucntMDButton.setDisable(false);
						}
						break;
					case "memoryRefCheck":
						if (checkbox.isSelected()) {
							memoryRefBox.setDisable(false);
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
							memoryMDButton.setDisable(true);

							memoryPeriodCheck.setDisable(true);
							memoryPeriodBox.setDisable(true);
							memoryPeriodButton.setDisable(true);
						} else {
							memoryDistLabel.setDisable(false);
							memoryDistribution.setDisable(false);
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

							memoryMDCheck.setDisable(false);
							memoryMDBox.setDisable(false);
							memoryMDButton.setDisable(false);

							memoryPeriodCheck.setDisable(false);
							memoryPeriodBox.setDisable(false);
							memoryPeriodButton.setDisable(false);
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

							memoryPeriodCheck.setDisable(true);
							memoryPeriodBox.setDisable(true);
							memoryPeriodButton.setDisable(true);
						} else {
							memoryRefCheck.setDisable(false);
							memoryRefLabel.setDisable(false);
							memoryRefBox.setDisable(false);
							memoryExprLabel.setDisable(false);
							memoryExprText.setDisable(false);

							memoryDistLabel.setDisable(false);
							memoryDistribution.setDisable(false);
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

							memoryPeriodCheck.setDisable(false);
							memoryPeriodBox.setDisable(false);
							memoryPeriodButton.setDisable(false);
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
							memoryMDButton.setDisable(true);
						} else {
							memoryRefCheck.setDisable(false);
							memoryRefLabel.setDisable(false);
							memoryRefBox.setDisable(false);
							memoryExprLabel.setDisable(false);
							memoryExprText.setDisable(false);

							memoryMDCheck.setDisable(false);
							memoryMDBox.setDisable(false);
							memoryMDButton.setDisable(false);
						}
						break;
					}
				}
				if (event.getSource() instanceof ComboBox) {
					ComboBox<?> combobox = (ComboBox<?>) event.getSource();
					if ("taskCountDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							taskCountAvg.setDisable(false);
							taskCountStdev.setDisable(true);
							taskCountMax.setDisable(true);
							taskCountMin.setDisable(true);
							taskCountSeed.setDisable(true);
							break;
						case "normal":
							taskCountAvg.setDisable(false);
							taskCountStdev.setDisable(false);
							taskCountMax.setDisable(false);
							taskCountMin.setDisable(false);
							taskCountSeed.setDisable(false);
							break;
						case "poisson":
							taskCountAvg.setDisable(false);
							taskCountStdev.setDisable(true);
							taskCountMax.setDisable(false);
							taskCountMin.setDisable(false);
							taskCountSeed.setDisable(false);
							break;
						case "uniform":
							taskCountAvg.setDisable(true);
							taskCountStdev.setDisable(true);
							taskCountMax.setDisable(false);
							taskCountMin.setDisable(false);
							taskCountSeed.setDisable(false);
							break;
						}
					} else if ("taskLengthDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							taskLengthAvg.setDisable(false);
							taskLengthStdev.setDisable(true);
							taskLengthMax.setDisable(true);
							taskLengthMin.setDisable(true);
							taskLengthSeed.setDisable(true);
							break;
						case "normal":
							taskLengthAvg.setDisable(false);
							taskLengthStdev.setDisable(false);
							taskLengthMax.setDisable(false);
							taskLengthMin.setDisable(false);
							taskLengthSeed.setDisable(false);
							break;
						case "poisson":
							taskLengthAvg.setDisable(false);
							taskLengthStdev.setDisable(true);
							taskLengthMax.setDisable(false);
							taskLengthMin.setDisable(false);
							taskLengthSeed.setDisable(false);
							break;
						case "uniform":
							taskLengthAvg.setDisable(true);
							taskLengthStdev.setDisable(true);
							taskLengthMax.setDisable(false);
							taskLengthMin.setDisable(false);
							taskLengthSeed.setDisable(false);
							break;
						}
					} else if ("jobPackLenDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							jobPackLenAvg.setDisable(false);
							jobPackLenStdev.setDisable(true);
							jobPackLenMax.setDisable(true);
							jobPackLenMin.setDisable(true);
							jobPackLenSeed.setDisable(true);
							break;
						case "normal":
							jobPackLenAvg.setDisable(false);
							jobPackLenStdev.setDisable(false);
							jobPackLenMax.setDisable(false);
							jobPackLenMin.setDisable(false);
							jobPackLenSeed.setDisable(false);
							break;
						case "poisson":
							jobPackLenAvg.setDisable(false);
							jobPackLenStdev.setDisable(true);
							jobPackLenMax.setDisable(false);
							jobPackLenMin.setDisable(false);
							jobPackLenSeed.setDisable(false);
							break;
						case "uniform":
							jobPackLenAvg.setDisable(true);
							jobPackLenStdev.setDisable(true);
							jobPackLenMax.setDisable(false);
							jobPackLenMin.setDisable(false);
							jobPackLenSeed.setDisable(false);
							break;
						}
					} else if ("cpucntDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							cpucntAvg.setDisable(false);
							cpucntStdev.setDisable(true);
							cpucntMax.setDisable(true);
							cpucntMin.setDisable(true);
							cpucntSeed.setDisable(true);
							break;
						case "normal":
							cpucntAvg.setDisable(false);
							cpucntStdev.setDisable(false);
							cpucntMax.setDisable(false);
							cpucntMin.setDisable(false);
							cpucntSeed.setDisable(false);
							break;
						case "poisson":
							cpucntAvg.setDisable(false);
							cpucntStdev.setDisable(true);
							cpucntMax.setDisable(false);
							cpucntMin.setDisable(false);
							cpucntSeed.setDisable(false);
							break;
						case "uniform":
							cpucntAvg.setDisable(true);
							cpucntStdev.setDisable(true);
							cpucntMax.setDisable(false);
							cpucntMin.setDisable(false);
							cpucntSeed.setDisable(false);
							break;
						}
					} else if ("cpucntDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							cpucntAvg.setDisable(false);
							cpucntStdev.setDisable(true);
							cpucntMax.setDisable(true);
							cpucntMin.setDisable(true);
							cpucntSeed.setDisable(true);
							break;
						case "normal":
							cpucntAvg.setDisable(false);
							cpucntStdev.setDisable(false);
							cpucntMax.setDisable(false);
							cpucntMin.setDisable(false);
							cpucntSeed.setDisable(false);
							break;
						case "poisson":
							cpucntAvg.setDisable(false);
							cpucntStdev.setDisable(true);
							cpucntMax.setDisable(false);
							cpucntMin.setDisable(false);
							cpucntSeed.setDisable(false);
							break;
						case "uniform":
							cpucntAvg.setDisable(true);
							cpucntStdev.setDisable(true);
							cpucntMax.setDisable(false);
							cpucntMin.setDisable(false);
							cpucntSeed.setDisable(false);
							break;
						}
					} else if ("memoryDistribution".equals(combobox.getId())) {
						switch (combobox.getValue().toString()) {
						case "constant":
							memoryAvg.setDisable(false);
							memoryStdev.setDisable(true);
							memoryMax.setDisable(true);
							memoryMin.setDisable(true);
							memorySeed.setDisable(true);
							break;
						case "normal":
							memoryAvg.setDisable(false);
							memoryStdev.setDisable(false);
							memoryMax.setDisable(false);
							memoryMin.setDisable(false);
							memorySeed.setDisable(false);
							break;
						case "poisson":
							memoryAvg.setDisable(false);
							memoryStdev.setDisable(true);
							memoryMax.setDisable(false);
							memoryMin.setDisable(false);
							memorySeed.setDisable(false);
							break;
						case "uniform":
							memoryAvg.setDisable(true);
							memoryStdev.setDisable(true);
							memoryMax.setDisable(false);
							memoryMin.setDisable(false);
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
		this.cpucntDistribution.setOnAction(eventHandler);
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

		this.cpucntRefCheck.setOnAction(eventHandler);
		this.cpucntMDCheck.setOnAction(eventHandler);
		this.cpucntPeriodCheck.setOnAction(eventHandler);
		
		this.memoryRefCheck.setOnAction(eventHandler);
		this.memoryMDCheck.setOnAction(eventHandler);
		this.memoryPeriodCheck.setOnAction(eventHandler);
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
	void taskCountRefCheckListener(ActionEvent event) {
		System.out.println("hello");
		if (this.taskCountRefCheck.isSelected()) {
			this.taskCountAvg.setDisable(false);
		} else {
			this.taskCountAvg.setDisable(true);
		}
	}

	// MyCheckBoxListener.java´úÂëÆ¬¶Î
	public class MyCheckBoxListener implements ChangeListener {

		TextField textfield = new TextField();
		Button button = new Button();
		CheckBox checkBox;

		public MyCheckBoxListener() {

		}

		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			boolean select = checkBox.isSelected();
			// logger.info("checkbox newValue:" + newValue +",oldValue:"+oldValue);
			if (!select) {
				textfield.setDisable(true);
				button.setDisable(true);
			} else {
				textfield.setDisable(false);
				button.setDisable(false);
			}
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub

		}
	}

	@FXML
	void generateButtonClick(ActionEvent event) {
		WorkloadGenerator generator = new WorkloadGenerator();
		ConfigurationOptions configurationOptions = ConfigurationOptions.getConfiguration(workloadFilename.getText(),
				outputFolder.getText());

		generator.run(configurationOptions, workloadWrapper());
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
		taskCount.setAvg(Double.parseDouble(this.taskCountAvg.getText()));
		taskCount.setDistribution(
				ParameterAttributesDistributionType.valueOf(this.taskCountDistribution.getValue().toString()));
		taskCount.setMax(Double.parseDouble(this.taskCountMax.getText()));
//		taskCount.setExpr(expr);

		/** WorkloadConfiguration */
		WorkloadConfiguration workload = new WorkloadConfiguration();
		workload.setSimulationStartTime(simulationStartTime);

		WorkloadConfigurationChoice workloadConfigurationChoice = new WorkloadConfigurationChoice();
		workloadConfigurationChoice.setJobCount(jobCount);
		workload.setWorkloadConfigurationChoice(workloadConfigurationChoice);

		workload.setTaskCount(taskCount);

		return workload;

	}
}

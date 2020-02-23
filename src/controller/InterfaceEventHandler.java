package controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class InterfaceEventHandler extends WorkGenViewController implements EventHandler {
	
	@Override
	public void handle(Event event) {
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
					taskCountMDButton.setDisable(true);

					taskCountPeriodCheck.setDisable(true);
					taskCountPeriodBox.setDisable(true);
					taskCountPeriodButton.setDisable(true);
				} else {

					changeTaskCountNodeStatus();

					taskCountRefBox.setDisable(true);
					taskCountExprLabel.setDisable(true);
					taskCountExprText.setDisable(true);

					taskCountDistLabel.setDisable(false);
					taskCountDistribution.setDisable(false);

					taskCountMDCheck.setDisable(false);
					taskCountMDBox.setDisable(true);
					taskCountMDButton.setDisable(true);

					taskCountPeriodCheck.setDisable(false);
					taskCountPeriodBox.setDisable(true);
					taskCountPeriodButton.setDisable(true);
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
					taskCountMDButton.setDisable(false);

					taskCountPeriodCheck.setDisable(true);
					taskCountPeriodBox.setDisable(true);
					taskCountPeriodButton.setDisable(true);
				} else {
					changeTaskCountNodeStatus();
					taskCountRefCheck.setDisable(false);

					taskCountDistLabel.setDisable(false);
					taskCountDistribution.setDisable(false);

					taskCountMDBox.setDisable(true);
					taskCountMDButton.setDisable(true);
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
					taskCountMDButton.setDisable(true);

					taskCountPeriodBox.setDisable(false);
					taskCountPeriodButton.setDisable(false);
				} else {
					changeTaskCountNodeStatus();
					taskCountRefCheck.setDisable(false);
					taskCountMDCheck.setDisable(false);
					taskCountPeriodBox.setDisable(true);
					taskCountPeriodButton.setDisable(true);

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

}

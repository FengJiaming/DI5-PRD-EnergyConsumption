package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
	    private ComboBox<?> Distribution;

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
	    
		public void init() {
			
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
						ComboBox<?> combobox = (ComboBox<?>) event.getSource();
							switch (combobox.getValue().toString()) {
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
				}
			};
			this.Distribution.setOnAction(eventHandler);
		}

		@FXML
		public void saveButtonClick() {
			
		}
		
		@FXML
		public void deleteButtonClick() {
			
		}
		
		public void cancelButtonClick() {
		    Stage stage = (Stage)Cancel.getScene().getWindow();
		    stage.close();
		}
	}

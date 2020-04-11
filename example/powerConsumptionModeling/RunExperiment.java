package powerConsumptionModeling;
import simulator.DataCenterWorkloadSimulator;


public class RunExperiment {
	
	public static void main(String args[]){
		RunExperiment runner = new RunExperiment();
		runner.run();
	}
	
	public void run(){
		String args[] = {
				"example/powerConsumptionModeling/powerConsumptionModeling.properties"
				};
		DataCenterWorkloadSimulator.main(args);
	}

}

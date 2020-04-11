package plugins.globalplugin;

import model.Parameters;
import model.PluginConfiguration;
import model.events.scheduling.SchedulingEventType;
import model.scheduling.TaskList;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.plugin.SchedulingPluginConfiguration;
import model.scheduling.plugin.grid.GlobalSchedulingPlugin;
import model.scheduling.plugin.grid.ModuleList;
import model.scheduling.queue.TaskQueue;
import model.scheduling.queue.TaskQueueList;
import model.scheduling.tasks.TaskInterface;
import schemas.StringValueWithUnit;

public abstract class BaseGlobalPlugin implements GlobalSchedulingPlugin {

	SchedulingPluginConfiguration plugConf;
	
	public PluginConfiguration getConfiguration() {
		return plugConf;
	}
	
	public boolean placeTasksInQueues(TaskList newTasks, TaskQueueList queues, ResourceManager resourceManager,
			ModuleList moduleList) {

		// get the first queue from all available queues.
		TaskQueue queue = queues.get(0);

		for (int i = 0; i < newTasks.size(); i++) {
			TaskInterface<?> task = newTasks.get(i);
			queue.add(task);
		}

		return true;
	}
	
	public void init(Parameters parameters) {
		plugConf = new SchedulingPluginConfiguration();
		try{
			StringValueWithUnit freq = parameters.get("frequency").get(0);
			plugConf.addServedEvent(SchedulingEventType.TIMER, Double.valueOf(freq.getContent()).intValue());
		} catch(Exception e){
			
		}
	}
	
	public String getName() {
		return getClass().getName();
	}
	
}

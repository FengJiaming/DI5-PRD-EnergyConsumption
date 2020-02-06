package example.globalplugin;

import schedframe.Parameters;
import schedframe.PluginConfiguration;
import schedframe.events.scheduling.SchedulingEventType;
import schedframe.scheduling.TaskList;
import schedframe.scheduling.manager.resources.ResourceManager;
import schedframe.scheduling.plugin.SchedulingPluginConfiguration;
import schedframe.scheduling.plugin.grid.GlobalSchedulingPlugin;
import schedframe.scheduling.plugin.grid.ModuleList;
import schedframe.scheduling.queue.TaskQueue;
import schedframe.scheduling.queue.TaskQueueList;
import schedframe.scheduling.tasks.TaskInterface;
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

package example.localplugin;

import gridsim.Gridlet;
import model.events.scheduling.SchedulingEvent;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.manager.tasks.JobRegistry;
import model.scheduling.plan.SchedulingPlanInterface;
import model.scheduling.plugin.grid.ModuleList;
import model.scheduling.queue.Queue;
import model.scheduling.queue.TaskQueueList;
import model.scheduling.tasks.TaskInterface;

import java.util.List;
import java.util.Properties;


public class LCFS_LocalPlugin extends BaseLocalSchedulingPlugin {

	
	public LCFS_LocalPlugin(){
	}
	
	public void schedule(SchedulingEvent event,
			List<? extends TaskInterface<?>> inExecution,
			List<? extends Queue<? extends TaskInterface<?>>> queues
			) {
		
		// do this trick to make add() method available
		List <TaskInterface<?>> execute = (List<TaskInterface<?>>) inExecution;
		
		// chose the events types to serve. 
		// Different actions for different events are possible.
		switch(event.getType()){
			case START_TASK_EXECUTION:
			case TASK_FINISHED:
				// our tasks are placed only in first queue (see BaseLocalPlugin.placeTasksInQueues() method)
				Queue<? extends TaskInterface<?>> q = queues.get(0);
				// check all tasks in queue
				for(int i = q.size() - 1; i >= 0; i--){
					TaskInterface<?> task = q.get(i);
					// if status of the tasks in READY
					if(task.getStatus() == Gridlet.READY){
						// then try to execute this task. Add it the execute list.
						if(execute.add(task)){
							// if task started successfully, then remove it from the queue.
							q.remove(i);
						}
						else{
							break;
						}
					}
				}
				break;
		}
		
	}

	public String getPluginName() {
		return getClass().getName();
	}

	public void initPlugin(Properties properties) {
		// no extra initialization is expected.
	}

	@Override
	public SchedulingPlanInterface<?> schedule(SchedulingEvent event, TaskQueueList queues, JobRegistry jobRegistry,
			ResourceManager resourceManager, ModuleList modules) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

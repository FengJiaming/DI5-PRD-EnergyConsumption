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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

//import simulator.utils.MergeSort;

public class LJF_LocalPlugin extends BaseLocalSchedulingPlugin {

	public LJF_LocalPlugin(){
	}
	
//	public void schedule(SchedulingEvent event,
//			List<? extends TaskInterface<?>> inExecution,
//			List<? extends Queue<? extends TaskInterface<?>>> queues,
//			ResourceUnitsManagerInterface unitsManagerInterface) {
//		
//		// do this trick to make add() method available
//		List <TaskInterface<?>> execute = (List<TaskInterface<?>>) inExecution;
//		
//		// chose the events types to serve. 
//		// Different actions for different events are possible.
//		switch(event.getType()){
//			//case START_TASK_EXECUTION:
//			//case TASK_FINISHED:
//			case TIMER:
//				// our tasks are placed only in first queue (see BaseLocalPlugin.placeTasksInQueues() method)
//				Queue<? extends TaskInterface<?>> q = queues.get(0);
//				
//				sortTasksInQueue(q);
//
//				// check all tasks in queue
//				for(int i = 0; i < q.size(); i++){
//					TaskInterface<?> task = q.get(i);
//					// if status of the tasks in READY
//					if(task.getStatus() == Gridlet.READY){
//						// then try to execute this task. Add it the execute list.
//						if(execute.add(task)){
//							// if task started successfully, then remove it from the queue.
//							q.remove(i);
//							i--;
//						}
//					}
//				}
//				break;
//		}
//		
//	}

	public String getPluginName() {
		return getClass().getName();
	}

	public void initPlugin(Properties properties) {
		// no extra initialization is expected.
	}
	
//    public void sortTasksInQueue(Queue<? extends TaskInterface<?>> q) {
//    	
//    	TaskInterface<?>[] arrayQueue = new TaskInterface<?>[q.size()];
//    	int i = 0;
//    	for(TaskInterface<?> task: q){
//    		arrayQueue[i] = task;
//    		i++;
//    	}
//    	MergeSort mergeSort = new MergeSort();
//        mergeSort.initSort(arrayQueue, "getLength", false);
//        q.clear();
//        q.addAll(new ArrayList(Arrays.asList(arrayQueue)));
//    }

	@Override
	public SchedulingPlanInterface<?> schedule(SchedulingEvent event, TaskQueueList queues, JobRegistry jobRegistry,
			ResourceManager resourceManager, ModuleList modules) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

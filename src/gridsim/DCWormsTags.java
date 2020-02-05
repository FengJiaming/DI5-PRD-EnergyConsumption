package gridsim;

import gridsim.GridSimTags;

public class DCWormsTags extends GridSimTags {
	
	protected static final int DCWORMSBASE = 600; 
	
	public static final int TO_COMP_RESOURCES = DCWORMSBASE - 4;
	
	public static final int TO_SCHEDULERS = DCWORMSBASE - 3;
	
	public static final int PHASE_CHANGED = DCWORMSBASE - 2;
	
	public static final int UPDATE_PROCESSING = DCWORMSBASE - 1;
	
	public static final int TIMER = DCWORMSBASE + 1;
	
	public static final int TASK_READY_FOR_EXECUTION = DCWORMSBASE + 2;
	
	public static final int TASK_EXECUTION_FINISHED = DCWORMSBASE + 3;
	

	/**
	 * Message tag: query resource about its current state in object form (load, queues, etc.)
	 */ 
	public static final int QUERY_RESOURCE_DESC = DCWORMSBASE + 4; 

	public static final int QUERY_RESOURCE_DESC_RESULT = DCWORMSBASE + 5; 
	
	public static final int TASK_REQUESTED_TIME_EXPIRED = DCWORMSBASE + 6;

	
    public static final int CREATED = 0;

    public static final int SUBMITTED = 1;
    
    public static final int READY = 2;

    public static final int QUEUED = 3;

    public static final int INEXEC = 4;

    public static final int SUCCESS = 5;

    public static final int FAILED = 6;

    public static final int CANCELED = 7;

    public static final int PAUSED = 8;

    public static final int RESUMED = 9;

    public static final int FAILED_RESOURCE_UNAVAILABLE = 10;

}
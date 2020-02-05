package controller.workload;



public abstract class SWFFields {
	
	public static final String COMMENT_VERSION = "Version";
	public static final String COMMENT_COMPUTER = "Computer";
	public static final String COMMENT_INSTALLATION = "Installation";
	public static final String COMMENT_ACKNOWLEDGE = "Acknowledge";
	public static final String COMMENT_INFORMATION = "Information";
	public static final String COMMENT_CONVERSION = "Conversion";
	public static final String COMMENT_MAXJOBS = "MaxJobs";
	public static final String COMMENT_MAXRECORDS = "MaxRecords";
	public static final String COMMENT_PREEMPTION = "Preemption";
	public static final String COMMENT_UNIXSTARTTIME = "UnixStartTime";
	public static final String COMMENT_TIMEZONE = "TimeZone";
	public static final String COMMENT_TIMEZONESTRING = "TimeZoneString";
	public static final String COMMENT_ENDTIME = "EndTime";
	public static final String COMMENT_MAXNODES = "MaxNodes";
	public static final String COMMENT_MAXPROCS = "MaxProcs";
	public static final String COMMENT_MAXRUNTIME = "MaxRuntime";
	public static final String COMMENT_MAXMEMORY = "MaxMemory";
	public static final String COMMENT_ALLOWOVERUSE = "AllowOveruse";
	public static final String COMMENT_MAXQUEUES = "MaxQueues";
	public static final String COMMENT_QUEUES = "Queues";
	public static final String COMMENT_QUEUE = "Queue";
	public static final String COMMENT_MAXPARTITIONS = "MaxPartitions";
	public static final String COMMENT_PARTITIONS = "Partitions";
	public static final String COMMENT_PARTITION = "Partition";
	public static final String COMMENT_STARTTIME = "StartTime";
	public static final String COMMENT_NOTE = "Note";
	public static final String COMMENT_PUSPEED = "PUSpeed";
	public static final String COMMENT_IDMAPPING = "IDMapping";
	public static final String COMMENT_APPLICATION = "Application";
	
	public static final String COMMENTS_LABELS[] = {SWFFields.COMMENT_VERSION,SWFFields.COMMENT_COMPUTER,
		SWFFields.COMMENT_INSTALLATION,SWFFields.COMMENT_ACKNOWLEDGE,SWFFields.COMMENT_INFORMATION,
		SWFFields.COMMENT_CONVERSION,SWFFields.COMMENT_MAXJOBS,SWFFields.COMMENT_MAXRECORDS,
		SWFFields.COMMENT_PREEMPTION,SWFFields.COMMENT_UNIXSTARTTIME,SWFFields.COMMENT_TIMEZONE,
		SWFFields.COMMENT_TIMEZONESTRING,SWFFields.COMMENT_STARTTIME,SWFFields.COMMENT_ENDTIME,
		SWFFields.COMMENT_MAXNODES,SWFFields.COMMENT_MAXPROCS,SWFFields.COMMENT_MAXRUNTIME,
		SWFFields.COMMENT_MAXMEMORY,SWFFields.COMMENT_ALLOWOVERUSE,SWFFields.COMMENT_MAXQUEUES,
		SWFFields.COMMENT_QUEUES,SWFFields.COMMENT_QUEUE, SWFFields.COMMENT_MAXPARTITIONS,
		SWFFields.COMMENT_PARTITIONS,SWFFields.COMMENT_PARTITION, SWFFields.COMMENT_NOTE, SWFFields.COMMENT_PUSPEED, 
		SWFFields.COMMENT_IDMAPPING};

	
	public static final int DATA_JOB_NUMBER = 0;
	public static final int DATA_SUBMIT_TIME = 1;
	public static final int DATA_WAIT_TIME = 2;
	public static final int DATA_RUN_TIME = 3;
	public static final int DATA_NUMBER_OF_ALLOCATED_PROCESSORS = 4;
	public static final int DATA_AVERAGE_CPU_TIME_USED = 5;
	public static final int DATA_USED_MEMORY = 6;
	public static final int DATA_REQUESTED_NUMBER_OF_PROCESSORS = 7;
	public static final int DATA_REQUESTED_TIME = 8;
	public static final int DATA_REQUESTED_MEMORY = 9;
	public static final int DATA_STATUS = 10;
	public static final int DATA_USER_ID = 11;
	public static final int DATA_GROUP_ID = 12;
	public static final int DATA_EXECUTABLE_NUMBER = 13;
	public static final int DATA_QUEUE_NUMBER = 14;
	public static final int DATA_PARTITION_NUMBER = 15;
	public static final int DATA_PRECEDING_JOB_NUMBER = 16;
	public static final int DATA_THINK_TIME_FROM_PRECEDING_JOB = 17;
	public static final int DATA_FIELDS[] = {SWFFields.DATA_JOB_NUMBER,SWFFields.DATA_SUBMIT_TIME,
		SWFFields.DATA_WAIT_TIME,SWFFields.DATA_RUN_TIME,SWFFields.DATA_NUMBER_OF_ALLOCATED_PROCESSORS,
		SWFFields.DATA_AVERAGE_CPU_TIME_USED,SWFFields.DATA_USED_MEMORY,
		SWFFields.DATA_REQUESTED_NUMBER_OF_PROCESSORS,SWFFields.DATA_REQUESTED_TIME,
		SWFFields.DATA_REQUESTED_MEMORY,SWFFields.DATA_STATUS,SWFFields.DATA_USER_ID,
		SWFFields.DATA_GROUP_ID,SWFFields.DATA_EXECUTABLE_NUMBER,SWFFields.DATA_QUEUE_NUMBER,
		SWFFields.DATA_PARTITION_NUMBER, SWFFields.DATA_PRECEDING_JOB_NUMBER,
		SWFFields.DATA_THINK_TIME_FROM_PRECEDING_JOB};

}

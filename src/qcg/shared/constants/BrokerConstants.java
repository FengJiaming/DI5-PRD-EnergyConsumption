package qcg.shared.constants;


public interface BrokerConstants {
    
    public static final short SUBMITION_NOTIFICATION = 0;
    public static final short MIGRATION_NOTIFICATION = 1;
    //public static final short MIGRATION_PENDING_NOTIFICATION = 2;
    public static final short IMPROVE_NOTIFICATION = 2;
    
    public static final long JOB_STATUS_UNCOMMITTED = 1;
    public static final long JOB_STATUS_SUBMITTED = 2;
    public static final long JOB_STATUS_ACTIVE = 4;
    public static final long JOB_STATUS_FINISHED = 8;
    public static final long JOB_STATUS_FAILED = 16;
    public static final long JOB_STATUS_CANCELED = 32;
    public static final long JOB_STATUS_SUSPENDED = 64;
    public static final long JOB_STATUS_BROKEN = 128;
    
    public static final long TASK_STATUS_UNSUBMITTED = 1;
    public static final long TASK_STATUS_QUEUED = 2;
    public static final long TASK_STATUS_PREPROCESSING = 4;
    public static final long TASK_STATUS_PENDING = 8;
    public static final long TASK_STATUS_RUNNING = 16;
    public static final long TASK_STATUS_STOPPED = 32;
    public static final long TASK_STATUS_POSTPROCESSING = 64;
    public static final long TASK_STATUS_FINISHED = 128;
    public static final long TASK_STATUS_SUSPENDED = 256;
    public static final long TASK_STATUS_FAILED = 512;
    public static final long TASK_STATUS_CANCELED = 1024;
    public static final long TASK_STATUS_UNCOMMITTED = 2048;
    
    public static final int LOCAL_STATUS_UNKNOWN = 0;
    public static final int LOCAL_STATUS_PENDING = 1;
    public static final int LOCAL_STATUS_ACTIVE = 2;
    public static final int LOCAL_STATUS_FAILED = 4;
    public static final int LOCAL_STATUS_DONE = 8;
    public static final int LOCAL_STATUS_SUSPENDED = 16;
    public static final int LOCAL_STATUS_UNSUBMITTED = 32;
    public static final int LOCAL_STATUS_CANCELED = 64;
      
    
    public static final int QUEUE_ACTIVE = 0;
    public static final int QUEUE_PENDING = 1;
    
    public static final long TASK_QUEUED                = 1;
    public static final long TASK_RESOURCE              = 2;
    public static final long TASK_RESOURCE_DONE         = 4;
    public static final long TASK_STAGE_IN              = 8;
    public static final long TASK_STAGE_IN_DONE         = 16;
    public static final long TASK_SUBMIT                = 32;
    public static final long TASK_SUBMIT_DONE           = 64;
    public static final long TASK_EXEC                  = 128;
    public static final long TASK_EXEC_PENDING          = 256;
    public static final long TASK_EXEC_ACTIVE           = 512;
    public static final long TASK_EXEC_DONE             = 1024;
    public static final long TASK_STAGE_OUT             = 2048;
    public static final long TASK_STAGE_OUT_DONE        = 4096;
    public static final long TASK_DONE                  = 8192;
    public static final long TASK_FAILED                = 16384;
    public static final long TASK_CANCEL_DONE           = 32768;
    public static final long TASK_CANCEL_FAILED         = 65536;
    public static final long MIGRATE_QUEUED             = 1 << 17;
    public static final long MIGRATE_EXEC_SUSPEND       = 1 << 18;
    public static final long MIGRATE_EXEC_SUSPEND_DONE  = 1 << 19;
    public static final long MIGRATE_RESOURCE           = 1 << 20;
    public static final long MIGRATE_RESOURCE_DONE      = 1 << 21;
    public static final long MIGRATE_STAGE_IN           = 1 << 22;
    public static final long MIGRATE_STAGE_IN_DONE      = 1 << 23;
    public static final long MIGRATE_SUBMIT             = 1 << 24;
    public static final long MIGRATE_DONE               = 1 << 25;
    public static final long MIGRATE_FAILED             = 1 << 26;
    public static final long MIGRATE_STAGE_OUT          = 1 << 27;
    public static final long MIGRATE_STAGE_OUT_DONE     = 1 << 28;
    public static final long REQUEST_FAILED             = 1 << 29;
    public static final long TASK_CANCEL                = 1 << 30;
    public static final long TASK_UNSUBMITTED           = 1 << 31;
    public static final long TASK_UNCOMMITTED           = 1 << 32;
    
    public static final int PARAM_HOST = 0;
    public static final int PARAM_LRM = 1;
    public static final int PARAM_MEM = 2;
    public static final int PARAM_CPUS = 3;
    public static final int PARAM_SPEED = 4;
    public static final int PARAM_OSTYPE = 5;
    public static final int PARAM_OSVER = 6;
    public static final int PARAM_OSREL = 7;
    public static final int PARAM_BAND = 8;
    public static final int PARAM_LAT = 9;
    public static final int PARAM_CAP = 10;
    public static final int PARAM_TYPE = 11;
    public static final int PARAM_FREEMEM = 12;
    public static final int PARAM_FREECPU = 13;
    
    public final static String CONFIG_FNAME = "./etc/config.prop";
    public final static String ERRORS_FNAME = "./etc/errors.prop";
    
    public static final int SUBMITION_TYPE = 0;
    public static final int MIGRATION_TYPE = 1;
    public static final int MIGRATION_TYPE_DESC = 2;
    public static final int MIGRATION_TYPE_NODESC = 3;
    public static final int RESUME_TYPE_DESC = 4;
    public static final int RESUME_TYPE_NODESC = 5;
}

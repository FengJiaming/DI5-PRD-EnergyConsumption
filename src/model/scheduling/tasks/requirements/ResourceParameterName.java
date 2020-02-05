package model.scheduling.tasks.requirements;


public enum ResourceParameterName {

	/**
     * Constant OSNAME
     */
    OSNAME("osname"),
    /**
     * Constant OSTYPE
     */
    OSTYPE("ostype"),
    /**
     * Constant CPUARCH
     */
    CPUARCH("cpuarch"),
    /**
     * Constant OSVERSION
     */
    OSVERSION("osversion"),
    /**
     * Constant OSRELEASE
     */
    OSRELEASE("osrelease"),
    /**
     * Constant MEMORY
     */
    MEMORY("Memory"),
    /**
     * Constant FREEMEMORY
     */
    FREEMEMORY("freememory"),
    /**
     * Constant CPUCOUNT
     */
    CPUCOUNT("cpucount"),
    
    /**
     * Constant CPUCOUNT
     */
    GPUCOUNT("gpucount"),
    
    /**
     * Constant FREECPUS
     */
    FREECPUS("freecpus"),
    /**
     * Constant CPUSPEED
     */
    CPUSPEED("cpuspeed"),
    /**
     * Constant APPLICATION
     */
    APPLICATION("application"),
    /**
     * Constant DISKSPACE
     */
    DISKSPACE("Storage"),
    /**
     * Constant FREEDISKSPACE
     */
    FREEDISKSPACE("freediskspace"),
    /**
     * Constant REMOTESUBMISSIONINTERFACE
     */
    REMOTESUBMISSIONINTERFACE("remoteSubmissionInterface"),
    /**
     * Constant LOCALRESOURCEMANAGER
     */
    LOCALRESOURCEMANAGER("localResourceManager"),
    /**
     * Constant HOSTNAME
     */
    HOSTNAME("hostname"),
    
    COST("cost");
    
    private final java.lang.String value;


    private ResourceParameterName(final java.lang.String value) {
        this.value = value;
    }

    public static ResourceParameterName fromValue(final java.lang.String value) {
        for (ResourceParameterName c: ResourceParameterName.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }

    public void setValue(final java.lang.String value) {
    }

    public java.lang.String toString() {
        return this.value;
    }

    public java.lang.String value() {
        return this.value;
    }
}

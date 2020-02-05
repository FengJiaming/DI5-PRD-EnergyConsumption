package model.resources.units;


public enum StandardResourceUnitName implements ResourceUnitName{


    /**
     * Constant MEMORY
     */
    MEMORY("memory"),

    /**
     * Constant CPU
     */
    CPU("cpu"),
    
    /**
     * Constant GPU
     */
    GPU("gpu"),
    
    /**
     * Constant CPUSPEED
     */
    CPUSPEED("cpuspeed"),
    
    /**
     * Constant APPLICATION
     */
    APPLICATION("application"),
    
    /**
     * Constant STORAGE
     */
    STORAGE("storage"),
    
    COST("cost"),
    
    PE("processingElement");

    
    private final String name;

    private StandardResourceUnitName(String value) {
        this.name = value;
    }

    public String getName(){
    	return this.name;
    }

}

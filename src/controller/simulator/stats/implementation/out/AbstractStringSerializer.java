package controller.simulator.stats.implementation.out;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractStringSerializer implements StatsSerializer {
	
	protected boolean useExtended = true;
	protected String fieldSeparator = ";";
	protected NumberFormat defaultNumberFormat;
	protected Set<String> printedHeaders = new HashSet<String>();
	
	public void useExtendedOutput(boolean arg){
		this.useExtended = arg;
	}
	
	public void setFieldSeparator(String separator){
		this.fieldSeparator = separator;
	}
	
	public void setDefaultNumberFormat(NumberFormat value){
		this.defaultNumberFormat = value;
	}
}

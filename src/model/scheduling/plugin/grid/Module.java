package model.scheduling.plugin.grid;

import java.util.Properties;

import model.exceptions.ModuleException;


public interface Module {

	public void init(Properties properties) throws ModuleException;
	
	public void dispose() throws ModuleException;
	
	public ModuleType getType();
	
}

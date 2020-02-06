package model.resources.computing.extensions;

import java.util.Properties;

import model.events.Event;


public interface Extension /*extends Module*/{

	public boolean supportsEvent(Event event);

	//public void handleEvent(Sim_event ev);
	
	public void handleEvent(Event event);
	
	public void init(Properties properties) throws ExtensionException;
	
	public ExtensionType getType();
}


package model;

public interface Plugin {
	
	/**
	 * Returns the plugin name used in the simulation
	 * @return the name of the plugin with all the package namespace, e.g. org.gridcompany.plugins.forecast.GridPlugin
	 */
	public String getName();
	
	/**
	 * Initializes the plugin
	 */
	public void init(Parameters parameters);
	
	public PluginConfiguration getConfiguration();
	
}

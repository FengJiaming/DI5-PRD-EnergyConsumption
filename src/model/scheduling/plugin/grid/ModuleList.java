package model.scheduling.plugin.grid;

import java.util.List;

public interface ModuleList extends List<Module>{
	
	public <T extends Module> List<T> get(ModuleType type);
	
	public Module getModule(ModuleType type);
	
	public boolean isModuleAvailable(ModuleType type);
}

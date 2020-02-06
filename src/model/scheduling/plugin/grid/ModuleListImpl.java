package model.scheduling.plugin.grid;

import java.util.ArrayList;
import java.util.List;

public class ModuleListImpl extends ArrayList<Module> implements ModuleList {

	private static final long serialVersionUID = -3824600938144742457L;

	public ModuleListImpl(){
		super();
	}
	
	public ModuleListImpl(int initialSize){
		super(initialSize);
	}
	
	public List<Module> get(ModuleType type){
		List<Module> list = null;
		
		for(int i = 0; i < size(); i++){
			Module m = get(i);
			if(m.getType() == type){
				list = (list == null ? new ArrayList<Module>(1) : list);
				list.add(m);
			}
		}
		return list;
	}
	
	public Module getModule(ModuleType type){
		
		for(int i = 0; i < size(); i++){
			Module m = get(i);
			if(m.getType() == type){
				return m;
			}
		}
		return null;
	}
	
	public boolean isModuleAvailable(ModuleType type){
		for(int i = 0; i < size(); i++){
			Module m = get(i);
			if(m.getType() == type){
				return true;
			}
		}
		return false;
	}
	
	
}

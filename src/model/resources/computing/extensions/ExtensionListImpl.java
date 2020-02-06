package model.resources.computing.extensions;

import java.util.ArrayList;
import java.util.List;



public class ExtensionListImpl extends ArrayList<Extension> implements ExtensionList {

	private static final long serialVersionUID = -3824600938144742457L;

	public ExtensionListImpl(){
		super();
	}
	
	public ExtensionListImpl(int initialSize){
		super(initialSize);
	}
	
	public List<Extension> get(ExtensionType type){
		List<Extension> list = null;
		
		for(int i = 0; i < size(); i++){
			Extension e = get(i);
			if(e.getType() == type){
				list = (list == null ? new ArrayList<Extension>(1) : list);
				list.add(e);
			}
		}
		return list;
	}
	
	public Extension getExtension(ExtensionType type){
		
		for(int i = 0; i < size(); i++){
			Extension e = get(i);
			if(e.getType() == type){
				return e;
			}
		}
		return null;
	}
	
	public boolean isExtensionAvailable(ExtensionType type){
		for(int i = 0; i < size(); i++){
			Extension e = get(i);
			if(e.getType() == type){
				return true;
			}
		}
		return false;
	}
	
	
}

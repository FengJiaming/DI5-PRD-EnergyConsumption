package model.resources.computing.extensions;

import java.util.List;

public interface ExtensionList extends List<Extension>{
	
	public <T extends Extension> List<T> get(ExtensionType type);
	
	public Extension getExtension(ExtensionType type);
	
	public boolean isExtensionAvailable(ExtensionType type);
}

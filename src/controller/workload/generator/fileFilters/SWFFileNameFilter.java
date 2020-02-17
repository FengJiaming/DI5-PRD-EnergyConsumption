package controller.workload.generator.fileFilters;

import java.io.File;
import java.io.FilenameFilter;

public class SWFFileNameFilter implements FilenameFilter{
	
	public boolean accept(File dir, String name) {
		if(name.endsWith("swf"))
			return true;
		else
			return false;
	}

}

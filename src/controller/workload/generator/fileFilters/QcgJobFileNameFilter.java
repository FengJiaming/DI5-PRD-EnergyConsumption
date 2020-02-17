package controller.workload.generator.fileFilters;

import java.io.File;
import java.io.FilenameFilter;


public class QcgJobFileNameFilter implements FilenameFilter{

	public static final String FILE_NAME_PREFIX = "QcgJob";
	
	public QcgJobFileNameFilter(){};
	
	public boolean accept(File dir, String name) {
			if(name.startsWith(FILE_NAME_PREFIX) && name.endsWith("xml"))
				return true;
			else
				return false;
	}
}

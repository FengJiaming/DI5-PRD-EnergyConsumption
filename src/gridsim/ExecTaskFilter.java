package gridsim;

import eduni.simjava.Sim_event;
import gridsim.filter.FilterResult;
import gridsim.schedframe.Executable;

public class ExecTaskFilter extends FilterResult {

	public ExecTaskFilter(int taskId, int tag) {
		super(taskId, tag);
	}

	public boolean match(Sim_event ev) {

		if (ev == null) {
			return false;
		}

		boolean result = false;
		try {

			if (tag_ == ev.get_tag()) {
				Object obj = ev.get_data();

				if (obj instanceof Executable) {

					Executable exec = (Executable) obj;

					if (exec.getUniqueId() == eventID_) {
						result = true;
					}
				}
			}
		} catch (Exception e) {
			result = false;
		}

		return result;
	}

}

package model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import gridsim.schedframe.ExecTask;
import gridsim.schedframe.Executable;


public class ExecutablesList implements List<ExecTask> {

	private List<ExecTask> executables;
	
	public ExecutablesList (){
		executables = Collections.synchronizedList(new ArrayList<ExecTask>());
	}

	public List<Executable> getJobExecutables(String jobId){
		List<Executable> list = new ArrayList<Executable>();
		synchronized (executables)  {
			for(int i = 0; i < executables.size(); i++){
				Executable exec = (Executable)executables.get(i);
				
				if(exec.getJobId().equals(jobId))
					list.add(exec);
			}
		}
		return list;
	}
	
	public List<Executable> getTaskExecutables(String jobId, String taskId){
		
		List<Executable> list = new ArrayList<Executable>();
		synchronized (executables)  {
			for(int i = 0; i < executables.size(); i++){
				Executable exec = (Executable)executables.get(i);
				
				if(exec.getJobId().equals(jobId) && exec.getId().equals(taskId))
					list.add(exec);
			}
		}
		return list;
	}
	
	public Executable getTaskExecutable(Integer executableId){
		synchronized (executables)  {
			for (ExecTask task : executables) {
				Executable exec = (Executable)task;
				if (exec.getUniqueId() == executableId) {
					return exec;
				}
			}
		}
		return null;
	}
	
	
	public boolean add(ExecTask arg0) {
		return executables.add(arg0);
	}

	public void add(int arg0, ExecTask arg1) {
		executables.add(arg0, arg1);
	}

	public boolean addAll(Collection<? extends ExecTask> arg0) {
		return executables.addAll(arg0);
	}

	public boolean addAll(int arg0, Collection<? extends ExecTask> arg1) {
		return executables.addAll(arg0, arg1);
	}

	public void clear() {
		executables.clear();
	}

	public boolean contains(Object arg0) {
		return executables.contains(arg0);
	}

	public boolean containsAll(Collection<?> arg0) {
		return executables.containsAll(arg0);
	}

	public ExecTask get(int arg0) {
		return executables.get(arg0);
	}

	public int indexOf(Object arg0) {
		return indexOf(arg0);
	}

	public boolean isEmpty() {
		return executables.isEmpty();
	}

	public Iterator<ExecTask> iterator() {
		return executables.iterator();
	}

	public int lastIndexOf(Object arg0) {
		return executables.lastIndexOf(arg0);
	}

	public ListIterator<ExecTask> listIterator() {
		return executables.listIterator();
	}

	public ListIterator<ExecTask> listIterator(int arg0) {
		return executables.listIterator(arg0);
	}

	public boolean remove(Object arg0) {
		return executables.remove(arg0);
	}

	public ExecTask remove(int arg0) {
		return executables.remove(arg0);
	}

	public boolean removeAll(Collection<?> arg0) {
		return executables.removeAll(arg0);
	}

	public boolean retainAll(Collection<?> arg0) {
		return executables.retainAll(arg0);
	}

	public ExecTask set(int arg0, ExecTask arg1) {
		return executables.set(arg0, arg1);
	}

	public int size() {
		return executables.size();
	}

	public List<ExecTask> subList(int arg0, int arg1) {
		return executables.subList(arg0, arg1);
	}

	public Object[] toArray() {
		return executables.toArray();
	}

	public <T> T[] toArray(T[] arg0) {
		return executables.toArray(arg0);
	}
	
}


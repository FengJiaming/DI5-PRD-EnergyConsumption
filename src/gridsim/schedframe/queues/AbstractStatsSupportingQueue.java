package gridsim.schedframe.queues;

import java.util.ArrayList;
import java.util.Collection;


import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;
import model.scheduling.queue.Queue;


public abstract class AbstractStatsSupportingQueue<K> extends ArrayList<K> implements Queue<K>{

	private static final long serialVersionUID = 2067736388365966634L;
	protected double lastUpdateTime;
	protected Sim_stat stats;
	protected String measureName;
	
	protected AbstractStatsSupportingQueue(){
		this.stats = null;
		this.lastUpdateTime = 0.0;
		this.measureName = null;
	}
	
	public boolean add(K o) {
		boolean ret = super.add(o);
		updateStats();
		return ret;
	}

	public void add(int index, K element) {
		super.add(index, element);
		updateStats();
	}

	public boolean addAll(Collection<? extends K> c) {
		boolean ret = super.addAll(c);
		updateStats();
		return ret;
	}

	public boolean addAll(int index, Collection<? extends K> c) {
		boolean ret = super.addAll(index, c);
		updateStats();
		return ret;
	}

	public void clear() {
		super.clear();
		updateStats();
	}

	public boolean remove(Object o) {
		boolean ret = super.remove(o);
		updateStats();
		return ret;
	}

	public K remove(int index) {
		 K ret = super.remove(index);
		 updateStats();
		 return ret;
	}

	public boolean removeAll(Collection<?> c) {
		boolean ret = super.removeAll(c);
		updateStats();
		return ret;
	}

	public boolean retainAll(Collection<?> c) {
		boolean ret = super.retainAll(c);
		updateStats();
		return ret;
	}

	public K set(int index, K element) {
		K ret = super.set(index, element);
		updateStats();
		return ret;
	}
	
	
	public void setStats(Sim_stat stats, String measureName){
		this.stats = stats;
		this.measureName = measureName;
	}
	
	protected void updateStats() {
		if(stats == null)
			return;
		
		double time = Sim_system.clock();
		stats.update(measureName, size(), lastUpdateTime); //continuous
		lastUpdateTime = time;
	}
	
}

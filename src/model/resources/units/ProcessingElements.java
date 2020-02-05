package model.resources.units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import model.resources.ResourceStatus;
import model.resources.ResourceType;
import model.resources.computing.ComputingResource;

public class ProcessingElements extends PEUnit implements List<ComputingResource> {

	protected List<ComputingResource> resources;

	public ProcessingElements(){
		super();
		resources =  new ArrayList<ComputingResource>(1);
		provisioner = new ProcessingElementsResourceUnitProvisioner(ResourceUnitState.FREE, 0);
	}
	
	public ProcessingElements(String resName){
		this();
		resourceId = resName;
	}
	
	public ProcessingElements(List<ComputingResource> resources){
		this();
		this.resources = resources;
	}
	
	public ProcessingElements(String resName, List<ComputingResource> resources){
		this(resName);
		this.resources = resources;
	}

	public ResourceType getResourceType(){
		if(resources != null && resources.size() > 0)
			return resources.get(0).getType();
		else return null;
	}
	
	public int getAmount(){
		return this.resources.size();
	}
	
	public int getSpeed(){
		int peCnt = getAmount();
		
		double avgSpeed = 0;
		for(int i = 0; i < peCnt; i++){
			try {
				avgSpeed += resources.get(i).getResourceCharacteristic().getResourceUnit(StandardResourceUnitName.CPUSPEED).getAmount();
			} catch (NoSuchFieldException e) {
				avgSpeed +=1;
			}
		}
		avgSpeed = avgSpeed / peCnt;
		int speed = (int) Math.round(avgSpeed);
		return speed;
	}

	@Override
	public boolean add(ComputingResource e) {
		if(resources == null)
			resources = new ArrayList<ComputingResource>(1);
		return resources.add(e);

	}

	@Override
	public void add(int index, ComputingResource element) {
		if(resources == null)
			resources = new ArrayList<ComputingResource>(1);
		resources.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends ComputingResource> c) {
		if(resources == null)
			resources = new ArrayList<ComputingResource>(c.size());
		return resources.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends ComputingResource> c) {
		return resources.addAll(index, c);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(Object o) {
		return resources.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return resources.containsAll(c);
	}

	@Override
	public ComputingResource get(int index) {
		return resources.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return resources.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return resources.isEmpty();
	}

	@Override
	public Iterator<ComputingResource> iterator() {
		return resources.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return resources.lastIndexOf(o);
	}

	@Override
	public ListIterator<ComputingResource> listIterator() {
		return resources.listIterator();
	}

	@Override
	public ListIterator<ComputingResource> listIterator(int index) {
		return resources.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ComputingResource remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ComputingResource set(int index, ComputingResource element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return resources.size();
	}

	@Override
	public List<ComputingResource> subList(int fromIndex, int toIndex) {
		return resources.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return resources.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return resources.toArray(a);
	}

	
	protected int getAmount(ResourceStatus status){
		int sum = 0;
		for(int i = 0; i < this.resources.size(); i++){
			if(resources.get(i).getStatus() == status){
				sum++;
			}
		}
		return sum;
	}
	
	public int getFreeAmount(){
		return getAmount(ResourceStatus.FREE);
	}

	public int getUsedAmount(){
		return getAmount(ResourceStatus.BUSY);
	}
	
	public void setUsedAmount(int amount) {
		int cnt = getAmount(ResourceStatus.BUSY);
		int delta = amount - cnt;

		//allocate resources
		if(delta > 0){
				for(int i = 0; i < this.resources.size() && delta > 0; i++){
					ComputingResource r = this.resources.get(i);
					if(r.getStatus() == ResourceStatus.FREE || r.getStatus() == ResourceStatus.PENDING){
						r.setStatus(ResourceStatus.BUSY);
						delta--;
					}
				}
		}
		//free resources
		else if(delta < 0) {
			for(int i = this.resources.size() - 1; i >= 0 && delta < 0; i--){
				ComputingResource r = this.resources.get(i);
				if(r.getStatus() == ResourceStatus.BUSY){
					r.setStatus(ResourceStatus.FREE);
					delta++;
				}
			}
		}
	}
	
	public ProcessingElements replicate(int amount){
		List<ComputingResource> compResources =  new ArrayList<ComputingResource>(amount);
		Iterator<ComputingResource> it = resources.iterator();
		amount = Math.min(resources.size(), amount);
		while(it.hasNext() && amount > 0){
			ComputingResource compRes = it.next();
			if(compRes.getStatus() == ResourceStatus.FREE){
				compResources.add(compRes);
				amount--;
			}
		}
		return new ProcessingElements(compResources);
	}

	public ResourceUnitProvisioner getProvisioner() {
		if(provisioner == null){
			provisioner = new ProcessingElementsResourceUnitProvisioner(ResourceUnitState.FREE, 0);
		}
        return provisioner;
	}
	
	class ProcessingElementsResourceUnitProvisioner implements ResourceUnitProvisioner{

		protected ResourceUnitState state;
		protected int pending;

		public ProcessingElementsResourceUnitProvisioner (ResourceUnitState state, int pending) {
			this.state = state;
			this.pending = pending;
		}

		public ResourceUnitState getState() {
			return state;
		}

		public void setState(ResourceUnitState newState) {

			if(newState == ResourceUnitState.FREE){
				setUsedAmount(getUsedAmount() - getAmount());
			} else if(newState == ResourceUnitState.PENDING){
				for (int i = 0; i < resources.size(); i++) {
					ComputingResource pe = resources.get(i);
					if (pe.getStatus() == ResourceStatus.FREE) {
						pe.setStatus(ResourceStatus.PENDING);
					}
				}
				setPending(getPending() + getAmount());
			} else if(state == ResourceUnitState.PENDING && newState == ResourceUnitState.BUSY){
				setPending(getProvisioner().getPending() - getAmount());
				setUsedAmount(getUsedAmount() + getAmount());
			} else if(state == ResourceUnitState.FREE && newState == ResourceUnitState.BUSY){
				setUsedAmount(getUsedAmount() + getAmount());
			}
			state = newState;
		}
		
		public int getPending() {
			return pending;
		}

		public void setPending(int pending) {
			this.pending = pending;
		}
		
	}
	

}

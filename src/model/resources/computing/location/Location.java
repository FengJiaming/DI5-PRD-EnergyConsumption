package model.resources.computing.location;

import model.Parameters;

public class Location {
	
	protected long horizontal;
	protected long vertical;
	protected long depth;
	
	public Location(long horizontal, long vertical, long depth) {
		super();
		this.horizontal = horizontal;
		this.vertical = vertical;
		this.depth = depth;
	}
	
	public long getHorizontal() {
		return horizontal;
	}
	public long getVertical() {
		return vertical;
	}
	public long getDepth() {
		return depth;
	}
	
	public void init(Parameters parameters){
		
	}
	
}

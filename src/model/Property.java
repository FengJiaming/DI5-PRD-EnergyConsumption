package model;

import java.util.ArrayList;

import schemas.StringValueWithUnitType;

public class Property extends ArrayList<StringValueWithUnitType>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String name;

	public Property(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}

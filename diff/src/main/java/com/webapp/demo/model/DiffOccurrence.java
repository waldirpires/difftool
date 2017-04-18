package com.webapp.demo.model;

/**
 * Stores a Diff occurrence (position and size)
 *
 */
public class DiffOccurrence {

	private int position;
	private int size;
	
		
	public DiffOccurrence(int position) {
		super();
		this.position = position;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public void updateSize()
	{
		this.size++;
	}
}

package bgu.atd.a1.sim.privateStates;

import java.util.LinkedList;
import java.util.List;


import bgu.atd.a1.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		super();
		this.availableSpots = -1;
		this.registered = 0;
		this.regStudents = new LinkedList<>();
		this.prequisites = new LinkedList<>();
	}

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrequisites() {
		return prequisites;
	}

	public void setAvailableSpots(Integer availableSpots) {
		this.availableSpots = availableSpots;
	}

	public void registerStudent(String studentId){
		availableSpots--;
		registered++;
		getRegStudents().add(studentId);
	}

	public void unregisterStudent(String studentId){
		availableSpots++;
		registered--;
		getRegStudents().remove(studentId);
	}

	public void setRegistered(Integer registered) {
		this.registered = registered;
	}
}

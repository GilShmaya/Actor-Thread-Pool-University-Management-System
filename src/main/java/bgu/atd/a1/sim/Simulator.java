/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.atd.a1.sim;
import java.io.IOException;
import java.util.HashMap;

import bgu.atd.a1.Action;
import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.actions.OpenANewCourseAction;
import javafx.util.Pair;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	private static ActorThreadPool actorThreadPool;
	private static Input input;
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
		input.phase1.forEach(actorThreadPool.submit());

    }

	private void getAction(Input.ActionArgs actionArgs){
		switch (actionArgs.actionName){
			case "Open Course":
				OpenANewCourseAction(actionArgs.actionName, actionArgs.departmentName, actionArgs.courseName, actionArgs.space);
		}
	}
	
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool = myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	
	
	public static int main(String [] args){

		try{
			input = JsonInputReader.getInputFromJson(args[0]);
		}catch (IOException e){}

		attachActorThreadPool(new ActorThreadPool(input.threads));

		start();
		return 1;
	}
}

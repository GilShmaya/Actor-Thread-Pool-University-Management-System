/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.atd.a1.sim;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import bgu.atd.a1.Action;
import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.actions.*;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;
import javafx.util.Pair;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {


    private static ActorThreadPool actorThreadPool;
    private static Input input;
    public static CountDownLatch phase;


    /**
     * Begin the simulation Should not be called before attachActorThreadPool()
     */

    public static void start() throws InterruptedException {
        actorThreadPool.start();
        WarehousePrivateState warehouse = new WarehousePrivateState();
        phase = new CountDownLatch(input.computers.size());
        input.computers.forEach(computer -> {
                Action<Boolean> action = new AddComputerAction(computer.computerType, computer);
                actorThreadPool.submit(new SchedulerPhaseAction(null, action, "Warehouse"), "Warehouse", warehouse);
                });
        phase.await();

        phase = new CountDownLatch(input.phase1.size());
        for (Input.ActionArgs action : input.phase1)
            submitAction(action);
        phase.await();
        phase = new CountDownLatch(input.phase2.size());
        for (Input.ActionArgs action : input.phase2)
            submitAction(action);
        phase.await();
        phase = new CountDownLatch(input.phase3.size());
        for (Input.ActionArgs action : input.phase3)
            submitAction(action);
        phase.await();
    }

    private static void submitAction(Input.ActionArgs actionArgs) {
        String actionName = actionArgs.actionName;
        Action<Pair<Boolean, String>> action = null;
        String actorName = null;
        PrivateState actorState = null;
        DepartmentPrivateState departmentPrivateState = actorThreadPool.getPrivateState(actionArgs.departmentName) == null ? new DepartmentPrivateState() : (DepartmentPrivateState) actorThreadPool.getPrivateState(actionArgs.departmentName);
        CoursePrivateState coursePrivateState = actorThreadPool.getPrivateState(actionArgs.courseName) == null ? new CoursePrivateState() : (CoursePrivateState) actorThreadPool.getPrivateState(actionArgs.courseName);
        StudentPrivateState studentPrivateState = actorThreadPool.getPrivateState(actionArgs.studentId) == null ? new StudentPrivateState() : (StudentPrivateState) actorThreadPool.getPrivateState(actionArgs.studentId);

        switch (actionName) {
            case "Open Course":
                action = new OpenANewCourseAction(actionArgs.departmentName, actionArgs.courseName, actionArgs.space, actionArgs.prerequisites);
                actorName = actionArgs.departmentName;
                actorState = departmentPrivateState;
                break;
            case "Add Student":
                action = new AddStudentAction(actionArgs.departmentName, actionArgs.studentId);
                actorName = actionArgs.departmentName;
                actorState = departmentPrivateState;
                break;
            case "Participate In Course":
                action = new ParticipatingInCourseAction(actionArgs.studentId, actionArgs.courseName, actionArgs.grades);
                actorName = actionArgs.courseName;
                actorState = coursePrivateState;
                break;
            case "Unregister":
                action = new UnregisterAction(actionArgs.studentId, actionArgs.courseName);
                actorName = actionArgs.courseName;
                actorState = coursePrivateState;
                break;
            case "Close Course":
                action = new CloseACourseAction(actionArgs.departmentName, actionArgs.courseName);
                actorName = actionArgs.departmentName;
                actorState = departmentPrivateState;
                break;
            case "Add Spaces":
                action = new OpenNewPlacesInACourseAction(actionArgs.courseName, actionArgs.newAvailablePlaces);
                actorName = actionArgs.courseName;
                actorState = coursePrivateState;
                break;
            case "Administrative Check":
                action = new CheckAdministrativeObligationsAction(actionArgs.departmentName, actionArgs.studentsId, actionArgs.computer, actionArgs.conditions);
                actorName = actionArgs.departmentName;
                actorState = departmentPrivateState;
                break;
            case "Register With Preferences":
                action = new RegisterWithPreferencesAction(actionArgs.studentId, actionArgs.preferences, actionArgs.grades);
                actorName = actionArgs.studentId;
                actorState = studentPrivateState;
                break;
        }

        actorThreadPool.submit(new SchedulerPhaseAction(actionName, action, actorName), actorName, actorState);
    }

    /**
     * attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
     *
     * @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
     */
    public static void attachActorThreadPool(ActorThreadPool myActorThreadPool) {
        actorThreadPool = myActorThreadPool;
    }

    /**
     * shut down the simulation
     * returns list of private states
     */
    public static HashMap<String, PrivateState> end() throws InterruptedException {
        actorThreadPool.shutdown();
        return (HashMap<String, PrivateState>) actorThreadPool.getActors();
    }


    public static void main(String[] args) throws InterruptedException, IOException {

        try {
            input = JsonInputReader.getInputFromJson(args[0]);
        } catch (IOException e) {
        }

        attachActorThreadPool(new ActorThreadPool(input.threads));

        start();

        // serialized object to the file "result.ser"
        Map<String, PrivateState> simulatorResult = end();
        FileOutputStream outputFile = new FileOutputStream("result.ser");
        ObjectOutputStream oos = new ObjectOutputStream(outputFile);
        oos.writeObject(simulatorResult);
    }
}

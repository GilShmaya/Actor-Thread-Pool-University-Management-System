package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UnregisterStudentsAndCloseCourseAction extends Action<Boolean> {
    private final String departmentName;
    private final String courseName;

    public UnregisterStudentsAndCloseCourseAction(String departmentName, String courseNae) {
        this.departmentName = departmentName;
        this.courseName = courseNae;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof CoursePrivateState))
            throw new IllegalAccessException("The actor should be in type Course");
        CoursePrivateState courseActorState = (CoursePrivateState) actorState;

        if (courseActorState.getAvailableSpots() == -1) {
            complete(false);
            return;
        }
        if (courseActorState.getRegistered() == 0) {
            courseActorState.setAvailableSpots(-1);
            complete(true);
            return;
        }

        List<UnregisterAction> actionsDependency1 =
                courseActorState.getRegStudents()
                        .stream()
                        .map(student ->
                                new UnregisterAction(student, courseName))
                        .collect(Collectors.toList());


        then(actionsDependency1, () -> {
            courseActorState.setAvailableSpots(-1);
            complete(true);
        });

        actionsDependency1
                .forEach(action -> sendMessage(action, courseName, courseActorState));


    }
}

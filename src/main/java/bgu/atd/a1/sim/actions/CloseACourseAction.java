package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class CloseACourseAction extends Action<Pair<Boolean, String>> {
    private String departmentName;
    private String courseName;

    public CloseACourseAction(String departmentName, String courseName) {
        this.departmentName = departmentName;
        this.courseName = courseName;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof DepartmentPrivateState))
            throw new IllegalAccessException("The actor should be in type Department");

        DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState) actorState;

        if (!departmentPrivateState.getCourseList().contains(courseName)) {
            complete(new Pair<>(false, "Failed, the course is not in the department"));
            return;
        }

        List<Action<Boolean>> actionsDependency = new LinkedList<>();
        Action<Boolean> unregisterStudentsAndCloseCourseAction = new UnregisterStudentsAndCloseCourseAction(departmentName, courseName);
        actionsDependency.add(unregisterStudentsAndCloseCourseAction);

        then(actionsDependency, () -> {
            departmentPrivateState.getCourseList().remove(courseName);
            complete(new Pair<>(true, "The course " + courseName + " removed successfully"));
        });
        sendMessage(unregisterStudentsAndCloseCourseAction, courseName, pool.getPrivateState(courseName));

    }
}

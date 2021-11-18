package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CloseACourseAction extends Action<String> {
    private final String departmentName;
    private final String courseName;

    public CloseACourseAction(String actionName, String departmentName, String courseNae) {
        setActionName(actionName);
        this.departmentName = departmentName;
        this.courseName = courseNae;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof CoursePrivateState))
            throw new IllegalAccessException("The actor should be in type Course");
        CoursePrivateState courseActorState = (CoursePrivateState) actorState;

        if (courseActorState.getAvailableSpots() == -1)
            complete("The course is already closed");

        List<UnregisterAction> actionsDependency1 =
                courseActorState.getRegStudents()
                        .stream()
                        .map(student ->
                                new UnregisterAction("Unregister Name", student, courseName))
                        .collect(Collectors.toList());
        RemoveCourseFromDepartmentAction removeCourseFromDepartmentAction = new RemoveCourseFromDepartmentAction(
                "Remove Course From Department Action", departmentName, courseName);
        List<Action<Boolean>> actionsDependency2 = new LinkedList<>();
        actionsDependency2.add(removeCourseFromDepartmentAction);

        then(actionsDependency1, () -> {
            if (actionsDependency1.stream().allMatch(action -> Boolean.parseBoolean(action.getResult().get()))) {
                courseActorState.setAvailableSpots(-1);
                then(actionsDependency2, () -> {
                    if (actionsDependency2.get(0).getResult().get())
                        complete("The course " + courseName + " removed successfully");
                    else {
                        complete("Failed to close the course " + courseName + ".");
                    }
                });
                sendMessage(removeCourseFromDepartmentAction, departmentName, pool.getPrivateState(departmentName));
            } else {
                complete("Failed to close the course " + courseName + ".");
            }
        });
        actionsDependency1
                .forEach(action -> sendMessage(action, action.getStudentId(), pool.getPrivateState(action.getStudentId())));


    }
}

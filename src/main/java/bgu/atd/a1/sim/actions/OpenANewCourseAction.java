package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class OpenANewCourseAction extends Action<Pair<Boolean, String>> {
    private final String departmentName;
    private final String courseName;
    private final int space;
    private final List<String> prerequisites;

    public OpenANewCourseAction(String actionName, String departmentName, String courseName, int space, List<String> prerequisites) {
        setActionName(actionName);
        this.departmentName = departmentName;
        this.courseName = courseName;
        this.space = space;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(actorState instanceof DepartmentPrivateState))
            throw new IllegalAccessException("The actor should be in type Department");
        List<Action<Boolean>> actionsDependency = new ArrayList<>();
        Action<Boolean> setCourseDetailsAction = new InitializeCourseDetailsAction("Initialize Course Details", departmentName, courseName, space, prerequisites);
        actionsDependency.add(setCourseDetailsAction);
        then(actionsDependency, () -> {
                    Boolean confirmationResult = actionsDependency.get(0).getResult().get();
                    if (confirmationResult) {
                        ((DepartmentPrivateState) actorState).getCourseList().add(courseName);
                        complete(new Pair<>(true, "The new course " + courseName + " opened successfully"));
                    }
                    else
                        complete(new Pair<>(false, "Failed to open the new course " + courseName + ". The course id already opened"));
                }
        );
        sendMessage(setCourseDetailsAction, courseName, new CoursePrivateState());
    }
}

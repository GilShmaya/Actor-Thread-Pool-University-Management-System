package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.List;

public class InitializeCourseDetailsAction extends Action<Boolean> {
    private final String departmentName;
    private final String courseName;
    private final int space;
    private final List<String> prerequisites;

    public InitializeCourseDetailsAction(String departmentName, String courseName, int space, List<String> prerequisites) {
        this.departmentName = departmentName;
        this.courseName = courseName;
        this.space = space;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof CoursePrivateState))
            throw new IllegalAccessException("The actor should be in type Course");
        if (((CoursePrivateState) actorState).getAvailableSpots() != -1) // course has been initialized already
            complete(false);
        else {
            ((CoursePrivateState) actorState).getPrequisites().addAll(prerequisites);
            ((CoursePrivateState) actorState).setAvailableSpots(space);
            complete(true);
        }
    }
}

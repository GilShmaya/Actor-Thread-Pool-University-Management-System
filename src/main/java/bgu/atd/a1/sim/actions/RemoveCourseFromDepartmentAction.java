package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

public class RemoveCourseFromDepartmentAction extends Action<Boolean> {
    private String departmentName;
    private String courseName;

    public RemoveCourseFromDepartmentAction(String departmentName, String courseName) {
        this.departmentName = departmentName;
        this.courseName = courseName;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(actorState instanceof DepartmentPrivateState))
            throw new IllegalAccessException("The actor should be in type Department");

        DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState) actorState;

        if(departmentPrivateState.getCourseList().contains(courseName)){
            departmentPrivateState.getCourseList().remove(courseName);
            complete(true);
        }
        complete(false);
    }
}

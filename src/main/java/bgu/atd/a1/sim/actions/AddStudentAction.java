package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.List;

public class AddStudentAction extends Action<String> {
    private final String departmentName;
    private final String studentId;

    public AddStudentAction(String actionName, String departmentName, String studentId) {
        setActionName(actionName);
        this.departmentName = departmentName;
        this.studentId = studentId;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(actorState instanceof DepartmentPrivateState))
            throw new IllegalAccessException("The actor should be in type Department");
        List<String> studentList = ((DepartmentPrivateState) actorState).getStudentList();
        if (studentList.contains(studentId))
            complete("Failed adding student to department. The student with Id " + studentId + " has been already added to " + departmentName + " department");
        else {
            studentList.add(studentId);
            complete("The student with Id " + studentId + " was added successfully to " + departmentName + " department");
        }

    }
}

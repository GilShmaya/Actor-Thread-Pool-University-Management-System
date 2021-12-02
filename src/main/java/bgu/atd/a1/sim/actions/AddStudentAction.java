package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import javafx.util.Pair;

import java.util.List;

public class AddStudentAction extends Action<Pair<Boolean, String>> {
    private final String departmentName;
    private final String studentId;

    public AddStudentAction(String departmentName, String studentId) {
        this.departmentName = departmentName;
        this.studentId = studentId;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(actorState instanceof DepartmentPrivateState))
            throw new IllegalAccessException("The actor should be in type Department");
        List<String> studentList = ((DepartmentPrivateState) actorState).getStudentList();
        if (studentList.contains(studentId))
            complete(new Pair<>(false, "Failed adding student to department. The student with Id " + studentId + " has been already added to " + departmentName + " department"));
        else {
            StudentPrivateState studentPrivateState = pool.getPrivateState(studentId) == null ? new StudentPrivateState() : (StudentPrivateState) pool.getPrivateState(studentId);
            sendMessage(new InitializeStudentDetailsAction(), studentId, studentPrivateState);
            studentList.add(studentId);
            complete(new Pair<>(true, "The student with Id " + studentId + " was added successfully to " + departmentName + " department"));
        }
    }
}

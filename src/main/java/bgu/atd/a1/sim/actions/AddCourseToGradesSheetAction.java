package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;


public class AddCourseToGradesSheetAction extends Action<Boolean> {
    private String studentId;
    private String courseName;
    private Integer grade;

    public AddCourseToGradesSheetAction(String actionName, String studentId, String courseName, Integer grade) {
        setActionName(actionName);
        this.studentId = studentId;
        this.courseName = courseName;
        this.grade = grade;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof StudentPrivateState))
            throw new IllegalAccessException("The actor should be in type Student");
        StudentPrivateState studentActorState = (StudentPrivateState) actorState;

        studentActorState.getGrades().put(courseName, grade);
        complete(true);
    }
}

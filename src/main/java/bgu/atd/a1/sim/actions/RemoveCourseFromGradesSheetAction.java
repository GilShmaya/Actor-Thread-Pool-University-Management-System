package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class RemoveCourseFromGradesSheetAction extends Action<Boolean> {
    private String studentId;
    private String courseName;

    public RemoveCourseFromGradesSheetAction(String actionName, String studentId, String courseName) {
        setActionName(actionName);
        this.studentId = studentId;
        this.courseName = courseName;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof StudentPrivateState))
            throw new IllegalAccessException("The actor should be in type Student");
        StudentPrivateState studentActorState = (StudentPrivateState) actorState;

        if(studentActorState.getGrades().containsKey(courseName)){
            studentActorState.getGrades().remove(courseName);
            complete(true);
        }
        else{
            complete(false);
        }
    }
}

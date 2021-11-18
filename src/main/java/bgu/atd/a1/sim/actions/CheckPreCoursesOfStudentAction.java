package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.List;

public class CheckPreCoursesOfStudentAction extends Action<Boolean> {
    private String studentId;
    private List<String> prequisites;

    public CheckPreCoursesOfStudentAction(String studentId, List<String> prequisites) {
        this.studentId = studentId;
        this.prequisites = prequisites;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof StudentPrivateState))
            throw new IllegalAccessException("The actor should be in type Student");
        StudentPrivateState studentActorState = (StudentPrivateState) actorState;

        Boolean hasAllPreCourses = prequisites.stream().allMatch(pre -> studentActorState.getGrades().containsKey(pre));
        complete(hasAllPreCourses);
    }
}

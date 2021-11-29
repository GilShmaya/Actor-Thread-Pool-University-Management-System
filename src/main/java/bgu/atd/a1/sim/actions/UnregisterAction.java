package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class UnregisterAction extends Action<Pair<Boolean, String>> {
    private final String studentId;
    private final String courseName;

    public UnregisterAction(String actionName, String studentId, String courseName) {
        setActionName(actionName);
        this.studentId = studentId;
        this.courseName = courseName;
    }

    public String getStudentId() {
        return studentId;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof CoursePrivateState))
            throw new IllegalAccessException("The actor should be in type Course");
        CoursePrivateState courseActorState = (CoursePrivateState) actorState;

        PrivateState studentPrivateState = pool.getPrivateState(studentId);
        if (studentPrivateState == null || !(studentPrivateState instanceof StudentPrivateState))
            complete(new Pair<>(false, "Failed in unregistering student with Id " + studentId + " to " + courseName + " Course. Student is not found"));

        if (!courseActorState.getRegStudents().contains(studentId))
            complete(new Pair<>(false, "The student with Id " + studentId + " is not registered to " + courseName + " Course."));

        List<Action<Boolean>> actionsDependency = new ArrayList<>();
        Action<Boolean> removeCourseFromGradesSheetAction = new RemoveCourseFromGradesSheetAction(
                "Remove Course From Grades Sheet Action", studentId, courseName);
        actionsDependency.add(removeCourseFromGradesSheetAction);

        then(actionsDependency, () -> {
            if (actionsDependency.get(0).getResult().get()) {
                courseActorState.unregisterAvailableSpots(studentId);
                complete(new Pair<>(true, "The student with Id " + studentId + "is unregistered from the course with Id " + courseName + " successfully"));
            } else {
                complete(new Pair<>(false, "Failed in unregistering student with Id " + studentId + " to " + courseName + " Course. The student is already unregistered"));
            }
        });
        sendMessage(removeCourseFromGradesSheetAction, studentId, studentPrivateState);
    }
}

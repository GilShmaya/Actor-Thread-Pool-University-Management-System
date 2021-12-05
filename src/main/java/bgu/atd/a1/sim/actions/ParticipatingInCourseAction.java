package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ParticipatingInCourseAction extends Action<Pair<Boolean, String>> {
    private String studentId;
    private String courseName;
    private Integer grade;

    public ParticipatingInCourseAction(String studentId, String courseName, List<String> grades) {
        this.studentId = studentId;
        this.courseName = courseName;
        this.grade = grades.get(0).equals("-") ? -1 : Integer.parseInt(grades.get(0));
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof CoursePrivateState))
            throw new IllegalAccessException("The actor should be in type Course");
        CoursePrivateState courseActorState = (CoursePrivateState) actorState;

        if (courseActorState.getAvailableSpots() == -1) {
            complete(new Pair<>(false, "The course is closed"));
            return;
        }
        if (courseActorState.getRegStudents().contains(studentId)) {
            complete(new Pair<>(false, "The student with Id " + studentId + " is already registered to " + courseName + " Course."));
            return;
        }
        List<Action<Boolean>> actionsDependency1 = new ArrayList<>();
        List<Action<Boolean>> actionsDependency2 = new ArrayList<>();
        Action<Boolean> checkPreCoursesOfStudentAction = new CheckPreCoursesOfStudentAction(studentId, courseActorState.getPrequisites());
        Action<Boolean> addCourseToGradesSheetAction = new AddCourseToGradesSheetAction(studentId, courseName, grade);
        actionsDependency1.add(checkPreCoursesOfStudentAction);
        actionsDependency2.add(addCourseToGradesSheetAction);
        PrivateState studentState = pool.getPrivateState(studentId) == null ? new StudentPrivateState() : (StudentPrivateState) pool.getPrivateState(studentId);

        then(actionsDependency1, () -> {
            if (actionsDependency1.get(0).getResult().get()) { // the student has all pre courses
                if (courseActorState.getAvailableSpots() <= 0) {
                    complete(new Pair<>(false, "Failed in registering student with Id " + studentId + " to " + courseName + "Course. There are no available spots."));
                } else {
                    courseActorState.registerStudent(studentId);
                    then(actionsDependency2, () -> {
                        if (actionsDependency2.get(0).getResult().get()) { // the course has been added to the student's grades sheet
                            complete(new Pair<>(true, "The student with Id " + studentId + " is registered to the course with Id " + courseName + "successfully"));
                        } else {
                            complete(new Pair<>(false, "Failed in registering student with Id " + studentId + " to " + courseName + "Course."));
                        }
                    });
                    sendMessage(addCourseToGradesSheetAction, studentId, studentState);
                }
            } else {
                complete(new Pair<>(false, "Failed in registering student with Id " + studentId + " to " + courseName + "Course. Some pre courses are missing"));
            }
        });

        sendMessage(checkPreCoursesOfStudentAction, studentId, studentState);

    }
}

package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.util.ArrayList;
import java.util.List;

public class ParticipatingInCourseAction extends Action<String> {
    private String studentId;
    private String courseName;
    private Integer grade;

    public ParticipatingInCourseAction(String actionName, String studentId, String courseName, List<String> grades) {
        setActionName(actionName);
        this.studentId = studentId;
        this.courseName = courseName;
        this.grade = grades.get(0).equals("-") ? null : Integer.parseInt(grades.get(0));
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof CoursePrivateState))
            throw new IllegalAccessException("The actor should be in type Course");
        CoursePrivateState courseActorState = (CoursePrivateState) actorState;

        if(courseActorState.getAvailableSpots() == -1)
            complete("The course is closed");

        if (courseActorState.getRegStudents().contains(studentId))
            complete("The student with Id " + studentId + " is already registered to " + courseName + "Course.");

        List<Action<Boolean>> actionsDependency1 = new ArrayList<>();
        List<Action<Boolean>> actionsDependency2 = new ArrayList<>();
        Action<Boolean> checkPreCoursesOfStudentAction = new CheckPreCoursesOfStudentAction(studentId, courseActorState.getPrequisites());
        Action<Boolean> addCourseToGradesSheetAction = new AddCourseToGradesSheetAction(
                "Add Course To Grades Sheet Action", studentId, courseName, grade);
        actionsDependency1.add(checkPreCoursesOfStudentAction);
        actionsDependency2.add(addCourseToGradesSheetAction);
        PrivateState studentState = new StudentPrivateState();

        then(actionsDependency1, () -> {
            if (actionsDependency1.get(0).getResult().get()) { // the student has all pre courses
                if (courseActorState.getAvailableSpots() <= 0) {
                    complete("Failed in registering student with Id " + studentId + " to " + courseName + "Course. There are no available spots.");
                } else {
                    courseActorState.registerStudent(studentId);
                    then(actionsDependency2, () -> {
                        if (actionsDependency1.get(0).getResult().get()) { // the course has been added to the student's grades sheet
                            complete("The student with Id " + studentId + " is registered to the course with Id " + courseName + "successfully");
                        } else {
                            complete("Failed in registering student with Id " + studentId + " to " + courseName + "Course.");
                        }
                    });
                    sendMessage(addCourseToGradesSheetAction, studentId, studentState);
                }
            } else {
                complete("Failed in registering student with Id " + studentId + " to " + courseName + "Course. Some pre courses are missing");
            }
        });
        sendMessage(checkPreCoursesOfStudentAction, studentId, studentState);
    }
}

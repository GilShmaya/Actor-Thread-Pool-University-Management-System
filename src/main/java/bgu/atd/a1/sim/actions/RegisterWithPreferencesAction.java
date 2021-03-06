package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class RegisterWithPreferencesAction extends Action<Pair<Boolean, String>> {
    private final String studentId;
    private final List<String> preferences;
    private final List<String> grades;

    public RegisterWithPreferencesAction(String studentId, List<String> preferences, List<String> grades) {
        this.studentId = studentId;
        this.preferences = preferences;
        this.grades = grades;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof StudentPrivateState))
            throw new IllegalAccessException("The actor should be in type Student");
        StudentPrivateState studentActorState = (StudentPrivateState) actorState;

        CoursePrivateState courseState = null;
        while (preferences != null && !preferences.isEmpty() && (courseState = (CoursePrivateState) pool.getPrivateState(preferences.get(0))) == null) {
            preferences.remove(0);
            grades.remove(0);
        }

        if (courseState != null) {
            List<Action<Pair<Boolean, String>>> actionsDependency1 = new ArrayList<>();
            Action<Pair<Boolean, String>> participatingInCourseAction = new ParticipatingInCourseAction(studentId, preferences.get(0), grades.subList(0, 1));
            actionsDependency1.add(participatingInCourseAction);
            CoursePrivateState finalCourseState = courseState;
            then(actionsDependency1, () -> {
                if (actionsDependency1.get(0).getResult().get().getKey()) {
                    complete(new Pair<>(true, "The student register successfully to the course " + preferences.get(0)));
                } else { // failed in registering to the first course in the preference list, try the next one
                    preferences.remove(0);
                    grades.remove(0);
                    List<Action<Pair<Boolean, String>>> actionsDependency2 = new ArrayList<>();
                    Action<Pair<Boolean, String>> newRegisteringAttempt = new RegisterWithPreferencesAction(studentId, preferences, grades);
                    actionsDependency2.add(newRegisteringAttempt);
                    then(actionsDependency2, () -> {
                        complete(new Pair<>(true, "Done action"));
                    });
                    sendMessage(newRegisteringAttempt, studentId, studentActorState);
                }
            });
            sendMessage(participatingInCourseAction, preferences.get(0), courseState);
        } else {
            complete(new Pair<>(false, "Unable to register to any of the courses in the preference list"));
        }

    }
}

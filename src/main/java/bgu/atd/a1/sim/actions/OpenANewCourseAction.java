package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

public class OpenANewCourseAction extends Action<String> {
    private String departmentName;
    private String courseName;
    private int space;
    private String[] Prerequisites;

    public OpenANewCourseAction(String actionName, String departmentName, String courseName, int space, String[] prerequisites) {
        setActionName(actionName);
        this.departmentName = departmentName;
        this.courseName = courseName;
        this.space = space;
        Prerequisites = prerequisites;
    }

    @Override
    protected void start() {
        Action<String> openNewPlaceInCourseAction = new OpenNewPlacesInACourseAction()
        sendMessage(OpenNewPlacesInACourseAction, courseName, new CoursePrivateState());

    }
}

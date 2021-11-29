package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import javafx.util.Pair;

public class OpenNewPlacesInACourseAction extends Action<Pair<Boolean, String>> {
    private final String courseName;
    private final int newAvailablePlaces;

    public OpenNewPlacesInACourseAction(String actionName, String courseName, int newAvailablePlaces) {
        setActionName(actionName);
        this.courseName = courseName;
        this.newAvailablePlaces = newAvailablePlaces;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof CoursePrivateState))
            throw new IllegalAccessException("The actor should be in type Course");
        ((CoursePrivateState) ((CoursePrivateState) actorState)).setAvailableSpots(newAvailablePlaces);
        complete(new Pair<>(true, "The number of available spaces for the course " + courseName + " was increase to " + newAvailablePlaces));
    }
}

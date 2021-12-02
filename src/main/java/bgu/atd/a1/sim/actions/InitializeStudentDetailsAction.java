package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class InitializeStudentDetailsAction extends Action<Boolean> {

    public InitializeStudentDetailsAction() {
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof StudentPrivateState))
            throw new IllegalAccessException("The actor should be in type Student");
        complete(true);
    }
}

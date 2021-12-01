package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;

import java.util.List;

public class CheckStudentObligationAction extends Action<Boolean> {
    private final Computer computer;
    private final List<String> conditions;

    public CheckStudentObligationAction(String actionName, Computer computer, List<String> conditions) {
        setActionName(actionName);
        this.computer = computer;
        this.conditions = conditions;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof StudentPrivateState))
            throw new IllegalAccessException("The actor should be in type Student");

        StudentPrivateState studentActorState = (StudentPrivateState) actorState;
        studentActorState.setSignature(computer.checkAndSign(conditions, studentActorState.getGrades()));
        complete(true);
    }
}

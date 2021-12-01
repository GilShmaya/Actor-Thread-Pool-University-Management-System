package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Simulator;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class SchedulerPhaseAction extends Action<Boolean> {
    private final Action<Pair<Boolean, String>> action;
    private final String actorName;

    public SchedulerPhaseAction(Action<Pair<Boolean, String>> action, String actorName) {
        setActionName("Scheduler");
        this.action = action;
        this.actorName = actorName;
    }

    @Override
    protected void start() throws IllegalAccessException {
        List<Action<Pair<Boolean, String>>> actionsDependency = new ArrayList<>();
        actionsDependency.add(action);
        then(actionsDependency, () -> {
            Simulator.phase.countDown();
            complete(true);
        });
        sendMessage(action, actorName, actorState);
    }
}

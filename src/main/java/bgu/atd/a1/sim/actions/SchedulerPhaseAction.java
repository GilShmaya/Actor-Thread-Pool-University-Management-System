package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Simulator;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class SchedulerPhaseAction extends Action<Boolean> {
    private final Action<Pair<Boolean, String>> action;
    private final String actorName;

    public SchedulerPhaseAction(String actionName, Action<Pair<Boolean, String>> action, String actorName) {
        setActionName(actionName);
        this.action = action;
        this.actorName = actorName;
    }

    @Override
    protected void start() throws IllegalAccessException {
        List<Action<Pair<Boolean, String>>> actionsDependency = new ArrayList<>();
        actionsDependency.add(action);
        then(actionsDependency, () -> {
            complete(true);
            Simulator.phase.countDown();
        });
        sendMessage(action, actorName, actorState);
    }
}

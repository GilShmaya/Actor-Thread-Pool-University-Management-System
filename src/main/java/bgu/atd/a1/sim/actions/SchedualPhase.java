package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Simulator;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class SchedualPhase extends Action<Boolean>{
            private final Action<Pair<Boolean,String>> action;
            private final String actorName;

    public SchedualPhase(Action<Pair<Boolean, String>> action, String actorName) {
        setActionName("schedular");
        this.action = action;
        this.actorName = actorName;
    }

    @Override
    protected void start() throws IllegalAccessException {
        List<Action<Pair<Boolean, String>>> actionList = new ArrayList<>();
        actionList.add(action);
        then(actionList, () -> {
            Simulator.phase.countDown();
            complete(true);
        });
        sendMessage(action,actorName,actorState);
    }
}

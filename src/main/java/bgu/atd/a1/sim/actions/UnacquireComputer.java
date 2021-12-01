package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;

public class UnacquireComputer extends Action<Boolean> {
    private final String departmentName;
    private final String computerName;

    public UnacquireComputer(String actionName, String departmentName, String computerName) {
        this.departmentName = departmentName;
        this.computerName = computerName;
    }

    @Override
    protected void start() throws IllegalAccessException {
        WarehousePrivateState warehouseActorState = (WarehousePrivateState) actorState;
        warehouseActorState.unAcquired(computerName);
        complete(true);
    }
}

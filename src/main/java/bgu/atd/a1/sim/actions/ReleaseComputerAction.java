package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;

public class ReleaseComputerAction extends Action<Boolean> {
    private final String departmentName;
    private final String computerName;

    public ReleaseComputerAction(String departmentName, String computerName) {
        this.departmentName = departmentName;
        this.computerName = computerName;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof WarehousePrivateState))
            throw new IllegalAccessException("The actor should be in type Warehouse");

        WarehousePrivateState warehouseActorState = (WarehousePrivateState) actorState;
        warehouseActorState.release(computerName);
        complete(true);
    }
}

package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;

public class AcquireComputerAction extends Action<Computer> {
    private final String departmentName;
    private final String computerName;

    public AcquireComputerAction(String departmentName, String computerName) {
        this.departmentName = departmentName;
        this.computerName = computerName;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof WarehousePrivateState))
            throw new IllegalAccessException("The actor should be in type Warehouse");

        WarehousePrivateState warehouseActorState = (WarehousePrivateState) actorState;
        Computer computer = warehouseActorState.acquired(computerName, departmentName);
        if (computer == null) { // the computer is not available, try later
            sendMessage(this, "Warehouse", warehouseActorState);
        } else {
            complete(computer);
        }
    }
}

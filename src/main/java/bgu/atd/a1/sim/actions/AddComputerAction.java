package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;

public class AddComputerAction extends Action<Boolean> {
    private final String computerName;
    private final Computer computer;

    public AddComputerAction(String computerName, Computer computer) {
        this.computerName = computerName;
        this.computer = computer;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if (!(actorState instanceof WarehousePrivateState))
            throw new IllegalAccessException("The actor should be in type Warehouse");

        WarehousePrivateState warehouseActorState = (WarehousePrivateState) actorState;
        warehouseActorState.addComputer(computerName, computer);
        complete(true);
    }
}

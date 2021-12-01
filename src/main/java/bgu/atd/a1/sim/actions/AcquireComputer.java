package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public class AcquireComputer extends Action<Boolean> {
    private final String departmentName;
    private final String computerName;

    public AcquireComputer(String actionName, String departmentName, String computerName) {
        this.departmentName = departmentName;
        this.computerName = computerName;
    }

    @Override
    protected void start() throws IllegalAccessException {
        WarehousePrivateState warehouseActorState = (WarehousePrivateState) actorState;
        complete(warehouseActorState.acquired(computerName, departmentName));
    }
}

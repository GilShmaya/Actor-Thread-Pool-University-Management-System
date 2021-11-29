package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;
import javafx.util.Pair;

import java.util.List;

public class AnnounceTheEndOfRegistrationPeriodAction extends Action<Pair<Boolean, String>> {
    private final String departmentName;
    private final List<String> students;
    private final String computer;
    private final List<String> conditions;

    public AnnounceTheEndOfRegistrationPeriodAction(String actionName, String departmentName, List<String> students, String computer, List<String> conditions) {
        setActionName(actionName);
        this.departmentName = departmentName;
        this.students = students;
        this.computer = computer;
        this.conditions = conditions;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(actorState instanceof DepartmentPrivateState))
            throw new IllegalAccessException("The actor should be in type Department");

        PrivateState warehousePrivateState = pool.getPrivateState("warehouse");
        if (warehousePrivateState == null || !(warehousePrivateState instanceof WarehousePrivateState))
            complete(new Pair<>(false, "Warehouse is not exist"));




    }
}

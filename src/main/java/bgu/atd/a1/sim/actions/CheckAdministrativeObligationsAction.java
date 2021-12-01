package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CheckAdministrativeObligationsAction extends Action<Pair<Boolean, String>> {
    private final String departmentName;
    private final List<String> students;
    private final String computerName;
    private final List<String> conditions;

    public CheckAdministrativeObligationsAction(String actionName, String departmentName, List<String> students, String computerName, List<String> conditions) {
        setActionName(actionName);
        this.departmentName = departmentName;
        this.students = students;
        this.computerName = computerName;
        this.conditions = conditions;
    }

    @Override
    protected void start() throws IllegalAccessException {
        if(!(actorState instanceof DepartmentPrivateState))
            throw new IllegalAccessException("The actor should be in type Department");

        PrivateState warehousePrivateState = pool.getPrivateState("warehouse");
        if (warehousePrivateState == null || !(warehousePrivateState instanceof WarehousePrivateState))
            complete(new Pair<>(false, "Warehouse is not exist"));

        List<Action<Boolean>> actionsDependency = new ArrayList<>();
        Action<Boolean> acquireComputer = new AcquireComputer("Acquire computer", departmentName, computerName);
        actionsDependency.add(acquireComputer);

        then(actionsDependency, ()-> {
            if(actionsDependency.get(0).getResult().get()){

            }
            else{
                complete(new Pair<>(false, "Failed in checking administrative obligations, computer is not available"));
            }
        });
        sendMessage(acquireComputer, "Warehouse", warehousePrivateState);

    }

package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
        if (!(actorState instanceof DepartmentPrivateState))
            throw new IllegalAccessException("The actor should be in type Department");

        PrivateState warehousePrivateState = pool.getPrivateState("warehouse");
        if (warehousePrivateState == null || !(warehousePrivateState instanceof WarehousePrivateState))
            complete(new Pair<>(false, "Warehouse is not exist"));

        List<Action<Computer>> actionsDependency = new ArrayList<>();
        Action<Computer> acquireComputer = new AcquireComputerAction("Acquire computer", departmentName, computerName);
        actionsDependency.add(acquireComputer);

        then(actionsDependency, () -> {
            Computer computer = actionsDependency.get(0).getResult().get();
            if (computer != null) {
                List<Action<Boolean>> studentActionsDependency = students
                        .stream()
                        .map(student -> new CheckStudentObligationAction("Check Student Obligation", computer, conditions))
                        .collect(Collectors.toList());
                then(studentActionsDependency, () -> {
                    sendMessage(new ReleaseComputerAction("Release Computer", departmentName, computerName), "Warehouse", warehousePrivateState);
                    complete(new Pair<>(true, "Succeed to check administrative obligations"));
                });
                Iterator<Action<Boolean>> studentActionsIterator = studentActionsDependency.stream().iterator();
                students.forEach(student -> sendMessage(studentActionsIterator.next(), student, pool.getPrivateState(student)));
            } else {
                complete(new Pair<>(false, "Failed in checking administrative obligations, computer is null"));
            }
        });
        sendMessage(acquireComputer, "Warehouse", warehousePrivateState);

    }
}

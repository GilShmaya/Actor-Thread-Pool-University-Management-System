package bgu.atd.a1.sim.privateStates;

import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.Computer;

import java.util.HashMap;

/**
 * represents a warehouse that holds a finite amount of computers
 * and their suspended mutexes.
 * releasing and acquiring should be blocking free.
 */
public class WarehousePrivateState extends PrivateState {
    HashMap<String, Computer> computers = new HashMap<>();

    // key: computer name, value: department name or null if available
    HashMap<String, String> acquiredToDepartment = new HashMap<>();

    public WarehousePrivateState() {
        super();
        computers = new HashMap<>();
        acquiredToDepartment = new HashMap<>();
    }

    public void addComputer(String computerName, Computer computer) {
        if (computers.containsKey(computerName))
            throw new IllegalArgumentException("the computer " + computerName + " is already exist");
        computers.put(computerName, computer);
        acquiredToDepartment.put(computerName, null);
    }

    public Boolean isAcquired(String computerName) {
        if (!computers.containsKey(computerName))
            return false;
        return acquiredToDepartment.get(computerName) != null;
    }

    // Return the computer only in case that the computer is available or null if otherwise.
    public Computer acquired(String computerName, String department) {
        if (!computers.containsKey(computerName))
            throw new IllegalArgumentException("the computer is not exist");
        if (!isAcquired(computerName)) {
            acquiredToDepartment.put(computerName, department);
            return computers.get(computerName);
        }
        return null; // the computer is not available
    }

    public void release(String computerName) {
        if (!computers.containsKey(computerName))
            throw new IllegalArgumentException("the computer is not exist");
        acquiredToDepartment.put(computerName, null);
    }
}

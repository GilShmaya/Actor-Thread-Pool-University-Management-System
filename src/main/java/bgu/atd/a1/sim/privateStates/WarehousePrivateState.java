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
    HashMap<String, Boolean> isAcquired = new HashMap<>();

    public WarehousePrivateState() {
        computers = new HashMap<>();
        isAcquired = new HashMap<>();
    }

    public void addComputer(String computerName, Computer computer) {
        if (computerName.contains(computerName))
            throw new IllegalArgumentException("the computer is already exist");
        computers.put(computerName, computer);
        isAcquired.put(computerName, false);
    }

    public Boolean isAcquired(String computerName) {
        if (!computerName.contains(computerName))
            return false;
        return isAcquired.get(computerName);
    }

    public Computer acquired(String computerName) {
        if (!computerName.contains(computerName))
            throw new IllegalArgumentException("the computer is not exist");
        if (!isAcquired(computerName)) {
            isAcquired.put(computerName, true);
            return computers.get(computerName);
        }
        return null; // the computer is not available
    }

    public void unAcquired(String computerName) {
        if (!computerName.contains(computerName))
            throw new IllegalArgumentException("the computer is not exist");
        isAcquired.put(computerName, false);
    }
}

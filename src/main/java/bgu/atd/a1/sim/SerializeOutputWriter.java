package bgu.atd.a1.sim;

import bgu.atd.a1.PrivateState;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class SerializeOutputWriter {
    public static void getStringToSerFile(Simulator simulatorImpl) throws InterruptedException, IOException {
        Map<String, PrivateState> simulatorResult = simulatorImpl.end();
        FileOutputStream outputFile = new FileOutputStream("result.ser");
        ObjectOutputStream oos = new ObjectOutputStream(outputFile);
        oos.writeObject(simulatorResult);
    }
}

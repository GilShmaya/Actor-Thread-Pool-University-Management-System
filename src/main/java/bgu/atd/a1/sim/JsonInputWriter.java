package bgu.atd.a1.sim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class JsonInputWriter {
    public static void getStringToJson(Object obj, String[] args) throws IOException {
        // pretty print
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Java objects to String
        String json = gson.toJson(obj);

        try (FileWriter writer = new FileWriter("output.json")) {
            gson.toJson(obj, writer);
        }

        try (FileWriter writer = new FileWriter(args[1])) {
            gson.toJson(obj, writer);
        }

    }
}

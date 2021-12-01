package bgu.atd.a1.sim;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Input {
    @SerializedName("threads")
    public int threads;
    @SerializedName("Computers")
    public List<Computer> computers;
    @SerializedName("Phase 1")
    public List<ActionArgs> phase1;
    @SerializedName("Phase 2")
    public List<ActionArgs> phase2;
    @SerializedName("Phase 3")
    public List<ActionArgs> phase3;

    public static class ActionArgs {
        @SerializedName("Action")
        public String actionName;

        @SerializedName("Department")
        public String departmentName;

        @SerializedName("Course")
        public String courseName;

        @SerializedName("Space")
        public String space;

        @SerializedName("Prerequisites")
        public String prerequisites;

        @SerializedName("Student")
        public String studentName;

        @SerializedName("Grade")
        public List<String> grades;

        @SerializedName("Number")
        public String newAvailablePlaces;

        @SerializedName("Preferences")
        public List<String> preferences;

        @SerializedName("Computer")
        public String computer;

        @SerializedName("Conditions")
        public List<String> conditions;
    }
}

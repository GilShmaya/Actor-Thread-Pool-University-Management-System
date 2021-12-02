package bgu.atd.a1.sim;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Computer {
	@SerializedName("Type")
	String computerType;
	@SerializedName("Sig Fail")
	long failSig;
	@SerializedName("Sig Success")
	long successSig;

	public Computer(String computerType) {
		this.computerType = computerType;
	}
	
	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){
		int passGrade = 56;
		boolean isPass = courses.stream().allMatch(course -> coursesGrades.get(course) >= passGrade);
		if(isPass)
			return successSig;
		else
			return failSig;
	}
}

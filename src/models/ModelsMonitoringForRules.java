package models;

public class ModelsMonitoringForRules {
	
	public static double time=0;
	public static double CPUUsed=0;
	public static int steps=0;
	public static int status=0;
	public static int totalSteps=0;
	public int index=0;
	
	public ModelsMonitoringForRules(){}
	
	public ModelsMonitoringForRules(int index) {
		super();
		this.index = index;
		setTotalSteps(0);
		setTime(0);
		setSteps(0);
		setStatus(0);
	}
	public static double getCPUUsed() {
		return CPUUsed;
	}

	public static void setCPUUsed(double cPUUsed) {
		CPUUsed = cPUUsed;
	}
	public static int getTotalSteps() {
		return totalSteps;
	}
	public static void setTotalSteps(int totalSteps) {
		ModelsMonitoringForRules.totalSteps = totalSteps;
	}
	public static double getTime() {
		return time;
	}
	public static void setTime(double time) {
		ModelsMonitoringForRules.time = time;
	}
	public static int getSteps() {
		return steps;
	}
	public static void setSteps(int steps) {
		ModelsMonitoringForRules.steps = steps;
	}
	public static int getStatus() {
		return status;
	}
	public static void setStatus(int status) {
		ModelsMonitoringForRules.status = status;
	}
	
	
}

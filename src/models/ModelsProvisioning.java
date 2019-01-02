package models;

import java.util.ArrayList;

public class ModelsProvisioning {
	
	int cpu=0;
	static double cpuAvg=0;
	double time=0;
	double cpuUSED=0;
	double memoryUSED=0;
	static double memoryUSEDSelected=0;
	static double cpuUsedSelected=0;
	int cpuSelected=0;
	static double timeSelected=0;
	static double cpuSum=0;
	static double cpuMax=0;
	double cpuNoUsed=0;
	static double  cpuNoUsedAvg=0;
	static double value=Double.MAX_VALUE;
	static double costbenefit =Double.MAX_VALUE;
	static double R2Time =0;
	static double R2CPU =0;
	double Balance;
	static double bestBalance;
	double price=0;
	static double priceSelected=0;
	
	

	public static double getMemoryUSEDSelected() {
		return memoryUSEDSelected;
	}

	public static void setMemoryUSEDSelected(double memoryUSEDSelected) {
		ModelsProvisioning.memoryUSEDSelected = memoryUSEDSelected;
	}

	public double getMemoryUSED() {
		return memoryUSED;
	}

	public void setMemoryUSED(double memoryUSED) {
		this.memoryUSED = memoryUSED;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public static double getPriceSelected() {
		return priceSelected;
	}

	public static void setPriceSelected(double priceSelected) {
		ModelsProvisioning.priceSelected = priceSelected;
	}

	public static double getBestBalance() {
		return bestBalance;
	}

	public static void setBestBalance(double bestBalance) {
		ModelsProvisioning.bestBalance = bestBalance;
	}

	public double getBalance() {
		return Balance;
	}

	public void setBalance(double balance) {
		Balance = balance;
	}
	static ArrayList<ModelsProvisioning> cpusCandidates=new ArrayList<ModelsProvisioning>();
	static ArrayList<Double>timesCandidates=new ArrayList<Double>();
	
	public ModelsProvisioning(){}
	
	public static double getR2Time() {
		return R2Time;
	}

	public static void setR2Time(double r2Time) {
		R2Time = r2Time;
	}

	public static double getR2CPU() {
		return R2CPU;
	}

	public static void setR2CPU(double r2cpu) {
		R2CPU = r2cpu;
	}

	public ModelsProvisioning(int cpu, double time, double cpuUSED, int cpuSelected, double cpuNoUsed) {
		super();
		this.cpu = cpu;
		this.time = time;
		this.cpuUSED = cpuUSED;
		this.cpuSelected = cpuSelected;
		this.cpuNoUsed = cpuNoUsed;
	}
	public static double getCostbenefit() {
		return costbenefit;
	}
	public static void setCostbenefit(double costbenefit) {
		ModelsProvisioning.costbenefit = costbenefit;
	}
	public static double getValue() {
		return value;
	}
	public static void setValue(double value) {
		ModelsProvisioning.value = value;
	}
	
	public static int getTimesCandidates() {
		return timesCandidates.size();
	}
	public static double getTimesCandidates(int i) {
		return timesCandidates.get(i);
	}
	public static void setTimesCandidates(double timeSelected) {
		ModelsProvisioning.timesCandidates.add(timeSelected);
	}
	public static int getCpusCandidatesSize() {
		return cpusCandidates.size();
	}
	public static ArrayList<ModelsProvisioning> getCpusCandidates() {
		return cpusCandidates;
	} 
	public static ModelsProvisioning getCpusCandidates(int i) {
		return cpusCandidates.get(i);
	}
	public static void setCpusCandidates(ModelsProvisioning cpuSelected) {
		System.out.println(cpusCandidates.size());
		cpusCandidates.add(cpuSelected);
	}
	
	public int getCpu() {
		return this.cpu;
	}
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}
	public static double getCpuAvg() {
		return cpuAvg;
	}
	public static void setCpuAvg(double cpuAvg) {
		ModelsProvisioning.cpuAvg = cpuAvg;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getCpuUSED() {
		return cpuUSED;
	}
	public void setCpuUSED(double cpuUSED) {
		this.cpuUSED = cpuUSED;
	}
	public static double getCpuUsedSelected() {
		return ModelsProvisioning.cpuUsedSelected;
	}
	public static void setCpuUsedSelected(double cpuUsedSelected) {
		ModelsProvisioning.cpuUsedSelected = cpuUsedSelected;
	}
	public int getCpuSelected() {
		return cpuSelected;
	}
	public void setCpuSelected(int cpuSelected) {
		this.cpuSelected = cpuSelected;
	}
	public static double getTimeSelected() {
		return timeSelected;
	}
	public static void setTimeSelected(double timeSelected) {
		ModelsProvisioning.timeSelected = timeSelected;
	}
	public static double getCpuSum() {
		return cpuSum;
	}
	public static void setCpuSum(double cpuSum) {
		ModelsProvisioning.cpuSum = cpuSum;
	}
	public static double getCpuMax() {
		return cpuMax;
	}
	public static void setCpuMax(double cpuMax) {
		ModelsProvisioning.cpuMax = cpuMax;
	}
	public double getCpuNoUsed() {
		return cpuNoUsed;
	}
	public void setCpuNoUsed(double cpuNoUsed) {
		this.cpuNoUsed = cpuNoUsed;
	}
	public static double getCpuNoUsedAvg() {
		return cpuNoUsedAvg;
	}
	public static void setCpuNoUsedAvg(double cpuNoUsedAvg) {
		ModelsProvisioning.cpuNoUsedAvg = cpuNoUsedAvg;
	}
	
	
	
}

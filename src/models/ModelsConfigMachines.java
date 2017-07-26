package models;

public class ModelsConfigMachines {
	private Double cpuPrice;
	private Double memoryPrice;
	private String localCpu;
	private String localMem;
	
	public Double getCpuPrice() {
		return cpuPrice;
	}
	public void setCpuPrice(Double cpuPrice) {
		this.cpuPrice = cpuPrice;
	}
	public Double getMemoryPrice() {
		return memoryPrice;
	}
	public String getLocalCpu() {
		return localCpu;
	}
	public String getLocalMem() {
		return localMem;
	}
	public void setLocalMem(String string) {
		localMem=string;
		
	}
	public void setLocalCpu(String string) {
		localCpu=string;
		
	}
	public void setMemoryPrice(Double memoryPrice) {
		this.memoryPrice = memoryPrice;
	}
		
	
}

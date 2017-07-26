package models;

public class ConfCurrentMachine {

	private float price=0;
	private float cpu=0;
	private float memory=0;
	private String ip="";
	
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getCpu() {
		return cpu;
	}
	public void setCpu(float cpu) {
		this.cpu = cpu;
	}
	public float getMemory() {
		return memory;
	}
	public void setMemory(float memory) {
		this.memory = memory;
	}

}

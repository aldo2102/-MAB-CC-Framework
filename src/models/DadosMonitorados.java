package models;

public class DadosMonitorados {
	
	public static int cont = 0;
	public static double AVGCPU=0;
	public static double AVGCPUidl=0;
	public static double SUMCPU=0;
	public static double SUMCPUidl=0;
	
	private double usrC=0;
	private double sysC=0;
	private double idlC=0;
	private double waiC=0;
	private double hiqC=0;
	private double siqC=0;
	
	private double usedM=0;
	private double buffM=0;
	private double cachM=0;
	private double freeM=0;

	private double recvN=0;
	private double sendN=0;
	
	private double readD=0;
	private double writD=0;
	
	public static double getAVGCPUidl() {
		return AVGCPUidl;
	}
	public static void setAVGCPUidl(double aVGCPUidl) {
		AVGCPUidl = aVGCPUidl;
	}
	public static double getSUMCPUidl() {
		return SUMCPUidl;
	}
	public static void setSUMCPUidl(double sUMCPUidl) {
		SUMCPUidl = sUMCPUidl;
	}
	public static int controller=0;
	
	public static double getSUMCPU() {
		return SUMCPU;
	}
	public static void setSUMCPU(double sUMCPU) {
		SUMCPU = sUMCPU;
	}
	
	public double getAVGCPU() {
		return AVGCPU;
	}
	public void setAVGCPU(double aVGCPU) {
		AVGCPU = aVGCPU;
	}
	
	public double getHiqC() {
		return hiqC;
	}
	public void setHiqC(double hiqC) {
		this.hiqC = hiqC;
	}
	public double getSiqC() {
		return siqC;
	}
	public void setSiqC(double siqC) {
		this.siqC = siqC;
	}
	
	public double getUsrC() {
		return usrC;
	}
	public void setUsrC(double usrC) {
		this.usrC = usrC;
	}
	public double getSysC() {
		return sysC;
	}
	public static int getCont() {
		return cont;
	}
	public static void setCont(int cont) {
		DadosMonitorados.cont = cont;
	}
	public static int getController() {
		return controller;
	}
	public static void setController(int controller) {
		DadosMonitorados.controller = controller;
	}
	public void setSysC(double sysC) {
		this.sysC = sysC;
	}
	public double getIdlC() {
		return idlC;
	}
	public void setIdlC(double idlC) {
		this.idlC = idlC;
	}
	public double getWaiC() {
		return waiC;
	}
	public void setWaiC(double waiC) {
		this.waiC = waiC;
	}
	public double getUsedM() {
		return usedM;
	}
	public void setUsedM(double usedM) {
		this.usedM = usedM;
	}
	public double getBuffM() {
		return buffM;
	}
	public void setBuffM(double buffM) {
		this.buffM = buffM;
	}
	public double getCachM() {
		return cachM;
	}
	public void setCachM(double cachM) {
		this.cachM = cachM;
	}
	public double getFreeM() {
		return freeM;
	}
	public void setFreeM(double freeM) {
		this.freeM = freeM;
	}
	public double getRecvN() {
		return recvN;
	}
	public void setRecvN(double recvN) {
		this.recvN = recvN;
	}
	public double getSendN() {
		return sendN;
	}
	public void setSendN(double sendN) {
		this.sendN = sendN;
	}
	public double getReadD() {
		return readD;
	}
	public void setReadD(double readD) {
		this.readD = readD;
	}
	public double getWritD() {
		return writD;
	}
	public void setWritD(double writD) {
		this.writD = writD;
	}

	
	@Override
	public String toString() {
		return "DadosMonitorados [usrC=" + usrC + ", sysC=" + sysC + ", idlC="
				+ idlC + ", waiC=" + waiC + ", hiqC=" + hiqC + ", siqC=" + siqC
				+ ", usedM=" + usedM + ", buffM=" + buffM + ", cachM=" + cachM
				+ ", freeM=" + freeM + ", recvN=" + recvN + ", sendN=" + sendN
				+ ", readD=" + readD + ", writD=" + writD + "]";
	}
	public DadosMonitorados() {}
	
	

}

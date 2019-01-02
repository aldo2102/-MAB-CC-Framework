package Agents;

import static jade.core.behaviours.ParallelBehaviour.WHEN_ALL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import models.DadosMonitorados;
import models.ModelsMonitoringForRules;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.util.leap.ArrayList;

public class MonitoringAgents extends Agent {

	private static final long serialVersionUID = 1L;
	private int controler = 0;
	public int verificationConect=0;
	ParallelBehaviour s;
	public static ArrayList dataList =  new ArrayList();
	 
	public static long tempoInicial;    

	protected void setup(){
		System.out.println("My name is "+ getLocalName());
		
		s = new ParallelBehaviour(this, WHEN_ALL){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public int onEnd() {
				System.out.println("Comportamento Composto Finalizado com Sucesso!");
				return 0;
			}
		};
		s.addSubBehaviour(new CyclicBehaviour(this) { 

			private static final long serialVersionUID = 1L;

			public void action() { 
				jade.lang.acl.ACLMessage msg = null;

				msg = myAgent.receive();
				if(msg!=null){
					//s.addSubBehaviour(new Conection());
					controler = Integer.parseInt(msg.getContent());
					switch (controler){
					case 1:
						s.addSubBehaviour(new Monitoring());
						break;
					case 2:
						s.addSubBehaviour(new KillMonitoring());
						break;
					case 3:
						//s.addSubBehaviour(new SalveMonitoring());
						break;
					default:
						break;
					}
				}
				controler = -1;
				msg = null;
			}  
		});

		addBehaviour(s);



	}
	protected void takeDown(){
		 
		System.out.println("Agent "+ getAID().getLocalName() +" finishing.");
	}

}


class Monitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final ArrayList owners = new ArrayList();
	private String[] lines1;


    // go !
	

	@Override
	public void action() {

		Process p;
		MonitoringAgents.tempoInicial = System.currentTimeMillis();

		try {
			System.out.println("Monitoring");
			String[] command = { "/bin/bash", "-c","gcloud compute ssh instancenew"+Starter.Starter.model.getCpuSelected()+" --zone us-central1-c --command='dstat --integer  -cmnd --noheaders --output stats.csv'"};
			System.out.println("gcloud compute ssh instancenew"+Starter.Starter.model.getCpuSelected()+" --zone us-west1-b command 'dstat --integer  -cmnd --noheaders --output stats.csv'");
			//System.out.println(command);
			ProcessBuilder pb = new ProcessBuilder(command);
			p = pb.start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line="";
			String splitBy1="\n";
			String split= "\\|[ ]{1,}|[ ]{1,}\\|[ ]{1,}|[ ]{1,}\\||[a-zA-Z]\\|[ ]{1,}|[a-zA-Z]\\||[a-zA-Z][ ]{1,}|[ ]{1,}|\\||[a-zA-Z]";
			DadosMonitorados date;
			lines1 = null;
			
			while ((line = reader.readLine()) != null) {

				lines1 = line.split(splitBy1);
				lines1 = lines1[0].split(split);
				

				if(lines1.length >= 14){
					if (Character.isDigit( lines1[ 1 ].charAt(0) ) ){
						date = new DadosMonitorados();
						if(lines1.length==15){
							date.setUsrC(Double.parseDouble(lines1[1]));
							date.setSysC(Double.parseDouble(lines1[2]));
							date.setIdlC(Double.parseDouble(lines1[3]));
							date.setWaiC(Double.parseDouble(lines1[4]));
							date.setHiqC(Double.parseDouble(lines1[5]));
							date.setSiqC(Double.parseDouble(lines1[6]));

							date.setUsedM(Double.parseDouble(lines1[7]));
							date.setBuffM(Double.parseDouble(lines1[8]));
							date.setCachM(Double.parseDouble(lines1[9]));
							date.setFreeM(Double.parseDouble(lines1[10]));

							date.setRecvN(Double.parseDouble(lines1[11]));
							date.setSendN(Double.parseDouble(lines1[12]));

							date.setReadD(Double.parseDouble(lines1[13]));
							date.setWritD(Double.parseDouble(lines1[14]));
						}else{
							date.setUsrC(Double.parseDouble(lines1[0]));
							date.setSysC(Double.parseDouble(lines1[1]));
							date.setIdlC(Double.parseDouble(lines1[2]));
							date.setWaiC(Double.parseDouble(lines1[3]));
							date.setHiqC(Double.parseDouble(lines1[4]));
							date.setSiqC(Double.parseDouble(lines1[5]));

							date.setUsedM(Double.parseDouble(lines1[6]));
							date.setBuffM(Double.parseDouble(lines1[7]));
							date.setCachM(Double.parseDouble(lines1[8]));
							date.setFreeM(Double.parseDouble(lines1[9]));

							date.setRecvN(Double.parseDouble(lines1[10]));
							date.setSendN(Double.parseDouble(lines1[11]));

							date.setReadD(Double.parseDouble(lines1[12]));
							date.setWritD(Double.parseDouble(lines1[13]));
						}
						

						DadosMonitorados.cont++;
						DadosMonitorados.SUMCPU=DadosMonitorados.SUMCPU+date.getUsrC() + date.getSysC();
						DadosMonitorados.SUMMemory=DadosMonitorados.SUMMemory+date.getUsedM();
						DadosMonitorados.SUMCPUidl=DadosMonitorados.SUMCPUidl + date.getIdlC();
						DadosMonitorados.AVGCPU=DadosMonitorados.SUMCPU/DadosMonitorados.cont;
						DadosMonitorados.AVGCPUidl=DadosMonitorados.SUMCPUidl/DadosMonitorados.cont;
						
						//System.out.println("SOmatorio memoria "+DadosMonitorados.SUMMemory);
						//System.out.println("Quantidade Memoria "+DadosMonitorados.cont);
						
						DadosMonitorados.AVGMemory=DadosMonitorados.SUMMemory/DadosMonitorados.cont;
						
						//System.out.println("Média Memoria "+DadosMonitorados.AVGMemory);
						//System.out.printf( "JADE %f \n",date.getAVGCPU());
						MonitoringAgents.dataList.add(date);
						
						ModelsMonitoringForRules m = new ModelsMonitoringForRules();
						ModelsMonitoringForRules.setTime(System.currentTimeMillis() - MonitoringAgents.tempoInicial);
						ModelsMonitoringForRules.setCPUUsed(DadosMonitorados.AVGCPUidl);
						
						try {
							Starter.Starter.kSession.insert(m);
							Starter.Starter.kSession.fireAllRules();
							
						} catch (Throwable t) {
							t.printStackTrace();
						}
						
						
					}
				}
				
			}

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	

}


class KillMonitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final ArrayList owners = new ArrayList();

	@Override
	public void action() {
		
		System.out.println("KillMonitoring");
		System.out.println(MonitoringAgents.dataList.size());
		/*for (int i = 0; i < MonitoringAgents.dataList.size(); i++) {
			System.out.println("Buff Memory " + ((DadosMonitorados) MonitoringAgents.dataList.get(i)).getBuffM()
				+ " , Cach Memory =" + ((DadosMonitorados) MonitoringAgents.dataList.get(i)).getCachM()
				+ " , Free Memory=" + ((DadosMonitorados) MonitoringAgents.dataList.get(i)).getFreeM()
				+ " , Hiq CPU="
				+ ((DadosMonitorados) MonitoringAgents.dataList.get(i)).getHiqC() + " , IDL CPU="
				+ ((DadosMonitorados) MonitoringAgents.dataList.get(i)).getIdlC() + "]");
		}*/

	}


}



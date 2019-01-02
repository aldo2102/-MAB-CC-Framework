/**
 * *************************************************************** 
 * JADE - Java Agent DEvelopment Framework is a framework to develop 
 * multi-agent systems in compliance with the FIPA specifications. 
 * Copyright (C) 2000 CSELT S.p.A. 
 *  
 * GNU Lesser General Public License 
 *  
 * This library is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation, 
 * version 2.1 of the License. 
 *  
 * This library is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * Lesser General Public License for more details. 
 *  
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, 
 * Boston, MA  02111-1307, USA. 
 * ************************************************************** 
 */

package Agents;

import static jade.core.behaviours.ParallelBehaviour.WHEN_ALL;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import jade.wrapper.StaleProxyException;
import models.DadosMonitorados;
import models.ModelsMonitoringForRules;

import java.net.InetAddress;
import java.text.SimpleDateFormat; 


@SuppressWarnings("unused")
public class ManagerG extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	ParallelBehaviour s = new ParallelBehaviour();
	SequentialBehaviour seqBehaviour = new SequentialBehaviour();

	protected void setup() {
		synchronized(this){
			System.out.println("Agent " + getLocalName() + " started.");
			
			addBehaviour(new StartGM());
			addBehaviour(new StartMonitoring());
			
			
			
			addBehaviour(new StartAPP());
			
		
			//s.addSubBehaviour(seqBehaviour);
			
			
			addBehaviour(new StopMonitoring());
			//addBehaviour(new SerializableMonitoring());



			//s.addSubBehaviour(pr);

		}

	}
	protected void takeDown(){
		System.out.println("Agent "+ getAID().getLocalName() +" finishing.");

	}
	

}




class StartAPP extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {

		Process p = null;

		String finalizado="";
		try {
			System.out.println("Executando MASE");

			String[] command = { "/bin/bash", "-c","gcloud compute ssh instancenew"+Starter.Starter.model.getCpuSelected()+" --zone us-central1-c --command='"+Starter.Starter.command+"'"};
			System.out.println("kkkkkkkkkkkk gcloud compute ssh instancenew"+Starter.Starter.model.getCpuSelected()+" --zone us-central1-c --command='"+Starter.Starter.command+"'");
			System.out.println(command);
			ProcessBuilder pb = new ProcessBuilder(command);
			p = pb.start();
			OutputStream rsyncStdIn = p.getOutputStream ();
			rsyncStdIn.write ("aldoaldo".getBytes ());
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line="";
			String[] lines1;
			while (((line = reader.readLine()) != null)) {
				
				System.out.println(line);
				/*lines1 = line.split("\n");
				lines1 = lines1[0].split(" ");
				if(lines1[0].equals("step")){
					ModelsMonitoringForRules.setSteps(Integer.parseInt(lines1[1]));
					System.out.println("Mase Step "+ModelsMonitoringForRules.getSteps());
					finalizado = "Finalizado Mase Step "+ModelsMonitoringForRules.getSteps();
				}
				if(lines1[0].equals("Started platform:")){
					ModelsMonitoringForRules.setSteps(Integer.parseInt(lines1[1]));
					System.out.println("Mase Step "+ModelsMonitoringForRules.getSteps());
					finalizado = "Finalizado Mase Step "+ModelsMonitoringForRules.getSteps();
				}*/
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Conection();
		System.out.println(finalizado);
	
	

	}
	
	public void Conection() { 
		Process p1;
		
		try {
			p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 "+Starter.Starter.machine.getIp());
			//System.out.println("ping -c 1 "+Starter.Starter.machine.getIp());
			String[] command2 = { "/bin/bash", "-c","gcloud compute ssh instancenew"+Starter.Starter.model.getCpuSelected()+" --zone us-central1-c --command='cat executando'"};
			System.out.println("gcloud compute ssh instancenew"+Starter.Starter.model.getCpuSelected()+" --zone us-central1-c --command='cat executando'");
			ProcessBuilder pb = new ProcessBuilder(command2);
			Process pr;
			pr = pb.start();
			OutputStream rsyncStdIn = pr.getOutputStream ();
			rsyncStdIn.write ("aldoaldo".getBytes ());
			String line ="";
			BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			while ((line = reader.readLine()) != null) {
				Starter.Starter.verification=line;
			}
			pr.waitFor();
			int returnVal = p1.waitFor();
			Thread.sleep(2000);
	    	boolean reachable = (returnVal==0);
	    	if(reachable && Starter.Starter.verification.equals("executando")){
	    		System.out.println("Connect");
	    		Starter.Starter.vm=1;
	    	}
	    	else{
	    		Starter.Starter.vm=0;
		    	System.out.println("No Connect");
		    	try {
					String[] command = { "/bin/bash", "-c","echo 'Y\n' | gcloud compute instances delete instancenew"+Starter.Starter.model.getCpuSelected()+" --zone 'us-central1-c'"};
					pb = new ProcessBuilder(command);
					Process pp = pb.start();
					rsyncStdIn = pr.getOutputStream ();
					rsyncStdIn.write ("aldoaldo".getBytes ());
					reader = new BufferedReader(new InputStreamReader(pp.getInputStream()));
					line="";

					try {
						while ((line = reader.readLine()) != null) {
							System.out.println(line);
						}
					} catch (NumberFormatException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pp.waitFor();

					
				} catch (IOException|InterruptedException  e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}




class StartMonitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {

		System.out.println("Monitoring");
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(new AID("MonitoringAgents", AID.ISLOCALNAME));
		message.setContent("1");
		myAgent.send(message);
		System.out.println("1");
	}

}

class SerializableMonitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(new AID("MonitoringAgents", AID.ISLOCALNAME));
		message.setContent("3");
		myAgent.send(message);
		System.out.println("3");
	}

}

class StopMonitoring extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void action() {
		


			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.addReceiver(new AID("MonitoringAgents", AID.ISLOCALNAME));
			message.setContent("2");
			myAgent.send(message);
			System.out.println("2");
			FileWriter arq;
			PrintWriter gravarArq = null ;
			try {
				arq = new FileWriter("/home/"+Starter.Starter.usuario+"/stats2.csv",true);
				gravarArq = new PrintWriter(arq); 
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MonitoringAgents.tempoInicial=(System.currentTimeMillis() - MonitoringAgents.tempoInicial);
			gravarArq.append(DadosMonitorados.AVGCPUidl+","+DadosMonitorados.AVGCPU+","+Starter.Starter.machine.getCpu()+","+Starter.Starter.transformationAgentQty + "," + (MonitoringAgents.tempoInicial)+ "," +models.ModelsProvisioning.getBestBalance()+ "\n");
			gravarArq.close();
			System.out.println(DadosMonitorados.AVGCPUidl+","+DadosMonitorados.AVGCPU+","+Starter.Starter.machine.getCpu()+","+Starter.Starter.transformationAgentQty + "," + (MonitoringAgents.tempoInicial)+ "\n");
			
			synchronized (Starter.Starter.platform) {
				Starter.Starter.platform.notifyAll();
							
			}

			Starter.Starter.stopPlatform();
		}



}

class StartGM extends OneShotBehaviour {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public void action() {
		AgentController novoAgent = null;
		Starter.Starter.platform=myAgent.getContainerController();

		try {
			novoAgent = Starter.Starter.platform.createNewAgent("MonitoringAgents", "Agents.MonitoringAgents", null);
		} catch (ControllerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		try {
			novoAgent.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

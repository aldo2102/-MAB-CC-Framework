package Starter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.opencsv.CSVReader;

import Agents.MonitoringAgents;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import junit.framework.Test;
import models.ConfCurrentMachine;
import models.DadosMonitorados;
import models.ModelPricesAWS;
import models.ModelsConfigMachines;
import models.ModelsMonitoringForRules;
import models.ModelsProvisioning;
import jade.core.Runtime;
import jade.util.Logger;

public class Starter extends Agent {

	public static long transformationAgentQty;
	public static int incremmet;

	public static PlatformController platform;
	public static AgentController geral = null;
	// public static int QuantCPU =0;
	private static final long serialVersionUID = 1L;
	public static ArrayList<ModelsProvisioning> valuesCpus = new ArrayList<ModelsProvisioning>();
	public static ArrayList<ModelPricesAWS> MV = new ArrayList<ModelPricesAWS>();
	public static ModelsProvisioning model = new ModelsProvisioning();
	public static ModelsConfigMachines prices = new ModelsConfigMachines();
	public static ConfCurrentMachine machine = new ConfCurrentMachine();
	public static String verification;
	public static int vm = 0; // verificaÃ§Ã£o do estatus da maquina
	public static ModelsMonitoringForRules monitoringRules = new ModelsMonitoringForRules();
	public static KieServices ks;
	public static KieContainer kContainer;
	public static KieSession kSession;
	public static long tempoModel;
	public static String usuario;
	public static int providerCloud;
	public static float timeVariable;
	public static float cpuUsageVariable;
	public static float priceVariable;
	public static int minimumTime;
	public static int lineNumber;
	public static String command;

	public static void main(String[] args) {

		// Argumento 4 diz o tempo
		// Argumento 5 diz o Uso de CPU
		// Argumento 6 diz o Pre�o

		timeVariable = Integer.parseInt(args[4]);
		cpuUsageVariable = Integer.parseInt(args[5]);
		priceVariable = Integer.parseInt(args[6]);

		timeVariable = timeVariable / 100;
		cpuUsageVariable = cpuUsageVariable / 100;
		priceVariable = priceVariable / 100;

		System.out.println(timeVariable + " " + cpuUsageVariable + " " + priceVariable);

		try {
			ks = KieServices.Factory.get();
			kContainer = ks.getKieClasspathContainer();
			kSession = kContainer.newKieSession("ksession-rules");
			tempoModel = System.currentTimeMillis();

		} catch (Throwable t) {
			t.printStackTrace();
		}
		Process pr = null;
		usuario = "aldoh_ti";
		if (args.length > 1) {
			
			command = args[1];

			//transformationAgentQty = Integer.parseInt(args[1]);
			transformationAgentQty = (command.hashCode()<0?command.hashCode()*-1:command.hashCode());
			System.out.println(command);
			System.out.println(transformationAgentQty);
			incremmet = Integer.parseInt(args[2]);
			System.out.println("Quantidade de agentes:" + transformationAgentQty);
			/*
			 * selecionar o provedor de Nuvem 1. Google Cloud 2. AWS
			 */

			providerCloud = Integer.parseInt(args[3]);

		} else {
			transformationAgentQty = 30;
		}

		BufferedReader br = null;
		try {
			File f = new File("falha.csv");
			if (f.exists() && !f.isDirectory()) {
				br = new BufferedReader(new FileReader("falha.csv"));
				try {
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					String[] lines1;
					while (line != null) {
						System.out.println(line);
						lines1 = line.split(",");
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
						// System.out.println("Teste leitura "+lines1[0]+" "+lines1[1]+" "+lines1[2]+"
						// "+lines1[lines1.length-2]);
					}
					br.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				vm++;
			}
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		FileWriter arqFalha = null;
		PrintWriter gravarArqFalha = null;
		File f = new File("falha.csv");
		if (f.exists() && !f.isDirectory()) {

			try {
				arqFalha = new FileWriter("falha.csv", false);
				gravarArqFalha = new PrintWriter(arqFalha);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gravarArqFalha.append(args[0] + ", " + args[1] + ", " + args[2] + ", " + args[3]);
			gravarArqFalha.close();
		}

		// Google Cloud
		if (providerCloud == 1) {
			try {
				PriceGoogleCloud.prices();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			minimumTime = 600;
			System.out.println(prices.getCpuPrice());
			System.out.println(prices.getMemoryPrice());
		}
		// AWS
		else if (providerCloud == 2) {
			PriceAWS.prices();
		}

		create();
		BufferedReader reader;
		String line;
		model.setCpuUsedSelected(0);
		model.setPriceSelected(Double.MAX_VALUE);
		model.setTimeSelected(Double.MAX_VALUE);
		model.setBestBalance(Double.MIN_VALUE);
		ModelsMonitoringForRules.setTotalSteps(365);
		valuesCpus = new ArrayList<ModelsProvisioning>();
		model = new ModelsProvisioning(0, 0, 0, 0, 0);
		monitoringRules = new ModelsMonitoringForRules(0);

		// MultipleLinearRegression Prov = new MultipleLinearRegression("base.csv");

		for (int ii = 0; ii < Integer.parseInt(args[0]); ii++) {
			model.setCpuUsedSelected(0);

			model.setTimeSelected(Double.MAX_VALUE);
			model.setBestBalance(Double.MIN_VALUE);
			ModelsMonitoringForRules.setTotalSteps(365);
			valuesCpus = new ArrayList<ModelsProvisioning>();
			model = new ModelsProvisioning(0, 0, 0, 0, 0);
			monitoringRules = new ModelsMonitoringForRules(0);
			ModelsProvisioning.setTimesCandidates(0);
			synchronized (platform) {
				String[] nextLine;
				String strFile;
				
				int cont = 1;
				CSVReader reader2 = null;

				// csv file containing data
				strFile = "base.csv";
				try {
					File fl = new File(strFile);
					if(!fl.exists()){
						fl.createNewFile();
					}else{
					  System.out.println("File already exists");
					}
					reader2 = new CSVReader(new FileReader(strFile));
					lineNumber=0;
					while ((nextLine = reader2.readNext()) != null) {
						if(nextLine.length>1)
							lineNumber++;
					}
					reader2.close();
					

				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Quantidade de linhas "+lineNumber);
				if (lineNumber <= 0) {
					model.setCpuSelected(1);
				} else if (lineNumber == 1) {
					model.setCpuSelected(2);
				} else if (lineNumber == 2) {
					model.setCpuSelected(4);
				}

				else if (lineNumber == 3) {
					model.setCpuSelected(6);
				}

				else if (lineNumber == 4) {
					model.setCpuSelected(8);
				}
				else if (lineNumber == 5) {
					Random r = new Random();
					int r2= r.nextInt((8 - 1) + 1) + 1;
					if(r2==3)
						r2--;
					else if(r2==5)
						r2--;
					else if(r2==7)
						r2--;
					model.setCpuSelected(r2);
				} else {
					try {
					MultipleLinearRegression Prov = new MultipleLinearRegression("base.csv");
					} catch (RuntimeException t) {
						Random r = new Random();
						int r2= r.nextInt((8 - 1) + 1) + 1;
						if(r2==3)
							r2--;
						else if(r2==5)
							r2--;
						else if(r2==7)
							r2--;
						model.setCpuSelected(r2);
					}
					System.out.println(" ________________________________________________________________");
					verification = "";
					vm = 0;
					System.out.println("Time: " + model.getTimeSelected() + " CpuUSED " + model.getCpuSelected()
							+ " cpu alocation " + ModelsProvisioning.getCpuUsedSelected());

					try {
						kSession.insert(ModelsMonitoringForRules.getSteps());
						kSession.fireAllRules();
					} catch (Throwable t) {
						t.printStackTrace();
					}
					// stopPlatform();

					// stopPlatform();

				}
				ProcessBuilder pb = new ProcessBuilder();

				try {
					// Google Cloud
					if (providerCloud == 2) {
						if (model.getCpuSelected() > 8)
							model.setCpuSelected(8);
						String[] command1 = { "/bin/bash", "-c",
								"gcloud compute instances create instancenew" + model.getCpuSelected()
										+ " --custom-cpu " + model.getCpuSelected() + " --custom-memory "
										+ model.getCpuSelected()
										+ "GB --zone us-central1-c --disk name=disk-1,boot=yes" };
						System.out.println("gcloud compute instances create instancenew" + model.getCpuSelected()
								+ " --custom-cpu " + model.getCpuSelected() + " --custom-memory "
								+ model.getCpuSelected() + "GB --zone us-central1-c --disk name=disk-1,boot=yes");
						pb = new ProcessBuilder(command1);
						pr = pb.start();
						System.out.println("create MV");
						OutputStream rsyncStdIn = pr.getOutputStream();
						rsyncStdIn.write("aldoaldo".getBytes());
						pr.waitFor();
						reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));

						while ((line = reader.readLine()) != null) {
						}
					}

					// AWS
					if (providerCloud == 1) {
						if (model.getCpuSelected() > 8)
							model.setCpuSelected(8);
						String[] command1 = { "/bin/bash", "-c",
								"gcloud compute instances create instancenew" + model.getCpuSelected()
										+ " --custom-cpu " + model.getCpuSelected() + " --custom-memory "
										+ model.getCpuSelected()
										+ "GB --zone us-central1-c  --disk name=disk-1,boot=yes" };
						System.out.println("gcloud compute instances create instancenew" + model.getCpuSelected()
								+ " --custom-cpu " + model.getCpuSelected() + " --custom-memory "
								+ model.getCpuSelected() + "GB --zone us-central1-c  --disk name=disk-1,boot=yes");
						pb = new ProcessBuilder(command1);
						pr = pb.start();
						System.out.println("create MV");
						OutputStream rsyncStdIn = pr.getOutputStream();
						rsyncStdIn.write("aldoaldo".getBytes());
						pr.waitFor();
						reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));

						while ((line = reader.readLine()) != null) {
						}
					}
					pr.waitFor();
				} catch (NumberFormatException | InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					String[] command1 = { "/bin/bash", "-c",
							"printf 'Y\n' | gcloud compute instances describe instancenew" + model.getCpuSelected()
									+ " --zone us-central1-c " };
					pb = new ProcessBuilder(command1);
					pr = pb.start();
					System.out.println("Get IP");
					OutputStream rsyncStdIn = pr.getOutputStream();
					rsyncStdIn.write("aldoaldo".getBytes());
					pr.waitFor();
					reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
					String split = "[ ]{2,}natIP: ";
					String[] lines = null;
					while ((line = reader.readLine()) != null) {
						lines = line.split("\n");
						lines = lines[0].split(split);
						if (lines.length > 1) {
							machine.setIp(lines[1]);
							break;
						}
					}

					String[] command2 = { "/bin/bash", "-c", "gcloud compute ssh instancenew" + model.getCpuSelected()
							+ " --zone us-central1-c --command=\"echo 'np' >executando\"" };
					pb = new ProcessBuilder(command2);
					System.out.println("gcloud compute ssh instancenew" + model.getCpuSelected()
							+ " --zone us-central1-c command \"echo 'executando' >executando\"");
					pr = pb.start();
					rsyncStdIn = pr.getOutputStream();
					rsyncStdIn.write("aldoaldo".getBytes());
					rsyncStdIn = pr.getOutputStream();
					rsyncStdIn.write("aldoaldo".getBytes());
					pr.waitFor();
				} catch (NumberFormatException | InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Process p = null;

				try {
					String[] command2 = { "/bin/bash", "-c", "gcloud compute ssh instancenew" + model.getCpuSelected()
							+ " --zone us-central1-c --command='nproc'" };

					pb = new ProcessBuilder(command2);
					pr = pb.start();
					p = pb.start();
					OutputStream rsyncStdIn = pr.getOutputStream();
					rsyncStdIn.write("aldoaldo".getBytes());
					pr.waitFor();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				line = "";

				machine.setCpu(0);
				try {
					while ((line = reader.readLine()) != null) {
						machine.setCpu(Integer.parseInt(line));
					}
				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				line = "";

				try {
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
					pr.waitFor();
				} catch (NumberFormatException | InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				model.getCpu();
				try {

					geral = platform.createNewAgent("StarterAgent", "Agents.StarterAgent", null);
					geral.start();

				} catch (ControllerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				synchronized (geral) {
					try {
						System.out.println("Waiting");
						geral.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				Process pp;

				int alternative = model.getCpuSelected();
				try {
					arqFalha = new FileWriter("falha.csv", true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				gravarArqFalha = new PrintWriter(arqFalha);
				gravarArqFalha.append(", " + ii);
				gravarArqFalha.close();
				verificationBase();
				transformationAgentQty = transformationAgentQty + incremmet;
				ModelsProvisioning.setCpuUsedSelected(0);
				ModelsProvisioning.setMemoryUSEDSelected(0);
				model.setTimeSelected(Double.MAX_VALUE);
				ModelsMonitoringForRules.setTotalSteps(365);
				valuesCpus = new ArrayList<ModelsProvisioning>();
				model = new ModelsProvisioning(0, 0, 0, 0, 0);
				monitoringRules = new ModelsMonitoringForRules(0);

				/*
				 * if(vm==0) { System.out.println("--------Validation--------"); ii--;
				 * transformationAgentQty=transformationAgentQty-incremmet; }
				 */
				System.out.println("\n\n Nova quantidade de Agentes " + transformationAgentQty + " \n\n");
				System.out.println(alternative + " valores " + model.getCpuSelected());

				if (alternative != model.getCpuSelected() || ii + 1 == Integer.parseInt(args[0])) {
					try {
						String[] command = { "/bin/bash", "-c",
								"echo 'Y\n' | gcloud compute instances delete instancenew" + alternative
										+ " --zone 'us-central1-c'" };
						// System.out.println("echo 'Y\n' | gcloud compute instances delete
						// instancenew"+model.getCpuSelected()+" --zone 'us-central1-c'");
						pb = new ProcessBuilder(command);
						pp = pb.start();
						System.out.println("Excluindo MV");
						OutputStream rsyncStdIn = pr.getOutputStream();
						rsyncStdIn.write("aldoaldo".getBytes());
						reader = new BufferedReader(new InputStreamReader(pp.getInputStream()));
						line = "";

						try {
							while ((line = reader.readLine()) != null) {
								System.out.println(line);
							}
						} catch (NumberFormatException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pp.waitFor();

					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (ii + 1 != Integer.parseInt(args[0])) {

					System.out.println("CreateNewPlatforma");
					create();
				}

			}

		}
		tempoModel = System.currentTimeMillis() - tempoModel;
		FileWriter arq;
		PrintWriter gravarArq = null;
		try {
			arq = new FileWriter("/home/" + usuario + "/statsTempo.csv", true);
			gravarArq = new PrintWriter(arq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gravarArq.append(tempoModel + "\n");
		gravarArq.close();
		try {
			arqFalha = new FileWriter("falha.csv", true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		gravarArqFalha = new PrintWriter(arqFalha);
		gravarArqFalha.append(", right");
		gravarArqFalha.close();
		System.exit(0);
	}

	public static void create() {
		// get a JADE runtime
		Runtime rt = Runtime.instance();
		// create a default profile
		Profile p1 = new ProfileImpl();
		// create the Main-container
		ContainerController mainContainer = rt.createMainContainer(p1);
		try {
			platform = mainContainer.getPlatformController();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void stopPlatform() {
		System.out.println("stop");
		synchronized (geral) {
			geral.notifyAll();
			try {
				platform.kill();
			} catch (ControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void verificationBase() {
		System.out.println("Verification");
		if (machine.getCpu() > 0) {
			try {
				Scanner file;
				PrintWriter writer;

				try {

					file = new Scanner(new File("/home/" + usuario + "/base.csv"));
					writer = new PrintWriter("/home/" + usuario + "/auxbase.csv");

					while (file.hasNext()) {
						String line = file.nextLine();
						if (!line.isEmpty()) {
							writer.write(line);
							writer.write("\n");
						}
					}

					file.close();
					writer.close();

				} catch (FileNotFoundException ex) {
					Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
				}

				Process pr;
				ProcessBuilder pb = null;
				/*
				 * String[] command = { "/bin/bash",
				 * "-c","cp base.csv /home/"+usuario+"/auxbase.csv"}; ProcessBuilder pb = new
				 * ProcessBuilder(command); pr = pb.start(); pr.waitFor();
				 */
				double r2TimeAux = ModelsProvisioning.getR2Time();
				double r2CPUaux = ModelsProvisioning.getR2CPU();

				FileWriter arq = null;
				PrintWriter gravarArq = null;
				try {
					arq = new FileWriter("/home/" + usuario + "/auxbase.csv", true);
					gravarArq = new PrintWriter(arq);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				gravarArq.append(DadosMonitorados.AVGCPUidl + "," + transformationAgentQty + "," + machine.getCpu()
						+ "," + DadosMonitorados.AVGCPU + "," + MonitoringAgents.tempoInicial + ","
						+ DadosMonitorados.AVGMemory + "\n");
				arq.close();
				gravarArq.close();
				System.out.println(DadosMonitorados.AVGCPUidl + "," + transformationAgentQty + "," + machine.getCpu()
						+ "," + DadosMonitorados.AVGCPU + "," + MonitoringAgents.tempoInicial + ","
						+ DadosMonitorados.AVGMemory + "\n");

				System.out.println("R2 time atual " + r2TimeAux + " R2 atualizado " + ModelsProvisioning.getR2Time());
				System.out.println("R2 CPU atual " + r2CPUaux + " R2 atualizado " + ModelsProvisioning.getR2CPU());

				// if(r2TimeAux<=ModelsProvisioning.getR2Time() &&
				// r2CPUaux<=ModelsProvisioning.getR2CPU()){
				System.out.println("=========== MELHOROU A BASE ===========");
				// String[] command1 = { "/bin/bash", "-c","cp /home/"+usuario+"/auxbase.csv
				// base.csv"};
				// ProcessBuilder pb = new ProcessBuilder(command1);
				// pr = pb.start();
				// pr.waitFor();
				// }

				try {

					file = new Scanner(new File("/home/" + usuario + "/auxbase.csv"));
					writer = new PrintWriter("/home/" + usuario + "/base.csv");

					while (file.hasNext()) {
						String line = file.nextLine();
						if (!line.isEmpty()) {
							writer.write(line);
							writer.write("\n");
						}
					}

					file.close();
					writer.close();

				} catch (FileNotFoundException ex) {
					Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
				}

				String[] command2 = { "/bin/bash", "-c", "rm /home/" + usuario + "/auxbase.csv" };
				pb = new ProcessBuilder(command2);
				pr = pb.start();
				pr.waitFor();

			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

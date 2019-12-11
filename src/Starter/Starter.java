package Starter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.opencsv.CSVReader;

import Agents.MonitoringAgents;
import jade.core.Agent;
import jade.core.AgentContainer;
import jade.core.IMTPException;
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
	public static int vm = 0; // verificaÃƒÂ§ÃƒÂ£o do estatus da maquina
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
	public static String box;
	public static String usuarioMasCloud;

	public static String vagrant;
	public static String vagrant2;
	public static String vagrant3;

	public static void main(String[] args) {
		
	
		// Argumento 4 diz o tempo
		// Argumento 5 diz o Uso de CPU
		// Argumento 6 diz o Preço

		create();
		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		System.out.println(OS);

		ProcessBuilder processBuilder = new ProcessBuilder();

		if (OS.equals("windows 10")) {
			vagrant3 = "/c";
			vagrant2 = "cmd.exe";
			vagrant = "C:\\Vagrant\\bin\\vagrant.exe";
		} else {
			vagrant3 = "-c";
			vagrant2 = "bash";
			vagrant = "vagrant";

		}

		timeVariable = Integer.parseInt(args[4]);
		cpuUsageVariable = Integer.parseInt(args[5]);
		priceVariable = Integer.parseInt(args[6]);

		timeVariable = timeVariable / 100;
		cpuUsageVariable = cpuUsageVariable / 100;
		priceVariable = priceVariable / 100;

		System.out.println(timeVariable + " " + cpuUsageVariable + " " + priceVariable);

		box = args[7];
		usuarioMasCloud = args[8];

		try {
			File diretorio = new File(usuarioMasCloud);
			diretorio.mkdir();
		} catch (Exception ex) {
			System.out.println(ex);
		}

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
			// String commandTrans = command;
			// transformationAgentQty = Integer.parseInt(args[1]);
			transformationAgentQty = command.hashCode();

			System.out.println("Agents of Transfomation " + transformationAgentQty);
			System.out.println(command);
			System.out.println(transformationAgentQty);
			incremmet = Integer.parseInt(args[2]);
			System.out.println("Quantidade de agentes:" + transformationAgentQty);
			/*
			 * selecionar o provedor de Nuvem 1. Google Cloud 2. AWS
			 */

			providerCloud = Integer.parseInt(args[3]);

		}

		BufferedReader br = null;
		try {
			File f = new File(usuarioMasCloud + "/" + "falha.csv");
			if (f.exists() && !f.isDirectory()) {
				br = new BufferedReader(new FileReader(usuarioMasCloud + "/" + "falha.csv"));
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
						// System.out.println("Teste leitura "+lines1[0]+" && "+lines1[1]+" &&
						// "+lines1[2]+"
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
		File f = new File(usuarioMasCloud + "/" + "falha.csv");
		if (f.exists() && !f.isDirectory()) {

			try {
				arqFalha = new FileWriter(usuarioMasCloud + "/" + "falha.csv", false);
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
		} else if (providerCloud == 3) {
			PriceVangrant.prices();
			System.out.println(prices.getCpuPrice());
			System.out.println(prices.getMemoryPrice());
		}

		BufferedReader reader;
		String line;
		ModelsProvisioning.setCpuUsedSelected(0);
		ModelsProvisioning.setPriceSelected(Double.MAX_VALUE);
		ModelsProvisioning.setTimeSelected(Double.MAX_VALUE);
		ModelsProvisioning.setBestBalance(Double.MIN_VALUE);
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
				strFile = usuarioMasCloud + "/" + "base.csv";
				try {
					File fl = new File(strFile);
					if (!fl.exists()) {
						fl.createNewFile();
					} else {
						System.out.println("File already exists");
					}
					reader2 = new CSVReader(new FileReader(strFile));
					lineNumber = 0;
					while ((nextLine = reader2.readNext()) != null) {
						if (nextLine.length > 1)
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
				System.out.println("Quantidade de linhas " + lineNumber);
				if (lineNumber <= 0) {
					machine.setCpu(1);
					model.setCpuSelected(1);
				} else if (lineNumber == 1) {
					machine.setCpu(2);
					model.setCpuSelected(2);
				} else if (lineNumber == 2) {
					machine.setCpu(4);
					model.setCpuSelected(4);
				}

				else if (lineNumber == 3) {
					machine.setCpu(6);
					model.setCpuSelected(6);
				}

				else if (lineNumber == 4) {
					machine.setCpu(8);
					model.setCpuSelected(8);
				} else if (lineNumber == 5) {
					Random r = new Random();
					int r2 = r.nextInt((8 - 1) + 1) + 1;
					if (r2 == 3)
						r2--;
					else if (r2 == 5)
						r2--;
					else if (r2 == 7)
						r2--;
					model.setCpuSelected(r2);
				} else {
					try {
						MultipleLinearRegression Prov = new MultipleLinearRegression(
								usuarioMasCloud + "/" + "base.csv");
					} catch (RuntimeException t) {
						System.out.println(t.getMessage());
						System.out.println("---------------- Atenção entrou em Cath, Verifique ----------------");
						Random r = new Random();
						int r2 = r.nextInt((8 - 1) + 1) + 1;
						if (r2 == 3)
							r2--;
						else if (r2 == 5)
							r2--;
						else if (r2 == 7)
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

				}
				ProcessBuilder pb = new ProcessBuilder();

				try {
					// Google Cloud
					/*
					 * if (providerCloud == 2) { if (model.getCpuSelected() > 8)
					 * model.setCpuSelected(8); String[] command1 = { vagrant2, vagrant3,
					 * "gcloud compute instances create instancenew" + model.getCpuSelected() +
					 * " --custom-cpu " + model.getCpuSelected() + " --custom-memory " +
					 * model.getCpuSelected() +
					 * "GB --zone us-central1-c --disk name=disk-bio,boot=yes" };
					 * System.out.println("gcloud compute instances create instancenew" +
					 * model.getCpuSelected() + " --custom-cpu " + model.getCpuSelected() +
					 * " --custom-memory " + model.getCpuSelected() +
					 * "GB --zone us-central1-c --disk name=disk-bio,boot=yes"); pb = new
					 * ProcessBuilder(command1); pr = pb.start(); System.out.println("create MV");
					 * OutputStream rsyncStdIn = pr.getOutputStream();
					 * rsyncStdIn.write("aldoaldo".getBytes()); pr.waitFor(); reader = new
					 * BufferedReader(new InputStreamReader(pr.getInputStream()));
					 * 
					 * while ((line = reader.readLine()) != null) { } }
					 */

					// AWS
					if (providerCloud == 2) {
						if (model.getCpuSelected() > 8)
							model.setCpuSelected(8);
						String[] command1 = { vagrant2, vagrant3,
								"gcloud compute instances create instancenew" + model.getCpuSelected()
										+ " --custom-cpu " + model.getCpuSelected() + " --custom-memory "
										+ model.getCpuSelected()
										+ "GB --zone us-central1-c  --disk name=disk-bio,boot=yes" };
						System.out.println("gcloud compute instances create instancenew" + model.getCpuSelected()
								+ " --custom-cpu " + model.getCpuSelected() + " --custom-memory "
								+ model.getCpuSelected() + "GB --zone us-central1-c  --disk name=disk-bio,boot=yes");
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

					if (providerCloud == 3) {

						/*
						 * String[] command1 = { vagrant2, vagrant3,
						 * "gcloud compute instances create instancenew" + model.getCpuSelected() +
						 * " --custom-cpu " + model.getCpuSelected() + " --custom-memory " +
						 * model.getCpuSelected() +
						 * "GB --zone us-central1-c  --disk name=disk-bio,boot=yes" };
						 */

						String str = "Vagrant.configure(\"2\") do |config|\n" + "  config.vm.box = \"" + box + "\"\n"
								+ "  config.vm.provider :virtualbox do |v|\n"
								+ "    v.customize [\"modifyvm\", :id, \"--memory\", 1024]\n"
								+ "    v.customize [\"modifyvm\", :id, \"--cpus\", " + model.getCpuSelected() + "]\n"
								+ "  end\n" + "end";

						System.out.println("Quantidade de CPUs " + model.getCpuSelected());

						try (FileWriter writer = new FileWriter(usuarioMasCloud + "/" + "Vagrantfile");
								BufferedWriter bw = new BufferedWriter(writer)) {

							bw.write(str);

						} catch (IOException e) {
							System.err.format("IOException: %s%n", e);
						}

					}
				} catch (NumberFormatException | InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					OutputStream rsyncStdIn;
					if (providerCloud == 2) {
						String[] command1 = { vagrant2, vagrant3,
								"printf 'Y\n' | gcloud compute instances describe instancenew" + model.getCpuSelected()
										+ " --zone us-central1-c " };
						pb = new ProcessBuilder(command1);
						pr = pb.start();
						System.out.println("Created VM");
						rsyncStdIn = pr.getOutputStream();
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
					}
					if (providerCloud == 3) {

						System.out.println("cd " + usuarioMasCloud + " && " + vagrant + " box add " + box);
						String[] command1 = { vagrant2, vagrant3,
								"cd " + usuarioMasCloud + " && " + vagrant + " box add " + box };
						pb = new ProcessBuilder(command1);
						pr = pb.start();

						pr.waitFor();
						reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));

						while ((line = reader.readLine()) != null) {
							System.out.println(line);
						}

						System.out.println("cd " + usuarioMasCloud + " && " + vagrant + " up");
						String[] command2 = { vagrant2, vagrant3, "cd " + usuarioMasCloud + " && " + vagrant + " up" };
						pb = new ProcessBuilder(command2);
						pr = pb.start();

						pr.waitFor();
						reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
						while ((line = reader.readLine()) != null) {
							System.out.println(line);
						}

						System.out.println("create MV");
					}

					if (providerCloud == 2) {
						String[] command2 = { vagrant2, vagrant3,
								"gcloud compute instances instancenew" + model.getCpuSelected()
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
					}
					if (providerCloud == 3) {
						String[] command2 = { vagrant2, vagrant3,
								"cd " + usuarioMasCloud + " && " + vagrant + " ssh -c \"echo 'np' >executando\"" };
						pb = new ProcessBuilder(command2);
						System.out.println("gcloud compute ssh instancenew" + model.getCpuSelected()
								+ " --zone us-central1-c command \"echo 'executando' >executando\"");
						pr = pb.start();
						rsyncStdIn = pr.getOutputStream();
						rsyncStdIn.write("aldoaldo".getBytes());
						rsyncStdIn = pr.getOutputStream();
						rsyncStdIn.write("aldoaldo".getBytes());
						pr.waitFor();
					}

				} catch (NumberFormatException | InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Process p = null;
				if (providerCloud == 2) {
					try {
						String[] command2 = { vagrant2, vagrant3, "gcloud compute ssh instancenew"
								+ model.getCpuSelected() + " --zone us-central1-c --command='nproc'" };

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
				}
				if (providerCloud == 3) {
					try {
						String[] command2 = { vagrant2, vagrant3,
								"cd " + usuarioMasCloud + " && " + vagrant + " ssh -c 'nproc'" };

						pb = new ProcessBuilder(command2);
						pr = pb.start();
						p = pb.start();

						pr.waitFor();
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				line = "";

				machine.setCpu(0);
				try {
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
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
					arqFalha = new FileWriter(usuarioMasCloud + "/" + "falha.csv", true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				gravarArqFalha = new PrintWriter(arqFalha);
				gravarArqFalha.append(", " + ii);
				gravarArqFalha.close();
				verificationBase();

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

				if (alternative != model.getCpuSelected() || ii + 1 == Integer.parseInt(args[0]))

					try {

						if (providerCloud == 2) {
							String[] command1 = { vagrant2, vagrant3,
									"echo 'Y\n' | gcloud compute instances delete instancenew" + alternative
											+ " --zone 'us-central1-c'" };
							System.out.println("echo 'Y\n' | gcloud compute instances delete" + "instancenew"
									+ model.getCpuSelected() + " --zone 'us-central1-c'); ");
							pb = new ProcessBuilder(command1);
						}

						if (providerCloud == 3) {
							String[] command1 = { vagrant2, vagrant3, "echo 'Y\n' " + vagrant + " halt" };
							System.out.println("echo 'Y\n' " + vagrant + " halt ");
							pb = new ProcessBuilder(command1);
						}

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
						} catch (NumberFormatException | IOException e) { // TODO Auto-generated catch block
							e.printStackTrace();
						}

						pp.waitFor();
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				/*
				 * if (ii + 1 != Integer.parseInt(args[0])) {
				 * 
				 * System.out.println("CreateNewPlatforma"); create(); }
				 */

			}

		}
		tempoModel = System.currentTimeMillis() - tempoModel;
		FileWriter arq;
		PrintWriter gravarArq = null;
		try {
			arq = new FileWriter(usuarioMasCloud + "/" + "statsTempo.csv", true);
			gravarArq = new PrintWriter(arq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gravarArq.append(tempoModel + "\n");
		gravarArq.close();

		try {
			arqFalha = new FileWriter(usuarioMasCloud + "/" + "falha.csv", true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		stopPlatform();
		System.out.println(command);
		System.out.println("------------------- Final -----------------------");
		gravarArqFalha = new PrintWriter(arqFalha);
		gravarArqFalha.append(", right");
		gravarArqFalha.close();
		System.exit(0);
	}

	public static void create() {
		
		// get a JADE runtime
		Runtime rt = Runtime.instance();

		// p.setParameter(Profile.MAIN_HOST, "localhost");
		int port = 10000;
		int controler = 1;

		ServerSocket socket = null;
		try {
			Random gerador = new Random();
			socket = new ServerSocket(0);
			port = socket.getLocalPort()+(gerador.nextInt(25)+1);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		//while (controler == 1) {
			ProfileImpl p = new ProfileImpl();
			p.setParameter(Profile.MAIN_PORT, port + "");
		    //p.setParameter(Profile.MAIN_HOST, "localhost");
		    //p.setParameter(Profile.GUI, "false");

			p.setParameter(Profile.CONTAINER_NAME, "Main-Container" + port);
			try {
				rt.shutDown();
				
				ContainerController cc = Runtime.instance().createMainContainer(p);
				System.out.println("-Main-Container" + port);
					  
				System.out.println(jade.core.Runtime.instance());
				//ContainerController mainContainer = rt.createMainContainer(p);

				platform = cc.getPlatformController();

				controler = 1;

			} catch (Exception e) {
				e.printStackTrace();
			}
		//}

	}

	public static void stopPlatform() {
		System.out.println("stop");

			try {
				//platform.notifyAll();
				platform.kill();
			} catch (ControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	public static void verificationBase() {
		System.out.println("Verification");
		if (machine.getCpu() > 0) {
			try {
				Scanner file;
				PrintWriter writer;

				try {

					file = new Scanner(new File(usuarioMasCloud + "/" + "base.csv"));
					writer = new PrintWriter(usuarioMasCloud + "/" + "auxbase.csv");

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
				 * String[] command = { vagrant2,
				 * vagrant3,"cp base.csv /home/"+usuario+"/auxbase.csv"}; ProcessBuilder pb =
				 * new ProcessBuilder(command); pr = pb.start(); pr.waitFor();
				 */
				double r2TimeAux = ModelsProvisioning.getR2Time();
				double r2CPUaux = ModelsProvisioning.getR2CPU();

				FileWriter arq = null;
				PrintWriter gravarArq = null;
				try {
					arq = new FileWriter(usuarioMasCloud + "/" + "auxbase.csv", true);
					gravarArq = new PrintWriter(arq);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				gravarArq.append(DadosMonitorados.AVGCPUidl + "," + transformationAgentQty + "," + machine.getCpu()
						+ "," + DadosMonitorados.AVGCPU + "," + MonitoringAgents.tempoInicial + ","
						+ DadosMonitorados.AVGMemory + "," + timeVariable + "," + cpuUsageVariable + "," + priceVariable
						+ ",\"" + command + "\"\n");
				arq.close();
				gravarArq.close();
				System.out.println(DadosMonitorados.AVGCPUidl + "," + transformationAgentQty + "," + machine.getCpu()
						+ "," + DadosMonitorados.AVGCPU + "," + MonitoringAgents.tempoInicial + ","
						+ DadosMonitorados.AVGMemory + "," + timeVariable + "," + cpuUsageVariable + "," + priceVariable
						+ ",\"" + command + "\"\n");

				System.out.println("R2 time atual " + r2TimeAux + " R2 atualizado " + ModelsProvisioning.getR2Time());
				System.out.println("R2 CPU atual " + r2CPUaux + " R2 atualizado " + ModelsProvisioning.getR2CPU());

				// if(r2TimeAux<=ModelsProvisioning.getR2Time() &&
				// r2CPUaux<=ModelsProvisioning.getR2CPU()){
				System.out.println("=========== MELHOROU A BASE ===========");

				FileWriter arq2 = null;
				PrintWriter gravarArq2 = null;
				System.out.println("Time estimado " + ModelsProvisioning.getTimeSelected() + " " + tempoModel + "\n");
				System.out.println("CPU estimado " + model.getCpuUSED() + " " + DadosMonitorados.AVGCPU + "\n");
				System.out
						.println("Memory estimado " + model.getMemoryUSED() + " " + DadosMonitorados.AVGMemory + "\n");

				try {
					arq2 = new FileWriter(usuarioMasCloud + "/" + "baseRegression22.csv", true);
					gravarArq2 = new PrintWriter(arq2);

					gravarArq2.append("Time estimado " + model.getTimeSelected() + " " + tempoModel + "\n");
					gravarArq2.append("CPU estimado " + model.getCpuUSED() + " " + DadosMonitorados.AVGCPU + "\n");
					gravarArq2.append(
							"Memory estimado " + model.getMemoryUSED() + " " + DadosMonitorados.AVGMemory + "\n");

					arq2.close();
					gravarArq2.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// String[] command1 = { vagrant2, vagrant3,"cp /home/"+usuario+"/auxbase.csv
				// base.csv"};
				// ProcessBuilder pb = new ProcessBuilder(command1);
				// pr = pb.start();
				// pr.waitFor();
				// }

				try {

					file = new Scanner(new File(usuarioMasCloud + "/" + "auxbase.csv"));
					writer = new PrintWriter(usuarioMasCloud + "/" + "base.csv");

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

				String[] command2 = { vagrant2, vagrant3, "rm " + usuarioMasCloud + "/auxbase.csv" };
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

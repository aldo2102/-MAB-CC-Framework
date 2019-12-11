package Starter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.opencsv.CSVReader;

import Jama.Matrix;
import Jama.QRDecomposition;
import models.ModelsProvisioning;


public class MultipleLinearRegression  {


	int N;        // number of 
	int p;        // number of dependent variables
	Matrix beta;  // regression coefficients
	private double SSE;         // sum of squared
	private double SST;         // sum of squared

	public MultipleLinearRegression(String base){
		action(base);
	}

	public MultipleLinearRegression(double[][] x, double[] y) {
		if (x.length != y.length) throw new RuntimeException("dimensions don't agree");
		N = y.length;
		p = x[0].length;
		
		Matrix X = new Matrix(x);

		// create matrix from vector
		Matrix Y = new Matrix(y, N);
		

		// find least squares solution
		QRDecomposition qr = new QRDecomposition(X);
		//System.out.println(Y.toString().toString()+" "+N+" "+y.length);
		
		beta = qr.solve(Y);
		


		// mean of y[] values
		double sum = 0.0;
		for (int i = 0; i < N; i++)
			sum += y[i];
		double mean = sum / N;

		// total variation to be accounted for
		for (int i = 0; i < N; i++) {
			double dev = y[i] - mean;
			SST += dev*dev;
		}

		// variation not accounted for
		Matrix residuals = X.times(beta).minus(Y);
		SSE = residuals.norm2() * residuals.norm2();

	}

	public double beta(int j) {
		return beta.get(j, 0);
	}

	public double R2() {
		return 1.0 - SSE/SST;
	}
	public void action(String base) {

		double x[][]= null;
		double y[]= null;
		double x1[][]= null;
		double y1[] = null;
		double x2[][]= null;
		double y2[] = null;
		double x3[][]= null;
		double y3[] = null;
		double x4[][]= null;
		double y4[] = null;
		String [] nextLine;
		String strFile;
		CSVReader reader=null;
		int lineNumber = 0;
		int cont = 1;
		try {
			//csv file containing data
			strFile = base;
			/*reader = new CSVReader(new FileReader(strFile));
			reader.close();*/
			reader = new CSVReader(new FileReader(strFile));
			while ((nextLine = reader.readNext()) != null) {
				lineNumber++;
			}
			
			
			x = new double[lineNumber][4];
			y = new double[lineNumber];
			x1 = new double[lineNumber][4];
			y1 = new double[lineNumber];
			x2 = new double[lineNumber][3];
			y2 = new double[lineNumber];
			x3 = new double[lineNumber][4];
			y3 = new double[lineNumber];
			x4 = new double[lineNumber][4];
			y4 = new double[lineNumber];
			reader = new CSVReader(new FileReader(strFile));
			lineNumber=0;
			while ((nextLine = reader.readNext()) != null) {
				
				// nextLine[] is an array of values from the line

				
				if(nextLine.length>1){
					if(Character.isDigit(nextLine[0].charAt(0))){
						double CPUNotUsed = (double)Double.parseDouble(nextLine[0]);
						long agents = (long)Integer.parseInt(nextLine[1]);
						int vCPU = (int)(Double.parseDouble(nextLine[2]));
						double CPUUsed = (double)Double.parseDouble(nextLine[3]);
						double time = (double)Double.parseDouble(nextLine[4]);
						double memoryUsed = (double)Double.parseDouble(nextLine[5]);

						x[lineNumber][0]=1;
						x[lineNumber][1]=agents;
						x[lineNumber][2]=vCPU;
						x[lineNumber][3]=memoryUsed;
						y[lineNumber]=time;
						x1[lineNumber][0]=1;
						x1[lineNumber][1]=agents;
						x1[lineNumber][2]=vCPU;
						x1[lineNumber][3]=memoryUsed;
						y1[lineNumber]=CPUUsed;
						x2[lineNumber][0]=1;
						x2[lineNumber][1]=agents;
						x2[lineNumber][2]=vCPU;
						y2[lineNumber]=CPUNotUsed;
						x3[lineNumber][0]=1;
						x3[lineNumber][1]=agents;
						x3[lineNumber][2]=vCPU;
						x3[lineNumber][3]=CPUUsed;
						y3[lineNumber]=time;
						x4[lineNumber][0]=1;
						x4[lineNumber][1]=agents;
						x4[lineNumber][2]=vCPU;
						x4[lineNumber][3]=CPUUsed;
						y4[lineNumber]=memoryUsed;
						
					}
					//System.out.println(nextLine[0] +" "+nextLine[1]+" "+nextLine[2]+" "+nextLine[3]+" "+nextLine[4]);
				}
				lineNumber++;
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		MultipleLinearRegression regression = new MultipleLinearRegression(x, y);

		System.out.printf("Time = %.2f + %.2f beta1 + %.2f beta2 + %.2f beta3   (R^2 = %.2f)\n",
				regression.beta(0), regression.beta(1), regression.beta(2), regression.beta(3), regression.R2());

		MultipleLinearRegression regression1 = new MultipleLinearRegression(x1, y1);
		

		System.out.printf("CPU Used = %.2f + %.2f beta1 + %.2f beta2 + %.2f beta3   (R^2 = %.2f)\n",
				regression1.beta(0), regression1.beta(1), regression1.beta(2), regression1.beta(3), regression1.R2());

		MultipleLinearRegression regression2 = new MultipleLinearRegression(x2, y2);

		System.out.printf("CPU Used = %.2f + %.2f beta1 + %.2f beta2  (R^2 = %.2f)\n",
				regression2.beta(0), regression2.beta(1), regression2.beta(2), regression2.R2());

		MultipleLinearRegression regression3 = new MultipleLinearRegression(x3, y3);

		System.out.printf("CPU Time with CPU = %.2f + %.2f beta1 + %.2f beta2 + %.2f beta3   (R^2 = %.2f)\n",
				regression3.beta(0), regression3.beta(1), regression3.beta(2), regression3.beta(3),  regression3.R2());
		
		MultipleLinearRegression regression4 = new MultipleLinearRegression(x4, y4);

		System.out.printf("Memory with CPU = %.2f + %.2f beta1 + %.2f beta2 + %.2f beta3   (R^2 = %.2f)\n",
				regression4.beta(0), regression4.beta(1), regression4.beta(2), regression4.beta(3),  regression4.R2());
		
		FileWriter arq2 = null;
		PrintWriter gravarArq2 = null;
		try {
			arq2 = new FileWriter("baseRegression.csv", true);
			gravarArq2 = new PrintWriter(arq2);
			
		
			gravarArq2.append("Time "+regression.beta(0)+" "
					+ regression.beta(1)+" beta1 "+regression.beta(2)+" beta2 + "
					+regression.beta(3)+ " beta3 ;  R^2 "+regression.R2() +"\n");
			
			gravarArq2.append("CPU "+regression1.beta(0)+" "
					+ regression1.beta(1)+" beta1 "+regression1.beta(2)+" beta2 "
					+regression1.beta(3)+ " beta3  ;  R^2 "+regression1.R2() +"\n");
			
			gravarArq2.append("Memory "+regression4.beta(0)+" "
					+ "+ "+ regression4.beta(1)+" beta1 "+regression4.beta(2)+" beta2 "
					+regression4.beta(3)+ " beta3 ;  R^2 "+regression4.R2() +"\n");
			arq2.close();
			gravarArq2.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ModelsProvisioning.setR2Time(regression.R2());
		ModelsProvisioning.setR2CPU(regression1.R2());
		double time=Double.MAX_VALUE;
		double timeTemp=0,cpuTemp=0, cpuNoUsedTemp=0, memoryTemp=0;
		//csv file containing data
		strFile = base;
		try {
			reader = new CSVReader(new FileReader(strFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double t1=0,sumNoUsed=0;
		int contator=1;
		ModelsProvisioning.setCpuAvg(0);
		ModelsProvisioning.setCpuSum(0);
		while(cont<=16){

			//System.out.println(ModelsProvisioning.getCpuAvg()+" "+ModelsProvisioning.getCpuUSED()+" avg "+ModelsProvisioning.getCpuMax()*0.75);
			Starter.model = new ModelsProvisioning();
			timeTemp=regression.beta(0) + regression.beta(1)*Starter.transformationAgentQty+regression.beta(2)*cont;

			cpuTemp=((regression1.beta(0) + regression1.beta(1)*(Starter.transformationAgentQty)+regression1.beta(2)*(cont)<100)?regression1.beta(0) + regression1.beta(1)*Starter.transformationAgentQty+regression1.beta(2)*cont:99);
			//System.out.println("CPUSSSSSS "+Math.pow(10,regression1.beta(0))+" " + regression1.beta(1)*(Starter.transformationAgentQty)+" "+regression1.beta(2)*(cont)+" "+Starter.transformationAgentQty);
			//cpuTemp=Math.pow(10,regression1.beta(0) + regression1.beta(1)*(Starter.transformationAgentQty)+regression1.beta(2)*(cont));
			cpuNoUsedTemp=regression2.beta(0) + regression2.beta(1)*Starter.transformationAgentQty+regression2.beta(2)*cont;
			memoryTemp=regression4.beta(0) + regression4.beta(1)*Starter.transformationAgentQty+regression4.beta(2)*cont;
			
			t1=ModelsProvisioning.getCpuSum()+cpuTemp;
			ModelsProvisioning.setCpuSum(t1);
			ModelsProvisioning.setCpuAvg(ModelsProvisioning.getCpuSum()/contator);
			Starter.model.setCpuUSED(cpuTemp);
			Starter.model.setMemoryUSED(memoryTemp);
			Starter.model.setCpu(cont);
			Starter.model.setTime(timeTemp);
			//float cpuReal = (float) (cpuTemp/Starter.cpuUsageVariable);
			//float timeReal = (float) (timeTemp*Starter.timeVariable);
			//float priceReal = (float) 0.0;
			
			if(timeTemp<Starter.minimumTime) {
				float priceReal = (float) (Starter.minimumTime * Starter.prices.getCpuPrice() * cont);
				priceReal += (float) (Starter.minimumTime * Starter.prices.getMemoryPrice() * cont);
				Starter.model.setPrice(priceReal);
			}else {
				float priceReal = (float) (timeTemp*Starter.prices.getCpuPrice() * cont);
				priceReal += (float) (timeTemp * Starter.prices.getMemoryPrice() * cont);
				Starter.model.setPrice(priceReal);
			}
			//priceReal = (float) (priceReal * Starter.priceVariable);
			
			//System.out.println(cpuReal+" "+timeReal+" "+priceReal+" "+cpuReal/(timeReal+priceReal));
			
			//Starter.model.setBalance(cpuTemp/(timeTemp+priceReal));
			
			//System.out.println("\n\n Provisionamento CPU Usado "+cpuTemp+ " Tempo "+timeTemp+" "+Starter.model.getTime()+" \n\n");
			Starter.model.setCpuNoUsed(cpuNoUsedTemp/time);
			
			sumNoUsed=sumNoUsed+Starter.model.getCpuNoUsed();
			ModelsProvisioning.setCpuNoUsedAvg(sumNoUsed/contator);

			if(ModelsProvisioning.getCpuMax()<cpuTemp){
				ModelsProvisioning.setCpuMax(cpuTemp);

			}
			Starter.valuesCpus.add(Starter.model);
			
			System.out.println(" -- --- --Agents "+ Starter.transformationAgentQty +" CPU " +cpuTemp +" CPU " +cont+" AVG " +Starter.model.getCpuAvg() +" max "+Starter.model.getCpuMax()+" tempo "+Starter.model.getTime()+" Balanço "+Starter.model.getBalance()+" Preço "+Starter.model.getBalance());
			if(cont==1)
				cont++;
			else
				cont+=2;
			contator++;

		}
		
		for(int i=0; i<Starter.valuesCpus.size();i++){
			
			ModelsProvisioning m = (ModelsProvisioning) Starter.valuesCpus.get(i);
			//System.out.println(m.getCpu()+" CPU "+ m.getCpuUSED()+" CPU Used "+m.getCpuMax()*0.6+" Time "+m.getTime());
			
			 System.out.println("Preco "+ModelsProvisioning.getPriceSelected() +" "+ (m.getPrice()+(m.getPrice()*Starter.priceVariable)));
			 System.out.println("tempo "+ModelsProvisioning.getTimeSelected() +" "+ (m.getTime()+(m.getTime()*Starter.timeVariable)));
			 System.out.println("cpu "+ModelsProvisioning.getCpuUsedSelected() +" "+ (m.getCpuUSED()+(m.getCpuUSED()*Starter.timeVariable)));
			 System.out.println("memory "+ModelsProvisioning.getMemoryUSEDSelected() +" "+ (m.getMemoryUSED()+(m.getMemoryUSED()*Starter.cpuUsageVariable)));
			        
			
			if(i==0){
				Starter.model.setTimeSelected(Double.MAX_VALUE);
				m.setCpu(Starter.valuesCpus.get(i).getCpu());
			}
			else
				m.setCpu(Starter.valuesCpus.get(i).getCpu());
			

			try {
				Starter.kSession.insert(m);
				Starter.kSession.fireAllRules();
				
				
			} catch (Throwable t) {
				t.printStackTrace();
			}
			
		}

		/*if(Starter.priceVariable >= Starter.cpuUsageVariable &&
		        Starter.priceVariable >= Starter.timeVariable) {
			Collections.sort(ModelsProvisioning.getCpusCandidates(), new Comparator<ModelsProvisioning>() {
			    @Override
			    public int compare(ModelsProvisioning z1, ModelsProvisioning z2) {
			        if (z1.getBalance() <= z2.getBalance())
			            return 1;
			        else if (z1.getBalance() > z2.getBalance())
			            return -1;
			        return 0;
			    }
			});
		}
		else if(Starter.timeVariable >= Starter.cpuUsageVariable &&
        Starter.timeVariable >= Starter.priceVariable) {
			Collections.sort(ModelsProvisioning.getCpusCandidates(), new Comparator<ModelsProvisioning>() {
			    @Override
			    public int compare(ModelsProvisioning z1, ModelsProvisioning z2) {
			        if (z1.getTime() < z2.getTime())
			            return 1;
			        else if (z1.getTime() > z2.getTime())
			            return -1;
			        return 0;
			    }
			});
		}
		else if(Starter.cpuUsageVariable >= Starter.timeVariable &&
        Starter.cpuUsageVariable >= Starter.priceVariable) {
			Collections.sort(ModelsProvisioning.getCpusCandidates(), new Comparator<ModelsProvisioning>() {
			    @Override
			    public int compare(ModelsProvisioning z1, ModelsProvisioning z2) {
			        if (z1.getCpuUSED()< z2.getCpuUSED())
			            return 1;
			        else if (z1.getCpuUSED() > z2.getCpuUSED())
			            return -1;
			        return 0;
			    }
			});
		}*/
		//System.out.println("Tamanho "+ModelsProvisioning.getCpusCandidatesSize());
		for(int i=0; i<ModelsProvisioning.getCpusCandidatesSize();i++){
			//System.out.println("CPU "+ModelsProvisioning.getCpusCandidates(i).getCpu()+" Tempo "+ModelsProvisioning.getCpusCandidates(i).getTime());
			try {
				
				
				Starter.kSession.insert(i);
				Starter.kSession.fireAllRules();

			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		FileWriter arq;
		PrintWriter gravarArq = null ;

		try {
			arq = new FileWriter("statsCPUSelected.csv",true);
			gravarArq = new PrintWriter(arq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gravarArq.append(Starter.transformationAgentQty + "," +ModelsProvisioning.getCpuUsedSelected()+"\n");
		gravarArq.close();
		
		//System.out.println(ModelsProvisioning.getCpusCandidates()+" KKKKK "+Starter.transformationAgentQty + "," +Starter.model.getCpuSelected());

	}




}

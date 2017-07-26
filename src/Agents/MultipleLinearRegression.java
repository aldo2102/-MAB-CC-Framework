package Agents;

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
		String [] nextLine;
		String strFile;
		CSVReader reader=null;
		int lineNumber = 0;
		int cont = 1;
		try {
			//csv file containing data
			strFile = base;
			reader = new CSVReader(new FileReader(strFile));

			while ((nextLine = reader.readNext()) != null) {
				lineNumber++;
			}
			x = new double[lineNumber][3];
			y = new double[lineNumber];
			x1 = new double[lineNumber][3];
			y1 = new double[lineNumber];
			x2 = new double[lineNumber][3];
			y2 = new double[lineNumber];
			x3 = new double[lineNumber][4];
			y3 = new double[lineNumber];
			reader = new CSVReader(new FileReader(strFile));
			lineNumber = 0;
			while ((nextLine = reader.readNext()) != null) {

				//System.out.println("Line # " + lineNumber);
				// nextLine[] is an array of values from the line
				if(nextLine.length>1){
					if(Character.isDigit(nextLine[0].charAt(0))){
						x[lineNumber][0]=1;
						x[lineNumber][1]=Math.log10(Double.parseDouble(nextLine[1]));
						x[lineNumber][2]=Math.log10(Double.parseDouble(nextLine[2]));
						y[lineNumber]=Math.log10(Double.parseDouble(nextLine[4]));
						x1[lineNumber][0]=1;
						x1[lineNumber][1]=Math.log10(Double.parseDouble(nextLine[1]));
						x1[lineNumber][2]=Math.log10(Double.parseDouble(nextLine[2]));
						y1[lineNumber]=Math.log10(Double.parseDouble(nextLine[3]));
						x2[lineNumber][0]=1;
						x2[lineNumber][1]=Math.log10(Double.parseDouble(nextLine[1]));
						x2[lineNumber][2]=Math.log10(Double.parseDouble(nextLine[2]));
						y2[lineNumber]=Math.log10(Double.parseDouble(nextLine[0]));
						x3[lineNumber][0]=1;
						x3[lineNumber][1]=Math.log10(Double.parseDouble(nextLine[1]));
						x3[lineNumber][2]=Math.log10(Double.parseDouble(nextLine[2]));
						x3[lineNumber][3]=Math.log10(Double.parseDouble(nextLine[3]));
						y3[lineNumber]=Math.log10(Double.parseDouble(nextLine[4]));

					}
					//System.out.println(nextLine[0] +" "+nextLine[1]+" "+nextLine[2]+" "+nextLine[3]+" "+nextLine[4]);
				}
				lineNumber++;
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		MultipleLinearRegression regression = new MultipleLinearRegression(x, y);

		System.out.printf("Time = %.2f + %.2f beta1 + %.2f beta2  (R^2 = %.2f)\n",
				regression.beta(0), regression.beta(1), regression.beta(2), regression.R2());

		MultipleLinearRegression regression1 = new MultipleLinearRegression(x1, y1);

		System.out.printf("CPU Used = %.2f + %.2f beta1 + %.2f beta2  (R^2 = %.2f)\n",
				regression1.beta(0), regression1.beta(1), regression1.beta(2), regression1.R2());

		MultipleLinearRegression regression2 = new MultipleLinearRegression(x2, y2);

		System.out.printf("CPU Used = %.2f + %.2f beta1 + %.2f beta2  (R^2 = %.2f)\n",
				regression2.beta(0), regression2.beta(1), regression2.beta(2), regression2.R2());

		MultipleLinearRegression regression3 = new MultipleLinearRegression(x3, y3);

		System.out.printf("CPU Time with CPU = %.2f + %.2f beta1 + %.2f beta2 + %.2f beta3   (R^2 = %.2f)\n",
				regression3.beta(0), regression3.beta(1), regression3.beta(2), regression3.beta(3),  regression3.R2());
		
		ModelsProvisioning.setR2Time(regression.R2());
		ModelsProvisioning.setR2CPU(regression1.R2());
		double time=Double.MAX_VALUE;
		double timeTemp=0,cpuTemp=0, cpuNoUsedTemp=0;
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
		while(cont<=32){

			//System.out.println(ModelsProvisioning.getCpuAvg()+" "+ModelsProvisioning.getCpuUSED()+" avg "+ModelsProvisioning.getCpuMax()*0.75);
			Starter.Starter.model = new ModelsProvisioning();
			timeTemp=Math.pow(10,regression.beta(0) + regression.beta(1)*Math.log10(Starter.Starter.transformationAgentQty)+regression.beta(2)*Math.log10(cont));

			cpuTemp=((Math.pow(10,regression1.beta(0) + regression1.beta(1)*Math.log10(Starter.Starter.transformationAgentQty)+regression1.beta(2)*Math.log10(cont))<100)?Math.pow(10,regression1.beta(0) + regression1.beta(1)*Math.log10(Starter.Starter.transformationAgentQty)+regression1.beta(2)*Math.log10(cont)):99);
			//cpuTemp=Math.pow(10,regression1.beta(0) + regression1.beta(1)*Math.log10(Starter.Starter.transformationAgentQty)+regression1.beta(2)*Math.log10(cont));
			cpuNoUsedTemp=Math.pow(10,regression2.beta(0) + regression2.beta(1)*Math.log10(Starter.Starter.transformationAgentQty)+regression2.beta(2)*Math.log10(cont));
			
			
			t1=ModelsProvisioning.getCpuSum()+cpuTemp;
			ModelsProvisioning.setCpuSum(t1);
			ModelsProvisioning.setCpuAvg(ModelsProvisioning.getCpuSum()/contator);
			Starter.Starter.model.setCpuUSED(cpuTemp);
			Starter.Starter.model.setCpu(cont);
			Starter.Starter.model.setTime(Math.ceil(timeTemp/60));

			Starter.Starter.model.setCpuNoUsed(cpuNoUsedTemp/time);
			
			sumNoUsed=sumNoUsed+Starter.Starter.model.getCpuNoUsed();
			ModelsProvisioning.setCpuNoUsedAvg(sumNoUsed/contator);

			if(ModelsProvisioning.getCpuMax()<cpuTemp){
				ModelsProvisioning.setCpuMax(cpuTemp);

			}
			Starter.Starter.valuesCpus.add(Starter.Starter.model);
			
			//System.out.println("Agents "+ Starter.Starter.transformationAgentQty +" CPU " +cpuTemp +" CPU " +cont+" AVG " +Starter.Starter.model.getCpuAvg() +" max "+Starter.Starter.model.getCpuMax());
			if(cont==1)
				cont++;
			else
				cont+=2;
			contator++;

		}

		//System.out.println("quantas CPU "+Starter.Starter.valuesCpus.size());
		for(int i=0; i<Starter.Starter.valuesCpus.size();i++){


			ModelsProvisioning m = (ModelsProvisioning) Starter.Starter.valuesCpus.get(i);
			//System.out.println(m.getCpu()+" CPU "+ m.getCpuUSED()+" CPU Used "+m.getCpuMax()*0.6+" Time "+m.getTime());
			
			
			if(i==0){
				ModelsProvisioning.setTimeSelected(Double.MAX_VALUE);
				m.setCpu(i+1);
			}
			else
				m.setCpu(i*2);

			try {
				Starter.Starter.kSession.insert(m);
				Starter.Starter.kSession.fireAllRules();

			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
		System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkk"+ModelsProvisioning.getCpusCandidates());
		for(int i=0; i<ModelsProvisioning.getCpusCandidates();i++){
			//System.out.println("CPU "+ModelsProvisioning.getCpusCandidates(i)+" Tempo "+ModelsProvisioning.getTimesCandidates(i)+" Preço "+ModelsProvisioning.getTimesCandidates(i)*ModelsProvisioning.getCpusCandidates(i)*Starter.Starter.prices.getCpuPrice());
			try {
				Starter.Starter.kSession.insert(i);
				Starter.Starter.kSession.fireAllRules();

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

		gravarArq.append(Starter.Starter.transformationAgentQty + "," +ModelsProvisioning.getCpuUsedSelected()+"\n");
		gravarArq.close();
		
		System.out.println(ModelsProvisioning.getCpusCandidates()+" KKKKK "+Starter.Starter.transformationAgentQty + "," +Starter.Starter.model.getCpuSelected());

	}




}

package Starter;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import models.ModelsConfigMachines;

public class PriceGoogleCloud {


  public static void prices() {
	  
		String jsonString = callURL("http://cloudpricingcalculator.appspot.com/static/data/pricelist.json");
		jsonString="[ "+jsonString+" ]";
		
		
		//System.out.println(jsonString);

//Replace this try catch block for all below subsequent examples
		try {  

			JSONArray jsonArray = new JSONArray(jsonString);

			JSONObject row = jsonArray.getJSONObject(0);
			row = row.getJSONObject("gcp_price_list");
			
			System.out.println(row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-CORE").toString());
			System.out.println(row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-RAM").toString());
			
			JSONObject CPU=row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-CORE");
			JSONObject Mem=row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-RAM");
			
			
			Double europeCpu=Double.parseDouble(CPU.get("europe").toString());
			Double asiaCpu=Double.parseDouble(CPU.get("asia").toString());
			Double usCpu=Double.parseDouble(CPU.get("us").toString());
			
			if(europeCpu<asiaCpu && europeCpu<usCpu){
				Starter.prices.setCpuPrice(europeCpu/60);
				((ModelsConfigMachines) Starter.prices).setLocalMem("europe");
			}else if(asiaCpu<europeCpu && asiaCpu<usCpu){
				Starter.prices.setCpuPrice(asiaCpu/60);
				((ModelsConfigMachines) Starter.prices).setLocalCpu("asia");
			}else {
				Starter.prices.setCpuPrice(usCpu/60);
				Starter.prices.setLocalMem("us");
			}
			
			Double europeMem=Double.parseDouble(Mem.get("europe").toString());
			Double asiaMem=Double.parseDouble(Mem.get("asia").toString());
			Double usMem=Double.parseDouble(Mem.get("us").toString());
			
			if(europeMem<asiaMem && europeMem<usMem){
				Starter.prices.setMemoryPrice(europeMem/60);
				Starter.prices.setLocalMem("europe");
			}else if(asiaMem<europeMem && asiaMem<usMem){
				Starter.prices.setMemoryPrice(asiaMem/60);
				Starter.prices.setLocalMem("asia");
			}else {
				Starter.prices.setMemoryPrice(usMem/60);
				Starter.prices.setLocalMem("us");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("preço "+Starter.prices.getCpuPrice());
	}

	public static String callURL(String myURL) {
		System.out.println("Requested URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 

		return sb.toString();
	}

}
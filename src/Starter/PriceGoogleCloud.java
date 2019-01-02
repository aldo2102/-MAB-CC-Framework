package Starter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import models.ModelsConfigMachines;

public class PriceGoogleCloud {
	private final static String USER_AGENT = "Mozilla/5.0";
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }

  public static void prices() throws IOException {
	  
		
		
		
		//System.out.println(jsonString);

//Replace this try catch block for all below subsequent examples
		try {  
			//JSONObject jsonString = readJsonFromUrl("https://cloudpricingcalculator.appspot.com/static/data/pricelist.json");
			FileReader reader = new FileReader("pricelist.json");
		    JSONTokener tokener = new JSONTokener(reader);
		    JSONObject jsonString = new JSONObject(tokener);
			
			
			JSONObject row = jsonString.getJSONObject("gcp_price_list");
			
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

	
}
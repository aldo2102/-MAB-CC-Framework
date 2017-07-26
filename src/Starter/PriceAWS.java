package Starter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import models.ModelPricesAWS;


public class PriceAWS {


  public static void prices() {
	  
		String jsonString = callURL("https://a0.awsstatic.com/pricing/1/deprecated/ec2/pricing-on-demand-instances.json");
		jsonString="[ "+jsonString+" ]";
		
		
		//System.out.println(jsonString);

//Replace this try catch block for all below subsequent examples
		try {  

			JSONArray jsonArray = new JSONArray(jsonString);

			JSONObject row = jsonArray.getJSONObject(0);
			System.out.println(row);
			row = row.getJSONObject("config");

			jsonArray = row.getJSONArray("regions");
			
			

			JSONArray jsonArray2;
			JSONArray jsonArray3;
			ModelPricesAWS item = new ModelPricesAWS();
			for(int i=0;i<jsonArray.length();i++) {
				
				item.setRegion(jsonArray.getJSONObject(i).get("region").toString());
				
				jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("instanceTypes").getJSONObject(2).getJSONArray("sizes");
				
				
				for(int j=0;j<jsonArray2.length();j++) {
					
					item.setSize(jsonArray2.getJSONObject(j).get("size").toString());
					
					jsonArray3 = jsonArray2.getJSONObject(j).getJSONArray("valueColumns");
					
					jsonArray3 = new JSONArray("["+ jsonArray3.getJSONObject(0).get("prices").toString() +"]");
					
					item.setPrice(Double.parseDouble(jsonArray3.getJSONObject(0).get("USD").toString()));
					
					Starter.MV.add(item);
				}
			}
			
			

			//System.out.println(row);
			/* 
			System.out.println(row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-CORE").toString());
			System.out.println(row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-RAM").toString());
			
			JSONObject CPU=row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-CORE");
			JSONObject Mem=row.getJSONObject("CP-COMPUTEENGINE-CUSTOM-VM-RAM");
			
			
			Double europeCpu=Double.parseDouble(CPU.get("europe").toString());
			Double asiaCpu=Double.parseDouble(CPU.get("asia").toString());
			Double usCpu=Double.parseDouble(CPU.get("us").toString());
			*/
			
			

		} catch (JSONException e) {
			e.printStackTrace();
		}
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
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


public class PriceVangrant {


  public static void prices() {
	  

		try {  

			Starter.prices.setCpuPrice((double) 1);
			Starter.prices.setMemoryPrice((double) 1);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
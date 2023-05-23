package com.example.workflow;
import java.net.HttpURLConnection;
import java.text.NumberFormat;
import java.net.*;
import java.util.Locale;
import java.util.Scanner;
import javax.inject.Named;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.DelegateExecution;

@Named
public class Example implements JavaDelegate {
	@Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {

            URL url = new URL("https://api.hnb.hr/tecajn-eur/v3?valuta=USD");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();

                JSONParser parse = new JSONParser();
                JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));


                JSONObject currencyData = (JSONObject) dataObject.get(0);

                
                String srednjiTecaj = currencyData.get("srednji_tecaj").toString();
                
                NumberFormat nf_in = NumberFormat.getNumberInstance(Locale.GERMANY);
                double val = nf_in.parse(srednjiTecaj).doubleValue();
                
                double tecajUSD = 1/val;
                
                delegateExecution.setVariable("srednji_tecaj", tecajUSD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

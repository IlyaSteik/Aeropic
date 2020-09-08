package com.travels.searchtravels;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class PriceTest {

    @Test
    public void price_isCorrect(){
        URL url = null;
        try {
            String city = "Rimini";
            URL obj = new URL("https://autocomplete.travelpayouts.com/places2?locale=en&types[]=city&term="+city);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0" );
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection.setRequestProperty("Content-Type", "application/json");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            JSONArray responseJSON = new JSONArray(response.toString());
            Log.d("myLogs", "responseJSON = " + responseJSON);
            String code = responseJSON.getJSONObject(0).getString("code");
            String countryName = responseJSON.getJSONObject(0).getString("country_name");

            Log.d("myLogs", "code = " + code);

            URL obj2 = new URL("https://api.travelpayouts.com/v1/prices/cheap?origin=LED&depart_date=2020-10-10&return_date=2020-10-20&token=471ae7d420d82eb92428018ec458623b&destination="+code);
            HttpURLConnection connection2 = (HttpURLConnection) obj2.openConnection();

            //add reuqest header
            connection2.setRequestMethod("GET");
            connection2.setRequestProperty("User-Agent", "Mozilla/5.0" );
            connection2.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection2.setRequestProperty("Content-Type", "application/json");
            connection2.setRequestProperty("X-Access-Token", "471ae7d420d82eb92428018ec458623b");

            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
            String inputLine2;
            StringBuffer response2 = new StringBuffer();

            while ((inputLine2 = bufferedReader2.readLine()) != null) {
                response2.append(inputLine2);
            }
            bufferedReader2.close();

            JSONObject responseJSON2 = new JSONObject(response2.toString());
            Log.d("myLogs", "responseJSON2 = " + responseJSON2);

            Integer tiketPriceOriginal = 12393;
            Integer ticketPrice = responseJSON2.getJSONObject("data").getJSONObject(code).getJSONObject("1").getInt("price");
            assertEquals(tiketPriceOriginal, ticketPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

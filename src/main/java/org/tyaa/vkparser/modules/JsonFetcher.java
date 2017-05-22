/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.vkparser.modules;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.unbescape.html.HtmlEscape;

/**
 *
 * @author Юрий
 */
public class JsonFetcher
{
    public String fetchByUrl(String _urlString){
    
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonString = "";
        
        try {
            
            URL url = new URL(_urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            jsonString = HtmlEscape.unescapeHtml(buffer.toString());
            //jsonString = jsonString.replace("?", "");
            //byte[] b = jsonString.getBytes("UTF-8");
            //jsonString = new String(jsonString.getBytes("Cp1251"),"UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}

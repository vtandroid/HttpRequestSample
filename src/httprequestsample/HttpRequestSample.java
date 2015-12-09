/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequestsample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author hoabt2
 */
public class HttpRequestSample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ClientRequestHelper clientRequestHelper = new ClientRequestHelper();
        clientRequestHelper.setProxy("10.61.11.39", 3128);
        HashMap<String, String> hmR = new HashMap<String, String>();
        hmR.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        hmR.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        hmR.put("Accept-Language", "en-US,en;q=0.5");
        hmR.put("Content-Type", "application/x-www-form-urlencoded");
        String result = clientRequestHelper.sendGet("http://www.otohits.net/account/login", hmR);
        Document doc = Jsoup.parse(result);
        Elements lstElement = doc.getElementsByAttributeValue("name", "__RequestVerificationToken");
        String requestVeriToken = "";
        HashMap<String, String> hmData = new HashMap<>();

        if (lstElement != null && lstElement.size() > 0) {
            requestVeriToken = lstElement.get(0).attr("value");
        }
        hmData.put("__RequestVerificationToken", requestVeriToken);
        hmData.put("Email", "getmoneykhmt3@gmail.com");
        hmData.put("Password", "asd123");
        result = clientRequestHelper.sendPost("http://www.otohits.net/account/login", hmR, hmData);
        System.out.println(result);

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequestsample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

/**
 *
 * @author hoabt2
 */
public class ClientRequestHelper {

    private PersistentCookieStore cookieStore = null;
    private DefaultHttpClient mClient = null;

    public ClientRequestHelper() {
        cookieStore = new PersistentCookieStore();
        mClient = new DefaultHttpClient();
        mClient.setCookieStore(cookieStore);
    }

    public void setProxy(String host, int port) {
        HttpHost proxy = new HttpHost(host, port, "http");
        mClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
       // mClient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, true);
        mClient.getParams().setParameter(ClientPNames.MAX_REDIRECTS, 100);
        
        mClient.setRedirectStrategy(new DefaultRedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
                boolean isRedirect = false;
                try {
                    isRedirect = super.isRedirected(request, response, context);
                } catch (ProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == 301 || responseCode == 302) {
                        return true;
                    }
                }
                return isRedirect;
            }
        });
    }

    public String sendGet(String url, HashMap<String, String> hmHeader) {
        String response = "";
        HttpGet httpGet = new HttpGet(url);
        Set<String> setKey = hmHeader.keySet();
        //Add request Header
        for (String key : setKey) {
            String value = hmHeader.get(key);
            if (value != null) {
                httpGet.addHeader(key, value);
            }
        }
        //

        HttpResponse httpResponse;
        try {
            httpResponse = mClient.execute(httpGet);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(httpResponse.getEntity().getContent()));
            System.out.println("Status Code:" + httpResponse.getStatusLine().getStatusCode());
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            response = result.toString();
        } catch (IOException ex) {
            Logger.getLogger(ClientRequestHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public String sendPost(String url, HashMap<String, String> hmHeader, HashMap<String, String> hmData) {
        String response = "";
        try {
            HttpPost post = new HttpPost(url);
            Set<String> setKey = hmHeader.keySet();
            //Add request Header
            for (String key : setKey) {
                String value = hmHeader.get(key);
                if (value != null) {
                    post.addHeader(key, value);
                }
            }
            //
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            Set<String> setKeyData = hmData.keySet();
            for (String key : setKeyData) {
                String value = hmData.get(key);
                if (value != null) {
                    urlParameters.add(new BasicNameValuePair(key, value));
                }
            }

            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse httpResponse = mClient.execute(post);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(httpResponse.getEntity().getContent()));

            System.out.println("Status Code:" + httpResponse.getStatusLine().getStatusCode());
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            response = result.toString();

        } catch (Exception ex) {
            Logger.getLogger(ClientRequestHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
}

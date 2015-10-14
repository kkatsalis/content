/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FlowPusher;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author kostas
 */
public class ApacheHttpClient {
 
 String controllerIP;    

    public ApacheHttpClient(String controllerIP) {
        this.controllerIP=controllerIP;
    }
 
 
 public void postFloodlightClient(String jsonBody) throws UnsupportedEncodingException, IOException
 {

     try {
         
         CloseableHttpClient httpClient = HttpClients.createDefault();
         
         URI uri = new URIBuilder()
                 .setScheme("http")
                 .setHost(controllerIP+":8080")
                 .setPath("/wm/staticflowentrypusher/json")
                 .build();

         HttpPost httpPost=new HttpPost(uri);
         
        StringEntity params =new StringEntity(jsonBody);
        httpPost.addHeader("content-type", "application/json");
        httpPost.setEntity(params);
        HttpResponse response = httpClient.execute(httpPost);
         
        String x="kostas";
     } catch (URISyntaxException ex) {
         Logger.getLogger(ApacheHttpClient.class.getName()).log(Level.SEVERE, null, ex);
     }
     

 } 
    
public void deleteFloodlightClient(String flowName) throws UnsupportedEncodingException, IOException
{

    String body ="{\"name\":\"" + flowName + "\"}";
    String url="http://"+controllerIP+":8080/wm/staticflowentrypusher/json";
    
      CloseableHttpClient httpClient = HttpClients.createDefault();
    
        HttpDeleteWithBody httpdelete=new HttpDeleteWithBody(url);
       
        StringEntity params =new StringEntity(body);
        httpdelete.addHeader("content-type", "application/json");
        httpdelete.setEntity(params);
        HttpResponse response = httpClient.execute(httpdelete);
    
       String y;

}        
    
     class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";

        public String getMethod() {
            return METHOD_NAME;
        }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        public HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpDeleteWithBody() {
            super();
        }
    }   
     
    
    
 

    
}
   
   
   
   

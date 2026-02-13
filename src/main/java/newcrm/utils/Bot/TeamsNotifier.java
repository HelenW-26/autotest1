package newcrm.utils.Bot;

import newcrm.global.GlobalProperties;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

public class TeamsNotifier {
  
  public void SendNotificationToTeams(String webhookUrl, String message) {
      CloseableHttpClient client = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(webhookUrl);

      try {
          JSONObject json = new JSONObject();
          json.put("text", message);

          StringEntity entity = new StringEntity(json.toString(), org.apache.http.entity.ContentType.APPLICATION_JSON);
          httpPost.setEntity(entity);
          httpPost.setHeader("Accept", "application/json");
          httpPost.setHeader("Content-type", "application/json; charset=UTF-8");

          HttpResponse response = client.execute(httpPost);
          String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

          // Check for a successful response
          if (response.getStatusLine().getStatusCode() == 200) {
              System.out.println("Message sent successfully: " + responseString);
          } else {
              System.out.println("Failed to send message: " + responseString);
          }

          client.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  public void main(String[] args) {
	   String webhookUrl = GlobalProperties.AUTOMATION_CPS_Monitor_webhookUrl;
	   String message = "Hello, Teams!";
	   SendNotificationToTeams(webhookUrl,message);
	  
  }
}

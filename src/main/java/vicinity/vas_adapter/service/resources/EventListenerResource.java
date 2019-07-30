package vicinity.vas_adapter.service.resources;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import java.io.IOException;

public class EventListenerResource extends ServerResource {

    private String executeSubscribe(String iid, String oid, String eid) throws IOException {
        //send POST to agent: /objects/{oid}/events/{eid}
        HttpClient client = HttpClientBuilder.create().build();
        String callEndpoint = System.getProperty("agent.endpoint") + "/objects/" + oid + "/events/" + eid;

        System.out.println("----------------------------------------------------------");
        System.out.println("SUBSCRIBING TO EVENT CHANNEL:");
        System.out.println("endpoint: " + callEndpoint);

        HttpPost request = new HttpPost(callEndpoint);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("adapter-id", System.getProperty("adapter.id"));
        request.addHeader("infrastructure-id", iid);

        HttpResponse response = client.execute(request);
        int status = response.getStatusLine().getStatusCode();

        System.out.println("----------------------------------------------------------");
        System.out.println("POST status: " + status);
        String responseContent = EntityUtils.toString(response.getEntity());
        System.out.println("response: " + responseContent);

        System.out.println("----------------------------------------------------------");

        return responseContent;
    }

    private String executeUnsubscribe(String iid, String oid, String eid) throws IOException {
        //send DELETE to agent: /objects/{oid}/events/{eid}
        HttpClient client = HttpClientBuilder.create().build();
        String callEndpoint = System.getProperty("agent.endpoint") + "/objects/" + oid + "/events/" + eid;

        System.out.println("----------------------------------------------------------");
        System.out.println("UNSUBSCRIBING FROM EVENT CHANNEL:");
        System.out.println("endpoint: " + callEndpoint);

        HttpDelete request = new HttpDelete(callEndpoint);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("adapter-id", System.getProperty("adapter.id"));
        request.addHeader("infrastructure-id", iid);

        HttpResponse response = client.execute(request);
        int status = response.getStatusLine().getStatusCode();

        System.out.println("----------------------------------------------------------");
        System.out.println("POST status: " + status);
        String responseContent = EntityUtils.toString(response.getEntity());
        System.out.println("response: " + responseContent);

        System.out.println("----------------------------------------------------------");

        return responseContent;
    }

    private String receiveEvent(String iid, String eid, JSONObject out) {
        // do sth with received data
        // anomaly detection
        // data anomalous? send alarm
        return out.toString();
    }

    @Post
    public String subscribe() {
        try {
            String iid = this.getAttribute("iid");
            String oid = this.getAttribute("oid");
            String eid = this.getAttribute("eid");

            System.out.println(String.format("Subscribing %s to event channel: %s", iid, eid));

            return this.executeSubscribe(iid, oid, eid);
        } catch (Exception e) {
            return "{\"something\": \"went ape\"}";
        }

    }

    @Delete
    public String unsubscribe() {
        try {
            String iid = this.getAttribute("iid");
            String oid = this.getAttribute("oid");
            String eid = this.getAttribute("eid");

            return this.executeUnsubscribe(iid, oid, eid);
        } catch (Exception e) {
            return "{\"something\": \"went ape\"}";
        }

    }

    @Put
    public String receiveEvent(Representation entity) {
        try {
            String iid = this.getAttribute("iid");
            String oid = this.getAttribute("oid");
            String eid = this.getAttribute("eid");
            System.out.println(String.format("Service %s: Receiving event from %s: %s", iid, oid, eid));

            JSONObject input = new JSONObject(entity.getText());
            JSONObject out = new JSONObject();
            out.put("echo", "receiving event");
            out.put("iid", iid);
            out.put("eid", eid);
            out.put("payload", input);
            return this.receiveEvent(iid, eid, out);
        } catch (Exception e) {
            return "{\"something\": \"went ape\"}";
        }
    }
}

package vicinity.vas_adapter.service.resources;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

//Resource for activating/deactivating event channel and publishing event for sending alarm when anomaly is detected
//Maybe won't be used if alarm needs to be sent to another dest.
public class EventChannelResource extends ServerResource {
    private String executeActivation(String iid, String eid, JSONObject payload) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        String callEndpoint = System.getProperty("agent.endpoint") + "/events/" + eid;

        System.out.println("----------------------------------------------------------");
        System.out.println("ACTIVATING EVENT CHANNEL:");
        System.out.println("endpoint: " + callEndpoint);
        System.out.println("payload: " + payload.toString());

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

    private String executeDeactivation(String iid, String eid, JSONObject payload) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        String callEndpoint = System.getProperty("agent.endpoint") + "/events/" + eid;

        System.out.println("----------------------------------------------------------");
        System.out.println("DEACTIVATING EVENT CHANNEL:");
        System.out.println("endpoint: " + callEndpoint);
        System.out.println("payload: " + payload.toString());

        HttpDelete request = new HttpDelete(callEndpoint);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("adapter-id", System.getProperty("adapter.id"));
        request.addHeader("infrastructure-id", iid);

        HttpResponse response = client.execute(request);
        int status = response.getStatusLine().getStatusCode();

        System.out.println("----------------------------------------------------------");
        System.out.println("DELETE status: " + status);
        String responseContent = EntityUtils.toString(response.getEntity());
        System.out.println("response: " + responseContent);

        System.out.println("----------------------------------------------------------");

        return responseContent;
    }

    private String executePublishing(String iid, String eid, JSONObject payload) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        String callEndpoint = System.getProperty("agent.endpoint") + "/events/" + eid;

        System.out.println("----------------------------------------------------------");
        System.out.println("EXECUTING EVENT PUBLISHING:");
        System.out.println("endpoint: " + callEndpoint);
        System.out.println("payload: " + payload.toString());

        HttpPut request = new HttpPut(callEndpoint);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("infrastructure-id", iid);
        request.addHeader("adapter-id", System.getProperty("adapter.id"));
        StringEntity data = new StringEntity(payload.toString());
        request.setEntity(data);
        HttpResponse response = client.execute(request);
        int status = response.getStatusLine().getStatusCode();

        System.out.println("----------------------------------------------------------");
        System.out.println("PUT status: " + status);
        String responseContent = EntityUtils.toString(response.getEntity());
        System.out.println("response: " + responseContent);
        return responseContent;
    }

    @Post
    public String activate(Representation entity) {
        try {
            System.out.println("Requested event channel activation...");
            String eid = this.getAttribute("eid");
            String iid = this.getAttribute("iid");
            JSONObject out = new JSONObject();
            out.put("echo", "publish event");
            out.put("eid", eid);
            return this.executeActivation(iid, eid, out);
        } catch (Exception e) {
            return "{\"something\": \"went ape\"}";
        }
    }

    @Put
    public String publish(Representation entity) {
        try {
            System.out.println("Requested event publishing...");
            String iid = this.getAttribute("iid");
            String eid = this.getAttribute("eid");
            JSONObject input = new JSONObject(entity.getText());
            JSONObject out = new JSONObject();
            out.put("echo", "publish event");
            out.put("iid", iid);
            out.put("eid", eid);
            out.put("payload", input);
            return this.executePublishing(iid, eid, out);
        } catch (Exception e) {
            return "{\"something\": \"went ape\"}";
        }
    }

    @Delete
    public String deactivate(Representation entity) {
        try {
            System.out.println("Requested event channel deactivation...");
            String eid = this.getAttribute("eid");
            String iid = this.getAttribute("iid");
            JSONObject out = new JSONObject();
            out.put("echo", "publish event");
            out.put("eid", eid);
            return this.executeDeactivation(iid, eid, out);
        } catch (Exception e) {
            return "{\"something\": \"went ape\"}";
        }
    }
}

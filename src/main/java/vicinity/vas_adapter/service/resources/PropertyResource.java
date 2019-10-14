package vicinity.vas_adapter.service.resources;

import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class PropertyResource extends ServerResource {
    public PropertyResource() {
    }

    @Get("json")
    public String getPropertyValue() {
        try {
            System.out.println("getting property");
            String oid = this.getAttribute("oid");
            String pid = this.getAttribute("pid");
            JSONObject out = new JSONObject();
            out.put("echo", "get property");
            out.put("oid", oid);
            out.put("pid", pid);
            return out.toString();
        } catch (Exception var4) {
            return "{}";
        }
    }

    @Put
    public String setPropertyValue(Representation entity) {
        try {
            System.out.println("setting property");
            String oid = this.getAttribute("oid");
            String pid = this.getAttribute("pid");
            JSONObject input = new JSONObject(entity.getText());
            JSONObject out = new JSONObject();
            out.put("echo", "set property");
            out.put("oid", oid);
            out.put("pid", pid);
            out.put("payload", input);
            return out.toString();
        } catch (Exception var6) {
            return "{}";
        }
    }
}

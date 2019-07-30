package vicinity.vas_adapter.service;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class AdapterComponent extends Component {

    AdapterComponent(){
        this.getServers().add(Protocol.HTTP, Integer.parseInt(System.getProperty("server.port")));
        this.getDefaultHost().attach("/adapter", new AdapterApplication());
    }
}

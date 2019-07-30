package vicinity.vas_adapter.service;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import vicinity.vas_adapter.service.resources.EventChannelResource;
import vicinity.vas_adapter.service.resources.EventListenerResource;
import vicinity.vas_adapter.service.resources.GetSetPropertyResource;
import vicinity.vas_adapter.service.resources.ObjectsResource;

public class AdapterApplication extends Application {

    // iid - internal object identifier (oid as defined in thing description .json file
    // oid - VICINITY object identifier
    // Agent/Adapter holds oid -> iid mappings

    private static final String OBJECTS = "/objects";
    private static final String GET_SET_PROPERTY = "/objects/{oid}/properties/{pid}";
    private static final String EVENT_CHANNEL = "/objects/{iid}/events/{eid}";
    private static final String EVENT_LISTENER="/objects/{iid}/publishers/{oid}/events/{eid}";


    private ChallengeAuthenticator createApiGuard(Restlet next) {
        ChallengeAuthenticator apiGuard = new ChallengeAuthenticator(this.getContext(), ChallengeScheme.HTTP_BASIC, "realm");
        apiGuard.setNext(next);
        apiGuard.setOptional(true);
        return apiGuard;
    }

    private Router createApiRouter() {
        Router apiRouter = new Router(this.getContext());

        apiRouter.attach(OBJECTS, ObjectsResource.class); //GET http request
        apiRouter.attach(GET_SET_PROPERTY, GetSetPropertyResource.class); //GET & POST http request
        apiRouter.attach(EVENT_CHANNEL + "/activate", EventChannelResource.class); //POST http request
        apiRouter.attach(EVENT_CHANNEL + "/deactivate", EventChannelResource.class); //DELETE http request
        apiRouter.attach(EVENT_CHANNEL + "/publish", EventChannelResource.class); //PUT http request
        apiRouter.attach(EVENT_LISTENER, EventListenerResource.class);
        return apiRouter;
    }

    public Restlet createInboundRoot() {
        Router apiRouter = this.createApiRouter();
        ChallengeAuthenticator apiGuard = this.createApiGuard(apiRouter);
        return apiGuard;
    }
}

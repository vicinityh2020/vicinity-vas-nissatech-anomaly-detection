package vicinity.vas_adapter.service;

public class AdapterServer {
    public AdapterServer() {
    }

    public static void main(String[] args) throws Exception {
        AdapterComponent component = new AdapterComponent();
        component.start();
        System.out.println("starting");
    }
}
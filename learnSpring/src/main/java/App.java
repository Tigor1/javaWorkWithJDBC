import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    private Client client;
    private EventLogger eventLogger;

    private void logEvent(String msg) {
        String result = msg.replaceAll(String.valueOf(client.getId()), client.getFullName());
        eventLogger.logEvent();
    }

    public App(Client client, EventLogger eventLogger) {
        this.client = client;
        this.eventLogger = eventLogger;
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("common.xml");

        App app = (App) context.getBean("app");

        app.logEvent("log 1");
        app.logEvent("log 2");
    }
}

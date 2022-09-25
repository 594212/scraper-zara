import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bot {
    private Logger log = LoggerFactory.getLogger(getClass());

    public String process(String message) {
        if (message == null) {
            return null; // skip non-text messages
        }

        log.info("Received message: {}", message);

        return "Why did you say \"" + message.replace("\"", "-") + "\"?";
    }


}

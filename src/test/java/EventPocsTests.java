import org.apache.logging.log4j.core.async.RingBufferLogEvent;
import org.junit.jupiter.api.Test;

public class EventPocsTests {

    @Test
    public void executeRingBufferLogEventTest(){
        RingBufferLogEvent event = new RingBufferLogEvent();
        event.execute(true);
    }
}

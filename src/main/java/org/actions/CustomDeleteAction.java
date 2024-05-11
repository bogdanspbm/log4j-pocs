package org.actions;

import org.apache.logging.log4j.core.appender.rolling.action.*;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.script.AbstractScript;

import java.io.IOException;

public class CustomDeleteAction {

    DeleteAction action;

    public CustomDeleteAction(String basePath) {
        action = DeleteAction.createDeleteAction(basePath, false, 1, true, new PathSorter() {
            @Override
            public int compare(PathWithAttributes o1, PathWithAttributes o2) {
                return o1.toString().compareTo(o2.toString());
            }
        }, new PathCondition[]{
                IfFileName.createNameCondition(String.format("%s-*.log.zip", "appname"), null, IfAccumulatedFileCount.createFileCountCondition(1))
        }, null, new DefaultConfiguration());
    }

    public boolean execute() throws IOException {
        return action.execute();
    }


}

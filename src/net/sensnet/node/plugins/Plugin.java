package net.sensnet.node.plugins;

import net.sensnet.node.SensNetNodeConfiguration;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public abstract class Plugin {
    private SensNetNodeConfiguration configuration;

    public Plugin(SensNetNodeConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getProperty(String property) {
        String desiredOrp = getClass().getName().toLowerCase() + "." + property;
        String res = configuration.getProperty(desiredOrp);
        if (res == null) {
            throw new IllegalArgumentException("Misconfigured plugin '"
                    + getClass().getName()
                    + "'! Couldn't find configuration property '" + desiredOrp
                    + "'.");
        }
        return res;
    }

    public Logger getLoger() {
        return Log.getLogger("PLUGIN: " + getClass().getSimpleName());
    }
}

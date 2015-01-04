package org.cacert.gigi.output.template;

import java.io.PrintWriter;
import java.util.Map;

public final class OutputVariableCommand implements Outputable {

    private final String raw;

    public OutputVariableCommand(String raw) {
        this.raw = raw;
    }

    @Override
    public void output(PrintWriter out, Map<String, Object> vars) {
        Template.outputVar(out, vars, raw);
    }
}

package org.cacert.gigi.output.template;

import java.io.PrintWriter;
import java.util.Map;

public final class IfStatement implements Outputable {

    private final String variable;
    private final TemplateBlock body;

    public IfStatement(String variable, TemplateBlock body) {
        this.variable = variable;
        this.body = body;
    }

    @Override
    public void output(PrintWriter out, Map<String, Object> vars) {
        Object o = vars.get(variable);
        if (!(o == Boolean.FALSE || o == null)) {
            body.output(out, vars);
        }
    }
}

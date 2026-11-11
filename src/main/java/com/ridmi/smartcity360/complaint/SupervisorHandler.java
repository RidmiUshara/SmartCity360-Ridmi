
package com.ridmi.smartcity360.complaint;

public class SupervisorHandler extends ComplaintHandler {

    @Override
    protected boolean canHandle(Severity severity) {
        return severity == Severity.MEDIUM;
    }

    @Override
    protected String getRole() {
        return "Supervisor";
    }
}

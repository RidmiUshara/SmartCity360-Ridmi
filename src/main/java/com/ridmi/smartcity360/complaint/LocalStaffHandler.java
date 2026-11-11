
package com.ridmi.smartcity360.complaint;

public class LocalStaffHandler extends ComplaintHandler {

    @Override
    protected boolean canHandle(Severity severity) {
        return severity == Severity.LOW;
    }

    @Override
    protected String getRole() {
        return "Local Staff";
    }
}

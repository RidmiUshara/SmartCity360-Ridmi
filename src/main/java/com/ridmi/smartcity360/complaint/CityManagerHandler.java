
package com.ridmi.smartcity360.complaint;

public class CityManagerHandler extends ComplaintHandler {

    @Override
    protected boolean canHandle(Severity severity) {
        return severity == Severity.HIGH ||
                severity == Severity.CRITICAL;
    }

    @Override
    protected String getRole() {
        return "City Manager";
    }
}

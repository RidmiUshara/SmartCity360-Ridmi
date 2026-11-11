
package com.ridmi.smartcity360.complaint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Complaint {

    private final String description;
    private final String location;
    private final Severity severity;

    private String assignedTo;
    private final List<String> escalationPath = new ArrayList<>();

    public Complaint(String description, String location,
                     Severity severity) {
        this.description = description;
        this.location = location;
        this.severity = Objects.requireNonNull(severity);
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void addEscalationStep(String officer) {
        escalationPath.add(officer);
    }

    public List<String> getEscalationPath() {
        return List.copyOf(escalationPath);
    }
}

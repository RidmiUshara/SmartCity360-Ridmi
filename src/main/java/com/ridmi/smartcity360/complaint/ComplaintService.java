package com.ridmi.smartcity360.complaint;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ComplaintService {
    private final ComplaintHandler firstHandler;
    private final List<Complaint> complaints = new ArrayList<>();

    public ComplaintService() {
        LocalStaffHandler local = new LocalStaffHandler();
        SupervisorHandler supervisor = new SupervisorHandler();
        CityManagerHandler manager = new CityManagerHandler();
        local.setNext(supervisor);
        supervisor.setNext(manager);
        this.firstHandler = local;
    }

    public synchronized Complaint submit(String description, String location, Severity severity) {
        if (description == null || description.isBlank() || description.length() > 300) {
            throw new IllegalArgumentException("Complaint description must contain 1 to 300 characters.");
        }
        if (location == null || location.isBlank() || location.length() > 120) {
            throw new IllegalArgumentException("Location must contain 1 to 120 characters.");
        }
        Complaint complaint = new Complaint(description.trim(), location.trim(), Objects.requireNonNull(severity));
        firstHandler.handle(complaint);
        complaints.add(0, complaint);
        return complaint;
    }

    public synchronized List<Complaint> getComplaints() {
        return List.copyOf(complaints);
    }
}

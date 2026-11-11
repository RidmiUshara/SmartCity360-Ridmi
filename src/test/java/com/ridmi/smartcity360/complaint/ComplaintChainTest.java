
package com.ridmi.smartcity360.complaint;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComplaintChainTest {

    @Test
    void testComplaintEscalation() {

        LocalStaffHandler local = new LocalStaffHandler();
        SupervisorHandler supervisor = new SupervisorHandler();
        CityManagerHandler manager = new CityManagerHandler();

        // Connect the chain
        local.setNext(supervisor);
        supervisor.setNext(manager);

        Complaint low = new Complaint(
                "Street light not working",
                "Main Street",
                Severity.LOW
        );

        local.handle(low);

        assertEquals("Local Staff", low.getAssignedTo());
        assertEquals(
                List.of("Local Staff"),
                low.getEscalationPath()
        );

        Complaint medium = new Complaint(
                "Garbage not collected",
                "Colombo",
                Severity.MEDIUM
        );

        local.handle(medium);

        assertEquals("Supervisor", medium.getAssignedTo());
        assertEquals(
                List.of("Local Staff", "Supervisor"),
                medium.getEscalationPath()
        );

        Complaint critical = new Complaint(
                "Dangerous electrical wire",
                "City Centre",
                Severity.CRITICAL
        );

        local.handle(critical);

        assertEquals("City Manager", critical.getAssignedTo());
        assertEquals(
                List.of("Local Staff", "Supervisor", "City Manager"),
                critical.getEscalationPath()
        );
    }
}

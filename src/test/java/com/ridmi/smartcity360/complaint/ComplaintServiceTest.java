package com.ridmi.smartcity360.complaint;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ComplaintServiceTest {
    @Test
    void recordsEscalationAndHistory() {
        ComplaintService service = new ComplaintService();
        Complaint result = service.submit("Power line danger", "City Centre", Severity.CRITICAL);
        assertEquals("City Manager", result.getAssignedTo());
        assertEquals(List.of("Local Staff", "Supervisor", "City Manager"), result.getEscalationPath());
        assertEquals(1, service.getComplaints().size());
        assertThrows(IllegalArgumentException.class, () -> service.submit(" ", "Street", Severity.LOW));
    }
}

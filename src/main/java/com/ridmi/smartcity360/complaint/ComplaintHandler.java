
package com.ridmi.smartcity360.complaint;

public abstract class ComplaintHandler {

    private ComplaintHandler nextHandler;

    public ComplaintHandler setNext(ComplaintHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public void handle(Complaint complaint) {

        complaint.addEscalationStep(getRole());

        if (canHandle(complaint.getSeverity())) {

            complaint.setAssignedTo(getRole());

        } else if (nextHandler != null) {

            nextHandler.handle(complaint);

        } else {

            throw new IllegalStateException(
                    "No handler available for this complaint"
            );
        }
    }

    protected abstract boolean canHandle(Severity severity);

    protected abstract String getRole();
}

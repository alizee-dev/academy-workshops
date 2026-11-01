package projects.academyApp.workshop;

import projects.academyApp.utils.FieldValidator;

public class OnlineWorkshop extends Workshop {
    private final String meetingLink;

    public OnlineWorkshop(int id, String title, int capacity, String meetingLink) {
        super(id, title, capacity);
        this.meetingLink = FieldValidator.requireNonBlank(meetingLink, "Meeting link");
    }

    @Override
    public String joiningInstructions() {
        return "Join the course via: " + meetingLink;
    }
}

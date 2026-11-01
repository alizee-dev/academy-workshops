package projects.academyApp.workshop;

import projects.academyApp.utils.FieldValidator;

public class ClassroomWorkshop extends Workshop{
    private final String room;

    public ClassroomWorkshop(int id, String title, int capacity, String room) {
        super(id, title, capacity);
        this.room = FieldValidator.requireNonBlank(room, "Room");
    }

    @Override
    public String joiningInstructions() {
        return "Go to room " + room;
    }

}

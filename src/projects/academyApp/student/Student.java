package projects.academyApp.student;

import projects.academyApp.utils.FieldValidator;

public class Student {
    private final int id;
    private final String name;
    private final ContactDetails contactDetails;

    public Student(int id, String name, ContactDetails contactDetails) {
        this.id = FieldValidator.requirePositive(id, "StudentId");
        this.name = FieldValidator.requireNonBlank(name, "Student name");

        this.contactDetails = FieldValidator.requireNonNull(contactDetails, "Contact details");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactDetails=" + contactDetails +
                '}';
    }
}

package projects.academyApp;

import projects.academyApp.student.ContactDetails;
import projects.academyApp.student.Student;
import projects.academyApp.workshop.ClassroomWorkshop;
import projects.academyApp.workshop.OnlineWorkshop;
import projects.academyApp.workshop.Workshop;

public class Main {
    public static void main(String[] args) {
        Academy academy = new Academy();

        Student chitrakalpa = new Student(1, "Chitrakalpa", new ContactDetails("chitrakalpa@example.com", "Brussels"));
        Student paul = new Student(2, "Paul", new ContactDetails("paul@example.com", "Ghent"));
        Student gayatri = new Student(3, "Gayatri", new ContactDetails("gayatri@example.com", "Brussels"));
        Student oleh = new Student(4, "Oleh", new ContactDetails("oleh@example.com", "Antwerp"));

        Workshop javaBasics = new ClassroomWorkshop(10, "Java Basics", 2, "B12");
        Workshop junitPractice = new OnlineWorkshop(20, "JUnit Practice", 3, "https://example.com/junit");

//    Add students to the academy
        academy.addStudent(chitrakalpa);
        academy.addStudent(paul);
        academy.addStudent(gayatri);
        academy.addStudent(oleh);

//    Add workshops to the academy
        academy.addWorkshop(javaBasics);
        academy.addWorkshop(junitPractice);

//    Register students to a classroom workshop, register a student to a full workshop
        academy.register(chitrakalpa.getId(), javaBasics.getId());
        academy.register(paul.getId(), javaBasics.getId());
        try {
            academy.register(gayatri.getId(), javaBasics.getId());
        } catch (IllegalStateException e) {
            System.out.println("Registration refused: " + e.getMessage());
        }

//    Register a student after a cancellation
        academy.unregister(paul.getId(), javaBasics.getId());
        academy.register(gayatri.getId(), javaBasics.getId());

//    Register students to an online workshop
        academy.register(oleh.getId(), junitPractice.getId());
        academy.register(chitrakalpa.getId(), junitPractice.getId());

//    Print report
        for (String line : academy.buildReport()) {
            System.out.println(line);
        }

//     Available places
        System.out.println("Available places for " + javaBasics.getTitle() + ": " + javaBasics.availablePlaces());
        System.out.println("Available places for " + junitPractice.getTitle() + ": " + junitPractice.availablePlaces());

//        Count by city
        System.out.println("Student count by city for " + javaBasics.getTitle() + " workshop: " + academy.participantCountsByCity(javaBasics.getId()));
        System.out.println("Student count by city for " + junitPractice.getTitle() + " workshop: " + academy.participantCountsByCity(junitPractice.getId()));

//        All cities
        System.out.println("All cities in the academy: " + academy.allCities());
    }

}

package projects.academyApp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import projects.academyApp.student.ContactDetails;
import projects.academyApp.student.Student;
import projects.academyApp.workshop.ClassroomWorkshop;
import projects.academyApp.workshop.OnlineWorkshop;
import projects.academyApp.workshop.Workshop;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class AcademyTest {
    private Academy academy;
    private Student student1;
    private Student student2;
    private Workshop workshop1;
    private Workshop workshop2;

    @BeforeEach
    void setUp() {
         academy = new Academy();
         student1 = new Student(1, "Lou", new ContactDetails("lou@test.com", "Brussels"));
         student2 = new Student(2, "Tom", new ContactDetails("tom@test.com", "Brussels"));
         workshop1 = new ClassroomWorkshop(1, "Java Basics", 3, "B12");
         workshop2 = new OnlineWorkshop(2, "Java Advanced", 3, "https://link.com");
    }

    @Test
    void addStudentAlreadyAddedThrowsException() {
        academy.addStudent(student1);
        assertThrows(IllegalArgumentException.class, () -> academy.addStudent(student1));
    }

    @Test
    void addWorkshopAlreadyAddedThrowsException() {
        academy.addWorkshop(workshop1);
        assertThrows(IllegalArgumentException.class, () -> academy.addWorkshop(workshop1));
    }

//    findStudent

    @Test
    void findStudentWithUnknownIdReturnEmptyOptional() {
        assertEquals(Optional.empty(), academy.findStudent(4));
    }

    @Test
    void findValidStudentReturnsStudent() {
        academy.addStudent(student1);
        assertEquals(Optional.of(student1), academy.findStudent(1));
    }

//    (un)register

    @Test
    void registerWithUnknownStudentIdThrowsException() {
        academy.addWorkshop(workshop1);
        assertThrows(IllegalArgumentException.class, () -> academy.register(7, 1));
    }

    @Test
    void registerWithUnknownWorkshopIdThrowsException() {
        academy.addStudent(student1);
        assertThrows(IllegalArgumentException.class, ()-> academy.register(1, 7));
    }

    @Test
    void registerWithValidIdsAddsStudentToWorkshopsParticipants() {
        academy.addStudent(student1);
        academy.addWorkshop(workshop1);
        academy.register(1, 1);

        assertEquals(List.of(student1), workshop1.getParticipants());
    }

    @Test
    void unregisteredAStudentReturnsTrue() {
        academy.addStudent(student1);
        academy.addWorkshop(workshop1);
        academy.register(1, 1);
        assertTrue(academy.unregister(1, 1));
    }

//    searchStudents

    @Test
    void searchStudentCaseInsensitiveReturnsStudents() {
        Student student3 = new Student(3, "Chitrakalpa", new ContactDetails("Chitrakalpa@test.com", "Brussels"));
        academy.addStudent(student3);
        assertEquals("Chitrakalpa", academy.searchStudents("AlP").get(0).getName());
    }

    @Test
    void searchStudentByNameWithoutResultReturnsEmpty() {
        academy.addStudent(student2);
        assertEquals(List.of(), academy.searchStudents("ream"));
    }

    @Test
    void searchStudentByNameWithBlankReturnsOrderedStudents() {
        Student student3 = new Student(3, "Chitrakalpa", new ContactDetails("Chitrakalpa@test.com", "Brussels"));
        academy.addStudent(student1);
        academy.addStudent(student2);
        academy.addStudent(student3);

        assertEquals(List.of(student3, student1, student2), academy.searchStudents("   "));
    }

    @Test
    void searchByNameReturnsSeveralStudentsWithTheSameNameSortedByIds() {
        Student student3 = new Student(3, "Tom", new ContactDetails("tomtom@test.com", "Namur"));
        academy.addStudent(student2);
        academy.addStudent(student3);

        assertEquals(List.of(student2, student3), academy.searchStudents("tom"));
    }

    @Test
    void searchStudentsWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> academy.searchStudents(null));
    }

//    allCities
    @Test
    void allCitiesReturnsUniqueOrderedCities() {
        Student student3 = new Student(3, "Sarah", new ContactDetails("sarah@test.com", "Namur"));
        academy.addStudent(student1);
        academy.addStudent(student2);
        academy.addStudent(student3);
        assertIterableEquals(List.of("Brussels", "Namur"), academy.allCities());
    }

//    participantCountsByCity
    @Test
    void countByCityReturnsCorrectKeysAndValues() {
        Student student3 = new Student(3, "Sarah", new ContactDetails("sarah@test.com", "Namur"));
        academy.addStudent(student1);
        academy.addStudent(student2);
        academy.addStudent(student3);
        academy.addWorkshop(workshop1);
        academy.register(1, 1);
        academy.register(2, 1);
        academy.register(3, 1);

        TreeMap<String, Long> result = academy.participantCountsByCity(1);

        assertIterableEquals(List.of("Brussels", "Namur"), result.keySet());
        assertEquals(2L, result.get("Brussels"));
        assertEquals(1L, result.get("Namur"));
    }

    @Test
    void countParticipantsByCityForUnknowWorkshopThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> academy.participantCountsByCity(1));
    }

//    participantNames

    @Test
    void participantNameReturnsNamesAlphabeticallySorted() {
        academy.addStudent(student1);
        academy.addStudent(student2);
        academy.addWorkshop(workshop1);
        academy.register(2,1);
        academy.register(1,1);


        assertEquals(List.of("Lou", "Tom"), academy.participantNames(1));
    }

//    buildReport

    @Test
    void buildReportForEmptyWorkshop() {
        academy.addWorkshop(workshop1);
        assertEquals(List.of("Java Basics | 0/3 participants", "Go to room B12"), academy.buildReport());
    }

//    emptyAcademy
    @Test
    void searchStudentsInEmptyAcademyReturnsAnEmptyCollection() {
        assertEquals(List.of(), academy.searchStudents(""));
    }

    @Test
    void allCitiesInEmptyAcademyReturnsAnEmptyCollection() {
        assertIterableEquals(List.of(), academy.allCities());
    }
}

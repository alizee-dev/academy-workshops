package projects.academyApp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import projects.academyApp.student.ContactDetails;
import projects.academyApp.student.Student;
import projects.academyApp.workshop.ClassroomWorkshop;
import projects.academyApp.workshop.OnlineWorkshop;
import projects.academyApp.workshop.Workshop;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorkshopTest {
    private Student paul;
    private Workshop workshop;

    @BeforeEach
    void setUp() {
        paul = new Student(1, "Paul", new ContactDetails("paul@test.com", "Brussels"));
        workshop = new ClassroomWorkshop(1, "Java Basics", 2, "B12");
    }

    @Test
    void constructorStoresTrimmedValues() {
        Workshop w = new ClassroomWorkshop(10, "   Java Basics   ", 2, "B12");

        assertEquals(10, w.getId());
        assertEquals("Java Basics", w.getTitle());
        assertEquals(2, w.getCapacity());
    }

    @Test
    void capacityZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new ClassroomWorkshop(10, "Java Basics", 0, "B12"));
    }

    @Test
    void negativeIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new ClassroomWorkshop(-3, "Java Basics", 12, "B12"));
    }

    @Test
    void emptyTitleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new ClassroomWorkshop(10, "    ", 3, "B12"));
    }

    @Test
    void emptyRoomThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new ClassroomWorkshop(10, "Java Basics", 2, ""));
    }

    @Test
    void registerParticipantAdd1ToList(){
        workshop.register(paul);

        assertEquals(1, workshop.getParticipants().size());
        assertEquals(paul.getId(), workshop.getParticipants().get(0).getId());
    }

    @Test
    void registerTwoTimesThrowsException() {
        workshop.register(paul);
        assertThrows(IllegalStateException.class, () -> workshop.register(paul));
        assertEquals(1, workshop.getParticipants().size());
    }

    @Test
    void lastPlaceAccepted() {
        Student lou = new Student(2,"Lou", new ContactDetails("lou@test.com", "Brussels"));
        workshop.register(paul);
        workshop.register(lou);

        assertEquals(0, workshop.availablePlaces());
    }

    @Test
    void registerOnFullWorkShopThrowsException () {
        Student lou = new Student(2,"Lou", new ContactDetails("lou@test.com", "Brussels"));
        Student tom = new Student(3,"Tom", new ContactDetails("tom@test.com", "Brussels"));
        workshop.register(paul);
        workshop.register(lou);

        assertThrows(IllegalStateException.class, ()-> workshop.register(tom));
        assertEquals(List.of(paul, lou), workshop.getParticipants());
    }

    @Test
    void registerANullStudent() {
        assertThrows(IllegalArgumentException.class, () -> workshop.register(null));
        assertEquals(0, workshop.getParticipants().size());
    }

    @Test
    void unregisterStudentInTheMiddleOfParticipants() {
        Student lou = new Student(2,"Lou", new ContactDetails("lou@test.com", "Brussels"));
        Student tom = new Student(3,"Tom", new ContactDetails("tom@test.com", "Brussels"));
        Workshop workshop2 = new ClassroomWorkshop(2, "Java intermediaire", 3, "A15");
        workshop2.register(paul);
        workshop2.register(lou);
        workshop2.register(tom);

        assertTrue(workshop2.unregister(lou.getId()));
        assertEquals(List.of(paul, tom), workshop2.getParticipants());
    }

    @Test
    void unregisterANonRegisteredStudentReturnFalse() {
        workshop.register(paul);
        Student lou = new Student(2, "Lou", new ContactDetails("lou@test.com", "Brussels"));

        assertFalse(workshop.unregister(lou.getId()));
        assertEquals(1, workshop.getParticipants().size());
    }

    @Test
    void unregisteredAStudentFreesUpASpot() {
        Student lou = new Student(2, "Lou", new ContactDetails("lou@test.com", "Brussels"));
        workshop.register(paul);
        workshop.register(lou);
        workshop.unregister(lou.getId());

        assertEquals(1, workshop.availablePlaces());
    }

    @Test
    void registerAnUnregisteredStudent() {
        workshop.register(paul);
        workshop.unregister(paul.getId());
        workshop.register(paul);

        assertEquals(1, workshop.getParticipants().get(0).getId());
    }

    @Test
    void joiningInstructionIsValidForEachChild() {
        Workshop onlineWorkshop = new OnlineWorkshop(1, "Java advanced", 5, "https://www.teams.com");
        Workshop classroomWorkshop = new ClassroomWorkshop(2, "Java intermediate", 5, "C5");

        assertEquals("Join the course via: https://www.teams.com", onlineWorkshop.joiningInstructions());
        assertEquals("Go to room C5", classroomWorkshop.joiningInstructions());
    }

    @Test
    void getParticipantsPreventsNewRegistrationWithAnException() {
        workshop.register(paul);
        Student lou = new Student(2, "Lou", new ContactDetails("lou@test.com", "Brussels"));
        List<Student> participants = workshop.getParticipants();
        assertThrows(UnsupportedOperationException.class, () -> participants.add(lou));
        assertEquals(1, workshop.getParticipants().size());
    }
}

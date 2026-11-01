package projects.academyApp.workshop;

import projects.academyApp.student.Student;
import projects.academyApp.utils.FieldValidator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Workshop {
    private final int id;
    private final String title;
    private final int capacity;
    private final List<Student> participants = new ArrayList<>();
    //    Son contenu peut changer mais la variable pointe toujours vers la même liste :

    protected Workshop(int id, String title, int capacity) {
        this.id = FieldValidator.requirePositive(id, "Workshop id");
        this.title = FieldValidator.requireNonBlank(title, "Title");
        this.capacity = FieldValidator.requirePositive(capacity, "Capacity");
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCapacity() {
        return capacity;
    }

    //    Renvoie une copie et non pas l'originale pour protéger la vraie liste dans workshop. Avec l'originale, on pourrait ajouter/supprimer des participants sans passer par register
    public List<Student> getParticipants() {
        return List.copyOf(participants);
    }

    public int availablePlaces() {
        return capacity - participants.size();
    }

    public void register(Student student) {
        FieldValidator.requireNonNull(student, "Student");
        for(Student participant : participants) {
            if(participant.getId() == student.getId()) {
                throw new IllegalStateException("This student is already enrolled to this workshop.");
            }
        }
        if(availablePlaces() == 0) {
            throw new IllegalStateException("This workshop is already full.");
        }

        participants.add(student);
    }

    public boolean unregister (int studentId) {
        Iterator<Student> iterator = participants.iterator();
        while(iterator.hasNext()) {
            Student student = iterator.next();
            if(student.getId() == studentId) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public abstract String joiningInstructions();
}

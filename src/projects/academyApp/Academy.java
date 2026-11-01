package projects.academyApp;

import projects.academyApp.student.Student;
import projects.academyApp.utils.FieldValidator;
import projects.academyApp.workshop.Workshop;

import java.util.*;
import java.util.stream.Collectors;

public class Academy {
    private final Map<Integer, Student> students = new HashMap<>();
    private final Map<Integer, Workshop> workshops = new LinkedHashMap<>();

    public void addStudent(Student student) {
        FieldValidator.requireNonNull(student, "Student");
        if(students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student with " + student.getId() + " already exists.");
        }
        students.put(student.getId(), student);
    }

    public void addWorkshop(Workshop workshop) {
        FieldValidator.requireNonNull(workshop, "Workshop");
        if(workshops.containsKey(workshop.getId())) {
            throw new IllegalArgumentException("Workshop " + workshop.getId() + " already exists.");
        }
        workshops.put(workshop.getId(), workshop);
    }

    public Optional<Student> findStudent(int studentId) {
        return Optional.ofNullable(students.get(studentId));
    }

    public Optional<Workshop> findWorkshop(int workshopId) {
        return Optional.ofNullable(workshops.get(workshopId));
    }

    public List<Student> searchStudents(String text) {
        FieldValidator.requireNonNull(text, "Text");
        String query = text.trim().toLowerCase(Locale.ROOT);

        if(text.isBlank()) {
            return students.values().stream()
                    .sorted(Comparator.comparing(Student::getName)
                            .thenComparing(Student::getId))
                    .toList();
        } else {
            return students.values().stream()
                    .filter(s -> s.getName().toLowerCase(Locale.ROOT).contains(query))
                    .sorted(Comparator.comparing(Student::getName)
                            .thenComparing(Student::getId))
                    .toList();
        }
    }

    public List<String> participantNames(int workshopId) {
        Workshop workshop = findWorkshop(workshopId)
                .orElseThrow(()-> new IllegalArgumentException("Unknown wordkshop " +workshopId));
        return workshop.getParticipants().stream()
                .sorted(Comparator.comparing(Student::getName).thenComparing(Student::getId))
                .map(s -> s.getName())
                .toList();
    }

    public TreeMap<String, Long> participantCountsByCity(int workshopId) {
        findWorkshop(workshopId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown workshop " + workshopId));

        TreeMap<String, Long> countParticipantsByCity =  workshops.get(workshopId).getParticipants().stream()
                .collect(Collectors.groupingBy(s -> s.getContactDetails().getCity(), TreeMap::new, Collectors.counting()));
//         par quoi regrouper / dans quel type de map ranger / on calcule quoi pour chaque groupe
        return countParticipantsByCity;
    }

    public TreeSet<String> allCities () {
        return students.values().stream()
                .map(s -> s.getContactDetails().getCity())
//                .sorted()
//                sorted() est inutile car TreeSet sort déja les cities par ordre alpha
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public List<String> buildReport() {
        List<String> lines = new ArrayList<>();

        for (Workshop workshop : workshops.values()) {
            if(!lines.isEmpty()) {
                lines.add("");
            }

            String workshopTitle = workshop.getTitle();
            int numberOfParticipants = workshop.getParticipants().size();
            int capacity = workshop.getCapacity();
            String instructions = workshop.joiningInstructions();

            lines.add(workshopTitle + " | " + numberOfParticipants + "/" + capacity + " participants");
            lines.add(instructions);

            for (String name : participantNames(workshop.getId())) {
                lines.add("- " + name);
            }
        }
        return lines;
    }

    public void register(int studentId, int workshopId) {
        Student student = findStudent(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown student " + studentId));
        Workshop workshop = findWorkshop(workshopId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown workshop " + workshopId));

        workshop.register(student);
    }

    public boolean unregister(int studentId, int workshopId) {
        findStudent(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown student " + studentId));
        Workshop workshop = findWorkshop(workshopId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown workshop " + workshopId));

        return workshop.unregister(studentId);
    }


}

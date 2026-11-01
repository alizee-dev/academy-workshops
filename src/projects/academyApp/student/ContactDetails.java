package projects.academyApp.student;

import projects.academyApp.utils.FieldValidator;

public class ContactDetails {
    private final String email;
    private final String city;

    public ContactDetails(String email, String city) {
        this.email = FieldValidator.requireNonBlank(email, "Email");
        this.city = FieldValidator.requireNonBlank(city, "City");
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }
}

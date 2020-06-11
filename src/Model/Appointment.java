package Model;

public class Appointment {
    private int appointmentId, cusotmerId, userId;
    private String title, description, location, contact, type, url, start, end;

    public Appointment() {}

    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, String start, String end) {
        this.appointmentId = appointmentId;
        this.cusotmerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
    }

    public Appointment(int appointmentId, String title, String type, String start, String end) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCusotmerId() {
        return cusotmerId;
    }

    public void setCusotmerId(int cusotmerId) {
        this.cusotmerId = cusotmerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}

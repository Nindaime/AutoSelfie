package app0.com.autoselfie.Model;

import java.util.List;

public class ScheduleDay {
    private String name;
    private List<Course> courses;

    public ScheduleDay(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public List<Course> getCourses() {
        return courses;
    }
}

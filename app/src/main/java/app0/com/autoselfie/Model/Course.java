package app0.com.autoselfie.Model;

import java.util.ArrayList;
import java.util.List;

public class Course implements Cloneable {
    private final String courseName;
    private final String courseCode;
    private String startTime, endTime;

    public String getStartTime() {
        return startTime;
    }

    public Course setStartTime(String startTime) {
        this.startTime = startTime;

        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public Course setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }


    private static List<Course> courses;


    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public Course(String courseName, String courseCode) {

        this.courseName = courseName;
        this.courseCode = courseCode;
        this.startTime = "N/A";
        this.endTime = "N/A";


    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private static void createCourses() {
        courses = new ArrayList<>();

        // EIE COURSES
        courses.add(new Course("Introduction to computer science", "EIE521"));
        courses.add(new Course("Introduction to computer science II", "EIE528"));
        courses.add(new Course("Introduction to Algorithms", "EIE524"));
        courses.add(new Course("Introduction to Algorithms II", "EIE520"));

        // ICE COURSES
        courses.add(new Course("Compiler Construction", "ICE520"));
        courses.add(new Course("Compiler Construction II", "ICE521"));
        courses.add(new Course("Database Administration II", "ICE523"));

        // USED TWICE
        courses.add(new Course("Mathematics of Finance", "ICE522"));


        //OTHER COURSES
        courses.add(new Course("Database Administration", "DLD221"));
        courses.add(new Course("Database Administration II", "EDS PRACTICAL"));
        courses.add(new Course("Database Administration III", "TMC521"));


        // WEDNESDAY COURSES


    }

    /**
     * Factory Method for creating courses on app initialization
     *
//     * @param null None needed
     * @return List<Course> The valid list of courses for this application.
     */
    public static List<Course> getCourses() {
        createCourses();
        return courses;
    }
}

package app0.com.autoselfie.Model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseName, courseCode;
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

    public Course (String courseName, String courseCode){

        this.courseName = courseName;
        this.courseCode = courseCode;
        this.startTime = "N/A";
        this.endTime = "N/A";

    }

    private static void createCourses(){
        courses = new ArrayList<>();

        courses.add( new Course("Introduction to computer science", "CSC 101"));
        courses.add( new Course("Introduction to computer science II", "CSC 102"));
        courses.add( new Course("Compiler Construction", "CSC 201"));
        courses.add( new Course("Compiler Construction II", "CSC 202"));
        courses.add( new Course("Database Administration", "CSC 203"));
        courses.add( new Course("Database Administration II", "CSC 204"));
        courses.add( new Course("Mathematics of Finance", "CSC 205"));
        courses.add( new Course("Mathematics of Finance II", "CSC 206"));
        courses.add( new Course("Introduction to Algorithms", "CSC 207"));
        courses.add( new Course("Introduction to Algorithms II", "CSC 208"));
    }

    /**
     * Factory Method for creating courses on app initialization
     * @param null
     * @return List<Course>
     *
     * */
    public static List<Course> getCourses() {
        createCourses();
        return courses;
    }
}

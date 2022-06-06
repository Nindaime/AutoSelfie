package app0.com.autoselfie.Model;

public class Course {
    private String courseId, courseCode;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public Course (String courseId, String courseCode){

        this.courseId = courseId;
        this.courseCode = courseCode;
        this.time = "N/A";
    }
}

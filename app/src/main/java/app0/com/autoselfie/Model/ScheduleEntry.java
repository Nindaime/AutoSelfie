package app0.com.autoselfie.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ScheduleEntry {
    private final String day;
    private final String courseCode;
    private final String startTime;
    private final String endTime;
    public int id;
    private String venue;
    private static final String EIE500LH = "EIE500LH";
    private static final String ICE500LH = "ICE500LH";
    private static final String CHAPEL = "CHAPEL";
    private static final String LABORATORY = "LABORATORY";
    private static final String MONDAY = "Monday";
    private static final String TUESDAY = "Tuesday";
    private static final String WEDNESDAY = "Wednesday";
    private static final String THURSDAY = "Thursday";
    private static final String FRIDAY = "Friday";


    private static List<ScheduleEntry> schedule;

    /**
     * //     * @param  String day, String courseCode, String startTime, String endTime
     */

    public ScheduleEntry(String day, String courseCode, String startTime, String endTime, String venue) {
        this.day = day;
        this.courseCode = courseCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
    }

    public String getDay() {
        return day;
    }


    public String getCourseCode() {
        return courseCode;
    }


    public String getStartTime() {
        return startTime;
    }


    public String getEndTime() {
        return endTime;
    }

    public String getTime() {

        return String.format("%s - %s", startTime, endTime);
    }

    public String getVenue() {
        return this.venue;
    }

    public int getId() {
        return id;
    }

    public ScheduleEntry setId(int id) {
        this.id = id;

        return this;
    }


    private static void setSchedule(List<Course> courses) throws CloneNotSupportedException {

        schedule = new ArrayList<>();


        // MONDAY SHIFT
        Course firstShift = courses.get(0).setStartTime("8:00 am").setEndTime("10:00 am");
        Course secondShift = courses.get(1).setStartTime("10:00 am").setEndTime("12:00 pm");
        Course thirdShift = courses.get(2).setStartTime("12:00 pm").setEndTime("2:00 pm");
        Course fourthShift = courses.get(3).setStartTime("3:00 pm").setEndTime("5:00 pm");

        // TUESDAY SHIFT
        Course fifthShift = courses.get(4).setStartTime("10:00 am").setEndTime("12:00 pm");
        Course sixthShift = courses.get(5).setStartTime("12:00 pm").setEndTime("2:00 pm");
        Course seventhShift = courses.get(6).setStartTime("3:00 pm").setEndTime("5:00 pm");

        //WEDNESDAY SHIFT
        Course eighthShift = ((Course) (seventhShift.clone())).setStartTime("9:00 am").setEndTime("10:00 am");
        Course ninthShift = courses.get(7).setStartTime("10:00 am").setEndTime("12:00 pm");
        Course tenthShift = courses.get(8).setStartTime("12:00 pm").setEndTime("2:00 pm");
        Course eleventhShift = courses.get(9).setStartTime("3:00 pm").setEndTime("5:00 pm");

        // FRIDAY SHIFT
        Course twelveShift = courses.get(10).setStartTime("8:00 am").setEndTime("9:00 am");


        schedule.add(new ScheduleEntry(MONDAY, firstShift.getCourseCode(), firstShift.getStartTime(), firstShift.getEndTime(), EIE500LH));
        schedule.add(new ScheduleEntry(MONDAY, secondShift.getCourseCode(), secondShift.getStartTime(), secondShift.getEndTime(), EIE500LH));
        schedule.add(new ScheduleEntry(MONDAY, thirdShift.getCourseCode(), thirdShift.getStartTime(), thirdShift.getEndTime(), ICE500LH));
        schedule.add(new ScheduleEntry(MONDAY, fourthShift.getCourseCode(), fourthShift.getStartTime(), fourthShift.getEndTime(), ICE500LH));

        schedule.add(new ScheduleEntry(TUESDAY, fifthShift.getCourseCode(), fifthShift.getStartTime(), fifthShift.getEndTime(), CHAPEL));
        schedule.add(new ScheduleEntry(TUESDAY, sixthShift.getCourseCode(), sixthShift.getStartTime(), sixthShift.getEndTime(), ICE500LH));
        schedule.add(new ScheduleEntry(TUESDAY, seventhShift.getCourseCode(), seventhShift.getStartTime(), seventhShift.getEndTime(), ICE500LH));


        schedule.add(new ScheduleEntry(WEDNESDAY, ninthShift.getCourseCode(), ninthShift.getStartTime(), ninthShift.getEndTime(), ICE500LH));
        schedule.add(new ScheduleEntry(WEDNESDAY, tenthShift.getCourseCode(), tenthShift.getStartTime(), tenthShift.getEndTime(), EIE500LH));
        schedule.add(new ScheduleEntry(WEDNESDAY, eleventhShift.getCourseCode(), eleventhShift.getStartTime(), eleventhShift.getEndTime(), EIE500LH));
        schedule.add(new ScheduleEntry(WEDNESDAY, twelveShift.getCourseCode(), twelveShift.getStartTime(), twelveShift.getEndTime(), LABORATORY));

        schedule.add(new ScheduleEntry(FRIDAY, eighthShift.getCourseCode(), eighthShift.getStartTime(), eighthShift.getEndTime(), CHAPEL));


    }

    /**
     * Factory Method For Creating Schedule on app initialization
     * <p>
     * //     * @param List<Course> the list of courses that will be expected.
     * For this app length should be 10. If least of intended
     * Courses is greater than 10. Please configure setSchedule appropriately
     *
     * @return List<ScheduleEntry> returned days with courses attached to them
     */
    public static List<ScheduleEntry> getSchedule(List<Course> courses) throws Exception {

        if (courses.size() < 10)
            throw new Exception("List of Courses for this setup must be at least 10");

        setSchedule(courses);
        return schedule;
    }
}

package app0.com.autoselfie.Model;

import java.util.ArrayList;
import java.util.List;

public class ScheduleEntry {
    private final String day;
    private final String courseCode;
    private final String startTime;
    private final String endTime;
    public int id;




    private static List<ScheduleEntry> schedule;

    /**
//     * @param  String day, String courseCode, String startTime, String endTime
     */

    public ScheduleEntry(String day, String courseCode, String startTime, String endTime) {
        this.day = day;
        this.courseCode = courseCode;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public int getId() {
        return id;
    }

    public ScheduleEntry setId(int id) {
        this.id = id;

        return this;
    }


    private static void setSchedule(List<Course> courses) throws CloneNotSupportedException {

        schedule = new ArrayList<>();

        String MONDAY = "monday";
        String TUESDAY = "tuesday";
        String WEDNESDAY = "wednesday";
        String THURSDAY = "thursday";
        String FRIDAY = "friday";


        Course firstShift = courses.get(0).setStartTime("8:00 am").setEndTime("9:00 am");
        Course secondShift = ((Course) (firstShift.clone())).setStartTime("3:00 pm").setEndTime("4:00 pm");
        Course thirdShift = courses.get(1).setStartTime("10:00 am").setEndTime("11:00 am");
        Course fourthShift = ((Course) (thirdShift.clone())).setStartTime("4:00 pm").setEndTime("5:00 pm");
        Course fifthShift = courses.get(2).setStartTime("8:00 am").setEndTime("9:00 am");
        Course sixthShift = ((Course) (fifthShift.clone())).setStartTime("3:00 pm").setEndTime("4:00 pm");
        Course seventhShift = courses.get(3).setStartTime("10:00 am").setEndTime("11:00 am");
        Course eighthShift = ((Course) (seventhShift.clone())).setStartTime("4:00 pm").setEndTime("5:00 pm");
        Course ninthShift = courses.get(4).setStartTime("8:00 am").setEndTime("9:00 am");
        Course tenthShift = ((Course) (ninthShift.clone())).setStartTime("3:00 pm").setEndTime("4:00 pm");
        Course eleventhShift = courses.get(5).setStartTime("10:00 am").setEndTime("11:00 am");
        Course twelveShift = ((Course) (eleventhShift.clone())).setStartTime("4:00 pm").setEndTime("5:00 pm");
        Course thirteenthShift = courses.get(6).setStartTime("8:00 am").setEndTime("9:00 am");
        Course fourteenthShift = ((Course) (thirteenthShift.clone())).setStartTime("3:00 pm").setEndTime("4:00 pm");
        Course fifteenthShift = courses.get(7).setStartTime("10:00 am").setEndTime("11:00 am");
        Course sixteenthShift = ((Course) (fifteenthShift.clone())).setStartTime("4:00 pm").setEndTime("5:00 pm");
        Course seventeenthShift = courses.get(8).setStartTime("8:00 am").setEndTime("9:00 am");
        Course eighteenthShift = ((Course) (seventeenthShift.clone())).setStartTime("3:00 pm").setEndTime("4:00 pm");
        Course nineteenthShift = courses.get(9).setStartTime("10:00 am").setEndTime("11:00 am");
        Course twentiethShift = ((Course) (nineteenthShift.clone())).setStartTime("4:00 pm").setEndTime("5:00 pm");


        schedule.add(new ScheduleEntry(MONDAY, firstShift.getCourseCode(), firstShift.getStartTime(), firstShift.getEndTime()));
        schedule.add(new ScheduleEntry(MONDAY, secondShift.getCourseCode(), secondShift.getStartTime(), secondShift.getEndTime()));
        schedule.add(new ScheduleEntry(MONDAY, thirdShift.getCourseCode(), thirdShift.getStartTime(), thirdShift.getEndTime()));
        schedule.add(new ScheduleEntry(MONDAY, fourthShift.getCourseCode(), fourthShift.getStartTime(), fourthShift.getEndTime()));
        schedule.add(new ScheduleEntry(TUESDAY, fifthShift.getCourseCode(), fifthShift.getStartTime(), fifthShift.getEndTime()));
        schedule.add(new ScheduleEntry(TUESDAY, sixthShift.getCourseCode(), sixthShift.getStartTime(), sixthShift.getEndTime()));
        schedule.add(new ScheduleEntry(TUESDAY, seventhShift.getCourseCode(), seventhShift.getStartTime(), seventhShift.getEndTime()));
        schedule.add(new ScheduleEntry(TUESDAY, eighthShift.getCourseCode(), eighthShift.getStartTime(), eighthShift.getEndTime()));
        schedule.add(new ScheduleEntry(WEDNESDAY, ninthShift.getCourseCode(), ninthShift.getStartTime(), ninthShift.getEndTime()));
        schedule.add(new ScheduleEntry(WEDNESDAY, tenthShift.getCourseCode(), tenthShift.getStartTime(), tenthShift.getEndTime()));
        schedule.add(new ScheduleEntry(WEDNESDAY, eleventhShift.getCourseCode(), eleventhShift.getStartTime(), eleventhShift.getEndTime()));
        schedule.add(new ScheduleEntry(WEDNESDAY, twelveShift.getCourseCode(), twelveShift.getStartTime(), twelveShift.getEndTime()));
        schedule.add(new ScheduleEntry(THURSDAY, thirteenthShift.getCourseCode(), thirteenthShift.getStartTime(), thirteenthShift.getEndTime()));
        schedule.add(new ScheduleEntry(THURSDAY, fourteenthShift.getCourseCode(), fourteenthShift.getStartTime(), fourteenthShift.getEndTime()));
        schedule.add(new ScheduleEntry(THURSDAY, fifteenthShift.getCourseCode(), fifteenthShift.getStartTime(), fifteenthShift.getEndTime()));
        schedule.add(new ScheduleEntry(THURSDAY, sixteenthShift.getCourseCode(), sixteenthShift.getStartTime(), sixteenthShift.getEndTime()));
        schedule.add(new ScheduleEntry(FRIDAY, seventeenthShift.getCourseCode(), seventeenthShift.getStartTime(), seventeenthShift.getEndTime()));
        schedule.add(new ScheduleEntry(FRIDAY, eighteenthShift.getCourseCode(), eighteenthShift.getStartTime(), eighteenthShift.getEndTime()));
        schedule.add(new ScheduleEntry(FRIDAY, nineteenthShift.getCourseCode(), nineteenthShift.getStartTime(), nineteenthShift.getEndTime()));
        schedule.add(new ScheduleEntry(FRIDAY, twentiethShift.getCourseCode(), twentiethShift.getStartTime(), twentiethShift.getEndTime()));


    }

    /**
     * Factory Method For Creating Schedule on app initialization
     *
//     * @param List<Course> the list of courses that will be expected.
     *                     For this app length should be 10. If least of intended
     *                     Courses is greater than 10. Please configure setSchedule appropriately
     * @return List<ScheduleEntry> returned days with courses attached to them
     */
    public static List<ScheduleEntry> getSchedule(List<Course> courses) throws Exception {

        if (courses.size() < 10)
            throw new Exception("List of Courses for this setup must be at least 10");

        setSchedule(courses);
        return schedule;
    }
}

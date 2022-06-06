package app0.com.autoselfie.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleDay {
    private String name;
    private List<Course> courses;
    private static Map<String, List<Course>>  schedule;

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

    private static void setSchedule(List<Course> courses){

        schedule = new HashMap<>();

        Course c = courses.get(0);

        ArrayList<Course> coursesForMonday = new ArrayList<>();
        ArrayList<Course> coursesForTuesday = new ArrayList<>();
        ArrayList<Course> coursesForWednesday = new ArrayList<>();
        ArrayList<Course> coursesForThursday = new ArrayList<>();
        ArrayList<Course> coursesForFriday = new ArrayList<>();

        coursesForMonday.add( courses.get(0).setStartTime("8:00 am").setEndTime("9:00 am"));
        coursesForMonday.add( courses.get(0).setStartTime("3:00 pm").setEndTime("4:00 pm"));
        coursesForMonday.add( courses.get(1).setStartTime("10:00 am").setEndTime("11:00 am"));
        coursesForMonday.add( courses.get(1).setStartTime("4:00 pm").setEndTime("5:00 pm"));

        coursesForTuesday.add( courses.get(2).setStartTime("8:00 am").setEndTime("9:00 am"));
        coursesForTuesday.add( courses.get(2).setStartTime("3:00 pm").setEndTime("4:00 pm"));
        coursesForTuesday.add( courses.get(3).setStartTime("10:00 am").setEndTime("11:00 am"));
        coursesForTuesday.add( courses.get(3).setStartTime("4:00 pm").setEndTime("5:00 pm"));

        coursesForWednesday.add( courses.get(4).setStartTime("8:00 am").setEndTime("9:00 am"));
        coursesForWednesday.add( courses.get(4).setStartTime("3:00 pm").setEndTime("4:00 pm"));
        coursesForWednesday.add( courses.get(5).setStartTime("10:00 am").setEndTime("11:00 am"));
        coursesForWednesday.add( courses.get(5).setStartTime("4:00 pm").setEndTime("5:00 pm"));

        coursesForThursday.add( courses.get(6).setStartTime("8:00 am").setEndTime("9:00 am"));
        coursesForThursday.add( courses.get(6).setStartTime("3:00 pm").setEndTime("4:00 pm"));
        coursesForThursday.add( courses.get(7).setStartTime("10:00 am").setEndTime("11:00 am"));
        coursesForThursday.add( courses.get(7).setStartTime("4:00 pm").setEndTime("5:00 pm"));

        coursesForFriday.add( courses.get(8).setStartTime("8:00 am").setEndTime("9:00 am"));
        coursesForFriday.add( courses.get(8).setStartTime("3:00 pm").setEndTime("4:00 pm"));
        coursesForFriday.add( courses.get(9).setStartTime("10:00 am").setEndTime("11:00 am"));
        coursesForFriday.add( courses.get(9).setStartTime("4:00 pm").setEndTime("5:00 pm"));

        schedule.put("monday", coursesForMonday);
        schedule.put("tuesday", coursesForTuesday);
        schedule.put("wednesday", coursesForWednesday);
        schedule.put("thursday", coursesForThursday);
        schedule.put("friday", coursesForFriday);

    }
    /**
     * Factory Method For Creating Schedule on app initialization
     *
     * @param List<Course>
     * @return Map<String, List<Course>>
     *
     * */
    public Map<String, List<Course>> getSchedule(List<Course> courses){
        setSchedule(courses);
        return schedule;
    }
}

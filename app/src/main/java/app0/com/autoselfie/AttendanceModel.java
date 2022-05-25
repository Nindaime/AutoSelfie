package app0.com.autoselfie;

public class AttendanceModel {

    UserModel userModel;
    String time;

    public AttendanceModel(UserModel userModel,String time){
        this.userModel = userModel;
        this.time = time;
    }

    public String getNameOfUser(){
        return userModel.getFirstName()+" "+userModel.getLastName();
    }

    public String getTime(){
        return time;
    }
}

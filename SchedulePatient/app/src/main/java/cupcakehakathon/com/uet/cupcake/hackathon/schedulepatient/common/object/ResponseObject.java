package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object;

/**
 * Created by NgocThai on 12/03/2017.
 */

public class ResponseObject {

    private String appointmentTime;
    private String appointmentTimeEnd;
    private String doctorDesc;
    private String doctorName;
    private int limitOneDay;
    private String responseDesc;
    private String roomName;

    public ResponseObject() {
    }

    public String getAppointmentTimeEnd() {
        return appointmentTimeEnd;
    }

    public void setAppointmentTimeEnd(String appointmentTimeEnd) {
        this.appointmentTimeEnd = appointmentTimeEnd;
    }

    public ResponseObject(String appointmentTime, String appointmentTimeEnd, String doctorDesc, String doctorName, int limitOneDay, String responseDesc, String roomName) {
        this.appointmentTime = appointmentTime;
        this.appointmentTimeEnd = appointmentTimeEnd;
        this.doctorDesc = doctorDesc;
        this.doctorName = doctorName;
        this.limitOneDay = limitOneDay;
        this.responseDesc = responseDesc;
        this.roomName = roomName;
    }

    public String getAppointmentTime() {

        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getDoctorDesc() {
        return doctorDesc;
    }

    public void setDoctorDesc(String doctorDesc) {
        this.doctorDesc = doctorDesc;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getLimitOneDay() {
        return limitOneDay;
    }

    public void setLimitOneDay(int limitOneDay) {
        this.limitOneDay = limitOneDay;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}

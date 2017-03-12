package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object;

/**
 * Created by Dat UET on 3/12/2017.
 */
public class HistoryObject {

    private int id;
    private String symptom;
    private String requestTime;
    private String nameFaculty;
    private String dayTarget;
    private String appointmentTime;
    private String appointmentTimeEnd;
    private String doctorDesc;
    private String description;
    private String doctorName;
    private int idRequest;
    private String roomName;
    private String hospital;
    private int checked;

    public HistoryObject(int id, String symptom, String requestTime, String nameFaculty, String dayTarget, String appointmentTime, String appointmentTimeEnd, String doctorDesc, String description, String doctorName, int idRequest, String roomName) {
        this.id = id;
        this.symptom = symptom;
        this.requestTime = requestTime;
        this.nameFaculty = nameFaculty;
        this.dayTarget = dayTarget;
        this.appointmentTime = appointmentTime;
        this.appointmentTimeEnd = appointmentTimeEnd;
        this.doctorDesc = doctorDesc;
        this.description = description;
        this.doctorName = doctorName;
        this.idRequest = idRequest;
        this.roomName = roomName;
    }

    public HistoryObject(String symptom, String requestTime, String nameFaculty, String dayTarget, String appointmentTime, String appointmentTimeEnd, String doctorDesc, String description, String doctorName, int idRequest, String roomName) {

        this.symptom = symptom;
        this.requestTime = requestTime;
        this.nameFaculty = nameFaculty;
        this.dayTarget = dayTarget;
        this.appointmentTime = appointmentTime;
        this.appointmentTimeEnd = appointmentTimeEnd;
        this.doctorDesc = doctorDesc;
        this.description = description;
        this.doctorName = doctorName;
        this.idRequest = idRequest;
        this.roomName = roomName;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getNameFaculty() {
        return nameFaculty;
    }

    public void setNameFaculty(String nameFaculty) {
        this.nameFaculty = nameFaculty;
    }

    public String getDayTarget() {
        return dayTarget;
    }

    public void setDayTarget(String dayTarget) {
        this.dayTarget = dayTarget;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentTimeEnd() {
        return appointmentTimeEnd;
    }

    public void setAppointmentTimeEnd(String appointmentTimeEnd) {
        this.appointmentTimeEnd = appointmentTimeEnd;
    }

    public String getDoctorDesc() {
        return doctorDesc;
    }

    public void setDoctorDesc(String doctorDesc) {
        this.doctorDesc = doctorDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}

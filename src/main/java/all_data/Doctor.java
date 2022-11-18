package all_data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Doctor implements Serializable {
    private final String loginID;
    private final String nameOfDoctor;
    private ArrayList<GregorianCalendar> dateAppointments = new ArrayList<>();

    public void addDateAppointments(GregorianCalendar date) {
        this.dateAppointments.add(date);
    }

    public Doctor(String loginID, String nameOfDoctor) {
        this.loginID = loginID;
        this.nameOfDoctor = nameOfDoctor;
    }

    public String getLoginID() {
        return loginID;
    }

    public String getNameOfDoctor() {
        return nameOfDoctor;
    }

    public ArrayList<GregorianCalendar> getDateAppointments() {
        return dateAppointments;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "loginID='" + loginID + '\'' +
                ", nameOfDoctor='" + nameOfDoctor + '\'' +
                ", dateAppointments=" + dateAppointments +
                '}';
    }

}

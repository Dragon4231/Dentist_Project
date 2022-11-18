package all_data;

import all_data.Doctor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.util.ArrayList;

public interface RmiInterface extends Remote {
    ArrayList<Doctor> getDoctorsName() throws IOException, ClassNotFoundException;

    void newAppointment(Doctor doctor) throws IOException, ClassNotFoundException;
}

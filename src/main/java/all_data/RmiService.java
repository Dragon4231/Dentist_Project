package all_data;

import all_data.Doctor;
import all_data.RmiInterface;

import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class RmiService extends UnicastRemoteObject implements RmiInterface {

    public RmiService() throws RemoteException {

    }

    @Override
    public ArrayList<Doctor> getDoctorsName() throws IOException, ClassNotFoundException {
        return getStaticDoctors();
    }

    @Override
    public void newAppointment(Doctor doctor) throws IOException, ClassNotFoundException {
        ArrayList<Doctor> listDoctors = getStaticDoctors();
        for(int i = 0 ; i < listDoctors.size(); i++){
            if(listDoctors.get(i).getNameOfDoctor().equals(doctor.getNameOfDoctor())){
                listDoctors.get(i).addDateAppointments(doctor.getDateAppointments().get(0));
            }
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src\\main\\resources\\data\\doctors.data"));
        objectOutputStream.writeObject(listDoctors);
        objectOutputStream.close();
    }
     private ArrayList<Doctor> getStaticDoctors() throws IOException, ClassNotFoundException {
        ArrayList<Doctor> listDoctors;
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("src\\main\\resources\\data\\doctors.data"));
        listDoctors = (ArrayList<Doctor>) objectInputStream.readObject();
        return listDoctors;
    }

}

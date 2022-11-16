package server;

import all_data.Doctor;
import all_data.MessageSend;

import java.io.*;
import java.util.ArrayList;

public class MessageHandle {
    static ArrayList<Doctor> statListDoctors;

    public MessageSend handleMessage(MessageSend message) throws IOException, ClassNotFoundException {
        MessageSend result = null;
        if(message.getMessage().equals("getDoctorsName")){
            if(statListDoctors == null){
                ArrayList<Doctor> listDoctors = getDoctorsName();
                statListDoctors = listDoctors;
                result = new MessageSend("OK",listDoctors);
            }else{
                result = new MessageSend("OK",statListDoctors);
            }
        }else if(message.getMessage().equals("newAppointment")){
            Doctor onThePackage = (Doctor) message.getThePackage();
            System.out.println(onThePackage);
            for(int i = 0 ; i < statListDoctors.size(); i++){
                if(statListDoctors.get(i).getNameOfDoctor().equals(onThePackage.getNameOfDoctor())){
                    statListDoctors.get(i).addDateAppointments(onThePackage.getDateAppointments().get(0));
                }
            }
            System.out.println(statListDoctors);
            result = new MessageSend("OK/UPDATE",statListDoctors);
            saveToData();
        }
        return result;
    }

    private ArrayList<Doctor> getDoctorsName() throws IOException, ClassNotFoundException {
        ArrayList<Doctor> listDoctors;
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("src\\main\\resources\\data\\doctors.data"));
        listDoctors = (ArrayList<Doctor>) objectInputStream.readObject();
        return listDoctors;
    }

    private void saveToData() throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src\\main\\resources\\data\\doctors.data"));
        objectOutputStream.writeObject(statListDoctors);
    }
}

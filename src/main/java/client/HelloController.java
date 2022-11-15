package client;


import all_data.Doctor;
import all_data.MessageSend;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.stream.Collectors;


public class HelloController {
    public ListView listView;
    private boolean isConnected = false;
    private static Socket socket;
    private static final int PORT = 8080;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    @FXML
    public ComboBox<String> cmbbox1;
    @FXML
    public ProgressIndicator progrInd;
    public Label labelFail;
    public DatePicker dateChanger;

    public void clickForApply(MouseEvent mouseEvent) throws IOException, ClassNotFoundException {
        if (!isConnected || cmbbox1.getValue() == null || dateChanger.getValue() == null) return;

        Doctor newAppointment = new Doctor(cmbbox1.getValue(), cmbbox1.getValue());
        newAppointment.addDateAppointments(GregorianCalendar.from(dateChanger.getValue().atStartOfDay(ZoneId.systemDefault())));
        out.writeObject(new MessageSend("newAppointment", newAppointment));

       /* ObservableList<String> langs = FXCollections.observableArrayList(listView.getItems());
        langs.add(cmbbox1.getValue() + " | " + dateChanger.getValue());
        listView.setItems(langs);*/
    }

    public void clickForConnect(MouseEvent mouseEvent) throws IOException, InterruptedException {
        setConnecting();
    }

    void setConnecting() {
        progrInd.setVisible(true);
        progrInd.setProgress(0.0);
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (double l = 0.0; l < 1; l += 0.05) {
                            progrInd.setProgress(l);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    socket = new Socket("localhost", PORT);
                    out = new ObjectOutputStream(socket.getOutputStream());
                    in = new ObjectInputStream(socket.getInputStream());
                    //listenMessage();
                    progrInd.setProgress(1);
                    labelFail.setVisible(false);
                    checkForConnecting();
                    isConnected = true;
                } catch (Exception e) {
                    progrInd.setVisible(false);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            labelFail.setVisible(true);
                            labelFail.setText("Connect is failed");
                        }
                    });
                    labelFail.setVisible(true);
                }
                try {
                    getDoctors();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t2.start();
    }

    void checkForConnecting() {
        Thread checkConnecting = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (out != null || in != null) continue;
                        in.readObject();
                    } catch (Exception e) {
                        isConnected = false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                labelFail.setVisible(true);
                                labelFail.setText("Disconnecting...");
                            }
                        });
                        System.out.println("disconnecting");
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        checkConnecting.start();
    }

    void getDoctors() throws IOException, ClassNotFoundException {
        if (!isConnected) return;

        out.writeObject(new MessageSend("getDoctorsName", new ArrayList<>()));
        ArrayList<Doctor> nameOfDoctors;
        while (true) {
            Object result = in.readObject();
            if (result != null) {
                MessageSend m = (MessageSend) result;
                nameOfDoctors = (ArrayList<Doctor>) m.getThePackage();
                ObservableList<String> langs = FXCollections.observableArrayList(nameOfDoctors.stream().map(x -> x.getNameOfDoctor()).collect(Collectors.toList()));
                cmbbox1.setItems(langs);
                break;
            }
        }
    }

/*    void listenMessage() {
        Thread listen = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Object result = null;
                    System.out.println("l");
                    try {
                        result = in.readObject();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    if (result != null) {
                        System.out.println("nn");
                        MessageSend m = (MessageSend) result;
                        if (m.getMessage().equals("OK")) {
                            ArrayList<Doctor> nameOfDoctors;
                            nameOfDoctors = (ArrayList<Doctor>) m.getThePackage();
                            ObservableList<String> langs = FXCollections.observableArrayList(nameOfDoctors.stream().map(x -> x.getNameOfDoctor()).collect(Collectors.toList()));
                            cmbbox1.setItems(langs);
                        } else if (m.getMessage().equals("OK/UPDATE")) {
                            ArrayList<Doctor> nameOfDoctors;
                            nameOfDoctors = (ArrayList<Doctor>) m.getThePackage();
                            System.out.println(nameOfDoctors);
                            ObservableList<String> langs = FXCollections.observableArrayList(listView.getItems());
                            for (Doctor d : nameOfDoctors) {
                                if (d.getDateAppointments().isEmpty()) continue;
                                for (int i = 0; i < d.getDateAppointments().size(); i++) {
                                    langs.add(d.getNameOfDoctor() + " | " + d.getDateAppointments().get(i));
                                }
                            }
                            listView.setItems(langs);
                        }
                    }
                }
            }
        });
        listen.start();
    }*/

}
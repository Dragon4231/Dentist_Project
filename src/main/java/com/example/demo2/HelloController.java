package com.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class HelloController {

    @FXML
    public ProgressIndicator progrInd;
    public Label labelFail;
    private static Socket socket;
    private static BufferedReader input; // поток чтения из сокета
    private static BufferedWriter output; // поток записи в сокет

    @FXML
    public ComboBox<String> cmbbox1;

    @FXML
    private Button myBtn;

    @FXML
    private ProgressBar progresBar;

    @FXML
    private TextField textField1;

    @FXML
    public void myBtnClick() throws IOException {
        progresBar.setProgress(new Random().nextDouble());
    }

    public void clickForApply(MouseEvent mouseEvent) throws IOException {
        output.write("w");
        /*ObservableList<String> langs = FXCollections.observableArrayList("Java", "JavaScript", "C#", "Python");
        cmbbox1.setItems(langs);*/
    }

    synchronized public void clickForConnect(MouseEvent mouseEvent) throws IOException, InterruptedException {
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
                try (Socket newSocket = new Socket("localhost", 8080);
                     BufferedReader in = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
                     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(newSocket.getOutputStream()))) {
                    output = out;
                    input = in;
                    Thread.sleep(300);
                    /*out.write("d");
                    out.flush();*/
                    getNamesOfDoctors();
                    t1.join();
                    progrInd.setProgress(1);
                   /* ObservableList<String> langs = FXCollections.observableArrayList(getNamesOfDoctors());
                    cmbbox1.setItems(langs);*/
                    labelFail.setVisible(false);
                } catch (Exception e) {
                    progrInd.setVisible(false);
                    labelFail.setVisible(true);
                }
            }
        });
        t2.start();
    }

    synchronized private void getNamesOfDoctors() throws IOException {
        List<String> result = new ArrayList<>();
        output.write("getNamesOfDoctors");
        output.flush();
        Scanner scanner = new Scanner(input);
        while (true) {
            if (!scanner.hasNext()) break;
            result.add(scanner.next());
        }
        System.out.println(result.toString());
    }
}
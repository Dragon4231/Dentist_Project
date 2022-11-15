package server;


import all_data.Doctor;
import all_data.MessageSend;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main extends Thread {
    public static final int PORT = 8080;

    public static LinkedList<Client> clientsList = new LinkedList<>();

    public static void main(String[] args) throws IOException {

/*
     ArrayList<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("first", "Зубель А."));
        doctors.add(new Doctor("second", "Мироновский И."));
        doctors.add(new Doctor("third", "Ваниленко В."));
        doctors.add(new Doctor("fourth", "Шоколадов Ш."));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src\\main\\resources\\data\\doctors.data"));
        objectOutputStream.writeObject(doctors);
*/


        ServerSocket server = new ServerSocket(PORT);
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    System.out.println("new connecting");
                    clientsList.add(new Client(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

    static class Client extends Thread {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public Client(Socket socket) throws IOException {
            this.socket = socket;
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            start();
        }

        @Override
        public void run() {
            try {
                MessageSend message;
                while (true) {
                    message = (MessageSend) in.readObject();
                    if (message != null) {
                        System.out.println("new message");
                        MessageSend answer = new MessageHandle().handleMessage(message);
                        if (answer.getMessage().equals("OK/UPDATE")) {
                            sendForAll(answer);
                        } else {
                            out.writeObject(answer);
                        }
                    }
                }
            } catch (IOException e) {

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        private void sendForAll(MessageSend messageSend) throws IOException {
            System.out.println(messageSend.getMessage());
            System.out.println(messageSend.getThePackage());
            for(Client c : clientsList){
                c.out.writeObject(messageSend);
            }
        }
    }
}

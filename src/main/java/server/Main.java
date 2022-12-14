package server;

import all_data.MessageSend;
import all_data.RmiInterface;
import all_data.RmiService;

import java.io.*;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class Main extends Thread {
    public static final int PORT = 8080;

    public static LinkedList<Client> clientsList = new LinkedList<>();

    public static void main(String[] args) throws IOException, AlreadyBoundException {

/*
     ArrayList<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("first", "Зубель А."));
        doctors.add(new Doctor("second", "Мироновский И."));
        doctors.add(new Doctor("third", "Ваниленко В."));
        doctors.add(new Doctor("fourth", "Шоколадов Ш."));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src\\main\\resources\\data\\doctors.data"));
        objectOutputStream.writeObject(doctors);
*/

        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        RmiInterface rmiInterface = new RmiService();

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("clien", rmiInterface);

/*        ServerSocket server = new ServerSocket(PORT);
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
        }*/
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
                        out.reset();
                        out.writeObject(answer);
                    }
                }
            } catch (IOException e) {

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

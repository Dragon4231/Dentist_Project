package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Main extends Thread {
    public static final int PORT = 8080;
    public static LinkedList<ServerSomthing> serverList = new LinkedList<>(); // список всех нитей

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        try {
            while (true) {
                Socket socket = server.accept();
                System.out.println("new connecting" + socket.getInetAddress().toString());
                try {
                    serverList.add(new ServerSomthing(socket)); // добавить новое соединенние в список
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

    static class ServerSomthing extends Thread {

        private Socket socket;
        private BufferedReader in;
        private BufferedWriter out;

        public ServerSomthing(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            start();
        }

        @Override
        public void run() {
            String word;
            try {
                while (true) {
                    word = in.readLine();
                    if (word != null) System.out.println(word);
                    if (word != null && word.equals("getNamesOfDoctors")) {
                        out.write("Sema \nLexa");
                        out.flush();
                        System.out.println(word);
                    }
                }
            } catch (IOException e) {
            }
        }

        private void send(String msg) {
            try {
                out.write(msg + "\n");
                out.flush();
            } catch (IOException ignored) {
            }
        }
    }
}
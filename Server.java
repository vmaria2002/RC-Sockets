import java.io.*;
import java.net.ServerSocket;
import java.net.*;
import java.util.ArrayList;

public class Server {
    private static ArrayList<DataOutputStream> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        boolean listening = true;
        BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));

        try {
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        } catch (IOException e) {
            System.err.println("Eroare pe port" + Integer.parseInt(args[0]));
            System.exit(-1);
        }



        System.out.println("Server pornit");
        new KeyboardReader(keyboardInput).start();

        while (listening) {
            Socket clientSocket = serverSocket.accept();
            DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());
            //primul PC
            if(clientSocket.getLocalSocketAddress().toString().equals("/127.0.0.1:5555")) {
                clients.add(clientOutput);
                new Worker(clientSocket, clientOutput).start();
            }

        }

        serverSocket.close();
    }

    private static class Worker extends Thread {
        private Socket socket = null;
        private DataOutputStream output = null;

        public Worker(Socket socket, DataOutputStream output) {
            super("Worker");
            this.socket = socket;
            this.output = output;
            if(socket.getLocalSocketAddress().toString().equals("/127.0.0.3:5555")) {
                System.out.println("Client acceptat: " + socket.getLocalSocketAddress());
            }else {
                System.out.println("mu ecc");
            }
        }

        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line;

                while ((line = input.readLine()) != null) {
                    System.out.println("Primit de la client " + socket.getInetAddress() + ": " + line);

                    for (DataOutputStream clientOutput : clients) {
                        clientOutput.writeBytes(line + "\n");
                        clientOutput.flush();
                    }
                }

                socket.close();
                clients.remove(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class KeyboardReader extends Thread {
        private BufferedReader keyboardInput;

        public KeyboardReader(BufferedReader keyboardInput) {
            this.keyboardInput = keyboardInput;
        }
        public void run() {
            try {
                while (true) {
                    String line = keyboardInput.readLine();

                    for (DataOutputStream clientOutput : clients) {

                        clientOutput.writeBytes("[SERVER] " + line + "\n");
                        clientOutput.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

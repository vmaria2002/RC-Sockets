import java.io.*;
import java.net.*;

public class Client {
    public static void main(String args[]) {
        Socket socket = null;
        DataOutputStream out = null;
        BufferedReader in = null;
        BufferedReader c = null;
        c = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        String line2 = null;

        try {
            System.out.println("Client pornit");
            socket = new Socket(args[0], Integer.parseInt(args[1]));
            System.out.println("Socket info " + socket);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            // trimitem mesaje catre server si afisam raspunsul in consola clientului
            while (true) {
                // citim mesajul de la server si il afisam in consola clientului
                line2 = in.readLine();
                System.out.println("Mesaj primit de la server: " +socket.getLocalSocketAddress()+ line2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

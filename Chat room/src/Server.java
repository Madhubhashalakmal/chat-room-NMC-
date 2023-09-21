
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final ServerSocket Ss;

    public Server(ServerSocket Ss) {
        this.Ss = Ss;
    }

    public void startServer() {

        try {

            while (!Ss.isClosed()) {
                Socket socket = Ss.accept();
                System.out.println("New Client Connected to the chat!");
                CH CH = new CH(socket);

                Thread thread = new Thread(CH);
                thread.start();
            }
        } catch (IOException e) {

        }
    }

    public void closeServerSocket() {
        try {
            if (Ss != null) {
                Ss.close();
            }
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket Ss = new ServerSocket(10620);
        Server server = new Server(Ss);
        server.startServer();
    }
}

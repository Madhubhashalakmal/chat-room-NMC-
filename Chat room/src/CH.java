import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

 
public class CH implements Runnable {
    
    public static ArrayList<CH> CH = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUn;
    
    public CH(Socket socket){
        try{
            this.socket=socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUn = bufferedReader.readLine();
            CH.add(this);
            broadcastMessage("SERVER: " + clientUn + " has entered to the chat room !! ");            
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        
        while (socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    
    

    private void broadcastMessage(String messageToSend) {
        for (CH CH : CH){
            try{
                if (!CH.clientUn.equals(clientUn)){
                    CH.bufferedWriter.write(messageToSend);
                    CH.bufferedWriter.newLine();
                    CH.bufferedWriter.flush();
                }                
            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }    

    public void removeClientHandler(){
        CH.remove(this);
        broadcastMessage("SERVER: " + clientUn +" has left from the chat!!");
    }
    
    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
        }
    }
}
/*    private static class InputStreamWriter extends Reader {

        public InputStreamWriter(InputStream inputStream) {
        }

        @Override
        public int read(char[] chars, int i, int i1) throws IOException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void close() throws IOException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}*/

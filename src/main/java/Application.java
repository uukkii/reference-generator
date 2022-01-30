import Server.Server;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Application {

    public static ObjectMapper mapper = new ObjectMapper();


    public static void main(String[] args) throws IOException {

        Server server = new Server();
        server.start();

    }
}

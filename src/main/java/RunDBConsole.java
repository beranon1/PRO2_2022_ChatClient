import org.apache.derby.tools.ij;

import java.io.IOException;

public class RunDBConsole {

    public static void main(String[] args) {
        try {
            // připojení do databáze
            // spustit tuto třídu a zkopírovat tento příkaz
            // connect 'jdbc:derby:ChatClientDb_skB;create=true';
            ij.main(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exceptions.TimeExceededException;
import it.polimi.ingsw.utils.ReaderHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ReaderHelperTest {

    ReaderHelper reader;

    @Before
    public void setup() {
        reader = new ReaderHelper();
    }
    /**
    @Test
    //Test that after 3 seconds the read stops
    public void readUntilStop() throws IOException, TimeExceededException {
        //The thread the stops the read
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reader.stop();
        }).start();
        String read = reader.readLineUntilStop();
        System.out.println("Ho letto: "+ read);
    }
    */

}

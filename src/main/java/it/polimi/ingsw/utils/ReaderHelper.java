package it.polimi.ingsw.utils;


import it.polimi.ingsw.exceptions.TimeExceededException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class that handle all the readings from console.
 */
public class ReaderHelper {

    /**
     * If isStopped is true, the reader stops the read
     */
    private boolean isStopped;
    private boolean isReading;

    /**
     * Constructor.
     */
    public ReaderHelper() {
        isStopped = false;
        isReading = false;
    }

    /**
     * Method that put in b the byte read from the stream and return the number of byte read. The read has a timeout.
     * @param is is input stream
     * @param b is the byte array read from the stream
     * @param timeoutMillis is the timeout
     * @return the number of byte read
     * @throws IOException if there are stream errors.
     */
    private int readInputStreamWithTimeout(InputStream is, byte[] b, int timeoutMillis) throws IOException {
        int bufferOffset = 0;
        long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length) {
            isReading = true;
            int readLength = java.lang.Math.min(is.available(),b.length-bufferOffset);
            // can alternatively use bufferedReader, guarded by isReady():
            int readResult = is.read(b, bufferOffset, readLength);
            if (readResult == -1) break;
            bufferOffset += readResult;
        }
        isReading = false;
        return bufferOffset;
    }

    /**
     * Method that put in b the byte read from the stream and return the number of byte read.
     * The read stops after timeout or when the user press enter
     * @param is is input stream
     * @param b is the byte array read from the stream
     * @param timeoutMillis is the timeout
     * @return the number of byte read
     * @throws IOException if there are stream errors.
     */
    private int readInputStreamWithTimeoutAndEnter(InputStream is, byte[] b, int timeoutMillis) throws IOException {
        int bufferOffset = 0;
        long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
        boolean enter = true;
        while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length && enter) {
            isReading = true;
            int readLength = java.lang.Math.min(is.available(),b.length-bufferOffset);
            // can alternatively use bufferedReader, guarded by isReady():
            int readResult = is.read(b, bufferOffset, readLength);
            if (readResult == -1) break;
            bufferOffset += readResult;
            String read = new String(b);
            if(read.contains("\n"))
                enter = false;
        }
        isReading = false;
        return bufferOffset;
    }

    /**
     * Method that put in b the byte read from the stream and return the number of byte read.
     * The read stops after timeout or when the user press enter or when isStopped is true
     * @param is is input stream
     * @param b is the byte array read from the stream
     * @return the number of byte read
     * @throws IOException if there are stream errors.
     */
    private int stoppableRead(InputStream is, byte[] b) throws IOException, TimeExceededException {
        int bufferOffset = 0;
        isStopped = false;
        boolean enter = true;
        while (bufferOffset < b.length && enter && !isStopped) {
            isReading = true;
            int readLength = java.lang.Math.min(is.available(),b.length-bufferOffset);
            // can alternatively use bufferedReader, guarded by isReady():
            int readResult = is.read(b, bufferOffset, readLength);
            if (readResult == -1) break;
            bufferOffset += readResult;
            String read = new String(b);
            if(read.contains("\n"))
                enter = false;
        }
        if(isStopped){
            WriterHelper.printColored(ANSIColors.RED_BOLD, "STOPPATO");
            throw new TimeExceededException();
        }
        isReading = false;
        return bufferOffset;
    }

    /**
     * Reads a single line from console input stream.
     * @return the string read.
     * @throws IOException if there are stream issues.
     */
    public String readLine() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    /**
     * Read a single line from console input stream with a timeout.
     * @param timeoutMillis are the milliseconds of timeout.
     * @return the string read.
     * @throws IOException if there are
     */
    public String readLineWithTimeout(int timeoutMillis) throws IOException {
        byte[] inputData = new byte[1024];
        readInputStreamWithTimeoutAndEnter(System.in, inputData, timeoutMillis);
        return new String(inputData);
    }

    /**
     * Read single line from console until stopped attribute is false.
     * @return the string read.
     * @throws IOException if there are stream issues.
     * @throws TimeExceededException if read is stopped.
     */
    public String readLineUntilStop() throws IOException, TimeExceededException {
        byte[] inputData = new byte[1024];
        stoppableRead(System.in, inputData);
        return new String(inputData);
    }

    /**
     * Setter.
     * @return is stopped attri
     */
    public boolean isStopped() {
        return isStopped;
    }

    /**
     * Stops the read.
     */
    public void stop() {
        this.isStopped = true;
    }

    /**
     * Use readLineUntilStop method and convert the string read in a integer value.
     * @return 0 if the time is exceed or if there are cast errors, else returns the value.
     * @throws IOException if there are stream errors.
     * @throws TimeExceededException if read is stopped.
     */
    public int readUserChoice() throws IOException, TimeExceededException {
        String choiceStr = readLineUntilStop();
        choiceStr = choiceStr.trim();
        if(choiceStr.length() == 0)
            return 0;
        else{
            int choiceInt = 0;
            try{
                choiceInt = Integer.parseInt(choiceStr);
                return choiceInt;
            }catch (NumberFormatException ex) {
                return 0;
            }
        }
    }


}

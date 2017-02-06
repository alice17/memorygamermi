/**
 * Created by alice on 06/02/17.
 */

import static java.lang.Thread.sleep;

public class WaitThread implements Runnable {

    public void run(){
        int sec = 60000;

        try {
            System.out.println("Wait for " + sec + " ms");
            sleep(sec);
            System.out.println("Wait ended.");

            //notifyAll();
        }catch(Exception e){
            System.out.println("WaitThread err: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

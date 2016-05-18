/**
 * A class to represent a male or female person.
 *
 * @author Cormac Redmond -- credmond85 /at/ gmail
 */
public class Person implements Runnable {

    protected Bathroom bathroom;
    private boolean isMale;

    // Minimum time to idle
    private int minIdleTime = 100;

    // Maximum time to idle
    private int maxIdleTime = 250;

    // Minimum time to spend in a stall
    private int minInStallTime = 100;

    // Maximum time to spend in a stall
    private int maxInStallTime = 250;
    private long startWaitTime;

    public Person(Bathroom bathroom, boolean isMale) {
        this.bathroom = bathroom;
        this.isMale = isMale;
    }

    /**
     * Determines how long a person has been waiting in the queue.
     *
     * @return The length a person has been waiting.
     */
    public long getWaitingTime() {
        return System.currentTimeMillis() - startWaitTime;
    }

    /**
     * Returns gender of person.
     *
     * @return true indicates male, false indicates female.
     */
    public boolean isMale() {
        return isMale;
    }

    /**
     * The run method - called when thread starts.
     */
    public void run() {
        // Constantly loop
        // Wait random amount of time before queuing to use the bathroom
        try {
            Thread.sleep((int) (minIdleTime + (maxIdleTime - minIdleTime)
                    * Math.random()));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Queue up for the Bathroom.
        try {
            // Remember the time person started to queue.
            startWaitTime = System.currentTimeMillis();

            // Queue in the bathroom - it will sleep until woken up.
            bathroom.enqueuePerson(this);
        }
        catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        // Now 'bathroom' has woken person up, meaning they are in a stall.
        try {

            // Do something for a random amount of time to represent
            // 'using' the bathroom
            Thread.sleep((int) (minInStallTime + (maxInStallTime - minInStallTime)
                    * Math.random()));
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Finished using the bathroom, so inform it.
        bathroom.offerStall(this);
        // Loop to begin cycle again.
        while (true) {
            // Wait random amount of time before queuing to use the bathroom
            try {
                Thread.sleep((int) (minIdleTime + (maxIdleTime - minIdleTime)
                        * Math.random()));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Queue up for the Bathroom.
            try {
                // Remember the time person started to queue.
                startWaitTime = System.currentTimeMillis();
                // Queue in the bathroom - it will sleep until woken up.
                bathroom.enqueuePerson(this);
            }
            catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            // Now 'bathroom' has woken person up, meaning they are in a
            // stall.
            try {
                // Do something for a random amount of time to represent
                // 'using' the bathroom
                Thread.sleep((int) (minInStallTime + (maxInStallTime - minInStallTime)
                        * Math.random()));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Finished using the bathroom, so inform it.
            bathroom.offerStall(this);
            // Loop to begin cycle again.
        }
    }
}

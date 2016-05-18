import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class represents a bathroom.
 *
 * @author Cormac Redmond -- credmond85 /at/ gmail
 */
public class Bathroom {

    private LinkedList bathroomQueue = new LinkedList();
    private LinkedList stallOccupants = new LinkedList();

    private int noOfStalls = 5;

    // Used as measurements
    private int totalFemales = 0;
    private long femaleQueueTimeTotal = 0;
    private int totalMales = 0;
    private long maleQueueTimeTotal = 0;

    /*
      * The JFrame to update in order to show measurements. Not really a very OO
      * way of doings things (e.g., forcing unrelated classes to rely on each
      * other), but works fine for demonstration purposes.
      */
    private RunBathroom windowThread;

    public Bathroom(RunBathroom windowThread) {
        this.windowThread = windowThread;
    }

    /**
     * Enters a {@link Person} Person into the queue for the bathroom
     *
     * @param person The {@link Person} Person to enter into the queue
     * @throws InterruptedException
     */
    public void enqueuePerson(Person person) throws InterruptedException {
        Person nextInStall;
        // Add person to the queue and then check to see if they're next in
        // line.
        // Lock the object to prevent a context switch allowing someone else
        // enter the queue first
        synchronized (this) {
            bathroomQueue.add(person);
            nextInStall = offerStall(null);
        }
        // If not next in line, put person to sleep
        if (person != nextInStall) {
            synchronized (person) {
                person.wait();
            }
        }
    }

    /**
     * Method the check who is next to enter bathroom
     *
     * @param remove Person to remove from a stall (can be null)
     * @return The Person
     */
    public synchronized Person offerStall(Person remove) {

        // If given, remove the person from a stall
        if (remove != null) {
            removePerson(remove, stallOccupants);
        }
        Person in = null;
        // If no-one is queuing, or if the stalls are full, return null.
        if (bathroomQueue.size() == 0 || stallOccupants.size() >= noOfStalls) {
            in = null;
        } else {
            // Otherwise find next the next person to enter bathroom
            Person occupiersGender = (Person) stallOccupants.peek();
            Person nextPerson = (Person) bathroomQueue.peek();
            boolean maleInControl; // = false;

            // If someone is in the bathroom, find out which sex
            if (occupiersGender != null) {
                maleInControl = occupiersGender.isMale(); // stallOc
            }
            // If nobody is in bathroom, then give control to whoever is next in queue
            else {
                maleInControl = nextPerson.isMale();
            }
            // If someone exists in the queue
            if (nextPerson != null) {
                // If the next person is male, and males are in control of the
                // bathroom, or the bathroom is empty
                if (nextPerson.isMale()
                        && (maleInControl || stallOccupants.size() == 0)) {
                    in = nextPerson;
                }
                // Otherwise, if next person is female, and females are in control, or the bathroom is empty
                else if (!nextPerson.isMale()
                        && (!maleInControl || stallOccupants.size() == 0)) {
                    maleInControl = false;
                    in = nextPerson;
                }
                // Otherwise, the next person in the queue cannot yet enter.
                else {
                    in = null;
                }
            }
        }

        // If a person was chosen to enter bathroom
        if (in != null) {
            // Remove them from the queue, and update measurements/window
            if (in.isMale()) {
                removePerson(in, bathroomQueue);
                maleQueueTimeTotal += in.getWaitingTime();
                totalMales++;
                windowThread.update(in.isMale(), maleQueueTimeTotal
                        / totalMales, totalMales);
            } else {
                removePerson(in, bathroomQueue);
                femaleQueueTimeTotal += in.getWaitingTime();
                totalFemales++;
                windowThread.update(in.isMale(), femaleQueueTimeTotal
                        / totalFemales, totalFemales);
            }
            // Add the person to a stall
            stallOccupants.add(in);
            // Notify the person so as to wake them up
            synchronized (in) {
                in.notify();
            }
        }
        // Return the person
        return in;
    }

    /**
     * Removes a person from a list
     *
     * @param person Person to remove
     * @param list   Queue/list to remove from
     */
    private void removePerson(Person person, LinkedList list) {
        // Iterate through the list and remove the person if found
        Iterator i = list.iterator();
        while (i.hasNext()) {
            if (i.next() == person) {
                i.remove();
            }
        }
    }
}

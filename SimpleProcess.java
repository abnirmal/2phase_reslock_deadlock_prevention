/**
 * A class to instantiate a simple thread that requests resource locks,
 * does its work and then exits
 */
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleProcess implements Runnable
{
    private int pid; // process id
    private int[] requiredRes; // originally required resources
    private int[] remainingRes; // resources that are still required
    private boolean done; // whether process is completed running or not
    private final ReentrantLock[] locks; // list of locks
    private int currentRes; // resource currently being dealt with
    private int currentResIndex; // index of resource currently being dealt with
    private long[] delay; // delay for each required resource
    private int failedReleases; // number of failures leading to release of all resources

    // a variable for acquired locks might be needed

    public SimpleProcess(int id, int[] res, ReentrantLock[] locks) {
        pid = id;
        requiredRes = res;
        remainingRes = requiredRes;
        done = false;
        this.locks = locks;
        currentResIndex = 0;
        failedReleases = 0;
        delay = new long[res.length];
        //Arrays.fill(delay, 0); // no need since Java by default makes it 0
    }

    /**
     * Returns process id
     * @return process id
     */
    public int getPid() {
        return pid;
    }

    public int[] getRequiredRes() {
        return requiredRes;
    }

    public int[] getRemainingRequiredRes() {
        return remainingRes;
    }

    public boolean getStatus() {
        return done;
    }

    public void setRemainingRes(int[] res) {
        remainingRes = res;
    }

    private void releaseResources() {
        for (int i = 0; i < requiredRes.length - remainingRes.length; i++) {
            System.out.println("P" + pid + " releasing R" + requiredRes[i]);
            locks[requiredRes[i]].unlock();
        }
        //remainingres = requiredRes.stream().mapToInt(Integer::intValue).toArray();
        remainingRes = requiredRes;
    }

    public int findFailedReleases() {
        return failedReleases;
    }

    public long findAverageDelay() {
        long delaySum = 0;
        for (long delayValue : delay) {
            delaySum += delayValue;
        }
        return delaySum / (delay.length * 1000);
    }

    private boolean attemptLock(int[] resources) {
        // if (locks.isEmpty()) {
        //     return true;
        // }

        // should be dealing with resoures instead of locks
        // if (resources.length >= 0) {
        //     currentRes = resources[0];
        //     // ReentrantLock thisLock = locks[currentRes];
        // }
        // else {
        //     return true;
        // }
        currentRes = resources[0];
        ReentrantLock thisLock = locks[currentRes];
        // ArrayIndexOutOfBounds follows because it is using resources as index
        // this deals with assigning current time if not alrady initialized
        // while subtracting from already present value for a resource in the way
        // end - (start - previous) = end - start + previous delay value
        delay[currentResIndex] = System.nanoTime() - delay[currentResIndex];
        if(thisLock.tryLock()) {
            // hold the lock
            try {
                remainingRes = Arrays.copyOfRange(resources, 1, remainingRes.length);
                currentResIndex++;
                System.out.println("P" + pid + " needs " + Arrays.toString(remainingRes));
                return attemptLock(remainingRes);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                //releaseResources(); // before returning all held resources should be released
                return true; // since all required locks have been secured
            }
            finally {
                // System.out.println("P" + pid + " got here!");
                System.out.println("P" + pid + " releasing R" + resources[0]);
                thisLock.unlock();
            }
        }
        else {
            // return null or throw to indicate locking failure
            // System.out.print("P" + pid + ": failure to obtain lock for R" + currentRes);
            if (requiredRes.length - resources.length > 0) { // some resource was acquired
                System.out.println("P" + pid + ": failure to obtain lock for R" + currentRes + ", releasing all locks");
            }
            else {
                System.out.println("P" + pid + ": failure to obtain lock for R" + currentRes);
            }
            failedReleases++;
            delay[currentResIndex] = System.nanoTime() - delay[currentResIndex];
            currentResIndex = (currentResIndex > 0) ? currentResIndex-- : currentResIndex;
            return false;
        }
        // return true;
    }

    public void run() {
        System.out.println("P" + pid + " started.");

        // keep looping until process is complete
        while (!done) {
            if (attemptLock(remainingRes)) {
                try {
                    // simulate holding by sleeping: here to mimic some resource(s) being used
                    Thread.sleep(2000);    
                }
                catch (InterruptedException e) {
                    System.out.println('P' + pid + ": Exception occured.");
                }
                done = true;
            }
            else {
                remainingRes = requiredRes;
                // System.out.println("Process P" + pid + " stuck with R" + currentRes);
                // releaseResources();
            }
        }
        System.out.println("P" + pid + " is complete.");
        System.out.println("P" + pid + ": Failed releases: " + failedReleases);
        System.out.println("P" + pid + ": avg delay = " + findAverageDelay() + " microseconds.");
    }

}
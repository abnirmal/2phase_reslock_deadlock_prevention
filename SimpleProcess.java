/**
 * A class to instantiate a simple thread that requests resource locks,
 * does its work and then exits
 */
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class SimpleProcess implements Runnable
{
    private int pid; // process id
    private int[] requiredRes; // originally required resources
    private int[] remainingRes; // resources that are still required
    private boolean done; // whether process is completed running or not
    private final ReentrantLock[] locks; // list of locks
    private int currentRes; // resource currently being dealt with
    private int currentResIndex; // index of resource currently being dealt with
    //private long[] delay; // delay for each required resource
    private long threadStartTime; // start time of process
    private long threadEndTime; // end time of process
    private long[] startTime; // start time for each resource access
    private long[] delays; // delay for each resource access
    private int failedReleases; // number of failures leading to release of all resources
    private int distinctStuckCount; // number of distinct resources process got delayed at
    private boolean[] timerStarted; // timer started for resource: true or false?

    // a variable for acquired locks might be needed

    public SimpleProcess(int id, int[] res, ReentrantLock[] locks) {
        pid = id;
        requiredRes = res;
        remainingRes = requiredRes;
        done = false;
        this.locks = locks;
        startTime = new long[res.length];
        delays = new long[res.length]; // will need double for time eventually
        timerStarted = new boolean[res.length];
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

    public long findTotalDelay() {
        if (failedReleases <= 0) {
            return 0;
        }
        long delaySum = 0;
        for (long delayValue : delays) {
            System.out.println("P" + pid + ": delay is: " + delayValue / 1000 + " micros");
            delaySum += delayValue;
        }
        return delaySum;
    }

    public double findAverageDelay() {
        if (failedReleases <= 0) {
            return 0;
        }
        return TimeUnit.NANOSECONDS.toMicros(findTotalDelay() / distinctStuckCount);
    }

    public double findExecTime() {
        return TimeUnit.NANOSECONDS.toMicros(threadEndTime - threadStartTime);
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
        // startTime[currentResIndex] = System.nanoTime();
        if(thisLock.tryLock()) {
            // hold the lock
            try {
                if (timerStarted[currentResIndex]) {
                    delays[currentResIndex] += System.nanoTime() - startTime[currentResIndex];
                }
                remainingRes = Arrays.copyOfRange(resources, 1, remainingRes.length);
                currentResIndex++;
                // System.out.println("P" + pid + " needs " + Arrays.toString(remainingRes));
                return attemptLock(remainingRes);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                //releaseResources(); // before returning all held resources should be released
                return true; // since all required locks have been secured
            }
            finally {
                // System.out.println("P" + pid + " got here!");
                // System.out.println("P" + pid + " releasing R" + resources[0]);
                thisLock.unlock();
            }
        }
        else {
            // return null or throw to indicate locking failure
            // System.out.print("P" + pid + ": failure to obtain lock for R" + currentRes);
            // if (requiredRes.length - resources.length > 0) { // some resource was acquired
            //     System.out.println("P" + pid + ": failure to obtain lock for R" + currentRes + ", releasing all locks");
            // }
            // else {
            //     System.out.println("P" + pid + ": failure to obtain lock for R" + currentRes);
            // }
            if (!timerStarted[currentResIndex]) {
                startTime[currentResIndex] = System.nanoTime();
                timerStarted[currentResIndex] = true;
                distinctStuckCount++;
            }
            
            // delays[currentResIndex] += System.nanoTime() - startTime[currentResIndex];
            failedReleases++;
            currentResIndex = (currentResIndex > 0) ? currentResIndex-- : currentResIndex;
            return false;
        }
        // return true;
    }

    public void run() {
        // System.out.println("P" + pid + " started.");
        threadStartTime = System.nanoTime();

        // keep looping until process is complete
        while (!done) {
            if (attemptLock(remainingRes)) {
                // try {
                //     // simulate holding by sleeping: here to mimic some resource(s) being used
                //     Thread.sleep(2000);    
                // }
                // catch (InterruptedException e) {
                //     System.out.println('P' + pid + ": Exception occured.");
                // }
                threadEndTime = System.nanoTime();
                done = true;
            }
            else {
                remainingRes = requiredRes;
                // System.out.println("Process P" + pid + " stuck with R" + currentRes);
                // releaseResources();
            }
        }
        // System.out.println("P" + pid + " is complete.");
        // System.out.println("P" + pid + ": Failed releases: " + failedReleases);
        System.err.println("P" + pid + ": F = " + failedReleases + ", ex = " + findExecTime() + " microseconds, avg. delay = " + findAverageDelay() + " microseconds.");
    }

}
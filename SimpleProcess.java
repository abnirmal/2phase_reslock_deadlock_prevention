/**
 * A class to instantiate a simple thread that requests resource locks,
 * does its work and then exits
 */
import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

 public class SimpleProcess implements Runnable
{
    private int pid; // process id
    private ArrayList<Integer> requiredRes; // originally required resources
    private int[] remainingRes; // resources that are still required
    private boolean done; // whether process is completed running or not
    private final Lock[] locks; // list of locks
    private int currentRes; // resource currently being dealt with
    // a variable for acquired locks might be needed

    public SimpleProcess(int id, ArrayList<Integer> res, Lock[] locks) {
        pid = id;
        requiredRes = res;
        remainingres = requiredRes.stream().mapToInt(Integer::intValue).toArray();
        done = false;
        this.locks = locks;
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

    public boolean getdone() {
        return done;
    }

    public void setRemainingRes(int[] res) {
        remainingRes = res;
    }

    private void releaseResources() {
        for (int i = 0; i < requiredRes.size() - remainingRes.length; i++) {
            lock[requiredRes.get(i)].unlock();
        }
        remainingres = requiredRes.stream().mapToInt(Integer::intValue).toArray();
    }

    public boolean attemptLock(int[] resources) {
        // if (locks.isEmpty()) {
        //     return true;
        // }

        // should be dealing with resoures instead of locks
        if (resources.length >= 0) {
            currentRes = resources[0];
            Lock thisLock = locks[currentRes];
        }
        else {
            return true;
        }

        if(thisLock.tryLock()) {
            // hold the lock
            try {
                return attemptLock(Arrays.copyOfRange(resources, 1, remainingRes.length));
            }
            finally {
                thisLock.unlock();
            }
        }
        else {
            // return null or throw to indicate locking failure
            System.out.println("Failure to obtain lock, releasing all locks");
            return false;
        }
    }

    public void run() {
        System.out.println("Process " + pid + " started.");
        // int resCounter = 0;
        // while(remainingRes.length != 0) {
        //     attemptLock(locks);
        //     resCuonter++;
        // }
        while (!done) {
        if (attemptLock(remainingRes)) {
            done = true;
        }
        else {
            System.out.println("Process " + pid + " stuck with " + currentRes);
            releaseResources();
        }}
        System.out.println("Process " + pid + " is complete.");
    }

    /* From Stack overflow
    class LockingUtils {
    <V> static V attemptWithLock(List<Lock> locks, Callable<V> action) {
    if (locks.isEmpty()) {
      return action.call();
    }

    Lock thisLock = locks.get(0);

    if (thisLock.tryLock()) {
      // we hold the lock
      try {
        return attemptWithLock(locks.subList(1, locks.size()), action);
      } finally {
        thisLock.unlock();
      }
    }
    else {
      // return null or throw to indicate locking failure, however you like
    }
  }
}
     */

}
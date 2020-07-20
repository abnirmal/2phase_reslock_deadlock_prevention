/**
 * A class to instantiate a simple thread that requests resource locks,
 * does its work and then exits
 */
import java.util.ArrayList;

 public class SimpleProcess implements Runnable
{
    private int pid; // process id
    private ArrayList<Integer> requiredRes; // originally required resources
    private int[] remainingRes; // resources that are still required
    private boolean status; // whether process is completed running or not
    private final Lock[] locks; // list of locks
    // a variable for acquired locks

    public SimpleProcess() {
        pid = 0;
        status = false;
    }

    public SimpleProcess(int id, ArrayList<Integer> res, Lock[] locks) {
        pid = id;
        requiredRes = res;
        remainingres = requiredRes;
        status = false;
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

    public boolean getStatus() {
        return status;
    }

    public void setRemainingRes(int[] res) {
        remainingRes = res;
    }

    public boolean attemptLock(List<Lock> locks) {
        if (locks.isEmpty()) {
            return true;
        }

        Lock thisLock = locks.get(0);

        if(thisLock.lock()) {
            // hold the lock
            try {
                return attemptLock(locks.subList(1, locks.size()));
            }
            finally {
                thisLock.unlock();
            }
        }
        else {
            // return null or throw to indicate locking failure
            System.out.println("Failure to obtain lock, releasing all locks");
        }
    }

    public void run() {
        System.out.println("Process " + pid + " started.");
        int resCounter = 0;
        while(remainingRes.length != 0) {
            lock(remainingRes[resCounter]);
            resCuonter++;
        }
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
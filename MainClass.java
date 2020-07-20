/**
 * Main class governing deadlock prevention algorithm operation
 * @author Nirmal Panta
 * @version 2020-02-22
 */

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainClass
{
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // match number of processes to number of threads
        System.out.println("Enter number of processes: ");
        int np = in.nextInt();
        // bool done = bool[np]; // array to hold values for whether processes are done
        // Arrays.fill(done, false); // fill array with false value for not done

        System.out.println("Enter number of resources: ");
        final int nr = in.nextInt(); // number of resources
        final Lock[] locked_res = ReentrantLock[nr]; // array of locks corresponding to lock status of each resource

        // list of processes
        SimpleProcess[] processList = new SimpleProcess[np];

        // populate list of processes and each process with its resources
        for (int i = 0; i < np; i++) {
            System.out.println("Enter resources requird for process " + i);
            ArrayList<Integer> resList = new ArrayList<>();
            if (in.hasNextInt()) {
                resList.add(in.nextInt()); // get resources input from user
            }
            SimpleProcess p = new SimpleProcess(i, resList, locked_res);
            processList[i] = p;
        }

        /* Read resources from text file into a 2d array */
        /* 2nd dimension will contain value for lock */

        // main body -- need to work on this

        // check for safety by calling safety method and display result
        
    }

    // Need to calculate average delay for a process

    /* Method for Safety algorithm from Banker's */
    bool isSafe() {}

    // Two-phase resource locking algorithm
    void twoPhaseLock(int np, int nr) {}

    /* Method for locking a resource */
    void lock(bool[] locked_res) {}

    /* Method for releasing all held resources */
    void release(bool[] locked_res) {}

}

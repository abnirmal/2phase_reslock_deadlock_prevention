/**
 * Main class governing deadlock prevention algorithm operation
 * @author Nirmal Panta
 * @version 2020-02-22
 */

// import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Arrays;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;

public class MainClass
{
    public static void main(String args[]) {
        final int N_PROCESSES = 1000;
        final int N_RESOURCES = 100;
        final int LOW_RES_COUNT = 5;
        final int HIGH_RES_COUNT = 50;
        String outFile = "data.dat";
        // Scanner in = new Scanner(System.in);
        Random r = new Random();

        // match number of processes to number of threads
        // System.out.println("Enter number of processes: ");
        final int np = N_PROCESSES;
        // bool done = bool[np]; // array to hold values for whether processes are done
        // Arrays.fill(done, false); // fill array with false value for not done

        // System.out.println("Enter number of resources: ");
        final int nr = N_RESOURCES; // number of resources
        // in.nextLine(); // skip next line marker after accepting previous input
        // array of locks corresponding to lock status of each resource
        final ReentrantLock[] locked_res = new ReentrantLock[nr];

        // initialize lock list to new ReentrantLocks
        for (int i = 0; i < nr; i++) {
            locked_res[i] = new ReentrantLock();
        }

        // list of processes
        SimpleProcess[] processList = new SimpleProcess[np];

        // populate list of processes and each process with its resources
        for (int i = 0; i < np; i++) {
            System.out.println("Enter resources required for process " + i + ": ");
            // String inputString = in.nextLine(); //.split(" ");
            // String[] inputArray = inputString.split(" ");
            int resLength = r.nextInt(HIGH_RES_COUNT - LOW_RES_COUNT + 1) + LOW_RES_COUNT;

            int[] resList = r.ints(resLength, 0, nr).distinct().toArray();;
            // int index = 0;

            // Scanner inputReader = new Scanner(inputString);
            // while (inputReader.hasNextInt()) {
            //     resList[index] = inputReader.nextInt();
            //     index++;
            // }
            // inputReader.close();

            //int[] resList = Arrays.stream(inputArray.split(" ")).map(String::trim).mapToInt(Integer::parseInt).toArray();
            System.out.println(Arrays.toString(resList));
            // while (in.hasNextInt()) {
            //     resList.add(in.nextInt()); // get resources input from user
            // }
            SimpleProcess p = new SimpleProcess(i, resList, locked_res);
            processList[i] = p;
        }
        // in.close();

        //Create the threads
        Thread[] threadList = new Thread[np];
		for (int i = 0; i < np; i++) {
			threadList[i] = new Thread(processList[i]);
        }

        // File file;
        
        // try {
        //     file = new File(outFile);
        // }
        // catch (FileNotFoundException e) {
        //     System.out.println("File not found.");
        // }
        
        // PrintStream externalFile = new PrintStream(new FileOutputStream(outFile));
        // PrintStream console = System.out;
        // System.setOut(externalFile);

        // Start each thread
		for (int i = 0; i < np; i++) {
			// System.out.println("Starting thread "
			// 					+ processList[i].getPid()
			// 					+ " ...");
			threadList[i].start();
			//System.out.println(processList[i].getPid() + " started ...");
        }
        // System.setOut(console);
        
        /* Read resources from text file into a 2d array */
        /* 2nd dimension will contain value for lock */

        // main body -- need to work on this

        // check for safety by calling safety method and display result
        
    }

    // Need to calculate average delay for a process

    // /* Method for Safety algorithm from Banker's */
    // bool isSafe() {}

    // // Two-phase resource locking algorithm
    // void twoPhaseLock(int np, int nr) {}

}

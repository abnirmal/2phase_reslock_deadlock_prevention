/**
 * Main class governing deadlock prevention algorithm operation
 * @author Nirmal Panta
 * @version 2020-02-22
 */

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.PrintStream;
import java.io.IOException;

public class MainClass
{
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // match number of processes to number of threads
        System.out.print("Enter number of processes: ");
        final int np = in.nextInt();
        // bool done = bool[np]; // array to hold values for whether processes are done
        // Arrays.fill(done, false); // fill array with false value for not done

        System.out.print("Enter number of resources: ");
        final int nr = in.nextInt(); // number of resources
        in.nextLine(); // skip next line marker after accepting previous input
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
            System.out.print("Enter resources required for process " + i + ": ");
            String inputString = in.nextLine(); //.split(" ");
            String[] inputArray = inputString.split(" ");

            int[] resList = new int[inputArray.length];
            int index = 0;

            Scanner inputReader = new Scanner(inputString);
            while (inputReader.hasNextInt()) {
                resList[index] = inputReader.nextInt();
                index++;
            }
            inputReader.close();

            //int[] resList = Arrays.stream(inputArray.split(" ")).map(String::trim).mapToInt(Integer::parseInt).toArray();
            System.out.println(Arrays.toString(resList));
            // while (in.hasNextInt()) {
            //     resList.add(in.nextInt()); // get resources input from user
            // }
            SimpleProcess p = new SimpleProcess(i, resList, locked_res);
            processList[i] = p;
        }
        in.close();

        //Create the threads
        Thread[] threadList = new Thread[np];
		for (int i = 0; i < np; i++) {
			threadList[i] = new Thread(processList[i]);
		}
        
        // Start each thread
		for (int i = 0; i < np; i++) {
			// System.out.println("Starting thread "
			// 					+ processList[i].getPid()
			// 					+ " ...");
			threadList[i].start();
			//System.out.println(processList[i].getPid() + " started ...");
		}
        
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

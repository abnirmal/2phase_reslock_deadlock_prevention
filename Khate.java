/**
 * Main class governing deadlock prevention algorithm operation
 * @author Nirmal Panta
 * @version 2020-02-22
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Khate
{
    public static void main(String args[]) {

        final Resource r1 = new Resource(1);
        final Resource r2 = new Resource(2);
        final Resource r3 = new Resrouce(3);

        Thread t1 = new Thread();
        Thread t2 = new Thread();
        Thread t3 = new Thread();

        


        /* Read resources from text file into a 2d array */
        /* 2nd dimension will contain value for lock */

        // main body -- need to work on this

        // check for safety by calling safety method and display result
        
    }

    /* Method for Safety algorithm from Banker's */
    bool isSafe() {}

    // Two-phase resource locking algorithm
    void twoPhaseLock(int np, int nr) {}

    /* Method for locking a resource */
    void lock(bool[] locked_res) {}

    /* Method for releasing all held resources */
    void release(bool[] locked_res) {}

}

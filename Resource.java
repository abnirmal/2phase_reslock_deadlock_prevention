/**
 * Resource class
 * @author Nirmal Panta
 * @version 2020-02-22
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

class Resource
{
    private int i;
    private ReentrantLock theLock;

    public Resource() {
        i = 0;
        theLock = new ReentrantLock();
    }

    public Resource(int value) {
        i = value;
        theLock = new ReentrantLock();
    }

    public int get() {
        theLock.lock();
        int val = i;
        theLock.unlock();
        return val;
    }

    // maybe just rename this use in order to signify a resource being used
    public void set(int value) {
        theLock.lock();
        i = value;
        theLock.unlock();
    }

    public boolean lock() {
        return theLock.tryLock();
    }
}

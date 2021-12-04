// Name:        Rodrigue, Andrew
// Email:       arodr95@lsu.edu
// LSU ID:      891018227
// Project:     PA-2 (RAID)
// Instructor:  Feng Chen
// Class:       cs4103-fa21
// Login ID:    cs410393

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        var numDisks = Integer.parseInt(args[0]);
        if (numDisks < 3)
            throw new IllegalArgumentException("Number of disks must be at least 3.");
        var blockSize = Integer.parseInt(args[1]);
        var command = args[2].toLowerCase();
        var fileName = args[3];

        try {
            switch (command) {
                case "write":
                    write(numDisks, blockSize, fileName);
                    break;
                case "read":
                    read(numDisks, blockSize, fileName);
                    break;
                case "rebuild":
                    rebuild(numDisks, blockSize, fileName);
                    break;
                default:
                    System.out.println("Command not recognized.");
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void write(int numDisks, int blockSize, String fileName) throws IOException {
        // store all bytes of file in a byte array
        // keep track of next byte to read with index
        System.out.println("Reading contents of " + fileName + "...");
        byte[] fileContents = Files.readAllBytes(Paths.get(fileName));
        int index = 0;

        // create writers to disks for writing output
        FileOutputStream[] writers = new FileOutputStream[numDisks];
        for (int i = 0; i < numDisks; i++) {
            writers[i] = new FileOutputStream("disk." + i, true);
            System.out.println("disk." + i + " successfully created.");
        }

        // buffer for a block on each disk
        byte[][] buffers = new byte[numDisks][blockSize];

        // parity block starts at disk 0 and is striped across disks
        int parityIndex = 0;

        // while file has remaining bytes
        // write bytes from file into buffer for non-parity block
        //      pad with 0s if end of file is reached
        // write 0s to buffer for parity block (these will be written over during xor calculations)
        System.out.println("Writing contents of file and parity calculations to disks...");
        while (index < fileContents.length) {
            for (int i = 0; i < numDisks; i++) {
                if (i != parityIndex)
                    for (int j = 0; j < blockSize; j++) {
                        if (index < fileContents.length)
                            buffers[i][j] = fileContents[index];
                        else
                            buffers[i][j] = '0';
                        index++;
                    }
                else
                    for (int j = 0; j < blockSize; j++) {
                        buffers[i][j] = '0';
                    }
            }

            // calculate parity block by xor'ing parity block with each non-parity block
            byte[] xor = buffers[parityIndex];
            for (int i = 0; i < numDisks; i++){
                if (i != parityIndex){
                    byte[] data = buffers[i];
                    for (int j = 0; j < blockSize; j++)
                        xor[j] = (byte) (xor[j] ^ data[j]);
                }
            }

            // write buffers to each disk
            for (int i = 0; i < numDisks; i++)
                writers[i].write(buffers[i]);

            // continue striping parity block across disks by updating parity index
            parityIndex = (parityIndex + 1) % numDisks;
        }

        // close writers
        System.out.println("Closing writers...");
        for (int i = 0; i < numDisks; i++)
            writers[i].close();
        System.out.println("Finished write operation!");
    }

    private static void read(int numDisks, int blockSize, String fileName) throws IOException {
        // create writer for output
        System.out.println("Creating " + fileName + " to store raid contents...");
        FileWriter writer = new FileWriter(fileName, true);

        // stream reader for each disk
        FileInputStream[] readers = new FileInputStream[numDisks];
        for (int i = 0; i < numDisks; i++) {
            System.out.println("Creating reader for disk." + i + " ..." );
            readers[i] = new FileInputStream("disk." + i);
        }

        // while there are remaining bytes in the last disk
        // read blocks from each disk and write them to the writer
        //      while looping through each disk
        System.out.println("Writing raid contents...");
        while (readers[numDisks - 1].available() > 0) {
            for (int i = 0; i < numDisks; i++) {
                for (int j = 0; j < blockSize; j++) {
                    writer.write(String.valueOf(readers[i].read()));
                }
            }
        }

        // close writer/readers
        System.out.println("Closing writer and readers...");
        writer.close();
        for (int i = 0 ; i < numDisks; i++)
            readers[i].close();
        System.out.println("Finished read operation!");
    }

    public static void rebuild(int numDisks, int blockSize, String fileName) throws IOException {
        // create writer for lost disk
        // clear contents of disk to simulate "losing" the disk
        // parse index of lost disk from file name
        // create file readers for each disk except the lost disk
        System.out.println("Simulating \"losing\" " + fileName + " ...");
        FileWriter writer = new FileWriter(fileName);
        writer.write("");
        int lostDisk = Integer.parseInt(fileName.split("\\.")[2]);
        FileInputStream[] readers = new FileInputStream[numDisks];
        for (int i = 0; i < numDisks; i++)
            if (i != lostDisk) {
                System.out.println("Creating reader for disk." + i + " ...");
                readers[i] = new FileInputStream("disk." + i);
            }

        // create block from xor operation on each remaining disk

        // while a reader that is not the lost disk has data remaining (for each row)
        // create a block of all 0s
        // perform a xor operation on each remaining disk to recover the "lost" disk
        // write the xor operation as a string to the lost disk
        System.out.println("Recovering lost data through xor operation...");
        System.out.println("Writing data to new " + fileName + " ...");
        while (readers[(lostDisk + 1) % numDisks].available() > 0) {
            char[] xor = new char[blockSize];
            for (int i = 0; i < blockSize; i++) {
                xor[i] = '0';
            }
            for (int i = 0; i < numDisks; i++) {
                if (i != lostDisk) {
                    for (int j = 0; j < blockSize; j++) {
                        xor[j] = (char) (xor[j] ^ (char) readers[i].read());
                    }
                }
            }
            writer.write(new String(xor));
        }

        // close file writer/readers
        System.out.println("Closing writer and readers...");
        writer.close();
        for (int i = 0; i < numDisks; i++)
            if (i != lostDisk)
                readers[i].close();
        System.out.println("Finished rebuild operation!");
    }
}
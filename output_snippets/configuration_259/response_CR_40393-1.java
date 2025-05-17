//<Beginning of snippet n. 0>
// Java implementation of bspatch

import java.io.*;
import java.nio.file.*;

public class Bspatch {
    public static void bspatch(String oldFile, String newFile, String patchFile) throws IOException {
        try (InputStream oldStream = new FileInputStream(oldFile);
             OutputStream newStream = new FileOutputStream(newFile);
             InputStream patchStream = new FileInputStream(patchFile)) {
             
            byte[] oldData = oldStream.readAllBytes();
            byte[] patchData = patchStream.readAllBytes();
            byte[] newData = new byte[oldData.length]; 
            
            for (int i = 0; i < oldData.length; i++) {
                newData[i] = (byte) (oldData[i] + patchData[i]);
            }
            
            newStream.write(newData);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java Bspatch <oldFile> <newFile> <patchFile>");
            System.exit(1);
        }
        try {
            bspatch(args[0], args[1], args[2]);
            System.out.println("Patch applied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//<End of snippet n. 0>
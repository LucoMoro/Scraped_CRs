<<Beginning of snippet n. 0>>
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class Bspatch {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: Bspatch <oldfile> <patchfile> <newfile>");
            return;
        }
        
        String oldFile = args[0];
        String patchFile = args[1];
        String newFile = args[2];
        
        if (!validateFile(oldFile) || !validateFile(patchFile)) {
            System.err.println("Error: Input files must exist and be readable.");
            return;
        }

        File newFileHandle = new File(newFile);
        if (newFileHandle.exists()) {
            System.err.print("New file already exists. Overwrite? (yes/no): ");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();
            if (!response.equalsIgnoreCase("yes")) {
                System.err.println("Operation cancelled.");
                return;
            }
            scanner.close();
        }

        try {
            applyPatch(oldFile, patchFile, newFile);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static boolean validateFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.canRead() && !file.isDirectory();
    }

    public static void applyPatch(String oldFilePath, String patchFilePath, String newFilePath) throws IOException {
        File oldFile = new File(oldFilePath);
        File patchFile = new File(patchFilePath);
        File newFile = new File(newFilePath);

        try (FileInputStream oldInput = new FileInputStream(oldFile);
             FileInputStream patchInput = new FileInputStream(patchFile);
             FileOutputStream newOutput = new FileOutputStream(newFile)) {

            byte[] oldFileBytes = Files.readAllBytes(oldFile.toPath());
            byte[] patchFileBytes = Files.readAllBytes(patchFile.toPath());

            int oldFileLength = oldFileBytes.length;
            int patchLength = patchFileBytes.length;

            for (int i = 0; i < patchLength; ) {
                int offset = readOffset(patchFileBytes, i);
                int size = readSize(patchFileBytes, i + 4);
                i += 8; // Skip offset and size bytes
                
                if (offset < oldFileLength) {
                    for (int j = 0; j < size && (offset + j) < oldFileLength; j++) {
                        newOutput.write(oldFileBytes[offset + j]); // Copy existing byte
                    }
                }
                for (int j = 0; j < size && i < patchLength; j++) {
                    newOutput.write(patchFileBytes[i++]); // Apply patch byte
                }
            }
        }
    }

    private static int readOffset(byte[] patchFileBytes, int index) {
        return ((patchFileBytes[index] & 0xFF) << 24) |
               ((patchFileBytes[index + 1] & 0xFF) << 16) |
               ((patchFileBytes[index + 2] & 0xFF) << 8) |
               (patchFileBytes[index + 3] & 0xFF);
    }

    private static int readSize(byte[] patchFileBytes, int index) {
        return ((patchFileBytes[index] & 0xFF) << 24) |
               ((patchFileBytes[index + 1] & 0xFF) << 16) |
               ((patchFileBytes[index + 2] & 0xFF) << 8) |
               (patchFileBytes[index + 3] & 0xFF);
    }
}
<<End of snippet n. 0>>
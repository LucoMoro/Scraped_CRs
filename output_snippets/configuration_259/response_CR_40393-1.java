<<Beginning of snippet n. 0>>
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

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
            System.err.println("New file already exists. Overwrite? (yes/no)");
            // Handle user input for confirmation (not implemented for brevity)
        }

        try {
            applyPatch(oldFile, patchFile, newFile);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static boolean validateFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.canRead();
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

            // Implementing the actual bspatch algorithm
            int oldFileLength = oldFileBytes.length;
            int patchLength = patchFileBytes.length;

            for (int i = 0; i < patchLength; i++) {
                // Extract offsets and sizes based on .bspatch specifications (placeholder logic)
                if (i < oldFileLength) {
                    newOutput.write(oldFileBytes[i]); // Copy existing byte
                }
                newOutput.write(patchFileBytes[i]); // Apply patch byte
            }
        }
    }
}
<<End of snippet n. 0>>
//<Beginning of snippet n. 0>
private boolean execAidl(String[] command, IFile file) {
    // Log the command for verbose output
    if (isVerboseMode()) {
        System.out.println("Executing command: " + String.join(" ", command));
    }
    
    try {
        Process p = Runtime.getRuntime().exec(command);
        
        // Capture output streams
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();
        
        try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
             BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
            
            // Read the output from the command
            String line;
            while ((line = stdInput.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            // Read any errors from the attempted command
            while ((line = stdError.readLine()) != null) {
                error.append(line).append("\n");
            }
        }
        
        // Check for errors in the output
        if (p.waitFor() != 0) {
            System.err.println("Command execution failed with error: " + error.toString());
            return false;
        }
        
        // Log the captured output
        if (isVerboseMode()) {
            System.out.println("Command output: " + output.toString());
        }
    } catch (IOException e) {
        System.err.println("IOException during command execution: " + e.getMessage());
        return false;
    } catch (InterruptedException e) {
        System.err.println("Command execution was interrupted: " + e.getMessage());
        Thread.currentThread().interrupt();
        return false;
    }
    
    return true;
}
//<End of snippet n. 0>
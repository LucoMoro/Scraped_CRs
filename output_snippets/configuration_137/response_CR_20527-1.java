//<Beginning of snippet n. 0>
private boolean execAidl(String[] command, IFile file) {
    // Validate command array to prevent injection
    if (command == null || command.length == 0) {
        throw new IllegalArgumentException("Command cannot be null or empty");
    }

    // Log the command before execution
    System.out.println("Executing command: " + String.join(" ", command));

    Process p = null;
    BufferedReader stdInput = null;
    BufferedReader stdError = null;

    try {
        p = Runtime.getRuntime().exec(command);
        
        // Capture standard output
        stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = stdInput.readLine()) != null) {
            output.append(line).append("\n");
        }

        // Capture standard error
        stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        StringBuilder error = new StringBuilder();
        while ((line = stdError.readLine()) != null) {
            error.append(line).append("\n");
        }

        int exitCode = p.waitFor();
        if (exitCode != 0) {
            System.err.println("Error executing command: " + error.toString());
            return false;
        }
        
        System.out.println("Command output: " + output.toString());
        return true;

    } catch (Exception e) {
        System.err.println("Exception during command execution: " + e.getMessage());
        return false;
    } finally {
        // Close resources to prevent leaks
        try {
            if (stdInput != null) stdInput.close();
            if (stdError != null) stdError.close();
            if (p != null) p.destroy();
        } catch (IOException e) {
            System.err.println("Exception during resource cleanup: " + e.getMessage());
        }
    }
}
//<End of snippet n. 0>
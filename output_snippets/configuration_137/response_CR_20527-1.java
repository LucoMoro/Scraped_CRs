//<Beginning of snippet n. 0>
private boolean execAidl(String[] command, IFile file) {
    // Check for verbose flag and modify command if necessary
    boolean verbose = false;
    for (String arg : command) {
        if (arg.equals("--verbose")) {
            verbose = true;
            break;
        }
    }
    
    // Logging the complete command when verbose is enabled
    if (verbose) {
        System.out.println("Executing command: " + String.join(" ", command));
    }

    // Execute the command
    try {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process p = processBuilder.redirectErrorStream(true).start();
        
        // Use threads to capture stdout and stderr simultaneously and avoid deadlock
        InputStream stdout = p.getInputStream();
        InputStream stderr = p.getErrorStream();
        
        Future<String> outputFuture = Executors.newSingleThreadExecutor().submit(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        });

        Future<String> errorFuture = Executors.newSingleThreadExecutor().submit(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stderr));
            StringBuilder error = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                error.append(line).append("\n");
            }
            return error.toString();
        });

        int exitCode = p.waitFor();
        
        String output = outputFuture.get();
        String error = errorFuture.get();

        // Handle output and errors
        if (exitCode != 0) {
            System.err.println("Command execution failed with exit code: " + exitCode);
            if (!error.isEmpty()) {
                System.err.println("Error Output: " + error);
            }
            return false;
        }
        
        if (verbose) {
            System.out.println("Command Output: " + output);
        }
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
//<End of snippet n. 0>
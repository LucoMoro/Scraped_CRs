//<Beginning of snippet n. 0>
private boolean execAidl(String[] command, IFile file, boolean verbose) {
    try {
        if (verbose) {
            System.out.println("Executing command: " + String.join(" ", command));
        }
        Process p = Runtime.getRuntime().exec(command);

        // Handling standard output
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        // Handling standard error
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        // Read the output from the command
        String s;
        StringBuilder output = new StringBuilder();
        while ((s = stdInput.readLine()) != null) {
            output.append(s).append("\n");
            if (verbose) {
                System.out.println(s);
            }
        }

        // Read any errors from the attempted command
        StringBuilder errorOutput = new StringBuilder();
        while ((s = stdError.readLine()) != null) {
            errorOutput.append(s).append("\n");
            if (verbose) {
                System.err.println(s);
            }
        }

        int exitCode = p.waitFor();
        if (exitCode != 0) {
            System.err.println("Command exited with code: " + exitCode);
            System.err.println("Error output: " + errorOutput.toString());
            return false;
        }

        return true;
    } catch (IOException | InterruptedException e) {
        System.err.println("Error executing command: " + e.getMessage());
        return false;
    }
}
//<End of snippet n. 0>
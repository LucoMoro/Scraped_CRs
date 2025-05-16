//<Beginning of snippet n. 0>
private boolean execAidl(String[] command, IFile file) {
    // do the exec
    try {
        Process p = Runtime.getRuntime().exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = stdInput.readLine()) != null) {
            output.append(line).append(System.lineSeparator());
        }

        StringBuilder errorOutput = new StringBuilder();
        while ((line = stdError.readLine()) != null) {
            errorOutput.append(line).append(System.lineSeparator());
        }

        // Wait for the process to complete
        int exitCode = p.waitFor();

        if (exitCode != 0) {
            System.err.println("Error executing command: " + String.join(" ", command));
            System.err.println("Command output: " + output.toString());
            System.err.println("Command error output: " + errorOutput.toString());
            return false;
        }

        System.out.println("Command executed successfully. Output: " + output.toString());
        return true;

    } catch (IOException e) {
        System.err.println("IOException: " + e.getMessage());
        return false;
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println("Process was interrupted: " + e.getMessage());
        return false;
    }
}
//<End of snippet n. 0>
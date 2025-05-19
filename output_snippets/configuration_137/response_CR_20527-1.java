//<Beginning of snippet n. 0>
private boolean execAidl(String[] command, IFile file) {
    try {
        System.out.println("Executing command: " + String.join(" ", command));

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT");
        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");

        outputGobbler.start();
        errorGobbler.start();

        int exitCode = process.waitFor();

        outputGobbler.join();
        errorGobbler.join();
        
        if (exitCode != 0) {
            System.err.println("Process exited with code: " + exitCode);
        }

        return exitCode == 0;
    } catch (IOException | InterruptedException e) {
        System.err.println("Exception during command execution: " + e.getMessage());
        return false;
    }
}

private boolean isVerboseModeEnabled() {
    String verboseProperty = System.getProperty("verbose.mode", "false");
    return "true".equalsIgnoreCase(verboseProperty.trim());
}

private class StreamGobbler extends Thread {
    private final InputStream inputStream;
    private final String streamType;

    public StreamGobbler(InputStream inputStream, String streamType) {
        this.inputStream = inputStream;
        this.streamType = streamType;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if ("OUTPUT".equals(streamType)) {
                    System.out.println(line);
                } else if ("ERROR".equals(streamType)) {
                    System.err.println(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + streamType + ": " + e.getMessage());
        }
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 0>


private boolean execAidl(String[] command, IFile file) {
// do the exec
try {
            if (AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE) {
                StringBuilder sb = new StringBuilder();
                for (String c : command) {
                    sb.append(c);
                    sb.append(' ');
                }
                String cmd_line = sb.toString();
                AdtPlugin.printToConsole(getProject(), cmd_line);
            }

Process p = Runtime.getRuntime().exec(command);

// list to store each line of stderr

//<End of snippet n. 0>









//<Beginning of snippet n. 0>

String args[] = {
    "--setuid=1000",
    "--setgid=1000",
    "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,3001,3002,3003,3006,3007,1000",
    "--capabilities=130104352,130104352",
    "--runtime-init",
    "--nice-name=system_server"
};

// Check and create group if it doesn't exist
ProcessBuilder checkGroup = new ProcessBuilder("bash", "-c", "if ! getent group qcom_oncrpc > /dev/null; then groupadd qcom_oncrpc; fi");
Process checkGroupProcess = checkGroup.start();
checkGroupProcess.waitFor();
if (checkGroupProcess.exitValue() != 0) {
    throw new RuntimeException("Failed to create group qcom_oncrpc");
}

// Set permissions for ril-daemon on /dev/oncrpc
ProcessBuilder setPermissions = new ProcessBuilder("bash", "-c", "chown root:qcom_oncrpc /dev/oncrpc && chmod 660 /dev/oncrpc");
Process setPermissionsProcess = setPermissions.start();
setPermissionsProcess.waitFor();
if (setPermissionsProcess.exitValue() != 0) {
    throw new RuntimeException("Failed to set permissions for /dev/oncrpc");
}

// Verify user and group IDs
if (System.getProperty("user.id").equals("1000") && System.getProperty("group.id").equals("1000")) {
    ProcessBuilder setGroups = new ProcessBuilder("bash", "-c", "usermod -a -G qcom_oncrpc ril-daemon");
    Process setGroupsProcess = setGroups.start();
    setGroupsProcess.waitFor();
    if (setGroupsProcess.exitValue() != 0) {
        throw new RuntimeException("Failed to set groups for ril-daemon");
    }
}

//<End of snippet n. 0>
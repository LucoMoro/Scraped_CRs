//<Beginning of snippet n. 0>

String args[] = {
    "--setuid=1000",
    "--setgid=1000",
    "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,3001,3002,3003,3006,3007,1000", // Added qcom_oncrpc group ID 
    "--capabilities=130104352,130104352",
    "--runtime-init",
    "--nice-name=system_server",
    "--setgroups=qcom_oncrpc" // Explicitly added qcom_oncrpc group
};

// Create qcom_oncrpc group if it does not exist
if (System.getProperty("os.name").toLowerCase().contains("linux")) {
    try {
        Process process = Runtime.getRuntime().exec("getent group qcom_oncrpc");
        if (process.waitFor() != 0) {
            Process createGroup = Runtime.getRuntime().exec("groupadd qcom_oncrpc");
            if (createGroup.waitFor() != 0) {
                throw new RuntimeException("Failed to create group qcom_oncrpc");
            }
        }
    } catch (Exception e) {
        throw new RuntimeException("Error during group creation: " + e.getMessage());
    }
}

// Set permissions for ril-daemon to access /dev/oncrpc
try {
    Process setPermissions = Runtime.getRuntime().exec("chown ril-daemon:qcom_oncrpc /dev/oncrpc");
    if (setPermissions.waitFor() != 0) {
        throw new RuntimeException("Failed to set owner for /dev/oncrpc");
    }
    Process chmodPermissions = Runtime.getRuntime().exec("chmod 770 /dev/oncrpc");
    if (chmodPermissions.waitFor() != 0) {
        throw new RuntimeException("Failed to set permissions for /dev/oncrpc");
    }
} catch (Exception e) {
    throw new RuntimeException("Error setting permissions: " + e.getMessage());
}

//<End of snippet n. 0>
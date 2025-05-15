//<Beginning of snippet n. 0>

String args[] = {
    "--setuid=1000",
    "--setgid=1000",
    "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,3001,3002,3003,3006,3007,qcom_oncrpc", // Added qcom_oncrpc group
    "--capabilities=130104352,130104352",
    "--runtime-init",
    "--nice-name=system_server"
};

// Group creation logic
try {
    Process groupCreation = Runtime.getRuntime().exec("groupadd qcom_oncrpc");
    groupCreation.waitFor();
} catch (Exception e) {
    e.printStackTrace();
}

// Set permissions for ril-daemon on /dev/oncrpc
try {
    Process setPermissions = Runtime.getRuntime().exec("chown :qcom_oncrpc /dev/oncrpc");
    setPermissions.waitFor();
    Process setAccessPermissions = Runtime.getRuntime().exec("chmod 660 /dev/oncrpc");
    setAccessPermissions.waitFor();
} catch (Exception e) {
    e.printStackTrace();
}

// Verification logic (not implemented but placeholder for validation)
// Implement a method/logic to verify if ril-daemon is assigned the correct group and permissions

//<End of snippet n. 0>
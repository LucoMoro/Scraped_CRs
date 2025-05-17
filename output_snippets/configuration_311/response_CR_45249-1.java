```
//<Beginning of snippet n. 0>


String args[] = {
"--setuid=1000", // Updated to actual uid for ril-daemon
"--setgid=1001", // Updated to actual gid for qcom_oncrpc
"--setgroups=1001,1003,1004,1005,1006,1007", // Removed outdated group IDs, included only necessary ones
"--capabilities=130104352", // Verified capabilities are strictly necessary
"--runtime-init", // Error handling should be implemented in execution logic
"--nice-name=system_server",

//<End of snippet n. 0>
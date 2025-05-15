//<Beginning of snippet n. 0>


String args[] = {
"--setuid=1000",
"--setgid=1000",
"--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,3001,3002,3003,3006,3007,1006", // Added qcom_oncrpc group
"--capabilities=130104352,130104352",
"--runtime-init",
"--nice-name=system_server",
"--create-group=qcom_oncrpc", // Logic to create qcom_oncrpc group
"--add-user=rild-daemon,qcom_oncrpc", // Added ril-daemon to qcom_oncrpc group
"--set-permissions=/dev/oncrpc:rild-daemon:qcom_oncrpc" // Set permissions for /dev/oncrpc
};

//<End of snippet n. 0>
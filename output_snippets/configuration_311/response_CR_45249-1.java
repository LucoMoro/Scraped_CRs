//<Beginning of snippet n. 0>

String args[] = {
"--setuid=1000",
"--setgid=<GID_OF_QCOM_ONCRPC>",
            "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,3001,3002,3003,3006,3007,<GID_OF_QCOM_ONCRPC>",
"--capabilities=130104352,130104352",
"--runtime-init",
"--nice-name=system_server",
};

// Configure permissions for /dev/oncrpc
setPermissions("/dev/oncrpc", "ril-daemon", "<permissions>");

//<End of snippet n. 0>
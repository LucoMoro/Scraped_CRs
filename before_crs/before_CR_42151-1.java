/*zygote: start system_server before class preloading

Class and resource preloading takes significant time (~5s on my
device) is CPU bound and blocks system_server from starting up (which
is mainly I/O bound during initialization).

This patch moves starting the system server before class preloading,
so that we overlap I/O and CPU usage and thus improve boot time. This
saves ~2s from boot time.

Change-Id:Ie5d6b4c196de88b85aa774b4f13fc81448225326Signed-off-by: Octavian Purdila <octavian.purdila@intel.com>
Signed-off-by: Andrew Boie <andrew.p.boie@intel.com>*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/ZygoteInit.java b/core/java/com/android/internal/os/ZygoteInit.java
//Synthetic comment -- index 4924326..10bdc3b 100644

//Synthetic comment -- @@ -515,6 +515,18 @@

public static void main(String argv[]) {
try {
// Start profiling the zygote initialization.
SamplingProfilerIntegration.start();

//Synthetic comment -- @@ -531,17 +543,6 @@
// Do an initial gc to clean up after startup
gc();

            // If requested, start system server directly from Zygote
            if (argv.length != 2) {
                throw new RuntimeException(argv[0] + USAGE_STRING);
            }

            if (argv[1].equals("start-system-server")) {
                startSystemServer();
            } else if (!argv[1].equals("")) {
                throw new RuntimeException(argv[0] + USAGE_STRING);
            }

Log.i(TAG, "Accepting command socket connections");

if (ZYGOTE_FORK_MODE) {








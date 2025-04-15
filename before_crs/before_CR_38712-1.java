/*Fix code problem in writePidDns

When app call startUsingNetworkFeature() to keep alive, there is a
code problem in writePidDns() to update the net.dnsX.<pid> property.

Change-Id:I83b02da4808f106c9ca00e350ad38e4bd5cba689*/
//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index b7dc4a2..29cee1c 100644

//Synthetic comment -- @@ -2158,8 +2158,9 @@
String dnsString = dns.getHostAddress();
if (changed || !dnsString.equals(SystemProperties.get("net.dns" + j + "." + pid))) {
changed = true;
                SystemProperties.set("net.dns" + j++ + "." + pid, dns.getHostAddress());
}
}
return changed;
}








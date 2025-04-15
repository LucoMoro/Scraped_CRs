/*Avoid crash when switching to 2G/3G network.

Adding test that active APN array is not empty.

Change-Id:I16246aa45c991a9396ef03d9bb083d97c3ca2e1a*/




//Synthetic comment -- diff --git a/src/com/android/settings/RadioInfo.java b/src/com/android/settings/RadioInfo.java
//Synthetic comment -- index f0fcdd7..f1c30d0 100644

//Synthetic comment -- @@ -774,15 +774,18 @@
sb.append("\n    to ")
.append(pdp.getApn().toString());
}
                String[] activeApnTypes = phone.getActiveApnTypes();
                if (activeApnTypes.length > 0) {
                    sb.append("\ninterface: ")
                      .append(phone.getInterfaceName(activeApnTypes[0]))
                      .append("\naddress: ")
                      .append(phone.getIpAddress(activeApnTypes[0]))
                      .append("\ngateway: ")
                      .append(phone.getGateway(activeApnTypes[0]));
                    String[] dns = phone.getDnsServers(activeApnTypes[0]);
                    if (dns != null) {
                        sb.append("\ndns: ").append(dns[0]).append(", ").append(dns[1]);
                    }
}
} else if (dc.isInactive()) {
sb.append("    disconnected with last try at ")








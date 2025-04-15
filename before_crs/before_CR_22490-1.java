/*Avoid crash when switching to 2G/3G network.

Adding test that active APN array is not empty.

Change-Id:I16246aa45c991a9396ef03d9bb083d97c3ca2e1a*/
//Synthetic comment -- diff --git a/src/com/android/settings/RadioInfo.java b/src/com/android/settings/RadioInfo.java
//Synthetic comment -- index f0fcdd7..f1c30d0 100644

//Synthetic comment -- @@ -774,15 +774,18 @@
sb.append("\n    to ")
.append(pdp.getApn().toString());
}
                sb.append("\ninterface: ")
                  .append(phone.getInterfaceName(phone.getActiveApnTypes()[0]))
                  .append("\naddress: ")
                  .append(phone.getIpAddress(phone.getActiveApnTypes()[0]))
                  .append("\ngateway: ")
                  .append(phone.getGateway(phone.getActiveApnTypes()[0]));
                String[] dns = phone.getDnsServers(phone.getActiveApnTypes()[0]);
                if (dns != null) {
                    sb.append("\ndns: ").append(dns[0]).append(", ").append(dns[1]);
}
} else if (dc.isInactive()) {
sb.append("    disconnected with last try at ")








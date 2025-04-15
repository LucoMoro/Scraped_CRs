/*NsdManager: Updated documentation to warn usage of listerns implementing
more than one callback listener interface.

Change-Id:I9df2a3e1c338df1e3ff79de88d48c1e8b4756285*/




//Synthetic comment -- diff --git a/core/java/android/net/nsd/NsdManager.java b/core/java/android/net/nsd/NsdManager.java
//Synthetic comment -- index 08ba728..60cd018 100644

//Synthetic comment -- @@ -111,6 +111,10 @@
* resolve is notified on {@link ResolveListener#onServiceResolved} and a failure is notified
* on {@link ResolveListener#onResolveFailed}.
*
 * <p> An application performing multiple operations like discovering and resolving services as
 * well as publishing services should not use the same instance of listener (class implementing
 * more than one listener interface) for receiving callbacks.
 *
* Applications can reserve for a service type at
* http://www.iana.org/form/ports-service. Existing services can be found at
* http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xml








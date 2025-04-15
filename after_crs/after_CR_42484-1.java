/*onDestroy() has to wait for shutdown() to complete

In shutdown() the broadcast receivers belonging to the thread
will be unregistered. All receivers must have been unregistered
when onDestroy returns. So onDestroy has to wait for shutdown
to complete.

Change-Id:Ie1f9dccbd488401418de1cb4cadf488022bb8abe*/




//Synthetic comment -- diff --git a/exchange2/src/com/android/exchange/ExchangeService.java b/exchange2/src/com/android/exchange/ExchangeService.java
//Synthetic comment -- index b1ae3b7..47abe96 100644

//Synthetic comment -- @@ -1914,6 +1914,13 @@
}
}
}});
        // We need to wait for sServiceThread to complete recycling receivers etc.
        try {
            if (sServiceThread != null) {
                sServiceThread.join();
            }
        } catch (InterruptedException e) {
        }
}

void maybeStartExchangeServiceThread() {








/*Merge commit 'remotes/korg/cupcake' into cupcake_to_master*/




//Synthetic comment -- diff --git a/src/org/apache/http/conn/ssl/SSLSocketFactory.java b/src/org/apache/http/conn/ssl/SSLSocketFactory.java
//Synthetic comment -- index 498b43e..1be6c3a 100644

//Synthetic comment -- @@ -217,6 +217,19 @@
}

/**
     * Constructs an HttpClient SSLSocketFactory backed by the given JSSE
     * SSLSocketFactory.
     *
     * @hide
     */
    public SSLSocketFactory(javax.net.ssl.SSLSocketFactory socketfactory) {
        super();
        this.sslcontext = null;
        this.socketfactory = socketfactory;
        this.nameResolver = null;
    }

    /**
* Creates the default SSL socket factory.
* This constructor is used exclusively to instantiate the factory for
* {@link #getSocketFactory getSocketFactory}.








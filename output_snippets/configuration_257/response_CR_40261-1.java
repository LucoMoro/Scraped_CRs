//<Beginning of snippet n. 0>

private static final Map<String, Provider> providersNames = new HashMap<String, Provider>(20);
private static final Object lock = new Object();
private static volatile Provider.Service cachedSecureRandomService = null;
private static final List<Provider> providers = new ArrayList<>();
private static final Map<String, Provider.Service> services = new HashMap<>();
private static boolean needRefresh = false;
private static int cacheVersion = 0;

static {
    loadProviders();
}

private static void loadProviders() {
    synchronized (lock) {
        String providerClassName = null;
        int i = 1;
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        while ((providerClassName = Security.getProperty("security.provider." + i++)) != null) {
            try {
                Class<?> providerClass = Class.forName(providerClassName.trim(), true, cl);
                Provider p = (Provider) providerClass.newInstance();
                providers.add(p);
                providersNames.put(p.getName(), p);
                initServiceInfo(p);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ignored) {
            }
        }
        Engine.door.renumProviders();
    }
}

public static Provider[] getProviders() {
    synchronized (lock) {
        return providers.toArray(new Provider[0]);
    }
}

public static List<Provider> getProvidersList() {
    synchronized (lock) {
        return new ArrayList<>(providers);
    }
}

public static Provider getProvider(String name) {
    if (name == null) {
        return null;
    }
    synchronized (lock) {
        return providersNames.get(name);
    }
}

public static int insertProviderAt(Provider provider, int position) {
    synchronized (lock) {
        int size = providers.size();
        if ((position < 1) || (position > size)) {
            position = size + 1;
        }
        providers.add(position - 1, provider);
        providersNames.put(provider.getName(), provider);
        setNeedRefresh();
        return position;
    }
}

public static void removeProvider(int providerNumber) {
    synchronized (lock) {
        if (providerNumber > 0 && providerNumber <= providers.size()) {
            Provider p = providers.remove(providerNumber - 1);
            providersNames.remove(p.getName());
            setNeedRefresh();
        }
    }
}

public static void initServiceInfo(Provider p) {
    synchronized (lock) {
        for (Provider.Service service : p.getServices()) {
            String type = service.getType();
            if (cachedSecureRandomService == null && type.equals("SecureRandom")) {
                cachedSecureRandomService = service;
            }
            String key = type + "." + service.getAlgorithm().toUpperCase(Locale.US);
            services.putIfAbsent(key, service);
            for (String alias : Engine.door.getAliases(service)) {
                key = type + "." + alias.toUpperCase(Locale.US);
                services.putIfAbsent(key, service);
            }
        }
    }
}

public static boolean isEmpty() {
    synchronized (lock) {
        return services.isEmpty();
    }
}

public static Provider.Service getService(String key) {
    synchronized (lock) {
        return services.get(key);
    }
}

public static Provider.Service getSecureRandomService() {
    getCacheVersion(); // used for side effect of updating cache if needed
    return cachedSecureRandomService;
}

public static void setNeedRefresh() {
    synchronized (lock) {
        needRefresh = true;
    }
}

public static int getCacheVersion() {
    synchronized (lock) {
        if (needRefresh) {
            cacheVersion++;
            services.clear();
            cachedSecureRandomService = null;
            for (Provider p : providers) {
                initServiceInfo(p);
            }
            needRefresh = false;
        }
        return cacheVersion;
    }
}

//<End of snippet n. 0>
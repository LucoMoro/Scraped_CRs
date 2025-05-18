//<Beginning of snippet n. 0>
private static final AtomicReference<Provider.Service> cachedSecureRandomService = new AtomicReference<>();

private static boolean needRefresh;

private static final List<Provider> providers = new CopyOnWriteArrayList<>();
private static final Map<String, Provider> providersNames = new ConcurrentHashMap<>();
private static final Set<String> allowedProviderClassNames = Set.of("com.example.Provider1", "com.example.Provider2");

static {
    loadProviders();
}

private static void loadProviders() {
    String providerClassName;
    int i = 1;
    ClassLoader cl = ClassLoader.getSystemClassLoader();

    while ((providerClassName = Security.getProperty("security.provider." + i++)) != null) {
        if (!allowedProviderClassNames.contains(providerClassName.trim())) {
            continue; 
        }
        try {
            Class<?> providerClass = Class.forName(providerClassName.trim(), true, cl);
            Provider p = (Provider) providerClass.getDeclaredConstructor().newInstance();
            synchronized (providers) {
                providers.add(p);
                providersNames.put(p.getName(), p);
                initServiceInfo(p);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            Logger.getLogger(ClassName.class.getName()).log(Level.SEVERE, "Error loading provider: " + providerClassName, e);
        } 
    }
    Engine.door.renumProviders();
}

public static Provider[] getProviders() {
    return providers.toArray(new Provider[0]);
}

public static List<Provider> getProvidersList() {
    return new ArrayList<>(providers);
}

public static Provider getProvider(String name) {
    if (name == null) {
        return null;
    }
    return providersNames.get(name);
}

public static int insertProviderAt(Provider provider, int position) {
    synchronized (providers) {
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
    synchronized (providers) {
        Provider p = providers.remove(providerNumber - 1);
        providersNames.remove(p.getName());
        setNeedRefresh();
    }
}

public static void initServiceInfo(Provider p) {
    for (Provider.Service service : p.getServices()) {
        String type = service.getType();
        if (cachedSecureRandomService.get() == null && type.equals("SecureRandom")) {
            cachedSecureRandomService.set(service);
        }
        String key = type + "." + service.getAlgorithm().toUpperCase(Locale.US);
        synchronized (services) {
            if (!services.containsKey(key)) {
                services.put(key, service);
            }
        }
        for (String alias : Engine.door.getAliases(service)) {
            key = type + "." + alias.toUpperCase(Locale.US);
            synchronized (services) {
                if (!services.containsKey(key)) {
                    services.put(key, service);
                }
            }
        }
    }
}

public static boolean isEmpty() {
    return services.isEmpty();
}

public static Provider.Service getService(String key) {
    return services.get(key);
}

public static Provider.Service getSecureRandomService() {
    getCacheVersion(); 
    return cachedSecureRandomService.get();
}

public static void setNeedRefresh() {
    synchronized (providers) {
        needRefresh = true;
    }
}

public static int getCacheVersion() {
    synchronized (providers) {
        if (needRefresh) {
            cacheVersion++;
            synchronized (services) {
                services.clear();
            }
            cachedSecureRandomService.set(null);
            for (Provider p : providers) {
                initServiceInfo(p);
            }
            needRefresh = false;
        }
        return cacheVersion;
    }
}
//<End of snippet n. 0>
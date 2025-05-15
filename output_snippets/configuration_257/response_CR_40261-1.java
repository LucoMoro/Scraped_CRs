//<Beginning of snippet n. 0>
private static final Map<String, Provider> providersNames = new ConcurrentHashMap<>(20);
private static final List<Provider> providers = new CopyOnWriteArrayList<>();
private static final Map<String, Provider.Service> services = new ConcurrentHashMap<>();
private static AtomicReference<Provider.Service> cachedSecureRandomService = new AtomicReference<>(null);
private static volatile boolean needRefresh = false;
private static volatile int cacheVersion = 0;

static {
    loadProviders();
}

private static void loadProviders() {
    String providerClassName = null;
    int i = 1;
    ClassLoader cl = ClassLoader.getSystemClassLoader();

    while ((providerClassName = Security.getProperty("security.provider." + i++)) != null) {
        try {
            Class<?> providerClass = Class.forName(providerClassName.trim(), true, cl);
            Provider p = (Provider) providerClass.getDeclaredConstructor().newInstance();
            providers.add(p);
            providersNames.put(p.getName(), p);
            initServiceInfo(p);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ignored) {
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
    int size = providers.size();
    if ((position < 1) || (position > size)) {
        position = size + 1;
    }
    providers.add(position - 1, provider);
    providersNames.put(provider.getName(), provider);
    setNeedRefresh();
    return position;
}

public static void removeProvider(int providerNumber) {
    Provider p = providers.remove(providerNumber - 1);
    providersNames.remove(p.getName());
    setNeedRefresh();
}

public static void initServiceInfo(Provider p) {
    for (Provider.Service service : p.getServices()) {
        String type = service.getType();
        cachedSecureRandomService.updateAndGet(current -> (current == null && type.equals("SecureRandom")) ? service : current);
        
        String key = type + "." + service.getAlgorithm().toUpperCase(Locale.US);
        services.putIfAbsent(key, service);
        for (String alias : Engine.door.getAliases(service)) {
            key = type + "." + alias.toUpperCase(Locale.US);
            services.putIfAbsent(key, service);
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
    getCacheVersion();  // used for side effect of updating cache if needed
    return cachedSecureRandomService.get();
}

public static void setNeedRefresh() {
    needRefresh = true;
}

public static int getCacheVersion() {
    if (needRefresh) {
        cacheVersion++;
        services.clear();
        cachedSecureRandomService.set(null);
        for (Provider p : providers) {
            initServiceInfo(p);
        }
        needRefresh = false;
    }
    return cacheVersion;
}
//<End of snippet n. 0>
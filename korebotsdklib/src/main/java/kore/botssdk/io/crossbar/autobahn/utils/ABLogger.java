package kore.botssdk.io.crossbar.autobahn.utils;

public class ABLogger {

    private static boolean isAndroid() {
        String vendor = System.getProperty("java.vendor");
        return vendor != null && vendor.equals("The Android Project");
    }

    public static IABLogger getLogger(String tag) {
        Class<?> loggerClass;

        try {
            if (isAndroid()) {
                loggerClass = Class.forName("kore.botssdk.io.crossbar.autobahn.utils.ABALogger");
            } else {
                loggerClass = Class.forName("kore.botssdk.io.crossbar.autobahn.utils.ABJLogger");
            }

            return (IABLogger) loggerClass.getConstructor(String.class).newInstance(tag);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

package me.offeex.bloomware.api.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassUtil {
    public static ArrayList<Class<?>> getClassesOther(String packageName, Class<?> assignableFrom) throws IOException, ClassNotFoundException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            try {
                dirs.add(new File(resource.toURI()));
            } catch (IllegalArgumentException e) {
                dirs.add(new File(resource.getFile()));
            }
        }
        ArrayList<Class<?>> classes = new ArrayList<>();

        dirs.forEach(directory -> {
            try {
                classes.addAll(findClasses(directory, packageName, assignableFrom));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName, Class<?> assignableFrom) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) return classes;
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName(), assignableFrom));
            } else if (file.getName().endsWith(".class")) {
                Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                if (assignableFrom == null || assignableFrom.isAssignableFrom(clazz)) classes.add(clazz);
            }
        }
        return classes;
    }
}

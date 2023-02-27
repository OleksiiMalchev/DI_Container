package com.malchev.spring.scanner;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class BeanScannerImpl implements BeanScanner {
    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. " +
            "Are you sure the package '%s' exists?";
    private  ScanConfig scanConfig = new ScanConfigImpl();
    private List<Class<?>> classesInPackage = new ArrayList<>();


    public List<Class<?>> findClassInPackage() {
        String scannedPath = scanConfig.getPackage().replace('.', '/');
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scanConfig.getPackage()));
        }
        File scannedDir = new File(scannedUrl.getFile());
        for (File file : scannedDir.listFiles()) {
            classesInPackage.addAll(find(file, scanConfig.getPackage()));
        }
        return classesInPackage;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + '.' + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(".class")) {
            int endIndex = resource.length() - ".class".length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }
}


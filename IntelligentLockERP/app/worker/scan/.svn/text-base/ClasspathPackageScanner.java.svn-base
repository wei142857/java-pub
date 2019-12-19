package worker.scan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import play.Logger;


public class ClasspathPackageScanner  {
    private String basePackage;
    private ClassLoader cl;
 
    public ClasspathPackageScanner(String basePackage) {
        this.basePackage = basePackage;
        this.cl = getClass().getClassLoader();
 
    }
 
    public ClasspathPackageScanner(String basePackage, ClassLoader cl) {
        this.basePackage = basePackage;
        this.cl = cl;
    }
 
    public List<Class<?>> getClasses() throws IOException, ClassNotFoundException {
        return doScan(basePackage, new ArrayList<Class<?>>());
    }
 
    private List<Class<?>> doScan(String basePackage, List<Class<?>> classList) throws IOException, ClassNotFoundException {
        String splashPath = ScanUtil.dotToSplash(basePackage);
        URL url = cl.getResource(splashPath);
        String filePath = ScanUtil.getRootPath(url);
        List<String> names = null;
        if (isJarFile(filePath)) {
        	names = readFromJarFile(filePath, splashPath);
        } else {
            names = readFromDirectory(filePath);
        }
 
        for (String name : names) {
            if (isClassFile(name)) {
                classList.add(Class.forName(toFullyQualifiedName(name, basePackage)));
            } else {
                doScan(basePackage + "." + name, classList);
            }
        }
 
        for (Class<?> n : classList) {
            Logger.info("找到{}"+ n.getName());
        }
 
        return classList;
    }
 
    /**
     * Convert short class name to fully qualified name.
     * e.g., String -> java.lang.String
     */
    private String toFullyQualifiedName(String shortName, String basePackage) {
        StringBuilder sb = new StringBuilder(basePackage);
        sb.append('.');
        sb.append(ScanUtil.trimExtension(shortName));
 
        return sb.toString();
    }
 
    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException, ClassNotFoundException {
        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();
 
        List<String> nameList = new ArrayList<String>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
            	int pos = name.lastIndexOf("/");
            	nameList.add(name.substring(pos+1));
            }
 
            entry = jarIn.getNextJarEntry();
        }
 
        return nameList;
    }
 
    private List<String> readFromDirectory(String path) {
        File file = new File(path);
        String[] names = file.list();
 
        if (null == names) {
            return null;
        }
 
        return Arrays.asList(names);
    }
 
    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }
 
    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }
 
    /**
     * For test purpose.
     */
    public static void main(String[] args) throws Exception {
    	ClasspathPackageScanner scan = new ClasspathPackageScanner("work.blacklist.strategy");
        List<Class<?>> list = scan.getClasses();
        System.out.println(list.size());
    }
 
}
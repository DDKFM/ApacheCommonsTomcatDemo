package de.mschaedlich.apache.commons.io.demo; /**
 * Created by MAXIMILIAN on 17.04.2017.
 */

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import sun.reflect.generics.tree.Tree;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@WebListener()
public class FileSystemListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    private static TreeMap<Date, String> logs = new TreeMap<Date, String>();
    private static FileAlterationMonitor monitor = null;
    private static String cloudUrl = "";
    public FileSystemListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        if(System.getProperty("cloud.url") != null)
            cloudUrl = System.getProperty("cloud.url");
        if(System.getProperty("monitoredDirectory") != null) {
            File parentFile = new File(System.getProperty("monitoredDirectory"));
            if(parentFile.exists()) {
                if (monitor == null) {
                    FileAlterationObserver observer = new FileAlterationObserver(parentFile.getAbsolutePath());
                    observer.addListener(new FileAlterationListener() {
                        @Override
                        public void onStart(FileAlterationObserver fileAlterationObserver) {

                        }

                        @Override
                        public void onDirectoryCreate(File file) {
                            logs.put(new Date(), "Ordner: " + file.getName() + " erstellt");
                        }

                        @Override
                        public void onDirectoryChange(File file) {
                            logs.put(new Date(), "Ordner: " + file.getName() + " verändert");
                        }

                        @Override
                        public void onDirectoryDelete(File file) {
                            logs.put(new Date(), "Ordner: " + file.getName() + " gelöscht");
                        }

                        @Override
                        public void onFileCreate(File file) {
                            logs.put(new Date(), "Datei: " + file.getName() + " erstellt");
                        }

                        @Override
                        public void onFileChange(File file) {
                            logs.put(new Date(), "Datei: " + file.getName() + " verändert");
                        }

                        @Override
                        public void onFileDelete(File file) {
                            logs.put(new Date(), "Datei: " + file.getName() + " gelöscht");
                        }

                        @Override
                        public void onStop(FileAlterationObserver fileAlterationObserver) {

                        }
                    });
                    monitor = new FileAlterationMonitor(500,observer);
                    try {
                        monitor.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        if(monitor != null)
            try {
                monitor.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
      /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
      /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attibute
         is replaced in a session.
      */
    }
    public static TreeMap<Date, String> getLogs() {
        return FileSystemListener.logs;
    }
    public static String getCloudUrl() {
        return FileSystemListener.cloudUrl;
    }
}

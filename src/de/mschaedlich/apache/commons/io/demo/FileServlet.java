package de.mschaedlich.apache.commons.io.demo;

import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by MAXIMILIAN on 17.04.2017.
 */
@WebServlet(name = "FileServlet", urlPatterns = "/file")
public class FileServlet extends HttpServlet {
    private static List<File> files = new ArrayList<File>();
    private static File parentFile = null;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        if(System.getProperty("monitoredDirectory") != null) {
            String monitoredDirectory = System.getProperty("monitoredDirectory");
            parentFile = new File(monitoredDirectory);
        }
        if (parentFile != null && parentFile.exists()) {
            if(request.getParameter("method") != null) {
                switch(request.getParameter("method").toLowerCase().trim()) {
                    case "files":
                        response.setContentType("text/html");
                        File[] childFiles = parentFile.listFiles();
                        for(File file : childFiles) {
                            writer.append("<div id=\"fileEntry\">");
                            Map<String, String> iconMap = new HashMap<String, String>();
                            String extension = FilenameUtils.getExtension(file.getName());
                            iconMap.put("txt", "fa-file-o");
                            iconMap.put("java", "fa-file-code-o");
                            String icon = iconMap.containsKey(extension) ? iconMap.get(extension) : "";
                            writer.append("<i class=\"fa " + icon + "\"></i>");
                            writer.append("<a href=\"javascript:loadFileContent('" + file.getName() + "');\">" + file.getName() + "</a>");
                            writer.append("</div>");
                        }
                        break;
                    case "filecontent":
                        if(request.getParameter("filename") != null) {
                            String filename = request.getParameter("filename");
                            File childFile = new File(parentFile.getAbsolutePath() + File.separator + filename);
                            if(childFile != null && childFile.exists()) {
                                byte[] fileData = Files.readAllBytes(childFile.toPath());
                                writer.append(new String(fileData));
                            }
                        }
                        break;
                    case "log":
                        response.setContentType("text/html");
                        TreeMap<Date, String> logs = FileSystemListener.getLogs();

                        int countEntries = 0;

                        for (Date key : logs.keySet()) {
                            if(countEntries > 20)
                                break;
                            String formatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(key);
                            writer.append("<div id=\"logEntry\">" + countEntries + ": " + formatedDate + ": " + logs.get(key) + "</div>");
                            countEntries++;
                        }
                        break;
                }
            }
        } else {
            writer.append("<h1>Kein Ordner gefunden</h1>");
        }
    }
}

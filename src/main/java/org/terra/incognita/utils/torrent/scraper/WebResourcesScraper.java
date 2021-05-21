package org.terra.incognita.utils.torrent.scraper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.*;

/**
 * Extract URL torrent links from a Reader.
 *
 * <p>Hipotesis:  An url couldn't extends over several lines</p>
 */
public class WebResourcesScraper {

    public static final String RESOURCE_URL_END_TOKEN = ".torrent'"; // attribute delimiter could be " or '
    public static final String URL_END_TOKEN = "'";
    public static final String [] URL_BEGIN_TOKEMS  = { "'http://", "'https://","'//"};
    public static Logger log = LogManager.getLogger(WebResourcesScraper.class);

    public static List<WebResourceURL>  extractURLFromReader(Reader reader, boolean download, String destDirPath)
            throws IOException {
        List<WebResourceURL> result = new CopyOnWriteArrayList<>();
        BufferedReader breader = new BufferedReader(reader);

        File destDir = new File(destDirPath);
        if ( destDir == null || !destDir.isDirectory() ) {
            System.out.println("No existe el directorio destino");
            return result;
        }

        breader.lines().parallel().forEach( line -> {
                int index = 0;
                // Fist we check if there is a torrent url
                if ( ( index = line.indexOf(RESOURCE_URL_END_TOKEN)) != -1 ) {
                    // FIXME: Change to log
                    // System.out.println("index: " + index);
                    // We can find at least one resource url, or maybe more....
                    int beginIndex = -1;
                    int beginTokenLength = -1;
                    while ( index+RESOURCE_URL_END_TOKEN.length() <= line.length() ) {
                        for(String token: URL_BEGIN_TOKEMS) {
                            if ( (beginIndex=line.lastIndexOf(token,index)) != -1 ) {
                                beginTokenLength = token.length();
                                break;
                            }
                        };
                        // FIXME: Change to log
                        // System.out.println("beginIndex: " + beginIndex);
                        if ( beginIndex != -1 ) {
                            int endIndex = index + RESOURCE_URL_END_TOKEN.length();

                            // System.out.println(" beginIndex: " + beginIndex + " endIndex: " + endIndex);
                            if (line.substring(beginIndex, endIndex).endsWith(RESOURCE_URL_END_TOKEN)) {
                                // Found an url torrent
                                String url = line.substring(beginIndex + 1, endIndex - 1); // Remove the quotes
                                WebResourceURL resource = new WebResourceURL(url);
                                result.add(resource);
                                if ( download ) {
                                    // I need to extract the filename
                                    String filename = url.substring(url.lastIndexOf("/")+1,url.length());
                                    System.out.println("https:" + resource.getUrl().trim() + (destDirPath + File.separator + filename));
                                    URL uurl = null;
                                    try {
                                        uurl = new URL("https:" + resource.getUrl().trim());
                                        FileUtils.copyURLToFile(uurl, new File(destDirPath + File.separator + filename));
                                    } catch ( IOException ioe) {};
                                }
                                index = endIndex+1;
                            } else {
                                // It isn't an url resource
                                index++;
                            }
                        } else {
                            // No begin url, so no more urls
                            break; // Next line
                        }
                    }
                }
            });
        return result;
    }

    public static List<WebResourceURL>  extractURLFromReader(Reader reader) throws IOException {
        return extractURLFromReader(reader,false,null);
    }

        /**
         * Extract resource urls from text files (mainly html files) in a directory
         *
         * @param dirPath   Teh path of the directory to scan.
         * @param download  If the resources extracted must be downloaded
         * @return An array of resource urls
         *
         * @throws IOException
         */
    public static List<WebResourceURL>  extractURLFromDir(String dirPath, boolean download, String destDirPath) throws IOException {
        List<WebResourceURL> result = new CopyOnWriteArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(dirPath))) {
            walk.parallel().filter(Files::isRegularFile).forEach( file -> {
                FileReader f = null;
                System.out.println(" Escaneando el archivo: " + file.toString());
                try {
                    f = new FileReader(file.toFile());
                    if ( f != null ) {
                        result.addAll(WebResourcesScraper.extractURLFromReader(f,download, destDirPath));
                    }
                } catch ( IOException ioe ) {};
            });
        }
        return result;
    }

    public static List<WebResourceURL>  extractURLFromDir(String dirPath) throws IOException {
        return extractURLFromDir(dirPath,false,null);

    }
}

package org.terra.incognita.utils.torrent.scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.*;

/**
 * Extract URL torrent links from a Reader.
 *
 * <p>An url couldn't extends over several lines</p>
 */
public class WebResourcesScraper {

    public static final String RESOURCE_URL_END_TOKEN = ".torrent\"";
    public static final String URL_END_TOKEN = "\"";
    public static final String URL_BEGIN_TOKEM = "\"https://";
    public static Logger log = LogManager.getLogger(WebResourcesScraper.class);

    public static WebResourceURL[]  extractURLFromReader(Reader reader) throws IOException {
        List<WebResourceURL> result = new ArrayList<>();
        BufferedReader breader = new BufferedReader(reader);
        String line = null;
        int index = 0;
        while ( (line = breader.readLine()) != null ) {
            // Fist we check if there is a borrent url
            if ( ( index = line.indexOf(RESOURCE_URL_END_TOKEN)) != -1 ) {
                // We can find at least one resource url, or maybe more....
                while ( index < line.length() ) {
                    int beginIndex = line.indexOf(URL_BEGIN_TOKEM,index);
                    if ( beginIndex != -1 ) {
                        int endIndex = line.indexOf(URL_END_TOKEN, index + URL_BEGIN_TOKEM.length());
                        if ( endIndex != -1 ) {
                            if (line.substring(beginIndex, endIndex).endsWith(RESOURCE_URL_END_TOKEN)) {
                                // Found an url torrent
                                String url = line.substring(beginIndex + 1, endIndex - 1); // Remove the quotes
                                WebResourceURL resource = new WebResourceURL(url);
                                result.add(resource);
                                index = endIndex+1;
                            } else {
                                // It isn't an url resource
                                index++;
                            }
                        } else {
                            // There isn't end of the url
                            index++;
                        }
                    } else {
                        // No begin url, so no more urls
                        continue; // Next line
                    }
                }
            }
        }
        return (WebResourceURL[] )result.toArray( new WebResourceURL[result.size()]);
    }

}

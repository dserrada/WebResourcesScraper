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

    public static final String RESOURCE_URL_END_TOKEN = ".torrent'"; // attribute delimiter could be " or '
    public static final String URL_END_TOKEN = "'";
    public static final String [] URL_BEGIN_TOKEMS  = { "'http://", "'https://","'//"};
    public static Logger log = LogManager.getLogger(WebResourcesScraper.class);

    public static WebResourceURL[]  extractURLFromReader(Reader reader) throws IOException {
        List<WebResourceURL> result = new ArrayList<>();
        BufferedReader breader = new BufferedReader(reader);
        String line = null;
        int index = 0;
        while ( (line = breader.readLine()) != null ) {
            // Fist we check if there is a torrent url
            if ( ( index = line.indexOf(RESOURCE_URL_END_TOKEN)) != -1 ) {
                // FIXME: Change to log
                System.out.println("index: " + index);
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
                    System.out.println("beginIndex: " + beginIndex);
                    if ( beginIndex != -1 ) {
                        int endIndex = index + RESOURCE_URL_END_TOKEN.length();

                        System.out.println(" beginIndex: " + beginIndex + " endIndex: " + endIndex);
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
                        // No begin url, so no more urls
                        break; // Next line
                    }
                }
            }
        }
        return (WebResourceURL[] )result.toArray( new WebResourceURL[result.size()]);
    }

}

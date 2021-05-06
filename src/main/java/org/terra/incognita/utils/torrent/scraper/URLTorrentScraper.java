package org.terra.incognita.utils.torrent.scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.*;

/**
 * Extract URL torrent links from a Reader.
 *
 * <p>An url coudn't extends over several lines</p>
 */
public class URLTorrentScraper {

    public static final String TORRENT_URL_END_TOKEN = ".torrent\"";
    public static final String URL_END_TOKEN = "\"";
    public static final String URL_BEGIN_TOKEM = "\"https://";
    public static Logger log = Logger.getLogger(URLTorrentScraper.class);

    public TorrentURL []  extractURLFromReader(Reader reader) throws IOException {
        List<TorrentURL> result = new ArrayList<>();
        BufferedReader breader = new BufferedReader(rader);
        String line = null;
        while ( (line = breader.readLine()) != null ) {
            // Fist we check if there is a borrent url
            if ( line.indexOf(TORRENT_URL_END_TOKEN) != -1 ) {
                // We can find at least one torrent url, or maybe more....
                int index = 0;
                while ( index < line.length() ) {
                    int beginIndex = line.indexOf(URL_BEGIN_TOKEM,index);
                    int endIndex = line.indexOf(URL_END_TOKEN,index+URL_BEGIN_TOKEM.length());
                    if ( line.substring(beginIndex,endIndex).endsWith(TORRENT_URL_END_TOKEN) ) {
                        // Found an url torrent
                        String url = line.substring(beginIndex + 1, endIndex - 1); // Remove the quotes
                    }
                }
            }
        }
    }
}

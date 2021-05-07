package org.terra.incognita.utils.torrent.scraper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class WebResourcesScraperTest {

    private String [] testHTML =  {
            "one",
            "<td><a  class=\"text-white bg-primary rounded-pill d-block shadow-sm text-decoration-none my-1 py-1\" style=\"font-size: 18px; font-weight: 500;\" href='//blazing.network/torrents/series/150138_-1619837733-La-Costa-De-Los-Mosquitos---Temporada-1--HDTV-720p-AC3-5-2.torrent' download>Descargar</a></td>"
    } ;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void extractURLFromReader() {
        for(String u: testHTML) {
            try {
                WebResourceURL[] found = WebResourcesScraper.extractURLFromReader(new StringReader(u));
                System.out.println("Urls#: " + found.length);
            } catch ( IOException ioe ) {
                ioe.printStackTrace();;
            }
        }
    }
}
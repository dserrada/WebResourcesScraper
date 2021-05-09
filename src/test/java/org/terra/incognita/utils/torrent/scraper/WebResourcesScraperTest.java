package org.terra.incognita.utils.torrent.scraper;

import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;


import static org.junit.jupiter.api.Assertions.*;

class WebResourcesScraperTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void extractURLFromReader1() {
        final String test = "one";
        assertDoesNotThrow( () -> {
            WebResourceURL[] found = WebResourcesScraper.extractURLFromReader(new StringReader(test));
            Assert.isEmpty(found);
        });
    }

    @Test
    void extractURLFromReader2() {
        final String test = "<td><a  class=\"text-white bg-primary rounded-pill d-block shadow-sm text-decoration-none my-1 py-1\" style=\"font-size: 18px; font-weight: 500;\" href='//blazing.network/torrents/series/150138_-1619837733-La-Costa-De-Los-Mosquitos---Temporada-1--HDTV-720p-AC3-5-2.torrent' download>Descargar</a></td>";
        assertDoesNotThrow( () -> {
            WebResourceURL[] found = WebResourcesScraper.extractURLFromReader(new StringReader(test));
            Assert.isEmpty(found);
        });
    }

}
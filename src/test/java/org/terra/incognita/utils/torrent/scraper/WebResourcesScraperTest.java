package org.terra.incognita.utils.torrent.scraper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WebResourcesScraperTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void extractURLFromReaderString_1() {
        final String test = "one";
        assertDoesNotThrow( () -> {
            WebResourceURL[] found = WebResourcesScraper.extractURLFromReader(new StringReader(test));
            assertTrue(Arrays.asList(found).isEmpty());
        });
    }

    @Test
    void extractURLFromReaderString_2() {
        final String test = "<td><a  class=\"text-white bg-primary rounded-pill d-block shadow-sm text-decoration-none my-1 py-1\" style=\"font-size: 18px; font-weight: 500;\" href='//blazing.network/torrents/series/150138_-1619837733-La-Costa-De-Los-Mosquitos---Temporada-1--HDTV-720p-AC3-5-2.torrent' download>Descargar</a></td>";
        assertDoesNotThrow( () -> {
            WebResourceURL[] found = WebResourcesScraper.extractURLFromReader(new StringReader(test));
            assertFalse(Arrays.asList(found).isEmpty());
            assertEquals(found.length,1);
        });
    }

    @Test
    void extractURLFromReaderFile_1() {
        assertDoesNotThrow( () -> {
            Reader fileReader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("Saga-James-Bond"));
            WebResourceURL[] found = WebResourcesScraper.extractURLFromReader(fileReader);
            assertFalse(Arrays.asList(found).isEmpty());
            assertEquals(found.length,1);
            for(WebResourceURL resource : found) {
                System.out.println("url: " + resource.getUrl());
            }
        });
    }

    @Test
    void extractURLFromReaderDir_1() {
        System.out.println("Entrando");
        assertDoesNotThrow( () -> {
            try (Stream<Path> walk = Files.walk(Paths.get("/home/john/dontorrents.one"))) {
                walk.parallel().filter(Files::isRegularFile).forEach( file -> {
                    FileReader f = null;
                    try {
                        f = new FileReader(file.toFile());
                        if ( f != null ) {
                            WebResourceURL[] found = WebResourcesScraper.extractURLFromReader(f);
                            for(WebResourceURL resource : found) {
                                System.out.println(resource.getUrl());
                            }
                        }
                    } catch ( IOException ioe ) {};
                });
            }
        });

    }


}
package com.vv.personal.twm.render.engine;

import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.vv.personal.twm.render.engine.tw.ParseTribalWars.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Vivek
 * @since 29/11/20
 */
@RunWith(JUnit4.class)
public class ParseTribalWarsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseTribalWarsTest.class);

    public static String readFileFromLocation(String src) {
        StringBuilder data = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(new File(src)))) {
            String line;
            while ((line = in.readLine()) != null) data.append(line.trim()).append("\n");
        } catch (IOException e) {
            LOGGER.error("Failed to read file contents from '{}'. ", src, e);
        }
        //LOGGER.info("Data read in => \n{}", data);
        return data.toString();
    }

    @Test
    public void testGenerateVillaListBuilder() {
        String html = readFileFromLocation("src/test/resources/tw_overview.html");
        VillaProto.VillaList villaList = generateVillaList(html);
        LOGGER.info("{}", villaList);
        assertEquals(13, villaList.getVillasCount());
        assertTrue(villaList.getVillasList().stream().anyMatch(villa -> villa.getName().contains("Mahakal")));
        assertTrue(villaList.getVillasList().stream().anyMatch(villa -> villa.getName().contains("Bhoot")));
    }

    @Test
    public void testExtractWallInfo() {
        String html = readFileFromLocation("src/test/resources/tw_wall.html");
        int wallLevel = extractWallInfo(html);
        assertEquals(20, wallLevel);
    }

    @Test
    public void testExtractNoblemenInfo() {
        String html = readFileFromLocation("src/test/resources/tw_snob.html");
        int noblemen = extractNoblemenInfo(html);
        assertEquals(4, noblemen);
    }

    @Test
    public void testExtractNoblemenInfo2() {
        String html = readFileFromLocation("src/test/resources/tw_snob2.html");
        int noblemen = extractNoblemenInfo(html);
        assertEquals(0, noblemen);
    }

    @Test
    public void testExtractNoblemenInfo3() {
        String html = readFileFromLocation("src/test/resources/tw_snob3.html");
        int noblemen = extractNoblemenInfo(html);
        assertEquals(0, noblemen);
    }

    @Test
    public void testExtractNoblemenInfo4() {
        String html = readFileFromLocation("src/test/resources/tw_snob4.html");
        int noblemen = extractNoblemenInfo(html);
        assertEquals(1, noblemen);
    }

    @Test
    public void testExtractTroopsInfo() {
        String html = readFileFromLocation("src/test/resources/tw_train.html");
        VillaProto.Troops troops = extractTroopsInfo(html,
                extractWallInfo(readFileFromLocation("src/test/resources/tw_wall.html")),
                extractNoblemenInfo(readFileFromLocation("src/test/resources/tw_snob.html")));

        System.out.println(troops);
        assertEquals(5205, troops.getAx());
        assertEquals(2392, troops.getLc());
        assertEquals(352, troops.getRm());
        assertEquals(101, troops.getCt());
        assertEquals(0, troops.getHc());
    }

    @Test
    public void testExtractTroopsInfo2() {
        String html = readFileFromLocation("src/test/resources/tw_train2.html");
        VillaProto.Troops troops = extractTroopsInfo(html,
                extractWallInfo(readFileFromLocation("src/test/resources/tw_wall.html")),
                extractNoblemenInfo(readFileFromLocation("src/test/resources/tw_snob.html")));

        System.out.println(troops);
        assertEquals(1018, troops.getLc());
    }

}
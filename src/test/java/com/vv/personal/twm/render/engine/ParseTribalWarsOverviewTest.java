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

import static com.vv.personal.twm.render.engine.ParseTribalWarsOverview.generateVillaList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Vivek
 * @since 29/11/20
 */
@RunWith(JUnit4.class)
public class ParseTribalWarsOverviewTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseTribalWarsOverviewTest.class);

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
}
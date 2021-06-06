package com.vv.personal.twm.render.engine;

import com.vv.personal.twm.artifactory.generated.tw.SupportReportProto;
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
import java.util.List;

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

    @Test
    public void testExtractFarmStrength() {
        String html = readFileFromLocation("src/test/resources/tw_farm1.html");
        String farmStrength = extractFarmStrengthInfoFromHtml(html);
        System.out.println(farmStrength);
        assertEquals("24000/24000", farmStrength);

        VillaProto.Villa villa = inflateFarmStrengthInfo(farmStrength);
        System.out.println(villa);
        assertEquals("24000/24000", villa.getFarmStrength());
    }

    @Test
    public void testExtractSupportAcquired() {
        String html = readFileFromLocation("src/test/resources/tw.reps.support.acquired.html");
        SupportReportProto.SupportReport supportReport = extractSupportDetails(html);

        System.out.println(supportReport);
        assertEquals(SupportReportProto.SupportReportType.ACQUIRED, supportReport.getSupportReportType());
        assertEquals("Oldmangramps", supportReport.getFrom());
        assertEquals("vivekthewarrior", supportReport.getTo());
        assertEquals(425, supportReport.getTroops().getHc());
        assertEquals("Jan 20, 2021 11:26:02:423", supportReport.getReportTime());
        assertEquals("Oldmangramps supports FO.SER.R73.Bakasura (584|598) K55", supportReport.getReportSubject());

        html = readFileFromLocation("src/test/resources/tw.reps.support.sent.back.html");
        supportReport = extractSupportDetails(html);
        System.out.println(supportReport);
        assertEquals(SupportReportProto.SupportReportType.SENT_BACK, supportReport.getSupportReportType());
        assertEquals("vivekthewarrior", supportReport.getFrom());
        assertEquals("Oldmangramps", supportReport.getTo());
        assertEquals(410, supportReport.getTroops().getHc());
        assertEquals("Jan 20, 2021 15:32:52", supportReport.getReportTime());
        assertEquals("FO.SER.R73.Bakasura (584|598) K55 has sent the support from Gramps 013 back home", supportReport.getReportSubject());
    }

    @Test
    public void testExtractSupportReportLinks() {
        String html = readFileFromLocation("src/test/resources/tw.reps.support.links.1.html");
        List<String> list = extractSupportReportLinks(html);

        System.out.println(list);
        assertEquals(12, list.size());
        assertEquals("/game.php?village=11639&screen=report&mode=support&group_id=0&view=14191188", list.get(1));

        html = readFileFromLocation("src/test/resources/tw.reps.support.links.2.html");
        list = extractSupportReportLinks(html);

        System.out.println(list);
        assertEquals(12, list.size());
        assertEquals("/game.php?village=11639&screen=report&mode=support&group_id=0&view=14191167", list.get(1));
    }

    @Test
    public void testExtractSupportReportPagesLinks() {
        String html = readFileFromLocation("src/test/resources/tw.reps.support.links.1.html");
        List<String> list = extractSupportReportPagesLinks(html);

        System.out.println(list);
        assertEquals(9, list.size());
        assertEquals("/game.php?village=11639&screen=report&mode=support&from=48", list.get(3));
    }

    @Test
    public void testExtractCoinMintingCapacityDetails() {
        String html = readFileFromLocation("src/test/resources/tw.snob.coin.minter.html");
        VillaProto.Villa.Builder villa = extractCoinMintingCapacity(html);
        System.out.println(villa);
        assertEquals(8, villa.getCoinMintingCapacity());
        assertEquals("24000/24000", villa.getFarmStrength());
        assertEquals(190534L, villa.getResources().getCurrentWood());
        assertEquals(400000L, villa.getResources().getWarehouseCapacity());

        html = readFileFromLocation("src/test/resources/tw.snob.coin.minter2.html");
        villa = extractCoinMintingCapacity(html);
        System.out.println(villa);
        assertEquals(0, villa.getCoinMintingCapacity());
        assertEquals("20649/24000", villa.getFarmStrength());
        assertEquals(400000L, villa.getResources().getWarehouseCapacity());
    }

    @Test
    public void testExtractMarketInfo() {
        String html = readFileFromLocation("src/test/resources/tw.market.create_offers.html");
        VillaProto.Villa villaInfo = extractMarketInfoFromCreateOffers(html);

        System.out.println(villaInfo);
        assertEquals(46, villaInfo.getAvailableMerchants());
        assertEquals(9126, villaInfo.getResources().getCurrentWood());
        assertEquals(117887, villaInfo.getResources().getCurrentClay());
        assertEquals(14788, villaInfo.getResources().getCurrentIron());
    }

}
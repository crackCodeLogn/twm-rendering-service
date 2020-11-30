package com.vv.personal.twm.render.engine;

import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.vv.personal.twm.render.constants.Constants.*;

/**
 * @author Vivek
 * @since 29/11/20
 */
public class ParseTribalWarsOverview {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseTribalWarsOverview.class);

    public static VillaProto.VillaList generateVillaList(String overviewHtml) {
        VillaProto.VillaList.Builder villaListBuilder = VillaProto.VillaList.newBuilder();

        Document document = Jsoup.parse(overviewHtml);
        String world = document.getElementsByTag(TAG_LINK)
                .stream()
                .filter(line -> line.attr(ATTR_HREF).contains(ASSETS))
                .findFirst()
                .get().attr(ATTR_HREF); //https://dsen.innogamescdn.com/assets/enp9/fd6099770c4ee979ef4475c23d321680/merged/game.css
        world = world.substring(world.indexOf(ASSETS) + ASSETS.length());
        world = world.substring(0, world.indexOf('/'));
        String finalWorld = world;

        document.getElementsByClass(CLASS_QUICKEDIT_CONTENT)
                .forEach(element -> {
                    VillaProto.Villa.Builder villa = VillaProto.Villa.newBuilder();
                    String villaTitle = element.text();
                    String name = villaTitle.substring(0, villaTitle.indexOf(CHAR_SPACE));
                    String[] coord = villaTitle.substring(villaTitle.indexOf(CHAR_BRACE_START) + 1, villaTitle.indexOf(CHAR_BRACE_END)).split(COORD_SEPARATOR);
                    int x = Integer.parseInt(coord[0]), y = Integer.parseInt(coord[1]);
                    String id = element.getElementsByTag(TAG_A).get(0).attr(ATTR_HREF); //game.php?village=10546&amp;screen=overview
                    id = id.substring(id.indexOf(CHAR_EQUAL) + 1, id.indexOf(CHAR_AND));

                    villa.setX(x);
                    villa.setY(y);
                    villa.setName(name);
                    villa.setId(id);
                    villa.setWorld(finalWorld);
                    villaListBuilder.addVillas(villa);
                });
        LOGGER.info("Parsed out {} villas from the overview html", villaListBuilder.getVillasCount());
        return villaListBuilder.build();
    }

    public static VillaProto.Troops extractTroopsInfo(String trainHtml, int wallLevel, int noblemen) {
        VillaProto.Troops.Builder troopers = VillaProto.Troops.newBuilder();
        Map<String, Integer> troopCountMap = new HashMap<>();

        try {
            Document document = Jsoup.parse(trainHtml);
            Element dataTable = document.getElementsByClass(CLASS_VIS_TABLE).last();
            Elements rows = dataTable.select(TAG_TR);
            for (int i = 0; ++i < rows.size() - 1; ) { //iterating for total rows - 2
                Element row = rows.get(i);
                Elements columns = row.select(TAG_TD);
                //hard-wiring for non-premium account only - 4 columns only
                try {
                    String unitTitle = columns.get(0).getElementsByTag(TAG_A).get(0).attr(ATTR_DATA_UNIT);
                    int units = splitFractionAndGetDenominator(columns.get(2).text().trim());
                    troopCountMap.put(unitTitle, units);
                } catch (Exception e) {
                    LOGGER.error("Failed to extract unit info from row '{}'. ", row, e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to properly extract units information from trainHtml. ", e);
        }

        troopers.setSp(getDataPoint(troopCountMap, UNIT_SPEAR));
        troopers.setSw(getDataPoint(troopCountMap, UNIT_SWORD));
        troopers.setAx(getDataPoint(troopCountMap, UNIT_AXE));
        troopers.setAr(getDataPoint(troopCountMap, UNIT_ARCHER));
        troopers.setSu(getDataPoint(troopCountMap, UNIT_SCOUT));
        troopers.setLc(getDataPoint(troopCountMap, UNIT_LCAV));
        troopers.setMa(getDataPoint(troopCountMap, UNIT_MTD_ARCHER));
        troopers.setHc(getDataPoint(troopCountMap, UNIT_HCAV));
        troopers.setRm(getDataPoint(troopCountMap, UNIT_RAM));
        troopers.setCt(getDataPoint(troopCountMap, UNIT_CAT));
        troopers.setWl(wallLevel);
        troopers.setNb(noblemen);
        return troopers.build();
    }

    private static int getDataPoint(Map<String, Integer> dataMap, String dataPoint) {
        return dataMap.getOrDefault(dataPoint, ZERO_INT);
    }

    public static int extractWallInfo(String wallHtml) {
        try {
            Document document = Jsoup.parse(wallHtml);
            String wallInfo = document.getElementsByClass(CLASS_MAIN_TABLE).get(0).text();
            wallInfo = wallInfo.substring(wallInfo.indexOf(CHAR_BRACE_START) + 1, wallInfo.indexOf(CHAR_BRACE_END))
                    .substring(LEVEL_WALL.length() + 1);
            return Integer.parseInt(wallInfo);
        } catch (Exception e) {
            LOGGER.error("Failed to extract wall information from html. ", e);
        }
        return ZERO_INT;
    }

    public static int extractNoblemenInfo(String snobHtml) {
        try {
            Document document = Jsoup.parse(snobHtml);
            String nobleInfo = document.getElementsByClass(CLASS_VIS_TABLE).get(0).text();
            nobleInfo = nobleInfo.substring(nobleInfo.lastIndexOf(CHAR_COLON) + 1);
            nobleInfo = nobleInfo.substring(nobleInfo.indexOf(CHAR_SPACE) + 1);
            nobleInfo = nobleInfo.substring(0, nobleInfo.indexOf(CHAR_SPACE));
            return splitFractionAndGetDenominator(nobleInfo);
        } catch (Exception e) {
            LOGGER.error("Failed to extract noblemen information from html. ", e);
        }
        return ZERO_INT;
    }

    public static int splitFractionAndGetDenominator(String fraction) {
        return Integer.parseInt(fraction.split(FRACTION_SEPARATOR)[1]);
    }
}

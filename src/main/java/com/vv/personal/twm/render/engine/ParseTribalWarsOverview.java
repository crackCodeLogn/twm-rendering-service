package com.vv.personal.twm.render.engine;

import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vv.personal.twm.render.constants.Constants.*;

/**
 * @author Vivek
 * @since 29/11/20
 */
public class ParseTribalWarsOverview {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseTribalWarsOverview.class);

    public static VillaProto.VillaList.Builder generateVillaListBuilder(String overviewHtml) {
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
                    String[] coord = villaTitle.substring(villaTitle.indexOf(CHAR_COORD_START) + 1, villaTitle.indexOf(CHAR_COORD_END)).split(COORD_SEPARATOR);
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
        return villaListBuilder;
    }
}

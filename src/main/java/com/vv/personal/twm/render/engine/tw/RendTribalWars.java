package com.vv.personal.twm.render.engine.tw;

import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import com.vv.personal.twm.render.engine.Rend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static com.vv.personal.twm.render.constants.Constants.*;

/**
 * @author Vivek
 * @since 01/12/20
 */
public class RendTribalWars extends Rend {
    private static final Logger LOGGER = LoggerFactory.getLogger(RendTribalWars.class);

    public static String renderVillas(VillaProto.VillaList villas) {
        final StringBuilder table = new StringBuilder(HTML_TABLE_START);
        addHeaderCells(table,
                "Number",
                "World",
                "Name",
                "X|Y",
                "Id",
                "Type",
                "Wall",
                "Spear",
                "Sword",
                "Axe",
                "Archer",
                "Scout",
                "Lcav",
                "M Archer",
                "Hcav",
                "Ram",
                "Cat",
                "Paladin",
                "Noble");

        AtomicInteger counter = new AtomicInteger(0);
        villas.getVillasList().forEach(villa -> {
            try {
                //LOGGER.info(villa.toString()); //getting too verbose
                addRowCells(table,
                        counter.incrementAndGet(),
                        villa.getWorld(),
                        villa.getName(),
                        villa.getX() + COORD_SEPARATOR + villa.getY(),
                        villa.getId(),
                        villa.getType(),
                        villa.getTroops().getWl(),
                        villa.getTroops().getSp(),
                        villa.getTroops().getSw(),
                        villa.getTroops().getAx(),
                        villa.getTroops().getAr(),
                        villa.getTroops().getSu(),
                        villa.getTroops().getLc(),
                        villa.getTroops().getMa(),
                        villa.getTroops().getHc(),
                        villa.getTroops().getRm(),
                        villa.getTroops().getCt(),
                        villa.getTroops().getPd(),
                        villa.getTroops().getNb()
                );
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to HTML. Skipping. ", villa, e);
            }
        });
        table.append(HTML_TABLE_END);
        LOGGER.info("Rendering finished for {} villas", villas.getVillasCount());
        return table.toString();
    }
}

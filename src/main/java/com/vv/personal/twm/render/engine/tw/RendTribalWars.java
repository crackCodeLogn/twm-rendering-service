package com.vv.personal.twm.render.engine.tw;

import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import com.vv.personal.twm.render.engine.Rend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
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
        Set<String> extraDoubleKeys = new HashSet<>(), extraStringKeys = new HashSet<>(), extraIntKeys = new HashSet<>();
        villas.getVillasList().forEach(villa -> {
            extraDoubleKeys.addAll(villa.getExtraDoublesMap().keySet());
            extraStringKeys.addAll(villa.getExtraStringsMap().keySet());
            extraIntKeys.addAll(villa.getExtraIntsMap().keySet());
        });
        startRow(table);
        addUnboundedHeaderCells(table,
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
                "Arch",
                "Scout",
                "Lcav",
                "M.Arc",
                "Hcav",
                "Ram",
                "Cat",
                "Palad",
                "Noble",
                "Farm");
        extraDoubleKeys.forEach(key -> addUnboundedHeaderCells(table, key));
        extraStringKeys.forEach(key -> addUnboundedHeaderCells(table, key));
        extraIntKeys.forEach(key -> addUnboundedHeaderCells(table, key));
        endRow(table);

        AtomicInteger counter = new AtomicInteger(0);
        villas.getVillasList().forEach(villa -> {
            try {
                //LOGGER.info(villa.toString()); //getting too verbose
                startRow(table);
                addUnboundedRowCells(table,
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
                        villa.getTroops().getNb(),
                        villa.getFarmStrength()
                );
                extraDoubleKeys.forEach(key -> addUnboundedRowCells(table, villa.getExtraDoublesMap().getOrDefault(key, ZERO_DOUBLE)));
                extraStringKeys.forEach(key -> addUnboundedRowCells(table, villa.getExtraStringsMap().getOrDefault(key, EMPTY_STR)));
                extraIntKeys.forEach(key -> addUnboundedRowCells(table, villa.getExtraIntsMap().getOrDefault(key, ZERO_INT)));
                endRow(table);
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to HTML. Skipping. ", villa, e);
            }
        });
        table.append(HTML_TABLE_END);
        LOGGER.info("Rendering finished for {} villas", villas.getVillasCount());
        return table.toString();
    }
}

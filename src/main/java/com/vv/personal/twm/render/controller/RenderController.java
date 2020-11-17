package com.vv.personal.twm.render.controller;

import com.vv.personal.twm.render.engine.RendBank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vivek
 * @since 17/11/20
 */
@RestController("RenderController")
@RequestMapping("/render")
public class RenderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RenderController.class);

    @PostMapping("/rendBanks")
    public String rendBanks(@RequestBody String banksJson) {
        if (banksJson.isEmpty()) return "FAILED - EMPTY JSON!";
        LOGGER.info("Received string to render for bank: {}", banksJson);
        return RendBank.generateTable(banksJson);
    }

}

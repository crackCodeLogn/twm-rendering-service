package com.vv.personal.twm.render.controller;

import com.vv.personal.twm.render.engine.RendBank;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vivek
 * @since 17/11/20
 */
@RestController("RenderController")
@RequestMapping("/render")
public class RenderController {

    @PostMapping("/rendBanks")
    public String rendBanks(@RequestBody String banksJson) {
        if (banksJson.isEmpty()) return "FAILED - EMPTY JSON!";
        return RendBank.generateTable(banksJson);
    }

}

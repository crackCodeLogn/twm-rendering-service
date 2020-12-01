package com.vv.personal.twm.render.controller;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.artifactory.generated.tw.HtmlDataParcelProto;
import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import com.vv.personal.twm.render.engine.RendBank;
import com.vv.personal.twm.render.engine.RendFixedDeposit;
import com.vv.personal.twm.render.engine.tw.ParseTribalWars;
import com.vv.personal.twm.render.engine.tw.RendTribalWars;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.vv.personal.twm.render.constants.Constants.EMPTY_STR;

/**
 * @author Vivek
 * @since 17/11/20
 */
@RestController("RenderController")
@RequestMapping("/render")
public class RenderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RenderController.class);

    @PostMapping("/rendBanks")
    public String rendBanks(@RequestBody BankProto.BankList bankList) {
        if (bankList.getBanksList().isEmpty()) return "FAILED - EMPTY JSON!";
        LOGGER.info("Received string to render for bank: {}", bankList);
        return RendBank.generateTable(bankList);
    }

    @PostMapping("/rendFds")
    public String rendFds(@RequestBody FixedDepositProto.FixedDepositList fixedDepositList) {
        if (fixedDepositList.getFixedDepositsList().isEmpty()) return "FAILED - EMPTY JSON!";
        LOGGER.info("Received string to render for FD: {}", fixedDepositList);
        return RendFixedDeposit.generateTable(fixedDepositList);
    }

    @PostMapping("/tw/parse/overview")
    public VillaProto.VillaList parseTribalWarsOverviewHtml(@RequestBody String htmlData) {
        try {
            return ParseTribalWars.generateVillaList(htmlData);
        } catch (Exception e) {
            LOGGER.error("Failed to parse overview page. ", e);
        }
        return VillaProto.VillaList.newBuilder().build();
    }

    @PostMapping("/tw/parse/screens") //parsing wall, train and snob together -- hard binding!
    public VillaProto.Troops parseTribalWarsScreensHtml(@RequestBody HtmlDataParcelProto.Parcel parcel) {
        try {
            int wallLevel = ParseTribalWars.extractWallInfo(parcel.getWallPageSource());
            int noblemen = ParseTribalWars.extractNoblemenInfo(parcel.getSnobPageSource());
            return ParseTribalWars.extractTroopsInfo(parcel.getTrainPageSource(), wallLevel, noblemen);
        } catch (Exception e) {
            LOGGER.error("Failed to parse overview page. ", e);
        }
        return VillaProto.Troops.newBuilder().build();
    }

    @PostMapping("/tw/render/villas")
    public String renderTribalWarsVillas(@RequestBody VillaProto.VillaList villas) {
        try {
            return RendTribalWars.renderVillas(villas);
        } catch (Exception e) {
            LOGGER.error("Failed to render villas. ", e);
        }
        return EMPTY_STR;
    }
}

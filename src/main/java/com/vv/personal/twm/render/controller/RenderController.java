package com.vv.personal.twm.render.controller;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.artifactory.generated.tw.HtmlDataParcelProto;
import com.vv.personal.twm.artifactory.generated.tw.SupportReportProto;
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

import java.util.ArrayList;
import java.util.List;

import static com.vv.personal.twm.render.constants.Constants.EMPTY_STR;

/**
 * @author Vivek
 * @since 17/11/20
 * <p>
 * Note: As of 2020-12-06, was experiencing v.slow startup of Render-server in particular.
 * Found the explanation here: https://github.com/springfox/springfox/issues/2881#issuecomment-524884972
 * Switching to springfox-boot-starter's 3.0.0 swagger mitigated this issue and startup reduced from 150+s to < 20s
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
        if (fixedDepositList.getFixedDepositList().isEmpty()) return "FAILED - EMPTY JSON!";
        LOGGER.info("Received string to render for FD: {}", fixedDepositList);
        return RendFixedDeposit.generateTable(fixedDepositList);
    }

    @PostMapping("/tw/parse/overview")
    //@ApiOperation(value = "/tw/parse/overview", hidden = true, httpMethod = "POST") //attempt later on
    public VillaProto.VillaList parseTribalWarsOverviewHtml(@RequestBody String htmlData) {
        try {
            return ParseTribalWars.generateVillaList(htmlData);
        } catch (Exception e) {
            LOGGER.error("Failed to parse overview page. ", e);
        }
        return VillaProto.VillaList.newBuilder().build();
    }

    @PostMapping("/tw/parse/screens") //parsing wall, train and snob together -- hard binding!
    //@ApiOperation(value = "/tw/parse/screens", hidden = true, httpMethod = "POST") //parsing wall, train and snob together -- hard binding!
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

    @PostMapping("/tw/parse/screens/farm") //parsing farm
    public VillaProto.Villa parseTribalWarsFarmScreenHtml(@RequestBody HtmlDataParcelProto.Parcel parcel) {
        try {
            String farmStrength = ParseTribalWars.extractFarmStrengthInfoFromHtml(parcel.getFarmPageSource());
            return ParseTribalWars.inflateFarmStrengthInfo(farmStrength);
        } catch (Exception e) {
            LOGGER.error("Failed to parse overview page. ", e);
        }
        return VillaProto.Villa.newBuilder().build();
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

    /*@PostMapping("/tw/parse/scavenge") //parsing wall, train and snob together -- hard binding!
    //@ApiOperation(value = "/tw/parse/screens", hidden = true, httpMethod = "POST") //parsing wall, train and snob together -- hard binding!
    public VillaProto.Troops parseTribalWarsScavengeHtml(@RequestBody HtmlDataParcelProto.Parcel parcel) {
        try {
            int wallLevel = ParseTribalWars.extractWallInfo(parcel.getWallPageSource());
            int noblemen = ParseTribalWars.extractNoblemenInfo(parcel.getSnobPageSource());
            return ParseTribalWars.extractTroopsInfo(parcel.getTrainPageSource(), wallLevel, noblemen);
        } catch (Exception e) {
            LOGGER.error("Failed to parse overview page. ", e);
        }
        return VillaProto.Troops.newBuilder().build();
    }*/

    @PostMapping("/tw/parse/report/support")
    public SupportReportProto.SupportReport parseTribalWarsSupportReport(@RequestBody HtmlDataParcelProto.Parcel parcel) {
        try {
            return ParseTribalWars.extractSupportDetails(parcel.getSupportReportSource());
        } catch (Exception e) {
            LOGGER.error("Failed to parse support report page. ", e);
        }
        return SupportReportProto.SupportReport.newBuilder().build();
    }

    @PostMapping("/tw/parse/page/supportReports")
    public List<String> parseTribalWarsSupportReportLinks(@RequestBody HtmlDataParcelProto.Parcel parcel) {
        try {
            return ParseTribalWars.extractSupportReportLinks(parcel.getSupportReportSource());
        } catch (Exception e) {
            LOGGER.error("Failed to parse support report page links. ", e);
        }
        return new ArrayList<>();
    }

    @PostMapping("/tw/parse/page/supportReportPagesLinks")
    public List<String> parseTribalWarsSupportReportsPagesLinks(@RequestBody HtmlDataParcelProto.Parcel parcel) {
        try {
            return ParseTribalWars.extractSupportReportPagesLinks(parcel.getSupportReportSource());
        } catch (Exception e) {
            LOGGER.error("Failed to parse support report pages links. ", e);
        }
        return new ArrayList<>();
    }
}

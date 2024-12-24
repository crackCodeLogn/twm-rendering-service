package com.vv.personal.twm.render.engine;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

/**
 * @author Vivek
 * @since 17/11/20
 */
@RunWith(JUnit4.class)
public class RendBankTest {

    @Ignore
    @Deprecated
    @Test
    public void testGenerateTable() {
        String input = "[{\"name\":\"TR1\",\"bankType\":\"PRIVATE\",\"IFSC\":\"TRN1234\",\"contactNumber\":1213131}, {\"name\":\"TR2\",\"bankType\":\"PRIVATE\",\"IFSC\":\"TRN12344\",\"contactNumber\":12131231}]";
        String result = RendBank.generateTable(input);
        System.out.println(result);
        assertEquals("<table style=\"border: 3px solid black; width:100%\"><tr><th style=\"border:2px solid black; background-color:#cef\">Name</th><th style=\"border:2px solid black; background-color:#cef\">Type</th><th style=\"border:2px solid black; background-color:#cef\">IFSC</th><th style=\"border:2px solid black; background-color:#cef\">Contact</th></tr><tr><td style=\"border:1px solid black;text-align:center\">TR1</td><td style=\"border:1px solid black;text-align:center\">PRIVATE</td><td style=\"border:1px solid black;text-align:center\">TRN1234</td><td style=\"border:1px solid black;text-align:center\">1213131</td></tr><tr><td style=\"border:1px solid black;text-align:center\">TR2</td><td style=\"border:1px solid black;text-align:center\">PRIVATE</td><td style=\"border:1px solid black;text-align:center\">TRN12344</td><td style=\"border:1px solid black;text-align:center\">12131231</td></tr></table>", result);
    }

    @Ignore
    @Deprecated
    @Test
    public void testGenerateTableReadFromMongo() {
        String input = "[{\"_id\": {\"$oid\": \"5fb39c4b60aa712cb5fbfeb1\"}, \"name\": \"SBI ANANDNAGAR\", \"type\": \"GOVT\", \"IFSC\": \"SBIN0005097\", \"contactNumber\": \"079-26765522\"}, {\"_id\": {\"$oid\": \"5fb39d1160aa712cb5fbfeb2\"}, \"name\": \"HDFC MUMBAI\", \"type\": \"PRIVATE\", \"IFSC\": \"HDFC0000411\", \"contactNumber\": \"022-61606161\"}, {\"_id\": {\"$oid\": \"5fb39dd260aa712cb5fbfeb3\"}, \"name\": \"ANDHRA ANANDNAGAR\", \"type\": \"PRIVATE\", \"IFSC\": \"ANDB0001153\", \"contactNumber\": \"079-26930553\"}]";
        String result = RendBank.generateTable(input);
        System.out.println(result);
        assertEquals("<table style=\"border: 3px solid black; width:100%\"><tr><th style=\"border:2px solid black; background-color:#cef\">Name</th><th style=\"border:2px solid black; background-color:#cef\">Type</th><th style=\"border:2px solid black; background-color:#cef\">IFSC</th><th style=\"border:2px solid black; background-color:#cef\">Contact</th></tr><tr><td style=\"border:1px solid black;text-align:center\">SBI ANANDNAGAR</td><td style=\"border:1px solid black;text-align:center\">GOVT</td><td style=\"border:1px solid black;text-align:center\">SBIN0005097</td><td style=\"border:1px solid black;text-align:center\">079-26765522</td></tr><tr><td style=\"border:1px solid black;text-align:center\">HDFC MUMBAI</td><td style=\"border:1px solid black;text-align:center\">GOVT</td><td style=\"border:1px solid black;text-align:center\">HDFC0000411</td><td style=\"border:1px solid black;text-align:center\">022-61606161</td></tr><tr><td style=\"border:1px solid black;text-align:center\">ANDHRA ANANDNAGAR</td><td style=\"border:1px solid black;text-align:center\">GOVT</td><td style=\"border:1px solid black;text-align:center\">ANDB0001153</td><td style=\"border:1px solid black;text-align:center\">079-26930553</td></tr></table>", result);
    }

    @Test
    public void testGenerateTableFromProto() {
        BankProto.BankList bankList = BankProto.BankList.newBuilder().addBanks(generateBank("BANK1", "IFSCB001", BankProto.BankType.PRIVATE, "+1 1234567890", "CA")).addBanks(generateBank("BANK2", "IFSCB002", BankProto.BankType.GOVT, "+91 1234567890", "IN")).build();
        String result = RendBank.generateTable(bankList);
        System.out.println(result);
        assertEquals("<table style=\"border: 3px solid black; width:100%\"><tr><th style=\"border:2px solid black; " + "background-color:#cef\">Number</th><th style=\"border:2px solid black; " + "background-color:#cef\">Name</th><th style=\"border:2px solid black; " + "background-color:#cef\">Type</th><th style=\"border:2px solid black; " + "background-color:#cef\">IFSC</th><th style=\"border:2px solid black; " + "background-color:#cef\">Contact</th><th style=\"border:2px solid black; background-color:#cef\">CountryCode</th></tr><tr><td style=\"border:1px solid black;text-align:center\">1</td><td style=\"border:1px solid black;text-align:center\">BANK1</td><td style=\"border:1px solid black;text-align:center\">PRIVATE</td><td style=\"border:1px solid black;text-align:center\">IFSCB001</td><td style=\"border:1px solid black;text-align:center\">+1 1234567890</td><td style=\"border:1px solid black;text-align:center\">CA</td></tr><tr><td style=\"border:1px solid black;text-align:center\">2</td><td style=\"border:1px solid black;text-align:center\">BANK2</td><td style=\"border:1px solid black;text-align:center\">GOVT</td><td style=\"border:1px solid black;text-align:center\">IFSCB002</td><td style=\"border:1px solid black;text-align:center\">+91 1234567890</td><td style=\"border:1px solid black;text-align:center\">IN</td></tr></table>", result);
    }

    private BankProto.Bank generateBank(String name, String ifsc, BankProto.BankType bankType, String contactNumber, String countryCode) {
        return BankProto.Bank.newBuilder().setName(name).setIFSC(ifsc).setContactNumber(contactNumber).setCountryCode(countryCode).setBankType(bankType).build();
    }

}
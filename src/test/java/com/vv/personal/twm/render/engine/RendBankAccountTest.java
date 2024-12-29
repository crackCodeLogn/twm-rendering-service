package com.vv.personal.twm.render.engine;

import com.google.protobuf.Timestamp;
import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author Vivek
 * @since 2024-12-25
 */
@RunWith(JUnit4.class)
public class RendBankAccountTest {

    @Test
    public void testGenerateTableFromProto() {
        UUID uuid1 = UUID.fromString("115e516e-b8e9-4ff1-9169-30c90558bddf");
        Instant instant1 = Instant.ofEpochMilli(1735106164176L);
        UUID uuid2 = UUID.fromString("d850ec1a-bcad-4b35-b14f-db2578083684");
        Instant instant2 = Instant.ofEpochMilli(1735106178008L);

        BankProto.BankAccounts bankAccounts = BankProto.BankAccounts.newBuilder()
                .addAccounts(
                        BankProto.BankAccount.newBuilder()
                                .setId(uuid1.toString())
                                .setNumber("123456789")
                                .setName("test1")
                                .setBalance(121.21)
                                .addBankAccountTypes(BankProto.BankAccountType.CHQ)
                                .setBank(BankProto.Bank.newBuilder().setIFSC("12587"))
                                .setTransitNumber("123")
                                .setInstitutionNumber("4567")
                                .setInterestRate(0.05)
                                .setIsActive(true)
                                .setNote("note")
                                .setCcy(BankProto.CurrencyCode.INR)
                                .setLastUpdatedAt(Timestamp.newBuilder()
                                        .setSeconds(instant1.getEpochSecond())
                                        .setNanos(instant1.getNano())
                                        .build())
                )
                .addAccounts(
                        BankProto.BankAccount.newBuilder()
                                .setId(uuid2.toString())
                                .setNumber("f44t4444f")
                                .setName("test2")
                                .setBalance(22.21)
                                .addBankAccountTypes(BankProto.BankAccountType.TFSA)
                                .addBankAccountTypes(BankProto.BankAccountType.MKT)
                                .setBank(BankProto.Bank.newBuilder().setIFSC("12587"))
                                .setTransitNumber("56")
                                .setInstitutionNumber("903")
                                .setInterestRate(3.05)
                                .setIsActive(true)
                                .setLastUpdatedAt(Timestamp.newBuilder()
                                        .setSeconds(instant2.getEpochSecond())
                                        .setNanos(instant2.getNano())
                                        .build())
                )
                .build();
        String result = RendBankAccount.generateTable(bankAccounts);
        System.out.println(result);
        assertEquals("<table style=\"border: 3px solid black; width:100%\"><tr><th style=\"border:2px solid black; background-color:#cef\">Number</th><th style=\"border:2px solid black; background-color:#cef\">Id</th><th style=\"border:2px solid black; background-color:#cef\">AccNum</th><th style=\"border:2px solid black; background-color:#cef\">Name</th><th style=\"border:2px solid black; background-color:#cef\">Balance</th><th style=\"border:2px solid black; background-color:#cef\">Type</th><th style=\"border:2px solid black; background-color:#cef\">IFSC</th><th style=\"border:2px solid black; background-color:#cef\">Transit</th><th style=\"border:2px solid black; background-color:#cef\">Institution</th><th style=\"border:2px solid black; background-color:#cef\">Interest Rate</th><th style=\"border:2px solid black; background-color:#cef\">Active</th><th style=\"border:2px solid black; background-color:#cef\">CCY</th><th style=\"border:2px solid black; background-color:#cef\">Note</th><th style=\"border:2px solid black; background-color:#cef\">Last Updated</th></tr><tr><td style=\"border:1px solid black;text-align:center\">1</td><td style=\"border:1px solid black;text-align:center\">115e516e-b8e9-4ff1-9169-30c90558bddf</td><td style=\"border:1px solid black;text-align:center\">123456789</td><td style=\"border:1px solid black;text-align:center\">test1</td><td style=\"border:1px solid black;text-align:center\">121.21</td><td style=\"border:1px solid black;text-align:center\">CHQ</td><td style=\"border:1px solid black;text-align:center\">12587</td><td style=\"border:1px solid black;text-align:center\">123</td><td style=\"border:1px solid black;text-align:center\">4567</td><td style=\"border:1px solid black;text-align:center\">0.05</td><td style=\"border:1px solid black;text-align:center\">true</td><td style=\"border:1px solid black;text-align:center\">INR</td><td style=\"border:1px solid black;text-align:center\">note</td><td style=\"border:1px solid black;text-align:center\">2024-12-25T05:56:04.176Z</td></tr><tr><td style=\"border:1px solid black;text-align:center\">2</td><td style=\"border:1px solid black;text-align:center\">d850ec1a-bcad-4b35-b14f-db2578083684</td><td style=\"border:1px solid black;text-align:center\">f44t4444f</td><td style=\"border:1px solid black;text-align:center\">test2</td><td style=\"border:1px solid black;text-align:center\">22.21</td><td style=\"border:1px solid black;text-align:center\">TFSA|MKT</td><td style=\"border:1px solid black;text-align:center\">12587</td><td style=\"border:1px solid black;text-align:center\">56</td><td style=\"border:1px solid black;text-align:center\">903</td><td style=\"border:1px solid black;text-align:center\">3.05</td><td style=\"border:1px solid black;text-align:center\">true</td><td style=\"border:1px solid black;text-align:center\">CAD</td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\">2024-12-25T05:56:18.008Z</td></tr><tr><td style=\"border:1px solid black;text-align:center\">3</td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\">143.42</td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\"></td><td style=\"border:1px solid black;text-align:center\"></td></tr></table>", result);
    }
}
package com.vv.personal.twm.render.engine;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import static com.vv.personal.twm.render.constants.Constants.HTML_TABLE_END;
import static com.vv.personal.twm.render.constants.Constants.HTML_TABLE_START;

/**
 * @author Vivek
 * @since 2024-12-25
 */
public class RendBankAccount extends Rend {
    private static final Logger LOGGER = LoggerFactory.getLogger(RendBankAccount.class);

    public static String generateTable(BankProto.BankAccounts bankAccounts) {
        final StringBuilder table = new StringBuilder(HTML_TABLE_START);

        addHeaderCells(table,
                "Number",
                "Id",
                "AccNum",
                "Name",
                "Balance",
                "Type",
                "IFSC",
                "Transit",
                "Institution",
                "Interest Rate",
                "Active",
                "Last Updated");

        AtomicInteger counter = new AtomicInteger(0);
        bankAccounts.getAccountsList().forEach(bankAccount -> {
            try {
                addRowCells(table,
                        counter.incrementAndGet(),
                        bankAccount.getId(),
                        bankAccount.getNumber(),
                        bankAccount.getName(),
                        bankAccount.getBalance(),
                        bankAccount.getBankAccountType(),
                        bankAccount.getBank().getIFSC(),
                        bankAccount.getTransitNumber(),
                        bankAccount.getInstitutionNumber(),
                        bankAccount.getInterestRate(),
                        bankAccount.getIsActive(),
                        Instant.ofEpochSecond(bankAccount.getLastUpdatedAt().getSeconds(),
                                bankAccount.getLastUpdatedAt().getNanos())
                );
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to HTML. Skipping. ", bankAccount, e);
            }
        });
        table.append(HTML_TABLE_END);
        LOGGER.info("Rendering finished for {} bank accounts", bankAccounts.getAccountsCount());
        return table.toString();
    }
}

package org.poo.bank;

import lombok.Getter;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.plans.*;
import org.poo.fileio.UserInput;
import org.poo.transactions.Transaction;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that represents a user. Each user has a list of accounts and transactions.
 */
@Getter
public class User {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String birthDate;
    private final String occupation;
    private String plan;
    private PlanStrategy planStrategy;
    private final ArrayList<Account> accounts = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private List<SplitPayment> pendingPayments = new ArrayList<>();


    public User(final UserInput userInput) {
        firstName = userInput.getFirstName();
        lastName = userInput.getLastName();
        email = userInput.getEmail();
        birthDate = userInput.getBirthDate();
        occupation = userInput.getOccupation();
        if (occupation.equals("student")) {
            plan = "student";
        } else {
            plan = "standard";
        }
        setPlanStrategy(plan);
    }

    /**
     * Method that adds an account to the user. This method is used when creating a new account.
     * @param account the account to be added to the user's database
     */
    public void addAccount(final Account account) {
        accounts.add(account);
    }

    /**
     * Deletes an account from the user's list of accounts if its balance is zero.
     * All associated cards will also be deleted before the account is removed.
     * @param account the account to be removed from the user's database
     * @return true if the account was deleted, false if the account's balance is non-zero
     */
    public boolean deleteAccount(final Account account) {
        if (account.getBalance() != 0) {
            return false;
        }

        Iterator<Card> iterator = account.getCards().iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }

        accounts.remove(account);
        return true;
    }

    /**
     * Method that adds a transaction to the user's transaction history.
     * @param transaction the transaction to be added
     */
    public void addTransaction(final Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    public String upgradePlan(final String newPlan, Account account, Bank bank) {
        if (plan.equals(newPlan)) {
            return "The user already has the " + newPlan + " plan";
        }

        if (downgradePlan(newPlan)) {
            return "You cannot downgrade your plan.";
        }

        double fee = planUpgradeFee(newPlan);
        double exchangeRate = bank.getExchangeRates().getRate("RON", account.getCurrency());

        if (account.getBalance() < fee * exchangeRate) {
            return "Insufficient funds";
        }
        account.withdraw(fee * exchangeRate);

        plan = newPlan;
        setPlanStrategy(newPlan);
        return "Upgrade plan";
    }

    private boolean downgradePlan(final String newPlan) {
        if (plan.equals("gold") && (newPlan.equals("silver") || newPlan.equals("standard") ||
                newPlan.equals("student"))) {
            return true;
        }
        if (plan.equals("silver") && (newPlan.equals("standard") || newPlan.equals("student"))) {
            return true;
        }
        return false;
    }

    private double planUpgradeFee(final String newPlan) {
        if (newPlan.equals("silver") && (plan.equals("standard") || plan.equals("student"))) {
            return 100;
        }
        if (newPlan.equals("gold") && plan.equals("silver")) {
            return 250;
        }
        if (newPlan.equals("gold") && (plan.equals("standard") || plan.equals("student"))) {
            return 350;
        }
        return 0;
    }

    public void setPlanStrategy(final String plan) {
        switch (plan) {
            case "student":
                planStrategy = new StudentPlan();
                break;
            case "standard":
                planStrategy = new StandardPlan();
                break;
            case "silver":
                planStrategy = new SilverPlan();
                break;
            case "gold":
                planStrategy = new GoldPlan();
                break;
        }
    }

    public boolean has21Years() {
        LocalDate birth = LocalDate.parse(birthDate);
        LocalDate now = LocalDate.now();

        Period age = Period.between(birth, now);

        return age.getYears() >= 21;
    }
}

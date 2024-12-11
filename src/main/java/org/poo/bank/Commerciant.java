package org.poo.bank;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Commerciant {
    private String name;
    private double moneyReceived;

    public Commerciant(String name) {
        this.name = name;
        moneyReceived = 0;
    }

    public void receiveMoney(double amount) {
        moneyReceived += amount;
    }

}

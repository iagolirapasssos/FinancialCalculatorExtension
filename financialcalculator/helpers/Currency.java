package com.bosonshiggs.finanlcialcalculator.helpers;

import com.google.appinventor.components.common.OptionList;

import java.util.HashMap;
import java.util.Map;

public enum Currency implements OptionList<String> {
    BRL("pt_BR"),
    USD("en_US"),
    EUR("fr_FR"),
    JPY("ja_JP");

    private String currencyCode;

    Currency(String r) {
        this.currencyCode = r;
    }

    public String toUnderlyingValue() {
        return currencyCode;
    }

    private static final Map<String, Currency> lookup = new HashMap<>();

    static {
        for (Currency r : Currency.values()) {
            lookup.put(r.toUnderlyingValue(), r);
        }
    }

    public static Currency fromUnderlyingValue(String r) {
        return lookup.get(r);
    }
}

package com.bosonshiggs.finanlcialcalculator.helpers;

import com.google.appinventor.components.common.OptionList;
import java.util.HashMap;
import java.util.Map;

// Define an enum named BooleanValue that implements the OptionList interface with Boolean type.
public enum BooleanValue implements OptionList<Boolean> {
    FALSE(false),  // Enum constant for false value
    TRUE(true);    // Enum constant for true value

    private Boolean value; // Private variable to hold the boolean value

    // Constructor for the enum. Assigns the passed boolean value to the private variable.
    BooleanValue(Boolean value) {
        this.value = value;
    }

    // Method to return the underlying boolean value of the enum constant.
    public Boolean toUnderlyingValue() {
        return value;
    }

    // Static map to enable lookup of enum constants based on boolean values.
    private static final Map<Boolean, BooleanValue> lookup = new HashMap<>();

    // Static block to populate the lookup map with the enum constants.
    static {
        for (BooleanValue val : BooleanValue.values()) {
            lookup.put(val.toUnderlyingValue(), val);
        }
    }

    // Static method to get the enum constant corresponding to a given boolean value.
    public static BooleanValue fromUnderlyingValue(Boolean value) {
        return lookup.get(value);
    }
}

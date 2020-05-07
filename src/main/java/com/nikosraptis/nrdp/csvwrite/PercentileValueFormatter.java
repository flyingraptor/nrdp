package com.nikosraptis.nrdp.csvwrite;

public class PercentileValueFormatter {

    public enum Format{
        DEFAULT;
    }

    public String format(Format type, long value) {
        String formattedValue = String.valueOf(value);
        switch (type) {
            case DEFAULT: { /* Example: 50m30s*/
                formattedValue =  (value) / 60 + "m" + ((int) ((value) % 60) + "s");
                break;
            }
        }
        return formattedValue;
    }
}

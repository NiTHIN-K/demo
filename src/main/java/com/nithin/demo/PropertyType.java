package com.nithin.demo;

public enum PropertyType {
    COMMERCIAL,
    RESIDENTIAL;

    @Override
    public String toString() {
        switch (this) {
            case COMMERCIAL:
                return "C";
            case RESIDENTIAL:
                return "R";
            default:
                return null;
        }
    }
}



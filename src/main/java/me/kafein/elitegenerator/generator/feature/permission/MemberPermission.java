package me.kafein.elitegenerator.generator.feature.permission;

import lombok.Setter;

public enum MemberPermission {

    BREAK_GENERATOR(true), OPEN_SETTINGS(false), CHANGE_SETTINGS(false);

    @Setter
    private boolean value;

    MemberPermission(boolean defaultValue) {
        this.value = defaultValue;
    }

    public boolean getValue() {
        return value;
    }

}

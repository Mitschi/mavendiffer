package at.aau.diff.common;

public enum VersionChange {
    MAJOR_VERSION_INCREASE,MAJOR_VERSION_DECREASE,
    MINOR_VERSION_INCREASE,MINOR_VERSION_DECREASE,
    PATCH_VERSION_INCREASE,PATCH_VERSION_DECREASE,
    POSTFIX_VERSION_UPDATE,
    UNKNOWN_VERSION_UPDATE,
    NO_VERSION_UPDATE;
}
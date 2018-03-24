package at.aau.diff.common;

import java.util.Comparator;

public class ReleaseStringComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        Version v1= Version.parseVersion(s1);
        Version v2= Version.parseVersion(s2);
        return v1.compareTo(v2);
    }
}
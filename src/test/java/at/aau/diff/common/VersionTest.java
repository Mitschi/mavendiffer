package at.aau.diff.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VersionTest {
    @Test
    public void versionChange1() {
        VersionChange versionChange = Version.extractVersionChange(Version.parseVersion("2"),
                Version.parseVersion("3"));
        assertEquals(VersionChange.MAJOR_VERSION_INCREASE,versionChange);
    }
    @Test
    public void versionChange2() {
        VersionChange versionChange = Version.extractVersionChange(Version.parseVersion("2.1"),
                Version.parseVersion("2.2"));
        assertEquals(VersionChange.MINOR_VERSION_INCREASE, versionChange);
    }
    @Test
    public void versionChange3() {
        VersionChange versionChange = Version.extractVersionChange(Version.parseVersion("2.2.1"),
                Version.parseVersion("2.2.2"));
        assertEquals(VersionChange.PATCH_VERSION_INCREASE, versionChange);
    }
    @Test
    public void versionChange4() {
        VersionChange versionChange = Version.extractVersionChange(Version.parseVersion("1.255-SNAPSHOT"),
                Version.parseVersion("1.255"));
        assertEquals(VersionChange.POSTFIX_VERSION_UPDATE, versionChange);
    }
    @Test
    public void versionChange5() {
        VersionChange versionChange = Version.extractVersionChange(Version.parseVersion("1.582-SNAPSHOT"),
                Version.parseVersion("1.582"));
        assertEquals(VersionChange.POSTFIX_VERSION_UPDATE, versionChange);
    }
    @Test
    public void versionChange6() {
        VersionChange versionChange = Version.extractVersionChange(Version.parseVersion("1.582-SNAPSHOT"),
                Version.parseVersion("1.583"));
        assertEquals(VersionChange.MINOR_VERSION_INCREASE, versionChange);
    }
    @Test
    public void versionChange7() {
        VersionChange versionChange = Version.extractVersionChange(Version.parseVersion("6.0-SNAPSHOT"),
                Version.parseVersion("6.0.0rc1"));
        assertEquals(VersionChange.POSTFIX_VERSION_UPDATE, versionChange);
    }
    @Test
    public void versionChangeProperty() {
        VersionChange versionChange = Version.extractVersionChange(Version.parseVersion("2.7.0"),
                Version.parseVersion("${scala-version}"));
        assertEquals(VersionChange.UNKNOWN_VERSION_UPDATE,versionChange);
    }

    @Test
    public void testNoVersionString() {
        Version version = Version.parseVersion("");
        assertNull(version);
    }
    @Test
    public void testNoVersionProperty() {
        Version version = Version.parseVersion("${scala-version}");
        assertNull(version);
    }
    @Test
    public void testNoVersionNull() {
        Version version = Version.parseVersion(null);
        assertNull(version);
    }
    @Test
    public void testSingle() {
        Version version = Version.parseVersion("17");
        assertEquals(new Integer(17),version.getMajor());
    }
    @Test
    public void testTwoParts() {
        Version version = Version.parseVersion("17.2");
        assertEquals(new Integer(17),version.getMajor());
        assertEquals(new Integer(2),version.getMinor());
    }
    @Test
    public void testTwoPartsLong() {
        Version version = Version.parseVersion("1.255");
        assertEquals(new Integer(1),version.getMajor());
        assertEquals(new Integer(255),version.getMinor());
    }
    @Test
    public void testThreeParts() {
        Version version = Version.parseVersion("17.2.5");
        assertEquals(new Integer(17),version.getMajor());
        assertEquals(new Integer(2),version.getMinor());
        assertEquals(new Integer(5),version.getPatch());
    }
    @Test
    public void testTwoPlusSpecial() {
        Version version = Version.parseVersion("1.255-SNAPSHOT");
        assertEquals(new Integer(1),version.getMajor());
        assertEquals(new Integer(255),version.getMinor());
        assertEquals("-SNAPSHOT",version.getPostfix());
    }
    @Test
    public void testThreePlusSpecial() {
        Version version = Version.parseVersion("6.0.0rc1");
        assertEquals(new Integer(6),version.getMajor());
        assertEquals(new Integer(0),version.getMinor());
        assertEquals(new Integer(0),version.getPatch());
        assertEquals("rc1",version.getPostfix());
    }
    @Test
    public void testTwoPlusSpecialSnapshot() {
        Version version = Version.parseVersion("6.0-SNAPSHOT");
        assertEquals(new Integer(6),version.getMajor());
        assertEquals(new Integer(0),version.getMinor());
        assertEquals("-SNAPSHOT",version.getPostfix());
    }
}

package at.aau.diff.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version>{
    private Integer major;
    private Integer minor;
    private Integer patch;
    private String postfix;
    private String versionString;

    private Version(){}

    public static Version parseVersion(String versionStringParam) {
        if(versionStringParam==null) {
            return null;
        }
        Pattern pattern = Pattern.compile("([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})(.*)");
        Matcher matcher = pattern.matcher(versionStringParam);
        if (matcher.find()) {
            Version v = new Version();
            v.versionString=versionStringParam;
            v.setMajor(Integer.parseInt(matcher.group(1)));
            v.setMinor(Integer.parseInt(matcher.group(2)));
            v.setPatch(Integer.parseInt(matcher.group(3)));
            if(matcher.groupCount()>3) {
                v.setPostfix(matcher.group(4));
            }
            return v;
        }
        //for 2.0 versions
        pattern = Pattern.compile("([0-9]{1,3})\\.([0-9]{1,3})(.*)");
        matcher = pattern.matcher(versionStringParam);
        if (matcher.find()) {
            Version v = new Version();
            v.versionString=versionStringParam;
            v.setMajor(Integer.parseInt(matcher.group(1)));
            v.setMinor(Integer.parseInt(matcher.group(2)));
            v.setPatch(null);
            if(matcher.groupCount()>2) {
                v.setPostfix(matcher.group(3));
            }
            return v;
        }
        //for 28 versions
        pattern = Pattern.compile("([0-9]{1,3})(.*)");
        matcher = pattern.matcher(versionStringParam);
        if (matcher.find()) {
            Version v = new Version();
            v.versionString=versionStringParam;
            v.setMajor(Integer.parseInt(matcher.group(1)));
            v.setMinor(null);
            v.setPatch(null);
            if(matcher.groupCount()>1) {
                v.setPostfix(matcher.group(2));
            }
            return v;
        }

        return null;
    }

    public static VersionChange extractVersionChange(Version from, Version to) {
        if(from==null || to==null) {
            return VersionChange.UNKNOWN_VERSION_UPDATE;
        }
        if(from.getMajor() !=null && (!from.getMajor().equals(to.getMajor()))) {
            if(from.getMajor()<to.getMajor()) {
                return VersionChange.MAJOR_VERSION_INCREASE;
            } else {
                return VersionChange.MAJOR_VERSION_DECREASE;
            }
        }
        if(from.getMinor()!=null && (!from.getMinor().equals(to.getMinor()))) {
            if(from.getMinor()<to.getMinor()) {
                return VersionChange.MINOR_VERSION_INCREASE;
            } else {
                return VersionChange.MINOR_VERSION_DECREASE;
            }
        }
        if(from.getPatch()!=null && (!from.getPatch().equals(to.getPatch()))) {
            if(from.getPatch()<to.getPatch()) {
                return VersionChange.PATCH_VERSION_INCREASE;
            } else {
                return VersionChange.PATCH_VERSION_DECREASE;
            }
        }
        if(from.getPostfix()!=null && (!from.getPostfix().equals(to.getPostfix()))) {
            return VersionChange.POSTFIX_VERSION_UPDATE;
        }
        if(from.getVersionString()!=null && (from.getVersionString().equals(to.getVersionString()))) {
            return VersionChange.NO_VERSION_UPDATE;
        } else {
            return VersionChange.UNKNOWN_VERSION_UPDATE;
        }
    }

    @Override
    public int compareTo(Version o) {
        if(this.major.equals(o.getMajor())) {
            if(this.minor.equals(o.getMinor())) {
                if(this.patch==null ||this.patch.equals(o.getPatch())) {
                    if(this.postfix!=null) {
                        if(o.getPostfix()!=null) {
                            return this.postfix.compareTo(o.getPostfix());
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                } else {
                    if(this.patch !=null) {
                        if(o.getPatch()!=null) {
                            return this.patch.compareTo(o.getPatch());
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                }
            } else {
                return this.minor.compareTo(o.getMinor());
            }
        } else {
            return this.major.compareTo(o.getMajor());
        }
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public Integer getPatch() {
        return patch;
    }

    public void setPatch(Integer patch) {
        this.patch = patch;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        if (major != null ? !major.equals(version.major) : version.major != null) return false;
        if (minor != null ? !minor.equals(version.minor) : version.minor != null) return false;
        if (patch != null ? !patch.equals(version.patch) : version.patch != null) return false;
        if (postfix != null ? !postfix.equals(version.postfix) : version.postfix != null) return false;
        return versionString != null ? versionString.equals(version.versionString) : version.versionString == null;
    }

    @Override
    public int hashCode() {
        int result = major != null ? major.hashCode() : 0;
        result = 31 * result + (minor != null ? minor.hashCode() : 0);
        result = 31 * result + (patch != null ? patch.hashCode() : 0);
        result = 31 * result + (postfix != null ? postfix.hashCode() : 0);
        result = 31 * result + (versionString != null ? versionString.hashCode() : 0);
        return result;
    }
}

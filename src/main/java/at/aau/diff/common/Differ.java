package at.aau.diff.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public interface Differ 
{
    public List<Change> extractChanges(ByteArrayOutputStream file1, ByteArrayOutputStream file2) throws Exception;

    public List<Change> extractChanges(File oldTmpFile, File newTmpFile) throws Exception;
}

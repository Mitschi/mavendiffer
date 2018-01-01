package at.aau.diff.maven;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import at.aau.diff.common.Change;
import at.aau.diff.common.Differ;
import org.apache.commons.io.FileUtils;

public class Main {

	public static void main(String[] args) throws Exception {
		Differ buildDiffer = new MavenBuildFileDiffer();

		System.out.println("Files to diff: ");
		System.out.println(new File(args[0]).getAbsolutePath());
		System.out.println(new File(args[1]).getAbsolutePath());
		System.out.println("===========================");
		System.out.println("Changes:");
		System.out.println("===========================");
		
//		new File("poms/tmp").mkdirs();



        List<Change> actualChanges = buildDiffer.extractChanges(transform(new File(args[0])), transform(new File(args[1])));
		
		for (Change change : actualChanges) {
			System.out.println(change);
		}
		
		System.out.println("===========================");
	}

	private static ByteArrayOutputStream transform(File file) throws IOException {
        String content = FileUtils.readFileToString(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        pw.write(content);
        pw.flush();
        pw.close();
        return baos;
    }

}

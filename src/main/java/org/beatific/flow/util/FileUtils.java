package org.beatific.flow.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils extends org.apache.commons.io.FileUtils {

	public void write(String dir, String fileName, String data) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(dir + "/" + fileName);
			fos.write(data.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}

	}

	public static List<File> recentModifiedFiles(String dir, Holder<Long> holder) {

		holder.hold();

		return periodModifiedFiles(dir, holder.previousValue(),
				holder.holdedValue());
	}

	public static List<File> recentModifiedFilesInDirectory(String dir,
			Holder<Long> holder) {

		holder.hold();

		return periodModifiedFilesInDirectory(dir, holder.previousValue(),
				holder.holdedValue());
	}

	public static List<File> periodModifiedFiles(String dir, long fromTime,
			long toTime) {

		List<File> recents = new ArrayList<File>();
		File fl = new File(dir);

		File[] files = fl.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		});

		if (files == null)
			return recents;
		for (File file : files) {
			if (file.lastModified() > fromTime && file.lastModified() <= toTime) {
				recents.add(file);
			}
		}
		return recents;
	}

	public static List<File> periodModifiedFilesInDirectory(String dir,
			long fromTime, long toTime) {

		List<File> recents = new ArrayList<File>();
		File fl = new File(dir);

		recents.addAll(periodModifiedFiles(dir, fromTime, toTime));

		File[] directories = fl.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});

		if (directories == null)
			return recents;

		for (File directory : directories) {
			recents.addAll(periodModifiedFilesInDirectory(directory.getPath(),
					fromTime, toTime));
		}

		return recents;

	}

}

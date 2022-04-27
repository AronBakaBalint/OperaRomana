package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FillTable {
	private SubGroup[][] table = new SubGroup[1500][3];
	private int maxLen1 = 0;
	private int maxLen2 = 0;
	private int maxindex1 = 0;
	private int maxindex2 = 0;

	public FillTable(int dim) {
		this.table = new SubGroup[dim][3];
	}

	public void fillColumn1(String path) {

		List<String> fileLines = getSubtitleLines(path);
		List<SubGroup> subtextList = transformLinesToSubtitleGroups(fileLines);

		for (int i = 0; i < subtextList.size(); ++i) {
			this.table[i][0] = new SubGroup(String.valueOf(i + 1));
			this.table[i][1] = subtextList.get(i);
			int textWidth = subtextList.get(i).length();
			if (textWidth > this.maxLen1) {
				this.maxindex1 = i;
				this.maxLen1 = textWidth;
			}
		}
	}

	public void fillColumn2(String path) {

		List<String> fileLines = getSubtitleLines(path);
		List<SubGroup> subtextList = transformLinesToSubtitleGroups(fileLines);
		
		for (int i = 0; i < subtextList.size(); ++i) {
			this.table[i][2] = (SubGroup) subtextList.get(i);
			int textWidth = subtextList.get(i).length();
			if (textWidth > this.maxLen2) {
				this.maxindex2 = i;
				this.maxLen2 = textWidth;
			}
		}
	}

	public static int nrOfRows(String path) {
		List<String> fileLines = getSubtitleLines(path);
		List<SubGroup> subtextList = transformLinesToSubtitleGroups(fileLines);
		return subtextList.size();
	}
	
	private static List<SubGroup> transformLinesToSubtitleGroups(List<String> fileLines){
		List<SubGroup> subtitleList = new ArrayList<>();

		for (int i = 0; i < fileLines.size(); i++) {
			if (i == 0) {
				subtitleList.add(new SubGroup(fileLines.get(0).trim()));
			} else if (fileLines.get(i).trim().length() > 0 && fileLines.get(i - 1).trim().length() > 0) {
				subtitleList.get(subtitleList.size() - 1).add(fileLines.get(i).trim());
			} else if (fileLines.get(i).trim().length() > 0) {
				subtitleList.add(new SubGroup(fileLines.get(i).trim()));
			}
		}
		
		return subtitleList;
	}
	
	private static List<String> getSubtitleLines(String path){
		try {
			return Files.readAllLines(Paths.get(path));
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

	public SubGroup[][] getTable() {
		return this.table;
	}

	public int maxIndex1() {
		return this.maxindex1;
	}

	public int maxIndex2() {
		return this.maxindex2;
	}
}
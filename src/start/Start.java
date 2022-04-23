package start;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import language.Language;
import view.FileOperation;

public class Start {

	private static int defaultOffset1 = 0;
	private static int defaultOffset2 = 0;
	private static int defaultFontSize = 16;
	private static String defaultTextType = "Arial";
	private static String defaultColor1 = "White";
	private static String defaultColor2 = "White";
	private static String defaultLanguage = "English";
	private static String lastOpened1 = null;
	private static String lastOpened2 = null;

	public static void main(String[] args) throws IOException {
		
		try (FileReader fileReader = new FileReader("input.txt"); BufferedReader bufferedReader = new BufferedReader(fileReader)){
			String[] data = bufferedReader.readLine().split("\t");
			defaultOffset1 = Integer.parseInt(data[0]);
			defaultOffset2 = Integer.parseInt(data[1]);
			defaultTextType = data[2];
			defaultFontSize = Integer.parseInt(data[3]);
			defaultColor1 = data[4];
			defaultColor2 = data[5];
			defaultLanguage = data[6];
			Language.currentLanguage = defaultLanguage.replace("RO", "Română");
		} catch (IOException e) {
			File myObj = new File("input.txt");
			myObj.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"));
			writer.write("0\t0\tarial\t12\twhite\twhite\tEnglish");
			writer.close();
		}

		try (FileReader fileReader = new FileReader("last.txt"); BufferedReader bufferedReader = new BufferedReader(fileReader)){
			String[] data = bufferedReader.readLine().split("\t");
			lastOpened1 = data[0];
			lastOpened2 = data[1];
		} catch (Exception e) {
			File myObj = new File("last.txt");
			myObj.createNewFile();
		}

		try {
			FileOperation fo = new FileOperation(defaultOffset1, defaultOffset2, defaultTextType, defaultFontSize, defaultColor1, defaultColor2, lastOpened1, lastOpened2);
			fo.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(new JFrame(), e.toString());
			System.exit(0);
		}
	}
}

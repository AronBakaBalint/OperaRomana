package model;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class SubGroup {
	private String row1;
	private String row2 = "";

	public SubGroup(String s) {
		this.row1 = s;
	}

	public void add(String s) {
		this.row2 = s;
	}

	public String toString() {
		String toAdd = "";
		if (this.row1.length() < this.row2.length()) {
			this.row1 = (toAdd + this.row1);
		} else {
			this.row2 = (toAdd + this.row2);
		}
		if (this.row2.trim().length() > 0) {
			return "<html>" + this.row1 + "<br>" + this.row2;
		}
		return "<html>" + this.row1;
	}

	public static String transformToPrintableForm(String s) {
		s = s.replace("<html>", "");
		s = s.replace("<br>", "\n");

		return s;
	}

	public int length() {
		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
		Font font = new Font("Calibri", 0, 12);

		int textWidth1 = (int) font.getStringBounds(this.row1, frc).getWidth();
		int textWidth2 = (int) font.getStringBounds(this.row2, frc).getWidth();
		if (textWidth1 > textWidth2) {
			return textWidth1;
		}
		return textWidth2;
	}
}

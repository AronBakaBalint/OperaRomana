package language;

import java.util.HashMap;
import java.util.Map;

public class Language {

	public static final Map<String, Map<String, String>> languages = new HashMap<>();
	public static String currentLanguage = "English";
	
	static {
		languages.put("English", english());
		languages.put("Magyar", hungarian());
		languages.put("Română", romanian());
	}
	
	private static Map<String, String> english(){
		Map<String, String> english = new HashMap<>();
		english.put("open", "Open");
		english.put("language", "Language");
		english.put("load", "Load");
		english.put("canvas", "Canvas");
		english.put("search", "Search");
		english.put("full screen", "Full Screen");
		english.put("longest line language", "Longest line language");
		english.put("offset", "Offset");
		english.put("color", "Color");
		english.put("save", "Save");
		english.put("nr.", "Nr.");
		english.put("noFileChosen", "No File Chosen");
		english.put("selectFile", "No files were selected");
		english.put("openCanvas", "Canvas is not open");
		english.put("noMoreResults", "No more results");
		return english;
	}
	
	private static Map<String, String> hungarian(){
		Map<String, String> hungarian = new HashMap<>();
		hungarian.put("open", "Megnyit");
		hungarian.put("language", "Nyelv");
		hungarian.put("load", "Betölt");
		hungarian.put("canvas", "Vetítővászon");
		hungarian.put("search", "Keres");
		hungarian.put("full screen", "Teljes Képernyő");
		hungarian.put("longest line language", "Leghosszabb sor nyelv");
		hungarian.put("offset", "Eltolás");
		hungarian.put("color", "Szín");
		hungarian.put("save", "Save");
		hungarian.put("nr.", "Sorsz.");
		hungarian.put("noFileChosen", "No File Chosen");
		hungarian.put("selectFile", "Fájl nincs kiválasztva");
		hungarian.put("openCanvas", "Vetítővászon nincs megnyitva");
		hungarian.put("noMoreResults", "Nincs több találat");
		return hungarian;
	}
	
	private static Map<String, String> romanian(){
		Map<String, String> romanian = new HashMap<>();
		romanian.put("open", "Deschide");
		romanian.put("language", "Limbă");
		romanian.put("load", "Încarcă");
		romanian.put("canvas", "Ecran de proiecție");
		romanian.put("search", "Caută");
		romanian.put("full screen", "Ecran Complet");
		romanian.put("longest line language", "Cea mai lungă linie limbă");
		romanian.put("offset", "Decalaj");
		romanian.put("color", "Culoare");
		romanian.put("save", "Salvează");
		romanian.put("nr.", "Nr.");
		romanian.put("noFileChosen", "Niciun fișier selectat");
		romanian.put("selectFile", "Nu ați selectat niciun fișier");
		romanian.put("openCanvas", "Ecranul de proiecție nu este deschis");
		romanian.put("noMoreResults", "Nu au mai fost găsite rezultate");
		return romanian;
	}
	
	public static String translate(String word) {
		return languages.get(currentLanguage).get(word);
	}
}

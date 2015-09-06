package es.instavino.wine.matcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.ObjectMapper;

import es.instavino.flume.model.FlattenedImageInstagram;
import es.instavino.wine.db.model.CorpusId;
import es.instavino.wine.db.model.CorpusInstagramMatch;
import es.instavino.wine.db.model.CorpusId.CorpusPairType;
import es.instavino.wine.db.model.CorpusId.CorpusType;
import es.instavino.wine.db.model.CorpusSingleMatch;
import es.instavino.wine.reducer.Corpus;

public class WineDailyMatcher {

	ObjectMapper om = new ObjectMapper();

	public void dailyMatch(Corpus cc, String pathDailyJson, CorpusType type, FileWriter fw) throws IOException {

		System.out.println("Processing "+pathDailyJson);
		Files.lines(Paths.get(pathDailyJson), Charset.forName("UTF-8")).map(line -> readWine(line))
				.filter(instagram -> instagram.getCaptionText() != null)
				.map(instagram -> searchMatches(instagram, cc, type))
				.forEach(buffer -> {
					try {
						fw.write(buffer.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

	}

	private StringBuffer searchMatches(FlattenedImageInstagram instagram, Corpus corpus, CorpusType type) {
		CorpusInstagramMatch im = new CorpusInstagramMatch();
		im.setInstagram(instagram);
		String[] captionSplit = cleanCaption(instagram);
		Set<String> totalTerms = new HashSet<String>();
		totalTerms.addAll(Arrays.asList(captionSplit));
		totalTerms.addAll(instagram.getTags());
		totalTerms.remove("");
		totalTerms.remove(" ");
		for (String term : totalTerms) {
			Set<CorpusId> matches = corpus.getMatches().get(term);
			if (matches != null) {
				checkForSingleMatches(term, matches, im);
			}
		}
		switch (type) {
		case APPELLATION:
			return im.toCSVAppellations(corpus);
		case GRAPE:
			return im.toCSVGrapes(corpus);
		case WINE:
		default:
			return null;
		}

	}

	private void checkForSingleMatches(String term, Set<CorpusId> matches, CorpusInstagramMatch im) {
		boolean wineFound = false;
		boolean appellationFound = false;
		boolean grapeFound = false;
		for (CorpusId match : matches) {
			if (match.getCorpusPairType() == CorpusPairType.SIMPLE) {
				if (match.getCorpusType() == CorpusType.WINE) {
					if (wineFound) {
						im.removeTerm(term, CorpusType.WINE);
					} else {
						im.getWineSingle().add(new CorpusSingleMatch(match, term));
						wineFound = true;
					}
				} else if (match.getCorpusType() == CorpusType.APPELLATION) {
					if (appellationFound) {
						im.removeTerm(term, CorpusType.APPELLATION);
					} else {
						im.getAppellationSingle().add(new CorpusSingleMatch(match, term));
						appellationFound = true;
					}
				} else if (match.getCorpusType() == CorpusType.GRAPE) {
					if (grapeFound) {
						im.removeTerm(term, CorpusType.GRAPE);
					} else {
						im.getGrapeSingle().add(new CorpusSingleMatch(match, term));
						grapeFound = true;
					}
				}
			}
		}
	}

	private String[] cleanCaption(FlattenedImageInstagram instagram) {
		String caption = instagram.getCaptionText().toLowerCase();
		caption = caption.replace(")", "");
		caption = caption.replace("(", "");
		caption = caption.replace("?", "");
		caption = caption.replace("¿", "");
		caption = caption.replace("!", "");
		caption = caption.replace("¡", "");
		caption = caption.replace("*", "");
		caption = caption.replace("' ", " ");
		caption = caption.replace(" '", " ");
		caption = caption.replace("'", " ");
		caption = caption.replace("# ", " ");
		caption = caption.replace(" #", " ");
		caption = caption.replace("#", " ");
		caption = caption.replace("'", " ");
		caption = caption.replace("& ", " ");
		caption = caption.replace(" &", " ");
		caption = caption.replace("&", " ");
		caption = caption.replace("- ", " ");
		caption = caption.replace(" -", " ");
		caption = caption.replace("-", " ");
		caption = caption.replace("\\", "");
		caption = caption.replace("/", "");
		caption = caption.replace(".", "");
		caption = caption.replace(",", "");
		caption = caption.replace("  ", " ");
		String[] captionSplit = caption.split(" ");
		return captionSplit;
	}

	private FlattenedImageInstagram readWine(String line) {
		try {
			return om.readValue(line, FlattenedImageInstagram.class);
		} catch (IOException e) {
			return null;
		}
	}

	public static void main(String... args) throws IOException {
		Corpus cc = new Corpus();
		cc.readAndCreateMatches("C:\\Users\\albertomv\\Dropbox\\Master\\Proyecto\\corpus");
		File finalCSV = new File("C:\\Users\\albertomv\\Dropbox\\Master\\Proyecto\\matchesGrapes.csv");
		finalCSV.createNewFile();
		FileWriter fw = new FileWriter(finalCSV);
		String days[] = { "C:\\dev\\tmp\\2015-08-18.json", "C:\\dev\\tmp\\2015-08-19.json",
				"C:\\dev\\tmp\\2015-08-20.json", "C:\\dev\\tmp\\2015-08-21.json", "C:\\dev\\tmp\\2015-08-29.json",
				"C:\\dev\\tmp\\2015-08-30.json", "C:\\dev\\tmp\\2015-08-31.json", "C:\\dev\\tmp\\2015-09-01.json",
				"C:\\dev\\tmp\\2015-09-02.json", "C:\\dev\\tmp\\2015-09-03.json" };
		WineDailyMatcher wdm = new WineDailyMatcher();
		for (String day : days) {
			wdm.dailyMatch(cc, day, CorpusType.GRAPE,fw);
		}
		fw.close();
	}

}

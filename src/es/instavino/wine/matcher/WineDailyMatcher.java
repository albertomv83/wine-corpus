package es.instavino.wine.matcher;

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

	public void dailyMatch(String pathCorpus, String pathDailyJson) throws IOException {
		System.out.println("Starting");
		Corpus cc = new Corpus();
		Map<String, Set<CorpusId>> corpus = cc.readAndCreateMatches(pathCorpus);
		
		List<FlattenedImageInstagram> dailyInstagrams = Files.lines(Paths.get(pathDailyJson), Charset.forName("UTF-8"))
				.map(line -> readWine(line)).collect(Collectors.toList());

		dailyInstagrams.forEach(instagram -> searchMatches(instagram, corpus,cc));

	}

	private void searchMatches(FlattenedImageInstagram instagram, Map<String, Set<CorpusId>> corpus, Corpus cc) {
		CorpusInstagramMatch im = new CorpusInstagramMatch();
		im.setInstagram(instagram);
		String[] captionSplit = cleanCaption(instagram);
		Set<String> totalTerms = new HashSet<String>();
		totalTerms.addAll(Arrays.asList(captionSplit));
		totalTerms.addAll(instagram.getTags());
		for (String term : totalTerms) {
			if (!" ".equalsIgnoreCase(term) && !"".equalsIgnoreCase(term)) {
				Set<CorpusId> matches = corpus.get(term);
				if(matches!=null){
					checkForSingleMatches(term, matches,im);
				}
			}
		}
		if(im.foundSomething()){
			System.out.println(im.prettyPrint(cc));
		}

	}

	private void checkForSingleMatches(String term, Set<CorpusId> matches, CorpusInstagramMatch im) {
		boolean wineFound = false;
		boolean appellationFound = false;
		boolean grapeFound = false;
		for(CorpusId match:matches){
			if(match.getCorpusPairType()==CorpusPairType.SIMPLE){
				if(match.getCorpusType()==CorpusType.WINE){
					if(wineFound){
						im.removeTerm(term,CorpusType.WINE);				
					}else{
						im.getWineSingle().add(new CorpusSingleMatch(match, term));
						wineFound = true;
					}
				}else if(match.getCorpusType()==CorpusType.APPELLATION){
					if(appellationFound){
						im.removeTerm(term,CorpusType.APPELLATION);
					}else{
						im.getAppellationSingle().add(new CorpusSingleMatch(match, term));
						appellationFound = true;
					}
				}else if(match.getCorpusType()==CorpusType.GRAPE){
					if(grapeFound){
						im.removeTerm(term,CorpusType.GRAPE);
					}else{
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
		WineDailyMatcher wdm = new WineDailyMatcher();
		wdm.dailyMatch("C:\\Users\\albertomv\\Dropbox\\Master\\Proyecto\\corpus", "C:\\dev\\tmp\\2015-08-19.json");
	}

}

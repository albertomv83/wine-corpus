/**
 * $Id$
 * @author amancheno
 * @date   28/8/2015 11:49:15
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package es.instavino.wine.reducer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.ObjectMapper;

import es.instavino.wine.db.model.AppellationMatch;
import es.instavino.wine.db.model.CorpusId;
import es.instavino.wine.db.model.CorpusId.CorpusPairType;
import es.instavino.wine.db.model.CorpusId.CorpusType;
import es.instavino.wine.db.model.GrapeMatch;
import es.instavino.wine.db.model.WineMatch;

/**
 *
 */
public class Corpus {

    private final ObjectMapper om = new ObjectMapper();

    private final Map<String, Set<CorpusId>> matches =
            new Hashtable<String, Set<CorpusId>>();
    
    private Map<Long,AppellationMatch> appellations;
    private Map<Long,GrapeMatch> grapes;
    private Map<Long,WineMatch> wines;

    public void readAndCreateMatches(final String path) throws IOException {

        List<AppellationMatch> appellations =
            Files
                .lines(Paths.get(path + "\\appellationsCorpus.json"),
                    Charset.forName("UTF-8"))
                .map(line -> createAppellationMatch(line))
                .collect(Collectors.toList());

        List<GrapeMatch> grapes =
            Files
                .lines(Paths.get(path + "\\grapeCorpus.json"),
                    Charset.forName("UTF-8"))
                .map(line -> createGrapeMatch(line))
                .collect(Collectors.toList());

        List<WineMatch> wines =
            Files
                .lines(Paths.get(path + "\\wineCorpus.json"),
                    Charset.forName("UTF-8"))
                .map(line -> createWineMatch(line))
                .collect(Collectors.toList());
        
        mapAppellations(appellations);
        mapGrapes(grapes);
        mapWines(wines);

        appellations
            .stream()
            .filter(appellation -> appellation.getMatches() != null)
            .forEach(
                appellation -> appellation
                    .getMatches()
                    .stream()
                    .forEach(
                        match -> {
                            Set<CorpusId> ids = matches.get(match);
                            if (ids == null) {
                                ids = new HashSet<CorpusId>();
                            }
                            ids.add(new CorpusId(appellation.getId(),
                                CorpusType.APPELLATION,CorpusPairType.SIMPLE));
                            matches.put(match, ids);
                        }));

        appellations
            .stream()
            .filter(appellation -> appellation.getPairMatches() != null)
            .forEach(
                appellation -> {
                    appellation.getPairMatches().forEach(
                        sublist -> {
                        sublist.stream()
                        .forEach(
                            match -> {
                                Set<CorpusId> ids =
                                        matches.get(match);
                                if (ids == null) {
                                    ids = new HashSet<CorpusId>();
                                }
                                ids.add(new CorpusId(appellation
                                    .getId(),
                                    CorpusType.APPELLATION,CorpusPairType.PAIRED));
                                matches.put(match, ids);
                            });
                    });
                });

        grapes
            .stream()
            .filter(grape -> grape.getMatches() != null)
            .forEach(
                grape -> grape
                    .getMatches()
                    .stream()
                    .forEach(
                        match -> {
                            Set<CorpusId> ids = matches.get(match);
                            if (ids == null) {
                                ids = new HashSet<CorpusId>();
                            }
                            ids.add(new CorpusId(grape.getId(),
                                CorpusType.GRAPE,CorpusPairType.SIMPLE));
                            matches.put(match, ids);
                        }));

        grapes
            .stream()
            .filter(grape -> grape.getPairMatches() != null)
            .forEach(
                grape -> {
                    grape.getPairMatches().forEach(
                        sublist -> {
                        sublist.stream()
                        .forEach(
                            match -> {
                                Set<CorpusId> ids =
                                        matches.get(match);
                                if (ids == null) {
                                    ids = new HashSet<CorpusId>();
                                }
                                ids.add(new CorpusId(
                                            grape.getId(),
                                    CorpusType.GRAPE,CorpusPairType.PAIRED));
                                matches.put(match, ids);
                            });
                    });
                });

        wines.stream().filter(wine -> wine.getMatches() != null)
        .forEach(wine -> wine.getMatches().stream().forEach(match -> {
            Set<CorpusId> ids = matches.get(match);
            if (ids == null) {
                ids = new HashSet<CorpusId>();
            }
            ids.add(new CorpusId(wine.getId(), CorpusType.WINE,CorpusPairType.SIMPLE));
            matches.put(match, ids);
        }));
        
        wines
        .stream()
        .filter(wine -> wine.getPairMatches() != null)
        .forEach(
            wine -> {
                wine.getPairMatches().forEach(
                    sublist -> {
                    sublist.stream()
                    .forEach(
                        match -> {
                            Set<CorpusId> ids =
                                    matches.get(match);
                            if (ids == null) {
                                ids = new HashSet<CorpusId>();
                            }
                            ids.add(new CorpusId(
                                        wine.getId(),
                                CorpusType.WINE,CorpusPairType.PAIRED));
                            matches.put(match, ids);
                        });
                });
            });
    }
    
    
    
    public WineMatch findWine(Long id){
    	return this.wines.get(id);
    }
    
    public AppellationMatch findAppellation(Long id){
    	return this.appellations.get(id);
    }
    
    public GrapeMatch findGrape(Long id){
    	return this.grapes.get(id);
    }    
    
    public Map<String, Set<CorpusId>> getMatches() {
		return matches;
	}



	private void mapAppellations(List<AppellationMatch> appellations) {
		this.appellations = new HashMap<Long,AppellationMatch>();
		for(AppellationMatch a:appellations){
			this.appellations.put(a.getId(), a);
		}
	}

    private void mapGrapes(List<GrapeMatch> grapes) {
		this.grapes = new HashMap<Long,GrapeMatch>();
		for(GrapeMatch a:grapes){
			this.grapes.put(a.getId(), a);
		}
	}
    


    private void mapWines(List<WineMatch> wines) {
		this.wines = new HashMap<Long,WineMatch>();
		for(WineMatch a:wines){
			this.wines.put(a.getId(), a);
		}
	}

	private AppellationMatch createAppellationMatch(final String line) {
        try {
            return om.readValue(line, AppellationMatch.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private GrapeMatch createGrapeMatch(final String line) {
        try {
            return om.readValue(line, GrapeMatch.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private WineMatch createWineMatch(final String line) {
        try {
            return om.readValue(line, WineMatch.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(final String... args) throws IOException {
        Corpus cc = new Corpus();
        cc.readAndCreateMatches("C:\\Users\\albertomv\\Dropbox\\Master\\Proyecto\\corpus");
        Map<String, Set<CorpusId>> corpus = cc.getMatches();
        System.out.println(corpus.get("white"));
    }
}

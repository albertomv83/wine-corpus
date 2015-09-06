package es.instavino.wine.db.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import es.instavino.flume.model.FlattenedImageInstagram;
import es.instavino.wine.db.model.CorpusId.CorpusType;
import es.instavino.wine.reducer.Corpus;

public class CorpusInstagramMatch {

	private FlattenedImageInstagram instagram;
	private Set<CorpusSingleMatch> wineSingle = new HashSet<CorpusSingleMatch>();
	private Set<CorpusSingleMatch> appellationSingle = new HashSet<CorpusSingleMatch>();
	private Set<CorpusSingleMatch> grapeSingle = new HashSet<CorpusSingleMatch>();

	public FlattenedImageInstagram getInstagram() {
		return instagram;
	}

	public void setInstagram(FlattenedImageInstagram instagram) {
		this.instagram = instagram;
	}

	public Set<CorpusSingleMatch> getWineSingle() {
		return wineSingle;
	}

	public void setWineSingle(Set<CorpusSingleMatch> wineSingle) {
		this.wineSingle = wineSingle;
	}

	public Set<CorpusSingleMatch> getAppellationSingle() {
		return appellationSingle;
	}

	public void setAppellationSingle(Set<CorpusSingleMatch> appellationSingle) {
		this.appellationSingle = appellationSingle;
	}

	public Set<CorpusSingleMatch> getGrapeSingle() {
		return grapeSingle;
	}

	public void setGrapeSingle(Set<CorpusSingleMatch> grapeSingle) {
		this.grapeSingle = grapeSingle;
	}

	public boolean foundSomething() {
		return wineSingle.size() > 0 || grapeSingle.size() > 0 || appellationSingle.size() > 0;
	}
	
	public StringBuffer toCSVAppellations(Corpus corpus) {
		
		StringBuffer finalSb = new StringBuffer();
		
		Set<Long> uniqueAppellations = new HashSet<Long>();		
		if (appellationSingle.size() > 0) {
			for (CorpusSingleMatch match : appellationSingle) {
				uniqueAppellations.add(match.getCorpusTerm().getId());
			}
		}
		
		for(Long appellationId:uniqueAppellations){
			AppellationMatch match = corpus.findAppellation(appellationId);
			StringBuffer sb = new StringBuffer();		
			sb.append(match.getId()+",");
			sb.append(match.getName()+",");
			sb.append(instagram.getLink()+",");
			sb.append(instagram.getUserProfilePictureURL()+",");
			sb.append(instagram.getLocationLat()+",");
			sb.append(instagram.getLocationLon()+",");
			sb.append(instagram.getCreatedTime()+",");
			sb.append(instagram.getLikesCount());
			//System.out.println(sb.toString());
			finalSb.append(sb.toString()).append(System.lineSeparator());
		}
		return finalSb;
	}
	

	
	public StringBuffer toCSVGrapes(Corpus corpus) {
		StringBuffer finalSb = new StringBuffer();
		Set<Long> uniqueGrapes = new HashSet<Long>();		
		if (grapeSingle.size() > 0) {
			for (CorpusSingleMatch match : grapeSingle) {
				uniqueGrapes.add(match.getCorpusTerm().getId());
			}
		}
		
		for(Long grapeId:uniqueGrapes){
			GrapeMatch match = corpus.findGrape(grapeId);
			StringBuffer sb = new StringBuffer();		
			sb.append(match.getId()+",");
			sb.append(match.getName()+",");
			sb.append(instagram.getLink()+",");
			sb.append(instagram.getUserProfilePictureURL()+",");
			sb.append(instagram.getLocationLat()+",");
			sb.append(instagram.getLocationLon()+",");
			sb.append(instagram.getCreatedTime()+",");
			sb.append(instagram.getLikesCount());
			finalSb.append(sb.toString()).append(System.lineSeparator());
		}
		return finalSb;
	}
	
	public StringBuffer toCSVWines(Corpus corpus) {
		StringBuffer finalSb = new StringBuffer();
		Set<Long> uniqueWines = new HashSet<Long>();		
		if (wineSingle.size() > 0) {
			for (CorpusSingleMatch match : wineSingle) {
				uniqueWines.add(match.getCorpusTerm().getId());
			}
		}
		
		for(Long wineId:uniqueWines){
			WineMatch match = corpus.findWine(wineId);
			StringBuffer sb = new StringBuffer();		
			sb.append(match.getId()+",");
			sb.append(match.getName()+",");
			sb.append(instagram.getLink()+",");
			sb.append(instagram.getUserProfilePictureURL()+",");
			sb.append(instagram.getLocationLat()+",");
			sb.append(instagram.getLocationLon()+",");
			sb.append(instagram.getCreatedTime()+",");
			sb.append(instagram.getLikesCount());
			finalSb.append(sb.toString()).append(System.lineSeparator());
		}
		return finalSb;
	}

	public void removeTerm(String term, CorpusType type) {
		if(type.equals(CorpusType.WINE)){
			Iterator<CorpusSingleMatch> it = this.wineSingle.iterator();
			while(it.hasNext()){
				CorpusSingleMatch wine = it.next();
				if(wine.getTerm().equals(term)){
					it.remove();
				}
			}
		}
		if(type.equals(CorpusType.APPELLATION)){
			Iterator<CorpusSingleMatch> it = this.appellationSingle.iterator();
			while(it.hasNext()){
				CorpusSingleMatch appellation = it.next();
				if(appellation.getTerm().equals(term)){
					it.remove();
				}
			}
		}
		if(type.equals(CorpusType.GRAPE)){
			Iterator<CorpusSingleMatch> it = this.grapeSingle.iterator();
			while(it.hasNext()){
				CorpusSingleMatch grape = it.next();
				if(grape.getTerm().equals(term)){
					it.remove();
				}
			}
		}
		
	}

}

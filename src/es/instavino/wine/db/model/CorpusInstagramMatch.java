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

	public String prettyPrint(Corpus corpus) {
		StringBuffer sb = new StringBuffer();
		sb.append("For Instagram: ").append(instagram.getLink()).append(" with text: ")
				.append(instagram.getCaptionText()).append("\r\n");

		if (wineSingle.size() > 0) {
			for (CorpusSingleMatch match : wineSingle) {
				sb.append("Found Wine: ").append(corpus.findWine(match.getCorpusTerm().getId())).append(" with term ")
						.append(match.getTerm()).append(".\r\n");
			}
		}

		if (appellationSingle.size() > 0) {
			for (CorpusSingleMatch match : appellationSingle) {
				sb.append("Found Appellation: ").append(corpus.findAppellation(match.getCorpusTerm().getId())).append(" with term ")
						.append(match.getTerm()).append(".\r\n");
			}
		}

		if (grapeSingle.size() > 0) {
			for (CorpusSingleMatch match : grapeSingle) {
				sb.append("Found Grape: ").append(corpus.findGrape(match.getCorpusTerm().getId())).append(" with term ")
						.append(match.getTerm()).append(".\r\n");
			}
		}

		return sb.toString();
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

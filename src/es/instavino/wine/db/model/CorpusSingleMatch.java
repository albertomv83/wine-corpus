package es.instavino.wine.db.model;

public class CorpusSingleMatch {
	
	private CorpusId corpusTerm;
	private String term;
	public CorpusId getCorpusTerm() {
		return corpusTerm;
	}
	public void setCorpusTerm(CorpusId corpusTerm) {
		this.corpusTerm = corpusTerm;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public CorpusSingleMatch(CorpusId corpusTerm, String term) {
		super();
		this.corpusTerm = corpusTerm;
		this.term = term;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((corpusTerm == null) ? 0 : corpusTerm.hashCode());
		result = prime * result + ((term == null) ? 0 : term.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CorpusSingleMatch other = (CorpusSingleMatch) obj;
		if (corpusTerm == null) {
			if (other.corpusTerm != null)
				return false;
		} else if (!corpusTerm.equals(other.corpusTerm))
			return false;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}
	
	
	
	

}

/**
 * $Id$
 * @author amancheno
 * @date   28/8/2015 12:44:10
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package es.instavino.wine.db.model;

/**
 *
 */
public class CorpusId {

    public enum CorpusType {
        WINE, APPELLATION, GRAPE
    };
    
    public enum CorpusPairType {
        SIMPLE, PAIRED
    };

    private Long id;

    private CorpusType corpusType;
    
    private CorpusPairType corpusPairType;

    public CorpusId() {

    }
    
    

    public CorpusId(Long id, CorpusType corpusType, CorpusPairType corpusPairType) {
		super();
		this.id = id;
		this.corpusType = corpusType;
		this.corpusPairType = corpusPairType;
	}



	public CorpusPairType getCorpusPairType() {
		return corpusPairType;
	}



	public void setCorpusPairType(CorpusPairType corpusPairType) {
		this.corpusPairType = corpusPairType;
	}



	/**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return Returns the corpusType.
     */
    public CorpusType getCorpusType() {
        return corpusType;
    }

    /**
     * @param corpusType
     *            The corpusType to set.
     */
    public void setCorpusType(final CorpusType corpusType) {
        this.corpusType = corpusType;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime * result
                + ((corpusType == null) ? 0 : corpusType.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CorpusId other = (CorpusId) obj;
        if (corpusType != other.corpusType) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CorpusId [" + id + ", " + corpusType + "]";
    }

}

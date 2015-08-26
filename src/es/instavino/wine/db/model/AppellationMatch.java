/**
 * $Id$
 * @author amancheno
 * @date   26/8/2015 15:02:34
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package es.instavino.wine.db.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 */
public class AppellationMatch {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Id")
    private Long id;

    @JsonProperty("Matches")
    private List<String> matches;

    @JsonProperty("PairMatches")
    private List<List<String>> pairMatches;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(final String name) {
        this.name = name;
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
     * @return Returns the matches.
     */
    public List<String> getMatches() {
        return matches;
    }

    /**
     * @param matches
     *            The matches to set.
     */
    public void setMatches(final List<String> matches) {
        this.matches = matches;
    }

    /**
     * @return Returns the pairMatches.
     */
    public List<List<String>> getPairMatches() {
        return pairMatches;
    }

    /**
     * @param pairMatches
     *            The pairMatches to set.
     */
    public void setPairMatches(final List<List<String>> pairMatches) {
        this.pairMatches = pairMatches;
    }

}

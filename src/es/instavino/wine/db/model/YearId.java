/**
 * $Id$
 * @author amancheno
 * @date   24/8/2015 13:01:33
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package es.instavino.wine.db.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 */
public class YearId {

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Id")
    private Long id;

    /**
     * @param year
     * @param id
     */
    public YearId(final String year, final Long id) {
        super();
        this.year = year;
        this.id = id;
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
     * @return Returns the year.
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year
     *            The year to set.
     */
    public void setYear(final String year) {
        this.year = year;
    }

}

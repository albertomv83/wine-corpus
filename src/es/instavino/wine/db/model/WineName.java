/**
 * $Id$
 * @author amancheno
 * @date   24/8/2015 12:24:44
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
public class WineName {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Id")
    private Long id;

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

}

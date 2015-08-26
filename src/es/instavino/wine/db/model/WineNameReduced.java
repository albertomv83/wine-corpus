/**
 * $Id$
 * @author amancheno
 * @date   24/8/2015 12:59:42
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
public class WineNameReduced {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("List")
    private List<YearId> list;

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
     * @return Returns the list.
     */
    public List<YearId> getList() {
        return list;
    }

    /**
     * @param list
     *            The list to set.
     */
    public void setList(final List<YearId> list) {
        this.list = list;
    }

}

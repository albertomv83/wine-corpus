/**
 * $Id$
 * @author amancheno
 * @date   24/8/2015 12:31:08
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package es.instavino.wine.reducer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.ObjectMapper;

import es.instavino.wine.db.model.WineName;
import es.instavino.wine.db.model.WineNameReduced;
import es.instavino.wine.db.model.YearId;

/**
 *
 */
public class WineNameReducer {

    ObjectMapper om = new ObjectMapper();

    public void reduce(final String path) throws IOException {
        File result = new File(path + ".reduced");
        result.createNewFile();
        FileWriter fw = new FileWriter(result);

        Files.lines(Paths.get(path), Charset.forName("UTF-8"))
            .map(line -> readValue(line))
            .collect(Collectors.groupingBy(WineName::getName))
            .forEach((name, list) -> writeValue(name, list, fw));

        fw.close();
    }

    private void writeValue(final String name, final List<WineName> list,
            final FileWriter fw) {
        WineNameReduced wnr = new WineNameReduced();
        wnr.setName(name);
        wnr.setList(new ArrayList<YearId>());
        list.forEach(wn -> {
            wnr.getList().add(new YearId(wn.getYear(), wn.getId()));
        });
        try {
            fw.write(om.writeValueAsString(wnr) + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WineName readValue(final String s) {
        try {
            return om.readValue(s, WineName.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(final String... args) throws Exception {
        WineNameReducer wnr = new WineNameReducer();
        wnr.reduce("C:\\Users\\amancheno\\Dropbox\\Master\\Proyecto\\wineNames.json");
    }

}

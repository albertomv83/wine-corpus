package es.instavino.wine.reducer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.ObjectMapper;

import es.instavino.wine.db.model.AppellationMatch;
import es.instavino.wine.db.model.GrapeMatch;
import es.instavino.wine.db.model.WineMatch;
import es.instavino.wine.db.model.WineNameReduced;

public class WineNameMatchCreater {

    private static List<String> exclude = Arrays.asList(new String[] {
            "bottle", "half", "375ml", "15l" });

    private final ObjectMapper om = new ObjectMapper();

    private final Set<String> matches = new TreeSet<String>();

    private final Set<List<String>> pairMatches =
        new TreeSet<List<String>>((final List<String> l1,
                final List<String> l2) -> {
                        if (l1.size() != l2.size()) {
                            return -1;
                        } else {
                            boolean equals = true;
                            for (String term : l1) {
                                equals = equals && l2.contains(term);
                            }
                            return equals ? 0 : -1;
                        }
                    });

    public void readAndCreateMatches(final String path) throws IOException {

        List<AppellationMatch> appellations =
            Files
                .lines(
                    Paths
                        .get("C:\\Users\\amancheno\\Dropbox\\Master\\Proyecto\\appellations.json"),
                    Charset.forName("UTF-8"))
                .map(line -> createAppellationMatch(line))
                .collect(Collectors.toList());

        List<GrapeMatch> grapes =
            Files
                .lines(
                    Paths
                        .get("C:\\Users\\amancheno\\Dropbox\\Master\\Proyecto\\grapeTypes.json"),
                    Charset.forName("UTF-8"))
                .map(line -> createGrapeMatch(line))
                .collect(Collectors.toList());

        appellations
            .stream()
            .filter(appellation -> appellation.getMatches() != null)
            .forEach(
                appellation -> matches.addAll(appellation.getMatches()));

        appellations
            .stream()
            .filter(appellation -> appellation.getPairMatches() != null)
            .forEach(
                appellation -> {
                    pairMatches.addAll(appellation.getPairMatches());
                    appellation.getPairMatches().forEach(
                        sublist -> matches.addAll(sublist));
                });

        grapes.stream().filter(grape -> grape.getMatches() != null)
            .forEach(grape -> matches.addAll(grape.getMatches()));

        grapes
            .stream()
            .filter(grape -> grape.getPairMatches() != null)
            .forEach(
                grape -> {
                    pairMatches.addAll(grape.getPairMatches());
                    grape.getPairMatches().forEach(
                        sublist -> matches.addAll(sublist));
                });

        File result = new File(path + ".matched");
        result.createNewFile();
        FileWriter fw = new FileWriter(result);

        Files.lines(Paths.get(path), Charset.forName("UTF-8"))
            .map(line -> createMatches(line)).collect(Collectors.toList())
            .forEach(wine -> writeValue(wine, fw));

        fw.close();
    }

    private AppellationMatch createAppellationMatch(final String line) {
        try {
            // System.out.println(line);
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

    private void writeValue(final WineMatch wine, final FileWriter fw) {
        try {
            fw.write(om.writeValueAsString(wine) + "\r\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WineMatch createMatches(final String line) {
        try {
            WineNameReduced wine =
                om.readValue(line, WineNameReduced.class);
            WineMatch wm = new WineMatch();
            wm.setList(wine.getList());
            wm.setName(wine.getName());
            String name = wine.getName().trim().toLowerCase();
            name = name.replace(")", "");
            name = name.replace("(", "");
            name = name.replace("?", "");
            name = name.replace("¿", "");
            name = name.replace("!", "");
            name = name.replace("!", "");
            name = name.replace("' ", " ");
            name = name.replace(" '", " ");
            name = name.replace("'", " ");
            name = name.replace("- ", " ");
            name = name.replace(" -", " ");
            name = name.replace("-", " ");
            name = name.replace("\\", "");
            name = name.replace("/", "");
            name = name.replace(".", "");
            name = name.replace(",", "");
            System.out.println(name);
            String[] nameSplits = name.split(" ");
            // create simple matches
            List<String> goodMatches = new ArrayList<String>();
            for (String match : nameSplits) {
                if (!exclude.contains(match)) {
                    if (!matches.contains(match)) {
                        goodMatches.add(match);
                    }
                }
            }
            wm.setMatches(goodMatches);
            // create concatenated matches
            // for (int i = 2; i <= goodMatches.size(); i++) {
            List<String> partialResult =
                    createConcatenatedMatches(2, goodMatches);
            wm.getMatches().addAll(partialResult);
            // }

            return wm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param i
     * @param goodMatches
     * @return
     */
    private List<String> createConcatenatedMatches(final int groups,
            final List<String> goodMatches) {
        List<String> result = new ArrayList<String>();
        int position = 0;
        StringBuffer sb = new StringBuffer("");
        while (position + groups <= goodMatches.size()) {
            for (int i = 0; i <= groups - 1; i++) {
                sb.append(goodMatches.get(position + i));
            }
            result.add(sb.toString());
            sb.delete(0, sb.length());
            position++;
        }
        return result;
    }

    public static void main(final String... args) throws Exception {
        WineNameMatchCreater wnr = new WineNameMatchCreater();
        wnr.readAndCreateMatches("C:\\Users\\amancheno\\Dropbox\\Master\\Proyecto\\wineNames.json.reduced");
    }

}

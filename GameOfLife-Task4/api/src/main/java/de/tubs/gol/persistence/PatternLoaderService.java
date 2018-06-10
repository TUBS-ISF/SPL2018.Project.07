package de.tubs.gol.persistence;

import de.tubs.gol.core.Cell;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Tino on 06.03.2016.
 */
public class PatternLoaderService {

    private final static String RESOURCEFOLDER = "/contextmenu";

    public List<ResourceFigure> loadBuildInFigures() {
        final List<ResourceFigure> figures = new ArrayList<>();

        URL resource = getClass().getResource(RESOURCEFOLDER);
        File folder = null;

        try {
            folder = new File(resource.toURI());

            if (folder.exists()) {
                for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
//                    System.out.println(fileEntry.getName());
                    figures.add(loadResourceFigure(fileEntry));
                }
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return figures;
    }

    private ResourceFigure loadResourceFigure(final File resource) throws IOException {
        final ResourceFigure figure = new ResourceFigure(resource.getName().substring(0, resource.getName().indexOf(".")));

        InputStream preis = new FileInputStream(resource);
        BufferedReader prereader = new BufferedReader(new InputStreamReader(preis));

        InputStream is = new FileInputStream(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        int maxWidth = Integer.MAX_VALUE;
        int maxHeight = Integer.MAX_VALUE;

        String line;
        while ((line = prereader.readLine()) != null) {
            if (line.startsWith("#")) continue;

            String[] split = line.split(" ");
            int width = Integer.valueOf(split[0]);
            int height = Integer.valueOf(split[1]);

            maxWidth = width < maxWidth ? width : maxWidth;
            maxHeight = height < maxHeight ? height : maxHeight;
        }

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) continue;

            figure.getCells().add(asCell(line, maxWidth, maxHeight));
        }

        return figure;
    }

    private Cell asCell(final String line, int maxWidth, int maxHeight) {
        final String[] coordinates = line.split("\\s");
        final int x = Integer.valueOf(coordinates[0]) - maxWidth;
        final int y = Integer.valueOf(coordinates[1]) - maxHeight;

//        System.out.print(x + " ");
//        System.out.println(y);

        return new Cell(x, y);
    }
}

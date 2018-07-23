package de.tubs.gol;

import de.tubs.gol.persistence.PatternLoaderService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public aspect Puffers {

    private final static String RESOURCEFOLDER = "/contextmenu";

    declare precedence: FromFiles;

    before(): execution(void de.tubs.gol.persistence.PatternLoaderService.loadBuildInFigures()) {
        URL resource = getClass().getResource(RESOURCEFOLDER);
        File folder = null;

        try {
            folder = new File(resource.toURI());

            if (folder.exists()) {
                for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                    if (fileEntry.getName().contains("Glider")) {
                        PatternLoaderService.figures.add(PatternLoaderService.loadResourceFigure(fileEntry));
                    }
                }
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

}

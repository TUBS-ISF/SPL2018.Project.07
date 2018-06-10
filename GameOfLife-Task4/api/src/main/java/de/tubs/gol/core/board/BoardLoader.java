package de.tubs.gol.core.board;

import java.util.*;

public class BoardLoader {

    private static final Map<String, Board> boards = loadHashFunctionFactories();

    private static Map<String, Board> loadHashFunctionFactories() {
        final Map<String, Board> factories = new HashMap<>();

        final ServiceLoader<Board> serviceLoader = ServiceLoader.load(Board.class);

        for (Board factory : serviceLoader) {
            factories.put(factory.getClass().getName(), factory);
        }

        return factories;
    }

    public static Board getBoard(final String functionName) {
        final Board board = boards.get(functionName);
        if (board == null) {
            throw new RuntimeException(String.format("Board %s not available", functionName));
        }
        return board;
    }

    public static Set<String> getAvailableBoards() {
        return new HashSet<>(boards.keySet());
    }
}

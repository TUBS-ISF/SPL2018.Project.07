package de.tubs.gol.core.board;

/**
 * Created by Tino on 18.02.2016.
 */
public class BoardPainterFactory {

    public BoardPainter build(final Board board, final double width, final double height, boolean heatmapFlag) {
        if (board instanceof BoundedBoard) {
            return new BoundedBoardPainter((BoundedBoard) board, width, height, heatmapFlag);
        }
        else if (board instanceof EndlessBoard) {
            return new EndlessBoardPainter((EndlessBoard) board, width, height, heatmapFlag);
        }
        throw new IllegalStateException("Board not available");
    }
}

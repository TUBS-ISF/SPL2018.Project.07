package de.tubs.gol.core.board;

import de.tubs.gol.core.board.endless.EndlessBoard;
import de.tubs.gol.core.board.endless.EndlessBoardPainter;
import de.tubs.gol.core.board.fixed.FixedBoard;
import de.tubs.gol.core.board.fixed.FixedBoardPainter;
import de.tubs.gol.core.board.torus.TorusBoard;
import de.tubs.gol.core.board.torus.TorusBoardPainter;

/**
 * Created by Tino on 18.02.2016.
 */
public class BoardPainterFactory {

    public BoardPainter build(final Board board, final double width, final double height, boolean heatmapFlag) {




        if (board instanceof TorusBoard) {
            return new TorusBoardPainter((TorusBoard) board, width, height, heatmapFlag);
        }


        if (board instanceof FixedBoard) {
            return new FixedBoardPainter((FixedBoard) board, width, height, heatmapFlag);
        }


        else if (board instanceof EndlessBoard) {
            return new EndlessBoardPainter((EndlessBoard) board, width, height, heatmapFlag);
        }


        throw new IllegalStateException("Board not available");
    }
}

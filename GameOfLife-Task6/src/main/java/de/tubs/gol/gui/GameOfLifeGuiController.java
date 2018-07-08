package de.tubs.gol.gui;

import de.tubs.gol.core.board.*;
import de.tubs.gol.core.Cell;
import de.tubs.gol.core.board.endless.EndlessBoard;
import de.tubs.gol.core.board.fixed.FixedBoard;
import de.tubs.gol.core.board.torus.TorusBoard;
import de.tubs.gol.persistence.ConversionService;
import de.tubs.gol.persistence.PersistenceService;
import de.tubs.gol.persistence.ResourceFigure;
import de.tubs.gol.persistence.PatternLoaderService;
import de.tubs.gol.persistence.XmlGameOfLifeState;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by Tino on 21.01.2016.
 */
public class GameOfLifeGuiController implements Initializable {

    private final static int NAVIGATION_STEP_SIZE = 5;

    @FXML
    private Button openBtn;
    @FXML
    private MenuButton newBtn;
    @FXML
    private MenuItem newBoundedFieldItem;
    @FXML
    private MenuItem newTorusFieldItem;
    @FXML
    private MenuItem newEndlessFieldItem;
    @FXML
    private Button saveBtn;

    @FXML
    Pane canvasHolder;
    private Canvas canvas;

    @FXML
    private Button nextStepBtn;
    @FXML
    private Button playBtn;
    @FXML
    private Button pauseBtn;
    @FXML
    private Label generationLabel;
    @FXML
    private Slider durationSlider;
    @FXML
    private Button leftBtn;
    @FXML
    private Button rightBtn;
    @FXML
    private Button upBtn;
    @FXML
    private Button downBtn;

    private final ContextMenu contextMenu = new ContextMenu();
    private Optional<Cell> cellForContextMenu = Optional.empty();

    private BoardPainter boardPainter;

    private Board board;
    private StepTimer timer;

    private ConversionService conversionService = new ConversionService();
    private PersistenceService persistenceService = new PersistenceService();
    private PatternLoaderService resourceLoaderService = new PatternLoaderService();
    private DialogSupport dialogSupport = new DialogSupport();
    private List<String> parameters;
    private boolean dragBool;
    private boolean heatmapFlag;

    private void initContextMenu() {
        final List<ResourceFigure> resourceFigures = resourceLoaderService.loadBuildInFigures();
        for (ResourceFigure resourceFigure : resourceFigures) {

            final MenuItem item = new MenuItem(resourceFigure.getName());
            contextMenu.getItems().add(item);

            item.setOnAction((ActionEvent event) -> {
                final Optional<ResourceFigure> editedFigure = dialogSupport.getResourceFigureEditedByUser(resourceFigure);
                if (editedFigure.isPresent()) {
                    addResourceFigureToBoard(editedFigure.get());
                }
            });
        }

        // Heatmap
        this.heatmapFlag = parameters.contains("heatmap");

        // Fields
        ObservableList<MenuItem> fields = newBtn.getItems();

        Set<String> availableBoards = BoardLoader.getAvailableBoards();
        availableBoards.forEach(s -> {
            MenuItem boardItem = new MenuItem(s);
            fields.add(boardItem);
            boardItem.setOnAction(event -> newBoard(s));
        });
    }

    private void initBindings() {
        durationSlider.valueProperty().bindBidirectional(timer.stepDurationProperty());

        canvas.widthProperty().bind(canvasHolder.widthProperty());
        canvas.heightProperty().bind(canvasHolder.heightProperty());
        canvas.widthProperty().addListener(event -> paint());
        canvas.heightProperty().addListener(event -> paint());
    }

    private void initListener() {
        canvas.setOnMouseClicked(event2 -> onCellClicked(event2, false));
        canvas.setOnMouseReleased(event -> {
            dragBool = false;
//            System.out.println("RELEASE");
        });
        canvas.setOnMousePressed(event -> {
            Optional<Cell> cellOnBoard = getCellOnBoard(event);
            if (cellOnBoard.isPresent()) {
                dragBool = board.cellIsAlive(cellOnBoard.get());
//                System.out.println("ENTERED ON: " + dragBool);
            }
        });
        canvas.setOnMouseDragged(event1 -> {
            if (event1.isPrimaryButtonDown()) {
                onCellClicked(event1, true);
            }
        });

        canvas.setOnScroll((ScrollEvent event) -> {
            if (!painterIsAvailable()) {
                return;
            }

            if (event.getDeltaY() < 0) {
                final int newWidth = boardPainter.cellWidthProperty().get() - 1;
                if (newWidth > 0) {
                    boardPainter.cellWidthProperty().set(newWidth);
                }
            } else {
                final int newWidth = boardPainter.cellWidthProperty().get() + 1;
                boardPainter.cellWidthProperty().set(newWidth);
            }

            paint();
        });
    }

    private void onCellClicked(MouseEvent event, boolean dragFlag) {
        contextMenu.hide();
        cellForContextMenu = Optional.empty();

        final Optional<Cell> clickedCell = getCellOnBoard(event);
        if (!clickedCell.isPresent()) {
            return;
        }

        if (event.isPopupTrigger()) {
            cellForContextMenu = clickedCell;
            contextMenu.show(canvas, event.getScreenX(), event.getScreenY());
        } else {
            handleUserEditCell(clickedCell.get(), dragFlag);
        }
    }

    private void handleUserEditCell(final Cell clickedCell, boolean dragFlag) {
        boolean active = dragFlag ? dragBool : board.cellIsAlive(clickedCell);

        if (active) {
            board.remove(clickedCell);
        } else {
            board.add(clickedCell);
        }

        paint();
    }

    private void addResourceFigureToBoard(final ResourceFigure figure) {
        if (!cellForContextMenu.isPresent()) {
            return;
        }

        final Cell userClickedCell = cellForContextMenu.get();

        for (Cell cell : figure.getCells()) {
            final Cell newCell = new Cell(userClickedCell.getX() + cell.getX(),
                    userClickedCell.getY() + cell.getY());
            board.add(newCell);
        }

        paint();
    }

    private Optional<Cell> getCellOnBoard(final MouseEvent event) {
        if (!painterIsAvailable()) {
            return Optional.empty();
        }
        final Point2D boardPos = boardPainter.getPosOnBoard(event.getX(), event.getY());
        return boardPainter.getCellAt(boardPos);
    }


    public void paint() {
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        Color col;
        if (!heatmapFlag) {
            col = Color.WHITE;
        } else {
            col = Color.WHITE.deriveColor(0, 0, 100, 0.1);
        }

        gc.setFill(col);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (painterIsAvailable()) {
            boardPainter.paint(gc);
        }
    }

    private BoardPainter createAndBindPainter(final Board board) {
        boardPainter = new BoardPainterFactory().build(board, canvas.getWidth(), canvas.getHeight(), heatmapFlag);
        boardPainter.setViewPortX(0);
        boardPainter.setViewPortY(0);
        boardPainter.viewPortWidthProperty().bind(canvas.widthProperty());
        boardPainter.viewPortHeightProperty().bind(canvas.heightProperty());
        return boardPainter;
    }

    private boolean painterIsAvailable() {
        return boardPainter != null;
    }

    private boolean boardIsEmpty() {
        if (board == null) {
            return true;
        }
        return board.countCells() == 0;
    }

    private boolean userPreventClearingOfBoard() {
        if (boardIsEmpty()) {
            return false;
        }
        final boolean discardBoard = dialogSupport.askUserForDiscardBoard();
        if (discardBoard) {
            return false;
        }
        return true;
    }

    private void clearBoard() {
        if (board != null) {
            board.clear();
        }
    }

    private void initBoard(final Board newBoard) {
        boardPainter = createAndBindPainter(newBoard);
        this.board = newBoard;

        updateBoardInfos();
        calculateToolbarStatus();

        paint();
    }

    private void newBoard(String boardName) {
        if (userPreventClearingOfBoard()) {
            return;
        }

        clearBoard();
        Board board = BoardLoader.getBoard(boardName);

        if (board.isBounded()) {
            final Optional<BoardBounds> bounds = dialogSupport.getBoundsFromUser();

            if (!bounds.isPresent()) {
                return;
            }

            board.setWidth(bounds.get().getWidth());
            board.setHeight(bounds.get().getHeight());
        }

        initBoard(board);
    }

    @FXML
    private void doSave() {
        final Optional<Path> fileOption = dialogSupport.selectPathForSave();
        if (!fileOption.isPresent()) {
            return;
        }

        saveGameState(fileOption.get());
    }

    private void saveGameState(final Path file) {
        final XmlGameOfLifeState gameState = conversionService.convert(board);
        persistenceService.save(file, gameState);
    }

    @FXML
    private void doOpen() {
        if (userPreventClearingOfBoard()) {
            return;
        }

        final Optional<Path> fileOption = dialogSupport.selectPathForOpen();
        if (!fileOption.isPresent()) {
            return;
        }

        loadGameState(fileOption.get());
    }

    private void loadGameState(final Path file) {
        if (board != null) {
            board.clear();
        }

        final XmlGameOfLifeState gameState = persistenceService.load(file);
        board = conversionService.convert(gameState);
        initBoard(board);
    }

    @FXML
    private void doNextStep() {
        board.nextRound();
        updateBoardInfos();

        paint();
    }

    private void updateBoardInfos() {
        generationLabel.setText(String.valueOf(board.getCurrentGeneration()));
    }

    @FXML
    private void doPlay() {
        timer.start();
        calculateToolbarStatus();
    }

    @FXML
    private void doPause() {
        timer.stop();
        calculateToolbarStatus();
    }

    @FXML
    private void navigateLeft() {
        boardPainter.setViewPortX(boardPainter.getViewPortX() - NAVIGATION_STEP_SIZE);
        paint();
    }

    @FXML
    private void navigateRight() {
        boardPainter.setViewPortX(boardPainter.getViewPortX() + NAVIGATION_STEP_SIZE);
        paint();
    }

    @FXML
    private void navigateUp() {
        boardPainter.setViewPortY(boardPainter.getViewPortY() - NAVIGATION_STEP_SIZE);
        paint();
    }

    @FXML
    private void navigateDown() {
        boardPainter.setViewPortY(boardPainter.getViewPortY() + NAVIGATION_STEP_SIZE);
        paint();
    }

    private void calculateToolbarStatus() {
        openBtn.setDisable(timer.isRunning());
        newBtn.setDisable(timer.isRunning());
        saveBtn.setDisable(timer.isRunning() || !painterIsAvailable());
        nextStepBtn.setDisable(timer.isRunning() || !painterIsAvailable());
        playBtn.setDisable(timer.isRunning() || !painterIsAvailable());
        pauseBtn.setDisable(!timer.isRunning() || !painterIsAvailable());
        leftBtn.setDisable(!painterIsAvailable());
        rightBtn.setDisable(!painterIsAvailable());
        upBtn.setDisable(!painterIsAvailable());
        downBtn.setDisable(!painterIsAvailable());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Wait for Parameters
        Platform.runLater(() -> {
            canvas = new ResizableCanvas();
            canvasHolder.getChildren().add(canvas);
            paint();

            timer = new StepTimer(500, () -> doNextStep());

            initContextMenu();
            initBindings();
            initListener();

            calculateToolbarStatus();
        });
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}

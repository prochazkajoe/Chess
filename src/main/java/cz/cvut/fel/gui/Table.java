package cz.cvut.fel.gui;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.Square;
import cz.cvut.fel.pieces.Piece;
import cz.cvut.fel.player.MoveMaker;
import cz.cvut.fel.player.ai.MiniMax;
import cz.cvut.fel.player.ai.MoveStrategy;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * Table class for chess board.
 *
 * @author frogp
 */
public class Table extends Observable {

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private static String defaultPieceIconPath = "graphic/PieceIcons/";
    private final GameSetup gameSetup;

    private Square sourceSquare;
    private Square destinationSquare;
    private Piece humanMovedPiece;

    private boolean highlightValidMoves;
    private Move computerMove;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension SQUARE_PANEL_DIMENSION = new Dimension(10, 10);

    public static final Table INSTANCE = new Table();
    
    private static final Logger LOGGER = Logger.getLogger(Table.class.getName());

    private Table() {
        this.gameFrame = new JFrame("ULTIMATE MEGA CHESS 0.99");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createClassicBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.highlightValidMoves = false;
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.addObserver(new AIWatcher());
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply = JOptionPane.showConfirmDialog(Table.get().getBoardPanel(),
                        "This will create new game, current progres will be lost. Do you want to continue?");
                if (reply == 0) {
                    System.out.println("Starting new game!");
                    chessBoard = Board.createClassicBoard();
                    Table.get().getBoardPanel().drawBoard(chessBoard);
                }
            }
        });
        fileMenu.add(newGame);

        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exiting!");
                System.exit(0);
            }
        });
        fileMenu.add(exit);

        return fileMenu;
    }

    public static Table get() {
        return INSTANCE;
    }

    public void show() {
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private GameSetup getGameSetup() {
        return this.gameSetup;
    }

    private JMenu createPreferencesMenu() {
        final JMenu prefMenu = new JMenu("Preferences");
        final JCheckBoxMenuItem highlightValidMovesCheckBox
                = new JCheckBoxMenuItem("Highlight Valid Moves", false);

        highlightValidMovesCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightValidMoves = highlightValidMovesCheckBox.isSelected();
            }
        });

        prefMenu.add(highlightValidMovesCheckBox);
        return prefMenu;
    }

    private JMenu createOptionMenu() {
        final JMenu optionMenu = new JMenu("Options");

        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });

        optionMenu.add(setupGameMenuItem);

        return optionMenu;
    }

    private void setupUpdate(GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class AIWatcher implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer())
                    && !Table.get().getGameBoard().getCurrentPlayer().isInCheckMate()
                    && !Table.get().getGameBoard().getCurrentPlayer().isInStaleMate()) {
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }
        }
    }

    public void updateGameBoard(Board board) {
        this.chessBoard = board;
    }

    public void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    private void moveMadeUpdate(PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    private static class AIThinkTank extends SwingWorker<Move, String> {

        private AIThinkTank() {

        }

        @Override
        protected Move doInBackground() throws Exception {

            final MoveStrategy minimax = new MiniMax(Table.get().getGameSetup().getSearchDepth());
            final Move bestMove = minimax.execute(Table.get().getGameBoard());
            return bestMove;
        }

        @Override
        public void done() {
            try {
                final Move bestMove = get();

                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().getCurrentPlayer().makeMove(bestMove).getBoard());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
                System.out.println(Table.get().getGameBoard());
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.log( Level.SEVERE, e.toString(), e );
            }
        }
    }

    private class BoardPanel extends JPanel {

        final List<SquarePanel> boardSquares;

        public BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardSquares = new ArrayList<>();
            for (int i = 0; i < 64; i++) {
                final SquarePanel squarePanel = new SquarePanel(this, i);
                this.boardSquares.add(squarePanel);
                add(squarePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.decode("#8B4726"));
            validate();
        }

        private void drawBoard(Board board) {
            removeAll();
            for (SquarePanel squarePanel : boardSquares) {
                squarePanel.drawSquare(board);
                add(squarePanel);
            }
            validate();
            repaint();
        }
    }

    private class SquarePanel extends JPanel {

        private final int squareID;

        public SquarePanel(final BoardPanel boardPanel, final int squareID) {
            super(new GridBagLayout());
            this.squareID = squareID;
            setPreferredSize(SQUARE_PANEL_DIMENSION);
            assignSquareColour();
            assignSquarePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        sourceSquare = null;
                        destinationSquare = null;
                        humanMovedPiece = null;
                        computerMove = null;
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        if (sourceSquare == null) {
                            // first click
                            sourceSquare = chessBoard.getSquare(squareID);
                            humanMovedPiece = sourceSquare.getPiece();
                            if (humanMovedPiece == null) {
                                sourceSquare = null;
                            }
                        } else {
                            // second click
                            destinationSquare = chessBoard.getSquare(squareID);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceSquare.getSquareCoordinate(),
                                    destinationSquare.getSquareCoordinate());
                            final MoveMaker maker = chessBoard.getCurrentPlayer().makeMove(move);
                            if (maker.getMoveStatus().isDone()) {
                                chessBoard = maker.getBoard();
                                System.out.println("Making new move!");
                                System.out.println(chessBoard);
                            }
                            sourceSquare = null;
                            destinationSquare = null;
                            humanMovedPiece = null;
                        }

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (gameSetup.isAIPlayer(chessBoard.getCurrentPlayer())) {
                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(chessBoard);
                                if (chessBoard.getCurrentPlayer().isInCheckMate()) {
                                    JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                                            "Game Over: " + Table.get().getGameBoard().getCurrentPlayer().getColour()
                                            + " Player is in checkmate!", "Game Over",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    System.out.println("Game Over!" + Table.get().getGameBoard().getCurrentPlayer()
                                            + " is in Checkmate!");
                                }

                                if (chessBoard.getCurrentPlayer().isInStaleMate()) {
                                    JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                                            "Game Over: " + Table.get().getGameBoard().getCurrentPlayer().getColour()
                                            + " Player is in stalemate!", "Game Over",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    System.out.println("GameOver!" + Table.get().getGameBoard().getCurrentPlayer()
                                            + " is in Stalemate!");
                                }
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            validate();
        }

        public void drawSquare(final Board board) {
            assignSquareColour();
            assignSquarePieceIcon(board);
            highlightValidSquares(board);
            highlightComputerMove();
            highlightSourceSquare();
            highlightCheck();
            validate();
        }

        private void assignSquarePieceIcon(final Board board) {
            this.removeAll();
            if (board.getSquare(this.squareID).isOccupied()) {
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultPieceIconPath
                            + board.getSquare(this.squareID).getPiece().getColour().toString().substring(0, 1)
                            + board.getSquare(this.squareID).getPiece().toString() + ".gif"));
                    this.add(new JLabel(new ImageIcon(image)));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.log( Level.SEVERE, e.toString(), e );
                }
            }
        }

        private void highlightValidSquares(final Board board) {
            if (highlightValidMoves) {
                for (final Move move : pieceValidMoves(board)) {
                    if (move.getDestination() == this.squareID) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("graphic/green_dot.png")))));
                        } catch (Exception e) {
                            e.printStackTrace();
                            LOGGER.log( Level.SEVERE, e.toString(), e );
                        }
                    }
                }
            }
        }

        private void highlightComputerMove() {
            if (computerMove != null) {
                if (this.squareID == computerMove.getDestination()) {
                    setBackground(Color.pink);
                } else if (this.squareID == computerMove.getDestination()) {
                    setBackground(Color.red);
                }
            }
        }

        private void highlightCheck() {
            if (Table.get().getGameBoard().getCurrentPlayer().isInCheck()) {
                if (this.squareID == Table.get().getGameBoard().getCurrentPlayer().getKing().getSquareID()) {
                    setBackground(Color.RED);
                }
            }
        }

        private void highlightSourceSquare() {
            if (sourceSquare != null && this.squareID == sourceSquare.getSquareCoordinate()) {
                setBackground(Color.YELLOW);
            }
        }

        private Collection<Move> pieceValidMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getColour() == board.getCurrentPlayer().getColour()) {
                return humanMovedPiece.findValidMoves(board);
            }
            return Collections.emptyList();

        }

        private void assignSquareColour() {
            final int squareX = this.squareID / 8;
            final int squareY = this.squareID % 8;
            setBackground((squareX + squareY) % 2 == 0 ? Color.decode("#FFFACD") : Color.decode("#593324"));
        }
    }

    enum PlayerType {
        HUMAN,
        COMPUTER
    }
}

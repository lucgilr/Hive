package lucgilr.inf.uva.hivev2.AI;

import lucgilr.inf.uva.hivev2.GameModel.Hexagon;
import lucgilr.inf.uva.hivev2.GameModel.Piece;

/**
 * @author Lucía Gil Román
 *
 * Used in the AI class.
 * Created to combine Piece, possible gaps where it can be moved and the score of the new position.
 * score will be used to choose the best move.
 */
public class PieceMoveScore {

    private Piece piece;
    private Hexagon hexagon;
    private int score;

    public PieceMoveScore(){
        this.piece =null;
        this.hexagon =null;
        this.score =0;
    }

    public PieceMoveScore(Piece piece, Hexagon hexagon, int score){
        this.piece = piece;
        this.hexagon = hexagon;
        this.score = score;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Hexagon getHexagon() {
        return hexagon;
    }

    public void setHexagon(Hexagon hexagon) {
        this.hexagon = hexagon;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

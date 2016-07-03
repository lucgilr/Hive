package lucgilr.inf.uva.hivev2.AI;

import lucgilr.inf.uva.hivev2.GameModel.Hexagon;
import lucgilr.inf.uva.hivev2.GameModel.Piece;

/**
 * @author Lucía Gil Román (https://github.com/lucgilr)
 *
 * Used in the AI class.
 * Created to combine Piece, possible gaps where it can be moved and the score of the new position.
 * score will be used to choose the best move.
 */
class PieceMoveScore {

    private final Piece piece;
    private final Hexagon hexagon;
    private final int score;

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

    public Hexagon getHexagon() {
        return hexagon;
    }

    public int getScore() {
        return score;
    }

    public String getInfo(){
        return "piece: "+piece.pieceInfo()+" hexagon: "+hexagon.toString()+" score: "+score;
    }
}

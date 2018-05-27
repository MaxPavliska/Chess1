package pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import board.Board;
import board.BoardUtils;
import board.Move;
import board.Tile;

public class King extends Piece {
	
	private final static int[] CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

	public King(final Alliance pieceAlliance,
				final int piecePosition) {
		super(PieceType.KING, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
			if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
				isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
				continue;
			}
			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);	
				if (!candidateDestinationTile.isTileOccupied()) {
					legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
				} else {
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
					if (this.pieceAlliance != pieceAlliance) {
						legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
					}
				}
			}
			
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidatesOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidatesOffset == -9 || candidatesOffset == -1 ||
				candidatesOffset == 7);
	}
	
	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 ||
				candidateOffset == 9);
	}
}

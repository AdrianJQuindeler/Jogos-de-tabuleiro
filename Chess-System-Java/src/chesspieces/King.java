package chesspieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{
	
	private ChessMatch chessMatch;
	
	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}
	
	private boolean canMove(Position position){
	    ChessPiece p = (ChessPiece)getBoard().getPiece(position);
	    return p == null || p.getColor() != this.getColor();
	}
	
	private boolean testRookCastling(Position position){
	    ChessPiece p = (ChessPiece)getBoard().getPiece(position);
	    return p != null && p instanceof Rook && p.getColor() == this.getColor() && p.getMoveCount() == 0;
	}
	
	@Override
	public String toString() {
		return "O";	
	}

	@Override
	public boolean[][] possibleMoves() {
		
		boolean[][] mat = new boolean[getBoard().getRow()][getBoard().getColumn()];
		Position p = new Position(0,0);
		
		//Up
		p.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }

		//Up - Right
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }

		//Up - Left
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }

		//Right
		p.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }

		//Left
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }
		
		//Down - Right
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }
		
		//Down
		p.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }
		
		//Down - Left
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }
		
		//Castling
		if(getMoveCount() == 0 && !chessMatch.isCheck()) {
			
			//KingSide
			p.setValues(position.getRow(), position.getColumn() + 3);
			if(testRookCastling(p)) {
				
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				
				if(getBoard().getPiece(p1) == null && getBoard().getPiece(p2) == null) {
					mat[position.getRow()][position.getColumn() + 2] = true;
				}
			}
			
			//QueenSide
			p.setValues(position.getRow(), position.getColumn() - 4);
			if(testRookCastling(p)) {
				
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);
				
				if(getBoard().getPiece(p1) == null && getBoard().getPiece(p2) == null && getBoard().getPiece(p3) == null) {
					mat[position.getRow()][position.getColumn() - 2] = true;
				}
			}
		}
		
		return mat;
	}
}
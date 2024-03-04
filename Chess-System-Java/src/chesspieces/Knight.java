package chesspieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {

	public Knight(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "L";	
	}
	
	public boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().getPiece(position);
		return p == null || p.getColor() != getColor();
	}

	@Override
	public boolean[][] possibleMoves() {
		
		boolean[][] mat = new boolean[getBoard().getRow()][getBoard().getColumn()];
		Position p = new Position(0,0);
		
		//Up - Right
		p.setValues(position.getRow() - 2, position.getColumn() + 1);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }

		//Up - Left
		p.setValues(position.getRow() - 2, position.getColumn() - 1);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }

		//Down - Right
		p.setValues(position.getRow() + 2, position.getColumn() + 1);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }

		//Down - Left
		p.setValues(position.getRow() + 2, position.getColumn() - 1);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }

		//Right - Up
		p.setValues(position.getRow() - 1, position.getColumn() + 2);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }
		
		//Right - Down
		p.setValues(position.getRow() + 1, position.getColumn() + 2);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }
		
		//Left - Up
		p.setValues(position.getRow() - 1, position.getColumn() - 2);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }
		
		//Left - Down
		p.setValues(position.getRow() + 1, position.getColumn() - 2);
		if (getBoard().positionExist(p) && canMove(p)) { mat[p.getRow()][p.getColumn()] = true; }
		
		return mat;
	}
}
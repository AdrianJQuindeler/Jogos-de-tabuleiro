package chesspieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece{

	public Queen(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "*";	
	}

	@Override
	public boolean[][] possibleMoves() {
		
		boolean[][] mat = new boolean[getBoard().getRow()][getBoard().getColumn()];
		Position p = new Position(0,0);
		
		//Up
		p.setValues(position.getRow() - 1, position.getColumn());
		while(getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
		}
		if(getBoard().positionExist(p) && isThereOpponentPiece(p)) {mat[p.getRow()][p.getColumn()] = true;}
		
		//Down
		p.setValues(position.getRow() + 1, position.getColumn());
		while(getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
		}
		if(getBoard().positionExist(p) && isThereOpponentPiece(p)) {mat[p.getRow()][p.getColumn()] = true;}
		
		//Right
		p.setValues(position.getRow(), position.getColumn() + 1);
		while(getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);
		}
		if(getBoard().positionExist(p) && isThereOpponentPiece(p)) {mat[p.getRow()][p.getColumn()] = true;}
		
		//Left
		p.setValues(position.getRow(), position.getColumn() - 1);
		while(getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1);
		}
		if(getBoard().positionExist(p) && isThereOpponentPiece(p)) {mat[p.getRow()][p.getColumn()] = true;}
		
		//Up - Right
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		while (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() + 1);
		}
		if(getBoard().positionExist(p) && isThereOpponentPiece(p)) {mat[p.getRow()][p.getColumn()] = true;}
		
		//Up - Left
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		while (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() - 1);
		}
		if(getBoard().positionExist(p) && isThereOpponentPiece(p)) {mat[p.getRow()][p.getColumn()] = true;}
		
		//Down - Right
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		while (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() + 1);
		}
		if(getBoard().positionExist(p) && isThereOpponentPiece(p)) {mat[p.getRow()][p.getColumn()] = true;}
		
		//Down - Left
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		while (getBoard().positionExist(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() - 1);
		}
		if(getBoard().positionExist(p) && isThereOpponentPiece(p)) {mat[p.getRow()][p.getColumn()] = true;}
		
		return mat;
	}
}
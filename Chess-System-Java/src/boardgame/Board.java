package boardgame;

public class Board {
	
	private int row;
	private int column;
	private Piece[][] pieces;
	
	public Board(int row, int column) {
	    if (row <= 0 || column <= 0) { throw new BoardException("The board must have at least one row and one column"); }
	    else {
	        this.row = row;
		    this.column = column;
		    pieces = new Piece[row][column];
	    }
	}
	
	public void placePiece(Piece piece, Position position) {
		if(thereIsAPiece(position)) { throw new BoardException("There is alredy a piece in this position"); }
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	public Piece removePiece(Position position){
		if(!positionExist(position)) { throw new BoardException("Position not on the board"); }
		if(getPiece(position) == null) { return null; }
		Piece aux = getPiece(position);
		aux.position = null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
	}
	
	public boolean positionExist(int row, int column){
	    return row < getRow() && row >= 0 && column < getColumn() && column >= 0;
	}
	
	public boolean positionExist(Position position){
	    return positionExist(position.getRow(), position.getColumn());
	}
	
	public boolean thereIsAPiece(Position position){
		if (!positionExist(position)) { throw new BoardException("Position not on the board"); }
	    return getPiece(position.getRow(), position.getColumn()) != null;
	}
	
	public Piece getPiece(int row, int column) {
		if (!positionExist(row, column)) { throw new BoardException("Position not on the board"); }
		return pieces[row][column];
	}
	
	public Piece getPiece(Position position) {
		if (!positionExist(position)) { throw new BoardException("Position not on the board"); }
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

}

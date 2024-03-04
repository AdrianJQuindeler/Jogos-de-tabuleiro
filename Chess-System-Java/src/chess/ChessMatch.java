package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chesspieces.Bishop;
import chesspieces.King;
import chesspieces.Knight;
import chesspieces.Pawn;
import chesspieces.Queen;
import chesspieces.Rook;

public class ChessMatch {
	private int turn;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private Color currentPlayer;
	private ChessPiece promoted;
	private ChessPiece enPassantVulnerable;
	private List<Piece> capturedPieces = new ArrayList<>();
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	
	
	public ChessMatch() {
		currentPlayer = Color.WHITE;
		board = new Board(8, 8);
		initialSetup();
		turn = 1;
	}
	
	
	private void nextTurn(){
	    turn++;
	    currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRow()][board.getColumn()];
		for (int i = 0; i < board.getRow(); i++) {
			for (int j = 0; j < board.getColumn(); j++) { mat[i][j] = (ChessPiece)board.getPiece(i, j); }
		}
		return mat;
	}
	
	
	public ChessPiece performChessMove(ChessPosition sourcePos, ChessPosition targetPos) {
		Position source = sourcePos.toPosition();
		Position target = targetPos.toPosition();
		validatePosition(source);
		validatePosition(source, target);
		Piece captured = makeAMove(source, target);
		
		if(testCheck(currentPlayer)) { 
			undoMove(source, target, (ChessPiece)captured);
			throw new ChessException("You can't put yourself in check"); 
		}
		
		ChessPiece movedPiece = (ChessPiece)board.getPiece(target);
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() + 2 || target.getRow() == source.getRow() - 2)) {
			enPassantVulnerable = movedPiece;
		}
		//Promotion
		promoted = null;
		if(movedPiece instanceof Pawn){
		    if(movedPiece.getColor() == Color.WHITE && target.getRow() == 0 || movedPiece.getColor() == Color.BLACK && target.getRow() == 7){
		        promoted = (ChessPiece)board.getPiece(target);
		        promoted = replacePromotedPiece("Q");
		    }
		}
		check = testCheck(opponent(currentPlayer));
		if(testChekcMate(opponent(currentPlayer))) { checkMate = true; }
		else { nextTurn(); }
		return (ChessPiece)captured;
	}

	
	private Piece makeAMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();		
		Piece captured = board.removePiece(target);
		board.placePiece(p, target);
		if(captured != null) {
			piecesOnTheBoard.remove(captured);
			capturedPieces.add(captured);
		}
		//Castling
		if (p instanceof King) {
			if (target.getColumn() == source.getColumn() + 2) {
				Position rSource = new Position(source.getRow(), source.getColumn() + 3);
				Position rTarget = new Position(source.getRow(), source.getColumn() + 1);
				ChessPiece rook = (ChessPiece)board.removePiece(rSource);
				board.placePiece(rook, rTarget);
				rook.increaseMoveCount();
			}
			if (target.getColumn() == source.getColumn() - 2) {
				Position rSource = new Position(source.getRow(), source.getColumn() - 4);
				Position rTarget = new Position(source.getRow(), source.getColumn() - 1);
				ChessPiece rook = (ChessPiece)board.removePiece(rSource);
				board.placePiece(rook, rTarget);
				rook.increaseMoveCount();
			}
		}
		//En Passant
		if(p instanceof Pawn && source.getColumn() != target.getColumn() && captured == null) {
			Position pawnPosition;
			if (p.getColor() == Color.WHITE) { pawnPosition = new Position(target.getRow() + 1, target.getColumn()); }
			else { pawnPosition = new Position(target.getRow() - 1, target.getColumn()); }
			captured = board.removePiece(pawnPosition);
			piecesOnTheBoard.remove(pawnPosition);
			capturedPieces.add(captured);
		}
		return captured;
	}
	
	
	private void undoMove(Position source, Position target, ChessPiece capturedPiece) {
		ChessPiece p = (ChessPiece)board.getPiece(target);
		p.decreaseMoveCount();
		board.removePiece(target);
		board.placePiece(p, source);
		if(capturedPiece != null) {
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
			board.placePiece(capturedPiece, target);
		}
		//Castling
		if (p instanceof King) {
			if (target.getColumn() == source.getColumn() + 2) {
				Position rSource = new Position(source.getRow(), source.getColumn() + 3);
				Position rTarget = new Position(source.getRow(), source.getColumn() + 1);
				ChessPiece rook = (ChessPiece)board.removePiece(rTarget);
				board.placePiece(rook, rSource);
				rook.increaseMoveCount();
			}
			if (target.getColumn() == source.getColumn() - 2) {
				Position rSource = new Position(source.getRow(), source.getColumn() - 4);
				Position rTarget = new Position(source.getRow(), source.getColumn() - 1);
				
				ChessPiece rook = (ChessPiece)board.removePiece(rTarget);
				board.placePiece(rook, rSource);
				rook.increaseMoveCount();
			}
		}
		//En Passant
		if(p instanceof Pawn && source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
			ChessPiece pawn = (ChessPiece)board.removePiece(target);
			Position pawnPosition;
			if (p.getColor() == Color.WHITE) { pawnPosition = new Position(3, target.getColumn()); }
			else { pawnPosition = new Position(4, target.getColumn()); }
			board.placePiece(pawn, pawnPosition);
		}
	}

	
	public ChessPiece replacePromotedPiece(String type){
	    if(promoted == null){ throw new IllegalStateException("There is no piece to be promoted"); }
	    if(!type.equals("R") && !type.equals("K") && !type.equals("B") && !type.equals("Q")){ return promoted; }
	    Position pos = promoted.getChessPosition().toPosition();
	    Piece p = board.removePiece(pos);
	    piecesOnTheBoard.remove(p);
	    ChessPiece newPiece = newPiece(type, promoted.getColor());
	    board.placePiece(newPiece, pos);
	    return newPiece;
	}
	
	
	private ChessPiece newPiece(String type, Color color){
	    if(type.equals("R")){ return new Rook(board, color); }
	    if(type.equals("K")){ return new Knight(board, color); }
	    if(type.equals("B")){ return new Bishop(board, color); }
	    return new Queen(board, color);
	}
	
	
	private void validatePosition(Position source) {
		if(!board.thereIsAPiece(source)) { throw new ChessException("There is no piece on source position!"); }
		if(currentPlayer != ((ChessPiece)board.getPiece(source)).getColor()){ throw new ChessException("This piece isn't yours!"); }
		if(!board.getPiece(source).isThereAnyPossibleMoves()) { throw new ChessException("There is no possible move for this piece!"); }
	}
	
	
	private void validatePosition(Position source, Position target) {
		if(!board.getPiece(source).possibleMove(target)) {
			throw new ChessException("The chosen piecen can't move to target position");
		}
	}

	
	public void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	
	public boolean[][] possibleMoves(ChessPosition chessPosition){
		Position position = chessPosition.toPosition();
		validatePosition(position);
		return board.getPiece(position).possibleMoves();
	}
	
	
	public Color opponent(Color color) { return (color == Color.WHITE) ? Color.BLACK : Color.WHITE; }
	
	
	public ChessPiece king(Color color) {
		List<Piece> pieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece piece : pieces) {
			if (piece instanceof King) {
				return (ChessPiece)piece;
			}
		}
		throw new IllegalStateException("There is no" + color + "King on the board");
	}
	
	
	public boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> pieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece piece : pieces) {
			boolean[][] mat = piece.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean testChekcMate(Color color) {
		if(!testCheck(color)) { return false; }
		List<Piece> pieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece piece : pieces) {
			boolean[][] mat = piece.possibleMoves();
			for (int i = 0; i < board.getRow(); i++) {
				for (int j = 0; j < board.getColumn(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece)piece).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece captured = makeAMove(source, target);
						boolean check = testCheck(color);
						undoMove(source, target, (ChessPiece)captured);
						if (!check) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	
	public void initialSetup() {
		for (int i = 0; i <= board.getColumn() - 1; i++) { placeNewPiece((char)('a'+i), 2,  new Pawn(board, Color.WHITE, this)); }
		placeNewPiece('a', 1, new Rook  (board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen (board, Color.WHITE));
		placeNewPiece('e', 1, new King  (board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook  (board, Color.WHITE));
		
		for (int i = 0; i <= board.getColumn() - 1; i++) { placeNewPiece((char)('a'+i), 7,  new Pawn(board, Color.BLACK, this)); }
		placeNewPiece('a', 8, new Rook  (board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen (board, Color.BLACK));
		placeNewPiece('e', 8, new King  (board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook  (board, Color.BLACK));
	}
	
	
	public int getTurn() {
		return turn;
	}
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean isCheck() {
		return check;
	}

	public boolean isCheckMate() {
		return checkMate;
	}

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}
}
package application;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class UI {
	
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	public static void printBoard(ChessPiece[][] pieces) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			
			for (int j = 0; j < pieces.length; j++) {
				if (i % 2 == j % 2) { printSquare(pieces[i][j], false, ANSI_WHITE_BACKGROUND); }
				else { printSquare(pieces[i][j], false, ANSI_YELLOW_BACKGROUND);}
			}
			
			System.out.println("");
		}
		System.out.println("  A B C D E F G H");
	}
	
	public static void printBoard(ChessPiece[][] pieces, boolean[][] mat) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			
			for (int j = 0; j < pieces.length; j++) {
				if (i % 2 == j % 2) { printSquare(pieces[i][j], mat[i][j], ANSI_WHITE_BACKGROUND); }
				else { printSquare(pieces[i][j], mat[i][j], ANSI_YELLOW_BACKGROUND); }
			}
			
			System.out.println("");
		}
		System.out.println("  A B C D E F G H");
	}
	
	public static void printMatch(ChessMatch chessMatch, List<ChessPiece> capturedPieces){
		
		printCapturedPieces(capturedPieces, Color.WHITE);
	    printBoard(chessMatch.getPieces());
	    printCapturedPieces(capturedPieces, Color.BLACK);
	    
	    System.out.println("");
	    
	    if (!chessMatch.isCheckMate()) {
		    System.out.println("Turn: " + chessMatch.getTurn());
	    	System.out.println("Current Player: " + chessMatch.getCurrentPlayer()); 
	    	if(chessMatch.isCheck()) { System.out.println(ANSI_GREEN + "CHECK!" + ANSI_RESET);}
	    }
	    else {
	    	System.out.println("CHECKMATE!");
		    System.out.println("Turns: " + chessMatch.getTurn());
	    	System.out.println("Winner: " + chessMatch.getCurrentPlayer() + "!");
	    }
	    
	}
	
	public static ChessPosition readChessPosition(Scanner sc) {
		
		try {
			String chessPosition = sc.nextLine().toLowerCase();
			char col = chessPosition.charAt(0);
			int row = Integer.parseInt(chessPosition.substring(1));
			return new ChessPosition(col, row);
		}
		catch(RuntimeException e) { throw new InputMismatchException("Positions must be between 'a1' and 'h8'"); }
	}
	
	private static void printSquare(ChessPiece piece, boolean move, String color) {
		if (piece == null) { 
			if(move) { System.out.print(ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET);  }
			else{ System.out.print(color + "  " + ANSI_RESET); } 
		}
		else {
			if (piece.getColor() == Color.WHITE) { 
				if (move) { System.out.print(ANSI_BLUE_BACKGROUND + ANSI_WHITE + piece + " " + ANSI_RESET); }
				else {{ System.out.print(ANSI_WHITE + piece + " " + ANSI_RESET); }}
			}
			else { 
				if (move) { System.out.print(ANSI_BLUE_BACKGROUND + ANSI_YELLOW + piece + " " + ANSI_RESET); }
				else {{ System.out.print(ANSI_YELLOW + piece + " " + ANSI_RESET); }}
			}
		}
	}
	
	private static void printCapturedPieces(List<ChessPiece> captured, Color color) {
		List<ChessPiece> pieces = captured.stream().filter(piece -> piece.getColor() == color).collect(Collectors.toList());
		
		if(color == Color.WHITE) { System.out.print(ANSI_WHITE + " " + ANSI_RED_BACKGROUND + " "); }
		else { System.out.print(ANSI_YELLOW + " " + ANSI_RED_BACKGROUND + " "); }
		
		for (ChessPiece chessPiece : pieces) {
			System.out.print(chessPiece + " ");
		}
		System.out.println(ANSI_RESET);
	}
	
	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
}
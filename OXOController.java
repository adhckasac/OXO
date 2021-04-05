package code;

import code.OXOExceptions.*;

//WEIFENG DONG

class OXOController<rows, columns, time>
{

    int step = 0;
    OXOModel gameModel;

    public OXOController(OXOModel model) {
        gameModel = model;
        //Gets the character of the current player（current_letter）
        OXOPlayer current_play = gameModel.getPlayerByNumber(step % gameModel.getNumberOfPlayers());
        gameModel.setCurrentPlayer(current_play);//Set the player's character
    }


    //Check if there is a winner
    public void checkWinner(int piece_i, int piece_j, OXOPlayer current_play, int rows, int columns) {

        int threshold = gameModel.getWinThreshold();//Get the threshold
        int[] count = {1, 1, 1,1};

        //Determine if vertical pieces have reached the threshold
        for (int i = 1; i < threshold; i++) {
            if ((piece_i + i < rows) && (current_play == gameModel.getCellOwner(piece_i + i, piece_j))) {
                count[0]++;
            } else {
                break;
            }
        }
        for (int i = 1; i < threshold; i++) {
            if ((piece_i - i >= 0) && (current_play == gameModel.getCellOwner(piece_i - i, piece_j))) {
                count[0]++;
            } else {
                break;
            }
        }
        //Determine if horizontal pieces have reached the threshold
        for (int j = 1; j < threshold; j++) {
            if ((piece_j + j < columns) && (current_play == gameModel.getCellOwner(piece_i, piece_j + j))) {
                count[1]++;
            } else {
                break;
            }
        }
        for (int j = 1; j < threshold; j++) {
            if ((piece_j - j >= 0) && (current_play == gameModel.getCellOwner(piece_i, piece_j - j))) {
                count[1]++;
            } else {
                break;
            }
        }
        //Determine if SW-towards or NE-towards pieces have reached the threshold
        for (int i = 1; i < threshold; i++) {
            if ((piece_i + i < rows) && (piece_j + i < columns) && (current_play == gameModel.getCellOwner(piece_i + i, piece_j + i))) {
                count[2]++;
            } else {
                break;
            }
        }
        for (int i = 1; i < threshold; i++) {
            if ((piece_i - i >= 0) && (piece_j - i >= 0) && (current_play == gameModel.getCellOwner(piece_i - i, piece_j - i))) {
                count[2]++;
            } else {
                break;
            }
        }
        //Determine if SE-towards or NW-towards pieces have reached the threshold
        for (int i = 1; i < threshold; i++) {
            if ((piece_i + i < rows) && (piece_j - i >= 0) && (current_play == gameModel.getCellOwner(piece_i + i, piece_j - i))) {
                count[3]++;
            } else {
                break;
            }
        }
        for (int i = 1; i < threshold; i++) {
            if ((piece_i - i >= 0) && (piece_j + i < columns) && (current_play == gameModel.getCellOwner(piece_i - i, piece_j + i))) {
                count[3]++;
            } else {
                break;
            }
        }

        if ((count[0] >= threshold) || (count[1] >= threshold) || (count[2] >= threshold)|| (count[3] >= threshold)) {
            gameModel.setWinner(current_play);//Set the winner
        }

    }


    //Check if the game become a draw
    public void checkDraw(int rows, int columns) {
        if (step == rows * columns) {
            gameModel.setGameDrawn();
        }
    }


    // Extract valid information in command statement, and determine if there is an error in the input
    public int[] extractCommand(String str) throws OXOMoveException {
        // Exception: invalid length
        if ((str.length() != 2)) {
            throw new InvalidIdentifierLengthException("The length of \"" + str + "\" is " + str.length() + " which is invalid.");
        }

        String valid_part = str.replaceAll("\\p{P}", "");//Extract valid character(s)
        String invalid_part = str.replaceAll(valid_part, "");//Extract invalid character(s)
        //Extract character of row (as string and English letter)
        String row_character = str.replaceAll("[^a-z^A-Z]", "");
        //Extract character of column (as string and number)
        String column_character = "";
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    column_character += str.charAt(i);
                }
            }
        }
        //Exception: both of row and column have invalid identifier
        if ((row_character == "") && (column_character == "")) {
            throw new InvalidIdentifierCharacterException("In the input \"" + str + "\", \"" + invalid_part + "\" is a invalid identifier for row and column.");
        }
        //Exception: row has invalid identifier
        if (row_character == "") {
            //Exception: the input includes two column identifier
            if (str.length() == valid_part.length()) {
                throw new InvalidIdentifierCharacterException("The input \"" + str + "\", \"" + valid_part + "\" includes two column identifiers and lacks row identifier.");
            }
            throw new InvalidIdentifierCharacterException("In the input \"" + str + "\", \"" + invalid_part + "\" is a invalid identifier for row.");
        }
        //Exception: column has invalid identifier
        if (column_character == "") {
            //Exception: the input includes two row identifier
            if (str.length() == valid_part.length()) {
                throw new InvalidIdentifierCharacterException("The input \"" + str + "\", \"" + valid_part + "\" includes two row identifiers and lacks column identifier.");
            }
            throw new InvalidIdentifierCharacterException("In the input \"" + str + "\", \"" + invalid_part + "\" is a invalid identifier for column.");
        }
        //String into integer
        int column_number = Integer.parseInt(column_character);
        //English letter is converted to serial number
        int ascii = (int) row_character.toString().charAt(0);
        int row_number;
        if (ascii > 96) {
            row_number = ascii - 96;
        } else {
            row_number = ascii - 64;
        }
        //Convert row and column into cell/arraylist location
        int piece_i = row_number - 1;
        int piece_j = column_number - 1;
        int[] extracted_array = {piece_i, piece_j};
        //Exception: outside cell range
        if (((piece_i < 0) || (piece_i >= gameModel.getNumberOfRows())) &&( (piece_j < 0) || (piece_j >= gameModel.getNumberOfColumns()))) {
            throw new OutsideCellRangeException("In the input \"" + str + "\", Row " + row_character + " and column " + column_character + " are out of range.");
        }
        if ((piece_i < 0) || (piece_i >= gameModel.getNumberOfRows())) {
            throw new OutsideCellRangeException("In the input \"" + str + "\", Row " + row_character + " is out of range.");
        }
        if ((piece_j < 0) || (piece_j >= gameModel.getNumberOfColumns())) {
            throw new OutsideCellRangeException("In the input \"" + str + "\", Column " + column_character + " is out of range.");
        }
        //Exception: cell already is taken exception
        if (gameModel.getCellOwner(piece_i, piece_j) != null) {
            throw new CellAlreadyTakenException("Cell " + row_character + column_character + " has already been claimed by a player.");
        }
        return extracted_array;
    }


    //Command function
    public void handleIncomingCommand(String command) throws OXOMoveException {

        if((gameModel.getWinner()!=null)||(gameModel.isGameDrawn()==true)){
            return;
        }

        //Get the position of the pieces
//        int rows = gameModel.getNumberOfRows();//Rows of the chess board
//        int columns = gameModel.getNumberOfColumns();//Column of the chess board
        int[] extract_command = extractCommand(command);
        int piece_i = extract_command[0];
        int piece_j = extract_command[1];
        //change the turn of the player
        OXOPlayer current_play = gameModel.getPlayerByNumber(step % gameModel.getNumberOfPlayers());
        OXOPlayer next_play = gameModel.getPlayerByNumber((step + 1) % gameModel.getNumberOfPlayers());
        gameModel.setCurrentPlayer(next_play);
        step++;
        //Set the current piece
        gameModel.setCellOwner(piece_i, piece_j, current_play);
        //Check the winning, losing and drawing
        checkWinner(piece_i, piece_j, current_play, gameModel.getNumberOfRows(), gameModel.getNumberOfColumns());
        checkDraw(gameModel.getNumberOfRows(), gameModel.getNumberOfColumns());
    }
}
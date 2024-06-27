/**
 * Vedamsh Ganta
 * 115004229
 * Recitation 01
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This class represents a zork story tree
 * It has a root, cursor, and a state field
 */
public class StoryTree{
    private StoryTreeNode root;
    private StoryTreeNode cursor;
    private GameState state;

    /**
     * Creates a new story tree
     * Sets root to default root node
     * Sets cursor to root
     * Sets state to game not over
     */
    public StoryTree(){
        root = new StoryTreeNode("root", "root", "Hello, and Welcome to Zork!");
        cursor = root;
        state = GameState.GAME_NOT_OVER;
    }

    /**
     * Reads a text file and adds all the node from the
     * file to a new tree and returns the tree
     * @param filename
     * the name of the file
     * @return
     * the new tree
     * @throws FileNotFoundException
     * if file is not found
     */
    public static StoryTree readTree(String filename) {
        if(filename == null || filename.equals("")) throw new IllegalArgumentException();
        Scanner fileIn = null;
        StoryTree newTree = new StoryTree();
        try {
            fileIn = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            return newTree;
        }

        while(fileIn.hasNextLine()){
            String[] data = fileIn.nextLine().split("\\|");

            if(newTree.root.isLeaf()){
                try {
                    newTree.cursor = newTree.root;
                    newTree.addChild(data[1].substring(1, data[1].length() - 1), data[2].substring(1));
                } catch (TreeFullException ignored) {}
            }
            else{
                newTree.resetCursor();
                for(int i = 2; i < data[0].length(); i += 2){
                    try {
                        newTree.selectChild(String.valueOf(data[0].charAt(i)));
                    } catch (NodeNotPresentException ignored) {}
                }
                try {
                    newTree.addChild(data[1].substring(1, data[1].length() - 1), data[2].substring(1));
                } catch (TreeFullException ignored) {}
            }
        }
        return newTree;
    }

    /**
     * adds all the nodes in a tree to the give file in a specific format
     * @param filename
     * the name if the file
     * @param tree
     * the tree from which the nodes are added to the file
     * @throws FileNotFoundException
     * if the file is not found
     */
    public static void saveTree(String filename, StoryTree tree) throws FileNotFoundException{
        PrintWriter fileOut = new PrintWriter(filename);
        if(tree.root.isLeaf()){
            System.out.println("Tree is empty.");
            return;
        }
        tree.resetCursor();
        String lines = tree.cursor.getDetails();
        fileOut.write(lines);
        fileOut.close();
    }

    /**
     * returns the state of the game
     * @return
     * state of the game
     */
    public GameState getGameState(){
        return state;
    }

    /**
     * returns the position of the node that the cursor is pointing to
     * @return
     * position of the node that the cursor is pointing to
     */
    public String getCursorPosition(){
        return cursor.getPosition();
    }

    /**
     * returns the option of the node that the cursor is pointing to
     * @return
     * option of the node that the cursor is pointing to
     */
    public String getCursorOption(){
        return cursor.getOption();
    }

    /**
     * returns the message of the node that the cursor is pointing to
     * @return
     * message of the node that the cursor is pointing to
     */
    public String getCursorMessage(){
        return cursor.getMessage();
    }

    /**
     * returns the positions and options of the children of the current node
     * @return
     * positions and options of the children of the current node
     * @throws NodeNotPresentException
     * if cursor is a leaf
     */
    public String[][] getOptions() throws NodeNotPresentException {
        if(cursor.isLeaf()){
            throw new NodeNotPresentException();
        }
        String[][] options;
        if(cursor.getRightChild() != null){
            options = new String[3][2];
            options[0][0] = cursor.getLeftChild().getPosition();
            options[0][1] = cursor.getLeftChild().getOption();
            options[1][0] = cursor.getMiddleChild().getPosition();
            options[1][1] = cursor.getMiddleChild().getOption();
            options[2][0] = cursor.getRightChild().getPosition();
            options[2][1] = cursor.getRightChild().getOption();
        }
        else if(cursor.getMiddleChild() != null){
            options = new String[2][2];
            options[0][0] = cursor.getLeftChild().getPosition();
            options[0][1] = cursor.getLeftChild().getOption();
            options[1][0] = cursor.getMiddleChild().getPosition();
            options[1][1] = cursor.getMiddleChild().getOption();
        }
        else{
            options = new String[1][2];
            options[0][0] = cursor.getLeftChild().getPosition();
            options[0][1] = cursor.getLeftChild().getOption();
        }
        return options;
    }

    /**
     * sets the message of the cursor to a new message
     * @param message
     * the new message that is set as the message of the cursor
     */
    public void setCursorMessage(String message){
        cursor.setMessage(message);
    }

    /**
     * sets the option of the cursor to a new option
     * @param option
     * the new option that is set as the option of the cursor
     */
    public void setCursorOption(String option){
        cursor.setOption(option);
    }

    /**
     * resets the cursor to the root
     */
    public void resetCursor(){
        cursor = root.getLeftChild();
        resetState();
    }

    /**
     * appropriately resets the state of game based on the position of the cursor
     */
    private void resetState(){
        if(cursor != null){
            if(cursor.isLeaf()) {
                if (cursor.isWinningNode())
                    state = GameState.GAME_OVER_WIN;
                else
                    state = GameState.GAME_OVER_LOSE;
            }
            else
                state = GameState.GAME_NOT_OVER;
        }
        else
            state = GameState.GAME_NOT_OVER;
    }

    /**
     * This method selects the appropriate child based on the given position
     * @param position
     * the position based on which the child is selected
     * @throws NodeNotPresentException
     * if the required child does not exist
     */
    public void selectChild(String position) throws NodeNotPresentException {
        if(position == null || position.equals("")){
            throw new IllegalArgumentException();
        }
        if(cursor.isLeaf()){
            throw new NodeNotPresentException(position);
        }
        position = cursor == root? position : cursor.getPosition() + "-" + position;
        boolean requiredChildExists = false;
        try{
            if(cursor.getLeftChild().getPosition().equals(position)){
                cursor = cursor.getLeftChild();
                requiredChildExists = true;
            }
        }
        catch(NullPointerException ignored){}
        try{
            if(cursor.getMiddleChild().getPosition().equals(position)){
                cursor = cursor.getMiddleChild();
                requiredChildExists = true;
            }
        }
        catch(NullPointerException ignored){}
        try{
            if(cursor.getRightChild().getPosition().equals(position)){
                cursor = cursor.getRightChild();
                requiredChildExists = true;
            }
        }
        catch(NullPointerException ignored){}
        if(requiredChildExists)
            resetState();
        else
            throw new NodeNotPresentException(position);
    }

    /**
     * Adds a new child to the cursor node
     * @param option
     * the new option of the child that is added
     * @param message
     * the new message of the child that is added
     * @throws TreeFullException
     * if the cursor node has no free spots to add the child
     */
    public void addChild(String option, String message) throws TreeFullException {
        if(option == null || option.equals("") ||
        message == null || message.equals("")){
            throw new IllegalArgumentException();
        }
        if(cursor.getLeftChild() == null)
            cursor.setLeftChild(new StoryTreeNode(cursor == root? "1" : cursor.getPosition() + "-1",
                    option, message));
        else if(cursor.getMiddleChild() == null)
            cursor.setMiddleChild(new StoryTreeNode(cursor == root? "2" : cursor.getPosition() + "-2",
                    option, message));
        else if(cursor.getRightChild() == null)
            cursor.setRightChild(new StoryTreeNode(cursor == root? "3" : cursor.getPosition() + "-3",
                    option, message));
        else
            throw new TreeFullException();
    }

    /**
     * This method removes a child based on the given position
     * @param position
     * the position at which the node is removed
     * @return
     * the removed node
     * @throws NodeNotPresentException
     * is the required node is not present
     */
    public StoryTreeNode removeChild(String position) throws NodeNotPresentException {
        if(position == null || position.equals("")){
            throw new IllegalArgumentException();
        }
        if(cursor.isLeaf()){
            throw new NodeNotPresentException(position);
        }

        position = cursor == root? position : cursor.getPosition() + "-" + position;
        StoryTreeNode requiredNode = null;

        try{
            if(cursor.getLeftChild().getPosition().equals(position)){
                requiredNode = cursor.getLeftChild();
                if(cursor.getMiddleChild() != null){
                    cursor.setLeftChild(cursor.getMiddleChild());
                    if(cursor.getLeftChild() != null){
                        cursor.setMiddleChild(cursor.getRightChild());
                        cursor.setRightChild(null);
                    }
                    else
                        cursor.setMiddleChild(null);
                }
            }
        }
        catch(NullPointerException ignored){}
        try{
            if(cursor.getMiddleChild().getPosition().equals(position)){
                requiredNode = cursor.getMiddleChild();
                if(cursor.getRightChild() != null){
                    cursor.setMiddleChild(cursor.getRightChild());
                    cursor.setRightChild(null);
                }
                else
                    cursor.setMiddleChild(null);
            }
        }
        catch(NullPointerException ignored){}
        try{
            if(cursor.getRightChild().getPosition().equals(position)){
                requiredNode = cursor.getRightChild();
                cursor.setRightChild(null);
            }
        }
        catch(NullPointerException ignored){}

        if(requiredNode != null){
            cursor.resetPosition();
            return requiredNode;
        }
        else
            throw new NodeNotPresentException(position);
    }
}

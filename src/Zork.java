/**
 * Vedamsh Ganta
 * 115004229
 * Recitation 01
 */

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This is the zork class that has an interface to run the zork game
 */
public class Zork {
    static Scanner sc = new Scanner(System.in);
    /**
     * This is the main method
     * @param args
     * command line parameter
     */
    public static void main(String[] args){
        System.out.println("Hello and Welcome to Zork!\n");

        StoryTree tree = null;
        String filename = null;


        System.out.print("Please enter the file name: ");
        filename = sc.nextLine();
        tree = StoryTree.readTree(filename);
        System.out.println("\nLoading game from file...\n");
        System.out.println("File loaded!");


        mainMenuLoop: while(true){
            System.out.println();
            System.out.print("Would you like to edit (E), play (P) or quit (Q)?  ");
            String mainMenuOption = sc.nextLine();
            if(mainMenuOption.equals("e")){
                editTree(tree);
            }
            else if(mainMenuOption.equals("p")){
                tree.resetCursor();
                try{
                    System.out.println();
                    System.out.println(tree.getCursorOption());
                    System.out.println();
                    System.out.println(tree.getCursorMessage());
                } catch(NullPointerException npe){
                    System.out.println("No nodes present in tree. Please add new nodes using the edit option, or quit and try with a new file.");
                    continue;
                }
                while (tree.getGameState() == GameState.GAME_NOT_OVER) {
                    String[][] cursorOptions = new String[0][];
                    try {
                        cursorOptions = tree.getOptions();
                    } catch (NodeNotPresentException ignored) {}

                    String selectedPosition = null;

                    for (String[] option : cursorOptions) {
                        System.out.println(option[0].charAt(option[0].length() - 1) + ") " +
                                option[1]);
                    }
                    System.out.print("Please make a choice. ");
                    selectedPosition = sc.nextLine();
                    try {
                        tree.selectChild(selectedPosition);
                    } catch (NodeNotPresentException nnpe) {
                        System.out.println(nnpe.getMessage());
                    }
                    System.out.println("\n" + tree.getCursorMessage());
                }
                System.out.println("thanks for playing.");
            }
            else if(mainMenuOption.equals("q")){
                System.out.println("\nGame being saved to " + filename + "...");
                try {
                    StoryTree.saveTree(filename, tree);
                } catch (FileNotFoundException ignored) {}
                System.out.println("\nSave successful!");
                System.out.println("\nProgram terminating normally.");
                break mainMenuLoop;
            }
            else{
                System.out.println("Invalid option.");
            }
        }
    }

    /**
     * This function is called when the user wants to edit the tree
     * @param tree
     */
    public static void editTree(StoryTree tree){
        editLoop: while(true){
            System.out.println();
            System.out.println("Zork Editor:\n" +
                    "    V: View the cursor's position, option and message.\n" +
                    "    S: Select a child of this cursor (options are 1, 2, and 3).\n" +
                    "    O: Set the option of the cursor.\n" +
                    "    M: Set the message of the cursor.\n" +
                    "    A: Add a child StoryNode to the cursor.\n" +
                    "    D: Delete one of the cursor's children and all its descendants.\n" +
                    "    R: Move the cursor to the root of the tree.\n" +
                    "    Q: Quit editing and return to main menu.");
            System.out.println();
            System.out.print("Please select an option: ");
            String editOption = sc.nextLine();
            switch(editOption){
                case "v":
                    System.out.println();
                    System.out.println("Position: " + tree.getCursorPosition());
                    System.out.println("Option: " + tree.getCursorOption());
                    System.out.println("Message: " + tree.getCursorMessage());
                    break;
                case "s":
                    String availableOptionsToSelect;
                    String[][] cursorOptionsToSelect;
                    try {
                        cursorOptionsToSelect = tree.getOptions();
                        if(cursorOptionsToSelect.length == 1)
                            availableOptionsToSelect = "[1]";
                        else if(cursorOptionsToSelect.length == 2)
                            availableOptionsToSelect = "[1,2]";
                        else
                            availableOptionsToSelect = "[1,2,3]";

                    } catch (NodeNotPresentException nnpe) {
                        System.out.println(nnpe.getMessage());
                        break;
                    }
                    System.out.print("Please select a child: " + availableOptionsToSelect + " ");
                    String selectedPosition = sc.nextLine();
                    try {
                        tree.selectChild(selectedPosition);
                    } catch (NodeNotPresentException nnpe) {
                        System.out.println(nnpe.getMessage());
                    }
                    break;
                case "o":
                    System.out.print("Please select an option: ");
                    String newOptionToSet = sc.nextLine();
                    tree.setCursorOption(newOptionToSet);
                    System.out.println("\nOption set.");
                    break;
                case "m":
                    System.out.println("Please enter a new Message: ");
                    String newMessageToSet = sc.nextLine();
                    tree.setCursorMessage(newMessageToSet);
                    System.out.println("\nMessage set.");
                    break;
                case "a":
                    System.out.print("Enter an option:  ");
                    String newOption = sc.nextLine();
                    System.out.print("Enter a message: ");
                    String newMessage = sc.nextLine();
                    try {
                        tree.addChild(newOption, newMessage);
                    } catch (TreeFullException tfe) {
                        System.out.println(tfe.getMessage());
                    }
                    System.out.println("\nChild added.");
                    break;
                case "d":
                    String availableOptionsToDelete;
                    String[][] cursorOptionsToDelete;
                    try {
                        cursorOptionsToDelete = tree.getOptions();
                        if(cursorOptionsToDelete.length == 1)
                            availableOptionsToDelete = "[1]";
                        else if(cursorOptionsToDelete.length == 2)
                            availableOptionsToDelete = "[1,2]";
                        else
                            availableOptionsToDelete = "[1,2,3]";
                    } catch (NodeNotPresentException nnpe) {
                        System.out.println(nnpe.getMessage());
                        break;
                    }
                    System.out.print("Please select a child: " + availableOptionsToDelete + " ");
                    String deletedPosition = sc.nextLine();
                    try {
                        tree.removeChild(deletedPosition);
                        System.out.println("\nSubtree deleted.");
                    } catch (NodeNotPresentException nnpe) {
                        System.out.println(nnpe.getMessage());
                    }
                    break;
                case "r":
                    tree.resetCursor();
                    System.out.println("Cursor moved to root.");
                    break;
                case "q":
                    break editLoop;
            }
        }
    }
}

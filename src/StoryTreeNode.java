/**
 * Vedamsh Ganta
 * 115004229
 * Recitation 01
 */

/**
 * This class represents a story tree node. It has 3 children and
 * zork 3 game data fields
 */
public class StoryTreeNode{
    static final String WIN_MESSAGE = "YOU WIN";
    static final String LOSE_MESSAGE = "YOU LOSE";

    private String position;
    private String option;
    private String message;
    private StoryTreeNode leftChild;
    private StoryTreeNode middleChild;
    private StoryTreeNode rightChild;

    /**
     * This method creates a new story tree node.
     * @param newPosition
     * the new position
     * @param newOption
     * the new option
     * @param newMessage
     * the new message
     */
    public StoryTreeNode(String newPosition, String newOption, String newMessage){
        position = newPosition;
        option = newOption;
        message = newMessage;
        leftChild = null;
        middleChild = null;
        rightChild = null;
    }

    /**
     * This method checks if a node is a leaf or not
     * @return
     * true or false depending on if the node is a leaf or not
     */
    public boolean isLeaf(){
        return leftChild == null && middleChild == null && rightChild == null;
    }

    /**
     * This method returns true is the node is a winning node
     * @return
     * true is the node is a winning node
     */
    public boolean isWinningNode(){
        if (isLeaf()) {
            return message.contains(WIN_MESSAGE);
        }
        return false;
    }

    /**
     * This method returns true is the node is a losing node
     * @return
     * true is the node is a losing node
     */
    public boolean isLosingNode(){
        if (isLeaf()) {
            return message.contains(LOSE_MESSAGE);
        }
        return false;
    }

    /**
     * returns the left child of the node
     * @return
     * left child of the node
     */
    public StoryTreeNode getLeftChild(){
        return leftChild;
    }

    /**
     * sets the left child to a new node
     * @param newLeft
     * the new node that is set as the left child
     */
    public void setLeftChild(StoryTreeNode newLeft){
        leftChild = newLeft;
    }

    /**
     * returns the middle child of the node
     * @return
     * middle child of the node
     */
    public StoryTreeNode getMiddleChild() {
        return middleChild;
    }

    /**
     * sets the middle child to a new node
     * @param newMiddle
     * the new node that is set as the middle child
     */
    public void setMiddleChild(StoryTreeNode newMiddle){
        middleChild = newMiddle;
    }

    /**
     * returns the right child of the node
     * @return
     * right child of the node
     */
    public StoryTreeNode getRightChild() {
        return rightChild;
    }

    /**
     * sets the right child to a new node
     * @param newRight
     * the new node that is set as the right child
     */
    public void setRightChild(StoryTreeNode newRight){
        rightChild = newRight;
    }

    /**
     * returns the position
     * @return
     * the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * returns the option
     * @return
     * the option
     */
    public String getOption() {
        return option;
    }

    /**
     * returns the message
     * @return
     * the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * sets the position as the new position
     * @param newPosition
     * the parameter that is set as the new position
     */
    public void setPosition(String newPosition) {
        position = newPosition;
    }

    /**
     * sets the option as the new option
     * @param newOption
     * the parameter that is set as the new option
     */
    public void setOption(String newOption) {
        option = newOption;
    }

    /**
     * sets the message as the new message
     * @param newMessage
     * the parameter that is set as the new message
     */
    public void setMessage(String newMessage) {
        message = newMessage;
    }

    /**
     * returns a string with the position, option, and message of the current
     * node and it's children
     * @return
     */
    public String getDetails(){
        String required = position + " | " + option + " | " + message;
        if(leftChild != null){
            required += "\n" + leftChild.getDetails();
        }
        if(middleChild != null){
            required += "\n" + middleChild.getDetails();
        }
        if(rightChild != null){
            required += "\n" + rightChild.getDetails();
        }
        return required;
    }

    /**
     * appropriately resets the position of the children of the node
     * in case of any error in position after removal
     */
    public void resetPosition(){
        if(leftChild != null){
            if(!leftChild.position.equals(position + "-1")){
                leftChild.setPosition(position + "-1");
                leftChild.resetPosition();
            }
        }
        if(middleChild != null){
            if(!middleChild.position.equals(position + "-2")){
                middleChild.setPosition(position + "-2");
                middleChild.resetPosition();
            }
        }
        if(rightChild != null){
            if(!rightChild.position.equals(position + "-3")){
                rightChild.setPosition(position + "-3");
                rightChild.resetPosition();
            }
        }
    }
}

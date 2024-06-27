/**
 * Vedamsh Ganta
 * 115004229
 * Recitation 01
 */

/**
 * This is an exception class. This exception is thrown if a required node is not present
 */
public class NodeNotPresentException extends Exception{
    public NodeNotPresentException(){
        super("no child");
    }

    public NodeNotPresentException(String position){
        super("error. no child " + position.charAt(position.length() - 1) + " for the current node.");
    }
}

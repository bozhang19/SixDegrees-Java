import java.util.Objects;

/**
 * This class represents a Hollywood graph vertex.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class Node {
    private String name;
    private Node next;
    private boolean isActor;

    /**
     * Creates a new vertex with name parameter.
     *
     * @param name  actor or movie name
     */
    public Node(String name) {
        this.name = name;
    }

    /**
     * Creates a new vertex with name and isActor parameters.
     *
     * @param name  actor or movie name
     * @param isActor  true if this is a actor vertex, false otherwise
     */
    public Node(String name, boolean isActor) {
        this.name = name;
        this.isActor = isActor;
    }

    /**
     * Returns the name field of this vertex.
     *
     * @return name  actor or movie name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the vertex's name field to the given name.
     *
     * @param name  new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the next vertex.
     *
     * @return next  the next linked vertex
     */
    public Node getNext() {
        return next;
    }

    /**
     * Sets the next field to the given next vertex.
     *
     * @param next  the linked next vertex
     */
    public void setNext(Node next) {
        this.next = next;
    }

    /**
     * Returns a boolean indicating whether this vertex
     * is a actor vertex or a movie vertex.
     *
     * @return isActor  true if this vertex is a actor vertex, false otherwise
     */
    public boolean isActor() {
        return isActor;
    }

    /**
     * Sets the isActor field to the given value.
     *
     * @param isActor  a boolean
     */
    public void setActor(boolean isActor) {
        this.isActor = isActor;
    }

    /**
     * Checks if two vertices are equal.
     *
     * @param other  the other vertex
     * @return true or false  true if this vertex is equal to the other vertex, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Node node = (Node) other;
        return Objects.equals(name, node.name);
    }

    /**
     * Returns a hash code of the vertex using its name field.
     *
     * @return an int  the hash code
     */
    @Override
    public int hashCode() {
        return Math.abs(Objects.hash(name));
    }

    /**
     * Returns a string representation of the vertex.
     *
     * @return a string  the string representation
     */
    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                //", next=" + next +
                ", isActor=" + isActor +
                '}';
    }
}

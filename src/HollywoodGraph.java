import java.util.*;

/**
 * This class represents a Hollywood graph.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class HollywoodGraph {
    private static final double LOAD_FACTOR = 0.75;
    private Node[] adjacencyList;
    private int nodeSize;
    private int edgeSize;

    /**
     *  Creates a new graph with the default size of ten.
     */
    public HollywoodGraph() {
        this.adjacencyList = new Node[10];
    }

    /**
     * Reads each line of the input file and add each vertex and edge in the graph.
     *
     * @param array  input array
     */
    public void add(String[] array) {
        addActors(array);
        addMovies(array);
    }

    /**
     * Adds all actor vertices in the adjacency list with linked movies.
     *
     * @param array  each line of input file
     */
    private void addActors(String[] array) {
        // building adjacency list, assuming each actor has at least one film
        // 1st part - from actor to movies
        // actor is unique when parsing each input line
        Node fromActor = new Node(array[0], true);
        int actorIndex = getPosition(fromActor);
        adjacencyList[actorIndex] = fromActor;
        nodeSize++;
        Node current = fromActor;

        for (int i = 1; i < array.length; i++) {
            current.setNext(new Node(array[i]));
            edgeSize++;
            current = current.getNext();
        }

        // check load factor after adding the leading node(actor)
        if ((nodeSize * 1.0) / adjacencyList.length >= LOAD_FACTOR) {
            rehash();
        }
    }

    /**
     * Adds all movie vertices in the adjacency list with linked actors.
     *
     * @param array  each line of input file
     */
    private void addMovies(String[] array) {
        // 2nd part - from movies to actor
        // movies are not unique
        for (int i = 1; i < array.length; i++) {
            Node nextMovie = new Node(array[i]);
            Node toActor = new Node(array[0], true);
            int moviePosition = getPosition(nextMovie);
            // if the movie is not yet at the position
            if (adjacencyList[moviePosition] == null) {
                adjacencyList[moviePosition] = nextMovie;
                nodeSize++;
                nextMovie.setNext(toActor);
                edgeSize++;

                // check load factor after adding the leading node(actor)
                if ((nodeSize * 1.0) / adjacencyList.length >= LOAD_FACTOR) {
                    rehash();
                }
            }
            // else the movie is already at the position
            else {
                Node current2 = adjacencyList[moviePosition];
                while (current2.getNext() != null) {
                    current2 = current2.getNext();
                }
                current2.setNext(toActor);
                edgeSize++;
            }
        }
    }

    /**
     * Gets the position of a vertex.
     *
     * @param node  input vertex
     * @return an int  index of the adjacency list
     */
    private int getPosition(Node node) {
        int hashCode = node.hashCode();
        int index = hashCode % adjacencyList.length;

        while (adjacencyList[index] != null && !adjacencyList[index].equals(node)) {
            index = (index + 1) % adjacencyList.length;
        }

        return index;
    }

    /**
     * Resizes the adjacency list.
     */
    private void rehash() {
        // create a new array with length twice of the previous
        Node[] newAdjacencyList = new Node[adjacencyList.length * 2];

        // iterate through the old array and put each node in the new array
        for (Node node : adjacencyList) {
            // skip the index if it is empty
            if (node != null) {
                int hashCode = node.hashCode();
                int index = hashCode % newAdjacencyList.length;
                while (newAdjacencyList[index] != null && !newAdjacencyList[index].equals(node)) {
                    index = (index + 1) % newAdjacencyList.length;
                }
                newAdjacencyList[index] = node;
            }
        }
        adjacencyList = newAdjacencyList;
    }

    /**
     *  Generates a map of pairs of actor name and actor number with the given actor name
     *  as the starting point.
     *
     * @param actorSource  actor name
     * @return a map  a hash map of pairs of actor name and actor number
     */
    public Map<String, Integer> generateActorNumbers(String actorSource) {
        // get the index of the actor if it exists, if the actor entered doesn't
        // exist, set the index to -1
        int index = getIndex(actorSource);

        // get the node of the actor if it exists
        Node actor = null;
        if (index != -1) {
            actor = adjacencyList[index];
        }

        // create a hash map for pairs such as ("Bergen, Candice": 1)
        Map<String, Integer> map = new HashMap<>();

        // if the actor entered does not exists, put it in the map and exit the method
        // since there is no network exists either
        if (actor == null) {
            map.put("null", -1);
            return map;
        }
        // if the actor entered does exists
        else {
            Set<Node> movies;                           // movie set for the new movies
            Set<Node> actors = new HashSet<>();         // actor set for the new actors
            Set<Node> visited = new HashSet<>();        // actor and movie set for the visited

            // add the actor source in the actors set
            actors.add(actor);

            int counter = 0;
            while (!actors.isEmpty()) {
                for (Node node : actors) {
                    map.put(node.getName(), counter);
                    visited.add(node);
                }

                movies = getSet(actors, visited);
                actors.clear();

                actors = getSet(movies, visited);
                visited.addAll(movies);
                movies.clear();

                counter++;
            }

            return map;
        }
    }

    /**
     * Returns the index of the source actor.
     *
     * @param actorSource  source actor name
     * @return an index  the index of the adjacency list
     */
    private int getIndex(String actorSource) {
        int index = -1;
        for (int i = 0; i < adjacencyList.length; i++) {
            if (adjacencyList[i] != null) {
                if (adjacencyList[i].getName().equals(actorSource)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    /**
     * Generates a set of nodes associated with the input set.
     *
     * @param inputSet  input set
     * @param visited  nodes visited
     * @return a set  the set of nodes associated with the input nodes set
     */
    private Set<Node> getSet(Set<Node> inputSet, Set<Node> visited) {
        Set<Node> set = new HashSet<>();
        for (Node inputNode : inputSet) {
            for (Node listNode : adjacencyList) {
                if (listNode != null) {
                    if (inputNode.getName().equals(listNode.getName())) {
                        Node current = listNode;
                        while (current.getNext() != null) {
                            current = current.getNext();
                            if (!visited.contains(current)) {
                                set.add(current);
                            }
                        }
                    }
                }
            }
        }
        return set;
    }

    /**
     * Returns the total number of vertices.
     *
     * @return an int  total number of vertices
     */
    public int getVertexSize() {
        return nodeSize;
    }

    /**
     * Returns the total number of edges.
     *
     * @return an int  total number of edges
     */
    public int getEdgeSize() {
        return edgeSize;
    }

    /**
     * Prints the adjacency list of the graph.
     */
    public void printAdjacencyList() {
        for (int i = 0; i < adjacencyList.length; i++) {
            if (adjacencyList[i] == null) {
                System.out.println("[index = " + i + "]: null");
            } else {
                System.out.print("[index = " + i + "]: " + adjacencyList[i].getName());
                Node current = adjacencyList[i];
                while (current.getNext() != null) {
                    System.out.print(" --> " + current.getNext().getName());
                    current = current.getNext();
                }
                System.out.println();
            }
        }
    }

    /**
     * Returns a string representation of a Hollywood graph.
     *
     * @return a String  the string representation
     */
    @Override
    public String toString() {
        return "HollywoodGraph{" +
                "adjacencyList=" + Arrays.toString(adjacencyList) +
                ", nodeSize=" + nodeSize +
                ", edgeSize=" + edgeSize +
                '}';
    }
}

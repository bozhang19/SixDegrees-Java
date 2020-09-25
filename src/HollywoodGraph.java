import java.util.*;

/**
 * This class represents a Hollywood graph.
 *
 * @author Bo Zhang
 * @version 1.0
 */
public class HollywoodGraph {
    private static final double LOAD_FACTOR = 0.75;
    private Vertex[] adjacencyList;
    private int vertexSize;
    private int edgeSize;

    /**
     *  Creates a new graph with the default size of ten.
     */
    public HollywoodGraph() {
        this.adjacencyList = new Vertex[10];
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
        Vertex fromActor = new Vertex(array[0], true);
        int actorIndex = getPosition(fromActor);
        adjacencyList[actorIndex] = fromActor;
        vertexSize++;
        Vertex current = fromActor;

        for (int i = 1; i < array.length; i++) {
            current.setNext(new Vertex(array[i]));
            edgeSize++;
            current = current.getNext();
        }

        // check load factor after adding the leading node(actor)
        if ((vertexSize * 1.0) / adjacencyList.length >= LOAD_FACTOR) {
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
            Vertex nextMovie = new Vertex(array[i]);
            Vertex toActor = new Vertex(array[0], true);
            int moviePosition = getPosition(nextMovie);
            // if the movie is not yet at the position
            if (adjacencyList[moviePosition] == null) {
                adjacencyList[moviePosition] = nextMovie;
                vertexSize++;
                nextMovie.setNext(toActor);
                edgeSize++;

                // check load factor after adding the leading node(actor)
                if ((vertexSize * 1.0) / adjacencyList.length >= LOAD_FACTOR) {
                    rehash();
                }
            }
            // else the movie is already at the position
            else {
                Vertex current2 = adjacencyList[moviePosition];
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
     * @param vertex  input vertex
     * @return an int  index of the adjacency list
     */
    private int getPosition(Vertex vertex) {
        int hashCode = vertex.hashCode();
        int index = hashCode % adjacencyList.length;

        while (adjacencyList[index] != null && !adjacencyList[index].equals(vertex)) {
            index = (index + 1) % adjacencyList.length;
        }

        return index;
    }

    /**
     * Resizes the adjacency list.
     */
    private void rehash() {
        // create a new array with length twice of the previous
        Vertex[] newAdjacencyList = new Vertex[adjacencyList.length * 2];

        // iterate through the old array and put each node in the new array
        for (Vertex vertex : adjacencyList) {
            // skip the index if it is empty
            if (vertex != null) {
                int hashCode = vertex.hashCode();
                int index = hashCode % newAdjacencyList.length;
                while (newAdjacencyList[index] != null && !newAdjacencyList[index].equals(vertex)) {
                    index = (index + 1) % newAdjacencyList.length;
                }
                newAdjacencyList[index] = vertex;
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
        Vertex actor = null;
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
            Set<Vertex> movies;                           // movie set for the new movies
            Set<Vertex> actors = new HashSet<>();         // actor set for the new actors
            Set<Vertex> visited = new HashSet<>();        // actor and movie set for the visited

            // add the actor source in the actors set
            actors.add(actor);

            int counter = 0;
            while (!actors.isEmpty()) {
                for (Vertex vertex : actors) {
                    map.put(vertex.getName(), counter);
                    visited.add(vertex);
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
    private Set<Vertex> getSet(Set<Vertex> inputSet, Set<Vertex> visited) {
        Set<Vertex> set = new HashSet<>();
        for (Vertex inputVertex : inputSet) {
            for (Vertex listVertex : adjacencyList) {
                if (listVertex != null) {
                    if (inputVertex.getName().equals(listVertex.getName())) {
                        Vertex current = listVertex;
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
        return vertexSize;
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
                Vertex current = adjacencyList[i];
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
                ", nodeSize=" + vertexSize +
                ", edgeSize=" + edgeSize +
                '}';
    }
}

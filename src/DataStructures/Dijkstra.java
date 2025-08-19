package DataStructures;

import java.util.HashSet;
import Main.*;

public class Dijkstra {

    private Particle source;
    private int[] position;
    private boolean positionFound;
    private HashSet<Particle> visited;
    private MinHeap<Particle> heap;

//    class Position {
//        int x;
//        int y;
//
//        private Position(int x, int y) {
//            this.x = x;
//            this.y = y;
//        }
//        private int getX() {
//            return x;
//        }
//        private int getY() {
//            return y;
//        }
//    }

    public Dijkstra(Particle s) {
        source = s;
        visited = new HashSet<>();
        positionFound = false;
        heap = new MinHeap<>();
        position = new int[] {s.getX(), s.getY()};
        findPosition();
    }

    private boolean validEdge(int x, int y) {

        if (y < 0 || y >= ParticleHandler.yPositions
                || x < 0 || x >= ParticleHandler.xPositions) {
            return false;
        }
        return ParticleHandler.getType(x, y) != Constants.WOOD;
    }

    private double distance(Particle p) {
        double yDiff = Math.pow((p.getY() - source.getY()), 2);
        double xDiff = Math.pow((p.getX() - source.getX()), 2);
        return Math.sqrt(yDiff + xDiff);
    }

    private int edgeType(int x, int y) {
        return ParticleHandler.getType(x, y);
    }

    private void addEdgeHelper(int x, int y) {
        if (edgeType(x, y) == 0) {
            positionFound = true;
            position = new int[] {x, y};
            return;
        }
        Particle edge = ParticleHandler.getParticle(x, y);
        if (!visited.contains(edge)) {
            heap.add(edge, distance(edge));
            visited.add(edge);
        }
    }

    private void addEdges(Particle p) {

        int x = p.getX();
        int y = p.getY();

        int up = y - 1;
        int right = x + 1;
        int left = x - 1;

        if (!positionFound && validEdge(right, y)) {
            addEdgeHelper(right, y);
        }
        if (!positionFound && validEdge(left, y)) {
            addEdgeHelper(left, y);
        }
        if (!positionFound && p.getType() == Constants.WATER && validEdge(x, up)) {
            addEdgeHelper(x, up);
        }

    }

    private void findPosition() {
        Particle current = source;
        addEdges(current);
        visited.add(current);
        while (!positionFound && heap.getSize() > 0) {
            current = heap.removeSmallest();
            addEdges(current);
        }
    }

    public int[] getPosition() {
        return position;
    }
}

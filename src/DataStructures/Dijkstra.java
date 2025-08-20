package DataStructures;

import java.util.Hashtable;
import Main.*;

public class Dijkstra {

    private Particle source;
    private int[] position;
    private boolean positionFound;
    private Hashtable<Particle, Integer> distTo;
    private MinHeap<Particle> heap;


    public Dijkstra(Particle s) {
        source = s;
        distTo = new Hashtable<>();
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

    private void addEdgeHelper(Particle p, int x, int y) {
        if (edgeType(x, y) == 0) {
            positionFound = true;
            position = new int[] {x, y};
            return;
        }
        Particle edge = ParticleHandler.getParticle(x, y);
        if (!distTo.containsKey(edge)) {
            distTo.put(edge, distTo.get(p) + 1);
            if (edge.getType() == Constants.WATER) {
                heap.add(edge, distTo.get(edge));
            }
            else {
                heap.add(edge, Double.POSITIVE_INFINITY);
            }
        }
    }

    private void addEdges(Particle p) {

        int x = p.getX();
        int y = p.getY();

        int up = y - 1;
        int right = x + 1;
        int left = x - 1;

        if (!positionFound && validEdge(right, y)) {
            addEdgeHelper(p, right, y);
        }
        if (!positionFound && validEdge(left, y)) {
            addEdgeHelper(p, left, y);
        }
        if (!positionFound && p.getType() == Constants.WATER && validEdge(x, up)) {
            addEdgeHelper(p, x, up);
        }

    }

    private void findPosition() {
        Particle current = source;
        distTo.put(current, 0);
        addEdges(current);
        while (!positionFound && heap.getSize() > 0) {
            current = heap.removeSmallest();
            addEdges(current);
        }
    }

    public int[] getPosition() {
        return position;
    }
}

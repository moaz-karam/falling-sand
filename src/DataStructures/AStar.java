package DataStructures;

import Main.Constants;
import Main.Particle;
import Main.ParticleHandler;

import java.util.Hashtable;

public class AStar {
    private Particle source;
    private int[] position;
    private boolean positionFound;
    private Hashtable<Particle, Integer> distTo;
    private MinHeap<Particle> heap;


    public AStar(Particle s) {
        source = s;
        distTo = new Hashtable<>();
        positionFound = false;
        heap = new MinHeap<>();
        findPosition();
    }

    private boolean validEdge(int x, int y) {

        if (y < 0 || y >= ParticleHandler.yPositions
                || x < 0 || x >= ParticleHandler.xPositions) {
            return false;
        }
        return ParticleHandler.getType(x, y) != Constants.WOOD;
    }

    private double heuristic(Particle p) {
        if (p.getType() == Constants.SAND) {
            return p.getY() + 50;
        }
        return p.getY();
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
            heap.add(edge, distTo.get(edge) + heuristic(edge));
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
        if (!positionFound && p.getType() != Constants.WATER && validEdge(x, up)) {
            addEdgeHelper(p, x, up);
        }
    }

    private void findPosition() {
        Particle current = source;
        distTo.put(current, 0);
        addEdges(current);
        while (distTo.get(current) <= 20 && !positionFound && heap.getSize() > 0) {
            current = heap.removeSmallest();
            addEdges(current);
        }
    }

    public int[] getPosition() {
        return position;
    }
}

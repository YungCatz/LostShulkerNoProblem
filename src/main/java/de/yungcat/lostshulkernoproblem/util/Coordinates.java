package de.yungcat.lostshulkernoproblem.util;

import com.google.common.base.Objects;

import java.util.UUID;

public class Coordinates {

    private final UUID world;
    private final int x;
    private final int y;
    private final int z;

    public Coordinates(UUID world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public UUID getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y && z == that.z && Objects.equal(world, that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(world, x, y, z);
    }
}

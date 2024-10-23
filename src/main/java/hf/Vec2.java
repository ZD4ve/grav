package hf;

import java.io.Serializable;

public class Vec2 implements Serializable{
    public final double x;
    public final double y;

    public static Vec2 fromPolar(double degrees, double amplitude) {
        return new Vec2(Math.cos(Math.toRadians(degrees)) * amplitude, Math.sin(Math.toRadians(degrees)) * amplitude);
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }
    public Vec2 scale(double scalar) {
        return new Vec2(this.x * scalar, this.y * scalar);
    }
    public Vec2 normalise() {
        return new Vec2(this.x / amplitude(), this.y / amplitude());
    }
    public double amplitude() {
        return Math.hypot(x, y);
    }
    public double angle() {
        return Math.toDegrees(Math.atan2(y, x));
    }

}

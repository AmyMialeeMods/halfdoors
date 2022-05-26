package amymialee.peculiarpieces.util;

public class PeculiarHelper {
    public static int clampLoop(int start, int end, int input) {
        if (input < start) {
            return clampLoop(start, end, (end - start + 1) + input);
        } else if (input > end) {
            return clampLoop(start, end, input - (end - start + 1));
        }
        return input;
    }
}
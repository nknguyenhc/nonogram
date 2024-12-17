package solver;

import org.junit.Assert;
import org.junit.Test;

public class ConstraintTest {
    @Test
    public void testOnePossibility() {
        Constraint constraint = new Constraint(new int[] {1, 3}, new int[] {0, 0, 0, 0, 0});
        Assert.assertEquals(1, constraint.possibilityCount());
        constraint.update(new int[] {2, 0, 2, 0, 0});
        Assert.assertEquals(1, constraint.possibilityCount());
        constraint.undo();
        constraint.update(new int[] {0, 2, 0, 0, 0});
        Assert.assertEquals(0, constraint.possibilityCount());
        constraint.undo();
        constraint.update(new int[] {0, 0, 2, 0, 0});
        Assert.assertEquals(1, constraint.possibilityCount());
        constraint.undo();
        constraint.update(new int[] {2, 2, 1, 0, 0});
        Assert.assertEquals(0, constraint.possibilityCount());
    }

    @Test
    public void testMultiplePossibility() {
        Constraint constraint = new Constraint(new int[] {1, 1}, new int[] {0, 0, 0, 0, 0});
        Assert.assertEquals(6, constraint.possibilityCount());
        constraint.update(new int[] {0, 2, 0, 0, 0});
        Assert.assertEquals(2, constraint.possibilityCount());
        constraint.undo();
        constraint.update(new int[] {1, 2, 0, 0, 0});
        Assert.assertEquals(2, constraint.possibilityCount());
        constraint.undo();
        constraint.update(new int[] {2, 1, 2, 0, 2});
        Assert.assertEquals(0, constraint.possibilityCount());
        constraint.undo();
        constraint.update(new int[] {2, 1, 2, 0, 0});
        Assert.assertEquals(1, constraint.possibilityCount());
        constraint.undo();
        constraint.update(new int[] {0, 2, 2, 0, 0});
        Assert.assertEquals(0, constraint.possibilityCount());
        constraint.undo();
        constraint.update(new int[] {2, 0, 0, 2, 0});
        Assert.assertEquals(1, constraint.possibilityCount());
    }

    @Test
    public void testUndo() {
        Constraint constraint = new Constraint(new int[] {1, 1}, new int[] {0, 1, 0, 0, 0});
        Assert.assertEquals(4, constraint.possibilityCount());
        constraint.update(new int[] {2, 1, 0, 0, 1});
        constraint.update(new int[] {2, 1, 2, 0, 1});
        constraint.update(new int[] {2, 1, 2, 2, 1});
        Assert.assertEquals(0, constraint.possibilityCount());
        constraint.undo();
        Assert.assertEquals(1, constraint.possibilityCount());
        constraint.undo();
        Assert.assertEquals(2, constraint.possibilityCount());
        constraint.undo();
        Assert.assertEquals(4, constraint.possibilityCount());
    }
}

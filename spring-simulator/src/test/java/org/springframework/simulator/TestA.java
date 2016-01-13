package org.springframework.simulator;

/**
 * Created by gman on 11/01/16.
 */
public class TestA {
    TestB b;

    public TestA() {
    }

    public TestB getB() {
        return b;
    }

    public void setB(TestB b) {
        this.b = b;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        TestA testA = (TestA) object;

        return !(b != null ? !b.equals(testA.b) : testA.b != null);

    }

    @Override
    public int hashCode() {
        return b != null ? b.hashCode() : 0;
    }
}

package org.springframework.simulator;

/**
 * Created by gman on 11/01/16.
 */
public class TestB {
    int inc;

    public TestB() {
    }

    public int getInc() {
        return inc;
    }

    public void setInc(int inc) {
        this.inc = inc;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        TestB testB = (TestB) object;

        return inc == testB.inc;

    }

    @Override
    public int hashCode() {
        return inc;
    }
}

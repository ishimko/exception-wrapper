package by.binarylifestyle.exception.wrapper.impl.support;

import java.util.Comparator;

public class ClassHierarchyComparator implements Comparator<Class<?>> {
    @Override
    public int compare(Class<?> c1, Class<?> c2) {
        if (c1 == c2) {
            return 0;
        } else if (c1.isAssignableFrom(c2)) {
            return 1;
        } else if (c2.isAssignableFrom(c1)) {
            return -1;
        } else {
            return 0;
        }
    }
}

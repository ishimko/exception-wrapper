package by.binarylifestyle.exception.wrapper.impl.support;

import by.binarylifestyle.exception.wrapper.api.support.VarargsFunction;
import by.binarylifestyle.exception.wrapper.support.TestData;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

public class VarargsFunctionTest {
    @Test
    public void applyTest() {
        Integer[] integers = {1, 2, 3};
        Function<Integer[], Integer> function = TestData.function();
        VarargsFunction<Integer, Integer> varargsFunction = new VarargsFunction<>(function);

        Integer actualAsVarargs = varargsFunction.apply(1, 2, 3);
        Integer actualAsFunction = varargsFunction.apply(integers);
        Integer expected = function.apply(integers);

        Assert.assertEquals(expected, actualAsVarargs);
        Assert.assertEquals(expected, actualAsFunction);
    }
}

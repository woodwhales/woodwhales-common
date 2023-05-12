package cn.woodwhales.common.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class RandomExtractorTest {


    @Test
    public void test() {
        List<Integer> sampleList = RandomExtractor.randomDrawWithRate(Arrays.asList(1, 2, 3, 4, 5), 0.6D);
        sampleList.stream().forEach(System.out::println);
    }

    @Test
    public void test2() {
        List<Integer> sampleList = RandomExtractor.randomDrawWithCount(Arrays.asList(1, 2, 3, 4, 5), 7);
        sampleList.stream().forEach(System.out::println);
    }
}
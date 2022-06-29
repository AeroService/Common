/*
 * Copyright 2020-2022 NatroxMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.natrox.common.counter;

import de.natrox.common.task.CachedTaskExecutor;
import de.natrox.common.task.TaskExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CounterTest {

    private static TaskExecutor executor;
    private final static Set<Counter> counter = new HashSet<>();
    private final static Set<Counter.Builder> counterBuilder = new HashSet<>();

    @BeforeAll
    private static void init() {
        executor = CachedTaskExecutor.create();
        counterBuilder.add(
            Counter.builder(executor)
                .tick(100, ChronoUnit.MILLIS)
                .startCount(10)
                .stopCount(1)
        );
        counterBuilder.add(
            Counter.builder(executor)
                .tick(100, ChronoUnit.MILLIS)
                .startCount(1)
                .stopCount(10)
        );
        for (Counter.Builder builder : counterBuilder)
            counter.add(builder.build());
    }

    private static Stream<Counter> counter() {
        return counter.stream();
    }

    private static Stream<Counter.Builder> counterBuilder() {
        return counterBuilder.stream();
    }

    @BeforeEach
    private void resetCounters() {
        counter.forEach(Counter::stop);
    }

    @ParameterizedTest
    @MethodSource("counter")
    void isRunningTest(Counter counter) {
        assertFalse(counter.isRunning(), "The Counter should not run unless it got started.");
        counter.start();
        assertTrue(counter.isRunning(), "The Counter should run if it got started, unless it got paused.");
        counter.pause();
        assertFalse(counter.isRunning(), "The Counter should not run if it got paused.");
        counter.resume();
        assertTrue(counter.isRunning(), "The Counter should run after it got resumed.");
        counter.stop();
        assertFalse(counter.isRunning(), "The Counter should not run after it got stopped.");
    }

    @ParameterizedTest
    @MethodSource("counter")
    private void isPausedTest(Counter counter) {
        assertFalse(counter.isPaused(), "The Counter should not be paused unless it got started.");
        counter.start();
        assertFalse(counter.isPaused(), "The Counter should not be paused if it got started, unless it got paused.");
        counter.pause();
        assertTrue(counter.isPaused(), "The Counter should be paused if it got paused.");
        counter.resume();
        assertFalse(counter.isPaused(), "The Counter should not be paused after it got resumed.");
        counter.stop();
        assertFalse(counter.isPaused(), "The Counter should not be paused after it got stopped.");
    }

    @ParameterizedTest
    @MethodSource("counter")
    void stateTest(Counter counter) {
        assertEquals(CounterStatus.IDLING, counter.status(), "The Counter should be idling unless it got started.");
        counter.start();
        assertEquals(CounterStatus.RUNNING, counter.status(), "The Counter should be running if it got started, unless it got paused.");
        counter.pause();
        assertEquals(CounterStatus.PAUSED, counter.status(), "The Counter should be paused if it got paused.");
        counter.resume();
        assertEquals(CounterStatus.RUNNING, counter.status(), "The Counter should be running after it got resumed.");
        counter.stop();
        assertEquals(CounterStatus.IDLING, counter.status(), "The Counter should be idling after it got stopped.");
    }

    @ParameterizedTest
    @MethodSource("counter")
    void failedStartTest(Counter counter) {
        assertDoesNotThrow(counter::start, "The Counter should not throw an exception when it gets started.");
        assertThrows(IllegalStateException.class, counter::start, "The Counter should throw an exception if it got started twice.");
        assertDoesNotThrow(counter::stop, "The Counter should not throw an exception when it gets stopped.");
        assertDoesNotThrow(counter::start, "The Counter should not throw an exception when it gets started again.");
    }

    @ParameterizedTest
    @MethodSource("counter")
    void tickedCountTest(Counter counter) {
        counter.start();
        long minCount = Math.min(counter.startCount(), counter.stopCount());
        final long maxCount = Math.max(counter.startCount(), counter.stopCount());
        for (; minCount <= maxCount; minCount++)
            this.checkTickedCount(counter, minCount);
    }

    void checkTickedCount(Counter counter, long count) {
        counter.setCurrentCount(count);
        assertEquals(count, counter.currentCount(), "The Counter should be at the set value of " + count + ".");
        assertEquals(Math.abs(counter.startCount() - counter.currentCount()), counter.tickedCount(),
            "The Counter should have ticked exactly the difference between the startCount and the currentCount times.");
    }

    @ParameterizedTest
    @MethodSource("counter")
    void presetVariablesTest(Counter counter) {
        counter.start();
        assertEquals(counter.startCount(), counter.currentCount(), "The Counter's startCount should equal the preset startCount.");
        assertEquals(counter.stopCount(), counter.stopCount(), "The Counter's stopCount should equal the preset stopCount.");
    }

    @ParameterizedTest
    @MethodSource("counterBuilder")
    void startCallbackTest(Counter.Builder counterBuilder) {
        AtomicInteger indicator = new AtomicInteger();
        Counter counter = counterBuilder
            .startCallback(c -> indicator.incrementAndGet())
            .build();
        assertEquals(0, indicator.get(), "The Counter should not have started yet.");
        counter.start();
        counter.pause();
        counter.resume();
        counter.stop();
        assertEquals(1, indicator.get(), "The Counter should have started exactly one time.");
    }

    @ParameterizedTest
    @MethodSource("counterBuilder")
    void tickCallbackTest(Counter.Builder counterBuilder) throws InterruptedException {
        AtomicInteger indicator = new AtomicInteger();
        Counter counter = counterBuilder
            .tickCallback(c -> indicator.incrementAndGet())
            .build();
        assertEquals(0, indicator.get(), "The Counter should not have ticked yet.");
        counter.start();
        counter.pause();
        counter.resume();
        Thread.sleep(expectedTimeNeeded(counter));
        counter.stop();
        assertEquals(ticksToFinish(counter), indicator.get(), "The Counter should have ticked from startCount to stopCount.");
    }

    @ParameterizedTest
    @MethodSource("counterBuilder")
    void finishCallbackTest(Counter.Builder counterBuilder) throws InterruptedException {
        AtomicInteger indicator = new AtomicInteger();
        Counter counter = counterBuilder
            .finishCallback(c -> indicator.incrementAndGet())
            .build();
        assertEquals(0, indicator.get(), "The Counter should not have finished yet.");
        counter.start();
        counter.pause();
        counter.resume();
        Thread.sleep(expectedTimeNeeded(counter));
        counter.stop();
        assertEquals(1, indicator.get(), "The Counter should have finished exactly one time.");
    }

    @ParameterizedTest
    @MethodSource("counterBuilder")
    void cancelCallbackTest(Counter.Builder counterBuilder) {
        AtomicInteger indicator = new AtomicInteger();
        Counter counter = counterBuilder
            .cancelCallback(c -> indicator.incrementAndGet())
            .build();
        assertEquals(0, indicator.get(), "The Counter should not have ticked yet.");
        counter.start();
        counter.pause();
        counter.resume();
        counter.stop();
        assertEquals(1, indicator.get(), "The Counter should have been cancelled exactly one time.");
    }

    private long expectedTimeNeeded(Counter counter) {
        //A buffer of one tick added.
        return counter.tickUnit().toMillis(this.ticksToFinish(counter) * counter.tickValue());
    }

    private long ticksToFinish(Counter counter) {
        return Math.abs(counter.startCount() - counter.stopCount()) + 1;
    }
}

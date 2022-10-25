package com.nextlabs.destiny.plugin.test;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bluejungle.framework.expressions.IEvalValue;
import com.nextlabs.destiny.plugin.AttributeDataProvider;
import com.nextlabs.destiny.plugin.domain.TestSubject;
import com.nextlabs.destiny.plugin.engine.ScriptedEngineFactory;

/**
 * 
 */

/**
 * @author kyu
 *
 */
public class GetAttributeTest {

    private AttributeDataProvider attributeDataProvider;

    private long startTime;

    @Before
    public void setUp() throws Exception {
        attributeDataProvider = new AttributeDataProvider();
        Properties paramProperties = new Properties();
        paramProperties.setProperty("enablecache", "true");
        attributeDataProvider.setProperties(paramProperties);
        attributeDataProvider.init();
        File scriptDir = new File("src/test/resources");
        for (File file : scriptDir.listFiles()) {
            ScriptedEngineFactory.loadScriptedEngine(file);
        }

        startTime = System.nanoTime();
    }

    @After
    public void breakDown() {
        System.out.println(System.nanoTime() - startTime + " ns elapsed");
    }

    @Test
    public void loadTest() {
        for (int index = 0; index < 1000; index++) {
            if (index % 100 == 0)
                System.out.println("testing with index: " + index);
            try {
                IEvalValue value = attributeDataProvider.getAttribute(new TestSubject(), "department");
                Assert.assertNotNull(value.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void loadRepeatedTest() {
        TestSubject testSubject = new TestSubject();
        for (int index = 0; index < 10000; index++) {
            if (index % 1000 == 0)
                System.out.println("testing with index: " + index);
            try {
                IEvalValue value = attributeDataProvider.getAttribute(testSubject, "department");
                Assert.assertNotNull(value.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void multiThreadTest() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int index = 0; index < 100; index++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    IEvalValue value;
                    try {
                        value = attributeDataProvider.getAttribute(new TestSubject(), "department");
                        System.out.println("from thread:" + Thread.currentThread().getName() + ":" + value.getValue());
                        Assert.assertNotNull(value.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(2L, TimeUnit.HOURS);
    }

}

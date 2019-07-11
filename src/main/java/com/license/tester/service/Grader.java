package com.license.tester.service;

import com.license.tester.service.exception.TestStructureException;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Grader {

    private static final Logger logger = LoggerFactory.getLogger(Grader.class);
    private final Class<?> interfaceClass;
    private final Class<?> implementingClass;
    private final Class<?> jUnitTestClass;

    public Grader(Class<?> interfaceClass, Class<?> implementingClass, Class<?> jUnitTestClass) {
        this.interfaceClass = interfaceClass;
        this.implementingClass = implementingClass;
        this.jUnitTestClass = jUnitTestClass;
    }

    public double run() {
        try {
            Constructor<?> constructor = jUnitTestClass.getConstructor(String.class, interfaceClass);
            List<Method> testMethods = getTestMethods();
            if (testMethods.isEmpty()) {
                throw new TestStructureException("Could not find any test methods in the test class");
            }
            TestSuite suite = new TestSuite();
            for (Method testMethod : testMethods) {
                suite.addTest((Test) constructor.newInstance(testMethod.getName(), implementingClass.getConstructor().newInstance()));
            }
            TestResult testResult = TestRunner.run(suite);
            return calculateGrade(testResult);
        } catch (NoSuchMethodException e) {
            logger.error("Could not find the required constructor in the test class.", e);
            throw new TestStructureException("Could not find the required constructor in the test class", e);
        } catch (IllegalAccessException e) {
            logger.error("Could not get access to the constructor of the implementing class.", e);
        } catch (InstantiationException e) {
            logger.error("Could not instantiate the implementing class.", e);
        } catch (InvocationTargetException e) {
            logger.error("Could not find the required no args constructor in the implementing class.", e);
        }
        return -1;
    }

    private double calculateGrade(TestResult testResult) {
        int runCount = testResult.runCount();
        int unsuccessfulCount = testResult.errorCount() + testResult.failureCount();
        return ((double) (runCount - unsuccessfulCount) / runCount) * 10;
    }

    private List<Method> getTestMethods() {
        Method[] methods = jUnitTestClass.getMethods();
        List<Method> testMethods = new ArrayList<>();
        for (Method method : methods) {
            Class<?> declaringClass = method.getDeclaringClass();
            if (declaringClass.equals(jUnitTestClass)) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }
}

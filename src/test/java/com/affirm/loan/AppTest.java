package com.affirm.loan;

import com.affirm.loan.repository.AssignmentRepository;
import com.affirm.loan.repository.YieldRepository;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class AppTest {

    @Test
    public void testAppOutput() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File assignmentFile = new File(classLoader.getResource("assignments.csv").getFile());
        File yieldFile = new File(classLoader.getResource("yields.csv").getFile());
        AssignmentRepository expectedAssignments = new AssignmentRepository(assignmentFile);
        YieldRepository expectedYields = new YieldRepository(yieldFile);

        App.main(null);

        Assert.assertEquals(expectedAssignments.get(1), App.assignmentRepository.get(1));
        Assert.assertEquals(expectedAssignments.get(2), App.assignmentRepository.get(2));
        Assert.assertEquals(expectedAssignments.get(3), App.assignmentRepository.get(3));

        Assert.assertEquals(expectedYields.get(1), App.yieldRepository.get(1).setScale(0, BigDecimal.ROUND_UP));
        Assert.assertEquals(expectedYields.get(2), App.yieldRepository.get(2).setScale(0, BigDecimal.ROUND_DOWN));
    }
}

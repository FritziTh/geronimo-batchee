/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.batchee.hazelcast;

import com.hazelcast.core.Hazelcast;
import org.apache.batchee.util.Batches;
import org.testng.annotations.Test;

import jakarta.batch.api.AbstractBatchlet;
import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.BatchStatus;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class LockTest {
    @Test
    public void test() {
        final JobOperator op = BatchRuntime.getJobOperator();
        final long id = op.start("lock", null);
        Batches.waitForEnd(op, id);
        assertEquals(BatchStatus.COMPLETED, op.getJobExecution(id).getBatchStatus());
    }

    private static boolean isLocked() {
        return Hazelcast.getHazelcastInstanceByName("batchee-test").getCPSubsystem().getLock("batchee-lock").isLocked();
    }

    public static class EnsureLockIsHold extends AbstractBatchlet {
        @Override
        public String process() throws Exception {
            assertTrue(isLocked());
            return "OK";
        }
    }

    public static class EnsureLockIsReleased extends AbstractBatchlet {
        @Override
        public String process() throws Exception {
            assertFalse(isLocked());
            return "OK";
        }
    }
}

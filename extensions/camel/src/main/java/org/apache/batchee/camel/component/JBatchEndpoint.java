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
package org.apache.batchee.camel.component;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;

import jakarta.batch.operations.JobOperator;
import org.apache.camel.support.DefaultEndpoint;

public class JBatchEndpoint extends DefaultEndpoint {
    private final JobOperator operator;
    private final String job;
    private int synchronous;
    private boolean restart;
    private boolean abandon;
    private boolean stop;

    public JBatchEndpoint(final String uri, final String remaining, final Component jBatchComponent, final JobOperator operator) {
        super(uri, jBatchComponent);
        this.operator = operator;
        this.job = remaining;
    }

    @Override
    public Producer createProducer() throws Exception {
        return new JBatchProducer(this, operator, job, synchronous, restart, stop, abandon);
    }

    public void setSynchronous(final int synchronous) {
        this.synchronous = synchronous;
    }

    public void setRestart(final boolean restart) {
        this.restart = restart;
    }

    public void setAbandon(final boolean abandon) {
        this.abandon = abandon;
    }

    public void setStop(final boolean stop) {
        this.stop = stop;
    }

    @Override
    public Consumer createConsumer(final Processor processor) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

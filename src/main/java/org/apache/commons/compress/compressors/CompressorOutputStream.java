/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.compressors;

import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class CompressorOutputStream extends OutputStream {

    private long bytesWritten = 0;
    private final List<CompressionProgressListener> listeners =
        new CopyOnWriteArrayList<CompressionProgressListener>();

    /**
     * Adds a listener that is notified of compression progress.
     *
     * <p>Not all streams support progress notifications.</p>
     *
     * @param l the listener to add
     */
    public void addCompressionProgressListener(CompressionProgressListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener that is notified of compression progress.
     *
     * @param l the listener to remove
     */
    public void removeCompressionProgressListener(CompressionProgressListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies all listeners of progress.
     *
     * @param blockNumber number of the block that is getting processed now
     * @param streamNumer number of the stream that is getting
     *        processed now
     * @param compressedBytesWritten number of compressed bytes written
     */
    protected void fireProgress(int blockNumber, int streamNumber, long compressedBytesWritten) {
        CompressionProgressEvent e = new CompressionProgressEvent(this, blockNumber, streamNumber,
            compressedBytesWritten, getBytesWritten());
        for (CompressionProgressListener l : listeners) {
            try {
                l.notify(e);
            } catch (Exception ex) {
                // TODO what to do with it?
            }
        }
    }

    /**
     * Increments the counter of already written bytes.
     *
     * @param written the number of bytes written
     */
    protected void count(long written) {
        bytesWritten += written;
    }

    /**
     * Returns the current number of bytes written from this stream.
     * @return the number of written bytes
     */
    public long getBytesWritten() {
        return bytesWritten;
    }
}
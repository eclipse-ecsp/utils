package com.harman.ignite.utils.metrics;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Histogram;

/**
 * Base histogram for Ignite
 * 
 * @author ssasidharan
 */
public abstract class AbstractIgniteHistogram {
    private Histogram histogram;

    protected void createHistogram(String name, String help, double[] buckets, String... labelNames) {
        histogram = Histogram.build(name, help).labelNames(labelNames).buckets(buckets).register(CollectorRegistry.defaultRegistry);
    }

    public void observe(double amt, String... labels) {
        histogram.labels(labels).observe(amt);
    }

    public IgniteTimer start() {
        return new IgniteTimer(this);
    }

    public <T> T observe(Supplier<T> f, String... labels) {
        IgniteTimer timer = start();
        try {
            return f.get();
        } finally {
            timer.observe(labels);
        }
    }

    public void observe(Runnable f, String... labels) {
        IgniteTimer timer = start();
        try {
            f.run();
        } finally {
            timer.observe(labels);
        }
    }

    /**
     * Allows exception to be thrown unlike observe(Supplier)
     * 
     * @param f
     * @param labels
     * @return
     * @throws Exception
     */
    public <V> V observeExtended(Callable<V> f, String... labels) throws Exception {
        IgniteTimer timer = start();
        try {
            return f.call();
        } finally {
            timer.observe(labels);
        }
    }

    public static class IgniteTimer {
        private AbstractIgniteHistogram histo = null;
        private long start = 0L;

        public IgniteTimer(AbstractIgniteHistogram histo) {
            this.histo = histo;
            this.start = System.nanoTime();
        }

        public double observe(String... labels) {
            double amt = (System.nanoTime() - start) / 1E9D;
            histo.observe(amt, labels);
            return amt;
        }
    }

    Histogram getHistogram() {
        return histogram;
    }
}
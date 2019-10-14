package edu.uci.ics.matthes3.service.api_gateway.threadpool;

import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;

public class ThreadPool {
    private int numWorkers;
    private Worker[] workers;
    private ClientRequestQueue queue;

    public ThreadPool(int numThreads) {
        ServiceLogger.LOGGER.info("Getting request to initialize " + numThreads + " workers.");
        this.numWorkers = numThreads;
        this.queue = new ClientRequestQueue();
        this.workers = new Worker[numThreads];
        for (int i = 0; i < numThreads; i++) {
            workers[i] = Worker.CreateWorker(i, this);
            workers[i].start();
        }

        startWorkers(workers);
    }

    private void startWorkers(Worker[] workers) {
//        for (int i = 0; i < workers.length; i++) {
//            workers[i] = Worker.CreateWorker(i, this);
//            workers[i].start();
//        }

//        ServiceLogger.LOGGER.info("Initialized " + workers.length + " workers.");
//
//        for (Worker w : workers) {
//            ServiceLogger.LOGGER.info("Starting worker " + w.getId());
//            w.start();
//        }
    }

    public void add(ClientRequest clientRequest) {
        queue.enqueue(clientRequest);
    }

    public ClientRequest remove() {
        return queue.dequeue();
    }

    public ClientRequestQueue getQueue() {
        return queue;
    }
}

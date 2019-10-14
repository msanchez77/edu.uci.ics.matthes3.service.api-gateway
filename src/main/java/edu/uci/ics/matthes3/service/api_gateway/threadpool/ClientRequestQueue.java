package edu.uci.ics.matthes3.service.api_gateway.threadpool;

import edu.uci.ics.matthes3.service.api_gateway.logger.ServiceLogger;

public class ClientRequestQueue {
    private ListNode head;
    private ListNode tail;

    public ClientRequestQueue() {
        head = tail = null;
    }

    public synchronized void enqueue(ClientRequest clientRequest) {
//        ServiceLogger.LOGGER.info("Enqueueing " + clientRequest.toString());

        boolean emptyBefore = isEmpty();
//        if (isEmpty()) {
//            this.notifyAll();
//        }

        final ListNode new_node = new ListNode(clientRequest, null);

        if (tail == null) {
//            ServiceLogger.LOGGER.info("Enqueued to empty queue");
            head = new_node;
        }
        else {
//            ServiceLogger.LOGGER.info("Enqueued to non-empty queue");
            tail.setNext(new_node);
        }

        tail = new_node;

        if (emptyBefore)
            notifyAll();
    }

    public synchronized ClientRequest dequeue() {
        while (isEmpty()) {
//            ServiceLogger.LOGGER.info("Can't dequeue empty queue.");

            try {
                this.wait();
            } catch (InterruptedException e) {
                ServiceLogger.LOGGER.info("WAITING EXCEPTION ENCOUNTERED.");
                e.printStackTrace();
            }
        }

        final ListNode oldHead = head;
        head = head.getNext();

        if (isEmpty()) {
            ServiceLogger.LOGGER.info("Queue is now empty.");
            tail = null;
        }

//        ServiceLogger.LOGGER.info("Returning client request: " + oldHead.getClientRequest().toString());
        return oldHead.getClientRequest();
    }

    boolean isEmpty() {
        return head == null;
    }

    boolean isFull() {
        return false;
    }

    @Override
    public String toString() {
        String result = "";

        ListNode current = head;
        while (current.getNext() != null) {
            result += current.getClientRequest().getEmail();
            if(current.getNext() != null){
                result += ", ";
            }
            current = current.getNext();
        }

        return "List: " + result;
    }
}

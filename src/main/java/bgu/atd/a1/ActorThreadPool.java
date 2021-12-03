package bgu.atd.a1;

import java.util.*;
import java.util.concurrent.*;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

    private Map<String, PrivateState> actors;
    private Map<String, Queue<Action>> actionQueue;
    private LinkedBlockingQueue<String> availableActors;
    private Queue<String> unavailableActors;
    private int threadsNum;
    private ExecutorService threads;


    /**
     * creates a {@link ActorThreadPool} which has nthreads. Note, threads
     * should not get started until calling to the {@link #start()} method.
     * <p>
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nThreads the number of threads that should be started by this thread
     *                 pool
     */
    public ActorThreadPool(int nThreads) {
        this.actors = new HashMap<>();
        this.actionQueue = new HashMap<>();
        this.availableActors = new LinkedBlockingQueue<>();
        this.unavailableActors = new ConcurrentLinkedDeque<>();
        this.threadsNum = nThreads;
        this.threads = Executors.newFixedThreadPool(nThreads);
    }

    /**
     * getter for actors
     *
     * @return actors
     */
    public Map<String, PrivateState> getActors() {
        return actors;
    }

    /**
     * getter for actor's private state
     *
     * @param actorId actor's id
     * @return actor's private state
     */
    public PrivateState getPrivateState(String actorId) {
        return actors.get(actorId);
    }


    /**
     * submits an action into an actor to be executed by a thread belongs to
     * this thread pool
     *
     * @param action     the action to execute
     * @param actorId    corresponding actor's id
     * @param actorState actor's private state (actor's information)
     */
    public void submit(Action<?> action, String actorId, PrivateState actorState) {
        synchronized (actorState) { // avoid double submit of the same actor at the same time
            if (!actors.containsKey(actorId)) {
                actors.put(actorId, actorState);
                actionQueue.put(actorId, new ConcurrentLinkedDeque<>());
            }
        }
        actionQueue.get(actorId).add(action);
        /* synchronized (availableActors) for avoiding a situation in which thread1 see that actorId is available and
        . take it from the availableActors queue. Then, thread2 submit the actorId and add it into the availableActors queue
        . although it's not supposed to be available.
        */
        synchronized (availableActors) {
            if (!unavailableActors.contains(actorId) && !availableActors.contains(actorId)) {
                availableActors.add(actorId);
                availableActors.notifyAll();
            }
        }
    }

    /**
     * closes the thread pool - this method interrupts all the threads and waits
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     * <p>
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is interrupted
     */
    public void shutdown() throws InterruptedException {
        threads.shutdownNow();
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        for (int i = 0; i < threadsNum; i++) {
            threads.execute(new HandleActorTask(this, actors, actionQueue, availableActors, unavailableActors, threadsNum, threads));
        }
    }
}

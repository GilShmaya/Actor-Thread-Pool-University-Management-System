package bgu.atd.a1;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class HandleActorTask implements Runnable {
    private ActorThreadPool pool;
    private Map<String, PrivateState> actors;
    private Map<String, Queue<Action>> actionQueue;
    private LinkedBlockingQueue<String> availableActors;
    private Queue<String> unavailableActors;
    private int threadsNum;
    private ExecutorService threads;

    public HandleActorTask(ActorThreadPool pool, Map<String, PrivateState> actors, Map<String, Queue<Action>> actionQueue, LinkedBlockingQueue<String> availableActors, Queue<String> unavailableActors, int threadsNum, ExecutorService threads) {
        this.pool = pool;
        this.actors = actors;
        this.actionQueue = actionQueue;
        this.availableActors = availableActors;
        this.unavailableActors = unavailableActors;
        this.threadsNum = threadsNum;
        this.threads = threads;
    }

    @Override
    public void run() {
        while (!threads.isShutdown()) {
            Boolean toAct = false;
            String actorName = null;
            PrivateState actorState = null;
            synchronized (availableActors) {
                try {
                    while (availableActors.isEmpty()) {
                        availableActors.wait();
                    }
                    actorName = availableActors.take();
                    unavailableActors.add(actorName);
                    actorState = actors.get(actorName);
                } catch (InterruptedException e) {
                    break;
                }
            }
            if(!actionQueue.get(actorName).isEmpty())
                actionQueue.get(actorName).poll().handle(pool, actorName, actorState);

            synchronized (availableActors) {
                unavailableActors.remove(actorName);
                if (!actionQueue.get(actorName).isEmpty()) {
                    availableActors.add(actorName);
                    availableActors.notifyAll();
                }
            }

        }
    }
}

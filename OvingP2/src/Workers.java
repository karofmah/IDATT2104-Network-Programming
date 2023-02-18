import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Workers {
    private final int numberOfThreads;
    private final ArrayList<Thread> threads = new ArrayList<>();
    private final List<Runnable> tasks = new ArrayList<>();
    private volatile boolean stop=false;
    private final ReentrantLock tasksLock = new ReentrantLock();
    private final Condition cv = tasksLock.newCondition();


    public Workers(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void start() {

        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(this::runTasks);
            threads.add(thread);
            thread.start();
        }
    }

    public void runTasks() {
        while (!stop) {
            Runnable task = null;
            try {
                tasksLock.lock();

                while (tasks.isEmpty() && !stop) {
                    cv.await();
                }

                if (!tasks.isEmpty()) {
                    task = tasks.get(0);
                    tasks.remove(task);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                tasksLock.unlock();
            }

            if (task != null) {
                post_timeout(task, 2000);
            }
            tasksLock.lock();
            if(tasks.isEmpty() && !stop){
                stop();
            }
            tasksLock.unlock();
        }
    }

    public void stop() {
        stop = true;
        cv.signalAll();

    }

    public void join() throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }

    public void post_timeout(Runnable task, long time) {
        try {
            Thread.sleep(time);
            task.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            tasksLock.lock();
            cv.signalAll();
            tasksLock.unlock();
        }
    }

    public void post(Runnable task) {
        tasksLock.lock();
        try {
            tasks.add(task);
            cv.signalAll();
        } finally {
            tasksLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Workers worker_threads = new Workers(4);
        Workers event_loop = new Workers(1);

        worker_threads.start();
        event_loop.start();

        worker_threads.post(() -> System.out.println("Task A"));
        worker_threads.post(() -> System.out.println("Task B"));

        event_loop.post(() -> System.out.println("Task C"));
        event_loop.post(() -> System.out.println("Task D"));

        worker_threads.join();
        event_loop.join();

    }
}

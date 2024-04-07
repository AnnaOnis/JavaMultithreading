//2) Есть объект, в котором 2 счетчика- 1 типа long, 2- integer (оба на старте 0).
//Задача: любыми средствами реализовать инкремент каждой величины в 3 потока одним методом по 1000 раз.
// В результате должен получиться объект с 3000 в каждом из полей.
public class Counters {
    public static void main(String[] args) {
        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            counter.incrementCounters();
        });

        Thread t2 = new Thread(() -> {
            counter.incrementCounters();
        });

        Thread t3 = new Thread(() -> {
            counter.incrementCounters();
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Long Counter: " + counter.getLongCounter());
        System.out.println("Int Counter: " + counter.getIntCounter());
    }
}

class Counter {
    private long longCounter = 0;
    private int intCounter = 0;

    public synchronized void incrementCounters() {
        for (int i = 0; i < 1000; i++) {
            longCounter++;
            intCounter++;
        }
    }

    public long getLongCounter() {
        return longCounter;
    }

    public int getIntCounter() {
        return intCounter;
    }
}

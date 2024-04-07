//1) Создать класс, в котором будет три метода:
//void first() {System.out(“first”)}, по аналогии second и third.
//Запустить 3 потока, которые по очереди вызывали бы соответствующие своему порядку инициализации методы.
//Работу организовать так, чтобы в консоли был вывод:
//Third Second First

public class Main {
    public static void main(String[] args) {
        MyClass obj = new MyClass();

        Thread t3 = new Thread(() -> {
                obj.third();
        });

        Thread t2 = new Thread(() -> {
            try {
                t3.join();
                obj.second();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t1 = new Thread(() -> {
            try {
                t2.join();
                obj.first();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t3.start();
    }
}

class MyClass {
    synchronized void first() {
        System.out.println("First");
    }

    synchronized void second() {
        System.out.println("Second");
    }

    synchronized void third() {
        System.out.println("Third");
    }
}
//3)  Есть массив (список) с 20_000_000 чисел от 1 до 20_000_000
// (если машина послабее, возьмите тот диапазон, который она высчитает за 10-20 секунд).
// Предлагается посчитать сумму элементов массива. Найти количество потоков (разное для каждого компьютера),
// начиная с которого разбиение на подзадачи и увеличение количества потоков перестанет давать результат.
public class ArraySumCalculate {
    public static void main(String[] args) {
        int[] array = new int[20_000_000];
        for(int i = 0; i < array.length; i++){
            array[i] = i+1;
        }
        ArraySumCalculator calculator = new ArraySumCalculator(array);

        //находим оптимальное количество потоков
        int optimalNumThreads = findOptimalNumThreads(array);

        long startTime = System.currentTimeMillis();
        long rezult = calculator.calculateSum(optimalNumThreads);
        long endTime = System.currentTimeMillis();

        System.out.println("Сумма элементов массива " + rezult);
        System.out.println("Оптимальное количество потоков: " + optimalNumThreads);
        System.out.println("Время выполнения: " + (endTime - startTime) + " миллисекунд");

    }
    //метод определения оптимального количества потоков для многопоточного вычисления суммы элементов массива
    //для конкретной машины
    private static int findOptimalNumThreads(int[] array) {
        int processors = Runtime.getRuntime().availableProcessors();
        int optimalNumThreads = 1;
        long minTime = Long.MAX_VALUE;

        // Проверяем производительность для разного количества потоков
        for (int numThreads = 1; numThreads <= processors; numThreads++) {
            long startTime = System.currentTimeMillis();
            ArraySumCalculator calculator = new ArraySumCalculator(array);
            calculator.calculateSum(numThreads);
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;

            if (timeTaken < minTime) {
                minTime = timeTaken;
                optimalNumThreads = numThreads;
            } else {
                // Как только производительность перестает расти, выходим из цикла
                break;
            }
        }
        return optimalNumThreads;
    }
}
class ArraySumCalculator{
    private  int[] array;
    private long sum;
    public ArraySumCalculator(int[] array){
        this.array = array;
    }
    public  long calculateSum(int numThread){

        SumThread[] threads = new SumThread[numThread];
        int chankSize = array.length / numThread;

        for(int i = 0; i < numThread; i++){
            int start = chankSize * i;
            int end = (i == numThread - 1) ? array.length : chankSize * (i + 1);
            threads[i] = new SumThread(start, end);
            threads[i].start();
        }

        try {
            for (SumThread thread : threads) {
                thread.join();
                sum += thread.getSum();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return sum;
    }
    private class SumThread extends  Thread{
        private int start;
        private  int end;
        private long sum;
        public SumThread(int s, int e){
            this.start = s;
            this.end = e;
        }

        @Override
        public void run(){
            for(int i = start; i <end; i++){
                sum += array[i];
            }
        }

        public long getSum() {
            return sum;
        }
    }
}




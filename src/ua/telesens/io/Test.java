package ua.telesens.io;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static ua.telesens.io.LookingForMask.atomic;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        LookingForMask lfm = new LookingForMask();
        GettingSomeLength fileWithLength = new GettingSomeLength(lfm);
        GettingSpecificAttributes fileWithAttributes = new GettingSpecificAttributes(lfm);
        GettingSomeText fileWithText = new GettingSomeText(lfm);

        Runnable task1 = () -> {
            lfm.run();
        };

//        Thread t1_mask = new Thread(task1);

        Runnable task2 = () -> {
            fileWithLength.run();
        };

        Runnable task3 = () -> {
            fileWithAttributes.run();
        };

        Runnable task4 = () -> {
            fileWithText.run();
        };

        new Thread(task1).start(); // запуск первой нити по поиску файлов в каталоге/подкаталогах с расширением

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(3); // пул потоков с расписанием
        ScheduledFuture<?> scheduledFuture2 = ses.scheduleAtFixedRate(task2, 3, 3, TimeUnit.SECONDS); //опрос каждые 3 секунды / задержка - 3 сек
        ScheduledFuture<?> scheduledFuture3 = ses.scheduleAtFixedRate(task3, 3, 3, TimeUnit.SECONDS);
        ScheduledFuture<?> scheduledFuture4 = ses.scheduleAtFixedRate(task4, 3, 3, TimeUnit.SECONDS);

        while (true) {
            Thread.sleep(4000);
            System.out.println("count :" + atomic); // вывод счетчика количества найденых файлов

            if (atomic.get() > 0) { // если найден хоть 1 файл, то начинается выполнение
                System.out.println("RESULT COUNT :" + atomic);
                Thread.sleep(5000); // даем 5 с на обработку найденых файлов
                scheduledFuture2.cancel(true); // завершаем выполнение наших нитей
                scheduledFuture3.cancel(true);
                scheduledFuture4.cancel(true);
                ses.shutdown();

                break;
            }
        }
        System.out.println("All tasks are done");
    }
}
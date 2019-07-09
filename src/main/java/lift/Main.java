package lift;

import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        /*ArrayList<Visitor> list = new ArrayList<>();
        list.add(new)*/
        //пункт 4.2
        /*BusinessCenter bc = new BusinessCenter();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            CompletableFuture<Visitor> future = CompletableFuture.supplyAsync(() -> new Visitor(bc), executorService);
            future.thenAcceptAsync(visitor -> {
                try {
                    System.out.println("starting "+visitor);
                    visitor.enterBuilding();
                    visitor.goUp();
                    visitor.doSomeWork();
                    visitor.goDown();
                } catch (InterruptedException e) {
                    System.out.println("interrupted");
                }
            });
        }
        System.out.println("All tasks submitted"); //все задания переданы
        executorService.awaitTermination(1, TimeUnit.MINUTES);*/

        //пункт 4.1
/*        BusinessCenter bc = new BusinessCenter();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            Future<Visitor> future = executorService.submit(() -> new Visitor(bc));
            Visitor visitor = future.get();
            System.out.println("Future" + (i+1) + " done? - " + future.isDone());
            System.out.println("Посетитель: " + visitor);
           executorService.submit(visitor);
        }

        executorService.shutdown();// потоки больше не могут получать задания и начинают выполнение
        System.out.println("All tasks submitted"); //все задания переданы
        executorService.awaitTermination(1, TimeUnit.MINUTES); //ждем сколько то врмени пока задания не будут выполнены*/

        //пункт 1
        BusinessCenter bc = new BusinessCenter();
        Thread thread1 = new Thread(new Visitor(bc));
        Thread thread2 = new Thread(new Visitor(bc));
        thread1.setName("Посетитель 1");
        thread1.setName("Посетитель 2");
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

    }
}

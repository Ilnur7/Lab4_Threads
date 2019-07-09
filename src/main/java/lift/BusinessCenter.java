package lift;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BusinessCenter {

    private int liftFloor = 1;
    private Visitor visitorAtControl;
    private Visitor visitorInLift;
    private boolean liftFree = true;
    private static long startTime;
    //private Object lock1 = new Object();
    //private Object lock2 = new Object();
    ReentrantLock lock1;
    Condition condition1;
    ReentrantLock lock2;
    Condition condition2;

    public BusinessCenter() {
        startTime = System.currentTimeMillis();
        lock1 = new ReentrantLock(); // создаем блокировку
        condition1 = lock1.newCondition(); // получаем условие, связанное с блокировкой
        lock2 = new ReentrantLock(); // создаем блокировку
        condition2 = lock2.newCondition(); // получаем условие, связанное с блокировкой
    }

    public boolean enterControl(Visitor visitor) throws InterruptedException {
        System.out.println((System.currentTimeMillis() - startTime) + " ms: Пришел Посетитель" + visitor.getNum());
        lock1.lock();
        try {
            while (getVisitorAtControl() != null) {
                condition1.await();
            }
            setVisitorAtControl(visitor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
        }
        return true;
    }

    public long runTime(){
        return System.currentTimeMillis() - startTime;
    }

    public void passControl(Visitor visitor) throws Exception {
        System.out.println(runTime() + " ms: Посетитель" + visitor.getNum() + " показывает документы");
        Thread.sleep(300);

        lock1.lock();
        try {
            if (getVisitorAtControl() == visitor) {
                System.out.println(runTime() + " ms: Посетитель" + visitor.getNum() + " показал документы");
                System.out.println(runTime() + " ms: Проходная свободна");

            } else {
                System.err.println("wrong visitor at control"); System.exit(1);
            }
            setVisitorAtControl(null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            condition1.signal();
            lock1.unlock();
        }
    }

    public boolean callLift(Visitor visitor) throws InterruptedException {

        System.out.println(runTime() + " ms: Посетитель" + visitor.getNum() + " вызывает лифт на этаж " + visitor.getFloor());
        lock2.lock();
        try {
            while ((visitorInLift != null) && (isLiftFree() == false)) {
                System.out.println(runTime() + " ms: Подождите, лифт занят Посетителем" + visitor.getNum());
                condition2.await();
                System.out.println(runTime() + " ms: Лифт едет к Посетителю" + visitor.getNum() + ", который ждет на этаже " + visitor.getFloor());
            }
            setVisitorInLift(visitor);
            setLiftFree(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock2.unlock();
        }
        return true;
    }

    public void moveLift(Visitor visitor, int targetFloor) {
        //synchronized (lock2) {
            int differenceFloor = targetFloor - getLiftFloor();
            if (targetFloor != getLiftFloor()){
                System.out.println(runTime() + " ms: Лифт на " + getLiftFloor() + " этаже и едет на " + targetFloor + " этаж");
            }else System.out.println(runTime() + " ms: Лифт на " + getLiftFloor() + " этаже");

            while (targetFloor != getLiftFloor()) {
                if (differenceFloor < 0) {
                    liftFloor--;
                    //setLiftFloor(liftFloor);
                    waitOneFloorLift();
                    System.out.println(runTime() + " ms: Лифт едет вниз, на " + getLiftFloor() + " этаже");

                } else {
                    liftFloor++;
                   // setLiftFloor(liftFloor);
                    waitOneFloorLift();
                    System.out.println(runTime() + " ms: Лифт едет вверх, на " + getLiftFloor() + " этаже");
                }
            }
        //}
    }

    public void waitOneFloorLift(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterLift(Visitor visitor, int targetFloor) {
        //synchronized (lock2){
            System.out.println(runTime() + " ms: Посетитель" + visitor.getNum() + " вошел в лифт и едет на " + targetFloor + " этаж");
        //}

    }

    public void exitLift(Visitor visitor) {
        lock2.lock();
        try {
            System.out.println(runTime() + " ms: Посетитель" + visitor.getNum() + " вышел из лифта ");
            System.out.println(runTime() + " ms: Лифт свободен ");
            visitor.setFloor(getLiftFloor());
            setLiftFree(true);
            setVisitorInLift(null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            condition2.signal();
            lock2.unlock();
        }
    }

    public int getLiftFloor() {
        return liftFloor;
    }

    public Visitor getVisitorAtControl() {
        return visitorAtControl;
    }

    public Visitor getVisitorInLift() {
        return visitorInLift;
    }

    public boolean isLiftFree() {
        return liftFree;
    }

    public static long getStartTime() {
        return startTime;
    }

    public void setLiftFloor(int liftFloor) {
        this.liftFloor = liftFloor;
    }

    public void setVisitorAtControl(Visitor visitorAtControl) {
        this.visitorAtControl = visitorAtControl;
    }

    public void setVisitorInLift(Visitor visitorInLift) {
        this.visitorInLift = visitorInLift;
    }

    public void setLiftFree(boolean liftFree) {
        this.liftFree = liftFree;
    }
}

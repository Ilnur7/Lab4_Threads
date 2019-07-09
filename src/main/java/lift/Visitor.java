package lift;

import java.util.Random;
import java.util.function.Supplier;

public class Visitor implements Runnable {

    private BusinessCenter place;
    private static int totalCount;
    private int num; //номер посетителя
    private int floor = 1;

    public Visitor(BusinessCenter place) {
        this.place = place;
        totalCount++;
        num = totalCount;
    }

    public void run(){
        try {
            enterBuilding();
            goUp();
            doSomeWork();
            goDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterBuilding() throws InterruptedException {
        boolean isAllowed = place.enterControl(this);
        if (isAllowed) {
            try {
                place.passControl(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void goUp() throws InterruptedException {
        Random random = new Random();
        int randomFloor = random.nextInt(10) + 2;
        boolean isReserveLift = place.callLift(this); //вызываем лифт
        if (isReserveLift){
            place.moveLift(this, this.getFloor());  //лифт едет на этаж, где посетитель
        }
        place.enterLift(this, randomFloor); //заходим в лифт
        place.moveLift(this, randomFloor); //едем на выбранный этаж
        place.exitLift(this); //освобождаем лифт
    }

    public void doSomeWork(){
        System.out.println((System.currentTimeMillis() - BusinessCenter.getStartTime()) + " ms: Посетитель" + this.getNum() + " что-то делает ");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println((System.currentTimeMillis() - BusinessCenter.getStartTime()) + " ms: Посетитель" + this.getNum() + " закончил дела ");
    }

    public void goDown() throws InterruptedException {
        boolean isReserveLift = place.callLift(this); //вызываем лифт
        if (isReserveLift){
            place.moveLift(this, this.getFloor());  //лифт едет на этаж, где посетитель
        } else throw new InterruptedException();
        place.enterLift(this, 1); //заходим в лифт
        place.moveLift(this, 1);//едем на выбранный этаж
        place.exitLift(this); //освобождаем лифт
    }

    public int getNum() {
        return num;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "num=" + num +
                ", floor=" + floor +
                '}';
    }
}

package chapter13;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ex26 {
    public static void main (String[] args) throws Exception{
        Table26 table = new Table26();

        new Thread(new Cook26(table), "COOK1").start();
        new Thread(new Customer26(table, "donut"), "CUST1").start();
        new Thread(new Customer26(table, "burger"), "CUST2").start();

        Thread.sleep(2000);
        System.exit(0);
    }
}

class Customer26 implements Runnable {
    private Table26 table;
    private String food;

    Customer26(Table26 table, String food){
        this.table = table;
        this.food = food;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e){
            }
            String name = Thread.currentThread().getName();

            table.remove(food);
            System.out.println(name + " ate a " + food);
        }

    }
}

class Cook26 implements Runnable {
    private Table26 table;
    Cook26(Table26 table){this.table = table;}

    @Override
    public void run() {
        while(true) {

            int idx = (int) (Math.random() * table.dishNum());
            table.add(table.dishNames[idx]);
            try {Thread.sleep(1);} catch (InterruptedException e){}
        }

    }
}

class Table26 {
    String[] dishNames = {"donut", "donut", "burger"};
    final int MAX_FOOD = 6;
    private ArrayList<String> dishes = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();
    private Condition forCook = lock.newCondition();
    private Condition forCust = lock.newCondition();


    public synchronized void add(String dish) {
        lock.lock();
        try {

            while (dishes.size() >= MAX_FOOD) {
                String name = Thread.currentThread().getName();
                System.out.println(name + " is waiting.");
                try {
                    forCook.await();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
            dishes.add(dish);
            forCust.signal();
            System.out.println("Dishes: " + dishes.toString());
        } finally {
            lock.unlock();
        }
    }

    public void remove(String dishName) {
        lock.lock();
//        synchronized (this) {
        String name = Thread.currentThread().getName();
        try {

            while (dishes.size() == 0) {
                System.out.println(name + " is waiting.");
                try {
                    forCust.await();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
            while(true) {
                for (int i = 0; i < dishes.size(); i++)
                    if (dishName.equals(dishes.get(i))) {
                        if (dishName.equals(dishes.get(i))) {
                            dishes.remove(i);
                            forCook.signal();
                            return;
                        }
                    }

                try {
                    System.out.println(name + " is waiting.");
                    forCust.await();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
            // } //sycnronized
        } finally {
            lock.unlock();
        }
    }
    public int dishNum() {return dishNames.length;}
}
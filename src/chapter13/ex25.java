package chapter13;

import java.util.ArrayList;

public class ex25 {
    public static void main (String[] args) throws Exception{
        Table25 table = new Table25();

        new Thread(new Cook25(table), "COOK1").start();
        new Thread(new Customer25(table, "donut"), "CUST1").start();
        new Thread(new Customer25(table, "burger"), "CUST2").start();

        Thread.sleep(5000);
        System.exit(0);
    }
}

class Customer25 implements Runnable {
    private Table25 table;
    private String food;

    Customer25(Table25 table, String food){
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

class Cook25 implements Runnable {
    private Table25 table;
    Cook25(Table25 table){this.table = table;}

    @Override
    public void run() {
        while(true) {

            int idx = (int) (Math.random() * table.dishNum());
            table.add(table.dishNames[idx]);
            try {Thread.sleep(1);} catch (InterruptedException e){}
        }

    }
}

class Table25 {
    String[] dishNames = {"donut", "donut", "burger"};
    final int MAX_FOOD = 6;

    private ArrayList<String> dishes = new ArrayList<>();

    public synchronized void add(String dish){
        while (dishes.size() >= MAX_FOOD) {
            String name = Thread.currentThread().getName();
            System.out.println(name + " is waiting.");
            try {
                wait();
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }
        dishes.add(dish);
        notify();
        System.out.println("Dishes: " + dishes.toString());
    }

    public void remove(String dishName){
        synchronized (this) {
            String name = Thread.currentThread().getName();
            while (dishes.size() == 0){
                System.out.println(name+" is waiting.");
                try {
                    wait();
                    Thread.sleep(500);}catch (InterruptedException e) {}
            }
            while (true){

                for (int i = 0 ; i< dishes.size(); i++)
                    if(dishName.equals(dishes.get(i))){
                        if(dishName.equals(dishes.get(i))){
                            dishes.remove(i);
                            notify();
                            return;
                        }
                    }

                try {
                    System.out.println(name + " is waiting.");
                    wait();
                    Thread.sleep(500);
                } catch (InterruptedException e) {}
            }
        }
    }

    public int dishNum() {return dishNames.length;}
}
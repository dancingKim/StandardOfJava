package chapter13;

import java.util.ArrayList;

public class ex23 {
    public static void main (String[] args) throws Exception{
        Table23 table = new Table23();

        new Thread(new Cook23(table), "COOK1").start();
        new Thread(new Customer23(table, "donut"), "CUST1").start();
        new Thread(new Customer23(table, "burger"), "CUST2").start();

        Thread.sleep(100);
        System.exit(0);
    }
}

class Customer23 implements Runnable {
    private Table23 table;
    private String food;

    Customer23(Table23 table, String food){
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

            if (eatFood())
                System.out.println(name + " ate a " + food);
            else
                System.out.println(name + " fialed to eat. :(");
        }

    }
    boolean eatFood() {return table.remove(food);}
}

class Cook23 implements Runnable {
    private Table23 table;
    Cook23(Table23 table){this.table = table;}

    @Override
    public void run() {

    }
}

class Table23 {
    String[] dishNames = {"donut", "donumt", "burger"};
    final int MAX_FOOD = 6;

    private ArrayList<String> dishes = new ArrayList<>();

    public void add(String dish){
        if (dishes.size() >= MAX_FOOD)
            return;
        dishes.add(dish);
        System.out.println("Dishes: " + dishes.toString());
    }

    public boolean remove(String dishName){
        for (int i = 0 ; i< dishes.size(); i++)
            if(dishName.equals(dishes.get(i))){
                if(dishName.equals(dishes.get(i))){
                    dishes.remove(i);
                    return true;
                }
            }
        return false;
    }

    public int dishNum() {return dishNames.length;}
}
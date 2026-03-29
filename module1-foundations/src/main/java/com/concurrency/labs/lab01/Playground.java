package com.concurrency.labs.lab01;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Playground {

public Playground(){

}

public static void main(String[] args){
    ExecutorService es = Executors.newFixedThreadPool(2);
    CountDownLatch cDownLatch = new CountDownLatch(1);
    es.submit(() -> {
        for(int threadId = 0; threadId < 10; threadId++){
            try{
                cDownLatch.await();
                System.out.println(threadId);
            } catch(InterruptedException interrputed){
                interrputed.printStackTrace();
            }
        }
        
    });
    cDownLatch.countDown();
    // cDownLatch.notifyAll();
    es.shutdown();
}

class CustomThread extends Thread{
    private int id;
    public CustomThread(int id){
        this.id = id;
    }

    public void printStatus(){
        System.out.println("ID is " + id);
    }
}

}
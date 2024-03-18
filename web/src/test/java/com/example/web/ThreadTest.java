package com.example.web;

import org.junit.jupiter.api.Test;

public class ThreadTest {
    @Test
    public void testThreadGetAndSet(){
         ThreadLocal tl=new ThreadLocal();

         new Thread(()->{
             tl.set("箫炎");
             System.out.println(Thread.currentThread().getName()+" :"+tl.get());
             System.out.println(Thread.currentThread().getName()+" :"+tl.get());
             System.out.println(Thread.currentThread().getName()+" :"+tl.get());
         },"蓝色").start();

        new Thread(()->{
            tl.set("药尘");
            System.out.println(Thread.currentThread().getName()+" :"+tl.get());
            System.out.println(Thread.currentThread().getName()+" :"+tl.get());
            System.out.println(Thread.currentThread().getName()+" :"+tl.get());
        },"红色").start();
    }
}

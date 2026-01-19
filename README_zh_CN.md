
# JellyFish

水母

[English](./README.md)

支持功能:
- [x] ConcurrencyHashMap
- [x] ConcurrencyTreeMap
- [x] ConcurrencyArrayDeque
- [x] ConcurrencyLinkedList
- [x] ThreadLocalMap
- [x] CompletableFutureOne
- [x] ThreadPool
- [x] ThreadScheduledOne

# ConcurrencyHashMap:
支持多线程读写

# ConcurrencyTreeMap:
支持多线程读写

# ConcurrencyArrayDeque:
支持多线程读写

# ConcurrencyLinkedList:
支持多线程读写

# ThreadLocalMap:
支持在线程池中读写当前线程的本地数据

# CompletableFutureOne:
支持异步任务处理.当前所需的返回值可以在将来完成

# ThreadPool:
线程池

# ThreadScheduledOne:
按计划执行的任务.线程是唯一的

## Quick Start

```xml
<!--Adding dependencies to pom. XML-->
        <dependency>
            <groupId>io.github.thierrysquirrel</groupId>
            <artifactId>jellyfish</artifactId>
            <version>1.0.0.0-RELEASE</version>
        </dependency>
```

# ConcurrencyHashMap:

```java
public static void textMap(){
        System.out.println("API test");
        int offset=16;
        ConcurrencyHashMap<Integer,String> thisMap=new ConcurrencyHashMap<>(offset);
        int key=1;
        String value="one";
        thisMap.set(key,value);

        String get=thisMap.get(key);
        System.out.println("get::"+get);
    }
```

# ConcurrencyTreeMap:

```java
public static void textTreeMap(){
        System.out.println("API test");
        int offset=16;
        ConcurrencyTreeMap<Integer,String> thisMap=new ConcurrencyTreeMap<>(offset);
        int key=1;
        String value="one";
        thisMap.set(key,value);

        String get=thisMap.get(key);
        System.out.println("get::"+get);
    }
```

# ConcurrencyArrayDeque:

```java
public static void textDeque(){
        System.out.println("API test");
        int offset=16;
        ConcurrencyArrayDeque<String> thisDeque=new ConcurrencyArrayDeque<>(offset);
        String value="21";
        thisDeque.pushBack(value);

        JellyfishContainer<String> container = thisDeque.tryPopBack();
        System.out.println("isEmpty::"+container.isEmpty());
        System.out.println("value::"+container.getValue());
    }
```

# ConcurrencyLinkedList:

```java
public static void textDeque(){
        System.out.println("API test");
        int offset=16;
        ConcurrencyLinkedList<String> thisDeque=new ConcurrencyLinkedList<>(offset);
        String value="21";
        thisDeque.pushBack(value);

        JellyfishContainer<String> container = thisDeque.tryPopBack();
        System.out.println("isEmpty::"+container.isEmpty());
        System.out.println("value::"+container.getValue());
    }
```

# ThreadLocalMap:

```java
public static void textLocalMap(){
        System.out.println("API test");
        int offset=16;
        ThreadLocalMap<String> thisMap=new ThreadLocalMap<>(offset);
        String value="21";
        thisMap.set(value);

        JellyfishContainer<String> container = thisMap.get();
        System.out.println("isEmpty::"+container.isEmpty());
        System.out.println("value::"+container.getValue());

    }
```

# CompletableFutureOne:

```java
public static void textFuture(){
        System.out.println("API test");
        CompletableFutureOne<String> futureOne=new CompletableFutureOne<>();
        String value="21";
        futureOne.tryOneComplete(value);

        int time =100;
        int maxTryCount = 5;
        JellyfishContainer<String> container = futureOne.tryOneGet(time, maxTryCount);
        System.out.println("isEmpty::"+container.isEmpty());
        System.out.println("value::"+container.getValue());
    }
```

# ThreadPool:

```java
public class ThreadDemo implements Runnable {

    private int age;
    private String name;

    public ThreadDemo(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public void demoRun(int age, String name){
        try {
            System.out.println("age::"+age+" name::"+name);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public void run() {
        demoRun(this.age,this.name);
    }
}
```

```java
public static void testPool()throws Exception{
        System.out.println("API test");
        int offset=3;
        ThreadPool threadPool=new ThreadPool(offset);

        for (int i = 0; i < 21; i++) {
            ThreadDemo threadDemo=new ThreadDemo(i,"HelloWorld::"+i);
            threadPool.execute(threadDemo);
        }
        Thread.sleep(1000);
        threadPool.deleteAll();
        threadPool=null;
    }
```

# ThreadScheduledOne:

```java
public static void testPool()throws Exception{
        System.out.println("API test");
        ThreadScheduledOne threadScheduledOne=new ThreadScheduledOne();

        int time=500;
        threadScheduledOne.execute(()->{
            System.out.println("Hello World");
        },time);

        Thread.sleep(1000);
        threadScheduledOne.deleteAll();
        threadScheduledOne=null;
    }
```

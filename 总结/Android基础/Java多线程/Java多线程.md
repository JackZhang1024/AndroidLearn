## Java 多线程

### 一. 线程和线程状态
```java
/**
 * A thread state.  A thread can be in one of the following states:
 * 1. NEW:
 * A thread that has not yet started is in this state.
 * 
 * 2. RUNNABLE
 * A thread executing in the Java virtual machine is in this state.
 * 
 * 3. BLOCKED
 * A thread that is blocked waiting for a monitor lock is * in this state.
 * 
 * 4. WAITING
 * A thread that is waiting indefinitely for another thread to
 * perform a particular action is in this state.
 * 
 * 5. TIMED_WAITING
 * A thread that is waiting for another thread to perform an action
 * for up to a specified waiting time is in this state.
 *  
 * 6. TERMINATED
 * A thread that has exited is in this state.
 *   
 * A thread can be in only one state at a given point in time.
 * These states are virtual machine states which do not reflect
 * any operating system thread states.
 *
 * @since   1.5
 * @see #getState
 */ 
```
线程的状态有六种 1. NEW出来的时候 创建状态但是还没有启动 2. RUNNABLE状态其实包含了两种情况 Ready和Running 这两种细分状态，这两种状态统称为RUNNABLE状态。Ready状态就是线程在进入Running转态之后又会因为时间片切换，CPU把资源用到了别处，此时就进入了Ready状态，一旦该线程又获得了时间片，则会又进入Running转态。 3. WAITING状态，由于Sleep, Wait , 被其他线程Join之后，进入的等待状态，4.BLOCKED状态，线程由于处于等待获取锁的情况，此时处于BLOCKED状态，6.Terminated, 线程运行结束或者运行过程中发生异常，进行死亡。
### 二. 线程start方法和run方法的区别
两种方法的区别
- start：

     用start方法来启动线程，真正实现了多线程运行，这时无需等待run方法体代码执行完毕而直接继续执行下面的代码。通过调用Thread类的start()方法来启动一个线程，这时此线程处于就绪（可运行）状态，并没有运行，一旦得到cpu时间片，就开始执行run()方法，这里方法run()称为线程体，它包含了要执行的这个线程的内容，Run方法运行结束，此线程随即终止。
- run：

     run()方法只是类的一个普通方法而已，如果直接调用Run方法，程序中依然只有主线程这一个线程，其程序执行路径还是只有一条，还是要顺序执行，还是要等待run方法体执行完毕后才可继续执行下面的代码，这样就没有达到写线程的目的。总结：调用start方法方可启动线程，而run方法只是thread的一个普通方法调用，还是在主线程里执行。这两个方法应该都比较熟悉，把需要并行处理的代码放在run()方法中，start()方法启动线程将自动调用run()方法，这是由jvm的内存机制规定的。并且run()方法必须是public访问权限，返回值类型为void。
- 两种方式的比较 ：

     实际中往往采用实现Runable接口，一方面因为java只支持单继承，继承了Thread类就无法再继续继承其它类，而且Runable接口只有一个run方法；另一方面通过结果可以看出实现Runable接口才是真正的多线程。
### 三. 同步通信机制
可以理解成线程之间互相沟通的机制，如一个线程通知另外一个线程开始执行某个方法，或者通过一个共享变量的变化来控制各自线程任务的执行。

- 同步方法机制

     同步机制就是利用获取一个锁的享有权之后，进行一番操作，结束之后释放锁的所有权，其他线程获取该锁的享有权，对同一对象进行修改的机制。
```java
static class MyBook {
    private int bookCount;
    public synchronized void addBook() {
        bookCount += 1;
        System.out.println("addBook " + bookCount);
    }
    public synchronized void borrowBook() {
        bookCount -= 1;
        System.out.println("borrowBook " + bookCount);
    }
}

static class ThreadA extends Thread {
    private MyBook myBook;
    public ThreadA(MyBook book) {
        myBook = book;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                myBook.addBook();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

static class ThreadB extends Thread {
    private MyBook myBook;
    public ThreadB(MyBook book) {
        myBook = book;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                Thread.sleep(3000);
                myBook.borrowBook();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public static void main(String[] args) {
    MyBook book = new MyBook();
    ThreadA threadA = new ThreadA(book);
    threadA.start();
    ThreadB threadB = new ThreadB(book);
    threadB.start();
}
```
输出结果：
```java
addBook 1
addBook 2
addBook 3
borrowBook 2
addBook 3
addBook 4
addBook 5
borrowBook 4
addBook 5
addBook 6
addBook 7
borrowBook 6
addBook 7
addBook 8
addBook 9
borrowBook 8
addBook 9
addBook 10
addBook 11
borrowBook 10
addBook 11
addBook 12
addBook 13
borrowBook 12
addBook 13
addBook 14
addBook 15
borrowBook 14
addBook 15
addBook 16
...
```
上面的机制就是线程ThreadA添加书之后，线程ThreadB去借书，永远保证书的个数不会发生错误, 意思就是说添加书和借书
这两个操作永远不会同时进行。

### 四. wait()和notify()机制（显式通信方式）
```Java
private static class Bread {
    int count;
}

private static class Producer extends Thread {
    private Bread mBread;
    
    public Producer(String name, Bread bread) {
        super(name);
        this.mBread = bread;
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.getMessage();
            }
            synchronized (mBread) { // 消费者拿到Bread的锁之后，就进不到生产者2这块了
                if (mBread.count > 0) {
                    try {
                        // 生产者开始等待 
                        mBread.wait();
                        // 生产者结束等待
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {
                    mBread.count += 10;
                    System.out.println("生产了" + mBread.count + "个面包");
                    mBread.notify();
                }
            }
        }
    }
}

private static class Consumer extends Thread {
    private Bread mBread;

    public Consumer(String name, Bread bread) {
        super(name);
        this.mBread = bread;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (mBread) {
                if (mBread.count == 0) {
                    try {
                        //消费者等待开始
                        mBread.wait();
                        //消费者等待结束
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {
                    try {
                        while (mBread.count > 0) {
                            System.out.println("消费了第 " + (--mBread.count) + "个面包");
                            Thread.sleep(1000);
                        }
                        mBread.notify();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        }
    }
}

public static void main(String[] args) {
    // 注意 我们使用的锁必须为同一个对象
    Bread bread = new Bread();
    Producer producer = new Producer("生产者", bread);
    Consumer consumer = new Consumer("消费者", bread);
    producer.start();
    consumer.start();
}
```
输出结果：

```java
生产了10个面包
消费了第 9个面包
消费了第 8个面包
消费了第 7个面包
消费了第 6个面包
消费了第 5个面包
消费了第 4个面包
消费了第 3个面包
消费了第 2个面包
消费了第 1个面包
消费了第 0个面包
生产了10个面包
消费了第 9个面包
消费了第 8个面包
消费了第 7个面包
消费了第 6个面包
消费了第 5个面包
消费了第 4个面包
消费了第 3个面包
消费了第 2个面包
消费了第 1个面包
消费了第 0个面包

```


### 五. 管道机制
```java 
// 向输出管道缓冲区中输出内容 然后输入管道从缓存区中读取内容
static class WriteThread extends Thread {

    private PipedOutputStream pipedOutputStream;

    public WriteThread(PipedOutputStream pout) {
        this.pipedOutputStream = pout;
    }

    @Override
    public void run() {
        super.run();
        String str = "Hello, I'm from WriteThread";
        try {
            Thread.sleep(3000);
            pipedOutputStream.write(str.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pipedOutputStream != null) {
                    pipedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

static class ReadThread extends Thread {
    private PipedInputStream pipedInputStream;

    public ReadThread(PipedInputStream pInput) {
        this.pipedInputStream = pInput;
    }

    @Override
    public void run() {
        super.run();
        byte[] bytes = new byte[1024];
        int length = 0;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            while ((length = pipedInputStream.read(bytes)) != -1) {
                stringBuilder.append(new String(bytes, 0, length));
            }
            System.out.println("ReadThread: " + stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pipedInputStream != null) {
                    pipedInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public static void main(String[] args) {
    PipedOutputStream pipedOutputStream = null;
    PipedInputStream pipedInputStream = null;
    try {
        pipedOutputStream = new PipedOutputStream();
        pipedInputStream = new PipedInputStream();
        WriteThread writeThread = new WriteThread(pipedOutputStream);
        ReadThread readThread = new ReadThread(pipedInputStream);
        // 将输出管道和输入管道通过connect方法相连接
        pipedOutputStream.connect(pipedInputStream);
        readThread.start();
        writeThread.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```
输出结果：
```java
ReadThread: Hello, I'm from WriteThread
```

### 六. 线程池相关
首先说明下线程池的好处
- 重用线程   能够重用线程池中的线程，避免因为线程的创建和销毁所带来的性能开销。
- 控制并发数  有效的控制线程池的最大并发数，避免了大量的线程之间因为互相抢占资源而导致的阻塞现象
- 对线程进行管理 能够对线程进行有效的管理，并提供了定时执行和周期执行等功能。

常见的线程池是利用Executors的静态方法产生的线程池，Fixed, Cached, Single, Schedule四种线程池
### 七. ThreadPoolExecutor
```java
ThreadPoolExecutor构造方法

public ThreadPoolExecutor(int corePoolSize, int maxmiumPoolSIze, int keepAliveTIme, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory );
```
corePoolSize 是核心线程数 是永久存在于线程池中的，maxmiumPoolSize 是最大线程数，当活动线程的数目达到这个数值后，
后续的新任务将会被阻塞， keepAliveTime 就是非核心线程的闲置时间最大时长，如果闲置时间超过这个时长，则非核心线程就会被回收，
unit 设置了keepAliveTime的时间单位，可以是毫秒，也可以是秒，workQueue是任务队列，通过线程池的execute方法提交的Runnable
对象都会被提交存储到这个队列中，threadFactory 则是为线程池提供创建新的线程，ThreadFactory是一个接口，只有一个newThread(Runnable r)
方法。

### 八. ThreadPoolExecutor的执行规则
- 如果线程池中的线程数量小于核心线程数量，则会直接启动一个核心线程来执行任务。
- 如果线程池中的线程数量大于核心线程数量，那么任务就会被添加到任务队列中等待执行。
- 如果在第二部的情况下无法将任务插入到任务队列中，这是因为任务对列已经满了，这时候如果线程的数量未达到线程池规定的最大数量，则立刻启动一个非核心线程来执行任务。
- 如果步骤3的中线程数量已经达到线程池规定的最大数量，则会执行拒绝策略，拒绝执行次任务，ThreadPoolExecutor
会调用RejectionExeceptionHandler的rejectExecption方法来通知调用者。

```java
public static void main(String[] args) {
    int coreSize = 4;
    int maxThreadSize = 10;
    long keepAliveTime = 2;
    //  // 4 + 队列容量（>90） + 临时线程数量（6） >= 任务总数（100）
    //  1. 当任务队列容量>= (96 = 100-4)的时候 不会出现非核心线程处理任务
    //  2. 当任务队列容量>= (90 = 100-4-6)的时候 会出现非核心线程处理任务
    //  3. 当任务队列容量< (90 = 100-4-6)的时候 会出现拒绝执行问题
    //  当队列容量小于等于89的时候 就会立刻出现拒绝问题 同时可以看到有6个其他非核心线程出现并执行任务
    //  Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task thread.threadpool.  
    //  ThreadPoolLearn023$1@1d44bcfa rejected from java.util.concurrent.ThreadPoolExecutor@266474c2[Running, pool size = 10, active 
    //  threads = 10, queued tasks = 89, completed tasks = 0]
    //	at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2063)
    //	at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:830)
    //	at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1379)
    //	at thread.threadpool.ThreadPoolLearn023.main(ThreadPoolLearn023.java:44)
    int capacity = 90;
    LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>(capacity);
    ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize, maxThreadSize, keepAliveTime,
        TimeUnit.SECONDS, linkedBlockingQueue);
        for (int index = 0; index < 100; index++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("ThreadName "+Thread.currentThread().getName());
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
    }
}
```
当变量capacity = 90的时候

输出结果：
```java
ThreadName pool-1-thread-2
ThreadName pool-1-thread-1
ThreadName pool-1-thread-3
ThreadName pool-1-thread-4
ThreadName pool-1-thread-5
ThreadName pool-1-thread-6
ThreadName pool-1-thread-7
ThreadName pool-1-thread-8
ThreadName pool-1-thread-9
ThreadName pool-1-thread-10
```
当变量capacity = 89的时候

输出结果：
```java
ThreadName pool-1-thread-1
ThreadName pool-1-thread-2
ThreadName pool-1-thread-3
ThreadName pool-1-thread-4
ThreadName pool-1-thread-5
ThreadName pool-1-thread-6
ThreadName pool-1-thread-7
ThreadName pool-1-thread-8
ThreadName pool-1-thread-9
ThreadName pool-1-thread-10
Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task thread.threadpool.ThreadPoolLearn023$1@1d44bcfa rejected from java.util.concurrent.ThreadPoolExecutor@266474c2[Running, pool size = 10, active threads = 10, queued tasks = 89, completed tasks = 0]
	at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2063)
	at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:830)
	at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1379)
	at thread.threadpool.ThreadPoolLearn023.main(ThreadPoolLearn023.java:51)
ThreadName pool-1-thread-3
ThreadName pool-1-thread-2
```

当变量capacity = 96的时候
输出结果：

```java
ThreadName pool-1-thread-1
ThreadName pool-1-thread-2
ThreadName pool-1-thread-3
ThreadName pool-1-thread-4
ThreadName pool-1-thread-2
ThreadName pool-1-thread-1
ThreadName pool-1-thread-4
ThreadName pool-1-thread-3
```

总结分析：
从上面的结果来看，当任务队列中的容量足够大的时候（超过任务数的时候），不会出现拒绝问题；当任务队列的容量大于任务数目减去核心线程数的时候，不会出现非核心线程处理任务；当任务队列的容量大于任务数目减去最大线程数的时候，会出非核心线程处理任务的情况；当任务队列的容量小于
任务数目减去最大线程数的时候，会发生线程拒绝执行任务的情况。

### 九. 常见的四种线程池
线程池分为四类：FixedThreadPool, CachedThreadPool , SingleThreadExecutor, ScheduledThreadPool
- FixedThreadPool 是一种线程数量固定的线程池，当线程处于空闲的时候，并不会回收，当所有的线程都处于活动状态的时候，如果有新的任务到来，会将任务放到队列中进行等待，直到有空闲的线程释放出来，才继续执行新的任务，并且其任务队列的大小也没有限制。
- CachedThreadPool 是一种线程数量不固定的线程池，它只有非核心线程，并且最大线程数为Integer.MAX_VALUE,这样当线程池的线程都处于活动状态时，新任务到来时会由线程池创建新的线程来处理，否则就会利用空闲的线程进行处理。线程池中的空闲线程有超时机制，60秒，超过60秒的闲置线程就会被回收。从CachedThreadPool的性质来看，这个线程池适合用于来执行数量多但是执行时间段的任务，当整个线程池都处于闲置状态时，线程都会应为超时停止，这个时候CachedThreadPool
这个是时候是不耗任何系统资源的。
- SingleThreadExecutor 是只有一个核心线程的线程池，能确保所有的任务都在同一个线程中按顺序执行。
- ScheduledThreadPool  是核心线程数量固定的，非核心线程数量没有固定的，并且非核心线程闲置的时候会被立刻回收（闲置线程超时时间为0），用于执行定时任务或者周期任务。

```java
// 创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，
// 那么就会回收部分空闲（60秒不执行任务）的线程，当任务数增加时，此线程池又可以智能的添加新线程来处理任务。
// 此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程大小。
static class CountTask extends Thread {
    private int mIndex;

    public CountTask(int index) {
        this.mIndex = index;
    }

    @Override
    public void run() {
        try {
            System.out.println("线程名称 "+Thread.currentThread().getName()+" mIndex " + mIndex);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public static void main(String[] args) {
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int index = 0; index < 100; index++) {
        executorService.execute(new CountTask(index));
    }
    executorService.shutdown();
}
```
输出结果：
```java
线程名称 pool-1-thread-3 mIndex 2
线程名称 pool-1-thread-1 mIndex 0
线程名称 pool-1-thread-2 mIndex 1
线程名称 pool-1-thread-4 mIndex 3
线程名称 pool-1-thread-5 mIndex 4
线程名称 pool-1-thread-6 mIndex 5
...
```

```java
// 线程池最大使用的线程数量是固定的，当任务数超过固定的线程数时，则会重用已经执行完任务的线程
// 如果没有可重用的线程，则会加入队列进行等待执行
static class CountTask extends Thread {
    private int mIndex;

    public CountTask(int index) {
        this.mIndex = index;
    }

    @Override
    public void run() {
        try {
            System.out.println("线程名称 "+Thread.currentThread().getName() + " mIndex "+mIndex);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public static void main(String[] args) {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors);
    for (int index = 0; index < 100; index++) {
        executorService.execute(new LearnCachedThreadPool.CountTask(index));
    }
    executorService.shutdown();
}

```

输出结果：
```java
线程名称 pool-1-thread-1 mIndex 0
线程名称 pool-1-thread-2 mIndex 1
线程名称 pool-1-thread-3 mIndex 2
线程名称 pool-1-thread-4 mIndex 3
线程名称 pool-1-thread-1 mIndex 4
线程名称 pool-1-thread-2 mIndex 5
线程名称 pool-1-thread-3 mIndex 6
线程名称 pool-1-thread-4 mIndex 7
...
```

```java
// 线程池中只有一个线程，需要执行的任务按照指定的顺序执行
public static void main(String[] args) {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    for (int index = 0; index < 10; index++) {
        executorService.execute(new LearnSingleThreadPool.CountTask(index));
    }
    executorService.shutdown();
}

static class CountTask extends Thread {
    private int mIndex;

    public CountTask(int index) {
        this.mIndex = index;
    }

    @Override
    public void run() {
        try {
            System.out.println("线程名称 "+Thread.currentThread().getName()+" mIndex " + mIndex);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```

输出结果：
```java
线程名称 pool-1-thread-1 mIndex 0
线程名称 pool-1-thread-1 mIndex 1
线程名称 pool-1-thread-1 mIndex 2
线程名称 pool-1-thread-1 mIndex 3
线程名称 pool-1-thread-1 mIndex 4
...
```

```java
// 创建一个大小无限的线程池。此线程池支持定时以及周期性执行任务的需求。
 public static void main(String[] args) {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    executorService.schedule(()->{
            System.out.println("定时执行");
    }, 2000, TimeUnit.MILLISECONDS);
    executorService.scheduleAtFixedRate(()->{
            System.out.println("延迟2秒，间隔三秒执行："+System.currentTimeMillis());
    }, 2000, 3000, TimeUnit.MILLISECONDS);

}
```
输出结果：
```java
定时执行
延迟2秒，间隔三秒执行：1592815563327
延迟2秒，间隔三秒执行：1592815566330
延迟2秒，间隔三秒执行：1592815569325
```

### 十. synchronize和volatile的区别
volatile 的使用场景：当存在多个线程之间需要根据某个条件来执行任务时，volatile可以保证这个条件在各个线程是可见的，这样就不会引起
多线程之间条件不统一的问题。但是只能用于此，其他的不能保证。用于多线程感知共享变量的改变，但不能用于数据处理。

原子性的意思就是 一个操作要么都全部执行，要么都不执行。
```java
举例，变量 i++ 自增过程
1.  从主内存中获取i的变量值
2.  i变量加1
3.  将自增后的变量刷回主内存中
上面的三步都是分开的，不止一个完整的操作，可能线程1执行到步骤2的时候，线程2执行到步骤1 ，这样线程2取到的i变量就是原来的数据，没有发生改变，因为线程1还没有将自增后的变量刷回主内存中，线程2是从工作内存中获取的数据。
```
synchronzie和volatile之间区别：
- synchronized 可以保证原子性，可见性，有序性，而volatile只能保证可见性，不能保证原子性，有序性。
- synchronized 能用于修改方法，代码块，类型，而volatile只能用来修饰变量。
- volatile修饰的变量禁止指令重排。指令重排的意思就是说代码经过优化后，代码的执行顺序可能会发生改变。

线程A执行的操作
```java
volatile boolean isInitiated=false;
HashMap configuraions = new HashMap();
String configText = readConfigs(dir, fileName);
parseConfig(configuraions, configText);
isInitiated = true;
```
线程B执行的操作
```java
while(!isInitiated){
      sleep();   
}
readConfigs(configurations);
```
如果变量isInitiated没有用volatile 修饰，那么就可能由于指令重排造成 isInitiated=ture 这句代码在parseConfig()方法之前执行，这样线程B会立即停止循环，执行readConfigs()方法， 但是由于configurations没有被线程A初始化完成，则获取的数据有问题。

### 十一. Runnable和Callable的区别
Runnable没有返回值，Callable有返回值， Runnalbe可由线程池的execute方法执行，Callable由线程池的submmit方法执行，submit方法
执行之后，返回Future<T> 类型的对象，然后通过Future<T>类型的对象的get方法就可以获取到由Callable的call方法返回的数据，
这个过程是个阻塞过程，直到数据有返回，才会执行get方法后面的代码。
```java
static class PlusTask implements Callable<Integer> {
    private Integer mMaxValue;

    public PlusTask(Integer mMaxValue) {
        this.mMaxValue = mMaxValue;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int index = 0; index < mMaxValue; index++) {
            sum += index;
            Thread.sleep(200);
        }
        return sum;
    }
}
public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    Future<Integer> plusFuture = executorService.submit(new PlusTask(20));
    try {
        // get()方法会一直等待
        System.out.println("PlusResult " + plusFuture.get());
        // get(int time, TimeUnit unit) 方法会等待指定的时间 如果没有完成 则会抛出超时异常
        //System.out.println("PlusResult "  + plusFuture.get(6, TimeUnit.SECONDS));
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    executorService.shutdown();
    System.out.println("Done ");
}
```
输出结果：
```java
PlusResult 190
Done 
```

### 十二. 守护线程Dameon方法的使用
```java
private static class MyThread extends Thread {

     public MyThread(String name) {
         super(name);
     }

     @Override
     public void run() {
         super.run();
         try {
             for (int i = 0; i < 5; i++) {
                  System.out.println("MyThread current " + i);
                  Thread.sleep(1000);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}

private static class MyDaemonThread extends Thread {

    public MyDaemonThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        super.run();
        int i = 0;
        for (;;) {
            try {
                 System.out.println("DaemonThread " + (i++));
                 Thread.sleep(1000);
            } catch (InterruptedException e) {
                 e.printStackTrace();
            }
        }
    }
}

public static void main(String[] args) {
    MyThread myThread = new MyThread("线程一");
    MyDaemonThread myDaemonThread = new MyDaemonThread("守护线程");
    myDaemonThread.setDaemon(true); // 设置守护线程
    myThread.start();
    myDaemonThread.start();
}
```
输出结果：
```java
MyThread current 0
DaemonThread 0
MyThread current 1
DaemonThread 1
DaemonThread 2
MyThread current 2
DaemonThread 3
MyThread current 3
MyThread current 4
DaemonThread 4
DaemonThread 5
```
总结：Thread对象的setDaemon()方法，参数设置为true, 可以将线程设置为守护线程，这样就能够在进程中其他所有线程结束之后才会自行结束，否则只要有一个线程还在执行，那么守护线程就不能结束。

### 十三. Thread.yield()方法的使用
```java
private static class MyThread extends Thread {

     public MyThread(String name) {
         super(name);
     }

     @Override
     public void run() {
         super.run();
         try {
             for (int i =0; i <30; i++) {
                  System.out.println(Thread.currentThread().getName()+" current "+i);
                  if (i == 3) {
                     Thread.yield(); // 将CPU分给的线程一的时间让给线程二
                  }
                  Thread.sleep(1000);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}

private static class MyThread2 extends Thread {
    
    public MyThread2(String name) {
        super(name);
    }

    @Override
    public void run() {
        super.run();
        try {
             for (int i =0; i <30; i++) {
                 System.out.println(Thread.currentThread().getName()+" current "+i);
                 Thread.sleep(300);
             }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

private static MyThread myThread;
private static MyThread2 myThread2;

public static void main(String[] args) {
    myThread = new MyThread("线程一");
    myThread2 = new MyThread2("线程二");
    myThread.start();
    myThread2.start();
}    
```
输出结果：
```java
线程一 current 0
线程二 current 0
线程二 current 1
线程二 current 2
线程二 current 3
线程一 current 1
线程一 current 2
线程一 current 3
```
总结：通过打印结果可以看出，调用Thread.yield()方法，可以将CPU时间尽可能的让给其他线程使用。

### 十四. 线程join的方法使用
```java
private static class MyThread1 extends Thread {

    public MyThread1(String name) {
        super(name);
    }

    @Override
    public void run() {
        super.run();
        try {
            for (int i = 0; i < 10; i++) {
                 System.out.println("线程名称 " + Thread.currentThread().getName() + " " + i);
                 Thread.sleep(2000);
                 if (i == 5) { // 当线程一数到5的时候 线程二抢占属于CPU分给线程一的时间片 执行线程二
                        myThread2.join();
                 }
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

private static class MyThread2 extends Thread {

    public MyThread2(String name) {
            super(name);
    }

    @Override
    public void run() {
        super.run();
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("线程名称 " + Thread.currentThread().getName() + " " + i);
                Thread.sleep(2000);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
```
输出结果：
```java
线程名称 线程一 0
线程名称 线程二 0
线程名称 线程二 1
线程名称 线程一 1
线程名称 线程二 2
线程名称 线程一 2
线程名称 线程二 3
线程名称 线程一 3
线程名称 线程一 4
线程名称 线程二 4
线程名称 线程一 5
线程名称 线程二 5
线程名称 线程二 6
线程名称 线程二 7
线程名称 线程二 8
线程名称 线程二 9
线程名称 线程一 6
线程名称 线程一 7
线程名称 线程一 8
线程名称 线程一 9
```
总结：
在线程一计数到5的时候，线程二就开始独占CPU时间，执行线程二中的代码，只有当线程二结束之后，
线程一才得以获取到CPU时间，继续执行线程一的代码。join方法的是执行线程插入操作，直至线程运行结束。
### 十五. ThreadLocal的使用
```java
private static ThreadLocal<String> mNameThreadLoacl = new ThreadLocal<>();
private static ThreadLocal<String> mValueThreadLocal = new ThreadLocal<>();

private static void doSomething() {
    for (int index=0; index < 10; index++){
        final String name  = "我是线程"+index;
        final String value = "线程设置的值"+ index;
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    // 注意：这块的name本属于主线程中的，但是因为使用了threadLocal，就将name变量备份了一份到当前线程中
                    // 这样每个线程中都会一个name的变量备份，尽管他们的数据不一样，同理 value变量也是如此
                    // 后面的代码的意思就是说这个变量和线程是绑定了的，只要使用threadLocal的的get()方法就可以取出值来。
                    mNameThreadLoacl.set(name);
                    mValueThreadLocal.set(value);
                    callA();
                } catch (Exception e){
                         e.printStackTrace();
                } finally {
                    mNameThreadLoacl.remove();
                    mValueThreadLocal.remove();
                }
            }
        }.start();
    }
}

private static void callA(){
     callB();
}

private static void callB(){
     callC();
}

private static void callC(){
    System.out.println("当前线程 "+mNameThreadLoacl.get()+"  "+mValueThreadLocal.get());
}

public static void main(String[] args) {
    doSomething();
}
```
输出结果：
```java
当前线程 我是线程0  线程设置的值0
当前线程 我是线程1  线程设置的值1
当前线程 我是线程2  线程设置的值2
当前线程 我是线程3  线程设置的值3
当前线程 我是线程4  线程设置的值4
当前线程 我是线程5  线程设置的值5
当前线程 我是线程6  线程设置的值6
当前线程 我是线程7  线程设置的值7
当前线程 我是线程8  线程设置的值8
当前线程 我是线程9  线程设置的值9
```
总结：ThreadLocal 顾名思义就是线程本地变量，就是说将一个变量可以做成线程的本地变量，如果有多个线程，那么这个变量就在不同的线程就有不同的副本，就是在各个副本中进行修改操作也不会影响其他线程中的副本的内容，线程之间是独立的。通过调用ThreadLocal对象的set方法将变量与线程进行绑定，在该线程后面需要该变量的地方，可以通过ThreadLocal对象的get方法获取。

```java
static class Basket {

    private ArrayList<String> fruits = new ArrayList<>();

    public Basket() {

    }

    public void addFruit(String fruit) {
        fruits.add(fruit);
    }

    public void sell() {
        while (!fruits.isEmpty()) {
            String fruit = fruits.remove(0);
            System.out.println(Thread.currentThread().getName() + "卖出 " + fruit);
        }
    }
}

private static ThreadLocal<Basket> basketThreadLocal = new ThreadLocal<Basket>(){
    @Override
    protected Basket initialValue() {
            // 设置默认的返回数据
            Basket basket = new Basket();
            basket.addFruit("哈密瓜");
            return basket;
        }
};

private static void sellFruitsByThreadLocal() {
    BossThreadLocal boss3 = new BossThreadLocal("xiaoli");
    BossThreadLocal boss4 = new BossThreadLocal("xiaozhang");
    boss3.start();
    boss4.start();
    basketThreadLocal.get().sell();
}

static class BossThreadLocal extends Thread {
    private String name;
    private Basket basket;

    public BossThreadLocal(String name) {
        super(name);
        // 在这块进行初始化 依然是在主线程中
        // 所以不能在这块对ThreadLocal进行数据设置
        this.name = name;
        this.basket = new Basket();
    }

    @Override
    public void run() {
        super.run();
        if ("xiaoli".equalsIgnoreCase(name)){
            basket.addFruit("梨");
            // 设置绑定xiaoli线程的数据
            basketThreadLocal.set(basket);
        } else if ("xiaozhang".equalsIgnoreCase(name)){
            basket.addFruit("桃");
            basket.addFruit("橘子");
            // 设置绑定xiaozhang线程的数据
            basketThreadLocal.set(basket);
        }
        basketThreadLocal.get().sell();
    }
}

static Basket basket = new Basket();
public static void main(String[] args) {
    basket.addFruit("苹果");
    // 设置绑定主线程的数据
    basketThreadLocal.set(basket);
    sellFruitsByThreadLocal();
}

```
输出结果：
```java
xiaoli卖出 梨
xiaozhang卖出 桃
xiaozhang卖出 橘子
main卖出 苹果
```
如果我们注释掉xiaoli线程的basketThreadLocal.set(basket)这行代码 输出结果是
```java
xiaoli卖出 哈密瓜
main卖出 苹果
xiaozhang卖出 桃
xiaozhang卖出 橘子
```
总结：如果我们初始化过ThreadLocal, 则在没ThreadLocal对象没有设置value的时候，ThreadLocal对象get方法返回的是是初始化值。

### 十六. 线程死锁问题
线程死锁问题引入
```c
线程： 小张(书) 小红(画)
小张说: 你把画给我 我就把书给你
小红说: 你把书给我 我就把画给你
小张说: 你把画给我 我就把书给你
小红说: 你把书给我 我就把画给你
...
 
两人都不肯给对方东西，所以就僵持不下 所以形成死锁
 
抽象的来看
小张 小红都可以看做一个线程 在运行的过程中 都拥有对方所有想要的东西(锁) 但是都释东西(释放锁) 最后结果是两个线程都不能获取后续程序继续执行所需要的锁 最后都卡住了
```

```java
private static class XiaoZhang extends Thread {

    public XiaoZhang(String name) {
        super(name);
    }

    @Override
    public void run() {
        super.run();
        try {
            synchronized (book) {
                System.out.println("小张：你把画给我，我就给你书 ");
                Thread.sleep(1000);
                synchronized (painting) {
                    System.out.println("小张得到了画");
                }
                System.out.println("小张 .....");
            }
        } catch (Exception e) {
                e.getMessage();
        }
    }
}

private static class XiaoHong extends Thread {

    public XiaoHong(String name) {
        super(name);
    }

    @Override
    public void run() {
        super.run();
        try {
            // 如果要放弃死锁 则可以在小张拿到画之后再进行同步 意思就是说先让小张拿到画的锁 
            // 所以可以让小红先不要获取画的锁 可以让小红休息10秒 然后再去获取到锁
            // Thread.sleep(10*1000);
            synchronized (painting) {
                System.out.println("小红：你把书给我，我就给你画");
                Thread.sleep(1000 * 6);
                synchronized (book) {
                    System.out.println("小红得到了书");
                }
                System.out.println("小红 .....");
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}

private static final Object book = new Object();
private static final Object painting = new Object();

public static void main(String[] args) {
    XiaoZhang xiaoZhang = new XiaoZhang("小张");
    XiaoHong xiaoHong = new XiaoHong("小红");
    xiaoHong.start();
    xiaoZhang.start();    
}
```
输出结果:
```java
小红：你把书给我，我就给你画
小张：你把画给我，我就给你书 
```
看结果就知道死锁了，我们可以在小红获取到画的锁之前，先让小红休息10秒，让小张先获取到画的锁，可以顺利拿到画，然后依次释放画的锁，书的锁。
最后，小红可以顺利拿到画的锁，书的锁，这样就可以顺利拿到书。

输出结果：
```
小张：你把画给我，我就给你书 
小张得到了画
小张 .....
小红：你把书给我，我就给你画
小红得到了书
小红 .....
```

死锁的条件:
```java
1. 互斥条件：资源是独占的且排他使用，线程互斥使用资源，即任意时刻一个资源只能给一个线程使用，
其他线程若申请一个资源，而该资源被另一线程占有时，则申请者等待直到资源被占有者释放。
2. 不可剥夺条件：进程所获得的资源在未使用完毕之前，不被其他线程强行剥夺，而只能由获得该资源的线程释放。
3. 请求和保持条件：线程每次申请它所需要的一部分资源，在申请新的资源的同时，继续占用已分配到的资源。
4. 循环等待条件：在发生死锁时必然存在一个进程等待队列{P1,P2,…,Pn},其中P1等待P2占有的资源，P2等待P3占有的资源，…，Pn等待P1占有的资源，形成一个进程等待环路，环路中每一个线程所占有的资源同时被另一个申请，也就是前一个线程占有后一个线程所申请的资源。
以上给出了导致死锁的四个必要条件，只要系统发生死锁则以上四个条件至少有一个成立。事实上循环等待的成立蕴含了前三个条件的成立，
似乎没有必要列出，然而考虑这些条件对死锁的预防是有利的，因为可以通过破坏四个条件中的任何一个来预防死锁的发生。
```
解除死锁的方法
个人认为最好的办法就是破除循环等待，可以先让一个线程先执行完成，然后另外一个线程再开始执行，这样不用相互等着。因为不会发生等着锁被其他线程释放这种情况，可以随意获取到锁，然后执行相关代码。

### 十七. JUC包相关
- 原子整数类 
```java
public static void main(String[] args) throws Exception {
    // AtomicInteger(100) 设置了初始值100
    AtomicInteger atomicInteger = new AtomicInteger(100);
    CountDownLatch countDownLatch = new CountDownLatch(5);
    for (int index = 0; index < 5; index++) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1);
                    // getAndIncrement() 返回值是没有增加之前的值
                    //int value = atomicInteger.getAndIncrement();
                    // incrementAndGet() 返回值是增加之后的值
                    int value = atomicInteger.incrementAndGet();
                    // getAndDecrement()返回值是减少之前的数值
                    // int value = atomicInteger.getAndDecrement();
                    // decrementAndGet()返回值是减少之后的数值
                    //int value = atomicInteger.decrementAndGet();
                    System.out.println(Thread.currentThread().getName()+ " value "+value);
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    // 等待所有的线程结束之后（countDownLatch减到0为止）才能执行countDownLatch.awati()之后的代码
    countDownLatch.await();
    System.out.println("atomicInteger result is " + atomicInteger.get());
    System.out.println("主线程结束");
}
```
输出结果
```java
Thread-2 value 101
Thread-0 value 102
Thread-3 value 104
Thread-1 value 103
Thread-4 value 105
atomicInteger result is 105
主线程结束
```
没有是用原子整数类的示例代码
```java
static int count = 100;
public static void main(String[] args) throws Exception {
    CountDownLatch countDownLatch = new CountDownLatch(5);
    for (int index = 0; index < 5; index++) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1);
                    count++;
                    System.out.println(Thread.currentThread().getName()+ " value "+count);
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    countDownLatch.await();
    System.out.println("count result is "+count);
    System.out.println("主线程结束");
}
```
输出结果：
```java
Thread-1 value 103
Thread-4 value 105
Thread-0 value 101
Thread-3 value 104
Thread-2 value 103
count result is 105
主线程结束
```
从上面结果来看，在多线程中，不使用原子类的整数相加是会出现问题的，比如103这个数值就不应该出现两次。而使用原子类的整数相加，则没有问题。
但是根据源码来看，原子整数类并没有使用阻塞的方式来进行数据的同步，而是使用非阻塞的方式来进行，简称CAS（Compare And Set）机制。
- 原子布尔类
假设有两个人早期要吃饭，依次要进行起床，洗脸，刷牙，吃早餐这几个动作，要求这两个依次进行，而不是同时进行。
```java
private static class Person implements Runnable {
    private String name;

    public Person(String name){
        this.name = name;
    }

    @Override
    public void run() {
        try {
            if (atomicBoolean.compareAndSet(false, true)) {
                System.out.println(name+" 起床....");
                Thread.sleep(1000);
                System.out.println(name+" 洗脸....");
                Thread.sleep(1000);
                System.out.println(name+" 刷牙....");
                Thread.sleep(1000);
                System.out.println(name+" 吃早餐...");
                atomicBoolean.set(false);
            } else {
                System.out.println(name+ " 想起床却执行不了...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
输出结果：
```
李四 想起床却执行不了...
张三 起床....
张三 洗脸....
张三 刷牙....
张三 吃早餐...
```
```java
/**
* Atomically sets the value to the given updated value
* if the current value {@code ==} the expected value.
*
* @param expect the expected value
* @param update the new value
* @return {@code true} if successful. False return indicates that
* the actual value was not equal to the expected value.
*/
public final boolean compareAndSet(boolean expect, boolean update) {
    int e = expect ? 1 : 0;
    int u = update ? 1 : 0;
    return unsafe.compareAndSwapInt(this, valueOffset, e, u);
}
```
总结：comparetAndSet比较当前的值和expect的值，如果当前值和expect的值相同， 则将update的值进行设置，并返回true，如果不同，则不进行设置，并返回false。 
小节开头的问题对应的代码
```java
private static class Person implements Runnable {
private String name;
private boolean flag;

public Person(String name) {
    this.name = name;
    this.flag = true;
}

@Override
public void run() {
    try {
        // true 用于保持准备重新执行compareAndSet 让没有机会执行代码块的线程有机会重新进入代码块
        // flag 用于只执行一次代码块 
        while (true && flag) { 
            if (atomicBoolean.compareAndSet(false, true)) {
                System.out.println(name + " 起床....");
                Thread.sleep(1000);
                System.out.println(name + " 洗脸....");
                Thread.sleep(1000);
                System.out.println(name + " 刷牙....");
                Thread.sleep(1000);
                System.out.println(name + " 吃早餐...");
                atomicBoolean.set(false);
                flag = false;        
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}

static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
public static void main(String[] args) throws Exception {
    new Thread(new Person("张三")).start();
    new Thread(new Person("李四")).start();
}
```
输出结果：
```java
张三 起床....
张三 洗脸....
张三 刷牙....
张三 吃早餐...
李四 起床....
李四 洗脸....
李四 刷牙....
李四 吃早餐...
```
- 可重入锁 ReentrantLock 
我们可以利用可重入锁进入轮流打印输出AB

```java
class A extends Thread {
        
    @Override
    public void run() {
        super.run();
        while (true) {
            reentrantLock.lock();
            try {
                System.out.println("print A");
                Thread.sleep(1000);
                bCondition.signal();
                aCondition.await();
            } catch (Exception e) {
              e.printStackTrace();
            }
            reentrantLock.unlock();
        }
    }
}

class B extends Thread {

    @Override
    public void run() {
        super.run();
        while (true) {
            reentrantLock.lock();
            try {
                System.out.println("print B");
                Thread.sleep(1000);
                aCondition.signal();
                bCondition.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            reentrantLock.unlock();
        }
    }
}

private ReentrantLock reentrantLock = new ReentrantLock();
private Condition aCondition = reentrantLock.newCondition();
private Condition bCondition = reentrantLock.newCondition();

public static void main(String[] args) {
    MultiThreadLearn16 multiThreadLearn16 = new MultiThreadLearn16();
    A threadA = multiThreadLearn16.new A();
    B threadB = multiThreadLearn16.new B();
    threadA.start();
    threadB.start();
}
```
输出结果：
```java
print A
print B
print A
print B
print A
print B
print A
print B
print A
print B
print A
print B
...
```
总结分析：
可重入锁利用condition对象的await方法用于释放锁并进入等待状态，直到发生异常或者被唤醒，signal通知其他线程可以获取锁了。如此，其他线程就有机会获取可重入锁，并执行
相关代码。
- CountDownLatch 
CountDownLatch 我们有时会希望线程进行等待，直到一个或者多个其他任务执行完成，等待线程才会继续执行
CountDownLatch在初始创建时带有事件计数器，在释放锁存器之前，必须发生指定数量的事件。每发生一次事件，计数器减一。当计数器达到0时，打开锁存器。

关键词 锁存器 CountDownLatch countDown await
```java
static class WaitThread implements Runnable {
    
    private CountDownLatch mCountDownLatch;

    public WaitThread(CountDownLatch mCountDownLatch) {
        this.mCountDownLatch = mCountDownLatch;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            System.out.println("WaitThread starting....");
            mCountDownLatch.await();
            System.out.println("WaitThread Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

static class WorkThread implements Runnable {
    private CountDownLatch mCountDownLatch;

    public WorkThread(CountDownLatch mCountDownLatch) {
        this.mCountDownLatch = mCountDownLatch;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            for (int index = 0; index < 5; index++) {
                System.out.println("Index " + index);
                Thread.sleep(1000);
                mCountDownLatch.countDown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public static void main(String[] args) {
    CountDownLatch countDownLatch = new CountDownLatch(5);
    new Thread(new WaitThread(countDownLatch));
    new Thread(new WorkThread(countDownLatch));
}
```
输出结果：
```java
WaitThread starting....
Index 0
Index 1
Index 2
Index 3
Index 4
WaitThread Done
```
总结分析：
CountDownLatch适用于等待其他线程结束之后再继续执行的某种操作，比如多个线程下载某个文件，当所有的下载任务结束之后，通知用户下载完成，就可以用CountDownLatch
来处理。

- CyclicBarrier 

有如下场景：统计三个人赛跑时间 等三个人都到达终点的时候才进行统计
情景分析：
1. 最先到达的人先等待
2. 二个到达的人等待
3. 第三个人到达
4. 进行比赛成绩统计

```java
// 比赛选手
static class Racer implements Runnable {
    
    private String mName;
    private CyclicBarrier mCyclicBarrier;

    public Racer(String mName, CyclicBarrier mCyclicBarrier) {
        this.mName = mName;
        this.mCyclicBarrier = mCyclicBarrier;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            doSomeThing();
            mCyclicBarrier.await();
            System.out.println("Racer " + mName + " run over  ");
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doSomeThing() {
        try {
            Random random = new Random();
            int time = random.nextInt(5) + 1;
            Thread.sleep(time * 1000);
            System.out.println("Racer " + mName + " 跑了 " + time + " 秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// 统计数据的线程
static class StaticsThread implements Runnable {
    @Override
    public void run() {
        System.out.println("StaticsThread start working...");
    }
}

public static void main(String[] args) {
    CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new StaticsThread());
    new Racer("A", cyclicBarrier);
    new Racer("B", cyclicBarrier);
    new Racer("C", cyclicBarrier);

    // cyclicBarrier 可以重用
    // new Racer("X", cyclicBarrier);
    // new Racer("Y", cyclicBarrier);
    // new Racer("Z", cyclicBarrier);
}

```
输出结果：
```java
Racer A 跑了 2 秒
Racer C 跑了 3 秒
Racer B 跑了 4 秒
StaticsThread start working...
Racer B run over  
Racer A run over  
Racer C run over  
```
总结分析：
CyclicBarrier就是用于当最后一个线程都执行了cyclicBarrier对象的await()之后，开始执行一个任务的操作。就好比赛马，当最后一匹马跑到终点之后（碰到Barrier之后），裁判会对所有赛马的成绩进行统计。CyclicBarrier(int parties, Runnable barrierAction)构造方法的第一个参数是所有线程在barrier触发之前必须调用await()方法的次数，barrierAction是当barrier被触发之后所要执行的命令，可以为空。

- Exchanger
简化两个线程之间的数据交换，exchange()方法直到被两个线程调用之后 才会成功返回，因此，exchange()方法可以同步数据的交互。

```java
// 生产者
static class Producer implements Runnable {
    private Exchanger<String> mExchanger;
    private String[] fruits = new String[]{"苹果", "梨", "橘子", "草莓"};

    public Producer(Exchanger<String> mExchanger) {
        this.mExchanger = mExchanger;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            for (int index = 0; index < fruits.length; index++) {
                String fruit = fruits[index];
                System.out.println("producer put " + fruit);
                mExchanger.exchange(fruit);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// 消费者
static class Consumer implements Runnable {
    private Exchanger<String> mExchanger;

    public Consumer(Exchanger<String> mExchanger) {
        this.mExchanger = mExchanger;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            for (int index = 0; index < 4; index++) {
                String result = mExchanger.exchange(new String()); // 用空字符串来替换满字符串
                System.out.println("Consumer Got " + result);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public static void main(String[] args) {
    Exchanger<String> exchanger = new Exchanger<>();
    new Producer(exchanger);
    new Consumer(exchanger);
}
```
输出结果：
```java
producer put 苹果
Consumer Got 苹果
producer put 梨
producer put 橘子
Consumer Got 梨
Consumer Got 橘子
producer put 草莓
Consumer Got 草莓
```
总结分析：
Exchanger用户两个线程之间的数据交换。

- Phasper 
```java
static class PhaserThread implements Runnable {
    private Phaser mPhaser;
    private String mName;

    public PhaserThread(Phaser mPhaser, String mName) {
        this.mPhaser = mPhaser;
        this.mName = mName;
        mPhaser.register();
        new Thread(this).start();
    }

    @Override
    public void run() {
        // 阶段一
        System.out.println("Thread " + mName + " start phase01");
        mPhaser.arriveAndAwaitAdvance(); // 通知已经到达阶段一
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 阶段二
        System.out.println("Thread " + mName + " start phase02");
        mPhaser.arriveAndAwaitAdvance(); // 通知已经到达阶段二
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 阶段三
        System.out.println("Thread " + mName + " start phase03");
        mPhaser.arriveAndDeregister(); // 到达阶段三并解除注册
    }
}

public static void main(String[] args) {
    int phase = 0;
    Phaser phaser = new Phaser(1);
    new PhaserThread(phaser, "小张");
    new PhaserThread(phaser, "小李");
    new PhaserThread(phaser, "小红");

    phase = phaser.getPhase();
    phaser.arriveAndAwaitAdvance(); // 导致main线程挂起 等待阶段一所有的party结束才会执行下一步
    System.out.println("Phase " + phase + " completed ");

    phase = phaser.getPhase();
    phaser.arriveAndAwaitAdvance(); // 导致main线程挂起 等待阶段二所有的party结束才会执行下一步
    System.out.println("Phase " + phase + " completed ");

    phase = phaser.getPhase();
    phaser.arriveAndAwaitAdvance(); // 导致main线程挂起 等待阶段三所有的party结束才会执行下一步
    System.out.println("Phase " + phase + " completed ");

    // Deregister the main thread
    phaser.arriveAndDeregister();
    if (phaser.isTerminated()) {
        System.out.println("The phaser is completed");
    }
}
```
输出结果：
```java
Thread 小李 start phase01
Thread 小红 start phase01
Thread 小张 start phase01
Phase 0 completed 
Thread 小李 start phase02
Thread 小张 start phase02
Thread 小红 start phase02
Phase 1 completed 
Thread 小红 start phase03
Thread 小张 start phase03
Thread 小李 start phase03
Phase 2 completed 
The phaser is completed
```
总结分析：
phaser用于只有执行完成一个阶段之后 才会再执行下一个阶段，执行的过程中等待其他注册的party都完成之后 才会进入下一个阶段

- Semaphore 信号量
```java
// 增大数量线程
static class IncreaseThread implements Runnable {
     
    private Semaphore mSemaphore;
    private String name;

    public IncreaseThread(Semaphore mSemaphore, String name) {
        this.mSemaphore = mSemaphore;
        this.name = name;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread" + name + " isWaitingFor a permit");
            mSemaphore.acquire();
            System.out.println("Thread" + name + " 获得了 permit");
            for (int index = 0; index < 5; index++) {
                Shared.count++;
                System.out.println("Increase " + Shared.count);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Thread " + name + " release permit");
            mSemaphore.release();
        }
    }
}

// 减小数量线程
static class DecreaseThread implements Runnable {
    private Semaphore mSemaphore;
    private String name;

    public DecreaseThread(Semaphore mSemaphore, String name) {
        this.mSemaphore = mSemaphore;
        this.name = name;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread" + name + " isWaitingFor a permit");
            mSemaphore.acquire();
            System.out.println("Thread" + name + " 获得了 permit");
            for (int index = 0; index < 5; index++) {
                Shared.count--;
                System.out.println("Decrease " + Shared.count);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Thread " + name + " release permit");
            mSemaphore.release();
        }
    }
}

static class Shared {
    static int count = 0;
}

public static void main(String[] args) {
    Semaphore semaphore = new Semaphore(1);
    new IncreaseThread(semaphore, "A");
    new DecreaseThread(semaphore, "B");
}
```

输出结果：
```java
ThreadA isWaitingFor a permit
ThreadA 获得了 permit
Increase 1
ThreadB isWaitingFor a permit
Increase 2
Increase 3
Increase 4
Increase 5
Thread A release permit
ThreadB 获得了 permit
Decrease 4
Decrease 3
Decrease 2
Decrease 1
Decrease 0
Thread B release permit
```
总结分析：
利用信号量进行数据的加减不交叉。
- Semaphor 生产者和消费者
 使用两个信号量对生产者和消费者线程进行管理

```java
 从结果上我们可以看到get()和put()方法是同步的,put()方法执行在get()方法之前
 执行get()方法的时候 我们必须从ConsumeSemaphore获取许可证，不让无法继续执行
 在获取到ConsumeSemaphore许可证之后，我们执行消费操作，然后我们释放ProducerSemaphore许可证
 这样put()方法就可以继续执行执行put()方法 我们先要从ProducerSemaphore许可证 然后执行put()操作，执行完成后
 释放ConsumerSemaphore许可证 这样get()方法就可以继续执行,这样的"给予获取"机制确保了在每次put()之后，都会有get()方法紧跟着执行
 ConsumeSemaphore初始化未没有许可证，保证了put()方法先执行。信号量可以初始化同步状态是信号量更为强大的一方面。  
```

```java
static class Plate {
    private int num;
    private Semaphore mConsumerSemaphore = new Semaphore(0); // 参数为0表示没有获取到信号量的许可证
    private Semaphore mProducerSemaphore = new Semaphore(1);

    public void get() {
        try {
            System.out.println("Consumer is ready to acquire permits ");
            mConsumerSemaphore.acquire();
            System.out.println("Consumer acquire permits");
            Thread.sleep(1000);
            System.out.println("Plate consume Num " + num);
            mProducerSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void put(int num) {
        try {
            System.out.println("Producer is ready to acquire permits");
            mProducerSemaphore.acquire();
            System.out.println("Producer acquire permits");
            this.num = num;
            Thread.sleep(1000);
            System.out.println("Plate produce Num " + num);
            mConsumerSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// 消费者
static class Consumer implements Runnable {
    private Plate mPlate;

    public Consumer(Plate mPlate) {
        this.mPlate = mPlate;
        new Thread(this).start();
    }

    @Override
    public void run() {
        for (int index = 0; index < 4; index++) {
                mPlate.get();
        }
    }
}

// 生产者
static class Producer implements Runnable {
    private Plate mPlate;

    public Producer(Plate mPlate) {
        this.mPlate = mPlate;
        new Thread(this).start();
    }

    @Override
    public void run() {
        for (int index = 0; index < 4; index++) {
            mPlate.put(index);
        }
    }
}
public static void main(String[] args) {
    Plate plate = new Plate();
    new Thread(new Consumer(plate));
    new Thread(new Producer(plate));
}
```

输出
```java
Consumer is ready to acquire permits 
Producer is ready to acquire permits
Producer acquire permits
Plate produce Num 0
Consumer acquire permits
Producer is ready to acquire permits
Plate consume Num 0
Consumer is ready to acquire permits 
Producer acquire permits
Plate produce Num 1
Producer is ready to acquire permits
Consumer acquire permits
Plate consume Num 1
Consumer is ready to acquire permits 
Producer acquire permits
Plate produce Num 2
Producer is ready to acquire permits
Consumer acquire permits
Plate consume Num 2
Consumer is ready to acquire permits 
Producer acquire permits
Plate produce Num 3
Consumer acquire permits
Plate consume Num 3
```
总结分析：可以看到两个线程是交替进行的，从而实现生产者和消费者模型。

### 十八. 锁相关
- synchronzied 关键字 
synchronized 非静态同步方法，synchronized 静态同步方法，synchronized(this) 同步代码块，
synchronized(object) 同步代码块，synchronized(A.class) 同步代码块。大体分为两部分，一类是对象锁，一类是类锁。
对象锁一般又分为两种情况，一种是共享变量的对象本身，this对象，一种是其他对象。类锁一般就是共享对象的类的锁。
一般常见的问题就是说不同类型的锁在多线程的情况下对于方法或者代码块的调用问题，代码如下
```java
static class ShareElement {
    private static int totalCount;
    private final Object lock = new Object();

    public ShareElement(){
        totalCount = 0;
    }

    public synchronized void doIncrement() throws Exception{
        System.out.println(Thread.currentThread().getName()+ " 已经进入 非静态同步方法");
        Thread.sleep(2000);
        totalCount += 1;
        System.out.println(Thread.currentThread().getName()+ " 非静态同步方法..."+totalCount);
    }

    public static synchronized void doStaticIncrement() throws Exception{
        System.out.println(Thread.currentThread().getName()+" 已经进入静态同步方法");
        Thread.sleep(200);
        totalCount += 1;
        System.out.println(Thread.currentThread().getName()+" 静态同步方法..."+totalCount);
    }

    public void doIncrementOnThis() throws Exception{
        System.out.println(Thread.currentThread().getName()+" 准备进入 this同步代码块");
        synchronized (this){
            System.out.println(Thread.currentThread().getName()+ " 已经进入 this 同步代码块");
            Thread.sleep(1000);
            totalCount += 1;
            System.out.println(Thread.currentThread().getName()+" this同步代码块..."+totalCount);
        }
    }

    public void doIncrementOnLock() throws Exception{
        System.out.println(Thread.currentThread().getName()+ " 准备进入lock代码块");
        synchronized (lock){
            System.out.println(Thread.currentThread().getName()+ " 已经进入lock代码块");
            Thread.sleep(300);
            totalCount += 1;
            System.out.println(Thread.currentThread().getName()+" lock锁同步代码块..."+totalCount);
        }
    }

    public void doIncrementOnClassLock() throws Exception{
        System.out.println(Thread.currentThread().getName()+" 准备进入Class代码块");
        synchronized (ShareElement.class){
            System.out.println(Thread.currentThread().getName()+" 已经进入Class代码块");
            Thread.sleep(2000);
            totalCount += 1;
            System.out.println(Thread.currentThread().getName()+" 类锁同步代码块..."+totalCount);
        }
    }
}

public static void main(String[] args) throws Exception {
        ShareElement shareElement = new ShareElement();
        //非静态同步方法和this同步代码块用的是同一个锁，会有线程阻塞发生
        new Thread(()->{ 
            try { 
                shareElement.doIncrement(); 
            } catch (Exception e){ 
                e.printStackTrace(); 
            }
        },"线程一").start();
        new Thread(()->{ 
            try { 
                shareElement.doIncrementOnThis(); 
            } catch (Exception e){ 
                e.printStackTrace(); 
            }
        },"线程二").start();
}
```
输出结果：
```java
线程一 已经进入 非静态同步方法
线程二 准备进入 this同步代码块
线程一 非静态同步方法...1
线程二 已经进入 this 同步代码块
线程二 this同步代码块...2
```
总结分析：
在进入非静态同步方法之后，可以线程二也准备进入 this 同步代码块 但是执行到this同步代码块是走在了最后，其实线程二只需要1000毫秒就执行完了，但是因为线程二因为线程一提前获取到对象锁，只能等待线程一释放锁之后才能继续执行。 
```java
// 不同的锁 线程之间不存在等待锁的情况，该干什么干什么
new Thread(()->{ 
    try { 
        shareElement.doIncrement(); 
    } catch (Exception e) { 
        e.printStackTrace(); 
    }
},"线程一").start();
new Thread(()->{
     try { 
         shareElement.doIncrementOnLock(); 
     } catch (Exception e){ 
         e.printStackTrace(); 
    }
},"线程二").start();
```
输出结果：
```
线程一 已经进入 非静态同步方法
线程二 准备进入lock代码块
线程二 已经进入lock代码块
线程二 lock锁同步代码块...1
线程一 非静态同步方法...2
```
总结分析：
因为是不同的锁（线程一的锁是this对象锁，线程二的锁是lock对象锁），所以线程之间是没有阻塞的情况发生。
```java
// 同是类的锁 就会发生线程阻塞等待的问题
new Thread(()->{ 
    try { 
        shareElement.doIncrementOnClassLock(); 
    } catch (Exception e){ 
        e.printStackTrace(); 
    }
},"线程一").start();
new Thread(()->{ 
    try { 
        ShareElement.doStaticIncrement(); 
    } catch (Exception e){ 
            e.printStackTrace(); 
    }
},"线程二").start();
```
输出结果：
```java
线程一 准备进入Class代码块
线程一 已经进入Class代码块
线程一 类锁同步代码块...1
线程二 已经进入静态同步方法
线程二 静态同步方法...2
```
总结分析：
因为是相同的锁（一个是类锁，一个是静态代码方法上锁），所以会发生谁先获取到锁，谁先执行，其他的线程只有等待该线程释放锁之后
才有机会获取到锁，然后执行相关代码。

综上：只有不同的锁才不发生线程阻塞的情况，相同的锁会发生阻塞等待情况。
- 公平锁和非公平锁Lock 

Lock的一般语法：
```java
Lock lock = new ReentrantLock();
lock.lock();
try {
   doSomethings();
} catch(Exception e){
   e.printStackTrace();
} finally {
  lock.unlock();
}
```
锁lock分为“公平锁”和“非公平锁”：
1. 公平锁：表示线程获取锁的顺序是按照线程加锁的顺序来分配的(就是按照启动的顺序来的)，即先来先得FIFO先进先出的顺序。
2. 非公平锁：就是一种获取锁的抢占机制，是随机获得锁的。和公平锁不一样的就是先来的不一定先得到锁。这个方式可能造成某些线程一直拿不到锁，结果就是不公平的。

synchronized是Java中的关键字，使用synchronized能够防止多个线程同时并发访问程序的临界区资源。
synchronized进行同步有四种情况：
- 第一种：修饰一个代码块，被修饰的代码块称为同步语句块，其作用的范围是大括号{}括起来的代码，作用的对象是调用这个代码块的对象；
- 第二种：修饰一个方法：被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象；
- 第三种：修饰一个静态的方法：其作用的范围是整个静态方法，作用的对象是这个类的所有对象；
- 第四种：修饰一个类：其作用的范围是synchronized后面括号括起来的部分，作用主的对象是这个类的所有对象。
3. Lock与synchronized的对比
 1）Lock是一个接口，而synchronized是Java中的关键字，synchronized是内置的语言实现；
 2）synchronized在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；
 而Lock在发生异常时，如果没有主动通过unLock()去释放锁，则很可能造成死锁现象，因此使用Lock时需要在finally块中释放锁；
 3）Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断；
 4）通过Lock可以知道有没有成功获取锁，而synchronized却无法办到。
 5）Lock可以提高多个线程进行读操作的效率。
 在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），
 此时Lock的性能要远远优于synchronized。因此，在具体使用时要根据适当情况选择。

 ```java
// 公平锁
public static class FairReentrantLock implements Runnable {
    private static final ReentrantLock mReentrantLock = new ReentrantLock(true);
    private static int count = 0;

    @Override
    public void run() {
        System.out.println("FairReentrantLock " + Thread.currentThread().getName()+" start");
        try {
            mReentrantLock.lock();
            System.out.println("FairReentrantLock " + Thread.currentThread().getName()+" access lock");
            count++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mReentrantLock.unlock();
        }
        System.out.println("FairReentrantLock " + Thread.currentThread().getName()+" result "+count);
    }
}
private static void testFairReentrantLock(){
    for (int index =0; index< 10; index++){
        new Thread(new FairReentrantLock()).start();
    }
}

public static void main(String[] args) {
    testFairReentrantLock();
}

 ```
输出结果
```java
FairReentrantLock Thread-0 start
FairReentrantLock Thread-1 start
FairReentrantLock Thread-0 access lock
FairReentrantLock Thread-2 start
FairReentrantLock Thread-1 access lock
FairReentrantLock Thread-0 result 1
FairReentrantLock Thread-2 access lock
FairReentrantLock Thread-2 result 3
FairReentrantLock Thread-1 result 2
FairReentrantLock Thread-3 start
FairReentrantLock Thread-3 access lock
FairReentrantLock Thread-4 start
FairReentrantLock Thread-3 result 4
FairReentrantLock Thread-4 access lock
FairReentrantLock Thread-4 result 5
FairReentrantLock Thread-5 start
FairReentrantLock Thread-5 access lock
FairReentrantLock Thread-5 result 6
FairReentrantLock Thread-6 start
FairReentrantLock Thread-6 access lock
FairReentrantLock Thread-6 result 7
FairReentrantLock Thread-7 start
FairReentrantLock Thread-7 access lock
FairReentrantLock Thread-7 result 8
FairReentrantLock Thread-8 start
FairReentrantLock Thread-8 access lock
FairReentrantLock Thread-8 result 9
FairReentrantLock Thread-9 start
FairReentrantLock Thread-9 access lock
FairReentrantLock Thread-9 result 10
```
总结分析：
从多次试验的结果来看，先启动（先执行到run方法里面）的线程是先获取到锁，后启动的线程后获取到锁。

```java
非公平锁
public static class UnFairReentrantLock implements Runnable {
    private static final ReentrantLock mReentrantLock = new ReentrantLock(false);
    private static int count = 0;

    @Override
    public void run() {
        System.out.println("UnFairReentrantLock " + Thread.currentThread().getName()+" start");
        try {
            mReentrantLock.lock();
            System.out.println("UnFairReentrantLock " + Thread.currentThread().getName()+" access lock");
            count++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mReentrantLock.unlock();
        }
        System.out.println("UnFairReentrantLock " + Thread.currentThread().getName()+" result "+count);
    }
}

private static void testUnFairReentrantLock(){
    for (int index =0; index< 10; index++){
        new Thread(new UnFairReentrantLock()).start();
    }
}

public static void main(String[] args) {
    testUnFairReentrantLock();
}
```
输出结果：
```java
UnFairReentrantLock Thread-1 start
UnFairReentrantLock Thread-0 start
UnFairReentrantLock Thread-2 start
UnFairReentrantLock Thread-3 start
UnFairReentrantLock Thread-1 access lock
UnFairReentrantLock Thread-1 result 1
UnFairReentrantLock Thread-4 start
UnFairReentrantLock Thread-2 access lock
UnFairReentrantLock Thread-2 result 2
UnFairReentrantLock Thread-5 start
UnFairReentrantLock Thread-0 access lock
UnFairReentrantLock Thread-0 result 3
UnFairReentrantLock Thread-3 access lock
UnFairReentrantLock Thread-4 access lock
UnFairReentrantLock Thread-3 result 4
UnFairReentrantLock Thread-6 start
UnFairReentrantLock Thread-6 access lock
UnFairReentrantLock Thread-4 result 5
UnFairReentrantLock Thread-5 access lock
UnFairReentrantLock Thread-6 result 6
UnFairReentrantLock Thread-5 result 7
UnFairReentrantLock Thread-7 start
UnFairReentrantLock Thread-7 access lock
UnFairReentrantLock Thread-8 start
UnFairReentrantLock Thread-7 result 8
UnFairReentrantLock Thread-8 access lock
UnFairReentrantLock Thread-8 result 9
UnFairReentrantLock Thread-9 start
UnFairReentrantLock Thread-9 access lock
UnFairReentrantLock Thread-9 result 10
```
总结分析：
线程Thread-0比线程Thread-2更早执行，但是Thread-0获取锁的时间比Thread-2获取锁的时间晚。由此可以得到，非公平锁不能保证线程获取锁的顺序是按照线程的启动顺序来执行的。
- 其他锁


### 十九. 常见面试题






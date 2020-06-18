## Java 多线程
### 1. 线程和线程状态
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
### 2. 线程start方法和run方法的区别
两种方法的区别
- start：

     用start方法来启动线程，真正实现了多线程运行，这时无需等待run方法体代码执行完毕而直接继续执行下面的代码。通过调用Thread类的start()方法来启动一个线程，这时此线程处于就绪（可运行）状态，并没有运行，一旦得到cpu时间片，就开始执行run()方法，这里方法run()称为线程体，它包含了要执行的这个线程的内容，Run方法运行结束，此线程随即终止。
- run：

     run()方法只是类的一个普通方法而已，如果直接调用Run方法，程序中依然只有主线程这一个线程，其程序执行路径还是只有一条，还是要顺序执行，还是要等待run方法体执行完毕后才可继续执行下面的代码，这样就没有达到写线程的目的。总结：调用start方法方可启动线程，而run方法只是thread的一个普通方法调用，还是在主线程里执行。这两个方法应该都比较熟悉，把需要并行处理的代码放在run()方法中，start()方法启动线程将自动调用run()方法，这是由jvm的内存机制规定的。并且run()方法必须是public访问权限，返回值类型为void。
- 两种方式的比较 ：

     实际中往往采用实现Runable接口，一方面因为java只支持单继承，继承了Thread类就无法再继续继承其它类，而且Runable接口只有一个run方法；另一方面通过结果可以看出实现Runable接口才是真正的多线程。
### 3. 同步通信机制
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

### 4. wait()和notify()机制（显式通信方式）
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


### 5. 管道机制
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

### 6. 线程池相关
首先说明下线程池的好处
- 重用线程   能够重用线程池中的线程，避免因为线程的创建和销毁所带来的性能开销。
- 控制并发数  有效的控制线程池的最大并发数，避免了大量的线程之间因为互相抢占资源而导致的阻塞现象
- 对线程进行管理 能够对线程进行有效的管理，并提供了定时执行和周期执行等功能。

常见的线程池是利用Executors的静态方法产生的线程池，Fixed, Cached, Single, Schedule四种线程池
### 7. ThreadPoolExecutor
```java
ThreadPoolExecutor构造方法

public ThreadPoolExecutor(int corePoolSize, int maxmiumPoolSIze, int keepAliveTIme, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory );
```
corePoolSize 是核心线程数 是永久存在于线程池中的，maxmiumPoolSize 是最大线程数，当活动线程的数目达到这个数值后，
后续的新任务将会被阻塞， keepAliveTime 就是非核心线程的闲置时间最大时长，如果闲置时间超过这个时长，则非核心线程就会被回收，
unit 设置了keepAliveTime的时间单位，可以是毫秒，也可以是秒，workQueue是任务队列，通过线程池的execute方法提交的Runnable
对象都会被提交存储到这个队列中，threadFactory 则是为线程池提供创建新的线程，ThreadFactory是一个接口，只有一个newThread(Runnable r)
方法。

### 8. ThreadPoolExecutor的执行规则
- 如果线程池中的线程数量小于核心线程数量，则会直接启动一个核心线程来执行任务。
- 如果线程池中的线程数量大于核心线程数量，那么任务就会被添加到任务队列中等待执行。
- 如果在第二部的情况下无法将任务插入到任务队列中，这是因为任务对列已经满了，这时候如果线程的数量未达到线程池规定的最大数量，则立刻启动一个非核心线程来执行任务。
- 如果步骤3的中线程数量已经达到线程池规定的最大数量，则会执行拒绝策略，拒绝执行次任务，ThreadPoolExecutor
会调用RejectionExeceptionHandler的rejectExecption方法来通知调用者。

### 9. 常见的四种线程池
线程池分为四类：FixedThreadPool, CachedThreadPool , SingleThreadExecutor, ScheduledThreadPool
- FixedThreadPool 是一种线程数量固定的线程池，当线程处于空闲的时候，并不会回收，当所有的线程都处于活动状态的时候，如果有新的任务到来，会将任务放到队列中进行等待，直到有空闲的线程释放出来，才继续执行新的任务，并且其任务队列的大小也没有限制。
- CachedThreadPool 是一种线程数量不固定的线程池，它只有非核心线程，并且最大线程数为Integer.MAX_VALUE,这样当线程池的线程都处于活动状态时，新任务到来时会由线程池创建新的线程来处理，否则就会利用空闲的线程进行处理。线程池中的空闲线程有超时机制，60秒，超过60秒的闲置线程就会被回收。从CachedThreadPool的性质来看，这个线程池适合用于来执行数量多但是执行时间段的任务，当整个线程池都处于闲置状态时，线程都会应为超时停止，这个时候CachedThreadPool
这个是时候是不耗任何系统资源的。
- SingleThreadExecutor 是只有一个核心线程的线程池，能确保所有的任务都在同一个线程中按顺序执行。
- ScheduledThreadPool  是核心线程数量固定的，非核心线程数量没有固定的，并且非核心线程闲置的时候会被立刻回收（闲置线程超时时间为0），
用于执行定时任务或者周期任务。

### 10. synchronize和volatile的区别
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
```
volatile boolean isInitiated=false;
HashMap configuraions = new HashMap();
String configText = readConfigs(dir, fileName);
parseConfig(configuraions, configText);
isInitiated = true;
```
线程B执行的操作
```
while(!isInitiated){
      sleep();   
}
readConfigs(configurations);
```
如果变量isInitiated没有用volatile 修饰，那么就可能由于指令重排造成 isInitiated=ture 这句代码在parseConfig()方法之前执行，这样线程B会立即停止循环，执行readConfigs()方法， 但是由于configurations没有被线程A初始化完成，则获取的数据有问题。

### 11. Runnable和Callable的区别
Runnable没有返回值，Callable有返回值， Runnalbe可由线程池的execute方法执行，Callable由线程池的submmit方法执行，submit方法
执行之后，返回Future<T> 类型的对象，然后通过Future<T>类型的对象的get方法就可以获取到由Callable的call方法返回的数据，
这个过程是个阻塞过程，直到数据有返回，才会执行get方法后面的代码。



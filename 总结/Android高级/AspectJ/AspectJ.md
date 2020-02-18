# AspectJ 面向切面编程

### 问题一：什么是AspectJ，什么是面向切面编程



![dd](./images/xefZFzDq2uGN0pI80ZqsZ9HdGkv8aD2G.gif)

要了解什么是AspectJ , 必须先要知道什么是AOP，AOP又是什么👻？AOP就是面向切面编程的意思，但是知道AOP的字面意思显然不能清楚的了解这个是干什么的，这就需要我们先回顾下我们之前在Java编程中所熟悉的一个概念，OOP，面向对象编程，OOP编程的就是叫我们将一个事物分为好几个步骤，按照模块来进行开发，然后按照指定的顺序来执行这些模块，比如我们熟悉的把大象放进冰箱的例子，它就是涉及到两个对象，三个步骤。

涉及到的对象有：

- 大象 Elephant 
- 冰箱 Refrigerator

步骤分为：

1. 打开冰箱
2. 把大象放进冰箱
3. 关闭冰箱

好了，知道OOP是什么之后，我们来看什么是AOP（面向切面编程）。面向切面编程是一种思想，可以将某个功能与一批对象进行隔离，从而达到与这批对象进行解耦的目的。这里隔离的意思就是可有可无，可有的意思就是说标记了这个模块我就增加一个新的功能了，可无的意思就是说我不标记，那么我就没有这个功能。注意，这里又有一个新的关键字，`标记` ，好，我们来理解标记的是啥意思，其实前面两句已经解释的很清楚了，就是在项目代码中有一个标记用来标注出来这个方法是需要新增加一个功能还是不需要。到此，我们大概清楚AOP是干什么的，就是在不修改原有的代码基础上，我们可以自己加一个方法来做自己想要干的事情，比如，我们在点击控件的时候，加一个统计点击次数的功能，或者在点击的时候，判断是否有网络，如果有网络，就把数据存储到服务中，如果没有则就存储在本地磁盘中，再比如，我们可以在某些方法上加一个统计执行时间的功能，或者执行权限的功能，判断当前用户是否有执行该方法的权限。

嗯，我们清楚了AOP的概念之后，在解释下AspectJ 是干什么的，AspectJ 就是实现我们AOP这种思想的一种手段，其实，还有其他实现的手段，比如注解处理器。有人可能会问，呀，你说的怎么和动态代理的套路差不多啊，是的，一个是在编译的时候，就把我们想要的功能已经写到字节码中了，另外一个就是在代码运行的过程中，动态执行我们插入的方法，实现的方式不一样。



### 问题二：AspectJ 在项目中如何引入？

1. 如何在AndroidStudio项目中引入ApsectJ ?

   在我们项目根目录下build.gradle文件中，加入如下配置

   ```java
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
   ```

   ```java
   dependencies {
        // 配置AspectJ 第一步
        classpath 'org.aspectj:aspectjtools:1.8.13'
        classpath 'org.aspectj:aspectjweaver:1.8.13'   
   }
   ```

2. 如何在app目录下进行配置？

   在app目录下的build.gradle进行如下配置

   ```java
   dependencies {
       //AOP面向切面编程，加入这行就不用在libs下引入jar包了，不然要写成compile file(libs/aspectjrt.jar)
       implementation 'org.aspectj:aspectjrt:1.8.13'
   }
   ```

   ```java
   import org.aspectj.bridge.IMessage
   import org.aspectj.bridge.MessageHandler
   import org.aspectj.tools.ajc.Main
   
   final def log = project.logger
   final def variants = project.android.applicationVariants
   
   variants.all { variant ->
       if (!variant.buildType.isDebuggable()) {
           log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
           return
       }
   
       JavaCompile javaCompile = variant.javaCompile
       javaCompile.doLast {
           String[] args = ["-showWeaveInfo",
                            "-1.5",
                            "-inpath", javaCompile.destinationDir.toString(),
                            "-aspectpath", javaCompile.classpath.asPath,
                            "-d", javaCompile.destinationDir.toString(),
                            "-classpath", javaCompile.classpath.asPath,
                            "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
           log.debug "ajc args: " + Arrays.toString(args)
   
           MessageHandler handler = new MessageHandler(true)
           new Main().run(args, handler)
           for (IMessage message : handler.getMessages(null, true)) {
               switch (message.getKind()) {
                   case IMessage.ABORT:
                   case IMessage.ERROR:
                   case IMessage.FAIL:
                       log.error message.message, message.thrown
                       break
                   case IMessage.WARNING:
                       log.warn message.message, message.thrown
                       break
                   case IMessage.INFO:
                       log.info message.message, message.thrown
                       break
                   case IMessage.DEBUG:
                       log.debug message.message, message.thrown
                       break
               }
           }
       }
   }
   ```

   完成以上配置操作之后，就可以进行写代码实现AOP思想的环节

### 问题三：AspectJ 在项目中如何实战？

1. 首先我们先写我们的业务代码 即在未来我们将侵入的代码，最终实现给这些代码加些新功能

  ```java
   // 摇一摇
    @OnClick(R.id.btn_shake)
    public void onShakeClick(){
        SystemClock.sleep(2000);
        Log.d(TAG, "onShakeClick: 美女：今晚有空吗？好热啊");
    }

    // 发红包
    @OnClick(R.id.btn_redpackage)
    public void onSendRedPackage(){
        SystemClock.sleep(1500);
        Log.d(TAG, "onSendRedPackage: 美女：亲爱的，我爱你哟，么么哒");
    }

    // 发语音
    @OnClick(R.id.btn_audio)
    public void onSendAudio(){
        SystemClock.sleep(1000);
        Log.d(TAG, "onSendAudio: 美女：去哪儿见面，期待马上见到你");
    }
  ```

2. 业务代码完成之后，我们先写一个注解，该注解就是我们实现侵入的标记, 然后用注解标记业务方法

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BehaviorTrace {
    	String value();
    	int type();
}
```

```java
    // 摇一摇
    @BehaviorTrace(value = "摇一摇", type = 0)
    @OnClick(R.id.btn_shake)
    public void onShakeClick(){
        SystemClock.sleep(2000);
        Log.d(TAG, "onShakeClick: 美女：今晚有空吗？好热啊");
    }


    // 发红包
    @BehaviorTrace(value = "发送包", type = 1)
    @OnClick(R.id.btn_redpackage)
    public void onSendRedPackage(){
        SystemClock.sleep(1500);
        Log.d(TAG, "onSendRedPackage: 美女：亲爱的，我爱你哟，么么哒");
    }


    // 发语音
    @BehaviorTrace(value = "发语音", type = 2)
    @OnClick(R.id.btn_audio)
    public void onSendAudio(){
        SystemClock.sleep(1000);
        Log.d(TAG, "onSendAudio: 美女：去哪儿见面，期待马上见到你");
    }

```



3. 注解写完之后，我们需要实现我们的侵入代码 

```java
@Aspect
public class BehaviorAspectJ {

    private static final String TAG = "BehaviorAspectJ";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 注解 注意 后面是 * *（..） 格式务必是这样的 否则就有问题了
    // 切出那些蛋糕
    @Pointcut("execution(@com.lucky.androidlearn.aspectj.BehaviorTrace * *(..))")
    public void annoBehavior(){}

    // 怎么吃蛋糕
    @Around("annoBehavior()")
    public Object dealPoint(ProceedingJoinPoint point) throws Throwable{
        // 1. 获取到注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        BehaviorTrace behaviorTrace = method.getAnnotation(BehaviorTrace.class);
        // 2. 获取到注解的值
        String value = behaviorTrace.value();
        Log.d(TAG, "dealPoint: value "+value+" date "+simpleDateFormat.format(new Date()));
        long beginTime = System.currentTimeMillis();
        // 3. 调用被注解的方法
        Object object = point.proceed();
        // 4. 执行切面编程加入的方法
        Log.d(TAG, "dealPoint: costTime "+(System.currentTimeMillis() - beginTime));
        return object;
    }

```

4. 观察结果，是否实现我们的目标 在不修改业务代码的情况下，新增加了一个时间戳和方法时长的功能

```java
com.lucky.androidlearn D/BehaviorAspectJ: dealPoint: value 摇一摇 date 2020-02-18 00:17:33
com.lucky.androidlearn D/AspectJActivity: onShakeClick: 美女：今晚有空吗？好热啊
com.lucky.androidlearn D/BehaviorAspectJ: dealPoint: costTime 2001
```

```java
com.lucky.androidlearn D/BehaviorAspectJ: dealPoint: value 发送包 date 2020-02-18 00:18:42
com.lucky.androidlearn D/AspectJActivity: onSendRedPackage: 美女：亲爱的，我爱你哟，么么哒
com.lucky.androidlearn D/BehaviorAspectJ: dealPoint: costTime 1501
```

```java
com.lucky.androidlearn D/BehaviorAspectJ: dealPoint: value 发语音 date 2020-02-18 00:19:21
com.lucky.androidlearn D/AspectJActivity: onSendAudio: 美女：去哪儿见面，期待马上见到你
com.lucky.androidlearn D/BehaviorAspectJ: dealPoint: costTime 1002
```

<img src="./images/1488546236946.jpg" style="zoom:200%;" />








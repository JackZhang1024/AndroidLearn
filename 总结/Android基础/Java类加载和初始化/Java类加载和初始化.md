- [Java 类加载和初始化](#java-类加载和初始化)
  - [Java 栈溢出 StackOverflowError](#java-栈溢出-stackoverflowerror)
  - [Java 堆溢出](#java-堆溢出)
  - [类加载器](#类加载器)
## Java 类加载和初始化
### Java 栈溢出 StackOverflowError
```java
public static class StackOom {
    public int num = 1
    public void stack() {
        num++;
        this.stack();   
    }
}

try {
     StackOom stackOom = new StackOom();
     stackOom.stack();
} catch (Exception e) {
     e.printStackTrace();
}

```
输出结果：
```java
Exception in thread "main" java.lang.StackOverflowError
at jvm.JVM01$StackOom.stack(JVM01.java:54)
at jvm.JVM01$StackOom.stack(JVM01.java:54)
```
总结分析：StackOverflowError这个是由于Java虚拟机栈中方法调用栈太深造成超过Java虚拟机线程栈的限制。
如果多个线程同时调用这种调用栈比较深的方法，则会可能造成无法申请更多的内存而导致的OOM。另外，tryCatch并
不能捕获并处理这种异常，因为这个Error直接回导致进程结束。

### Java 堆溢出 

```java
public static class HeapOom {
    private List<byte[]> data = new ArrayList<>();

    public void heap() {
        while (true) {
            data.add(new byte[1024 * 1024]);
        }
    }
}

try {
    HeapOom heapOom = new HeapOom();
    heapOom.heap();
} catch (Exception e) {
    e.printStackTrace();
}
````
输出结果：
```java
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
at jvm.JVM01$HeapOom.heap(JVM01.java:63)
at jvm.JVM01.main(JVM01.java:22)
```
总结分析：由于该HeapOom对象调用的方法不停的添加字节数组，会导致虚拟机中的堆内存不停的增大，直到最后不能继续申请内存，这样就会导致内存溢出情况。

### 类加载器
```java
// 在 jvm/ClassLoaderTest.java文件中 
public static void main(String[] args) {
    ClassLoader myLoader = new ClassLoader() {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            try {
                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                InputStream is = getClass().getResourceAsStream(fileName);
                if (is == null) {
                    return super.loadClass(name);
                }
                byte[] b = new byte[is.available()];
                is.read(b);
                return defineClass(name, b, 0, b.length);
            } catch (IOException e) {
                throw new ClassNotFoundException(name);
            }
         }
    };
    try {
        Object object = myLoader.loadClass("jvm.ClassLoaderTest").newInstance();
        System.out.println(" " + object.getClass());
        System.out.println(object instanceof jvm.ClassLoaderTest);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```
输出结果：
```java
class jvm.ClassLoaderTest
false
```
结果分析：
从结果上来看，自定义的类加载器也是可以加载已经加载过的类，不过系统类加载器和自己定义的类加载器加载同一个类文件，但是不是同一个类。




## Java泛型

### 1. 泛型出现的意义

 Java 泛型一般常见于Java集合中，比如ArrayList、Map等，这些集合的存在可以对不同类型的对象进行统一的操作。正常情况下是可以用Object来接收任何对象，但是存在局限性，比如对某个类对象要进行一项具体的操作， 那么利用Object来接， 就存在问题，需要强制

类型转换。

### 2. 泛型擦除
泛型擦除的意思就是在编译阶段将原有的泛型参数类型擦除掉了，只保留一个最初始的状态，可以认为是泛型参数就是Object，但是只能处理一般的情况。

我们先观察下面一段代码，了解下什么是泛型擦除：

```java 
public static void main(String[] args) {
    ArrayList<Integer> integers = new ArrayList<>();
    ArrayList<String> strings = new ArrayList<>();
    Class iClass = integers.getClass();
    Class sClass = strings.getClass();
    System.out.println("iClass == sClass "+(iClass == sClass)); 
}
```

以上代码的执行结果如下：

```java 
iClass == sClass true
```

从iClass == sClass 的结果来看，两个类是相同的。我们可以查看下对应的字节码是怎么表现的

```java
public static void main(String[] args) {
    ArrayList var9 = new ArrayList();
    ArrayList var10 = new ArrayList();
    Class var11 = var9.getClass();
    Class var12 = var10.getClass();
    System.out.println("iClass == sClass " + (var11 == var12));
}
```

可以看到，在字节码中，`ArrayList var9 = new ArrayList();ArrayList var10 = new ArrayList();`  这两句已经没有类型相关的内容了，根据后面的输出结果来看，我们可以确定在编译之后，我们能知道的是这个就是一个集合列表而已，其他的不知道。

注意：在实践中，发现Intellij Idea 中生成的字节码是还是有相关的泛型参数信息，但是我们利用命令行编译生成字节码的时候，会发现是没有相关类型信息的。操作就是切换到src目录，执行命令`javac generic/GenericLearn09.java` 生成字节码，然后执行`java generic/GenericLearn09` 最后输出结果`iClass == sClass true`

我们再来看一个泛型擦除的例子

```java 
static class Dog {
    public void bark() {
        System.out.println("bark...");
    }
}

static class DogWatcher<T> {
    private T t;

    public DogWatcher(T t) {
        this.t = t;
    }

    public void doBark() {
        //t.bark(); /// 这里不能编译通过
    }
}

Dog dog = new Dog();
DogWatcher<Dog> dogWatcher = new DogWatcher<Dog>(dog);
dogWatcher.doBark();
```

观察DogWatcher，这个类并不能执行Dog对象的bark()方法，因为编译都过不去，就是因为泛型擦除。在编译后，并不能确定T类型的对象就是Dog类型的对象，也有可能是其他类型的对象。但是在运行期间是可以确定类型的，我们可以对代码进行如下修改：

```java 
static class DogWatcher<T> {
     private T t;
       
   	 public DogWatcher(T t) {
            this.t = t;
     }
   
     public void doBark() {
         //t.bark(); /// 这里不能编译通过
         if (t instanceof Dog) {
               Dog dog = (Dog)t;
               dog.bark();
         }
     }
}
```

这也是可以运行的，可以输出`bark...` 但是这么写肯定不合适，我们需要的是在编译的时候就可以通过，就可以顺利调用相关对象的代码。其实我们可以进行如下修改：

```java 
static class DogWatcher<T extends Dog> {
    private T t;

    public DogWatcher(T t) {
        this.t = t;
    }

    public void doBark() {
        t.bark(); /// 这里可以编译通过
    }
}
```

通过如上修改，我们就可以不用判断类型，就可以直接调用Dog对象的方法了，因为我们使用了`边界限定符`。`T extends Dog`表示的就是T是Dog的子类，所以可以正常调用Dog的方法。

### 3. 泛型使用场景

1. 泛型类

   ```java 
   // 泛型类
   static class DataWrapper<T> {
     private T data;
     
     public DataWrapper(T t) {
        this.data = t;
     }
   
     public void setData(T data) {
        this.data = data;
     }
   
     public T getData() {
        return data;
     }  
   }  
   ```

2. 泛型接口

   ```java 
   interface IAddInterface<T> {
       T add(T k, T v);
   }
   
   static class IntegerAdd implements IAddInterface<Integer> {
        @Override
        public Integer add(Integer k, Integer v) {
            return k + v;
        }
   }
   
   static class StringAdd implements IAddInterface<String> {
        @Override
        public String add(String k, String v) {
            return new StringBuilder(k).append(v).toString();
        }
   }
   ```

3. 泛型方法

   ```java 
   static class MethodWrapper {
       // 获取泛型类型的返回数据 泛型类型放在返回值的前面
       public <T> T getMethodData(T t) {
           return t;
       }
   
       // 设置泛型类型的参数
       public <T> void setMethodData(T t) {
   
       }
   }
   ```

### 4. Extends和Super边界限定符的使用

```java 
static class Fruit {
     public void printName() {
        System.out.println(getClass().getName());
     }
}

static class Apple extends Fruit {

}

static class AppleOne extends Apple {

}

static class Orange extends Fruit {

}

// 这样写没有问题 因为指定了泛型参数就是Fruit
ArrayList<Fruit> fruits = new ArrayList<>();
fruits.add(new Apple());
fruits.add(new Orange());

// extends的使用
// ? extends Fruit 的意思就是说元素的类型是Fruit的任何子类
// 但是add的时候 添加的元素是 ？extends Fruit 这个要当做一个整体来看
// 相当于任何一个Fruit的子类型 结果就是add添加时候 不知道具体要添加那种Fruit的子类 可能是子类Apple，也
// 有可能是子类Orange  所以不能添加，同时Object也不能添加
// null 可以添加是因为null 是可以是任何类型
List<? extends Fruit> fruits2 = new ArrayList<>();
//fruits2.add(new Apple());
//fruits2.add(new Orange());
//fruits2.add(new Fruit());
//fruits2.add(new Object());
fruits2.add(null);
// contains 和 indexOf 为什么能够使用 是因为参数类型是Object
fruits2.contains(new Apple());
fruits2.indexOf(new Apple());
// Apple apple =  fruits2.get(0);
Object object = fruits2.get(0);

// super 的使用
// ? extends Fruit 的意思就是说元素的类型是Fruit的本身或者是父类
// 下面添加的元素是Apple的父类，? super Apple 表示任何Apple的父类
// 但是添加的时候并不能确定添加那种Apple的父类 所以不能添加父类的元素
List<? super Apple> fruits3 = new ArrayList<>();
fruits3.add(new Apple());
fruits3.add(new AppleOne());
//fruits3.add(new Fruit());
//fruits3.add(new Orange());
//fruits3.add(new Object());
fruits3.add(null);
fruits3.contains(new Apple());
fruits3.indexOf(new Apple());
//Apple apple = fruits3.get(0);
Object apple = fruits3.get(0);

//extends和super的经典使用
//JDK中Collections工具类中的copy方法
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    int srcSize = src.size();
    if (srcSize > dest.size())
       throw new IndexOutOfBoundsException("Source does not fit in dest");
    if (srcSize < COPY_THRESHOLD || (src instanceof RandomAccess && dest instanceof RandomAccess)) {
            for (int i=0; i<srcSize; i++)
                dest.set(i, src.get(i));
    } else {
         ListIterator<? super T> di=dest.listIterator();
         ListIterator<? extends T> si=src.listIterator();
         for (int i=0; i<srcSize; i++) {
             di.next();
             di.set(si.next());
         }
      }
}

// 列表拷贝
List<String> srcList = new ArrayList<>();
srcList.add("beijing");
srcList.add("shanghai");
List<String> destList = Arrays.asList(new String[srcList.size()]);
Collections.copy(destList, srcList);
for (int i=0; i< destList.size(); i++){
    System.out.println("element "+destList.get(i));
}
```

总结：PECS （Producer Extends Consumer Super），如果从集合中只是读取某个对象 就可以使用通配符 `? extends` , 如果从集合中只是写入某个对象 就可以使用通配符 `? super ` 。如果既想读取又想写入数据，那就不要使用通配符, 指定到具体的类。可以将PECS原则称之为Get Extends Put Super Principle （GEPS）原则 ，意思就是说我们在获取（读取）元素的时候使用Extends 边界限定符 如果在添加（写入）某个元素的时候 使用Super 边界限定符。

### 5. 泛型擦除带来的问题

泛型不能用于显式地引用运行时类型的操作之中，例如 转型，instanceOf 和 new操作（包括new一个对象或者new一个数组）
因为所有关于参数的类型信息都在运行时丢失了，所以在运行时需要获取类型的信息的操作都无法进行工作
如下：

```java if (obj instance T)
if (obj instance T) // 不能通过编译
T t = new T() // 不能通过编译
T[] ts = new T[6] // 不能通过编译
```

解决擦除带来的问题
1. 解决instanceOf问题，使用instanceOf 会失败，因为类型信息已经被擦除，因此我们可以引入类型标签 `Class<T>`就可以动态的 isInstance().

```java
static class A {}

static class B extends A {}

static class TestInstance<T> {
    private Class<T> t;

    public TestInstance(Class<T> t) {
        this.t = t;
    }

    public boolean compare(Object object) {
        return t.isInstance(object);
    }
}
```

2. 解决创建对象的问题，因为不能使用new关键字来创建泛型T对象，可以使用工厂来解决

```java
interface Factory<T> {
    T create();
}

static class Product<T> {
    public <F extends Factory<T>> Product(F factory){
         factory.create();
    }
}

static class ProductFactory implements Factory<String>{
   @Override
   public String create() {
      String result = new String("Hello World");
      System.out.println("result is "+result);
      return result;
   }
}

Product product = new Product<String>(new ProductFactory());
```

3. 解决创建泛型数组

```java 
// 解决创建泛型数组
// 不能创建泛型数组的情况下，一般的解决方案是使用ArrayList 代替泛型数组，因为
// ArrayList 内部就是使用数组 因此使用ArrayList 能够获取数组的行为，和由泛型
// 提供的编译器的类型安全

// 但是假如，某种特定的场合，你仍然需要使用泛型数组，推荐的方式是使用 类型标签+Array.newInstance
// 来实现，并用注解@SuppressWarnings("unchecked")来抑制警告
static class SingleTon<T> {
    private Class<T> t;
    public SingleTon(Class<T> t) {
        this.t = t;
    }

    @SuppressWarnings("unchecked")
    T[] create(int size) {
        return (T[])Array.newInstance(t, 10);
    }
}
```

### 6. 常见的泛型知识总结

1. 常见的比较类型的泛型使用 

   ```java
    //第一种排序方式
    // <T extends Comparable<T>> 是泛型参数列表 泛型参数列表在返回值的前面
   public static <T extends Comparable<T>> List<T> mySortFunction(List<T> list) {
          Collections.sort(list);
         return list;
   }
   
   public static <T extends Comparable<? super T>> List<T> mySortFunction2(List<T> list) {
           Collections.sort(list);
         return list;
   }
   
   static class Animal implements Comparable<Animal> {
         public int age;
         public Animal(int age) {
               this.age = age;
         }
   
         @Override
         public int compareTo(Animal o) {
               return this.age - o.age;
         }
   }
   
   static class Dog extends Animal {
         public Dog(int age) {
               super(age);
         }
   }
   
   public static void main(String[] args) {
        Class c1 = new ArrayList<Integer>().getClass();
        Class c2 = new ArrayList<String>().getClass();
        System.out.println("c1==c2 "+(c1==c2));
        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal(20));
        animals.add(new Dog(14));
        List<Dog> dogs = new ArrayList<>();
        dogs.add(new Dog(10));
        dogs.add(new Dog(2));
        //mySortFunction接收的参数是Animal列表，元素可以是Animal的自身或者子类 
        animals = mySortFunction(animals);
        //mySortFunction(dogs)方法不能执行 类型不兼容
        //为什么mySortFunction不能接收Dog列表, 期待的参数列表是Animal列表，而提供的是Dog列表，
        mySortFunction2(dogs);
        mySortFunction2(animals);
        for (Animal animal : animals) {
             System.out.println(" age " + animal.age);
        }
   }
   ```

   输出结果

   ```java 
   c1==c2 true
   age 14
   age 20
   ```

2. 泛型的好处

   - 不用考虑类型转换的问题 有类型转换问题在编译阶段就会报错
   - 使用泛型可以满足算法的普遍性 也就是说一种算法可以在多种数据类型下使用

   ```java 
   public static void main(String[] args) {
       Integer[] nums = new Integer[]{1, 2, 3, 4, 6};
       System.out.println(isIn(1, nums));
   
       String[] values = new String[]{"1", "2", "3", "4"};
       System.out.println(isIn("2", values));
   }
   
   public static <T extends Comparable<T>, V extends T> boolean isIn(T t, V[] v){
       for (V value: v) {
           if(value.equals(t)) {
              return true;
           }
       }
       return false;
   }
   ```

3. 构造函数使用泛型

   ```java 
   static class GenCons {
      private double val;
      // 构造函数可以泛型化
      <T extends Number> GenCons(T t) {
          val = t.doubleValue();
      }
   
      public void showVal() {
          System.out.println("val " + val);
      }
   }
   
   public static void main(String[] args) {
       GenCons cons1 = new GenCons(100);
       cons1.showVal();
       GenCons cons2 = new GenCons(200.56);
       cons2.showVal();
   }
   ```

   输出结果

   ```java 
   val 100.0
   val 200.56
   ```

4. 泛型接口

   ```java 
   public class MyGenType<T> implements MinMax<T>{} 正确，
   public class MyGneType implements MinMax<T> 错误
   ```

    一般情况下，实现了泛型接口，这个类必须是泛型类，而泛型类必须带有将要传递给泛型接口的类型参数。 如上第二种情况，因为没有给MyGenType声明类型参数，所以无法给MinMax接口传递类型参数, 对于这种情况，标识符T是未知的，编译器会报错。`public class MyGenType implements MinMax<Integer>` 正确，如果某个类实现了具有具体类型的泛型接口 , 那么实现类就不需要进行泛型化。

   泛型接口具有的两个优势：

   - 可以针对不同的数据类型进行接口实现
   - 可以对实现接口的数据类型进行限制

   泛型接口通用语法：

   `interface interface-name<type-param-list>{}`， `type-param-list`是由逗号分隔的类型参数列表，当实现泛型接口时，必须指定类型参数，`class class-name<type-param-list> implements interface-name<type-ara-list>`  
   
   ```java
   //泛型接口是指的是在接口的命名处后面加上类型参数
   //Comparable是类型参数的上界 Comparable指定了将要进行比较的对象类型是实现Comparable接口
   public interface MinMax<T extends Comparable<T>> {
           T min();
      
           T max();
   }
   ```
   
   总结：泛型接口实现类的命名是从左向右看的，MyMinMax实现了MinMax接口 MyMinMax类传递给MinMax接口的类型参数T，而MinMax泛型接口参数T是实现了Comparable接口，所以泛型类MyMinMax的类型参数T必须实现Comparable接口，而一旦泛型类的类型参数确定之后 就不需要在implements子句中进行指定，一旦确定了类型参数 就可以不加修改的传递给接口
   
   ```java
   public static class MyMinMax<T extends Comparable<T>> implements MinMax<T> {
          private T[] values;
          public MyMinMax(T[] values) {
              this.values = values;
          }
      
          @Override
          public T min() {
              T minVal = values[0];
              for (int index = 1; index < values.length; index++) {
                  if (values[index].compareTo(minVal) < 0) {
                      minVal = values[index];
                  }
              }
              return minVal;
          }
      
          @Override
          public T max() {
              T maxVal = values[0];
              for (int index = 1; index < values.length; index++) {
                  if (values[index].compareTo(maxVal) > 0) {
                      maxVal = values[index];
                  }
              }
              return maxVal;
          }
   }  
   ```
   
   ```java
   public static class MyIntegerMinMax implements MinMax<Integer> {
          private Integer[] values ;
          public MyIntegerMinMax(Integer[] vals) {
              this.values = vals;
          }
      
          @Override
          public Integer min() {
              Integer minVal = values[0];
              for (int index = 1; index < values.length; index++) {
                  if (values[index].compareTo(minVal) < 0) {
                      minVal = values[index];
                  }
              }
              return minVal;
          }
      
          @Override
          public Integer max() {
              Integer maxVal = values[0];
              for (int index = 1; index < values.length; index++) {
                  if (values[index].compareTo(maxVal) > 0) {
                      maxVal = values[index];
                  }
              }
              return maxVal;
          }
   }  
   ```
   
   ```java
    public static void main(String[] args) {
          Integer[] values = new Integer[]{1, 2, 3, 4};
          MyMinMax<Integer> myMinMax1 = new MyMinMax<Integer>(values);
          System.out.println("MinVal " + myMinMax1.min());
          System.out.println("MaxVal " + myMinMax1.max());
          String[] values2 = new String[]{"zx", "yu", "dz", "ac"};
          MyMinMax<String> myMinMax2 = new MyMinMax<>(values2);
          System.out.println("MinValStr " + myMinMax2.min());
          System.out.println("MaxValStr " + myMinMax2.max());
      
          Integer[] values3= new Integer[]{1, 2, 3, 6, 3};
          MyIntegerMinMax myIntegerMinMax = new MyIntegerMinMax(values3);
          System.out.println("MinValInteger "+myIntegerMinMax.min());
          System.out.println("MaxValInteger "+myIntegerMinMax.max());
   }
   ```
   
   输出结果
   
   ```java
   MinVal 1
   MaxVal 4
   MinValStr ac
   MaxValStr zx
   MinValInteger 1
   MaxValInteger 6
   ```

5. 泛型层次 (泛型的层次结构)

   泛型类可以是类层次的一部分，就像非泛型类那样。因此，泛型类可以作为超类或子类。
   
   泛型和非泛型层次之间的关键区别是:  在泛型层次中，类层次中的所有子类都必须向上传递超类所有类型参数。这与必须沿着类层次向上传递构造参数类似
   
   ```java
    public static class GenOne<T> {
          T t;
          public GenOne(T t) {
               this.t = t;
          }
      
          public T getT() {
              return t;
          }
      
          public void setT(T t) {
              this.t = t;
          }
   }
      
   public static class GenTwo<T> extends GenOne<T> {
          public GenTwo(T t) {
              super(t);
          }
   }
      
   public static class GenThree<T, V> extends GenOne<T> {
          private V number;
      
          public GenThree(T t, V num) {
              super(t);
              this.number = num;
          }
      
          public V getNumber() {
              return number;
          }
      
          public void setNumber(V number) {
              this.number = number;
          }
   }
      
   // 普通类型作为泛型类的超类
   public static class NonGen {
           private int num;
      
           public NonGen(int num) {
               this.num = num;
           }
      
           public int getNum() {
               return num;
           }
      
           public void setNum(int num) {
               this.num = num;
           }
   }
      
   public static class GenFour<T> extends NonGen {
           private T t;
      
           public GenFour(int num, T t) {
               super(num);
               this.t = t;
           }
      
           public T getT() {
               return t;
           }
      
           public void setT(T t) {
               this.t = t;
           }
   }
      
   public static class GenFive extends GenFour<String>{
          public GenFive(int num, String s) {
              super(num, s);
          }
   }
      
   public static void main(String[] args) {
          GenTwo<Integer> genTwo = new GenTwo<>(12);
          System.out.println(genTwo.getT());
      
          GenThree<String, Integer> genTree = new GenThree<>("Hello World!", 20);
          System.out.println(genTree.getT());
          System.out.println(genTree.getNumber());
      
          GenFour<String> genFour = new GenFour<>(12, "Nice To meet You");
          System.out.println(genFour.getT());
          System.out.println(genFour.getNum());
   }
   ```
   
   输出结果：
   
   ```java
   12
   Hello World!
   20
   Nice To meet You
   12
   ```

6. 运行时是否能获取泛型类型信息

   ```java 
   public static class Gen<T> {
        private T t;
        public Gen(T t) {
               this.t = t;
        }
   
        public T getT() {
            return t;
        }
   
        public void setT(T t) {
            this.t = t;
        }
   }
   
   public static class Gen2<T> extends Gen<T> {
        public Gen2(T t) {
            super(t);
        }
   }
   
   public static void main(String[] args) {
        Gen<Integer> genInteger   = new Gen<>(12);
        Gen2<Integer> gen2Integer = new Gen2<>(12);
        Gen2<String> gen2String   = new Gen2<>("hello");
   
        if (gen2Integer instanceof Gen2<?>) {
            System.out.println("gen2Integer is instance of Gen2");
        }
        if (gen2Integer instanceof Gen<?>){
            System.out.println("gen2Integer is instance of Gen");
        }
        if (gen2String instanceof Gen<?>){
            System.out.println("gen2String is instance of Gen");
        }
        if (gen2String instanceof Gen2<?>){
            System.out.println("gen2String is instance of Gen2");
        }
        if (genInteger instanceof Gen<?>){
            System.out.println("genInteger is instance of Gen");
        }
        if (genInteger instanceof Gen2<?>){
            System.out.println("genInteger is instance of Gen2");
        }
        // 不合法的参数 因为在运行的时候并不清楚genInteger的泛型参数类型是什么
        // 所以不能进行类型转换
   		 //  if (genInteger instanceof Gen<Integer>) { }
        Gen<Integer> genIntegerOne = gen2Integer;
        System.out.println("GenIntegerOne  "+genIntegerOne.getT());
   }
   ```

7. 通配符的使用

   ```java 
   public static class Stats<T extends Number> {
        private T[] nums;
   
        public Stats(T[] nums) {
            this.nums = nums;
        }
   
        public double average() {
            double sum = 0;
            for (int index = 0; index < nums.length; index++) {
                   sum += nums[index].doubleValue();
            }
            return sum;
        }
   
        //使用通配符实现不同类型的泛型实例相互之间的比较
        public boolean isSame(Stats<?> stats) {
            if (average() == stats.average()) {
                return true;
            }
            return false;
        }
   
        public boolean isSame2(Stats<T> stats) {
            if (average() == stats.average()) {
                return true;
            }
            return false;
        }
   }
   
   public static void main(String[] args) {
       Integer[] integerArrays = new Integer[]{10, 12, 13, 14};
       Stats<Integer> integerStats = new Stats<>(integerArrays);
   
       Double[] doubleArrays = new Double[]{10.0, 11.0, 13.0, 16.0};
       Stats<Double> doubleStats = new Stats<>(doubleArrays);
       System.out.println("isSame " + integerStats.isSame(doubleStats));
       //如果不同的类型参数的泛型类实例之间是不可以比较的
       //但是如果使用的是通配符 则可以突破这种限制
       //System.out.println("isSame2" + integerStats.isSame2(doubleStats));
   }
   ```

   输出结果：

   ```java
   isSame true
   ```

8. 边界符

   ```java 
   static class TwoD {
        private int x;
        private int y;
   
        public TwoD(int x, int y) {
            this.x = x;
            this.y = y;
        }
   
        public int getX() {
            return x;
   		 }
   
        public void setX(int x) {
            this.x = x;
        }
   
        public int getY() {
            return y;
        }
   
        public void setY(int y) {
            this.y = y;
        }
   }
   
   static class ThreeD extends TwoD {
       private int z;
   
       public ThreeD(int x, int y, int z) {
           super(x, y);
           this.z = z;
       }
   
       public int getZ() {
           return z;
       }
   
       public void setZ(int z) {
           this.z = z;
       }
   }
   
   static class FourD extends ThreeD {
       private int t;
   
       public FourD(int x, int y, int z, int t) {
           super(x, y, z);
           this.t = t;
       }
   
       public int getT() {
               return t;
       }
   
       public void setT(int t) {
               this.t = t;
       }
   }
   
   static class Coordinates<T extends TwoD> {
       public T[] coordinates;
   
       public Coordinates(T[] coordinates) {
            this.coordinates = coordinates;
       }
   }
   ```

   ```java 
   static class BoundWildCard  {
       public static void showXY(Coordinates<?> c) {
            System.out.println("X Y coordinates ");
            for (int index = 0; index < c.coordinates.length; index++) {
                   int x = c.coordinates[index].getX();
                   int y = c.coordinates[index].getY();
                   System.out.println("x " + x + " y " + y);
            }
       }
   
       public static void showXYZ(Coordinates<ThreeD> c) {
            //public static void showXYZ(Coordinates<? extends ThreeD> c) {
            System.out.println("X Y Z coordinates ");
            for (int index = 0; index < c.coordinates.length; index++) {
                   int x = c.coordinates[index].getX();
                   int y = c.coordinates[index].getY();
                   int z = c.coordinates[index].getZ();
                   System.out.println("x " + x + " y " + y + " z " + z);
            }
       }
   
       public static void showXYZT(Coordinates<? extends FourD> c) {
            System.out.println("X Y Z T coordinates ");
            for (int index = 0; index < c.coordinates.length; index++) {
                   int x = c.coordinates[index].getX();
                   int y = c.coordinates[index].getY();
                   int z = c.coordinates[index].getZ();
                   int t = c.coordinates[index].getT();
                   System.out.println("x " + x + " y " + y + " z " + z + " t " + t);
            }
       }
   }
   ```

   ```java 
   public static void main(String[] args) {
      TwoD[] twoDS = new TwoD[]{
                   new TwoD(10, 20),
                   new TwoD(10, 21),
                   new TwoD(10, 22)
      };
   
      ThreeD[] threeDS = new ThreeD[]{
                   new ThreeD(10, 20, 2),
                   new ThreeD(10, 21, 2),
                   new ThreeD(10, 22, 2)
      };
   
      FourD[] fourDS = new FourD[]{
                   new FourD(10, 2, 2, 1),
                   new FourD(10, 2, 2, 2),
                   new FourD(10, 2, 2, 3)
      };
      BoundWildCard.showXY(new Coordinates<>(twoDS));
      BoundWildCard.showXY(new Coordinates<>(threeDS));
      BoundWildCard.showXY(new Coordinates<>(fourDS));
   
      //有了通配符上界 <? extends ThreeD>则TwoD不能传给ShowXYZ作为参数
      //BoundWildCard.showXYZ(new Coordinates<>(twoDS));
      BoundWildCard.showXYZ(new Coordinates<>(threeDS));
      BoundWildCard.showXYZ(new Coordinates<>(fourDS));
   
   	 // BoundWildCard.showXYZT(new Coordinates<>(threeDS));
      BoundWildCard.showXYZT(new Coordinates<>(fourDS));
   }
   ```

9. 根据泛型类型参数创建实例

   ```java 
   interface Presenter {
       void setView(View v);
   
       void fetchData();
   }
   
   interface View {
       void showLoading();
   
       void showSuccess();
   
       void showFail();
   
       void dismissLoading();
   }
   
   static class Activity<V extends View, P extends Presenter> {
       private V v;
       private P p;
   
       public void onCreate() {
           // 需要获取到V和P泛型类型变量的实例
           // 根据子类传递过来的泛型类型参数 来实例化对应的变量
           ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
           Type[] arguments = parameterizedType.getActualTypeArguments();
           if (arguments.length > 1) {
               Type argument1 = arguments[0];
               Class clazz1 = ((Class) argument1).asSubclass(View.class);
               System.out.println("clazz1 " + clazz1.getName());
               try {
                  v = (V) clazz1.newInstance();
               } catch (Exception e) {
                  e.printStackTrace();
               }
               Type argument2 = arguments[1];
               Class clazz2 = ((Class) argument2).asSubclass(Presenter.class);
               System.out.println("clazz2 " + clazz2.getName());
               try {
                   p = (P) clazz2.newInstance();
               } catch (Exception e) {
                   e.printStackTrace();
               }
                p.setView(v);
           }
           if (p != null) {
                p.fetchData();
           }
      }
   }
   
   static class MallPresenter implements Presenter {
        private View view;
   
        @Override
        public void fetchData() {
            view.showLoading();
            try {
                   Thread.sleep(2000);
                   System.out.println("Mall fetchData...");
                   view.showSuccess();
            } catch (Exception e){
                   view.showFail();
            } finally {
                   view.dismissLoading();
            }
       }
   
       @Override
       public void setView(View v) {
            view = v;
       }
   }
   
   static class MallView implements View {
        @Override
        public void showSuccess() {
            System.out.println("加载成功");
        }
   
        @Override
        public void showFail() {
            System.out.println("加载失败");
        }
   
        @Override
        public void showLoading() {
            System.out.println("正在加载....");
        }
   
        @Override
        public void dismissLoading() {
               System.out.println("结束加载....");
        }
   }
   
   static class MallActivity extends Activity<MallView, MallPresenter> {
        @Override
        public void onCreate() {
            super.onCreate();
        }
   }
   
   public static void main(String[] args) {
        MallActivity mallActivity = new MallActivity();
        mallActivity.onCreate();
   }
   ```

   输出结果：

   ```java 
   clazz1 generic.GenericLearn12$MallView
   clazz2 generic.GenericLearn12$MallPresenter
   正在加载....
   Mall fetchData...
   加载成功
   结束加载....
   ```

10. 运行时获取泛型信息

    ```java 
    List<String> list = new ArrayList<>();
    Map<Integer, String> map = new HashMap<>();
    System.out.println("list "+Arrays.toString(list.getClass().getTypeParameters()));
    System.out.println("map "+Arrays.toString(map.getClass().getTypeParameters()));
    ```

    输出结果：

    ```java 
    list [E]
    map [K, V]
    ```

    修改代码

    ```java
    ArrayList<String> listData = new ArrayList<String>(){};
    HashMap<Integer, String> mapData = new HashMap<Integer, String>(){};
    //Type type =listData.getClass().getGenericSuperclass();
    Type type =mapData.getClass().getGenericSuperclass();
    ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
    for (Type argument: parameterizedType.getActualTypeArguments()){
        System.out.println("argument "+argument.getTypeName());
    }
    ```

    输出结果：

    ```java 
    argument java.lang.Integer
    argument java.lang.String
    ```

    一般类运行时泛型参数获取

    ```java 
    static class Holder<T> {
         private  T t ;
    
         public void setT(T t) {
             this.t = t;
         }
    
         public T getT() {
             return t;
         }
    }
    
    Holder<String> holder = new Holder<String>(){};
    Type holderType = holder.getClass().getGenericSuperclass();
    ParameterizedType  holderParameterizedType = ParameterizedType.class.cast(holderType);
    for (Type argument: holderParameterizedType.getActualTypeArguments()){
        System.out.println("holder argument "+argument.getTypeName());
    }
    ```

    输出结果：

    ```java
    holder argument java.lang.String
    ```

    结论：
    在第一段代码中，我们可能期望能够获得真实的泛型参数，但是仅仅获得了声明时泛型参数占位符。getTypeParameters 方法的 Javadoc 也是这么解释的：仅返回声明时的泛型参数。所以，通过 getTypeParamters 方法无法获得运行时的泛型信息。运行时获取泛型类型信息，其中最关键的差别是本节的变量声明多了一对大括号。有一定 Java 基础的同学都能看出本节的变量声明其实是创建了一个匿名内部类。这个类是 HashMap 的子类，泛型参数限定为了 String 和 Integer。其实在“泛型擦除”一节，我们已经提到，Java 引入泛型擦除的原因是避免因为引入泛型而导致运行时创建不必要的类。那我们其实就可以通过定义类的方式，在类信息中保留泛型信息，从而在运行时获得这些泛型信息。简而言之，Java 的泛型擦除是有范围的，即类定义中的泛型是不会被擦除的。

11. 多个泛型边界

    Java泛型编程的边界可以是多个，使用如<T extends A & B & C>语法来声明，其中只能有一个是类，并且只能是extends后面的第一个为类，其他的均只能为接口(和类/接口中的extends意义不同)。

    ```java
    interface Swim {
       void swim();
    }
    
    interface Fly {
       void fly();
    }
    
    static class Animal {
        private String name;
    
        public Animal(String name){
            this.name = name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getName() {
            return name;
        }
    }
    
    static class LittleAnimal extends Animal implements Swim, Fly {
         public LittleAnimal(String name) {
             super(name);
         }
    
         @Override
         public void swim() {
             System.out.println("LittleAnimal swim");
         }
    
         @Override
         public void fly() {
             System.out.println("LittleAnimal fly");
         }    
    }
    
    // 泛型参数继承实现有多个，第一个必须是类
    static class Duck<T extends Animal & Swim & Fly> {
         private T t;
    
         public void setT(T t) {
             this.t = t;
         }
    
         public T getT() {
             return t;
         }
    
         // 对Swim接口实现类进行调用
         public void swim(){
             t.swim();
         }
         // 对Fly接口实现类进行调用
         public void fly(){
             t.fly();
         }   
    }
           
    Duck<LittleAnimal> animalDuck = new Duck<>();
    animalDuck.setT(new LittleAnimal("Little Duck"));
    animalDuck.swim();
    animalDuck.fly();
    ```

    输出结果：

    ```java 
    LittleAnimal swim
    LittleAnimal fly
    ```

    
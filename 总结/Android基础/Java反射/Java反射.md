- [Java反射](#java反射)
  - [一 Java反射的使用场景](#一-java反射的使用场景)
  - [二 Java的Class类相关](#二-java的class类相关)
  - [二 Java利用反射访问私有的构造方法](#二-java利用反射访问私有的构造方法)
  - [二 Java利用反射访问对象的私有属性](#二-java利用反射访问对象的私有属性)
  - [三 Java利用反射访问对象的私有方法](#三-java利用反射访问对象的私有方法)
  - [四 泛型相关的反射](#四-泛型相关的反射)
  - [五 数组相关的反射](#五-数组相关的反射)
  - [六 Java利用反射访问类的私有静态属性和方法](#六-java利用反射访问类的私有静态属性和方法)
## Java反射
### 一 Java反射的使用场景
Java反射场景是为了解决不能通过正常的方式访问一个类或者对象的属性和方法而出现的手段，同时也在一些场景下通过字符串等方式进行生成一个对象等操作。
### 二 Java的Class类相关
```java
public abstract class FatherObject implements Runnable{

    public void doSomeThing(){
        System.out.println("DoSomeThings ... ");
    }
}

public class ChildrenObject extends FatherObject {

    private int age = 20;
    private String name = "Rose";
    private int score = 90;
    public String gender = "male";

    public ChildrenObject() {
    }

    public ChildrenObject(int age, String name, int score) {
        this.age = age;
        this.name = name;
        this.score = score;
    }

    private ChildrenObject(int age, int score){
        this.age = age;
        this.score = score;
        this.name = "Sunny";
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public void run() {
        System.out.println(" run ...");
    }


    @Override
    public void doSomeThing() {
        super.doSomeThing();
    }

    private void sayHello(){
        System.out.println("sayHello");
    }
}
```
```java
try {
      FatherObject children = new ChildrenObject();
      Class childrenClass0 = children.getClass();
      Class childrenClass1 = ChildrenObject.class;
      Class childrenClass2 = Class.forName("reflection.ChildrenObject");
      System.out.println("childrenClass0 == childrenClass1 " + (childrenClass0 == childrenClass1));
      System.out.println("childrenClass0 == childrenClass2 " + (childrenClass0 == childrenClass2));
      System.out.println("childrenClass1 == childrenClass2 " + (childrenClass1 == childrenClass2));
      String className0 = childrenClass0.getName();
      String className1 = childrenClass1.getName();
      String className2 = childrenClass2.getName();
      // FullName
      System.out.println("className0 " + className0);
      System.out.println("className1 " + className1);
      System.out.println("className2 " + className2);
      // SimpleName
      System.out.println("Simple className0 " + childrenClass0.getSimpleName());
      System.out.println("Simple className1 " + childrenClass1.getSimpleName());
      System.out.println("Simple className2 " + childrenClass2.getSimpleName());
      // 获取包名称
      String packageName = childrenClass0.getPackage().getName();
      System.out.println("packageName " + packageName);
      // 获取父类名称
      Class superClass = childrenClass0.getSuperclass();
      System.out.println("superClass Name " + superClass.getName());
      // 判断父类是否是抽象类
      boolean isAbstract = Modifier.isAbstract(superClass.getModifiers());
      System.out.println("superClass is Abstract " + isAbstract);
      // 获取父类接口
      Class[] interfaces = superClass.getInterfaces();
      for (Class clz : interfaces) {
          System.out.println("InterfaceName " + clz.getName()); 
      }
} catch (Exception e) {
    e.printStackTrace();
}
```
输出结果：
```java
childrenClass0 == childrenClass1 true
childrenClass0 == childrenClass2 true
childrenClass1 == childrenClass2 true
className0 reflection.ChildrenObject
className1 reflection.ChildrenObject
className2 reflection.ChildrenObject
Simple className0 ChildrenObject
Simple className1 ChildrenObject
Simple className2 ChildrenObject
packageName reflection
superClass Name reflection.FatherObject
superClass is Abstract true
InterfaceName java.lang.Runnable
```
总结分析：
`class.getName()`获取的是类全名，`class.getSimpleName()`获取的是类名。

### 二 Java利用反射访问私有的构造方法
```java
ChildrenObject childrenObject = new ChildrenObject();
Class childrenClass = childrenObject.getClass();

// 获取Children所有公开的构造方法
Constructor[] constructors = childrenClass.getConstructors();
for (Constructor constructor : constructors) {
   System.out.println("所有公开构造方法 constructor " + constructor.toString());
}

// 获取Children的指定构造方法
try {
    Constructor constructor = childrenClass.getConstructor(int.class, java.lString.class, int.class);
    ChildrenObject children = (ChildrenObject) constructor.newInstance(12, "Jack", 99);
    System.out.println("Age " + children.getAge());
    System.out.println("Name " + children.getName());
    System.out.println("Score " + children.getScore());

    Constructor constructor1 = childrenClass.getConstructor();
    ChildrenObject children1 = (ChildrenObject) constructor1.newInstance();
    System.out.println("Age  1 " + children1.getAge());
    System.out.println("Name 1 " + children1.getName());
    System.out.println("Score1 " + children1.getScore());
} catch (Exception e) {
    e.printStackTrace();
}

// 获取所有私有的构造方法
Constructor[] constructors1 = childrenClass.getDeclaredConstructors();
for (Constructor constructor : constructors1) {
    System.out.println("所有构造方法 constructor " + constructor.toString());
}
try {
    Constructor constructor2 = childrenClass.getDeclaredConstructor(int.class, int.class);
    // 如果构造方法是私有的，则必须设置可以访问私有构造方法
    constructor2.setAccessible(true);
    ChildrenObject childrenObject2 = (ChildrenObject) constructor2.newInstance(10, 89);
    System.out.println("Age " + childrenObject2.getAge());
    System.out.println("Score " + childrenObject2.getScore());
} catch (Exception e) {
    e.printStackTrace();
}
```
输出结果：
```java
所有公开构造方法 constructor public reflection.ChildrenObject(int,java.lang.String,int)
所有公开构造方法 constructor public reflection.ChildrenObject()
Age 12
Name Jack
Score 99
Age  1 20
Name 1 Rose
Score1 90
所有构造方法 constructor private reflection.ChildrenObject(int,int)
所有构造方法 constructor public reflection.ChildrenObject(int,java.lang.String,int)
所有构造方法 constructor public reflection.ChildrenObject()
Age 10
Score 89
```
### 二 Java利用反射访问对象的私有属性
```java
ChildrenObject childrenObject = new ChildrenObject();
Class childrenClass = childrenObject.getClass();
// 获取ChildrenObject 的所有公开属性
Field[] fields = childrenClass.getFields();
for (Field field : fields) {
    System.out.println("Field " + field.toString());
}

// 获取ChildrenObject 的所有属性
Field[] fields1 = childrenClass.getDeclaredFields();
for (Field field : fields1) {
    System.out.println("所有属性 Field " + field.toString());
}
try {
    Constructor constructor = childrenClass.getConstructor();
    ChildrenObject childrenObject1 = (ChildrenObject) constructor.newInstance();
    //Field field = childrenClass.getField("age");
    Field field = childrenClass.getDeclaredField("age");
    field.setAccessible(true);
    field.set(childrenObject1, 10);
    System.out.println("ChildrenObject age " + childrenObject1.getAge());
} catch (Exception e) {
    e.printStackTrace();
}
```
输出结果：
```java
Field public java.lang.String reflection.ChildrenObject.gender
所有属性 Field private int reflection.ChildrenObject.age
所有属性 Field private java.lang.String reflection.ChildrenObject.name
所有属性 Field private int reflection.ChildrenObject.score
所有属性 Field public java.lang.String reflection.ChildrenObject.gender
ChildrenObject age 10
```

### 三 Java利用反射访问对象的私有方法

```java
ChildrenObject childrenObject = new ChildrenObject();
Class childrenClass = childrenObject.getClass();
try {
    // 获取所有的公开的方法
    Method[] methods = childrenClass.getMethods();
    for (Method method : methods) {
        System.out.println("Methods " + method.toString());
        // 获取方法的参数
        for (Class parameter : method.getParameterTypes()) {
            System.out.println("ParameterType " + parameter.getName());
        }
        System.out.println("-----------------------\n");
    }
    // 获取所有的方法 包括私有方法
    Method[] methods1 = childrenClass.getDeclaredMethods();
    for (Method method : methods1) {
        System.out.println("Methods 2 " + method.toString());
    }
    Constructor constructor = childrenClass.getConstructor();
    ChildrenObject childrenObject1 = (ChildrenObject) constructor.newInstance();
    // 获取方法 并进行调用
    Method ageMethod = childrenClass.getMethod("setAge", int.class);
    ageMethod.invoke(childrenObject1, 34);
    System.out.println("Age is  " + childrenObject1.getAge());
    // 获取私有方法 并进行调用
    Method method2 = childrenClass.getDeclaredMethod("setGender", String.class);
    method2.setAccessible(true);
    method2.invoke(childrenObject1, "Female");
    System.out.println("Gender is  " + childrenObject1.getGender());
} catch (Exception e) {
    e.printStackTrace();
}

```
输出结果：
```java
Methods public void reflection.ChildrenObject.run()
-----------------------

Methods public java.lang.String reflection.ChildrenObject.getName()
-----------------------

Methods public void reflection.ChildrenObject.setName(java.lang.String)
ParameterType java.lang.String
-----------------------

Methods public void reflection.ChildrenObject.setAge(int)
ParameterType int
-----------------------

Methods public void reflection.ChildrenObject.setGender(java.lang.String)
ParameterType java.lang.String
-----------------------

Methods public int reflection.ChildrenObject.getAge()
-----------------------

Methods public int reflection.ChildrenObject.getScore()
-----------------------

Methods public java.lang.String reflection.ChildrenObject.getGender()
-----------------------

Methods public void reflection.ChildrenObject.doSomeThing()
-----------------------

Methods public void reflection.ChildrenObject.setScore(int)
ParameterType int
-----------------------

Methods public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException
ParameterType long
ParameterType int
-----------------------

Methods public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException
ParameterType long
-----------------------

Methods public final void java.lang.Object.wait() throws java.lang.InterruptedException
-----------------------

Methods public boolean java.lang.Object.equals(java.lang.Object)
ParameterType java.lang.Object
-----------------------

Methods public java.lang.String java.lang.Object.toString()
-----------------------

Methods public native int java.lang.Object.hashCode()
-----------------------

Methods public final native java.lang.Class java.lang.Object.getClass()
-----------------------

Methods public final native void java.lang.Object.notify()
-----------------------

Methods public final native void java.lang.Object.notifyAll()
-----------------------

Methods 2 public void reflection.ChildrenObject.run()
Methods 2 public java.lang.String reflection.ChildrenObject.getName()
Methods 2 public void reflection.ChildrenObject.setName(java.lang.String)
Methods 2 public void reflection.ChildrenObject.setAge(int)
Methods 2 public void reflection.ChildrenObject.setGender(java.lang.String)
Methods 2 public int reflection.ChildrenObject.getAge()
Methods 2 public int reflection.ChildrenObject.getScore()
Methods 2 public java.lang.String reflection.ChildrenObject.getGender()
Methods 2 public void reflection.ChildrenObject.doSomeThing()
Methods 2 public void reflection.ChildrenObject.setScore(int)
Methods 2 private void reflection.ChildrenObject.sayHello()
Age is  34
Gender is  Female

```
### 四 泛型相关的反射
```java
/**
* 因为Java泛型是通过擦除来实现的，很难直接得到泛型具体的参数化类型的信息，
* 但是我们可以通过一种间接的形式利用反射得到泛型信息。比如下面这个类：
*/
public class GenericObject {

    public List<String> lists;

    public List<String> getLists() {
        return lists;
    }

    public void setLists(List<String> lists) {
        this.lists = lists;
    }
}


// 泛型
//1. 反射得到返回类型为泛型类的方法
//2. 调用getGenericReturnType得到方法返回类型中的参数化类型
//3. 判断该type对象能不能向下转型为ParameterizedType
//4. 转型成功，调用getActualTypeArguments得到参数化类型的数组，因为有的泛型类，不只只有一个参数化类型如Map<K,V>
//5. 取出数组中的每一个的值，转型为Class对象输出。

Class genericClass = GenericObject.class;
try {
    Method method = genericClass.getMethod("getLists");
    // 获取返回的泛型返回类型
    Type genericReturnType = method.getGenericReturnType();
    if (genericReturnType instanceof ParameterizedType){
        ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
        // 返回类型中的泛型参数类型
        Type[] types = parameterizedType.getActualTypeArguments();
        for (Type type: types){
            Class clz = (Class) type;
            System.out.println(" GenericType "+clz.getName());
        }
    }
} catch (Exception e){
    e.printStackTrace();
}

try {
    Method setMethod = genericClass.getMethod("setLists", List.class);
    Type[] genericParameterTypes = setMethod.getGenericParameterTypes();
    for (Type genericParameterType: genericParameterTypes){
        System.out.println("GenericParameterTypes为 ： " + genericParameterType.getTypeName());
        if(genericParameterType instanceof ParameterizedType){
            ParameterizedType parameterizedType = ((ParameterizedType) genericParameterType);
            System.out.println("ParameterizedType为 :" + parameterizedType.getTypeName());
            Type types[] = parameterizedType.getActualTypeArguments();
            for (Type type : types){
                System.out.println("参数化类型为 ： " + ((Class) type).getName());
                if (((Class) type).isAssignableFrom(String.class)){
                    System.out.println("泛型参数是 String类型的 ");
                }
            }
        }
    }
} catch (Exception e){
    e.printStackTrace();
}
// 获取成员变量的泛型类型
try {
    Field field = genericClass.getField("lists");
    Type type = field.getGenericType();
    if (type instanceof ParameterizedType){
        ParameterizedType parameterizedType = ((ParameterizedType) type);
        Type [] types = parameterizedType.getActualTypeArguments();
        for (Type type1 : types) {
            System.out.println("参数化类型 ： " + ((Class) type1).getTypeName());
        }
    }
} catch (Exception e){
    e.printStackTrace();
}

```
输出结果：

```java
GenericType java.lang.String
GenericParameterTypes为 ： java.util.List<java.lang.String>
ParameterizedType为 :java.util.List<java.lang.String>
参数化类型为 ： java.lang.String
泛型参数是 String类型的 
参数化类型 ： java.lang.String
```
### 五 数组相关的反射
 
```java
// 创建一个int类型的数组， 长度为3
int[] intArray = (int[])Array.newInstance(int.class, 3);

// 通过反射的形式，给数组赋值
for (int i =0; i< intArray.length; i++){
     Array.set(intArray, i, i+2);
}
// 通过反射的方式，得到数组中的值
for (int i =0; i< intArray.length; i++){
    System.out.println(" "+ Array.get(intArray, i));
}
// 获取数组Class对象
try {
    // [表示是数组,L的右边是类名，类型的右边是一个;
    Class clz = int[].class;
    Class clz1 = Class.forName("[I");
    Class clz2 = Class.forName("[Ljava.lang.String;");
    // 下面注释的clz3使用方式是错误的
    //Class clz3 = Class.forName("[Lreflection.ChildrenObject");
    System.out.println("class "+clz.getTypeName());
    System.out.println("class1 "+clz1.getTypeName());
    System.out.println("class2 "+clz2.getTypeName());
    //System.out.println("class3 "+clz3.getName());
    //ChildrenObject[] childrenObjects = (ChildrenObject[]) Array.newInstance(clz3, 2); // 利用clz3是生成不了数组的
    ChildrenObject[] childrenObjects = (ChildrenObject[]) Array.newInstance(ChildrenObject.class, 2);
    for (int index = 0; index < childrenObjects.length; index++) {
        ChildrenObject childrenObject = new ChildrenObject(index, "Jack" + index, index);
        Array.set(childrenObjects, index, childrenObject);
    }
    for (int index = 0; index < childrenObjects.length; index++) {
        ChildrenObject childrenObject = (ChildrenObject) Array.get(childrenObjects, index);
        System.out.println("age "+childrenObject.getAge()+" name "+childrenObject.getName()+" score "+childrenObject.getScore());
    }
}catch (Exception e){
    e.printStackTrace();
}
```
输出结果：
```java
 2
 3
 4
class int[]
class1 int[]
class2 java.lang.String[]
class3 [Lreflection.ChildrenObject; // 错误的，不用这个生成对应的ChildrenObject实例
age 0 name Jack0 score 0
age 1 name Jack1 score 1
```


### 六 Java利用反射访问类的私有静态属性和方法
```java
Class personClass = PersonObject.class;
try {
    Method method = personClass.getDeclaredMethod("setName", String.class);
    method.setAccessible(true);
    method.invoke(null, "lisi");
    Field field = personClass.getDeclaredField("sName");
    field.setAccessible(true);
    Object name = field.get(null);
    System.out.println("name "+name);
} catch (Exception e) {
    e.printStackTrace();
}
```
输出结果：
```java
name lisi
```
总结分析：
当访问类的静态属性或者方法的时候，我们对其进行设置或者调用的时候，需要将调用的对象设置为null。





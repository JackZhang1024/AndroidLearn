- [Java注解](#java注解)
  - [一 什么是注解](#一-什么是注解)
  - [二 注解的相关解释](#二-注解的相关解释)
  - [三 类注解](#三-类注解)
  - [三 字段注解](#三-字段注解)
  - [四 方法注解](#四-方法注解)
  - [五 方法参数注解](#五-方法参数注解)
  - [五 参数注解进行类型限制](#五-参数注解进行类型限制)
  - [五 注解处理器](#五-注解处理器)
  - [六 自动代码生成](#六-自动代码生成)
  - [七 注解处理器相关](#七-注解处理器相关)
## Java注解
### 一 什么是注解
注解就是对一段代码的解释说明，可以在这段代码上附加一些信息，利用这些信息我们可以做一些事情，简化我们的代码，比如代码自动生成功能就是注解处理器通过获取注解然后自动生成代码，达到简化和解耦的目的，我们Android开发中常用的就有Dagger2，ButterKnife，ARouter等都是这么搞的。注解可以用在类，字段，方法，方法参数上。
### 二 注解的相关解释
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Author{
      String name();//姓名
      String press();//出版社
      String publish();//发布时间
}
```
上面的这段代码就是一段注解代码，`@interface Author`表示这是一个名为Author的注解, `@Target(ElementType.TYPE)` 表示该注解可用于类上进行注解，可以给被注解的类添加一些信息。`@Retention(RetentionPolicy.RUNTIME)`
表示该注解被保存的时间，`RetentionPolicy.RUNTIME`表示该注解可以在运行时依然存在，可以使用。
我们可以看下相关的Java源码:
```java
/**
 * Annotation retention policy.  The constants of this enumerated type
 * describe the various policies for retaining annotations.  They are used
 * in conjunction with the {@link Retention} meta-annotation type to specify
 * how long annotations are to be retained.
 *
 * @author  Joshua Bloch
 * @since 1.5
 */
public enum RetentionPolicy {
    /**
     * Annotations are to be discarded by the compiler. // 该注解会被编译器丢弃
     * 
     */
    SOURCE,

    /**
     * Annotations are to be recorded in the class file by the compiler
     * but need not be retained by the VM at run time.  This is the default
     * behavior.// 注解会被保存记录到class文件中，但是在运行时，不能使用。
     * 我们Android中一般用在自动代码生成技术中这种保存策略。
     */
    CLASS,

    /**
     * Annotations are to be recorded in the class file by the compiler and
     * retained by the VM at run time, so they may be read reflectively.
     * // 注解会被编译器保存记录，并且在运行时也可以使用。
     * @see java.lang.reflect.AnnotatedElement
     */
    RUNTIME
}
```
```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Target {
    /**
     * Returns an array of the kinds of elements an annotation type
     * can be applied to.
     * @return an array of the kinds of elements an annotation type
     * can be applied to
     */
    ElementType[] value();
}
```
```java
// 注解使用的地方
public enum ElementType {
    /** Class, interface (including annotation type), or enum declaration */
    TYPE,

    /** Field declaration (includes enum constants) */
    FIELD,

    /** Method declaration */
    METHOD,

    /** Formal parameter declaration */
    PARAMETER,

    /** Constructor declaration */
    CONSTRUCTOR,

    /** Local variable declaration */
    LOCAL_VARIABLE,

    /** Annotation type declaration */
    ANNOTATION_TYPE,

    /** Package declaration */
    PACKAGE,

    /**
     * Type parameter declaration
     *
     * @since 1.8
     */
    TYPE_PARAMETER,

    /**
     * Use of a type
     *
     * @since 1.8
     */
    TYPE_USE
}
```

### 三 类注解
类注解可以帮助我们获取一些关于类的一些信息，尽管我们可能获取的真的与类无关，但这种方式表示可以获取标注在类上的一些信息的可能性。
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Author{
      String name();//姓名
      String press();//出版社
      String publish();//发布时间
}

@Author(name = "LiBai",press = "RenMingPress",publish = "20170613")
public static class Book{

}

```
如上代码，我们利用注解`@Author`对`Book`进行了注解，添加了注解信息`name="LiBai"`，`press="RenMingPress"`，`publish="20170613"`。那么，问题来了，我们要这个有啥用，这个暂时不解释，后面的小节会讲。但是，我们这块需要在运行时，获取到该注解上的这些信息，如何获取？代码如下：
```java
Author author = Book.class.getAnnotation(Author.class);
System.out.println(String.format("作者%s 出版社%s 出版日期%s", author.name(), author.press(),author.publish()));
```
输出结果：
```java
作者LiBai 出版社RenMingPress 出版日期20170613
```
如此，就可以获取到类被注解的信息。
### 三 字段注解
字段上被注解，这样就可以在某些情况下初始化该字段，并注入到该类中。这种操作在Dagge2，ButterKnife中就有该种实现，比如ButterKnife，我们在控件TexTView上添加一个注解 `@BindView(R.id.tv_title)`。
```java

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Preface {
    String descriptions();
}

@Author(name = "LiBai",press = "RenMingPress",publish = "20170613")
public static class Book {

    @Preface(descriptions = "老鼠爱大米")
    private String preface;

    private Book(){
    
    }
}

Field field = Book.class.getDeclaredField("preface");
Preface preface =field.getAnnotation(Preface.class);
System.out.println(preface.descriptions());
```
输出结果：
```java
老鼠爱大米
```
### 四 方法注解
方法注解和字段注解一样，也有比较多的使用场景。例如，ButterKnife中的注解`@OnClick()`:
```java
@OnClick(R.id.btn_ipc_share_file)
public void onIPCShareFileClick(){

}
```    
对此，我们很熟悉，就是利用该注解自动生成一段点击事件的方法的代码（具体是通过注解的值然后找到对应的控件，然后设置点击事件），然后就可以在需要的时候执行点击操作了，是不是感觉很完美，再也不需要写又臭又长的点击事件代码了。下面，我们展示一段在方法上进行注解的代码：
```java

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Language{
    String language();
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface BooKPage{
    int pages();
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Comment{
    String comment();
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Marks{
    int marks();
}

// @Repeatable注解表示该注解可以多次使用，使用的地方是括号里面的注解
@Repeatable(Markets.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Market{
    String market();
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Markets{
    Market[] value();
}


@Author(name = "LiBai",press = "RenMingPress",publish = "20170613")
public static class Book{

    @Preface(descriptions = "老鼠爱大米")
    private String preface;

    private Book(){

    }

    @Language(language = "日语")
    public String getLanguage(){
        return "English";
    }

    @BooKPage(pages = 240)
    public int getPages(){
        return 200;
    }

    @Comment(comment = "很好很好啊！！！")
    public String getComment(String comment){
        return comment;
    }

    @Marks(marks = 39)
    public int getFavoriteMarks(int marks){
        return marks;
    }

    @Markets({@Market(market = "京东"), @Market(market = "天猫"), @Market(market = "苏宁")})
    public String[] getMarkets(){
        return new String[]{"新华书店" , "京东" , "天猫", "当当"};
    }
}

// 找到对应的方法
Method languageMethod = Book.class.getDeclaredMethod("getLanguage");
Method pageMethod = Book.class.getDeclaredMethod("getPages");
Method commentMethod = Book.class.getDeclaredMethod("getComment",String.class);
Method favoriteMarksMethod = Book.class.getDeclaredMethod("getFavoriteMarks",int.class);
Method marketsMethod = Book.class.getDeclaredMethod("getMarkets");

// 找到方法上的注解
Language language = languageMethod.getAnnotation(Language.class);
BooKPage booKPage = pageMethod.getAnnotation(BooKPage.class);
Comment comment = commentMethod.getAnnotation(Comment.class);
Marks marks = favoriteMarksMethod.getAnnotation(Marks.class);
Markets markets = marketsMethod.getAnnotation(Markets.class);

// 找到注解上的数值
System.out.println(String.format("语言 %s", language.language()));
System.out.println(String.format("页数 %s", booKPage.pages()));
System.out.println(String.format("评价 %s", comment.comment()));
System.out.println(String.format("喜欢的总数 %s", marks.marks()));

for (Market market:markets.value()) {
    System.out.println(String.format("渠道 %s", market.market()));
}
```
输出：
```java
语言 日语
页数 240
评价 很好很好啊！！！
喜欢的总数 39
渠道 京东
渠道 天猫
渠道 苏宁
```
### 五 方法参数注解
方法参数注解也是很常见的，比如在Retroif中我们就经常看到如下的写法：
```java
@GET("http://www.xxx.com/api/rest/products")
Call<ResponseBody> getProductDetail(@Query("id") int id);
```
在Retrofit的代码中`ServiceMethod.java`可以看到是如下处理的
```java
...
Query query = (Query) annotation;
String name = query.value();
boolean encoded = query.encoded();

Class<?> rawParameterType = Utils.getRawType(type);
gotQuery = true;
if (Iterable.class.isAssignableFrom(rawParameterType)) {
    if (!(type instanceof ParameterizedType)) {
        throw parameterError(p, rawParameterType.getSimpleName()
                + " must include generic type (e.g., "
                + rawParameterType.getSimpleName()
                + "<String>)");
    }
ParameterizedType parameterizedType = (ParameterizedType) type;
Type iterableType = Utils.getParameterUpperBound(0, parameterizedType);
Converter<?, String> converter =
retrofit.stringConverter(iterableType, annotations);
return new ParameterHandler.Query<>(name, converter, encoded).iterable();  
...   
```
```java

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@interface Query {
    String value() default "Hello";
}

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@interface Locate {
    String value() default "Hello";
}

public class ApiService {

    public void fetchIPAddress(@Query("address") String name, 
        @Locate("locate") @Query("city") String city) {
        //doSomething()
    }

}

try {
    Method method = ApiService.class.getDeclaredMethod("fetchIPAddress", String.class, String.class);
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    // Annotation[0] 表示的是第一个参数所拥有的所有注解
    // Annotation[1] 表示的是第二个参数所拥有的所有注解
    // ...
    int parameterCount = parameterAnnotations.length;
    System.out.println("parameterCount " + parameterCount);
    for (int index = 0; index < parameterCount; index++) {
        Annotation[] annotations = parameterAnnotations[index];
        System.out.println("childAnnotationSize " + annotations.length);
        for (Annotation annotation : annotations) {
            boolean isQuery = annotation.annotationType().isAssignableFrom(Query.class);
            // 获取@Query注解的value
            if (annotation.annotationType().isAssignableFrom(Query.class)) {
                Query query = (Query) annotation;
                String value = query.value();
                System.out.println("isQuery " + isQuery + " value " + value);
            }
            // 获取@Locate注解的value
            if (annotation.annotationType().isAssignableFrom(Locate.class)) {
                Locate locate = (Locate) annotation;
                String value = locate.value();
                System.out.println("locate " + value);
            }
        }
    }
} catch (Exception e) {
      e.printStackTrace();
}
```
输出结果：
```java
parameterCount 2
childAnnotationSize 1
isQuery true value address
childAnnotationSize 2
locate locate
isQuery true value city
```
总结分析：

1. 同一个注解在可以在不同的参数上使用
2. 同一个方法参数可以有不同的注解 所以 `method.getParameterAnnotations()` 是一个二维数组
3. 同一个参数上不能使用相同的注解

### 五 参数注解进行类型限制
由于在编译后，相关的注解信息就被丢弃了，相比较同样可以进行类型限制的的枚举类型，这样的操作可以节省内存。
`@IntDef`的使用
```java
@IntDef的源码

@Retention(SOURCE)
@Target({ANNOTATION_TYPE})
public @interface IntDef {
    /** Defines the allowed constants for this element */
    int[] value() default {};

    /** Defines whether the constants can be used as a flag, or just as an enum (the default) */
    boolean flag() default false;

    /**
     * Whether any other values are allowed. Normally this is
     * not the case, but this allows you to specify a set of
     * expected constants, which helps code completion in the IDE
     * and documentation generation and so on, but without
     * flagging compilation warnings if other values are specified.
     */
    boolean open() default false;
}

@StringDef的源码

@Retention(SOURCE)
@Target({ANNOTATION_TYPE})
public @interface StringDef {
    /** Defines the allowed constants for this element */
    String[] value() default {};

    /**
     * Whether any other values are allowed. Normally this is
     * not the case, but this allows you to specify a set of
     * expected constants, which helps code completion in the IDE
     * and documentation generation and so on, but without
     * flagging compilation warnings if other values are specified.
     */
    boolean open() default false;
}
```

```java
 // 告知编译器不要在.class文件中存储注解数据
@Retention(RetentionPolicy.SOURCE)
@IntDef({NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, NAVIGATION_MODE_ABS})
public @interface NavigationMode{

}

public abstract class AbstractActionBar {
    // 告知编译器不要在.class文件中存储注解数据
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, NAVIGATION_MODE_ABS})
    public @interface NavigationMode{

    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true, value = {NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, NAVIGATION_MODE_ABS})
    @StringDef
    public @interface NavigationActionBarMode{

    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.FIELD)
    public @interface NavigationTitle{
        String value() default "girls";
    }

    // 对传入的字符串进行限制
    @StringDef({NAVIGATION_MENU_START, NAVIGATION_MENU_PAUSE, NAVIGATION_MENU_STOP})
    @Retention(RetentionPolicy.CLASS)
    public @interface NavigationMenuText{

    }

    public static final String NAVIGATION_MENU_START = "start";
    public static final String NAVIGATION_MENU_PAUSE = "pause";
    public static final String NAVIGATION_MENU_STOP = "stop";

    // 定义常量
    public static final int NAVIGATION_MODE_STANDARD = 0;
    public static final int NAVIGATION_MODE_LIST = 1;
    public static final int NAVIGATION_MODE_ABS = 2;

    @NavigationMode
    public abstract int getNavigationMode();

    public abstract void setNavigationMode(@NavigationMode int mode);

    @NavigationActionBarMode
    public abstract int getNavigationActionBarMode();

    public abstract void setNavigationActionBarMode(@NavigationActionBarMode int mode);

    public abstract void setNavigationMenuText(@NavigationMenuText String menuText);
}

public class ActionBarImpl extends AbstractActionBar {

    @NavigationMode
    private int navigationMode;

    @NavigationActionBarMode
    private int navigationActionBarMode;

    // 当注解只有一个属性的时候 可以将属性或者方法设置成 String value()
    // 这样就不用再使用注解的时候 使用注解元素名称和等号
    @NavigationTitle("Science")
    private String navigatioinTitle;


    @Override
    public int getNavigationMode() {
        return navigationMode;
    }

    // 变量model接收的参数只能是@NavigationMode限定的证书  
    @Override
    public void setNavigationMode(@NavigationMode int mode) {
        navigationMode = mode;
    }

    @Override
    public int getNavigationActionBarMode() {
        return navigationActionBarMode;
    }

    // 变量mode接收的参数只能是@NavigationActionBarMode限定的整数
    @Override
    public void setNavigationActionBarMode(@NavigationActionBarMode int mode) {
        navigationActionBarMode = mode;
    }

    // 变量menuText接收的字符串只能是注解@NavigationMenuText限定的字符串
    @Override
    public void setNavigationMenuText(@NavigationMenuText String menuText) {

    }
}

ActionBarImpl actionBar = new ActionBarImpl();
actionBar.setNavigationMode(AbstractActionBar.NAVIGATION_MODE_STANDARD);
```

### 五 注解处理器
### 六 自动代码生成
### 七 注解处理器相关


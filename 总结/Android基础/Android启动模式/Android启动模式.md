## Android 启动模式

Android常见的Activity四种启动模式Standard, SingleTask, SingleTop, SingleInstance.

### Standard
标准启动模式，在原有的Activity栈中，压入一个新的Activity.

### SingleTask
栈内复用模式，在当前的Activity栈中，如果已经存在一个要启动的目标Activity（不是在栈顶）, 那么就会把目标Activity之上的所有Activity
退出栈，然后将已存在的Activity放到栈顶。如果目标Activity已经在栈顶，那么就会重用已经存在的这个Activity，而不是重新创建。
注意，这种启动的方式在栈顶复用的情况下会调用Activity的onNewIntent()方法。

### SingleTop
栈顶复用模式，如果目标Activity已经在栈顶，那么就会重用目标Activity, 不会重新创建。如果目标Activity不是在栈顶，那么就会重新创建一个新的
Activity放到栈顶。这种模式会在栈顶复用的情况下会调用onNewIntent()方法。

### SingleInstance
这种启动模式会重新启动一个Activity栈，并将这个Activity添加到这个栈中，并保证这个栈内只有该Activity的一个实例。




package com.lucky.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lucky.androidlearn.R
import kotlinx.android.synthetic.main.activity_kotlinlearn.*

class KotlinLearnMainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlinlearn);
        val iArray: IntArray = intArrayOf(1, 2, 3)
        val sArray: Array<String> = Array<String>(3, { i -> i.toString() })
        val anyArray: Array<Any> = arrayOf(1, "2", 3.0, 4.1f)
        val lArray: LongArray = longArrayOf(1L, 2L, 3L)
        //operator()
        //whenFun(25)
        listFruits()
        listItems()
        displayColors()
        downTo()
        var staff = Staff("codeAndroid", "Android开发", 22)
        //val变量不能重新赋值
        //staff.position = ""
        replaceName()
        staff.name = "Jack"
        var staff1 = staff.copy()
        var staff2 = staff.copy(name = "Rose", position = "UI")
        println("name :${staff.name} position :${staff.position} age : ${staff.age}")
        println("staff hashCode()  :${staff.hashCode()}  staff1 hashCode() :${staff1.hashCode()}")
        println("staff is equals staff1 ? ${staff.equals(staff1)}")
        println("maxValue is ${max(10, 2)}")
        printProduct("1", "2")
        printProduct("2", "3")
        printProduct("1", "a")
        printProduct("f", "e")
        btn_kotlin_01.setOnClickListener {
            println("hello world hahaahd")
        }

    }

    // 枚举类型
    enum class Color {
        RED, GREEN, BLUE, BLACK, ORANGE
    }

    private fun displayColors() {
        var color: Color = Color.BLACK
        // 转换指定name为枚举值，若未匹配成功，会抛出IllegalArgumentException
        Color.valueOf("BLACK")
        // 已数组的形式，返回枚举值
        Color.values()
        ////获取枚举名称
        println(color.name)
        //获取枚举值在所有枚举数组中定义的顺序,0开始
        println(color.ordinal)
    }


    // 文字替换
    private fun replaceName() {
        var name: String = "Jack"
        println("我的名字是 ${name}")
        println("我的名字是 $name")
    }

    // kotlin数据类
    data class Staff<T>(var name: String, val position: String, var age: T)

    private fun downTo() {
        //倒序输出5 4 3 2 1 0
        for (i in 5 downTo 0) {
            println(i)
        }
        //设置输出数据步长
        for (i in 1..5 step 3) print(i) // 输出 14
        //step和downTo混合使用
        for (i in 5 downTo 1 step 3) print(i) //输出52
    }


    // 控制语句 Any相当于Java中的Object类
    private fun whenFun(obj: Any) {
        when (obj) {
            25 -> println("25")
            "kotlin" -> println("kotlin")
            !is String -> println("Not String")
            is Long -> println("Number is Long")
            else -> println("Nothing")
        }
    }


    open class People public constructor(var id: String, var name: String) {
        // 可以在类中初始化属性
        var customName = name.toUpperCase()

        //
        constructor(id: String, name: String, age: Int) : this(id, name) {
            println("constructor")
        }

        init {
            println(" 初始化操作，可使用constructor参数")
        }

        // 需要open修饰，子类才可以
        open fun study() {
            println("study")
        }
    }

    class Student(id: String, name: String) : People(id, name) {
        var test: Number = 3
        private var name1: String?
            get() {
                return name1
            }
            set(value) {
                name1 = value
            }

        //override修饰的方法，默认是可以被继承的。若希望不被继承，可以使用 final 关键词修饰
        override fun study() {
            super.study()
        }
    }


    // 数组循环遍历
    fun forLoop(array: Array<String>) {
        //第一种方式直接输出字符 (类似java foreach)
        for (str in array) {
            println(str)
        }
        //Array提供了forEach函数
        array.forEach {
            println(it)
        }
        //array.indices是数组索引
        for (i in array.indices) {
            println(array[i])
        }
        var i = 0
        while (i < array.size) {
            println(array[i++])
        }
    }

    private fun operator() {
        var a: Int = 4
        var shl: Int = a shl (1)  // Java中的左移运算符 <<
        var shr: Int = a shr (1)  // Java中的右移运算符 >>
        var ushr: Int = a ushr (3)// 无符号右移 ，高位补 0
        var and: Int = 2 and (4)  // 按位与操作 &
        var or: Int = 2 or (4)   // 按位或操作 |
        var xor: Int = 2 xor (6)  // 按位异或操作 ^
        println("shl: " + shl + " shr " + shr + " ushr " + ushr + " and "
                + and + " or " + or + " xor " + xor)
    }

    // if表达式
    fun max(a: Int, b: Int) = if (a > b) a else b

    fun sum(a: Int, b: Int): Int {
        return a + b;
    }

    fun sum2(a: Int, b: Int) = a + b

    fun printIn(a: Int) {
        println(a)
    }

    private fun printProduct(var1: String, var2: String) {
        val x = parseInt(var1)
        val y = parseInt(var2)

        if (x != null && y != null) {
            print(x * y)
        } else {
            println("Either '$var1' or '$var2' is not a number")
        }
    }

    private fun listItems() {
        val items = listOf("apple", "banana", "orange", "pear")
        for (item in items) {
            println(item)
        }
        val fruitItems = setOf("apple", "banana", "kiwi")
        when {
            "orange" in fruitItems -> println("juicy")
            "apple" in fruitItems -> println("apple is fine too")
        }
    }

    private fun listFruits(){
        val fruits = listOf("apple", "ankle", "banana", "orange", "pear", "peach")
           fruits
                .filter { it.startsWith("a") }
                .sortedBy { it }
                .map { it.toUpperCase() }
                .forEach { println(it)}
    }


    // Int?可以返回空 null
    private fun parseInt(str: String): Int? {
        return str.toIntOrNull()
    }

    private object KotlinLearn {
        private var kotlin = ""

        @JvmStatic
        fun main(args: Array<String>) {
            kotlin = "I Love Kotlin"
            println(kotlin)
        }
    }

}

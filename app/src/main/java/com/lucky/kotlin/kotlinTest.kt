package com.lucky.kotlin

import kotlin.properties.Delegates


class School {

    constructor() {
       println("first constructor")
    }

    constructor(name: String, address: String) : this() {
        println("secondary constructor")
    }

    init {
        println("init代码块")
    }

}


fun main() {
    println("hello world")
//    var school = School("把一小学","西大街")
    var school = School()

    val age:Int
    age = 10
//    println(age)

//    val b:Array<Int> = Array(3, {k->k*k})
//    for (item in b){
//         println(item)
//    }

    var a:Int by Delegates.observable(123){
        p, new , old->
           println("name:${p.name} ${p}")
    }

    a = 10
}
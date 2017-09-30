package com.github.nyaculak.hashmap

// example usage
fun main(args: Array<String>) {
    val hashMap: HashMap = HashMap(40)

    hashMap.set("hi", 2)
    hashMap.set("bye", 3)

    println(hashMap.get("hi"))
    println(hashMap.get("bye"))

    println(hashMap.load())

    println(hashMap.delete("bye"))
    println(hashMap.get("bye"))

    println(hashMap.load())
}
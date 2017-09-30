package com.github.nyaculak.hashmap

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

private const val COLLISION_MAP_SIZE = 3
private const val COLLISION_KEY_1 = "a"
private const val COLLISION_KEY_2 = "d"
private const val COLLISION_KEY_3 = "g"
private const val COLLISION_VAL_1 = 1
private const val COLLISION_VAL_2 = 2
private const val COLLISION_VAL_3 = 3

class HashMapTest {
    private lateinit var collisionMap: HashMap

    @BeforeMethod
    fun setUp() {
        collisionMap = HashMap(3)
        collisionMap.set(COLLISION_KEY_1, COLLISION_VAL_1)
        collisionMap.set(COLLISION_KEY_2, COLLISION_VAL_2)
        collisionMap.set(COLLISION_KEY_3, COLLISION_VAL_3)
    }

    @Test(expectedExceptions = arrayOf(IllegalArgumentException::class))
    fun testHashMapConstructorWhenSizeIsNegative() {
        HashMap(-1)
    }

    @Test(expectedExceptions = arrayOf(IllegalArgumentException::class))
    fun testHashMapConstructorWhenSizeIsZero() {
        HashMap(0)
    }

    @Test
    fun testHashMapConstructorWhenSizeIsPositive() {
        val hashMap = HashMap(1)
        hashMap.set("test", 1)
    }

    @Test
    fun testHashMapGetReturnsNullWhenEmpty() {
        val hashMap = HashMap(40)
        assertThat(hashMap.get("")).isNull()
        assertThat(hashMap.get("a")).isNull()
        assertThat(hashMap.get("1")).isNull()
        assertThat(hashMap.get("This is a stress test of sorts")).isNull()
    }

    @Test
    fun testHashMapGetReturnsStoredValue() {
        val hashMap = HashMap(40)
        hashMap.set("", 1)
        hashMap.set("a", 2)
        hashMap.set("b", 2)
        assertThat(hashMap.get("")).isEqualTo(1)
        assertThat(hashMap.get("a")).isEqualTo(2)
        assertThat(hashMap.get("b")).isEqualTo(2)
    }

    @Test
    fun testHashMapGetSetArbitraryObject() {
        data class Obj(val x: Int, val y: String)
        val hashMap = HashMap(40)
        val a = 1
        val b = "hi"
        val c = Obj(2, "Seven")
        hashMap.set("a", a)
        hashMap.set("b", b)
        hashMap.set("c", c)
        assertThat(hashMap.get("a")).isEqualTo(a)
        assertThat(hashMap.get("b")).isEqualTo(b)
        assertThat(hashMap.get("c")).isEqualTo(c)
    }

    @Test
    fun testHashMapSetOverwritesPreviousValue() {
        val hashMap = HashMap(40)
        hashMap.set("a", 1)
        assertThat(hashMap.get("a")).isEqualTo(1)
        hashMap.set("a", 2)
        assertThat(hashMap.get("a")).isEqualTo(2)
    }

    @Test
    fun testHashMapSetReturnsFalseWhenHashMapIsFull() {
        val hashMap = HashMap(3)
        assertThat(hashMap.set("a", 1)).isTrue()
        assertThat(hashMap.set("b", 2)).isTrue()
        assertThat(hashMap.set("c", 3)).isTrue()
        assertThat(hashMap.set("d", 4)).isFalse()

        assertThat(hashMap.set("c", 3)).isTrue()
        hashMap.delete("c")
        assertThat(hashMap.set("d",4)).isTrue()
    }

    @Test
    fun testHashMapDeleteRemoveStoredValue() {
        val hashMap = HashMap(40)
        hashMap.set("a", 1)
        assertThat(hashMap.get("a")).isEqualTo(1)

        assertThat(hashMap.delete("a")).isEqualTo(1)
        assertThat(hashMap.get("a")).isNull()
    }

    @Test
    fun testHashMapInsertsWithHashCollision() {
        assertThat(COLLISION_KEY_1.hashCode() % COLLISION_MAP_SIZE)
                .isEqualTo(COLLISION_KEY_2.hashCode() % COLLISION_MAP_SIZE)
                .isEqualTo(COLLISION_KEY_3.hashCode() % COLLISION_MAP_SIZE) // all keys map to the same bucket in map
        assertThat(collisionMap.get(COLLISION_KEY_1)).isEqualTo(COLLISION_VAL_1)
        assertThat(collisionMap.get(COLLISION_KEY_2)).isEqualTo(COLLISION_VAL_2)
        assertThat(collisionMap.get(COLLISION_KEY_3)).isEqualTo(COLLISION_VAL_3)
    }

    @Test
    fun testHashMapDeleteFirstHashCollision() {
        collisionMap.delete(COLLISION_KEY_1)
        assertThat(collisionMap.get(COLLISION_KEY_1)).isNull()
        assertThat(collisionMap.get(COLLISION_KEY_2)).isEqualTo(COLLISION_VAL_2)
        assertThat(collisionMap.get(COLLISION_KEY_3)).isEqualTo(COLLISION_VAL_3)
    }

    @Test
    fun testHashMapDeleteMiddleHashCollision() {
        collisionMap.delete(COLLISION_KEY_2)
        assertThat(collisionMap.get(COLLISION_KEY_1)).isEqualTo(COLLISION_VAL_1)
        assertThat(collisionMap.get(COLLISION_KEY_2)).isNull()
        assertThat(collisionMap.get(COLLISION_KEY_3)).isEqualTo(COLLISION_VAL_3)
    }

    @Test
    fun testHashMapDeleteLastHashCollision() {
        collisionMap.delete(COLLISION_KEY_3)
        assertThat(collisionMap.get(COLLISION_KEY_1)).isEqualTo(COLLISION_VAL_1)
        assertThat(collisionMap.get(COLLISION_KEY_2)).isEqualTo(COLLISION_VAL_2)
        assertThat(collisionMap.get(COLLISION_KEY_3)).isNull()
    }

    @Test
    fun testHashMapDeleteConsecutiveHashCollisions() {
        assertThat(collisionMap.delete(COLLISION_KEY_1)).isEqualTo(COLLISION_VAL_1)
        assertThat(collisionMap.delete(COLLISION_KEY_2)).isEqualTo(COLLISION_VAL_2)
        assertThat(collisionMap.delete(COLLISION_KEY_3)).isEqualTo(COLLISION_VAL_3)
        assertThat(collisionMap.get(COLLISION_KEY_1)).isNull()
        assertThat(collisionMap.get(COLLISION_KEY_2)).isNull()
        assertThat(collisionMap.get(COLLISION_KEY_3)).isNull()
    }

    @Test
    fun testHashMapLoad() {
        val map = HashMap(3)
        assertThat(map.load()).isEqualTo(0.0f)

        map.set("a", 1)
        assertThat(map.load()).isEqualTo(1.0f/3)

        map.set("b", 2)
        assertThat(map.load()).isEqualTo(2.0f/3)

        map.delete("b")
        assertThat(map.load()).isEqualTo(1.0f/3)

        map.set("b", 2)
        map.set("b", 3)
        assertThat(map.load()).isEqualTo(2.0f/3)

        map.set("c", 4)
        assertThat(map.load()).isEqualTo(1.0f)

        map.delete("a")
        map.delete("b")
        map.delete("b")
        assertThat(map.load()).isEqualTo(1.0f/3)

        map.delete("c")
        assertThat(map.load()).isEqualTo(0.0f)
    }
}
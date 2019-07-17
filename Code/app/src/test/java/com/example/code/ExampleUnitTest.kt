package com.example.code

import org.junit.Test
import kotlin.math.max

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private fun insertSort(list: MutableList<Int>?): List<Int>? {
        return if (list == null || list.size < 2) {
            list
        } else {
            for (i in (1 until list.size)) {
                label@ for (j in i downTo 0 + 1) {
                    if (list[j] < list[j - 1]) {
                        val temp = list[j]
                        list[j] = list[j - 1]
                        list[j - 1] = temp
                    } else {
                        break@label
                    }
                }

            }
            list
        }
    }

    private fun selectSort(list: MutableList<Int>?): List<Int>? {
        return if (list == null || list.size < 2) {
            list
        } else {
            for (i in (0..list.size - 2)) {
                var minIndex = i
                for (j in (i + 1 until list.size)) {
                    if (list[minIndex] > list[j]) {
                        minIndex = j
                    }
                }
                if (i != minIndex) {
                    val temp = list[i]
                    list[i] = list[minIndex]
                    list[minIndex] = temp
                }
            }
            list
        }
    }

    private fun bubbleSort(list: MutableList<Int>?): List<Int>? {
        return if (list == null || list.size < 2) {
            list
        } else {
            for (i in (0..list.size - 2)) {
                for (j in (0..list.size - 2 - i)) {
                    if (list[j + 1] < list[j]) {
                        val temp = list[j + 1]
                        list[j + 1] = list[j]
                        list[j] = temp
                    }
                }
            }
            list
        }
    }

    /**
     * 快速排序
     *
     * https://wiki.jikexueyuan.com/project/easy-learn-algorithm/fast-sort.html
     */
    private fun quickSortMethod(list: MutableList<Int>, left: Int, right: Int) {
        if (list.size < 2 || left >= right) {
            return
        }
        val middle = getMiddle(list, left, right)
        quickSortMethod(list, left, middle)
        quickSortMethod(list, middle + 1, right)
    }

    private fun getMiddle(list: MutableList<Int>, left: Int, right: Int): Int {
        var low = left
        var high = right
        val temp = list[low]
        while (low != high) {
            while (list[high] >= temp && high > low) {
                high--
            }
            while (list[low] <= temp && high > low) {
                low++
            }
            if (low < high) {
                val value = list[low]
                list[low] = list[high]
                list[high] = value
            }
        }
        list[left] = list[low]
        list[low] = temp
        return low
    }

    /**
     * 三数之和
     *
     * https://zhuanlan.zhihu.com/p/53519899
     */
    private fun threeNumberSum(list: MutableList<Int>): MutableSet<String> {
        val mutableSet = mutableSetOf<String>()
        if (list.size >= 3) {
            list.sort()
            for (i in 0 until (list.size - 2)) {
                val target = -list[i]
                var low = i + 1
                var high = list.size - 1
                while (low < high) {
                    when {
                        list[low] + list[high] < target -> low++
                        list[low] + list[high] > target -> high--
                        else -> {
                            mutableSet.add("(${-target}, ${list[low]}, ${list[high]})")
                            low++
                            high--
                        }
                    }
                }
            }
        }
        return mutableSet
    }

    /**
     * 岛屿的最大面积
     *
     * https://blog.csdn.net/qq_38959715/article/details/80937405
     */
    private fun maxAreaOfIsland(list: Array<Array<Int>>): Int {
        var maxArea = 0
        for (i in 0 until list.size) {
            for (j in 0 until list[i].size) {
                if (list[i][j] == 1) {
                    maxArea = max(maxArea, countMaxArea(list, i, j))
                }
            }
        }
        return maxArea
    }

    private fun countMaxArea(list: Array<Array<Int>>, i: Int, j: Int): Int {
        if (i < 0 || i >= list.size || j < 0 || j >= list[0].size) {
            return 0
        }
        return if (list[i][j] == 1) {
            list[i][j] = -1
            1 + countMaxArea(list, i - 1, j) + countMaxArea(list, i, j - 1) + countMaxArea(
                list,
                i + 1,
                j
            ) + countMaxArea(
                list,
                i,
                j + 1
            )
        } else {
            0
        }
    }

    /**
     * 搜索旋转排序数组
     *
     * https://zhangluncong.com/2018/05/03/search/
     */
    private fun searchIndex(list: List<Int>, target: Int): Int {
        if (list.isNotEmpty()) {
            var low = 0
            var high = list.size - 1
            while (low < high) {
                val middle = (low + high) / 2
                if (list[middle] == target) {
                    return middle
                }
                if (list[middle] >= list[low]) {
                    if (target >= list[low] && target <= list[middle]) {
                        high = middle - 1
                    } else {
                        low = middle + 1
                    }
                } else {
                    if (target >= list[middle] && target <= list[high]) {
                        low = middle + 1
                    } else {
                        high = middle - 1
                    }
                }
            }
            if (list[low] == target) {
                return low
            }
            if (list[high] == target) {
                return high
            }
        }
        return -1
    }

    /**
     * 接雨水问题
     *
     * https://blog.csdn.net/lv1224/article/details/81023833
     */
    private fun receiveRain(list: List<Int>, left: Int, total: Int): Int {
        if (list.size < 3 || list.size - left < 3) {
            return total
        }
        var newTotal = total
        for (i in left + 1 until list.size) {
            if (list[i] >= list[left]) {
                for (j in left + 1 until i) {
                    newTotal = newTotal + list[left] - list[j]
                }
                return receiveRain(list, i, newTotal)
            }
        }
        var max = list[left + 1]
        var maxIndex = left + 1
        for (m in left + 2 until list.size) {
            if (list[m] > max) {
                max = list[m]
                maxIndex = m
            }
        }
        for (n in left + 1 until maxIndex) {
            newTotal = newTotal + max - list[n]
        }
        return receiveRain(list, maxIndex, newTotal)
    }

    @Test
    fun main() {
//        val list = mutableListOf(30, 10, 28, 14, 36, 10, 100, 32)
//        val sortedList = quickSortMethod(list, 0, list.size - 1)
//        System.out.println(list.toString())

//        threeNumberSum(mutableListOf(-1, 0, 1, 2, -1, -4))

//        val maxArea = maxAreaOfIsland(
//            arrayOf(
//                arrayOf(1, 0, 0, 1),
//                arrayOf(1, 0, 1, 1),
//                arrayOf(0, 1, 1, 0),
//                arrayOf(0, 0, 0, 0)
//            )
//        )
//        System.out.println(maxArea)

//        val index = searchIndex(listOf(1, 2, 3, -3, -2, -1, 0), -3)
//        System.out.println(index)

        val total = receiveRain(listOf(0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1), 0, 0)
        System.out.println(total)
    }
}

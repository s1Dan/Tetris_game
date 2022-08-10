package com.s1dan.tetris.models

import com.s1dan.tetris.helper.array2dOfByte

class Frame(private val wigth: Int) {
    val data: ArrayList<ByteArray> = ArrayList()

    // Обрабатываем строку, преобразуя каждый отдельный символ строки в байтовое представление,
    // и добавляем байтовое представление в байтовый массив,
    // после чего байтовый массив добавляется в список данных
    fun addRow(byteStr:String):Frame {
        val row = ByteArray(byteStr.length)
        for (index in byteStr.indices) {
            row[index] = "${byteStr[index]}".toByte()
        }
        data.add(row)
        return this
    }

    fun as2ByteArray():Array<ByteArray>{
        val bytes = array2dOfByte(data.size,wigth)
        return data.toArray(bytes)
    }

}
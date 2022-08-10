package com.s1dan.tetris

import android.graphics.Point
import com.s1dan.tetris.constants.CellConstants
import com.s1dan.tetris.constants.FieldConstrants
import com.s1dan.tetris.helper.array2dOfByte
import com.s1dan.tetris.models.Block
import com.s1dan.tetris_game.AppPreferences

class AppModel {

    var score: Int = 0
    private var preferences: AppPreferences? = null

    var currentBlock: Block? = null
    var currentState: String = Statuses.AWAITING_START.name

    private var field: Array<ByteArray> = array2dOfByte(FieldConstrants.ROW_COUNT.value, FieldConstrants.COLOMN_COUNT.value)

    enum class Statuses {
        AWAITING_START, ACTIVE,
//        INACTIVE,
        OVER
    }

    enum class Motions {
        LEFT, RIGHT, DOWN, ROTATE
    }

    fun setPreferences(preferences: AppPreferences?) {
        this.preferences = preferences
    }

    fun getCellStatus(row: Int, column: Int): Byte? {
        return field[row][column]
    }

    private fun setCellStatus(row: Int, column: Int, status: Byte?) {
        if (status != null) {
            field[row][column] = status
        }
    }

    fun isGameOver(): Boolean {
        return currentState == Statuses.OVER.name
    }

    fun isGameActive(): Boolean {
        return currentState == Statuses.ACTIVE.name
    }

    fun isGameAwaitingStart(): Boolean {
        return currentState == Statuses.AWAITING_START.name
    }

    private fun boostScore() {
        score += 10
        if (score > preferences?.getHighScore() as Int)
            preferences?.saveHighScore(score)
    }

    // Прежде, чем передать полю новый сгененированный блок, методом blockAdditionalPossible модель должна убедиться,
    // что поле еще не заполнено, и блок может перемещаться
    fun generateNextBlock() {
        currentBlock = Block.createBlock()
    }

    private fun validTranslation(position: Point, shape: Array<ByteArray>):
            Boolean {
        return if (position.y < 0 || position.x < 0) {
            false
        } else
            if (position.y + shape.size > FieldConstrants.ROW_COUNT.value) {
                false
            } else
                if (position.x + shape[0].size > FieldConstrants.COLOMN_COUNT.value) {
                    false
                } else {
                    for (i in 0 until shape.size) {
                        for (j in 0 until shape[i].size) {
                            val y = position.y + i
                            val x = position.y + j
                            if (CellConstants.EMPTY.value != shape[i][j] && CellConstants.EMPTY.value != field[y][x]) {
                                return false
                            }
                        }
                    }
                    true
                }
    }

    // Функция применяет validTranslation,
    // которая проверяет, разрешен ли выполненных ход игроком
    private fun moveValid(position: Point,frameNumber: Int?) : Boolean {
        val shape:Array<ByteArray>?= currentBlock?.getShape(frameNumber as Int)
        return validTranslation(position,shape as Array<ByteArray>)
    }

    // Генерируем обновление поля. Сначала проверяем,
    // находится ли игра в активном состоянии при вызове
    // Если активна, извлекаем номер фрейма и координаты блока.
    // Дальше определяем запрашиваемое действие, потом меняются координаты блока
    // Если запрашивается вращательное движение, то значение frameNumber изменяются с учетом соответствующего номера фрейма
    fun generateField(action: String) {
        if (isGameActive()) {
            resetField()
            var frameNumber: Int? = currentBlock?.frameNumber
            val coordinate: Point? = Point()
            coordinate?.x = currentBlock?.position?.x
            coordinate?.y = currentBlock?.position?.y

            when(action) {
                Motions.LEFT.name -> {
                    coordinate?.x = currentBlock?.position?.x?.minus(1)
                }
                Motions.RIGHT.name -> {
                    coordinate?.x = currentBlock?.position?.x?.plus(1)
                }
                Motions.DOWN.name -> {
                    coordinate?.y = currentBlock?.position?.y?.plus(1)
                }
                Motions.ROTATE.name -> {
                    frameNumber = frameNumber?.plus(1)
                    if (frameNumber != null) {
                        if(frameNumber >= currentBlock?.frameCount as Int) {
                            frameNumber = 0
                        }
                    }
                }
            }

            // проверяем, является ли запрашиваемое движение действительным.
            // Если нет, то блок фиксируется в поле в текущей позиции методом translateBlock()
            if (!moveValid(coordinate as Point, frameNumber)) {
                translateBlock(currentBlock?.position as Point, currentBlock?.frameNumber as Int)
                if (Motions.DOWN.name == action) {
                    boostScore()
                    persistCellData()
                    assessField()
                    generateNextBlock()
                    if (!blockAdditionalPossible()) {
                        currentBlock = null;
                        currentState = Statuses.OVER.name;
                        resetField(false);
                    }
                }
                else {
                    if (frameNumber != null) {
                        translateBlock( coordinate, frameNumber)
                        currentBlock?.setState(frameNumber, coordinate)
                    }
                }
            }
        }
    }

    private fun resetField(ephemeralCellOnly: Boolean = true) {
        for (i in 0 until FieldConstrants.ROW_COUNT.value) {
                (0 until FieldConstrants.COLOMN_COUNT.value)
                    .filter{ !ephemeralCellOnly || field[i][it] == CellConstants.EPHEMERAL.value }
                    .forEach{ field[i][it] == CellConstants.EMPTY.value }
        }
    }

    private fun persistCellData() {
        for (i in 0 until field.size) {
            for (j in 0 until field[i].size) {
                var status = getCellStatus(i, j)
                if (status == CellConstants.EPHEMERAL.value) {
                    status = currentBlock?.staticValue
                    setCellStatus(i, j, status)
                }
            }
        }
    }

    // если заполнены все ячейки в строке, строка очищается и сдвигается на величину,
    // определенную методом shiftRow()
    private fun assessField() {
        for (i in 0 until field.size) {
            var emptyCells = 0;
            for (j in 0 until field[i].size) {
                val status = getCellStatus(i,j)
                val isEmpty = CellConstants.EMPTY.value == status
                if (isEmpty)
                    emptyCells++
            }
            if (emptyCells == 0)
                shiftRows(i)
        }
    }

    private fun translateBlock(position: Point,frameNumber:Int) {
        synchronized(field) {
            val shape: Array<ByteArray>? = currentBlock?.getShape(frameNumber)
            if (shape != null) {
                for (i in shape.indices) {
                    for (j in 0 until shape[i].size) {
                        val y = position.y + i
                        val x = position.x + i
                        if (CellConstants.EMPTY.value != shape[i][j]) {
                            field[y][x] = shape[i][j]
                        }
                    }
                }
            }
        }
    }

    // метод для проверки поля на заполненность
    private fun blockAdditionalPossible(): Boolean {
        if (!moveValid(currentBlock?.position as Point,
                currentBlock?.frameNumber)) {
            return false
        }
        return true
    }

    private fun shiftRows(nToRow: Int) {
        if (nToRow > 0) {
            for (j in nToRow - 1 downTo 0) {
                for (m in 0 until field[j].size) {
                    setCellStatus(j+1,m,getCellStatus(j,m))
                }
            }
        }
        for (j in 0 until field[0].size) {
            setCellStatus(0,j,CellConstants.EMPTY.value)
        }
    }

    fun startGame() {
        if (!isGameActive()) {
            currentState = Statuses.ACTIVE.name
            generateNextBlock()
        }
    }

    fun restartGame(){
        resetModel()
        startGame()
    }

    fun endGame() {
        score = 0
        currentState = AppModel.Statuses.OVER.name
    }

    private fun resetModel() {
        resetField(false)
        currentState = Statuses.AWAITING_START.name
        score = 0
    }


}
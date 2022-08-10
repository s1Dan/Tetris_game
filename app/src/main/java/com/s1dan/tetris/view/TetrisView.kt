package com.s1dan.tetris.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.s1dan.tetris.AppModel
import com.s1dan.tetris.constants.CellConstants
import com.s1dan.tetris.constants.FieldConstrants
import com.s1dan.tetris.models.Block
import com.s1dan.tetris_game.GameActivity


class TetrisView : View {
    private val paint = Paint()
    private var lastMove: Long = 0
    private var model: AppModel? = null
    private var activity: GameActivity? = null
    private val viewHandler = ViewHandler(this)
    private var cellSize: Dimension = Dimension(0, 0)
    private var frameOffSet: Dimension = Dimension(0, 0)

    constructor(context: Context, attrs: AttributeSet) :
            super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context,attrs,defStyle)

    companion object {
        private const val DELAY = 500
        private const val BLOCK_OFFSET = 2
        private const val FRAME_BASE = 10
    }

     private class ViewHandler(private val owner: TetrisView): Handler() {
         override fun handleMessage(message: Message) {
             if (message.what == 0) {
                 if (owner.model != null) {
                     if (owner.model!!.isGameOver()) {
                         owner.model?.endGame()
                         Toast.makeText(owner.activity, "Game Over", Toast.LENGTH_LONG).show()
                     }
                 }
                 if (owner.model!!.isGameActive()) {
                     owner.setGameCommandWithDelay(AppModel.Motions.DOWN)
                 }
             }
         }
         fun sleep(delay: Long) {
             this.removeMessages(0)
             sendMessageDelayed(obtainMessage(0), delay)
         }
     }

    fun setModel(model:AppModel) {
        this.model = model
    }

    fun setActivity (gameActivity: GameActivity) {
        this.activity = gameActivity
    }

    // Устанавливает исполняемую игрой текущую команду перемещения
    fun setGameCommand(move: AppModel.Motions) {
         if (null != model &&(model?.currentState == AppModel.Statuses.ACTIVE.name)) {
             if (AppModel.Motions.DOWN == move) {
                 model?.generateField(move.name)
                 invalidate()
                 return
             }
             setGameCommandWithDelay(move)
         }
    }

    // Функционирует аналогично setGameCommand(),
    // обновляет счет игры и переводит viewHandler в спящий режим после выполнения игровой команды
    fun setGameCommandWithDelay(move: AppModel.Motions) {
        val now = System.currentTimeMillis()
        if (now - lastMove > DELAY) {
            model?.generateField(move.name);invalidate()
            lastMove = now
        }
        updateScore()
        viewHandler.sleep(DELAY.toLong())
    }

    // Служит для обновления в игровом действии текстовых представлений текущего счета и рекорда
    private fun updateScore() {
        activity?.tvCurrentScore?.text = "${model?.score}"
        activity?.tvHighScore?.text = "${activity?.appPreferences?.getHighScore()}"
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawFrame(canvas)
        if (model != null) {
            for (i in 0 until FieldConstrants.ROW_COUNT.value) {
                for (j in 0 until FieldConstrants.COLOMN_COUNT.value) {
                    drawCell(canvas, i, j)
                }
            }
        }
    }

    private fun drawFrame(canvas: Canvas) {
        paint.color = Color.LTGRAY
        canvas.drawRect(frameOffSet.width.toFloat(),
            frameOffSet.height.toFloat(), width -
                    frameOffSet.width.toFloat(),
            height - frameOffSet.height.toFloat(), paint)
    }

    private fun drawCell(canvas: Canvas, row: Int, col: Int) {
        val cellStatus = model?.getCellStatus(row, col)
        if (CellConstants.EMPTY.value != cellStatus) {
            val color = if (CellConstants.EPHEMERAL.value == cellStatus) {
                model?.currentBlock?.color
            }
            else {
                Block.getColor(cellStatus as Byte)
            }
            drawCell(canvas, col, row, color as Int)
        }
    }

    private fun drawCell(canvas: Canvas, row: Int, col: Int, rbgColor: Int) {
        paint.color = rbgColor
        val left: Float = (frameOffSet.height + x * cellSize.height + BLOCK_OFFSET)
        val top: Float = (frameOffSet.height + y * cellSize.height + BLOCK_OFFSET)
        val right: Float = (frameOffSet.height + (x + 1) * cellSize.height + BLOCK_OFFSET)
        val bottom: Float = (frameOffSet.height + (y + 1) * cellSize.height + BLOCK_OFFSET)
//        val bottom: Float = (frameOffSet.height + (y + 1) * cellSize.height + BLOCK_OFFSET).toFloat()
        val rectangle = RectF(left, top, right, bottom)
        canvas.drawRoundRect(rectangle, 4F, 4F, paint)
    }

    // Вызывается при изменении размера представления
    // Обеспечивает доступ к текущим значениям ширины и высоты представления, а также, к прежним значениям ширины и высоты
    // Вычисляются offsetX и offsetY и применяются для установки frameOffset
    override fun onSizeChanged(width: Int, height: Int, previousWidth: Int, previousHeight: Int) {
        super.onSizeChanged(width, height, previousWidth, previousHeight)
        val cellWidth =  (width - 2 * FRAME_BASE) / FieldConstrants.COLOMN_COUNT.value
        val cellHeight =  (height - 2 * FRAME_BASE) / FieldConstrants.COLOMN_COUNT.value
        val n = Math.min(cellWidth, cellHeight)
        this.cellSize = Dimension(n, n)
        val offsetX = (width - FieldConstrants.COLOMN_COUNT.value * n) /2
        val offsetY = (height - FieldConstrants.COLOMN_COUNT.value * n) /2
        this.frameOffSet = Dimension(offsetX, offsetY)
    }

    private data class Dimension(val width: Int, val height: Int) { }

}
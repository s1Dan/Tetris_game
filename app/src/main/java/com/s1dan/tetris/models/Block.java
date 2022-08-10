package com.s1dan.tetris.models;

import android.graphics.Color;
import android.graphics.Point;
import androidx.annotation.NonNull;
import com.s1dan.tetris.constants.FieldConstrants;
import java.util.Random;

public class Block {

    // форма блока, количество фреймов, цветовые характеристики и текущая пространственная позиция блока в игровом поле
    private final int shapeIndex;
    private int frameNumber;
    private final BlockColor color;
    private Point position;

    // конструктор класса ддля инициализации созданных переменных экземпляра
    private Block (int shapeIndex, BlockColor blockColor) {
        this.frameNumber = 0;
        this.shapeIndex = shapeIndex;
        this.color = blockColor;
        this.position = new Point(FieldConstrants.COLOMN_COUNT.getValue()/2, 0);
    }

     //цвета
    public enum BlockColor {
        PINK(Color.rgb(255, 105, 180),(byte) 2),
        GREEN(Color.rgb(0,128,0),(byte) 3),
        ORANGE(Color.rgb(255,255,0),(byte) 4),
        YELLOW (Color.rgb(255,255,0),(byte) 5),
        CYAN (Color.rgb(0,255,255),(byte) 6);

        BlockColor(int rgbValue, byte values) {
            this.rgbValue = rgbValue;
            this.byteValue = values;
        }

        private final int rgbValue;
        private final byte byteValue;
    }

    // Случайно выбирает индекс для формы в классе enum Shape и BlockColor
    // и присваивает 2 случайных значения элементам shapeIndex и blockColor
    public static Block createBlock() {
        Random random = new Random();
        int shapeIndex = random.nextInt(Shape1.values().length);
        BlockColor blockColor = BlockColor.values()[random.nextInt(BlockColor.values().length)];
        Block block = new Block(shapeIndex,blockColor);
        block.position.x = block.position.x - Shape1.values()[block.shapeIndex].getStartPosition();
        return block;
    }

    // геттеры и сеттеры
    // получение доступ к свойствам экземпляров блока
    public static int getColor(byte value) {
        for (BlockColor color : BlockColor.values()) {
            if (value == color.byteValue) {
                return color.rgbValue;
            }
        }
        return -1;
    }

    public final void setState(int frame, Point position) {
        this.frameNumber = frame;
        this.position = position;
    }
    @NonNull
    public final byte[][] getShape(int frameNumber) {
        return Shape1.values()[shapeIndex].getFrame(frameNumber).as2ByteArray();
    }

    public Point getPosition() {
        return this.position;
    }

    public final int getFrameCount() {
        return Shape1.values()[shapeIndex].getFrameCount();
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public int getColor() {
        return color.rgbValue;
    }

    public byte getStaticValue(){
        return color.byteValue;
    }
}


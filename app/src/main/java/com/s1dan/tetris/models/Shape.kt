package com.s1dan.tetris.models

enum class Shape1 (val frameCount: Int, val startPosition: Int) {

    // Фигура I горизонтальная и вертикальная
    Tetromino0 (2,2) {
        override fun getFrame (frameNumber: Int):Frame{
            return when (frameNumber) {
                0 -> Frame(4).addRow("1111")
                1 -> Frame(1)
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    },
    // Фигура квадрат
    Tetromino1(1,1) {
        override fun getFrame(frameNumber: Int): Frame {
            return Frame(2)
                .addRow("11")
                .addRow("11")
        }

    },

    // Фигура Z  2-х форматах
    Tetromino2(2,1){
        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber){

                0 -> Frame(3)
                    .addRow("110")
                    .addRow("011")

                1 -> Frame(3)
                    .addRow("01")
                    .addRow("11")
                    .addRow("10")

                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    },

    // Фигура S в 2-х формах
    Tetromino3(2,1){
        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber){

                0 -> Frame(3)
                    .addRow("011")
                    .addRow("110")

                1 -> Frame(2)
                    .addRow("10")
                    .addRow("11")
                    .addRow("01")

                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    },

    // Фигура I горизонтальная и вертикальная
    Tetromino4(2,2){
        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber){

                0 -> Frame(4).addRow("1111")

                1 -> Frame(1)
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")

                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    },

    // Фигура T в 4-х формах
    Tetromino5(4,1){
        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber){

                0 -> Frame(3)
                    .addRow("010")
                    .addRow("111")

                1 -> Frame(2)
                    .addRow("10")
                    .addRow("11")
                    .addRow("10")

                2 -> Frame(3)
                    .addRow("111")
                    .addRow("010")

                3 -> Frame(2)
                    .addRow("01")
                    .addRow("11")
                    .addRow("10")

                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    },

    // Фигура J в 4-х формах
    Tetromino6(4,1){
        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber){

                0 -> Frame(3)
                    .addRow("100")
                    .addRow("111")

                1 -> Frame(2)
                    .addRow("11")
                    .addRow("10")
                    .addRow("10")

                2 -> Frame(3)
                    .addRow("111")
                    .addRow("001")

                3 -> Frame(2)
                    .addRow("01")
                    .addRow("01")
                    .addRow("11")

                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    },

    // Фигура L в 4-х формах

    Tetromino7(4,1){
        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber){

                0 -> Frame(3)
                    .addRow("001")
                    .addRow("111")

                1 -> Frame(2)
                    .addRow("10")
                    .addRow("10")
                    .addRow("11")

                2 -> Frame(3)
                    .addRow("111")
                    .addRow("100")

                3 -> Frame(2)
                    .addRow("11")
                    .addRow("01")
                    .addRow("01")

                else -> throw IllegalArgumentException("$frameNumber is an invalid frame number.")
            }
        }
    };
    abstract fun getFrame(frameNumber: Int):Frame
}


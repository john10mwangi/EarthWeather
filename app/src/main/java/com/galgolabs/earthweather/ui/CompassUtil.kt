package com.galgolabs.earthweather.ui

class CompassUtil {
    private val MAX_DEGREE = 360
    var mDivisor = MAX_DEGREE / CPOINT.values().size

    public fun degreesToCampassDirection(mDegree: Int): CPOINT {
        var cDirection = CPOINT.NORTH
        for (i in 1..360){
            val dir = mDegree / mDivisor
            var dirMod = mDegree % mDivisor

            if (dirMod <= dirMod / 2){
                cDirection = CPOINT.values()[dir % CPOINT.values().size]
            }else {
                cDirection = CPOINT.values()[(dir + 1) % CPOINT.values().size]
            }
        }
        return cDirection
    }
}

enum class CPOINT{
    NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
}
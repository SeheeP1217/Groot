package com.chocobi.groot.view.pot.model

data class PotResponse(
    val msg: String,
    val pot: Pot,
    val plant: Plant
)

data class PotListResponse(
    val msg: String,
    val pots: List<Pot>,
)

data class Pot(
    val potId: Int,
    val plantId: Int,
    val potName: String,
    val imgPath: String,
    val plantKrName: String,
    val dates: Int,
    val createdTime: DateTime,
    val waterDate: DateTime?,
    val nutrientDate: DateTime?,
    val pruningDate: DateTime?,
    val survival: Boolean,
    val level: Int,
    val characterGLBPath: String,
    val characterPNGPath: String,
)

data class DateTime(
    val date: Date,
    val time: Time
)

data class Date(
    val year: Int,
    val month: Int,
    val day: Int
)

data class Time(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nano: Int
)

data class Plant(
    val plantId: Int,
    val krName: String,
    val sciName: String,
    val grwType: String,
    val waterCycle: String,
    val minHumidity: Int,
    val maxHumidity: Int,
    val minGrwTemp: Int,
    val maxGrwTemp: Int,
    val description: String,
    val place: String,
    val mgmtLevel: String,
    val mgmtDemand: String,
    val mgmtTip: String,
    val insectInfo: String,
    val img: String


)
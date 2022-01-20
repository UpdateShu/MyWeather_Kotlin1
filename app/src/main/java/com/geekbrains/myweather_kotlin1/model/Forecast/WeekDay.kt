package com.geekbrains.myweather_kotlin1.model.Forecast

enum class WeekDay(val dayIndex : Int, val dayName : String, val shortName: String) {
    MONDAY(0, "Понедельник", "Пн"),
    TUESDAY(1, "Вторник", "Вт"),
    WEDNESDAY(2, "Среда", "Ср"),
    THURSDAY(3, "Четверг", "Чт"),
    FRIDAY(4, "Пятница", "Пт"),
    SATURDAY(5, "Суббота", "Сб"),
    SUNDAY(6, "Воскресенье", "Вс")
}

package com.hadiubaidillah.shared.plugins

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun convertEpochToReadableTime(epochMillis: Long, zoneId: ZoneId = ZoneId.of("UTC")): String {

    // Convert epoch to Instant
    val instant = Instant.ofEpochMilli(epochMillis)

    // Convert Instant to ZonedDateTime in timezone
    val time = ZonedDateTime.ofInstant(instant, zoneId)

    // Format the result into a readable date-time format (optional)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
    return time.format(formatter)

}

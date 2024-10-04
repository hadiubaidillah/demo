package com.hadiubaidillah.shared.plugins

import java.security.SecureRandom
import java.time.Instant
import java.util.UUID

object UUIDv7 {
    private val random = SecureRandom()

    private fun generateUUIDBytes(): ByteArray {
        // random bytes
        val value = ByteArray(16)
        random.nextBytes(value)

        // current timestamp in ms
        val timestamp = Instant.now().toEpochMilli()

        // timestamp
        value[0] = ((timestamp shr 40) and 0xFF).toByte()
        value[1] = ((timestamp shr 32) and 0xFF).toByte()
        value[2] = ((timestamp shr 24) and 0xFF).toByte()
        value[3] = ((timestamp shr 16) and 0xFF).toByte()
        value[4] = ((timestamp shr 8) and 0xFF).toByte()
        value[5] = (timestamp and 0xFF).toByte()

        // version and variant
        value[6] = (value[6].toInt() and 0x0F or 0x70).toByte()
        value[8] = (value[8].toInt() and 0x3F or 0x80).toByte()

        return value
    }

    fun generate(): UUID {
        val uuidBytes = generateUUIDBytes()

        // Convert first 8 bytes into mostSignificantBits
        val mostSignificantBits = (uuidBytes[0].toLong() shl 56) or
                ((uuidBytes[1].toLong() and 0xFF) shl 48) or
                ((uuidBytes[2].toLong() and 0xFF) shl 40) or
                ((uuidBytes[3].toLong() and 0xFF) shl 32) or
                ((uuidBytes[4].toLong() and 0xFF) shl 24) or
                ((uuidBytes[5].toLong() and 0xFF) shl 16) or
                ((uuidBytes[6].toLong() and 0xFF) shl 8) or
                (uuidBytes[7].toLong() and 0xFF)

        // Convert last 8 bytes into leastSignificantBits
        val leastSignificantBits = (uuidBytes[8].toLong() shl 56) or
                ((uuidBytes[9].toLong() and 0xFF) shl 48) or
                ((uuidBytes[10].toLong() and 0xFF) shl 40) or
                ((uuidBytes[11].toLong() and 0xFF) shl 32) or
                ((uuidBytes[12].toLong() and 0xFF) shl 24) or
                ((uuidBytes[13].toLong() and 0xFF) shl 16) or
                ((uuidBytes[14].toLong() and 0xFF) shl 8) or
                (uuidBytes[15].toLong() and 0xFF)

        // Return new UUID with calculated bits
        return UUID(mostSignificantBits, leastSignificantBits)
    }

}
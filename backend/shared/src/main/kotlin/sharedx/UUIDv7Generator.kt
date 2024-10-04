//package com.hadiubaidillah.shared
//
//import java.util.UUID
//
//import java.nio.ByteBuffer
//import java.util.*
//
//fun UUIDv7GeneratorXXX(): UUID {
//    val timestamp = System.currentTimeMillis()
//    val randomBytes = ByteArray(10)
//    Random().nextBytes(randomBytes)
//
//    val buffer = ByteBuffer.allocate(16)
//    buffer.putLong(timestamp)
//    buffer.put(randomBytes)
//
//    buffer.flip()
//    val high = buffer.long
//    val low = buffer.long
//
//    return UUID(high, low)
//}

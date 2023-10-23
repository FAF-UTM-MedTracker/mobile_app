package com.example.medtracker.data

import java.security.MessageDigest

object HashUtils {
    fun hashString(input: String): String {
        val hexChars = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(hexChars[i shr 4 and 0x0f])
            result.append(hexChars[i and 0x0f])
        }

        return result.toString()
    }
}
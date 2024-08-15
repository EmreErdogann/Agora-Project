package com.agura.task.domain.util

object Validator {

    private const val MIN_VALUE = 6
    private const val MAX_VALUE = 30


    fun isUserNameLength(password: String): Boolean {
        return password.length in MIN_VALUE..MAX_VALUE
    }

    fun hasLetterAndNumber(username: String): Boolean {
        val hasLetter = username.any { it.isLetter() }
        val hasNumber = username.any { it.isDigit() }
        return hasLetter && hasNumber
    }
}
package com.example.kopring.security.common.exception

class InvalidInputException(
   val fieldName: String = "",
   message: String = "Invalid Input"
) : RuntimeException(message)
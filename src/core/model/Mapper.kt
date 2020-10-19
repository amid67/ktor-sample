package com.raywenderlich.core.model

interface Mapper<input, output> {
    fun mapFromInput(input: input): output
    fun mapToInput(output: output): input
}
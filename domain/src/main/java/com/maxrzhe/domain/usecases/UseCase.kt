package com.maxrzhe.domain.usecases

 abstract class UseCase<R> {

     protected abstract suspend fun buildUseCase() : R

     suspend fun execute(): R = buildUseCase()
}
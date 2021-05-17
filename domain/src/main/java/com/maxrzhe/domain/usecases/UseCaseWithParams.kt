package com.maxrzhe.domain.usecases

abstract class UseCaseWithParams<in Params, out R> {

    protected abstract suspend fun buildUseCase(params: Params) : R

    suspend fun execute(params: Params): R = buildUseCase(params)
}
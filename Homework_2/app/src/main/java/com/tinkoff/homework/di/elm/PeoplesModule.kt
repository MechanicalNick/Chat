package com.tinkoff.homework.di.elm

import com.tinkoff.homework.di.scope.PeoplesScope
import com.tinkoff.homework.domain.use_cases.interfaces.GetPeoplesUseCase
import com.tinkoff.homework.elm.BaseStoreFactory
import com.tinkoff.homework.elm.people.PeopleActor
import com.tinkoff.homework.elm.people.PeopleReducer
import com.tinkoff.homework.elm.people.PeopleStoreFactory
import com.tinkoff.homework.elm.people.model.PeopleEffect
import com.tinkoff.homework.elm.people.model.PeopleEvent
import com.tinkoff.homework.elm.people.model.PeopleState
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PeoplesModule {

    @PeoplesScope
    @Provides
    fun providePeopleState(): PeopleState {
        return PeopleState()
    }

    @PeoplesScope
    @Provides
    fun providePeopleReducer(): PeopleReducer {
        return PeopleReducer()
    }

    @PeoplesScope
    @Provides
    fun providePeopleActor(getPeopleUseCase: GetPeoplesUseCase): PeopleActor {
        return PeopleActor(getPeopleUseCase)
    }

    @PeoplesScope
    @Provides
    fun providePeopleStoreFactory(
        peopleState: PeopleState,
        peopleReducer: PeopleReducer,
        peopleActor: PeopleActor
    ): BaseStoreFactory<PeopleEvent, PeopleEffect, PeopleState> {
        return PeopleStoreFactory(peopleState, peopleReducer, peopleActor)
    }
}
package com.tinkoff.homework.elm.people

import com.tinkoff.homework.domain.use_cases.interfaces.GetPeoplesUseCase
import com.tinkoff.homework.elm.people.model.PeopleCommand
import com.tinkoff.homework.elm.people.model.PeopleEvent
import io.reactivex.Observable
import vivid.money.elmslie.rx2.Actor

class PeopleActor(private val peopleUseCase: GetPeoplesUseCase) :
    Actor<PeopleCommand, PeopleEvent> {
    override fun execute(command: PeopleCommand): Observable<PeopleEvent> {
        return when (command) {
            is PeopleCommand.LoadData -> peopleUseCase.execute("")
                .mapEvents(
                    { peopleDto -> PeopleEvent.Internal.DataLoaded(peopleDto) },
                    { error -> PeopleEvent.Internal.ErrorLoading(error) }
                )
            is PeopleCommand.Search -> peopleUseCase.execute(command.query)
                .mapEvents(
                    { peopleDto -> PeopleEvent.Internal.DataLoaded(peopleDto) },
                    { error -> PeopleEvent.Internal.ErrorLoading(error) }
                )
        }
    }
}
package hossein.gheisary.data.remote.core

import io.reactivex.Scheduler

interface Scheduler {
    fun mainThread():Scheduler
    fun io():Scheduler
}
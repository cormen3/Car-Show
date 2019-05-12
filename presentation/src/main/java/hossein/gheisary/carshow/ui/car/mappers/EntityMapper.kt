package hossein.gheisary.carshow.ui.car.mappers

interface EntityMapper<in M, out E> {
    fun map(info: M): E
}
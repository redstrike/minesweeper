class Event(val id: Int, val x: Int, val y: Int, val isLongClick: Boolean) {
    operator fun component1(): Int = y
    operator fun component2() = x
    operator fun component3(): Boolean = isLongClick
    operator fun component4(): Int = id
}

fun isEventLongClicked(events: Array<Event>, eventId: Int): Boolean? {
    for ((_, _, isLongClick, id) in events) {
        if (id == eventId) return isLongClick
    }
    return null
}
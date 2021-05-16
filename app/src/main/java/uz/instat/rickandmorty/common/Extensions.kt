package uz.instat.rickandmorty.common

import uz.instat.rickandmorty.data.model.Info
import uz.instat.rickandmorty.data.model.character.Location
import uz.instat.rickandmorty.data.model.character.Origin


fun <T> lazyFast(
    mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
    initializer: () -> T
): Lazy<T> = lazy(mode, initializer)


val Info.nextKey: Int?
    get() = next?.substringAfterLast('=')?.toInt()

val Info.prevKey: Int?
    get() = prev?.substringAfterLast('=')?.toInt()

val Location.id: Long
    get() = url.substringAfterLast('/').toLong()

val Origin.id: Long
    get() = url.substringAfterLast('/').toLong()

val String.id: Long
    get() = this.substringAfterLast('/').toLong()
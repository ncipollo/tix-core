package org.tix.presentation

/**
 * If we ever have android specific code we could alias this to android's view model on that platform.
 */
typealias ViewModel = CommonViewModel

abstract class CommonViewModel {
    open fun onCleared() {

    }
}
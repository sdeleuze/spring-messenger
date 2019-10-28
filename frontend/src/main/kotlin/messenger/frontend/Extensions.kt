package messenger.frontend

import org.w3c.dom.EventSource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.w3c.dom.Window
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.browser.window

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.js.json

@ExperimentalCoroutinesApi
fun EventSource.asFlow() = callbackFlow {
	onmessage = {
		offer(it.data as String)
	}
	onerror =  {
		cancel(CancellationException("EventSource failed"))
	}
	awaitClose {
		this@asFlow.close()
	}
}


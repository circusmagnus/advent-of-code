import java.util.ArrayDeque
import java.util.Deque
import java.util.LinkedList

fun assembleSleigh2(data: List<String>): String {
    val idRequires = getIdRequiresMap(data)

    tailrec fun getDoneSteps(idRequiresDeque: Deque<Pair<Char, List<Char>>>, doneSet: LinkedHashSet<Char>): Set<Char> {

        if (doneSet.size == idRequires.size) return doneSet
        if (idRequiresDeque.isEmpty()) return getDoneSteps(
            idRequires.toList().sortedBy { it.first }.let { LinkedList(it) },
            doneSet
        )

        val (currentChar, requires) = idRequiresDeque.pop()
        if (requires.isEmpty() || doneSet.containsAll(requires)) return getDoneSteps(idRequires.toList().sortedBy { it.first }.filter {
            doneSet.contains(
                it.first
            ).not()
        }.let { LinkedList(it) }, doneSet.apply { add(currentChar) })
        else return getDoneSteps(idRequiresDeque, doneSet)
    }

    return getDoneSteps(idRequires.toList().sortedBy { it.first }.let { LinkedList(it) }, LinkedHashSet())
        .joinToString(separator = "")
}

fun getIdRequiresMap(data: List<String>) =
    getIdAllowsPairs(data)
        .groupBy { (id, allows) -> allows }
        .mapValues { (key, requires) ->
            requires.map { (key, requires) -> key }
        }.let { idRequiresMap ->
            addCharsWhichDoesNotRequireAnything(idRequiresMap)
        }

private fun addCharsWhichDoesNotRequireAnything(idRequiresMap: Map<Char, List<Char>>) =
    idRequiresMap.values.flatten()
        .let { it.filter { idRequiresMap.keys.contains(it).not() } }
        .map { it to emptyList<Char>() }
        .let { idRequiresMap + it }

fun getIdAllowsPairs(data: List<String>): List<Pair<Char, Char>> = data
    .map { it[5] to it.reversed()[11] }

fun getAdditionalTimeForTask(char: Char) = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    .mapIndexed { index, charKey -> charKey to index + 1 }
    .first { (key, _) -> key == char }
    .let { (_, cost) -> cost }

class Worker(val id: Int, private val baseTime: Int) {

    var workState: WorkState = WorkState.Idle
        private set

    fun doWorkForSecond(): WorkState = workState.let { immutableState ->
        println("Progressing time: $workState in $this")
        when (immutableState) {
            is WorkState.Idle -> immutableState
            is WorkState.Busy -> immutableState.copy(forHowLong = immutableState.forHowLong - 1)
                .takeUnless { it.forHowLong == 0 }
                ?.apply { workState = this }
                ?: WorkState.Done(immutableState.doWhat).apply { workState = this }
            is WorkState.Done -> immutableState
        }
    }

    fun offerWork(task: Char): Boolean = workState.let { immutableState ->
        when (immutableState) {
            is WorkState.Busy -> false
            is WorkState.Idle, is WorkState.Done -> true
                .also { workState = WorkState.Busy(doWhat = task, forHowLong = getAdditionalTimeForTask(task) + baseTime) }
        }.also { println("worker $this accepting task $task") }
    }

    fun reportWork(to: MutableCollection<Char>) = workState.let { immutable ->
        if (immutable is WorkState.Done) {
            to.add(immutable.doneWhat)
            workState = WorkState.Idle
        }
    }
}

sealed class WorkState {
    object Idle : WorkState()
    data class Busy(val doWhat: Char, val forHowLong: Int) : WorkState()
    data class Done(val doneWhat: Char) : WorkState()
}

tailrec fun waitUntilSomeBodyIsFree(workers: List<Worker>): List<Worker> =
    workers.filter { it.workState is WorkState.Done || it.workState is WorkState.Idle }.takeIf { it.isNotEmpty() }
        ?: waitUntilSomeBodyIsFree(workers.apply { forEach { it.doWorkForSecond() } })

fun assembleSleigh3(data: List<String>, baseWorkTime: Int, workersCount: Int): Int {
    val idRequires = getIdRequiresMap(data)
    val workers: List<Worker> = generateSequence(Worker(id = 1, baseTime = baseWorkTime)) {
        Worker(id = it.id + 1, baseTime = baseWorkTime)
    }.take(workersCount).toList()
    val doneSet = mutableSetOf<Char>()

    tailrec fun getNextDoableSteps(idRequiresDeque: Deque<Pair<Char, List<Char>>>, doableSteps: List<Char>): List<Char> {
        if (idRequiresDeque.isEmpty()) return doableSteps
        val (currentChar, requires) = idRequiresDeque.pop()

        return if (requires.none { doneSet.contains(it).not() }) getNextDoableSteps(idRequiresDeque, doableSteps + currentChar)
        else getNextDoableSteps(idRequiresDeque, doableSteps)
    }

    fun getIdleWorkers(): List<Worker> = workers
        .filter { it.workState is WorkState.Idle || it.workState is WorkState.Done }

    tailrec fun passTimeUntilSomethingDone(busyWorkers: List<Worker>, timePassed: Int): Int {
        val isSomethingDone = busyWorkers.any { it.workState is WorkState.Done }

        return if (isSomethingDone) timePassed
        else {
            busyWorkers.forEach { it.doWorkForSecond() }
            passTimeUntilSomethingDone(busyWorkers, timePassed + 1)
        }
    }

    tailrec fun assignWork(availableWorkers: Deque<Worker>, jobs: Deque<Char>){
        if (availableWorkers.isEmpty() || jobs.isEmpty()) return
        else {
            val worker = availableWorkers.pop()
            val char = jobs.pop()
            worker.offerWork(char)
            return assignWork(availableWorkers, jobs)
        }
    }

    tailrec fun beTheBoss(timePassedInTotal: Int): Int {

        val availableWorkers = getIdleWorkers().apply {
            filter { it.workState is WorkState.Done }.forEach { it.reportWork(doneSet) }
        }

        if (doneSet.size == idRequires.size) return timePassedInTotal

        val workInProgress = workers
            .map { it.workState }
            .filter { it is WorkState.Busy }
            .map { (it as WorkState.Busy).doWhat }

        val jobsForTaking = ArrayDeque(idRequires.toList()
            .sortedBy { it.first }
            .filter { (id, requires) -> workInProgress.contains(id).not() }
            .filter { (id, requires) -> doneSet.contains(id).not() }
        )

        val doableJobs = getNextDoableSteps(jobsForTaking, emptyList())

        assignWork(ArrayDeque(availableWorkers), ArrayDeque(doableJobs))

        return beTheBoss(timePassedInTotal + passTimeUntilSomethingDone(workers, 0))
    }

    return beTheBoss(0)
}
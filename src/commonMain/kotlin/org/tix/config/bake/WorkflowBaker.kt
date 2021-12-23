package org.tix.config.bake

import org.tix.config.data.Action
import org.tix.config.data.TicketWorkflows
import org.tix.config.data.Workflow
import org.tix.config.data.raw.RawAction
import org.tix.config.data.raw.RawTicketWorkflows
import org.tix.config.data.raw.RawWorkflow

internal fun RawTicketWorkflows?.bake() = this?.let {
    TicketWorkflows(
        afterAll = afterAll.bake(),
        afterEach = afterEach.bake(),
        beforeAll = beforeAll.bake(),
        beforeEach = beforeEach.bake()
    )
} ?: TicketWorkflows()

private fun List<RawWorkflow>.bake() = map { it.bake() }

private fun RawWorkflow.bake() = Workflow(label = label, actions = actions.bakeActions())

// Name needs to be different from List<RawWorkflow>.bake() for some reason, was causing a JVM conflict
private fun List<RawAction>.bakeActions() = map { it.bake() }

private fun RawAction.bake() = Action(type = type, label = label, arguments = arguments.asMap())
package org.tix.feature.plan.domain.ticket.jira

import org.tix.integrations.jira.field.Field

fun Field.useValueKey() = schema.type == "option"
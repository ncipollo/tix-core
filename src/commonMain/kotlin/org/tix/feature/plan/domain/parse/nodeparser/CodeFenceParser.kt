package org.tix.feature.plan.domain.parse.nodeparser

import kotlinx.serialization.SerializationException
import org.tix.feature.plan.domain.parse.configparser.ConfigLanguage
import org.tix.feature.plan.domain.parse.configparser.ConfigLanguageDetector
import org.tix.feature.plan.domain.parse.fieldparser.FieldParser
import org.tix.feature.plan.domain.parse.parseError

internal class CodeFenceParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val segment = CodeFenceSegmentCreator.createCodeSegment(arguments.currentNode, arguments.markdownText)

        val fieldLanguage = ConfigLanguageDetector.detect(segment)
        if (fieldLanguage == ConfigLanguage.NO_CONFIG) {
            arguments.state.addBodySegments(segment)
        } else {
            try {
                val fields = FieldParser.parse(segment.code, fieldLanguage)
                arguments.state.addFields(fields)
            } catch (e: SerializationException) {
                parseError("Failed to parse tix fields.\nSerialization Error: $e", arguments)
            }
        }

        return arguments.resultsFromArgs()
    }
}
package org.tix.fixture.integrations

import io.ktor.client.engine.mock.*
import org.tix.config.data.GithubConfiguration
import org.tix.config.data.GithubFieldConfiguration
import org.tix.config.data.TicketWorkflows
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.config.reader.auth.EnvAuthSourceReader
import org.tix.integrations.github.GithubApi
import org.tix.platform.PlatformEnv
import org.tix.ticket.system.TicketSystemType

fun mockGithubApi(mockEngine: MockEngine, owner: String = "ncipollo", repo: String = "tix-core") =
    GithubApi(githubConfig(owner, repo), engine = mockEngine)

fun githubApi(owner: String = "ncipollo", repo: String = "tix-core") =
    GithubApi(githubConfig(owner, repo))

fun githubConfig(owner: String = "ncipollo", repo: String = "tix-core") =
    GithubConfiguration(
        auth = githubAuth(),
        owner = owner,
        repo = repo,
        noProjects = false,
        fields = GithubFieldConfiguration(),
        workflows = TicketWorkflows()
    )

private fun githubAuth() =
    EnvAuthSourceReader(PlatformEnv)
        .read("", RawAuthConfiguration(), TicketSystemType.GITHUB)
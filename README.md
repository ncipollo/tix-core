Tix is a tool for creating and managing tickets across a variety ticketing platforms.

Tix supports the following ticketing platforms:

- Jira
- GitHub Projects

# Why Tix? 🤔

Tix exists to help you plan your projects without losing your mind 🙂. Without tix a typical project planning work flow
might look something like this:

1. Come up with the idea.
2. Write out a design document.
3. Open your ticketing platform of your choice (or more realistically, your company's choice).
4. Jump around between tabs, wait for the web UI to load, and input your tickets one-by-one.

Tix lets you do all your planning offline, in a single document. Tix can then take this document and mass produce
tickets for you.

# Installation 💾

## macOS

```bash
brew tap ncipollo/tools
brew install kotlin-tix
```

# Planning 🔮

At a high level tix planning requires three inputs:

1. A configuration which informs tix how and where it should create tickets for you.
2. Authentication information for your target ticketing platforms.
3. A markdown file which contains your tickets.

Running tix from the command line will typically look something like this:

```bash
tix my_tickets.md
```

## Configuration 📜

### Config Files

Tix configuration may be specified in either json or yaml. Tix will look for configuration in the following locations:

- The workspace configuration located in the same directory as the markdown file. The configuration file is expected to
  be named `tix.yml` or `tix.json`
- Via an included configuration from `~/.tix/configs` (more on this later)
- In `~/.tix/config.yml` or `~/.tix/config.json`

If multiple configurations from the above list are found, they are merged in the order defined above. If there are
property conflicts the higher configuration will take precedence (i.e. the config in the same directory as the markdown
file overrides all others).

If the configuration file is in the same directory as the markdown file(s), your directory structure might look
something like this:

```
- tickets1.md
- tickets2.md
- tix.yml
```

### Config Format

The following is the expected format of the settings file (in yaml):

```yaml
include: my_saved_config # Optional saved configuration which will be overlaid on top of this configuration.
github:
  no_projects: true # Indicates if tix should use projects or treat root tickets as issues. Defaults to false.
  owner: owner # The owner of the GitHub repo (ex - ncipollo)
  repo: repo  # The GitHub repo (ex - tix-core)
  fields:
    default:
      default: default # Fields to be added to both projects and issues
    project:
      project: project # Fields to be added to projects
    issue:
      labels: [ label1, label2 ] # Fields to be added to issues
jira:
  no_epics: false # Indicates if tix should use epics or treat root tickets as stories / issues. Defaults to false. 
  url: # https://url.to.your.jira.instance.com
  fields:
    # All fields should be lowercase. Field name spaces should be included (ex- epic name)
    default:
      field: value # Fields to be added to any kind of jira issue
    epic:
      field: value # Fields to be added to epics
    issue:
      field: value # Fields to be added to issues
    task:
      field: value # Fields to be added to tasks (sub-issues of issues)
matrix:
  # Each entry here defines a matrix.
  Mobile: [ Android, iOS ]
variables:
  key: value
  envKey: $ENVIRONMENT_VARIABLE
  # tix will parse the Markdown document and replace each occurrence of "key" with its value (or environment variable
  # when a '$' precedes the value).  
```

#### Included Configuration

The `include` property allows you to reference a previously saved configuration in `~/.tix/configs`. This allows you to
save your commonly used configurations in a single location and reuse them.

The value of the include property should be the name of a saved configuration, minus the extension. In other words,
if `include` is set to `my_config`, tix will look for either `~/.tix/configs/my_config.json`
or `~/.tix/configs/my_config.yml`.

Note that it is completely valid for your workspace configuration to have just a single `include` entry in it.

#### Jira

You can use this section to define fields which should be included in all generated tickets (or all tickets of a certain
type).

Fields can be referenced by field name, or field id. It is possible to have multiple fields with the same name in Jira,
in which case you need to use the field ID to select the correct one. You can find a list of Jira fields by running the
following command:

```bash
tix info fields --include=my_saved_config # alternatively you can run this in a folder with your tix.yml config.
```

Here are some of the more common Jira fields:

- `affects_versions`: Jira's affects versions field.
- `components`: Jira's components field.
- `delete_ticket`: The Jira ticket to delete. When this is present the rest of the ticket specification is ignored.
  Should be the ticket key (ex- `TIX-123`)
- `fix_versions`: Jira's fix versions field.
- `labels`: Jira labels to apply to the ticket.
- `parent`: An explicit parent to attach the ticket to.
- `priority`: The Jira ticket priority.
- `type`: The type of the ticket. Tix will default these values to the most common for each level, but many projects may
  have custom types.
- `update_ticket`: The Jira ticket to update. When present, tix will update an existing ticket rather than creating a
  new one. Should be the ticket key (ex- `TIX-123`)

#### Github

You can use this section to define fields which should be included in all generated tickets (or all tickets of a certain
type). Tix supports the following GitHub fields:

- `assignees`: Assignees to attach to the ticket.
- `delete_ticket`: The GitHub ticket to delete. When this is present the rest of the ticket specification is ignored.
  Should be a GitHub number (ex - `#42`).
- `milestone`: A GitHub milestone to apply to the ticket. Will create a milestone if one doesn't already exist.
- `labels`: The GitHub labels to apply to the ticket.
- `parent`: An explicit parent to attach the ticket to. Should be a GitHub number (ex - `#42`).
- `update_ticket`: The GitHub ticket number to update. When present, tix will update an existing ticket rather than
  creating a new one. Should be a GitHub number (ex - `#42`).

#### Variables

Variables defined in this section may be referenced in your Markdown document. Within the Markdown document, all
variables must be preceded by a `$`.

Variables which have a value preceded by a `$` are pulled in from the environment. Please note that some environment
variables are filtered out for security reasons (for example all known ticket system auth tokens).

Note: Tix automatically creates some variables for you. For example:

- `ticket.parent.id`: The ID of the parent ticket (if one exists)
- `ticket.parent.key`: The key of the parent ticket (if one exists). For jira this would be the human facing ticket
  name (ex- `TIX-123`)

#### Matrix

You can use this section to define matrix expansions for tickets. Matrix expansion works as follows:

- In the tix configuration you define one or more matrices.
- The name of the matrix will be the key used to define it.
- Each matrix has a list of values associated with it.
- If a ticket has the matrix name in its title as a variable, we will duplicate that ticket N times (where N is the
  number of values associated with the matrix).
- For each duplicated ticket, we will add the matrix name as a variable, so it can be used in multiple places in the
  ticket (title, body, fields, etc).

For example, assuming we have the following matrix:
```yaml
matrix:
  Mobile: [ Android, iOS ]
```

And the following markdown:
```markdown
# $Mobile Ticket 1

# $Mobile Ticket 2

# Some other ticket
```

We would end up with the following tickets:
- Android Ticket 1
- iOS Ticket 1
- Android Ticket 2
- iOS Ticket 2
- Some other ticket

## Authentication 🎫

Ticket system authentication is typically configured via environment variables. The following section describe how to
authenticate with the supporting ticketing systems.

### Jira

To use Jira as a ticketing system you will need a personal access token. You can obtain one by following the
instructions here: [Jira Api tokens](https://confluence.atlassian.com/cloud/api-tokens-938839638.html)

Once you have the access token, add the following two properties to your environment:

- `JIRA_USERNAME`: This should be set to your Jira username (typically an email address).
- `JIRA_API_TOKEN`: This should be set to your Jira api key. This may be generated by following the instructions found
    - Note you can use `JIRA_PASSWORD` here instead (tix looks in both variables for the API key).

### Github

To use GitHub as a ticketing system you will need a personal access token. You can obtain one by following the
instructions
here: [GitHub API Tokens](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens)

The token you create above will need to have access to the repositories you plan to create issues and projects in.

Once you have the access token, add the following two property to your environment:

- `GITHUB_API_TOKEN`: This should be set to your GitHub access token.
    - Note you can use `GITHUB_PASSWORD` here instead (tix looks in both variables for the API key).

## Markdown ✍️

Tickets are authored in a markdown file. Tix interprets the content of `heading` elements as a tickets. The indent level
of the heading element will indicate the
level of nesting for the ticket. For example:

```
# Root Ticket

This will be a root level ticket.

## Child Ticket

This ticket will be a child of root ticket.

### Grandchild Ticket

This ticket will be a child of child ticket.

``` 

The parent-child relationship for tickets will translate into specific relationships for ticketing systems. In Jira,
for example, the root ticket could be an epic, the next level down a story, then the final level would be a task.

### Markdown Elements

Tix parses each markdown element into an abstract representation of that element. Tix will then utilize these
abstractions to generate representations which are specific to a ticketing system. For example:

- List type elements will would utilize the wiki-media style indentation markers for jira (`--`)
- Code blocks in jira will use the `{code}` markers.

### Special Blocks

Tix supports special code blocks which may be used to add or override fields for a ticket. For example:

    ```tix
    # Adds fields to the ticket, regardless of ticket system
    field: value
    ```

    ```tix_json
    {
      field: value
    }
    ```
    
    ```tix_yml
    # Adds fields to the ticket, regardless of ticket system
    field: value
    ```

### Ticket Levels

#### Jira

Jira tickets have the following relationship with heading indent levels:

- `#`: Epic, if epics are allowed via settings. Story otherwise.
- `##`: Story / issue
- `###`: Task

#### Github

GitHub tickets have the following relationship with heading indent levels:

- `#`: Project, if projects are allowed via settings. Issue otherwise.
- `##`: Issue

# Quick Tickets 🎟️💨

If you need to quickly create a ticket and fill the details in later you can use the `quick` command. For example:

```bash
tix quick --include=my_config_name "My Ticket Title"
```

This will create a ticket with the provided title, using the provided configuration. In the example above the title
would be `My Ticket Title`.

# CLI Tricks ⌨️

This section contains tips and tricks for using the tix cli.

## Dry Run

If you provide the `-d` option to tix it will perform a dry run of the ticket creation process. You can use this to spot
check your tickets before mass-producing them.

Example:

```bash
tix -d my_tickets.md
```

## Saved Configuration

Most tix commands allow you to specify a previously saved configuration to use via the `--include` option. This behaves
similarly to the `include` option in the configuration files. It will be overlaid on top of the root configuration, then
the workspace configuration will be overlaid on top of that.

Examples:

```bash
tix --include=my_config_name my_file.md
tix quick --include=my_config_name "My Ticket Title"
```

# Project Setup 💻

The tix project is contained in a few different repositories. At a high level there is a single core repository which
contains the bulk of the tix logic, then there are front end repositories which provide UX and tooling. The following
is a list of the tix project repositories:

- [tix-core](https://github.com/ncipollo/tix-core): This repo contains the bulk of the logic for tix. Front end repos
  depend upon it
- [tix-cli](https://github.com/ncipollo/tix-cli): The command line interface for tix.

# Go Version 🏛️

The original go version of tix may be found here: https://github.com/ncipollo/tix

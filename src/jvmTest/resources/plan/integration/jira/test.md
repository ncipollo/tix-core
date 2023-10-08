This should be ignored.

```tix
jira:
    no_epics: false
    url: 'https://tix-test.atlassian.net'
    tickets: 
        default:
            project: TIX
            labels: $label
    workflows:
        after_each:
            - label: cleanup
              actions:
                  - type: delete_issue
                    arguments:
                        issue: $ticket.jira.key
```

# Test Epic 

This should be an epic.

should be empty - ($jira_name $jira_token)

## $story_name

- Testing
- one 
- two 
- three

$ticket.parent.id

### $mobile: Test Task

This should be a task